1. 模块开发文档 V0.1

   

   

   

   ## 搭建完整模块开发环境

   ### 一 、克隆存储库

   克隆存储库到电脑

   一、创建文件

   1. 在modules包下以“模块名”新建“包名”
   2. 新建插件接口Itf+模块名
   3. 新建控制台界面ConsoleUI+模块名
   4. 如果想开发图形界面，还需新建GraphicUI+模块名的类

   * 示例
     * 假设我们要创建一个名为"Calculator"的模块
     * 首先创建一个"xyz.yhhit.OmnipotentTools.Modules.Calculator"的包
     * 然后创建名为"ItfCalculator"的接口
     * 之后创建一个名为"ConsoleUICalculator的类"
     * 如果想开发图形界面，还需新建GraphicUICalculator的类

   二、完善接口文件

   1. 接口文件需要至少包含以下几个函数

   ~~~java
       //此函数内应包含模块运行必须提前进行的操作（如没有函数体可为空），当其他模块调用此模块其他函数时，会先运行此函数
       public void onCreate();
       //调用此函数加载模块UI界面
       public void showUI();
       //用于获取软件的本地化名称,此函数应当无需启动模块便可直接调用
       public default String getLocalName() {
               return "软件名称";
       }
       //用于获取软件版本号
       public default String getVersion(){
           return "0.1";
           }
       //当模块退出时会调用此函数
       public void onExit();
   ~~~

   三、完善类文件

   1. 只需要遵循接口标准，编写响应函数实现即可