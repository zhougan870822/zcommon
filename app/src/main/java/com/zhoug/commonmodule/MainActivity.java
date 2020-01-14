package com.zhoug.commonmodule;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageButton;

import com.zhoug.common.base.AbsActivity;
import com.zhoug.common.glide.ImageLoader;
import com.zhoug.common.permission.PermissionManager;
import com.zhoug.common.utils.IntentUtils;
import com.zhoug.common.utils.StatusBarUtils;
import com.zhoug.common.utils.UriUtils;

public class MainActivity extends AbsActivity {


    private ImageButton mBtnImageLoader;

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
        mBtnImageLoader = findViewById(R.id.btn_imageLoader);
    }

    private static final int pick_code = 102;

    @Override
    protected void addListener() {
        mBtnImageLoader.setOnClickListener(v -> {
            new PermissionManager(this)
                    .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .setCallback((success, denied) -> {
                        if (success) {
                            Intent pickImageIntent = IntentUtils.getPickImageIntent();
                            startActivityForResult(pickImageIntent, pick_code);
                        } else {
                            toastShort("fail");
                        }
                    })
                    .request();

        });
    }

    @Override
    protected void onCreateFinish(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==pick_code && resultCode==RESULT_OK && data!=null){
            String pathFromUri = UriUtils.getPathFromUri(this, data.getData());
            ImageLoader.load(this,mBtnImageLoader , pathFromUri);
        }
    }
}
