package com.luoyu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
/*
 * 来自https://github.com/Hicores/QTool
 * 简化了一些无用操作
 */

public class FileUtil {
    
    public static synchronized void writeToFile(String File, String FileContent) {
        try {
            File parent = new File(File).getParentFile();
            if (!parent.exists()) parent.mkdirs();
            FileOutputStream fOut = new FileOutputStream(File);
            fOut.write(FileContent.getBytes(StandardCharsets.UTF_8));
            fOut.close();
        } catch (Exception e) {
        }
    }

    public static synchronized long getDirSize(File file) {
        // 判断文件是否存在
        if (file.exists()) {
            // 如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                if (children == null) return 0;
                for (File f : children) size += getDirSize(f);
                return size;
            } else { // 如果是文件则直接返回其大小,以“兆”为单位
                long size = file.length();
                return size;
            }
        } else {
            return 0;
        }
    }

    public static synchronized void writeToFile(String File, byte[] FileContent) {
        try {
            File parent = new File(File).getParentFile();
            if (!parent.exists()) parent.mkdirs();
            FileOutputStream fOut = new FileOutputStream(File);
            fOut.write(FileContent);
            fOut.close();
        } catch (Exception e) {
        }
    }

    public static synchronized String readFileString(File f) {
        try {
            FileInputStream fInp = new FileInputStream(f);
            String Content = new String(DataUtil.readAllBytes(fInp), StandardCharsets.UTF_8);
            fInp.close();
            return Content;
        } catch (Exception e) {
            return null;
        }
    }

    public static synchronized byte[] readFile(File f) {
        try {
            FileInputStream fInp = new FileInputStream(f);
            byte[] Content = DataUtil.readAllBytes(fInp);
            fInp.close();
            return Content;
        } catch (Exception e) {
            return null;
        }
    }

    public static String readFileString(String f) {
        return readFileString(new File(f));
    }

    public static synchronized void deleteFile(File file) {
        if (file == null) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null) return;
        // 遍历该目录下的文件对象
        for (File f : files) {
            if (f.isDirectory()) {
                deleteFile(f);
            } else {
                f.delete();
            }
        }
        file.delete();
    }

    public static synchronized void copyFile(String source, String dest) {

        try {

            File f = new File(dest);
            f = f.getParentFile();
            if (!f.exists()) f.mkdirs();

            File aaa = new File(dest);
            if (aaa.exists()) aaa.delete();

            InputStream in = new FileInputStream(new File(source));
            OutputStream out = new FileOutputStream(new File(dest));
            byte[] buffer = new byte[4096];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception e) {
        }
    }
    
}
