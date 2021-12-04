package xyz.yhhit.OmnipotentTools.Modules.test2.DataSheet;

//常量表
public class CSht {
    //模块运行模式，调试\发布...
    public static RUN_MODE_TYPE RUN_MODE= RUN_MODE_TYPE.RELEASE;
    //模块名
    public static String NAME="xxx";//修改此处为模块名
    //版本号
    public static String VERSION="x.x.x";//修改此处为版本号
    //模块运行模式，调试\发布...
    public enum RUN_MODE_TYPE{
        DEBUG,RELEASE
    }
    //程序退出代码
    public enum EXIT_TYPE{
        NORMAL,ABORT
    }
    //UI界面类型
    public enum UI_TYPE{
        CONSOLE_UI,GRAPHIC_UI
    }
    //UI界面名
    public static final String[] UI_TYPE_STR={"ConsoleUI","GraphicUI"};
    public static final String[] UI_TYPE_STR_CHINESE={"控制台界面","图形界面"};
    public static final String MetaDataFileName = "meta.properties";
}
