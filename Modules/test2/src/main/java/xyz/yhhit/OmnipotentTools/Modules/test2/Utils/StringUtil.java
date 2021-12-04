package xyz.yhhit.OmnipotentTools.Modules.test2.Utils;

public class StringUtil {
    public static String slantToBackSlant (String str){
        return str.replace("\\","/");
    }
    public static String doubleSlant (String str){
        return str.replace("\\","\\\\");
    }
}
