package xyz.yhhit.OmnipotentTools.Modules.ModuleName.UIS;

import xyz.yhhit.OmnipotentTools.Modules.ModuleName.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.Modules.ModuleName.ItfUI;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static xyz.yhhit.OmnipotentTools.Modules.ModuleName.Utils.OLogger.initialLogger;
import static xyz.yhhit.OmnipotentTools.Modules.ModuleName.Utils.OLogger.logInfo;


public class GD implements ItfUI {
    //测试入口
    public static void main(String[] args) {
        ItfUI ui=new ConsoleUI();
        ui.onCreate();
        ui.onShowUI(args);
        ui.onExit();
    }
    @Override
    public void onCreate() {
        initialLogger();
        try {
            Runtime.getRuntime().exec("cmd java --version");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onShowUI(String[] args) {
        while(true){
            //显示界面
            System.out.println("===========欢迎来到"+ CSht.NAME+" V"+CSht.VERSION+"===========");
            System.out.println("1.xxx。");
            System.out.println("2.xxx。");
            System.out.println("q.退出。");
            //读取输入
            Scanner scanner = new Scanner(System.in);

            try {
                switch (scanner.nextInt()){
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        logInfo("输入错误！请重新输入！",null);
                        scanner.nextLine();
                }
            }catch (InputMismatchException e){
                if(scanner.next().equals("q"))
                    return;
                else{
                    logInfo("输入错误！请重新输入！",e);
                    scanner.nextLine();
                }
            }
            catch (Exception e) {
                logInfo("输入错误！请重新输入！",e);
                scanner.nextLine();
            }
        }
    }

    @Override
    public void onExit() {

    }

}
