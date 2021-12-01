package xyz.yhhit.OmnipotentTools.Utils;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static xyz.yhhit.OmnipotentTools.DataSheet.VaSht.PACKAGE_NAME;
import static xyz.yhhit.OmnipotentTools.Utils.OLogger.logDebug;

public class Modules {
    //扫描所有模块
    public static void scanModules(){
        VaSht.MODULES.clear();
        File flMdPt=new File(VaSht.MODULES_PATH);
        if(flMdPt.isDirectory()){
            File[] files=flMdPt.listFiles();
            for(File f:files){
                if(f.isDirectory()){
                    VaSht.MODULES.add(f.getName());
                    logDebug(f.getName(),null);
                }
            }
        }
    }
    static void startModulesT(String modName,String uiName,String arg){
        try{
            callModuleFns(modName,uiName,"onCreate","onShowUI"+((arg==null)?"":" "+arg),"onExit");
        }catch (ClassNotFoundException e){
            OLogger.logInfo("模块文件不完整，请重新安装模块！",e);
        } catch (InvocationTargetException |NoSuchMethodException |IllegalAccessException |InstantiationException e) {
            OLogger.logInfo("模块文件可能损坏或版本过低，请重新安装最新版本！",e);
        }catch (Throwable e){
            OLogger.logInfo("模块文件可能损坏或版本过低，请重新安装最新版本！",e);
        }
    }
    public static void startModules(String modName,String uiName,String arg){
        if(CSht.UI_TYPE_STR[CSht.UI_TYPE.CONSOLE_UI.ordinal()].equals(uiName)){
            startModulesT(modName,uiName,arg);
        }else{
            VaSht.THREAD_POOL.submit(new Runnable() {
                @Override
                public void run() {
                    startModulesT(modName,uiName,arg);
                }
            });
        }
    }
    public static String getModuleLocalName(String modName){
        try{
            return (String)callModuleFn(modName,getModuleDftUI(modName),"getLocalName");
        }catch (Exception |Error e){
            OLogger.logDebug("模块文件不完整，请重新安装模块！", e);
        }
        return modName;
    }
    public static String getModuleVersion(String modName){
        try{
            return (String)callModuleFn(modName,getModuleDftUI(modName),"getVersion");
        }catch (Exception |Error e){
            OLogger.logDebug("模块文件不完整，请重新安装模块！",e);
        }
        return "未知";
    }
    public static String getModuleDftUI(String modName){
        return getModuleUIS(modName).get(0);
    }
    //移除xxx.class后面的.class
    public static ArrayList<String> getModuleUIS(String modName){
        File flUiPt=new File(VaSht.MODULES_PATH+File.separator+modName+File.separator+"UIS");
        ArrayList<String> uis=new ArrayList<String>();
        if(flUiPt.isDirectory()){
            File[] files=flUiPt.listFiles();
            for(File f:files){
                if(f.isFile()){
                    uis.add(f.getName().replaceAll("\\.class$", ""));
                }
            }
        }
        return uis;
    }
    public static String installModule(String path) throws Exception {
        path=path.trim();
        String modName=getModuleNameByJar(path);
        if(modName==null){
            throw new Exception("模块不完整！");
        }
        String basePath=VaSht.PATH+File.separator;
        String[] ignoreFile={basePath+"DataSheet"+File.separator,basePath+"UIS"+File.separator,basePath+"Utils"+File.separator,basePath+"ItfUI.class","META-INF"+File.separator};
        ZipUtils.unzip(path,".",ignoreFile);
        scanModules();
        return getModuleLocalName(modName);
    }
    public static void removeModule(int module) throws Exception {
        String modName=VaSht.MODULES.get(module);
        if(modName==null||"".equals(modName))
            throw new Exception("模块名错误");
        File flMd=new File(VaSht.MODULES_PATH+File.separator+modName);
        RemoveDir.remove(flMd);
        scanModules();
    }
    public static void cleanAllDependencies() throws Exception {
        File flRtPt=new File(".");
        File[] files=flRtPt.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if("xyz".equals(name)||"log".equals(name)||"jdk".equals(name))
                    return false;
                return true;
            }
        });
        for (File f:files
             ) {
            if(f.isDirectory())
                RemoveDir.remove(f);
        }
        scanModules();
    }
    public static String getModuleNameByJar(String path) throws IOException {
        path=path.trim();
        String modName=null;
        File zipPath=new File(path);
        ZipInputStream zipInputStream=new ZipInputStream(new FileInputStream(zipPath));
        ZipEntry zipEntry=zipInputStream.getNextEntry();
        while (zipEntry!=null){
            String pathName=zipEntry.getName();
            if(pathName.matches(StringUtil.slantToBackSlant(VaSht.MODULES_PATH+File.separator+"[^/]+"+File.separator+"$"))){
                String pathSplit[]=pathName.split(StringUtil.slantToBackSlant(File.separator));
                modName=pathSplit[pathSplit.length-1];
                zipInputStream.close();
                return modName;
            }
            zipInputStream.closeEntry();
            zipEntry=zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        return null;
    }
    public static String[] getModuleNotInstallDepName(String modName) throws IOException {
        String[] moduleDep = getModuleDepModulesByName(modName);
        ArrayList<String> notInstall=new ArrayList<String>();
        for (String m:
             moduleDep) {
            notInstall.add(m);
        }
        notInstall.removeAll(VaSht.MODULES);
        return notInstall.toArray(new String[notInstall.size()]);
    }
    public static String[] getModuleDepModulesByName(String modName) throws IOException {
        String modMetaDataFilePath=VaSht.MODULES_PATH+File.separator+modName+File.separator+ CSht.MetaDataFileName;
        File modMeta=new File(modMetaDataFilePath);
        FileInputStream inModMeta=new FileInputStream(modMeta);
        String[] modDeps = getModuleDepModulesByInputStream(inModMeta);
        inModMeta.close();
        return modDeps;
    }
    //TODO:未测试
    public static String[] getModuleDepModulesByJar(String path) throws IOException {
        path=path.trim();
        String modDep;
        File zipPath=new File(path);
        ZipInputStream zipInputStream=new ZipInputStream(new FileInputStream(zipPath));
        ZipEntry zipEntry=zipInputStream.getNextEntry();
        String[] modDeps=new String[0];
        while (zipEntry!=null){
            String pathName=zipEntry.getName();
            if(pathName.matches(StringUtil.slantToBackSlant(CSht.MetaDataFileName+"$"))){
                modDeps=getModuleDepModulesByInputStream(zipInputStream);
                break;
            }
            zipInputStream.closeEntry();
            zipEntry=zipInputStream.getNextEntry();
        }
        zipInputStream.close();
        return modDeps;
    }
    static Class<?> getModuleClassByName(String modName,String uiName) throws ClassNotFoundException {
        return  Class.forName(PACKAGE_NAME +".Modules." + modName + "." + "UIS." +uiName);
    }
    static Object callModuleFn(String modName,String uiName,String funNames) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = getModuleClassByName(modName,uiName);
        Object o = aClass.getConstructor().newInstance();
        return callFun(funNames,aClass,o);
    }
    static void callModuleFns(String modName,String uiName, String... funNames) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> aClass = getModuleClassByName(modName,uiName);
        Object o = aClass.getConstructor().newInstance();
        for (String f:funNames) {
            callFun(f,aClass,o);
        }
    }
    static Object callFun(String fun,Class<?> aClass,Object o) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String args[];
        Method m=null;
        if(fun.matches("^onShowUI")){
            args=splitArg(fun);
            m=aClass.getMethod(args[0], String[].class);
            //invoke调用参数是数组的函数时，需要将参数转换为(Object)
            return m.invoke(o,(Object) args);
        }else{
            m=aClass.getMethod(fun);
            return m.invoke(o);
        }
    }
    //将参数按照空格分割
    static String[] splitArg(String str){
        return str.split("[\\s]+");
    }
    static String[] getModuleDepModulesByInputStream(InputStream inModMeta) throws IOException {
        Properties proper=new Properties();
        proper.load(inModMeta);
        String dependencyModules = proper.getProperty("DependencyModules","").trim();
        String[] depMods=new String[0];
        if((dependencyModules!=null)&&(!dependencyModules.equals("")))
            depMods=dependencyModules.split("\\s+");
        return depMods;
    }
}
