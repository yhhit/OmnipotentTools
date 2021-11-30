package xyz.yhhit.OmnipotentTools.Utils;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

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

    public static void startModules(String modName,String uiName){
        try{
            callModuleFns(modName,uiName,"onCreate","onShowUI sfs","onExit");
        }catch (ClassNotFoundException e){
            OLogger.logInfo("模块文件不完整，请重新安装模块！",e);
        } catch (InvocationTargetException |NoSuchMethodException |IllegalAccessException |InstantiationException e) {
            OLogger.logInfo("模块文件可能损坏或版本过低，请重新安装最新版本！",e);
        }catch (Exception e){
            OLogger.logInfo("模块文件可能损坏或版本过低，请重新安装最新版本！",e);
        }
    }
    public static String getModuleLocalName(String modName){
        try{
            return (String)callModuleFn(modName,getModuleDftUI(modName),"getLocalName");
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e){
            OLogger.logDebug("模块文件不完整，请重新安装模块！",e);
        }
        return modName;
    }
    public static String getModuleVersion(String modName){
        try{
            return (String)callModuleFn(modName,getModuleDftUI(modName),"getVersion");
        }catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e){
            OLogger.logDebug("模块文件不完整，请重新安装模块！",e);
        }
        return "未知";
    }
    public static String getModuleDftUI(String modName){
        return getModuleUIS(modName).get(0);
    }
    public static ArrayList<String> getModuleUIS(String modName){
        File flUiPt=new File(VaSht.MODULES_PATH+File.separator+modName+File.separator+"UIS");
        ArrayList<String> uis=new ArrayList<String>();
        if(flUiPt.isDirectory()){
            File[] files=flUiPt.listFiles();
            for(File f:files){
                if(f.isFile()){
                    uis.add(f.getName().replaceAll("\\..*", ""));
                }
            }
        }
        return uis;
    }
    public static void installModule(String path) throws Exception {
        ZipUtils.unzip(path,".");
        File flMeIfPt=new File("."+File.separator+"META-INF");
        RemoveDir.remove(flMeIfPt);
        scanModules();
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
        if(fun.contains(" ")){
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

}
