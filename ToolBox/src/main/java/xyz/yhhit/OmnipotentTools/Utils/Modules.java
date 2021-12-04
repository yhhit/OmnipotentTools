package xyz.yhhit.OmnipotentTools.Utils;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;
import xyz.yhhit.OmnipotentTools.ItfUI;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static xyz.yhhit.OmnipotentTools.DataSheet.CSht.FILE_CHARSET;
import static xyz.yhhit.OmnipotentTools.DataSheet.CSht.META_DATA_FILE_NAME;
import static xyz.yhhit.OmnipotentTools.DataSheet.VaSht.*;
@SuppressWarnings("unused")
public class Modules {

    public static Object callExtFunction(String className,String methodName,Object[] args )
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {
        Class<?> classClass = Class.forName(className);
        Object classObject = classClass.getConstructor().newInstance();
        return callExtFunction(classClass,classObject,methodName,args);
    }
    public static Object callExtFunction(Class<?> classClass,Object classObject,
                                         String functionName,Object[] args )
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Class<?>[] argClasses=new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i]=args[i].getClass();
        }
        Method m= classClass.getMethod(functionName,argClasses);
        return m.invoke(classObject,args);
    }

    public static String[] getModuleNotInstallDepName(String modName) throws IOException {
        String[] moduleDep = getModuleDepModules(modName);
        ArrayList<String> notInstall=new ArrayList<>();
        Collections.addAll(notInstall, moduleDep);
        notInstall.removeAll(MODULES_NAME);
        return notInstall.toArray(new String[0]);
    }
    public static String[] getModuleDepModulesByJar(String path) throws IOException {
        path=path.trim();
        return getModuleDepModules(getJarMetaDataInStream(getJarZipInputStream(path)),true);
    }
    public static void cleanAllDependencies() {
        File fileRootPath=new File(".");
        File[] files=fileRootPath.listFiles((dir, name) -> !"xyz".equals(name) && !"log".equals(name) && !"jdk".equals(name));
        assert files != null;
        for (File f:files
        ) {
            if(f.isDirectory())
                RemoveDir.remove(f);
        }
        scanModules();
    }
    public static void removeModule(int modOrd) throws Exception {
        String modName= MODULES_NAME.get(modOrd);
        if(modName==null||"".equals(modName))
            throw new Exception("模块名错误");
        File modFile=new File(getModulePath(modName));
        RemoveDir.remove(modFile);
        scanModules();
    }
    //return:name to show
    public static String installModule(String path) throws Exception {
        path=path.trim();
        File oriJar=new File(path);
        File tarPath;
        File tarJar;
        var modInfo=getModuleInfoByJar(path);
        switch (modInfo.getRight()){
            case MODULE:
                //安装规范模块
                String basePath=VaSht.PATH+File.separator;
                String[] ignoreFile={basePath+"DataSheet"+File.separator,
                        basePath+"UIS"+File.separator,basePath+"Utils"+File.separator,
                        basePath+"ItfUI.class","META-INF"+File.separator,META_DATA_FILE_NAME};
                ZipUtils.unzip(path,".",ignoreFile);
                scanModules();
                return getModulesShowName(modInfo.getLeft());
            case JAR:
                //安装规范Jar
                tarPath=new File(MODULES_PATH+File.separator+modInfo.getLeft());
                OLogger.logDebug(tarPath.getParentFile().mkdir()?"true":"false",null);
                OLogger.logDebug(tarPath.mkdir()?"true":"false",null);
                tarJar=new File(tarPath+File.separator+oriJar.getName());
                copyFile(new FileInputStream(oriJar),new FileOutputStream(tarJar),true);
                copyFile(getJarMetaDataInStream(getJarZipInputStream(path)),
                        new FileOutputStream(tarJar.getParentFile()+
                                File.separator+META_DATA_FILE_NAME),true);
                scanModules();
                return modInfo.getLeft();
            case UNKNOWN_JAR:
                //安装不规范Jar
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                tarPath=new File(EXT_MODULES_PATH+File.separator+uuid);
                try {
                    tarPath.getParentFile().mkdir();
                    tarPath.mkdir();

                    tarJar=new File(tarPath+File.separator+oriJar.getName());
                    copyFile(new FileInputStream(oriJar),new FileOutputStream(tarJar),true);
                    scanModules();
                    String showName=VaSht.UI.setModuleShowName(uuid);
                    OutputStream moduleMetaDataOutStream = getModuleMetaDataOutStream(uuid);
                    Properties metaData=new Properties();
                    metaData.setProperty("ShowName",showName);
                    propertyStore(metaData, moduleMetaDataOutStream,"");
                    moduleMetaDataOutStream.close();
                    scanModules();
                    return showName;
                }catch (Throwable t){
                    RemoveDir.remove(tarPath);
                    scanModules();
                    throw t;
                }
            default:return null;
        }
    }
    static OutputStreamWriter getWriter(OutputStream outputStream) throws UnsupportedEncodingException {
        return new OutputStreamWriter(outputStream,FILE_CHARSET);
    }
    public static void setModuleShowName(String modName,String showName) throws IOException {
        Properties moduleProperties = getModuleProperties(modName);
        OutputStream moduleMetaDataOutStream = getModuleMetaDataOutStream(modName);
        propertyStore(moduleProperties, moduleMetaDataOutStream,"");
        moduleMetaDataOutStream.close();
    }

    //Triplet:modName,metaData,moduleType
    public static Triplet<String,Properties, CSht.MODULE_TYPE> getModuleInfoByJar(String path) throws IOException {
        path=path.trim();
        String modName;
        Properties metaData;
        ZipInputStream zipInputStream=getJarZipInputStream(path);
        ZipEntry zipEntry=zipInputStream.getNextEntry();
        while (zipEntry!=null){
            String pathName=zipEntry.getName();
            if(pathName.matches(getRexToMatchJarMetaDataFile())){
                metaData=getModuleProperties(zipInputStream,true);
                modName=getModuleName(metaData);
                return new Triplet<>(modName,metaData,CSht.MODULE_TYPE.JAR);
            }
            if(pathName.matches(getRexToMatchModuleMetaDataFile())){
                metaData=getModuleProperties(zipInputStream,true);
                String[] pathSplit = pathName.split(StringUtil.slantToBackSlant(File.separator));
                modName = pathSplit[pathSplit.length - 2];
                return new Triplet<>(modName,metaData,CSht.MODULE_TYPE.MODULE);
            }
            zipInputStream.closeEntry();
            zipEntry=zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        return new Triplet<>(null,null,CSht.MODULE_TYPE.UNKNOWN_JAR);
    }
    public static String getModuleDftUI(String modName){
        return getModuleUIS(modName).get(0);
    }
    //移除xxx.class后面的.class
    public static ArrayList<String> getModuleUIS(String modName){
        File moduleUISPathFile = getModuleUISPathFile(modName);
        ArrayList<String> uis=new ArrayList<>();
        if(moduleUISPathFile.isDirectory()){
            File[] files=moduleUISPathFile.listFiles();
            assert files != null;
            for(File f:files){
                if(f.isFile()){
                    uis.add(f.getName().replaceAll("\\.class$", ""));
                }
            }
        }
        return uis;
    }
    public static String getModulesShowName(String modName){
        CSht.MODULE_TYPE modType=MODULES.get(modName);
        try{
            if(modType== CSht.MODULE_TYPE.JAR||modType==CSht.MODULE_TYPE.UNKNOWN_JAR){
                String moduleShowName = getModuleShowName(getModuleProperties(modName));
                return moduleShowName ==null?"未知模块":moduleShowName;
            }else if(modType== CSht.MODULE_TYPE.MODULE){
                return (String)callModuleInitFn(modName,getModuleDftUI(modName),"getLocalName");
            }else
                return modName;
        }catch (Exception |Error e){
            OLogger.logDebug("模块文件不完整，请重新安装模块！", e);
        }
        return modName;
    }
    public static void startModules(String modName,String uiName,String args) throws Exception {
        switch (MODULES.get(modName)){
            case MODULE:startModule(modName,uiName,args);break;
            case JAR:
            case UNKNOWN_JAR:
                startJar(modName,args);break;
            default:throw new Exception("模块注册信息错误！");
        }
    }
    public static String[] getModuleDepModules(String modName) throws IOException {
        return getModuleDepModules(getModuleMetaDataInStream(modName),true);
    }

    public static String getModuleVersion(String modName) throws IOException {
        return getModuleVersion(getModuleProperties(modName));
    }

    public static String getModuleName(Properties metaDataPro){
        String value=metaDataPro.getProperty("Name","");
        return value.equals("")?null:value;
    }
    public static String getModuleVersion(Properties metaDataPro){
        String value=metaDataPro.getProperty("Version","");
        return value.equals("")?null:value;
    }
    public static String getModuleShowName(Properties metaDataPro){
        String value=metaDataPro.getProperty("ShowName","");
        return value.equals("")?null:value;
    }
    static String getJarModuleUIType(Properties metaDataPro){
        String value=metaDataPro.getProperty("UIType","");
        return value.equals("")?null:value;
    }
    static String getJarModuleUIType(String modName) throws IOException {
        switch (getModuleType(modName)){
            case JAR:;
            case UNKNOWN_JAR:
            default:return getJarModuleUIType(getModuleProperties(modName));
        }
    }
    public static CSht.MODULE_TYPE getModuleType(String modName){
        return MODULES.get(modName);
    }
    public static String getModuleDependencyModules(Properties metaDataPro){
        String value=metaDataPro.getProperty("DependencyModules","");
        return value.equals("")?null:value;
    }
    public static String getModuleOnlyRunnablePlatform(Properties metaDataPro){
        String value=metaDataPro.getProperty("OnlyRunnablePlatform","");
        return value.equals("")?null:value;
    }
    public static String NotRunnablePlatform(Properties metaDataPro){
        String value=metaDataPro.getProperty("NotRunnablePlatform","");
        return value.equals("")?null:value;
    }
    //扫描所有模块
    static void clearModules(){
        VaSht.MODULES.clear();
        MODULES_NAME.clear();
    }
    public static void scanModules(){
        clearModules();
        scanAddModules();
        scanAddExtModules();
    }
    static OutputStream getModuleMetaDataOutStream(String modName) throws FileNotFoundException {
        return new FileOutputStream(getModuleMetaDataFile(modName));
    }
    static File getModuleMetaDataFile(String modName){
        return new File(getModulePath(modName)+File.separator+META_DATA_FILE_NAME);
    }
    static InputStream getModuleMetaDataInStream(String modName) throws FileNotFoundException {
        return new FileInputStream(getModuleMetaDataFile(modName));
    }
    static String getModuleMetaDataPath(String modName){
        return getModulePath(modName) + File.separator + META_DATA_FILE_NAME;
    }
    static Properties getModuleProperties(String modName) throws IOException {
        return getModuleProperties(getModuleMetaDataInStream(modName),true);
    }
    static String getModulePath(String modName){
        if(VaSht.MODULES.get(modName).equals(CSht.MODULE_TYPE.MODULE)||VaSht.MODULES.get(modName).equals(CSht.MODULE_TYPE.JAR))
            return VaSht.MODULES_PATH+File.separator+modName;
        else
            return EXT_MODULES_PATH+File.separator+modName;
    }
    //将参数按照空格分割
    static String[] splitArg(String str){
        return str.split("[\\s]+");
    }
    static String[] getModuleDepModules(InputStream proInStream,boolean closeStream) throws IOException {
        Properties metaData=getModuleProperties(proInStream,closeStream);
        return getModuleDepModules(metaData);
    }
    static String[] getModuleDepModules(Properties metaData){
        String dependencyModules = metaData.getProperty("DependencyModules","").trim();
        String[] depMods=new String[0];
        if(!dependencyModules.equals(""))
            depMods=dependencyModules.split("\\s+");
        return depMods;
    }
    static Class<?> getModuleClassByName(String modName,String uiName) throws ClassNotFoundException {
        return  Class.forName(PACKAGE_NAME +".Modules." + modName + "." + "UIS." +uiName);
    }
    static InputStreamReader getReader(InputStream inputStream) throws UnsupportedEncodingException {
        return new InputStreamReader(inputStream,FILE_CHARSET);
    }
    static Properties getModuleProperties(InputStream proInStream,boolean closeStream) throws IOException {
        Properties metaDataPro=new Properties();
        propertiesLoad(metaDataPro,proInStream);
        if (closeStream)
            proInStream.close();
        return metaDataPro;
    }
    static Object callModuleInitFn(String modName,String uiName,String funNames) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = getModuleClassByName(modName,uiName);
        Object o = aClass.getConstructor().newInstance();
        return callModuleInitFun(funNames,aClass,o);
    }
    static void callModuleInitFns(String modName,String uiName, String... funNames) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> classClass = getModuleClassByName(modName,uiName);
        Object classObject = classClass.getConstructor().newInstance();
        for (String f:funNames) {
            try{
                if((int)callModuleInitFun(f,classClass,classObject)!=0){
                    moduleCrash(modName);
                }
            }catch (Throwable t){
                OLogger.logDebug("",t);
                moduleCrash(modName);
            }

        }
    }
    //TODO:
    static void moduleCrash(String mod){
        OLogger.logDebug("模块异常停止",null);
    }
    static Object callModuleInitFun(String functionName,Class<?> classClass,Object classObject) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] args;
        Method m;
        if(functionName.matches("^onShowUI")){
            args=splitArg(functionName);
            return callExtFunction(classClass,classObject,functionName,new Object[]{args});
        }else{
            return callExtFunction(classClass,classObject,functionName,new Object[]{});
        }
    }
    static String getModuleRootPath(){
        return VaSht.MODULES_PATH;
    }
    static File getModuleRootPathFile(){
        return new File(getModuleRootPath());
    }
    static String getModulePathMetaDataPath(String modName){
        return MODULES_PATH+File.separator+modName+File.separator+META_DATA_FILE_NAME;
    }
    static File getModulePathMetaDataFile(String modName){
        return new File(getModulePathMetaDataPath(modName));
    }
    static String getExtModulePathMetaDataPath(){
        return VaSht.EXT_MODULES_PATH;
    }
    static File getExtModulePathMetaDataFile(){
        return new File(getExtModulePathMetaDataPath());
    }
    static void scanAddModules(){
        scanPath(getModuleRootPathFile(),(ModPackage)->{
            if(ModPackage.isDirectory()){
                if(getModulePathUISFile(ModPackage.getName()).exists()){
                    //规范模块
                    MODULES.put(ModPackage.getName(), CSht.MODULE_TYPE.MODULE);
                }else{
                    //规范Jar
                    MODULES.put(ModPackage.getName(), CSht.MODULE_TYPE.JAR);

                }
                MODULES_NAME.add(ModPackage.getName());
            }
        });
    }
    static File getModulePathUISFile(String modName){
        return new File(VaSht.MODULES_PATH +File.separator+modName+File.separator+"UIS");
    }
    static void scanAddExtModules() {
        //扫描.../ExtModules/目录
        scanPath(getExtModulePathMetaDataFile(),(ModPackage)->{
            if(ModPackage.isDirectory()){
                //不规范Jar
                VaSht.MODULES.put(ModPackage.getName(),CSht.MODULE_TYPE.UNKNOWN_JAR);
                MODULES_NAME.add(ModPackage.getName());
            }
        });
    }
    static void scanPath(File Path,Consumer<File> addExtModule){
        if(Path.isDirectory()){
            File[] files=Path.listFiles();
            //扫描.../Modules/路径
            assert files!=null;
            Arrays.stream(files).forEach(addExtModule);
        }
    }
    static void startModule(String modName,String uiName,String arg) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if(ItfUI.UI_TYPE_STR[ItfUI.UI_TYPE.CONSOLE_UI.ordinal()].equals(uiName)){
            startModulesInit(modName,uiName,arg);
        }else{
            VaSht.THREAD_POOL.submit(()-> {
                try {
                    startModulesInit(modName,uiName,arg);
                } catch (Throwable t) {
                    OLogger.logInfo("模块文件可能损坏或版本过低，请重新安装最新版本！",t);
                }
            });
        }
    }
    static void startJar(String modName,String args) throws Exception {
        String arg="java -jar";
        String moduleUIType=null;
        try{
            moduleUIType = getJarModuleUIType(modName);
        }catch (Throwable t){
            OLogger.logDebug("meta.properties不存在",t);
        }
        String modulePath = getModulePath(modName);
        String jarName = scanJarForName(modulePath);
        if(jarName==null)
            throw new Exception("jar not found");
        String cmd=arg+" "+modulePath+File.separator+jarName+" "+(args==null?"":args);
        Process process=Runtime.getRuntime().exec(cmd);
        switch (moduleUIType==null? "ConsoleUI":moduleUIType){
            case "GraphicUI":;
            case "ConsoleUI":
            default:
                //没有找到可以调用控制台并且完美进行输入输出流传输的方法
                processClearStream(process.getInputStream());
                processClearStream(process.getErrorStream());

        }
    }

    static void processClearStream(InputStream inputStream){
        new Thread(()->{
                try {
                    while (inputStream.read()!=-1) ;

                } catch (IOException e) {
                    OLogger.logDebug("",e);
                }
        }).run();
    }
    static String scanJarForName(String path){
        File file=new File(path);
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File f:files) {
                if(f.getName().matches(".*\\.jar$")){
                    return f.getName();
                }
            }

        }
        return null;
    }
    static void startModulesInit(String modName,String uiName,String arg) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        callModuleInitFns(modName,uiName,"onCreate","onShowUI"+((arg==null)?"":" "+arg),"onExit");
    }
    static String getModuleUISPath(String modName){
        return VaSht.MODULES_PATH+File.separator+modName+File.separator+"UIS";
    }
    static File getModuleUISPathFile(String modName){
        return new File(getModuleUISPath(modName));
    }
    static ZipInputStream getJarZipInputStream(String path) throws FileNotFoundException {
        return new ZipInputStream(new FileInputStream(path));
    }
    static InputStream getJarMetaDataInStream(ZipInputStream zipInputStream) throws IOException {
        ZipEntry entry;
        while ((entry = zipInputStream.getNextEntry())!=null){
            if (entry.getName().matches(getRexToMatchAllMetaDataFile())){
                return zipInputStream;
            }
        }
        return null;
    }
    static String getRexToMatchModuleMetaDataFile(){
        return StringUtil.slantToBackSlant(VaSht.MODULES_PATH+File.separator+".+"+META_DATA_FILE_NAME+"$");
    }
    static String getRexToMatchJarMetaDataFile() {
        return StringUtil.slantToBackSlant(META_DATA_FILE_NAME+"$");
    }
    static String getRexToMatchAllMetaDataFile(){
        return ".*"+META_DATA_FILE_NAME+"$";
    }
    static void copyFile(InputStream in,OutputStream out,boolean closeStream) throws IOException {
        byte[] buffer=new byte[1024];
        int len;
        while ((len=in.read(buffer))!=-1){
            out.write(buffer,0,len);
        }
        if (closeStream){
            in.close();
            out.close();
        }
    }
    static void propertyStore(Properties properties,OutputStream outputStream,String comments) throws IOException {
        properties.store(getWriter(outputStream),comments);
    }
    static void propertiesLoad(Properties properties,InputStream inputStream) throws IOException {
        properties.load(getReader(inputStream));
    }
}
