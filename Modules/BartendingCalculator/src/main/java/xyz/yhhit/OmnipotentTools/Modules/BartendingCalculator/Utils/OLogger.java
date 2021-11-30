package xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.Utils;

import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.DataSheet.VaSht;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OLogger {
    public static final Logger myLogger = Logger.getLogger(VaSht.PACKAGE_NAME);
    //初始化日志记录器，日志会保存到模块包下log文件夹中
    public static void initialLogger(){
        if (System.getProperty("java.util.logging.config.class") == null
                && System.getProperty("java.util.logging.config.file") == null)
        {
            try
            {
                myLogger.setUseParentHandlers(false);
                switch (CSht.RUN_MODE){
                    case DEBUG: myLogger.setLevel (Level.ALL);break;
                    case RELEASE: myLogger.setLevel (Level.INFO);
                }

                final int LOG_ROTATION_COUNT = 20;
                File file=new File(VaSht.PATH+File.separator+"log");
                if(!file.exists()){//如果文件夹不存在
                    file.mkdir();//创建文件夹
                }
                Handler handler = new FileHandler(VaSht.PATH+File.separator+"log"+File.separator+"log%g.xml", 2*1024*1024, LOG_ROTATION_COUNT);//2MB
                handler.setEncoding("utf-8");
                handler.setLevel(Level.ALL);
                myLogger.addHandler(handler);
            }
            catch (IOException e)
            {
                myLogger.log(Level.SEVERE, "Can't create log file handler", e);
            }
        }
    }
    //用于记录导致程序崩溃信息
    public static void logCrash(Exception e){
        System.out.println("抱歉！程序出现了错误，即将终止运行，建议提交日志文件给开发者！");
        myLogger.log(Level.SEVERE,"程序崩溃",e);
        System.exit(CSht.EXIT_TYPE.ABORT.ordinal());
    }
    //用于记录可以显示给用户的信息
    public static void logInfo(String str,Exception e){
        System.out.println(str);
        myLogger.log(Level.INFO,str,e);
    }
    //用于记录调试模式(RUN_MODE=DEBUG)时才输出显示的信息
    public static void logDebug(String str,Exception e){
        switch (CSht.RUN_MODE){
            case DEBUG: System.out.println("DEBUG:"+str);break;
            case RELEASE: ;break;
        }
        myLogger.log(Level.FINEST,str,e);
    }
}
