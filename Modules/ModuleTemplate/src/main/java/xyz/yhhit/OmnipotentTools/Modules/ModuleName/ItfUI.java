package xyz.yhhit.OmnipotentTools.Modules.ModuleName;


import xyz.yhhit.OmnipotentTools.Modules.ModuleName.DataSheet.CSht;

public interface ItfUI {

    //此函数内应包含模块运行必须提前进行的操作（如没有函数体可为空），当其他模块调用此模块其他函数时，会先运行此函数
    public void onCreate();
    //调用此函数加载模块UI界面
    public void onShowUI(String[] args);
    //当模块退出时会调用此函数
    public void onExit();
    //用于获取软件的本地化名称,此函数应当无需启动模块便可直接调用
    public default String getLocalName() {
        return CSht.NAME;
    }
    //用于获取软件版本号
    public default String getVersion(){
        return CSht.VERSION;
    }

    //请将可供其他模块的调用的函数放在此处，建议使用public default reternValue funName(){}声明(也可不用default)，尽量不要声明为静态方法
}
