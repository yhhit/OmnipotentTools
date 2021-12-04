package xyz.yhhit.OmnipotentTools;

import xyz.yhhit.OmnipotentTools.DataSheet.CSht;

public interface ItfUI {
    public enum UI_TYPE{
        CONSOLE_UI,GRAPHIC_UI
    }
    public static final String[] UI_TYPE_STR={"ConsoleUI","GraphicUI"};
    public static final String[] UI_TYPE_STR_CHINESE={"控制台界面","图形界面"};
    //此函数内应包含模块运行必须提前进行的操作（如没有函数体可为空），当其他模块调用此模块其他函数时，会先运行此函数
    public int onCreate();
    //调用此函数加载模块UI界面
    public int onShowUI(String[] args);
    //用于获取软件的本地化名称,此函数应当无需启动模块便可直接调用
    public default String getLocalName() {
        return CSht.NAME;
    }
    //用于获取软件版本号
    public default String getVersion(){
        return CSht.VERSION;
    }
    //当模块退出时会调用此函数
    public int onExit();
    //用于获取界面是图形界面还是控制台界面
    public UI_TYPE getUIType();
    //调用UI设置模块显示名称
    public String setModuleShowName(String modName);
}
