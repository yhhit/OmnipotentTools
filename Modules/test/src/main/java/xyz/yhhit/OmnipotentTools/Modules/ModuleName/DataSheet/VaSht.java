package xyz.yhhit.OmnipotentTools.Modules.ModuleName.DataSheet;


import xyz.yhhit.OmnipotentTools.Modules.ModuleName.ItfUI;

import java.io.File;

public class VaSht {
    //ItfUI所在包全包名
    public static final String PACKAGE_NAME= ItfUI.class.getPackageName();
    //ItfUI所在相对路径
    public static final String PATH= PACKAGE_NAME.replace(".", File.separator);

}
