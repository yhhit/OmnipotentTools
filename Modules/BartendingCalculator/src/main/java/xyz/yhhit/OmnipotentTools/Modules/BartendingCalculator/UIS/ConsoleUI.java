package xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.UIS;

import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.ItfUI;
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
    public int onCreate() {
        return 0;
    }

    @Override
    public int onShowUI(String[] args) {
        while(true){
            String version=getVersion();
            //显示界面
            System.out.println("===========欢迎来到"+ getLocalName()+(version!=null?(" V"+version):"")+"===========");
            System.out.println("1.计算加基酒的质量。");
            System.out.println("2.计算加水和酒的质量。");
            System.out.println("q.退出。");
            //读取输入
            Scanner scanner = new Scanner(System.in);

            Double basicLiquorsContent=0.0;
            Double cocktailAlcoholContent=0.0;
            Double cocktailMass=0.0;
            Double[] basicLiquorsMass= new Double[1];
            Double[] mixersMass= new Double[1];
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
    @Override
    public UI_TYPE getUIType(){
        return UI_TYPE.CONSOLE_UI;
    }
}
