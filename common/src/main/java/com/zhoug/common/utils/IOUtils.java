package com.zhoug.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * io流工具
 */
public class IOUtils {

    /**
     * 保存文件
     *
     * @param resultPath 保存的路径
     * @param bytes      文件字节数组
     * @param append      是否在源文件末尾开始保存
     * @return
     */
    public static boolean keepFile(String resultPath, byte[] bytes,boolean append) {
        FileOutputStream fos = null;
        boolean success = false;
        File parentFile = new File(resultPath).getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            parentFile = null;
        }
        try {
            fos = new FileOutputStream(resultPath,append);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }



    /**
     * 保存文件
     *
     * @param resultPath 保存的路径
     * @param bytes      文件字节数组
     * @return
     */
    public static boolean keepFile(String resultPath, byte[] bytes) {
       return  keepFile(resultPath,bytes ,false );
    }

    /**
     * 复制文件
     * @param sourcePath 资源文件路径
     * @param resultPath 最终文件路径
     * @return
     */
    public static boolean copyFile(String sourcePath, String resultPath) {
        boolean success = false;
        //首先创建文件夹
        File parentFile = new File(resultPath).getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        FileOutputStream fos = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(sourcePath);
            fos = new FileOutputStream(resultPath);
            byte[] buf = new byte[1024 * 10];
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return success;
    }


}
