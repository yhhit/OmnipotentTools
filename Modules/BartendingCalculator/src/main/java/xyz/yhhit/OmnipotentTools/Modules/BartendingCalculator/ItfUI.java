package xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator;

import static xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.Utils.Modules.getModuleVersion;

public interface ItfUI {

    final static String LocalName="调酒计算器";
    public enum UI_TYPE{
        CONSOLE_UI,GRAPHIC_UI
    }
    //此函数内应包含模块运行必须提前进行的操作（如没有函数体可为空），当其他模块调用此模块其他函数时，会先运行此函数
    public int onCreate();
    //调用此函数加载模块UI界面
    public int onShowUI(String[] args);
    //当模块退出时会调用此函数
    public int onExit();
    //用于获取软件的本地化名称
    public default String getLocalName(){return LocalName;};
    //用于获取软件版本号
    public default String getVersion(){
        return getModuleVersion();
    }
    //用于获取界面是图形界面还是控制台界面
    public UI_TYPE getUIType();
    //请将可供其他模块的调用的函数放在此处，建议使用public default returnValue funName(){}声明(也可不用default)，尽量不要声明为静态方法
    //注意：函数参数不可使用基本类型如int等，应当使用相应的包装器。
    // Calculate the amount of base wine added
    public default void calcAmountOfBaseWine(Double basicLiquorsContent, Double cocktailAlcoholContent,
                                             Double[] mixersMass, Double basicLiquorsMass[]){
        basicLiquorsMass[0]=(cocktailAlcoholContent*mixersMass[0])/(basicLiquorsContent-cocktailAlcoholContent);
    }
    public default void calcAmountOfBaseWineAndWatter(Double basicLiquorsContent, Double cocktailAlcoholContent,
                                                      Double cocktailMass, Double mixersMass[], Double basicLiquorsMass[]){
        mixersMass[0]=cocktailMass*(basicLiquorsContent-cocktailAlcoholContent)/basicLiquorsContent;
        basicLiquorsMass[0]=cocktailMass-mixersMass[0];
    }
}
