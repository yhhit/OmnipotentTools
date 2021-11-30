package xyz.yhhit.OmnipotentTools.UIS;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;
import xyz.yhhit.OmnipotentTools.ItfUI;
import xyz.yhhit.OmnipotentTools.Utils.Modules;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static xyz.yhhit.OmnipotentTools.Utils.Modules.*;
import static xyz.yhhit.OmnipotentTools.Utils.OLogger.initialLogger;
import static xyz.yhhit.OmnipotentTools.Utils.OLogger.logInfo;

public class ConsoleUI implements ItfUI {
    public static void main(String[] args) {
        ItfUI omTl=new ConsoleUI();
        //启动UI界面
        omTl.onCreate();
        omTl.onShowUI(args);
        omTl.onExit();
    }
    @Override
    public void onCreate() {
        //加载log
        initialLogger();
        //扫描所有模块
        scanModules();
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onShowUI(String[] args) {
        while(true){
            //打印提示
            System.out.println("===========欢迎来到鸿昊万能工具箱===========");
            System.out.println("请选择要启动的工具:");
            int i=0;
            ArrayList<ArrayList> uiss=new ArrayList<ArrayList>();
            for(String name: VaSht.MODULES){
                System.out.print(++i+"."+ getModuleLocalName(name)+" V"+getModuleVersion(name)+"( 支持的UI:");
                ArrayList<String> uis=getModuleUIS(name);
                uiss.add(uis);
                int j=0;
                for (String s:uis) {
                    ++j;
                    String uiName=uis.get(j-1);
                    System.out.print(j+"."+uiNameToChinese(uiName)+" ");
                }
                System.out.println(")");
            }
            System.out.println("q.退出");
            System.out.println("i.安装模块");
            System.out.println("r.卸载模块");
            System.out.println("c.清理所有依赖(新手请勿使用此项)");

            //读取输入
            Scanner scanner = new Scanner(System.in);
            try {
                int choice=scanner.nextInt();
                if(!(uiss.get(choice-1).size() >1)){
                    startModules(VaSht.MODULES.get(choice-1),getModuleDftUI(VaSht.MODULES.get(choice-1)));
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
                    startModules(VaSht.MODULES.get(choice-1), (String) uiss.get(choice-1).get(choice2-1));


                }
            }catch (InputMismatchException e){
                String str=scanner.next();
                String confirm;
                switch (str){
                    case "q":System.exit(CSht.EXIT_TYPE.NORMAL.ordinal());break;
                    case "i":
                        System.out.println("请输入模块安装包路径(可以直接拖动文件到此):");
                        try {

                            Modules.installModule(scanner.next());
                            System.out.println("安装成功！");
                        } catch (Exception ex) {

                            logInfo("模块包错误！请重新下载！",ex);
                        }
                        ;break;
                    case "r":
                        try{
                            System.out.println("请输入模块序号:");
                            int mdOd=scanner.nextInt()-1;
                            while(true) {
                                System.out.println("您确定要卸载\"" +getModuleLocalName(VaSht.MODULES.get(mdOd)) + "\"吗?(yes/no)");
                                if ((confirm = scanner.next()).equals("yes")) {
                                    try {
                                        Modules.removeModule(mdOd);
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
    static String uiNameToChinese(String uiName){
        int k=0;
        if(uiName.equals(CSht.UI_TYPE_STR[k]))
            uiName=CSht.UI_TYPE_STR_CHINESE[k];
        else if(uiName.equals(CSht.UI_TYPE_STR[++k]))
            uiName=CSht.UI_TYPE_STR_CHINESE[k];
        return uiName;
    }

}