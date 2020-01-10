package com.zhoug.common.utils;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 文件工具
 */
public class FileUtils {
    private static final String TAG = "FileUtils";


    /**
     * 获取文件名(包括后缀名)
     *
     * @param file
     * @return
     */
    public static String getNameWithSuffix(File file) {
        if (file == null) {
            return null;
        }
        return file.getName();
    }

    /**
     * 获取文件名(包括后缀名)
     *
     * @param path
     * @return
     */
    public static String getNameWithSuffix(String path) {
        if (path == null || path.length() == 0) {
            return null;
        }
        return getNameWithSuffix(new File(path));

    }


    /**
     * 获取文件名(不包括后缀)
     *
     * @param path
     * @return
     */
    public static String getNameWithOutSuffix(String path) {
        String nameWithType = getNameWithSuffix(path);
        if (nameWithType != null) {
            int endIndex = nameWithType.lastIndexOf(".");
            if (endIndex > 0 && endIndex < nameWithType.length()) {
                return nameWithType.substring(0, endIndex);
            } else {
                return nameWithType;
            }
        } else {
            return null;
        }
    }

    /**
     * 获取文件名(不包括后缀)
     *
     * @param file
     * @return
     */
    public static String getNameWithOutSuffix(File file) {
        return file == null ? null : getNameWithOutSuffix(file.getAbsolutePath());
    }

    /**
     * 获取文件的后缀
     *
     * @param path
     * @return
     */
    public static String getSuffix(String path) {
       /* String nameWithType = getNameWithSuffix(path);
        if(nameWithType==null){
            return null;
        }else{
            int index = nameWithType.lastIndexOf(".");
            if(index>=0 && index<nameWithType.length()-1){
                return nameWithType.substring(index+1);
            }else{
                return null;//无后缀
            }
        }*/
        if (path == null) return null;
        int index = path.lastIndexOf(".");
        if (index >= 0 && index < path.length() - 1) {
            return path.substring(index + 1);
        } else {
            return null;//无后缀
        }

    }


    /**
     * 获取文件的后缀
     *
     * @param file
     * @return
     */
    public static String getSuffix(File file) {
        return file == null ? null : getSuffix(file.getAbsolutePath());
    }

    /**
     * 创建文件夹
     *
     * @param file
     * @return 是否创建成功
     */
    public static boolean createDirectory(File file) {
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;//文件夹存在
        }
    }

    /**
     * 创建文件夹
     *
     * @param absolutePath
     * @return 是否创建成功
     */
    public static boolean createDirectory(String absolutePath) {
        return createDirectory(new File(absolutePath));
    }

    /**
     * 创建文件
     *
     * @param file
     * @return 是否创建成功
     */
    public static boolean createFile(File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            if (mkdirs) {
                try {
                    return file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                return file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return 是否创建成功
     */
    public static boolean createFile(String path) {
        return createFile(new File(path));
    }


    /**
     * sdcard是否存在
     *
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取外部储存根目录下的文件
     *
     * @param folder   文件夹
     * @param fileName 文件
     * @return
     */
    public static File getExternalFile(String folder, String fileName) {
        if (isSDCardExist()) {
            File root = Environment.getExternalStorageDirectory();
            File file1 = new File(root, folder);
            if (!file1.exists()) {
                boolean mkdirs = file1.mkdirs();
                if (mkdirs) {
                    return new File(file1, fileName);
                } else {
                    return null;
                }
            } else {
                return new File(file1, fileName);
            }
        } else {
            return null;
        }

    }

    /**
     * 获取外部储存根目录下的文件
     *
     * @param path 文件路径
     * @return
     */
    public static File getExternalFile(String path) {
        if (isSDCardExist()) {
            File root = Environment.getExternalStorageDirectory();
            File file = new File(root, path);
            if (!file.getParentFile().exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                if (mkdirs) {
                    return file;
                }
            } else {
                return file;
            }
        }
        return null;
    }

    public static File getExternalPublicFolder(String type){
        if(isSDCardExist()){
            File root = Environment.getExternalStoragePublicDirectory(type);
            if(!root.exists()){
                root.mkdirs();
            }
            return root;
        }else{
            return null;
        }
    }


    /**
     * 获取外部缓存
     *
     * @param context
     * @param folder  缓存目录 为null时表示缓存根目录
     * @return
     */
    public static File getExternalCacheFolder(Context context, String folder) {
        File cacheDir = context.getExternalCacheDir();
        File file = cacheDir;
        if (!StringUtils.isEmpty(folder)) {
            file = new File(cacheDir, folder);
        }
        if (file != null) {
            if (!file.exists()) {
                if (file.mkdirs()) {
                    return file;
                }
            } else {
                return file;
            }
        }
        return null;
    }


    /**
     * 获取文件夹下面所有文件的大小
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        if (!file.exists()) {
            return 0;
        }
        long size = 0;
        if (file.isFile()) {
            return file.length();
        } else {
            File[] files = file.listFiles();
            for (File file1 : files) {
                size += getFileSize(file1);
            }
        }
//        Log.d(TAG, "getFileSize: size="+size);
        return size;
    }


    /**
     * 把字节大小转化为 以B\K\M... 为单位
     *
     * @param size
     * @return
     */
    public static String getFormatFileSize(long size) {
//        Log.d(TAG, "getFormatSize: size="+size);
        double kb = size / 1024;
        if (kb < 1) {
            return size + "B";
        }

        double mb = kb / 1024;
        if (mb < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kb));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "K";
        }

        double gb = mb / 1024;
        if (gb < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(mb));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "M";
        }

        double tb = gb / 1024;
        if (tb < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gb));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(tb);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 获取app外部和内部缓存大小
     *
     * @param context
     * @return
     */
    public static String getAppCacheSize(Context context) {
        long size = 0;
        //外部存储的缓存
        if (isSDCardExist()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null && externalCacheDir.exists()) {
                size += getFileSize(externalCacheDir);
            }
        }
        //内部存储的缓存
        File cacheDir = context.getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            size += getFileSize(cacheDir);
        }
        return getFormatFileSize(size);

    }

    /**
     * 删除文件/或文件夹下所有文件
     *
     * @param file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
//            Log.d(TAG, "deleteFile: ");
            boolean delete = file.delete();
        } else {
            File[] files = file.listFiles();
            for (File file1 : files) {
                deleteFile(file1);
            }
        }
    }

    /**
     * 清除app的缓存[外部存储的缓存:context.getExternalCacheDir+内部存储的缓存:context.getCacheDir()]
     *
     * @param context
     */
    public static void deleteAppCache(Context context) {
        //外部存储的缓存
        if (isSDCardExist()) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null && externalCacheDir.exists()) {
                deleteFile(externalCacheDir);
            }
        }
        //内部存储的缓存
        File cacheDir = context.getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            deleteFile(cacheDir);
        }
    }

    /**
     * 获取音视频文件的播放长度
     * @param videoPath
     * @return
     */
    public static int getVideoDuration(String videoPath) {
        if(videoPath==null){
            return 0;
        }
        int duration;
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            duration = Integer.parseInt(mmr.extractMetadata
                    (MediaMetadataRetriever.METADATA_KEY_DURATION));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return duration;
    }
}
