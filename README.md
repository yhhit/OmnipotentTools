# OmnipotentTools(全能工具箱) V0.1
[toc]

## 简介

**OmnipotentTools**是一款**强大的模块化工具箱**，跨平台，支持**简单快速开发模块**，模块**即插即用**，创新的提出了模块**相互调用**架构。

本软件的**终极目标**是打造**全平台**，**全功能**的全能软件，仅需查看其他模块的接口并且调用就可以为自己**快速开发自己需要的功能模块**，未来本软件将支持更多平台。

目前工具箱仅有控制台页面，但是**模块开发**已经**支持图形界面**，所以大家可以开始开发具有图形界面的模块了。

## 使用指南

### 小白指南

#### 下载

访问 https://github.com/yhhit/OmnipotentTools/releases 点击 OmnipotentTools_with_jdk.zip 开始下载。

#### 使用

解压 OmnipotentTools_with_jdk.zip  打开文件夹，双击 "看不懂的话点我运行.cmd"

### 快速上手

#### 下载运行

访问 https://github.com/yhhit/OmnipotentTools/releases 点击 OmnipotentTools.zip 或 OmnipotentTools_with_jdk.zip 开始下载。

解压 OmnipotentTools_with_jdk.zip  打开文件夹，双击 "runConsoleUi.cmd"运行。

#### 下载安装插件

访问 https://github.com/yhhit/OmnipotentTools/releases 下载 xxx.jar。

打开OmnipotentTools 在菜单中输入 i 按回车 拖入jar包进行安装。



## 开发指南

### 快速开发模块指南

#### 下载、导入模板项目

在  https://github.com/yhhit/OmnipotentTools/ 根目录直接下载 ModuleTemplate.zip 

解压并导入项目，配置Maven(如不使用Maven也可不用配置)

配置jdk版本为11

#### 修改模板

展开 xyz.yhhit.OmnipotentTools.Modules 包

修改 ModuleName 包名为 要开发的模块名

修改pom文件 artifactId 为模块名

```java
<artifactId>ModuleTemplate</artifactId>
```

修改 CSht.java 下的

```
//模块名
public static String NAME="xxx";//修改此处为模块名
//版本号
public static String VERSION="x.x.x";//修改此处为版本号
```

#### 正式开发

模块的功能都在ConsoleUI类中实现，ConsoleUI继承自ItfUI接口

```java
public class ConsoleUI implements ItfUI
```

ItfUI接口非常简单，需要注意的函数onCreate()、onShowUI()、onExit()三个函数;

分别在木块被创建、展示、退出时被调用、用于初始化、展示界面和清理工作。

模板中onShowUI中的代码是一个示例代码，可以完全删除，您可以根据喜好修改。

开发时需要将可供其他函数调用的功能卸载ItfUI接口中，这样其他模块便可以通过此接口调用你的模块的功能。

```java
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
```

#### 开发图形界面的模块

在UIS下新建类 GraphicUI 复制ConsoleUI的所有函数到 GraphicUI中即可

#### 运行测试

运行ConsoleUI类即可(图形界面运行GraphicUI类)

#### 打包模块

将模块直接打包为jar包即可

### 搭建完整开发环境指南

以上的模块开发并不包含ImnipotentTools主程序，无法集成测试。如需搭建完整开发环境请参见以下教程。

#### 克隆存储库，导入项目

在github上克隆存储库到你的电脑。导入开发软件中打开。

#### 目录结构

Modules目录用于存放模块代码，ToolBox是主程序代码

#### 配置生成文件目录及编译器工作目录

主程序的生成目录已经在pom文件中配置为项目根目录下的target/classes目录，以下是参考代码

模块模板的生成目录请自行配置到和主程序生成目录一致

```
<outputDirectory>../target/classes</outputDirectory>
<testOutputDirectory>../target/test-classes</testOutputDirectory>
```

接下来需要配置编译器工作目录到项目根目录下的target/classes目录中。

##### IDEA下配置编译器工作目录

点击右上角运行按钮左边的运行配置框，点击 Edit Configurations 修改 working directory 与上面的编译生成目录一致。

#### 注意事项

##### 打包前先清理项目

在使用完整开发环境开发时，编译的主程序文件和模块文件会混合在一起，如果直接打包会打包所有文件，所以应该先清理整个项目在打包模块。

## 开发计划

* 近期计划
  * 多进程，线程支持
  * 模块调用依赖支持
  * 下载模块
* 总计划
* 添加多国语言功能。
* 添加网络模块功能。
* 解决版本兼容性问题。
* 添加版本号比较工具。
* 添加图形界面。

## 本次更新

* 安装模块
* 卸载模块
* 清理依赖
* 架构升级

## 常见问题解答

如何解决模块依赖问题？

为了解决模块依赖问题，安装模块文件将直接覆盖模块文件到根目录下，并提供清理所有依赖功能，清理所有依赖后必须重新安装使用依赖的模块.





