package xyz.yhhit.OmnipotentTools.Utils;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;
import xyz.yhhit.OmnipotentTools.DataSheet.VaSht;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OLogger {
    public static final Logger mOLogger = Logger.getLogger(VaSht.PACKAGE_NAME);
    //程序运行模式，调试\发布...
    public static RUN_MODE_TYPE mRunMode = CSht.RUN_MODE;
    public enum RUN_MODE_TYPE{
        DEBUG,RELEASE
    }
    //初始化日志记录器，日志会保存到模块包下log文件夹中
    public static void initialLogger(RUN_MODE_TYPE runMode){
        mRunMode=runMode;
        if (System.getProperty("java.util.logging.config.class") == null
                && System.getProperty("java.util.logging.config.file") == null)
        {
            try
            {

                switch (mRunMode){
                    case DEBUG: mOLogger.setLevel (Level.ALL);break;
                    case RELEASE: mOLogger.setLevel (Level.INFO);mOLogger.setUseParentHandlers(false);
                }

                final int LOG_ROTATION_COUNT = 20;
                File file=new File("log");
                if(!file.exists()){//如果文件夹不存在
                    file.mkdir();//创建文件夹
                }
                Handler handler = new FileHandler("log"+File.separator+"log%g.xml", 2*1024*1024, LOG_ROTATION_COUNT);//2MB
                handler.setEncoding("utf-8");
                handler.setLevel(Level.ALL);
                mOLogger.addHandler(handler);
            }
            catch (IOException e)
            {
                mOLogger.log(Level.SEVERE, "Can't create log file handler", e);
            }
        }
    }
    //用于记录导致程序崩溃信息
    public static void logCrash(Throwable e){
        System.out.println("抱歉！程序出现了错误，即将终止运行，建议提交日志文件给开发者！");
        mOLogger.log(Level.SEVERE,"程序崩溃",e);
        System.exit(CSht.EXIT_TYPE.ABORT.ordinal());
    }
    //用于记录可以显示给用户的信息
    public static void logInfo(String str,Throwable e){
        System.out.println(str);
        mOLogger.log(Level.INFO,str,e);
    }
    //用于记录调试模式(RUN_MODE=DEBUG)时才输出显示的信息
    public static void logDebug(String str,Throwable e){
        switch (mRunMode){
            case DEBUG: System.out.println("DEBUG:"+str);
            if(e!=null)
                e.printStackTrace();break;
            case RELEASE: ;break;
        }
        mOLogger.log(Level.FINEST,str,e);
    }
    //用于记录未知但不致命的错误信息
    public static void logUnknown(String str,Throwable e){
        mOLogger.log(Level.SEVERE,str,e);
    }
}
