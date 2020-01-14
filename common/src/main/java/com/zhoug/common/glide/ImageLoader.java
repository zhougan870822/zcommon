package com.zhoug.common.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zhoug.common.utils.LogUtils;


/**
 * 图片加载器
 * @Author HK-LJJ
 * @Date 2019/12/11
 * @Description
 */
public class ImageLoader {
    private static final String TAG = ">>>ImageLoader";

    /**
     * 加载图片
     * @param context
     * @param imageView
     * @param imageUrl
     */
    public static void load(Context context, ImageView imageView, Object imageUrl){
        load(context, imageView, imageUrl,null ,null ,null );
    }


    /**
     * 加载圆形图片
     *
     * @param context
     * @param imageView
     * @param bitmap
     */
    public static void loadRoundedBitmapDrawable(Context context, ImageView imageView, @NonNull Bitmap bitmap) {
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        roundedBitmapDrawable.setAntiAlias(true);//抗锯齿
        roundedBitmapDrawable.setCircular(true);//设置为圆形
        imageView.setImageDrawable(roundedBitmapDrawable);
    }

    /**加载图片
     * @param context       上下文环境
     * @param imageView     图片组件
     * @param imageUrl      图片完整地址
     * @param placeholderId 加载占位图
     * @param errorId       加载错误图
     * @param progressBar   显示正在加载框
     */
    public static void load(Context context, ImageView imageView, Object imageUrl, @DrawableRes Integer placeholderId, @DrawableRes Integer errorId, ProgressBar progressBar) {
        if (imageUrl == null && errorId != null) {
            imageView.setImageResource(errorId);
        } else if (imageUrl != null) {
            RequestOptions requestOptions = new RequestOptions();
            if (placeholderId != null) {
                requestOptions = requestOptions.placeholder(placeholderId);
            }
            if (errorId != null) {
                requestOptions = requestOptions.error(errorId);
            }

            RequestBuilder<Drawable> requestBuilder = Glide.with(context).load(imageUrl).apply(requestOptions);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
                requestBuilder = requestBuilder.listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        return false;
                    }
                });
            }
            requestBuilder.into(imageView);
        }
    }



    /**
     * 加载圆形图片
     * @param context
     * @param imageView
     * @param imageUrl  图片完整地址
     * @param defaultImageRes 加载失败显示的默认图片
     */
    public static void loadCircle(Context context, ImageView imageView, Object imageUrl, @DrawableRes int defaultImageRes) {
        if (imageUrl != null) {
            RequestOptions options = RequestOptions.bitmapTransform(new CircleCrop());
            Glide.with(context).load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defaultImageRes);
                            loadRoundedBitmapDrawable(context, imageView, bitmap);
                            return true;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .apply(options)
                    .into(imageView);
        } else {
            LogUtils.d(TAG, "loadCircle: 加载默认图片");
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), defaultImageRes);
            if(bitmap!=null){
                loadRoundedBitmapDrawable(context, imageView, bitmap);
            }
        }
    }


}
