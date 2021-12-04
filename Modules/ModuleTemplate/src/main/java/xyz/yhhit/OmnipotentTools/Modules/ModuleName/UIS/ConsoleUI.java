package xyz.yhhit.OmnipotentTools.Modules.ModuleName.UIS;

import xyz.yhhit.OmnipotentTools.Modules.ModuleName.ItfUI;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleUI implements ItfUI {

    //测试入口
    public static void main(String[] args) {
        ItfUI ui=new ConsoleUI();
        ui.onCreate();
        ui.onShowUI(args);
        ui.onExit();
    }
    @Override
    public UI_TYPE getUIType(){
        return UI_TYPE.CONSOLE_UI;
    }

    @Override
    public int onCreate() {
        return 0;
    }

    @Override
    public int onShowUI(String[] args) {
        while(true){
            String version=getVersion();
            //显示界面
            System.out.println("===========欢迎来到"+ getLocalName()+(version!=null?(" V"+version):"")+"===========");
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
                        System.out.println("输入错误！请重新输入！");
                        scanner.nextLine();
                }
            }catch (InputMismatchException e){
                if(scanner.next().equals("q"))
                    return 0;
                else{
                    System.out.println("输入错误！请重新输入！");
                    scanner.nextLine();
                }
            }
            catch (Exception e) {
                System.out.println("输入错误！请重新输入！");
                scanner.nextLine();
            }
        }
    }

    @Override
    public int onExit() {
        return 0;
    }


}
