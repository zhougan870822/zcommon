package com.zhoug.common.permission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * 描述：android 权限申请管理
 * 当申明了WRITE_EXTERNAL_STORAGE权限时， READ_EXTERNAL_STORAGE权限会自动添加的
 * [注意坑,先只申请read权限,同意后,再申请write权限,如果用户拒绝了write权限,那么app闪退(例如荣耀v20)]
 * 所以最好同时申请
 * zhougan
 * 2019/8/2
 **/
public class PermissionManager {

    private static final String TAG = "PermissionManager";

    /**
     * 需要申请的权限集合
     */
    private String[] permissions;
    private Context mContext;
    /**
     * 权限申请回掉
     */
    private Callback callback;
    /**
     * 用户勾选了不在询问后的提示
     */
    private String notAsking = "必须要权限才能正常执行操作";
    /**
     * 默认提示notAsking
     */
    private boolean showNotAskingDialog = false;
    /**
     * 权限申请广播
     */
    private RequestPermissionBroadcastReceiver mReceiver;


    public PermissionManager() {

    }

    public PermissionManager(Context context) {
        this.mContext = context;
    }

    /**
     * 检查全部权限是否已经授权
     * @return
     */
    public boolean check() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //没有添加权限
            if (permissions == null || permissions.length == 0) {
                return true;
            } else {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 权限请求
     */
    public void request() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> shouldRequestPermissions = getShouldRequestPermissions();
            if (shouldRequestPermissions == null || shouldRequestPermissions.size() == 0) {
//                Log.i(TAG, "start: 都已授权");
                if (callback != null)
                    callback.onRequestPermissionsResult(true,null);
            } else {
                //需要请求权限
//                Log.i(TAG, "start: 需要请求权限: ="+mPermissions.length+"个");
                startPermissionActivity(shouldRequestPermissions);
            }
        } else {
            //6.0以下版本
            if (callback != null)
                callback.onRequestPermissionsResult(true,null);
        }

    }




    /**
     * 遍历permissions获取需要申请的权限
     */
    private ArrayList<String> getShouldRequestPermissions() {
        if (permissions == null || permissions.length == 0) {
            return null;
        }
        ArrayList<String> shouldRequestPermissions=new ArrayList<>();
        for (String permission:permissions) {
            //移除已经授权的
            if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                shouldRequestPermissions.add(permission);
            }
        }
        return shouldRequestPermissions;
    }

    /**
     * 启动权限申请页面
     *
     * @param permissions
     */
    private void startPermissionActivity(ArrayList<String> permissions) {
        //注册广播
        Log.i(TAG, "startPermissionActivity: 注册广播");
        mReceiver = new RequestPermissionBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        String action = UUID.randomUUID().toString();
        filter.addAction(action);
        mContext.registerReceiver(mReceiver, filter);

        int requestCode = new Random().nextInt(1000);

        Intent intent = new Intent(mContext, PermissionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putStringArrayListExtra("permissions", permissions);
        intent.putExtra("NotAsking", notAsking);
        intent.putExtra("showNotAskingDialig", showNotAskingDialog);
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("action", action);
        mContext.startActivity(intent);
    }


    //自定义广播接受权限申请结果
    public class RequestPermissionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (callback == null) {
                mContext.unregisterReceiver(mReceiver);
                return;
            }

            if (intent != null) {
                boolean success = intent.getBooleanExtra("success", false);
                ArrayList<String> denied = intent.getStringArrayListExtra("denied");
                callback.onRequestPermissionsResult(success,denied);
            } else {
                callback.onRequestPermissionsResult(false,null);
            }

            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }





    public PermissionManager addPermissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public Callback getCallback() {
        return callback;
    }

    public PermissionManager setCallback(Callback callback) {
        this.callback = callback;
        return this;
    }

    public PermissionManager setContext(Context mContext) {
        this.mContext = mContext;
        return this;
    }

    public PermissionManager setNotAsking(String notAsking) {
        this.notAsking = notAsking;
        return this;
    }

    public PermissionManager setshowNotAskingDialog(boolean showNotAskingDialog) {
        this.showNotAskingDialog = showNotAskingDialog;
        return this;
    }


    /**
     * 权限申请回掉
     */
    public interface Callback {
        /**
         *
         * @param success 只有全部通过才为true
         * @param denied success为false时存储未通过的权限
         */
        void onRequestPermissionsResult(boolean success, ArrayList<String> denied);
    }

}
