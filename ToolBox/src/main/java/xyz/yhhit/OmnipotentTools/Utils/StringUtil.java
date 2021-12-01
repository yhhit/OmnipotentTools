package xyz.yhhit.OmnipotentTools.Utils;

public class StringUtil {
    public static String slantToBackSlant (String str){
        return str.replace("\\","/");
    }
    public static String doubleSlant (String str){
        return str.replace("\\","\\\\");
    }
}
