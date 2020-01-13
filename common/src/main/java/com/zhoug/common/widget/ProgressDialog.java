package com.zhoug.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhoug.common.R;


/**
 *
 * 创建日期:2017/9/11 on 11:28
 * 描述：圆形进度dialog
 * 作者: zhougan
 */
public class ProgressDialog extends Dialog {
    private String title;
    private Drawable mDrawable;
    private ProgressBar progressBar;

    public ProgressDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//必须设置无标题 否则不会匹配内容
        setContentView(R.layout.common_dialog_progress);
        setCanceledOnTouchOutside(false);

        TextView tvTitle = findViewById(R.id.textView1);
        progressBar = findViewById(R.id.progressBar);
        if (mDrawable != null) {
            progressBar.setIndeterminateDrawable(mDrawable);
        }

//        imageView= findViewById(R.id.zg_image);
//        animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_circle);

        //设置标题
        if (tvTitle != null) {
            if (title != null) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            } else {
                tvTitle.setVisibility(View.GONE);
            }
        }

        //设置窗口宽高
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            attributes.width = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
            window.setBackgroundDrawableResource(R.drawable.common_transparent);//去掉背景
        }


    }


    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * 设置遮罩
     *
     * @param alpha true: 显示遮罩
     */
    public void setZezhao(boolean alpha) {
        if (getWindow() != null) {
            if (alpha) {
                getWindow().setDimAmount(0.4f);
            } else {
                getWindow().setDimAmount(0f);

            }
        }
    }

    /**
     * 设置遮罩
     *
     * @param alpha 遮罩透明度 alpha在0.0f到1.0f之间。1.0完全不透明，0.0f完全透明
     */
    public void setZezhao(float alpha) {
        if (getWindow() != null) {
            getWindow().setDimAmount(alpha);
        }
    }

    public void setIndeterminateDrawable(Drawable drawable) {
        mDrawable = drawable;
    }
}
