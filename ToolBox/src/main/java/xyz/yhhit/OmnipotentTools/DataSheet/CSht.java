package xyz.yhhit.OmnipotentTools.DataSheet;


import xyz.yhhit.OmnipotentTools.Utils.OLogger;

//常量表
public class CSht {
    public static String NAME="鸿昊万能工具箱";
    public static String VERSION="0.1.5";
    public static String FILE_CHARSET="UTF-8";
    //程序运行模式，调试\发布...
    public static OLogger.RUN_MODE_TYPE RUN_MODE = OLogger.RUN_MODE_TYPE.RELEASE;
    public enum EXIT_TYPE{
        NORMAL,ABORT
    }
    public static final String META_DATA_FILE_NAME = "meta.properties";

    public enum MODULE_TYPE{
        MODULE,JAR,UNKNOWN_JAR
    }
}
