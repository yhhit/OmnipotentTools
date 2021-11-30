package xyz.yhhit.OmnipotentTools.DataSheet;

import xyz.yhhit.OmnipotentTools.ItfUI;

import java.io.File;
import java.util.Vector;

public class VaSht {
    //ItfUI所在包全包名
    public static final String PACKAGE_NAME=ItfUI.class.getPackageName();
    //ItfUI所在相对路径
    public static final String PATH= PACKAGE_NAME.replace(".", File.separator);
    //Modules所在相对路径
    public static final String MODULES_PATH= PATH+File.separator+"Modules";
    //所有已安装模块名
    public static final Vector<String> MODULES=new Vector<>();


}
