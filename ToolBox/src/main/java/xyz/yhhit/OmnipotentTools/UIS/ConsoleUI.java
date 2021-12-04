package xyz.yhhit.OmnipotentTools.UIS;

import jdk.jshell.execution.Util;
import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;
import xyz.yhhit.OmnipotentTools.ItfUI;
import xyz.yhhit.OmnipotentTools.Utils.Modules;
import xyz.yhhit.OmnipotentTools.Utils.OLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static xyz.yhhit.OmnipotentTools.Utils.Modules.*;
import static xyz.yhhit.OmnipotentTools.Utils.OLogger.*;

public class ConsoleUI implements ItfUI {
    public static void main(String[] args) {
        VaSht.UI=new ConsoleUI();
        //启动UI界面
        VaSht.UI.onCreate();
        VaSht.UI.onShowUI(args);
        VaSht.UI.onExit();
    }
    @Override
    public int onCreate() {
        //加载log
        initialLogger(CSht.RUN_MODE);
        //扫描所有模块
        scanModules();
        return 0;
    }

    @Override
    public int onExit() {
        return 0;
    }

    @Override
    public int onShowUI(String[] args) {
        while(true){
            String version=getVersion();
            //打印提示
            System.out.println("===========欢迎来到"+getLocalName()+(version!=null?(" V"+version):"")+"===========");
            System.out.println("请选择要启动的工具:");
            int i=0;
            ArrayList<ArrayList> uiss=new ArrayList<ArrayList>();
            for(String modName: VaSht.MODULES_NAME){
                try{
                    version=getModuleVersion(modName);
                }catch (Exception |Error e){
                    OLogger.logDebug("获取模块版本信息失败！",e);
                    version=null;
                }
                System.out.print(++i+"."+ getModulesShowName(modName)+(version!=null?(" V"+version):" 未知的版本"));
                ArrayList<String> uis=getModuleUIS(modName);
                uiss.add(uis);
                if(getModuleType(modName)== CSht.MODULE_TYPE.MODULE){
                    System.out.print("( 支持的UI:");
                    int j=0;
                    for (String s:uis) {
                        ++j;
                        String uiName=uis.get(j-1);
                        System.out.print(j+"."+uiNameToChinese(uiName)+" ");
                    }
                    System.out.print(")");
                }
                System.out.println();

            }
            System.out.println("i.安装模块");
            System.out.println("r.卸载模块");
            System.out.println("c.清理所有依赖包(新手请勿使用此项)");
            System.out.println("q.退出");

            //读取输入
            Scanner scanner = new Scanner(getSystemInputStreamReader());
            try {
                int choice=scanner.nextInt();
                if(uiss.get(choice-1).size() <=1){
                    if(VaSht.MODULES.get(VaSht.MODULES_NAME.get(choice-1)).equals(CSht.MODULE_TYPE.MODULE))
                        startModulesAndCheckDep(choice - 1, getModuleDftUI(VaSht.MODULES_NAME.get(choice - 1)));
                    else
                        startModulesAndCheckDep(choice-1,null);
                }else{
                    System.out.println("请选择模块UI:");
                    ArrayList<String> uis= uiss.get(choice-1);
                    int j=0;
                    for (String s:uis) {
                        ++j;
                        System.out.print(j+"."+uiNameToChinese(uis.get(j-1)) +" ");
                    }
                    System.out.println();
                    int choice2=scanner.nextInt();
                    startModulesAndCheckDep(choice-1, (String) uiss.get(choice-1).get(choice2-1));
                }
            }catch (InputMismatchException e){
                String str=scanner.next();
                String confirm;
                switch (str){
                    case "q":return 0;
                    case "i":
                        System.out.println("请输入模块安装包路径(可以直接拖动文件到此):");
                        try {
                            String modName=Modules.installModule(scanner.next());
                            System.out.println("安装\""+modName+"\"成功！");
                        } catch (Exception ex) {
                            logInfo("模块包错误！请重新下载！",ex);
                        }
                        ;break;
                    case "r":
                        try{
                            System.out.println("请输入模块序号:");
                            int modOrd=scanner.nextInt()-1;
                            while(true) {
                                System.out.println("您确定要卸载\"" +getModulesShowName(VaSht.MODULES_NAME.get(modOrd)) + "\"吗?(yes/no)");
                                if ((confirm = scanner.next()).equals("yes")) {
                                    try {
                                        Modules.removeModule(modOrd);
                                        System.out.println("卸载成功！");
                                    } catch (Exception ex) {
                                        logInfo("卸载模块失败，模块被占用！", ex);
                                    }
                                    break;
                                } else if (confirm.equals("no")) {
                                    break;
                                } else {
                                    logInfo("输入错误！请重新输入！", null);
                                    scanner.nextLine();
                                }
                            }
                        }catch (Exception exc){
                            logInfo("输入错误！请重新输入！",exc);
                            scanner.nextLine();
                        }
                        break;
                    case "c":
                        while(true){
                            System.out.println("清理所有依赖后，所有需要依赖的模块都需要重新安装，确认是否清理？(yes/no):");
                            if((confirm=scanner.next()).equals("yes")){
                                try {
                                    Modules.cleanAllDependencies();
                                    System.out.println("清理成功！");
                                } catch (Exception ex) {
                                    logInfo("清理依赖失败，文件被占用！",ex);
                                }
                                break;
                            }else if(confirm.equals("no")){
                                break;
                            }else {
                                logInfo("输入错误！请重新输入！",null);
                                scanner.nextLine();
                            }
                        }
                        break;
                    default:logInfo("输入错误！请重新输入！",null);scanner.nextLine();

                }

            }catch (Exception e){
                logInfo("输入错误！请重新输入！",e);scanner.nextLine();
            }

        }

    }

    public String setModuleShowName(String modName){
        Scanner scanner = new Scanner(getSystemInputStreamReader());
        System.out.println("请为该模块设置显示名称:");
        String showName=scanner.next();
        try {
            Modules.setModuleShowName(modName,showName);
        } catch (IOException e) {
            logDebug("",e);
        }
        return showName==null?modName:showName;
    }
    static String uiNameToChinese(String uiName){
        int k=0;
        if(uiName.equals(ItfUI.UI_TYPE_STR[k]))
            uiName=ItfUI.UI_TYPE_STR_CHINESE[k];
        else if(uiName.equals(ItfUI.UI_TYPE_STR[++k]))
            uiName=ItfUI.UI_TYPE_STR_CHINESE[k];
        return uiName;
    }
    //检查模块依赖并启动模块
    static void startModulesAndCheckDep(int modOrd,String uiName) throws Exception {
        String modName=VaSht.MODULES_NAME.get(modOrd);
        try{
            String[] notInstall=getModuleNotInstallDepName(VaSht.MODULES_NAME.get(modOrd));
            if(notInstall.length>0){
                System.out.println("该模块依赖于以下模块:");
                for (String s:
                        notInstall) {
                    System.out.print(s+" ");
                }
                System.out.println("\n请先安装以上模块在试！");
            }else
                startModules(modName,uiName,null);
        }catch (IOException e){
            OLogger.logUnknown("依赖文件不存在!",e);
            startModules(modName,uiName,null);
        }catch (Exception e){
            OLogger.logUnknown("未知错误",e);
        }
    }
    static InputStreamReader getSystemInputStreamReader(){
        return new InputStreamReader(System.in, Charset.forName(VaSht.SYSTEM_IN_CHARSET));
    }
    @Override
    public UI_TYPE getUIType(){
        return UI_TYPE.CONSOLE_UI;
    }
}
