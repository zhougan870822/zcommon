package com.zhoug.common.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Bitmap工具
 */
public class BitmapUtils {
    private static final String TAG = "BitmapUtils";

    private BitmapUtils() {

    }

    /**
     * 计算宽高缩放比例inSampleSize
     * @param options  Options options
     * @param requestWidth  需要的宽度
     * @param requestHeight 需要的高度
     * @return inSampleSize
     */
    public static int calculateInSampleSize(Options options,int requestWidth, int requestHeight){
        int inSampleSize=1;
        if(requestWidth<=0 || requestHeight<=0)return inSampleSize;

        if(requestWidth<options.outWidth || requestHeight<options.outHeight){
            int widthRatio = Math.round((float) options.outWidth / (float) requestWidth);
            int heightRatio = Math.round((float) options.outHeight / (float) requestHeight);
            int maxRatio = widthRatio > heightRatio ? widthRatio : heightRatio;
            if(maxRatio>1){
                inSampleSize=maxRatio;
            }
        }
        return inSampleSize;
    }

    /**
     * 获取图片Bitmap
     *
     * @param path          图片路径
     * @param requestWidth  需要的宽度
     * @param requestHeight 需要的高度
     * @param config        默认Bitmap.Config.ARGB_8888
     * @return
     */
    public static Bitmap decodeFile(String path, int requestWidth, int requestHeight, Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        Options options = new Options();
        //只获取图片的宽高
        if (requestWidth > 0 && requestHeight > 0) {
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            options.inSampleSize=calculateInSampleSize(options,requestWidth,requestHeight);
        }
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = config;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 获取图片Bitmap
     *
     * @param path         图片路径
     * @param inSampleSize 压缩比 宽高分别为:1/inSampleSize,大小变为:inSampleSize的平方分之1
     * @param config       默认Bitmap.Config.ARGB_8888
     * @return
     */
    public static Bitmap decodeFile(String path, int inSampleSize, Bitmap.Config config) {
        if (config == null) {
            config = Bitmap.Config.ARGB_8888;
        }
        Options options = new Options();

        options.inJustDecodeBounds = false;
        options.inPreferredConfig = config;
        options.inSampleSize = inSampleSize > 1 ? inSampleSize : 1;
        return BitmapFactory.decodeFile(path, options);
    }


    /**
     * 质量压缩Bitmap,
     * bitmap的宽高,占用内存不会变化,
     * 它是在保持像素的前提下改变图片的位深及透明度等，来达到压缩图片的目的
     * 适合去传递二进制的图片数据
     *
     * @param bitmap
     * @param quality 0-100 (0:最低质量,100:最高质量)
     * @return
     */
    public static byte[] compressQuality(Bitmap bitmap, int quality) {
        if(bitmap==null) return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean compress = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
        if (compress) {
            byte[] bytes = byteArrayOutputStream.toByteArray();
            try {
                byteArrayOutputStream.close();
                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }


    /**压缩bitmap到指定的大小
     * 质量压缩,请不在在主线程中使用
     * 压缩太厉害会导致图片模糊
     * @param bitmap
     * @param maxSize 压缩后byte[]的最大size 单位KB
     * @return
     */
    public static byte[] compressQualityTo(Bitmap bitmap, int maxSize) {
        if(bitmap==null) return null;
        maxSize = maxSize * 1024;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao);
        int length=bao.size();
        while (length> maxSize) {
            quality-=10;
            if(quality<0){
                break;
            }
            bao.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao);
            length=bao.size();
            Log.i(TAG, "compressTo:quality="+quality+",length=" +length);
        }
        byte[] bytes = bao.toByteArray();
        try {
            bao.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 根据path获取图片,并压缩到指定大小,一般用于图片上传
     * @param path 图片路径
     * @param requestWidth 需要的宽度 默认720
     * @param requestHeight 需要的高度 默认1280
     * @param maxSize 转换成byte[]后的最大大小 单位kb
     * @return
     */
    public static byte[] compressTo(String path, int requestWidth, int requestHeight,int maxSize){
        int width=requestWidth>0 ? requestWidth :720;
        int height=requestHeight>0 ? requestHeight :1280;
        //先获取到指定宽高的bitmap
        Bitmap bitmap = decodeFile(path, width, height, Bitmap.Config.ARGB_8888);
        //压缩
        return compressQualityTo(bitmap, maxSize);
    }

    /**
     * 根据path获取图片,并压缩到指定大小(默认宽高[720,1280]),一般用于图片上传
     * @param path 图片路径
     * @param maxSize 转换成byte[]后的最大大小 单位kb
     * @return
     */
    public static byte[] compressTo(String path,int maxSize){
        return compressTo(path,0,0, maxSize);
    }

    /**
     * 水平镜像
     */
    public static Bitmap getMirrorHorizontal(Bitmap bitmap){
        if(bitmap==null) return null;
        Matrix matrix=new Matrix();
        matrix.postScale(-1, 1); // 镜像水平翻转
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 垂直镜像
     */
    public static Bitmap getMirrorVertical(Bitmap bitmap){
        if(bitmap==null) return null;
        Matrix matrix=new Matrix();
        matrix.postScale(1, -1); // 镜像水平翻转
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    /**
     * 创建圆形图片
     *
     * @param source
     * @return
     */
    public static Bitmap createCircleBitmap(Bitmap source) {
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }

    /**
     * 把图片用Base64编码转换成字符串
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap){
        byte[] bytes = compressQuality(bitmap, 100);
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encode);
    }

    /**
     * 把字符串转化为bitmap
     * @param encodeString
     * @return
     */
    public static Bitmap stringToBitmap(String encodeString) {
        byte[] bytes = Base64.decode(encodeString, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 获取视频缩略图
     * @param path
     * @return
     */
    public static Bitmap createVideoThumbnail(String path){
        return ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
    }

    /**
     * 保存bitmap
     * @param bitmap
     * @param quality  quality 0-100 (0:最低质量,100:最高质量)
     * @param path
     */
    public static boolean keepBitmap(Bitmap bitmap,int quality,String path){
        String suffix = FileUtils.getSuffix(path);
        Bitmap.CompressFormat format=Bitmap.CompressFormat.JPEG;
        if(suffix.equalsIgnoreCase("png")){
            format=Bitmap.CompressFormat.PNG;
        }
        FileOutputStream fos=null;
        boolean compress=false;
        try {
            fos=new FileOutputStream(new File(path));
            compress = bitmap.compress(format, quality, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return compress;
    }
}
