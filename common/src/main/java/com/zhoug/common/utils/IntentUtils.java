package com.zhoug.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;


import com.zhoug.common.Constant;

import java.io.File;


public class IntentUtils {
    private static final String TAG = "IntentUtils";

    /**
     * 调用第三方app打开文件的Intent
     *
     * @param context
     * @param path
     * @param authority
     * @param minitype  "audio/*" "video/*"
     * @return
     */
    public static Intent getReadFileIntent(Context context, String path, String authority, String minitype) {
        if (minitype == null) {
            minitype = "*/*";
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授予临时权限别忘了
            Uri contentUri = FileProvider.getUriForFile(context, authority, new File(path));
            intent.setDataAndType(contentUri, minitype);
        } else {
            intent.setDataAndType(Uri.fromFile(new File(path)), minitype);
        }
        return intent;
    }



    /**
     * 调用第三方app打开文件的Intent
     *
     * @param context
     * @param path
     * @param authority
     * @param minitype  "audio/*" "video/*"
     * @return
     */
    public static Intent getWriteFileIntent(Context context, String path, String authority, String minitype) {
        if (minitype == null) {
            minitype = "*/*";
        }


        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//授予临时权限别忘了
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, authority, new File(path));
            intent.setDataAndType(contentUri, minitype);
        } else {
            intent.setDataAndType(Uri.fromFile(new File(path)), minitype);
        }
        return intent;
    }




    /**
     * 调用系统打电话,必须在manifest.xml中配置权限
     * <uses-permission android:name="android.permission.CALL_PHONE"/>
     * 且动态请求权限
     * eg:{
     * if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
     * IntentUtils.callPhone(MainActivity.this, "10086");
     * else
     * ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1001);
     * <p>
     * }
     *
     * @param context
     * @param phone
     */
    @SuppressLint("MissingPermission")
    public static void callPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用系统发短信
     *
     * @param phoneNumber
     * @param message
     */
    public static void sendSms(Context context, String phoneNumber, String message) {
//        if(PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (message != null) {
            intent.putExtra("sms_body", message);
        }
        context.startActivity(intent);
//        }
    }

    /**
     * 获取系统图片列表的Intent
     *
     * @return
     */
    public static Intent getPickImageIntent() {
        return getPickIntent("image/*");
    }

    /**
     * 获取系统视频列表的Intent
     *
     * @return
     */
    public static Intent getPickVideoIntent() {
        return getPickIntent("video/*");
    }

    /**
     * 获取系统音频列表的Intent
     *
     * @return
     */
    public static Intent getPickAudioIntent() {
        return getPickIntent("audio/*");
    }

    /**
     * 获取选择文件的Intent :
     * 直接显示文件列表
     * @param mimeType {图片:"image/*";视频:"video/*";音频:"audio/*";星/星}
     * @return
     */
    public static Intent getPickIntent(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(mimeType);
        return intent;
    }


    /**
     * 获取选择文件的Intent
     * 优点:功能强大可以切换目录,可以使用文件管理器
     * 缺点:可以选择非指定类型的文件,选择的结果需要类型判断,
     *
     * @param mimeType {图片:"image/*";视频:"video/*";音频:"audio/*";星/星}
     * @return
     */
    public static Intent getContentIntent(String mimeType) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(mimeType);
        return intent;
    }

    /**
     * 获取调用系统拍照/录制视频/录音的intent
     *
     * @param context
     * @param action    {@link MediaStore#ACTION_IMAGE_CAPTURE,MediaStore#ACTION_VIDEO_CAPTURE,MediaStore.Audio.Media#RECORD_SOUND_ACTION}
     * @param keepPath   拍摄的文件存储路径,有的可以不会使用(比如录音会保存到默认的地址,然后把地址uri发送给你)
     * @param authority FileProvider 的authority
     * @return
     */
    public static Intent getCaptureIntent(Context context, String action, String keepPath, String authority) {
        Intent intent = new Intent(action);
        if(keepPath!=null){
            //储存路径的uri
            Uri uri = UriUtils.getUriForFile(context, keepPath, authority);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION|Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        }
        return intent;
    }

    /**
     * 获取调用系统拍照的intent
     * @param context
     * @param keepPath
     * @param authority
     * @return
     */
    public static Intent getCaptureImageIntent(Context context,String keepPath, String authority) {
        return getCaptureIntent(context,MediaStore.ACTION_IMAGE_CAPTURE , keepPath, authority);
    }

    /**
     * 获取调用系统录制视频的intent
     * @param context
     * @param keepPath
     * @param authority
     * @return
     */
    public static Intent getCaptureVideoIntent(Context context,String keepPath, String authority) {
        return getCaptureIntent(context,MediaStore.ACTION_VIDEO_CAPTURE , keepPath, authority);
    }

    /**
     * 获取调用系统录音的intent
     * @param context
     * @param keepPath
     * @param authority
     * @return
     */
    public static Intent getCaptureAudioIntent(Context context,String keepPath, String authority) {
        return getCaptureIntent(context,MediaStore.Audio.Media.RECORD_SOUND_ACTION , keepPath, authority);
    }



}
