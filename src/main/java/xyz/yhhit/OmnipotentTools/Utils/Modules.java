package xyz.yhhit.OmnipotentTools.Utils;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static xyz.yhhit.OmnipotentTools.DataSheet.VaSht.PACKAGE_NAME;
import static xyz.yhhit.OmnipotentTools.Utils.OLogger.logDebug;

public class Modules {
    //扫描所有模块
    public static void scanModules(){
        File file=new File(VaSht.MODULES_PATH);
        if(file.isDirectory()){
            File[] files=file.listFiles();
            for(File f:files){
                if(f.isDirectory()){
                    VaSht.MODULES.add(f.getName());
                    logDebug(f.getName(),null);
                }
            }
        }
    }

    public static void startModules(String modName,String uiName){
        try{
            callModuleFns(modName,uiName,"onCreate","onShowUI","onExit");
        }catch (ClassNotFoundException e){
            OLogger.logInfo("模块文件不完整，请重新安装模块！",e);
        } catch (InvocationTargetException |NoSuchMethodException |IllegalAccessException |InstantiationException e) {
            OLogger.logInfo("模块文件可能损坏或版本过低，请重新安装最新版本！",e);
        }
    }
    public static String getModuleLocalName(String modName){
        try{
            return (String)callModuleFn(modName,getModuleUIS(modName).get(0),"getLocalName");
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e){
            OLogger.logInfo("模块文件不完整，请重新安装模块！",e);
        }
        return modName;
    }
    public static String getModuleVersion(String modName){
        try{
            return (String)callModuleFn(modName,getModuleUIS(modName).get(0),"getVersion");
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e){
            OLogger.logInfo("模块文件不完整，请重新安装模块！",e);
        }
        return modName;
    }
    public static ArrayList<String> getModuleUIS(String modName){
        File file=new File(VaSht.MODULES_PATH+File.separator+modName+File.separator+"UIS");
        ArrayList<String> uis=new ArrayList<String>();
        if(file.isDirectory()){
            File[] files=file.listFiles();
            for(File f:files){
                if(f.isFile()){
                    uis.add(f.getName().replaceAll("\\..*", ""));
                }
            }
        }
        return uis;
    }
    static Class<?> getModuleClassByName(String modName,String uiName) throws ClassNotFoundException {
        return  Class.forName(PACKAGE_NAME +".Modules." + modName + "." + "UIS." +uiName);
    }
    static Object callModuleFn(String modName,String uiName,String funNames) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> aClass = getModuleClassByName(modName,uiName);
        Object o = aClass.getConstructor().newInstance();
        Method m=aClass.getMethod(funNames);
        return m.invoke(o);
    }
    static void callModuleFns(String modName,String uiName, String... funNames) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Class<?> aClass = getModuleClassByName(modName,uiName);
        Object o = aClass.getConstructor().newInstance();
        Method m=null;
        for (String f:funNames) {
            m=aClass.getMethod(f);
            m.invoke(o);
        }
    }

}
