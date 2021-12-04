package xyz.yhhit.OmnipotentTools.Modules.test2.UIS;

import xyz.yhhit.OmnipotentTools.Modules.test2.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.Modules.test2.ItfUI;
import xyz.yhhit.OmnipotentTools.Modules.test2.Utils.OLogger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.InputMismatchException;
import java.util.Scanner;

import static xyz.yhhit.OmnipotentTools.Modules.test2.DataSheet.VaSht.PACKAGE_NAME;
import static xyz.yhhit.OmnipotentTools.Modules.test2.Utils.Modules.splitArg;


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
        OLogger.initialLogger();

        try {
            String fun="calcAmountOfBaseWine";
//            String[] args=splitArg(fun);
//            String[] str={"String"};
//            Object ooo=(Object)str;
            Double[] ans=new Double[1];
            Object[] oa={40.0,5.0,new Double[]{100.0},ans};
            callExtFunction("xyz.yhhit.OmnipotentTools.Modules." + "BartendingCalculator" + "." + "UIS." +"ConsoleUI",fun,oa);
            System.out.println("ans is:"+ans[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return;
    }
    public static Object callExtFunction(String className,String methodName,Object[] args )
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, InstantiationException,
            IllegalAccessException {
        Class<?> classClass = Class.forName(className);
        Object classObject = classClass.getConstructor().newInstance();
        return callExtFunction(classClass,classObject,methodName,args);
    }
    public static Object callExtFunction(Class<?> classClass,Object classObject,
                               String methodName,Object[] args )
            throws NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        Class<?>[] argClasses=new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argClasses[i]=args[i].getClass();
        }
        Method m= classClass.getMethod(methodName, (Class<?>[]) argClasses);
        return m.invoke(classObject,args);
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
                        OLogger.logInfo("输入错误！请重新输入！",null);
                        scanner.nextLine();
                }
            }catch (InputMismatchException e){
                if(scanner.next().equals("q"))
                    return;
                else{
                    OLogger.logInfo("输入错误！请重新输入！",e);
                    scanner.nextLine();
                }
            }
            catch (Exception e) {
                OLogger.logInfo("输入错误！请重新输入！",e);
                scanner.nextLine();
            }
        }
    }

    @Override
    public void onExit() {

    }

}
