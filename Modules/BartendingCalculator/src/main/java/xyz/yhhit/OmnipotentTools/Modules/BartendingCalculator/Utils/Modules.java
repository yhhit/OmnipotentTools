package xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.Utils;


import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.ItfUI;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Properties;

public class Modules {
    public static final String MetaDataFileName = "meta.properties";
    public static final String OmniPackageName="xyz.yhhit.OmnipotentTools";
    public static final String DependencyModules="DependencyModules";
    public static final HashMap<String,String> RegisterDependency=new HashMap<>();

    //moduleName:UIName
    public static void registerDependency(HashMap<String,String> moduleNameAndUIName){
        RegisterDependency.putAll(moduleNameAndUIName);
    }
    static String getModuleVersion(Properties metaDataPro){
        String value=metaDataPro.getProperty("Version","");
        return value.equals("")?null:value;
    }
    static String getModulePath(){
        return packageNameToPath(ItfUI.class.getPackageName());
    }
    static String getModuleMetaDataPath(){
        return getModulePath() + File.separator + MetaDataFileName;
    }
    static String getModuleFullPackageName(String modName){
        return OmniPackageName+".Modules."+modName+".UIS."+RegisterDependency.get(modName);
    }
    public static Object callOtherModuleFunction(String modName,String methodName,Object[] args )
            throws Exception {
        if(RegisterDependency.containsKey(modName)){
            if(RegisterDependency.get(modName).equals("jar")){
                throw new Exception("\""+modName+"\"调用jar请使用callOtherModuleJar");
            }
            else
                return callExtFunction(getModuleFullPackageName(modName),methodName,args);
        }else{
            throw new Exception("\""+modName+"\"模块未被注册");
        }

    }
    public static void callOtherModuleJar(String modName,String args) throws Exception {
        if(RegisterDependency.containsKey(modName)){
            if(RegisterDependency.get(modName).equals("jar")){
                callExtJar(modName,args);
            }
            else
                throw new Exception("\""+modName+"\"调用模块请使用callOtherModuleFunction");
        }else{
            throw new Exception("\""+modName+"\"模块未被注册");
        }
    }
    static File getModuleMetaDateFile(){
        String modMetaDataPath=getModuleMetaDataPath();
        File modMeta=new File(modMetaDataPath);
        return modMeta;
    }
    static void generateDepMetaData(String[] deps) throws IOException {
        Properties metaData = getModuleProperties();
        String depString="";
        for (String d:
             deps) {
            depString=depString+d+" ";
        }
        metaData.setProperty(DependencyModules,depString);
        FileOutputStream proOutStream=new FileOutputStream(getModuleMetaDateFile());
        metaData.store(proOutStream,"");
        proOutStream.close();
    }
    static String packageNameToPath(String packageName){
        return packageName.replace(".", File.separator);
    }
    static String getModulesNameFromClassName(String className){
        return getModulesNameFromPackageName(className).replaceAll("\\.+","");
    }
    //只是裁剪掉包名前面的部分，后面的捕获裁剪
    static String getModulesNameFromPackageName(String modName){
        return modName.replace(OmniPackageName+"."+"Modules"+".","");
    }
    static Object callExtFunction(String className,String methodName,Object[] args )
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {
        Class<?> classClass = Class.forName(className);
        Object classObject = classClass.getConstructor().newInstance();
        return callExtFunction(classClass,classObject,methodName,args);
    }
    static Object callExtFunction(Class<?> classClass,Object classObject,
                                         String functionName,Object[] args )
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Class<?>[] argClasses=new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i]=args[i].getClass();
        }
        Method m= classClass.getMethod(functionName, (Class<?>[]) argClasses);
        return m.invoke(classObject,args);
    }
    //TODO:
    static void callExtJar(String modName,String args){

    }

    //获取模块版本信息
    public static String getModuleVersion(){
        try{
            return getModuleVersion(getModuleProperties());
        }catch (Exception |Error e){
            //don't do anything
        }
        return null;
    }
    static Properties getModuleProperties(InputStream proInStream) throws IOException {
        Properties metaDataPro=new Properties();
        metaDataPro.load(proInStream);
        proInStream.close();
        return metaDataPro;
    }
    static Properties getModuleProperties() throws IOException {
        FileInputStream proInStream=new FileInputStream(getModuleMetaDateFile());
        return getModuleProperties(proInStream);
    }

}
