package xyz.yhhit.OmnipotentTools.DataSheet;

import xyz.yhhit.OmnipotentTools.ItfUI;

import java.io.File;

//常量表
public class CSht {
    //程序运行模式，调试\发布...
    public static RUN_MODE_TYPE RUN_MODE=RUN_MODE_TYPE.DEBUG;
    public static String NAME="鸿昊万能工具箱";
    public static String VERSION="0.1";


    public enum RUN_MODE_TYPE{
        DEBUG,RELEASE
    }
    public enum EXIT_TYPE{
        NORMAL,ABORT
    }
    public enum UI_TYPE{
        CONSOLE_UI,GRAPHIC_UI
    }
    public static final String[] UI_TYPE_STR={"ConsoleUI","GraphicUI"};


}
