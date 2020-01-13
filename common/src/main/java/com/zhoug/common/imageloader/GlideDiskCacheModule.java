package com.zhoug.common.imageloader;

import android.content.Context;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.zhoug.common.Constant;
import com.zhoug.common.utils.FileUtils;

import java.io.File;

/**
 * 描述：glide图片缓存
 * zhougan
 * 2019/3/23
 **/
@GlideModule
public class GlideDiskCacheModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSize= 1024*1024*500;//500M
        boolean ok=false;
        //缓存在外部
        if(FileUtils.isSDCardExist()){
            File file = FileUtils.getExternalCacheFolder(context, Constant.GLIDE_CACHE);
            if(file!=null){
//                builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
                builder.setDefaultRequestOptions(RequestOptions.formatOf(DecodeFormat.PREFER_ARGB_8888));
                //设置缓存路径和大小
                builder.setDiskCache(new DiskLruCacheFactory(file.getAbsolutePath(),diskCacheSize));
                ok=true;
            }
        }
        //缓存在应用内部
        if(!ok){
            builder.setDiskCache(new InternalCacheDiskCacheFactory(context,Constant.GLIDE_CACHE,diskCacheSize));
        }
            //内存缓存
//        builder.setMemoryCache(new LruResourceCache());
//        builder.setBitmapPool(new LruBitmapPool());
    }

    /**
     * 禁止解析Manifest文件
     * 主要针对V3升级到v4的用户，可以提升初始化速度，避免一些潜在错误
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {

      /*  GlideRequest<Drawable> load = GlideApp.with(App.getApp()).load("");
        RequestBuilder<Drawable> load1 = Glide.with(App.getApp()).load("");*/

        return false;
    }
}
