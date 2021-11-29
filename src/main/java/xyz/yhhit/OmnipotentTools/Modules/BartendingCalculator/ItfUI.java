package xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator;


import xyz.yhhit.OmnipotentTools.Modules.BartendingCalculator.DataSheet.CSht;

public interface ItfUI {

    //此函数内应包含模块运行必须提前进行的操作（如没有函数体可为空），当其他模块调用此模块其他函数时，会先运行此函数
    public void onCreate();
    //调用此函数加载模块UI界面
    public void onShowUI();
    //用于获取软件的本地化名称,此函数应当无需启动模块便可直接调用
    public default String getLocalName() {
        return CSht.NAME;
    }
    //用于获取软件版本号
    public default String getVersion(){
        return CSht.VERSION;
    }
    //当模块退出时会调用此函数
    public void onExit();

    //Calculate the amount of base wine added
    public default void calcAmountOfBaseWine(double basicLiquorsContent, double cocktailAlcoholContent,
                                             double[] mixersMass, double basicLiquorsMass[]){
        basicLiquorsMass[0]=(cocktailAlcoholContent*mixersMass[0])/(basicLiquorsContent-cocktailAlcoholContent);
    }
    public default void calcAmountOfBaseWineAndWatter(double basicLiquorsContent, double cocktailAlcoholContent,
                                                      double cocktailMass, double mixersMass[], double basicLiquorsMass[]){
        mixersMass[0]=cocktailMass*(basicLiquorsContent-cocktailAlcoholContent)/basicLiquorsContent;
        basicLiquorsMass[0]=cocktailMass-mixersMass[0];
    }
}
