package xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.UIS;

import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.ItfUI;

import java.lang.reflect.InvocationTargetException;
import java.util.InputMismatchException;
import java.util.Scanner;

import static xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.Utils.OLogger.initialLogger;
import static xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.Utils.OLogger.logInfo;


public class ConsoleUI implements ItfUI {
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
    }

    @Override
    public void onShowUI(String[] args) {
        while(true){
            //显示界面
            System.out.println("===========欢迎来到"+ CSht.NAME+" V"+CSht.VERSION+"===========");
            System.out.println("1.计算加基酒的质量。");
            System.out.println("2.计算加水和酒的质量。");
            System.out.println("q.退出。");
            //读取输入
            Scanner scanner = new Scanner(System.in);

            double basicLiquorsContent=0;
            double cocktailAlcoholContent=0;
            double cocktailMass=0;
            double[] basicLiquorsMass= new double[1];
            double[] mixersMass= new double[1];
            try {
                switch (scanner.nextInt()){
                    case 1:
                        System.out.println("请依次输入:基酒酒精度(%)，加水质量(饮料/克)，配置后的酒精度（%）:");
                        basicLiquorsContent=scanner.nextDouble();
                        mixersMass[0]=scanner.nextDouble();
                        cocktailAlcoholContent=scanner.nextDouble();
                        calcAmountOfBaseWine(basicLiquorsContent,cocktailAlcoholContent,mixersMass,
                                basicLiquorsMass);
                        System.out.println("需要加入基酒"+basicLiquorsMass[0]+"克。");
                        break;
                    case 2:
                        System.out.println("请依次输入:基酒酒精度(%)，配置后的酒精度（%）,配置后的鸡尾酒质量(克):");
                        basicLiquorsContent=scanner.nextDouble();
                        cocktailAlcoholContent=scanner.nextDouble();
                        cocktailMass=scanner.nextDouble();
                        calcAmountOfBaseWineAndWatter(basicLiquorsContent, cocktailAlcoholContent,
                                cocktailMass, mixersMass, basicLiquorsMass);
                        System.out.println("需要加入基酒"+ basicLiquorsMass[0]+"克,加水(饮料)"+mixersMass[0]+"克。");
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
