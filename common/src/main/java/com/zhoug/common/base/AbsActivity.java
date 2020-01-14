package com.zhoug.common.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.zhoug.common.content.interfaces.ILoadProgress;
import com.zhoug.common.utils.StatusBarUtils;
import com.zhoug.common.utils.ToastUtils;
import com.zhoug.common.widget.ProgressDialog;


/**
 * 所有activity的基类
 * @Author HK-LJJ
 * @Date 2019/11/28
 * @Description
 */
public abstract class AbsActivity extends AppCompatActivity implements ILoadProgress {
    protected static final String TAG = ">>>AbsActivity";
    protected ProgressDialog loadDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOrientation();
        beforeLayout();
        setContentView(getLayoutResID());
        setStatusBar();
        findViews();
        addListener();
        onCreateFinish(savedInstanceState);
    }
    //设置横竖屏
    protected void setOrientation(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
    }

    /**
     * 状态栏设置
     */
    protected void setStatusBar() {
        //状态栏透明,且布局占用状态栏
        StatusBarUtils.transparentStatusBar(this);
        //不给状态栏预留位置
        StatusBarUtils.setFitsSystemWindows(this, false);
        //添加状态栏占位
        StatusBarUtils.addStatusPlaceView(this);
    }


    /**
     * 设置布局文件之前执行
     */
    protected void beforeLayout() {

    }

    /**
     * 布局文件id
     *
     * @return
     */
    protected abstract @LayoutRes
    int getLayoutResID();

    /**
     * view初始化
     */
    protected abstract void findViews();

    /**
     * 添加监听
     */
    protected abstract void addListener();

    /**
     * onCreate 方法最后调用
     *
     * @param savedInstanceState
     */
    protected abstract void onCreateFinish(@Nullable Bundle savedInstanceState);




    //以下是吐司消息
    protected void toastShort(Object msg) {
        ToastUtils.toastShort(getApplicationContext(), msg);
    }

    protected void toastLong(Object msg) {
        ToastUtils.toastLong(getApplicationContext(), msg);
    }

    protected void toastShortCenter(Object msg) {
        ToastUtils.toastShortCenter(getApplicationContext(), msg);
    }

    protected void toastLongCenter(Object msg) {
        ToastUtils.toastLongCenter(getApplicationContext(), msg);
    }

    /**
     * 显示正在加载框
     */
    @Override
    public void showLoading(){
        if (loadDialog == null) {
            loadDialog = new ProgressDialog(this);
            loadDialog.setCancelable(true);
            loadDialog.setCanceledOnTouchOutside(false);
            loadDialog.setZezhao(true);
        }
        if (!loadDialog.isShowing()) {
            loadDialog.show();
        }
    }

    /**
     * 隐藏正在加载框
     */
    @Override
    public void cancelLoading(){
        if (loadDialog != null && loadDialog.isShowing()) {
            loadDialog.cancel();
        }
    }

}
