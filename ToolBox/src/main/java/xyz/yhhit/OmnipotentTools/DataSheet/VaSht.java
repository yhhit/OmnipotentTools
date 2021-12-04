package xyz.yhhit.OmnipotentTools.DataSheet;

import xyz.yhhit.OmnipotentTools.ItfUI;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VaSht {
    public static ItfUI UI;
    public static boolean IS_WINDOWS_SYSTEM=System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
    public static String SYSTEM_IN_CHARSET=IS_WINDOWS_SYSTEM?"GBK":"UTF-8";
    //ItfUI所在包全包名
    public static final String PACKAGE_NAME=ItfUI.class.getPackageName();
    //ItfUI所在相对路径
    public static final String PATH= PACKAGE_NAME.replace(".", File.separator);
    //Modules所在相对路径，注意：主程序的MODULES_PATH和模块的MODULES_PATH设置的是不一样的
    public static final String MODULES_PATH= PATH+File.separator+"Modules";
    public static final String EXT_MODULES_PATH= PATH+File.separator+"ExtModules";

    public static final HashMap<String, CSht.MODULE_TYPE> MODULES=new HashMap<String, CSht.MODULE_TYPE>();
    //所有已安装模块名
    public static final Vector<String> MODULES_NAME=new Vector<>();
    public static final ExecutorService THREAD_POOL= Executors.newCachedThreadPool();


}
