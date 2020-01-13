package com.zhoug.common.permission;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zhoug.common.R;

import java.util.ArrayList;

/**
 * 权限申请页面
 */
public class PermissionActivity extends AppCompatActivity {
    private static final String TAG = "PermissionActivity";
    private ArrayList<String> permissions;
    private String notAsking;
    private boolean showNotAskingDialig;
    private int requestCode;
    private String action;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        Intent intent = getIntent();
        if (intent != null) {
            permissions = intent.getStringArrayListExtra("permissions");
            notAsking = intent.getStringExtra("NotAsking");
            showNotAskingDialig = intent.getBooleanExtra("showNotAskingDialig", false);
            requestCode = intent.getIntExtra("requestCode", 1638);
            action = intent.getStringExtra("action");
        }


        //申请权限
        String[] strings = permissions.toArray(new String[0]);
        for (String p : strings) {
            Log.d(TAG, "init: 需要申请的权限:" + p);
        }
        ActivityCompat.requestPermissions(this, strings, requestCode);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.requestCode) {
            //存储未申请成功的权限
            ArrayList<String> denied = new ArrayList<>();
            boolean result = false;//全部权限是否授权
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
                        denied.add(permissions[i]);
                    }
                }
            }
            //有权限没通过
            if (denied.size() == 0 && grantResults.length > 0) {
                result = true;
            }

            boolean show = false;//是否显示提示
            //请求失败,要显示提示
            if (!result && showNotAskingDialig) {
                //全部权限都点击了不在询问才显示
                Log.i(TAG, "show=true: ");
                show = true;
                for (String per : permissions) {
                    //只要有一个权限没勾选不再询问,就不显示
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, per)) {
                        show = false;
                        break;
                    }
                }
            }

            if (show) {
                showNotAskingDialog(result, denied);
            } else {
                //发送广播通知权限申请完成
                sendBroadcast(result, denied);
                finish();
            }

        }
    }

    /**
     * 用户点击了不再询问后显示的提示
     */
    private void showNotAskingDialog(final boolean result, final ArrayList<String> denied) {
        Log.i(TAG, "showNotAskingDialog: ");
        AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.permission_dialog)
                .setMessage(notAsking)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_SETTINGS);
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sendBroadcast(result, denied);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sendBroadcast(result, denied);
                        finish();
                    }
                })
                .create();

        alertDialog.setCancelable(false);
        alertDialog.show();


    }

    private void sendBroadcast(boolean result, ArrayList<String> denied) {
        //发送广播通知权限申请完成
        Intent broadcastIntent = new Intent(action);
        broadcastIntent.putExtra("success", result);
        broadcastIntent.putStringArrayListExtra("denied", denied);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onBackPressed() {
        sendBroadcast(false, null);
        super.onBackPressed();

    }
}
