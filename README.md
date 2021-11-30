# OmnipotentTools(全能工具箱) V0.1
## 简介

**OmnipotentTools**是一款**强大的模块化工具箱**，跨平台，支持**简单快速开发模块**，模块**即插即用**，模块之间可以**相互调用**。

本软件的**终极目标**是使得一切功能全部成为工具箱的模块，仅需查看其他模块的接口并且调用就可以为自己**快速开发自己需要的功能模块**，未来本软件将支持更多平台。

目前工具箱仅有控制台页面，但是**模块开发**已经**支持图形界面**，所以大家可以开始开发具有图形界面的模块了。

## 使用

### 快速上手

#### 下载release版本

访问 https://github.com/yhhit/OmnipotentTools/releases 点击 OmnipotentTools.zip 开始下载。

#### 解压运行

解压后双击runConsoleUI.cmd即可运行。

## 开发

### 搭建完整模块开发环境

#### 克隆存储库

在github上克隆存储库到你的电脑。

#### 导入项目

本项目使用IDEA创建，IDEA可以直接导入项目，使用其他开发环境的请自行导入项目。

#### 创建模块项目

创建”模块项目“通常有两种方式**(推荐使用第一种)**

1. 使用模板快速创建模块项目

   * 解压项目根目录下的"ModuleTemplet.zip"的"ModuleName"包到Modules下，更改包名为模块名即可。

2. 手动创建模块项目

   * 首先在“Modules”包下新建一个包,包名为“模块名”。

   * 然后在包下新建"DataSheet"和"UIS"两个包以及IftUI接口。

   * 在"DataSheet"包下新建CSht和VaSht两个类

   * 在"UIS"包下新建ConsoleUI类

   * 复制以下代码到"CSht.java"中

     ~~~java
     //常量表
     public class CSht {
        //模块运行模式，调试\发布...
        public static xyz.yhhit.OmnipotentTools.DataSheet.CSht.RUN_MODE_TYPE RUN_MODE= xyz.yhhit.OmnipotentTools.DataSheet.CSht.RUN_MODE;
        public static String NAME="模块名";
        public static String VERSION="模块版本号";//示例0.1
     }
     ~~~
     
   * 复制以下代码到"VaSht.java"中
   
     ~~~java
     public class VaSht {
         //ItfUI所在包全包名
         public static final String PACKAGE_NAME=ItfUI.class.getPackageName();
         //ItfUI所在相对路径
         public static final String PATH= PACKAGE_NAME.replace(".", File.separator);
     }
     ~~~
   
   * 复制以下代码到“ItfUI.java”中
   
     ~~~java
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
     }
     ~~~
   
   * 复制以下代码到"ConsoleUI.java"中
   
     ~~~java
     public class ConsoleUI implements ItfUI {
     
         @Override
         public void onCreate() {
     
         }
     
         @Override
         public void onShowUI() {
             while(true){
                 //显示界面
                 System.out.println("===========欢迎来到xxx===========");
                 System.out.println("1.xxx。");
                 System.out.println("2.xxx。");
                 System.out.println("q.退出。");
                 //读取输入
                 Scanner scanner = new Scanner(System.in);
                 try {
                     switch (scanner.nextInt()){
                         case 1:
                             break;
                         case 2:
                             break;
                         default:
                             logInfo("输入错误！请重新输入！",null);
                     }
                 }catch (InputMismatchException e){
                     if(scanner.next().equals("q"))
                         return;
                     else
                         logInfo("输入错误！请重新输入！",e);
                 }
                 catch (Exception e) {
                     logInfo("输入错误！请重新输入！",e);
                 }
             }
         }
     
         @Override
         public void onExit() {
     
         }
     
     }
     ~~~


#### 完善模块项目

接下来我们需要完善上面创建的几个文件。

1. 完善"CSht.java"中的模块名和版本号。

   ~~~java
       public static String NAME="模块名";
       public static String VERSION="模块版本号";//示例0.1
   ~~~

   

2. 在"ItfUI.java"中新建需要实现的函数声明,如没有可以不写（这里声明的公有函数将可以被其他模块调用）

   ~~~java
   //例如
   public void printHello();
   
   //通用函数建议提取到接口中作为默认实现
   public default void calcuDistance(double x,double y);
   ~~~

3. 在"ConsoleUI"中完善onShowUI函数,并实现刚刚定义在"ItfUI.java"接口中的函数，onShowUI函数由工具箱自动调用。

#### 配置编译器工作路径

因为编译的模块往往生成在target\classes目录下，但是编译器的工作路径在当前项目路径下，所以会导致调试时程序搜索不到模块，所以需要配置编译器工作路径到target\classes目录下。

##### IDEA的配置方法

打开项目 点击 file->project structure->project settings-> project。

配置 project compiler output 路径到当前项目的target\classes目录下即可。

#### 开发图形UI模块

如果想要支持图形界面的模块需要在"UIS"包下新建GraphicUI类。

```java
public class GraphicUI implements ItfUI {

    @Override
    public void onCreate() {

    }

    @Override
    public void onShowUI() {
       
    }

    @Override
    public void onExit() {

    }

}
```

### 发布模块

编译完成后，需要提取出模块文件。

##### 设置编译模式

修改package xyz.yhhit.OmnipotentTools.DataSheet;下的CSht.java文件

~~~java
public static RUN_MODE_TYPE RUN_MODE= RUN_MODE_TYPE.RELEASE;
~~~

##### 编译并提取模块

首先清理项目，编译项目。

使用IDEA开发者，在项目路径"target\classes"目录下将所有文件全部压缩，发布即可。

说明：目前工具箱还不支持联网功能，大家可以发布到本项目下，也可发布到自己的github上。

##### 部署模块

将上一步的模块文件覆盖到OmnipotentTools下的目录即可，注意实际上是包与包的覆盖。

## 开发计划

* 添加多国语言功能。

* 添加网络模块功能。

* 解决版本兼容性问题。

* 添加版本号比较工具。

* 添加图形界面。