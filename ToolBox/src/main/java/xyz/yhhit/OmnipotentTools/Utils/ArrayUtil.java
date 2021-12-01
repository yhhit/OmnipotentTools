package xyz.yhhit.OmnipotentTools.Utils;

public class ArrayUtil {
    public static boolean isContain(String[] arr, String targetValue) {
        for (String s : arr) {
            try{
                if (s.equals(targetValue))
                    return true;
            }catch (NullPointerException e){
                //don't do anything
            }
        }
        return false;
    }
    public static boolean isContainArrByRex(String[] arrRex, String targetValue) {
        for (String s : arrRex) {
            try{
                if (targetValue.matches(s))
                    return true;
            }catch (NullPointerException e){
                //don't do anything
            }
        }
        return false;
    }
}
