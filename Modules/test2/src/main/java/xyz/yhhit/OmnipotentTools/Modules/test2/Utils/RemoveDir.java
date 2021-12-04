package xyz.yhhit.OmnipotentTools.Modules.test2.Utils;


import java.io.File;

public class RemoveDir {
    public static void remove(File dir) {
        File files[] = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                remove(files[i]);
            } else {
                //删除文件
                //System.out.println("deleted  ::  " + files[i].toString());
                files[i].delete();
            }
        }
        //删除目录
        dir.delete();
        //System.out.println("deleted  ::  " + dir.toString());
    }

//    public static void main(String[] args) {
//        File dir = new File("F:\\test");
//        remove(dir);
//
//    }
}