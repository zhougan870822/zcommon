package com.zhoug.commonmodule;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zhoug.common.base.AbsActivity;
import com.zhoug.common.permission.PermissionManager;
import com.zhoug.common.utils.StatusBarUtils;

public class MainActivity extends AbsActivity {


    protected void setStatusBar() {
//        super.setStatusBar();
        //状态栏透明,且布局占用状态栏
        StatusBarUtils.transparentStatusBar(this);
        //不给状态栏预留位置
        StatusBarUtils.setFitsSystemWindows(this, true);
        //添加状态栏占位
//        StatusBarUtils.addStatusPlaceView(this);
    }
    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {

    }

    @Override
    protected void addListener() {

    }

    @Override
    protected void onCreateFinish(@Nullable Bundle savedInstanceState) {
        new PermissionManager(this)
                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .setCallback((success, denied) -> {
                    if (success) {
                        toastShort("ok");
                    } else {
                        toastShort("fail");
                    }
                })
                .request();
    }

}
