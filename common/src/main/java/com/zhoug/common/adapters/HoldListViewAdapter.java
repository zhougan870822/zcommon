package com.zhoug.common.adapters;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @Author zhoug
 * @Date 2019/9/20
 * @Description  use for {@link android.widget.ListView} 继承{@link BaseListViewAdapter}
 * 使用{@link ViewHolder}来构建ItemView
 */
public abstract class HoldListViewAdapter<T> extends BaseListViewAdapter<T> {

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=LayoutInflater.from(parent.getContext()).inflate(getLayoutId(),parent,false );
        }
        onBindData(new ViewHolder(convertView),getItem(position),position);

        return convertView;
    }

    public abstract @LayoutRes int getLayoutId();

    public abstract void onBindData(ViewHolder viewHolder,T itemData,int position);


    /**
     * 用来构建每个item
     */
    public static class ViewHolder{
        /**
         * 存储布局中的字组件item.findViewById集合
         */
        private SparseArray<View> mViews;
        /**
         * 根View
         */
        public View itemView;

        public ViewHolder(View itemView) {
            this.itemView=itemView;
            mViews=new SparseArray<>();
        }

        /**
         * 根据id获取布局文件中的子组件
         * @param id
         * @param <T>
         * @return
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getView(@IdRes int id){
            View view=mViews.get(id);
            if(view==null){
                view=itemView.findViewById(id);
                mViews.put(id,view);
            }
            return (T)view;
        }

        /**
         * 给TextView即TextView的子类设置文字
         * @param viewId 组件id
         * @param value
         */
        public ViewHolder setText(@IdRes int viewId,String value){
            TextView textView=getView(viewId);
            textView.setText(value);
            return this;
        }

        /**
         * 给TextView即TextView的子类设置文字
         * @param viewId 组件id
         * @param value
         */
        public ViewHolder setText(@IdRes int viewId,CharSequence value){
            TextView textView=getView(viewId);
            textView.setText(value);
            return this;
        }

        /**
         * 设置图片
         * @param viewId
         * @param drawable
         * @return
         */
        public ViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
            ImageView view = getView(viewId);
            view.setImageDrawable(drawable);
            return this;
        }

        /**
         * 设置图片
         * @param viewId
         * @param bitmap
         * @return
         */
        public ViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
            ImageView view = getView(viewId);
            view.setImageBitmap(bitmap);
            return this;
        }

        /**
         * 设置图片
         * @param viewId
         * @param imageResId
         * @return
         */
        public ViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
            ImageView view = getView(viewId);
            view.setImageResource(imageResId);
            return this;
        }

        /**
         * 设置选择状态
         * @param viewId
         * @param checked
         * @return
         */
        public ViewHolder setChecked(@IdRes int viewId, boolean checked) {
            View view = getView(viewId);
            if (view instanceof Checkable) {
                ((Checkable) view).setChecked(checked);
            }
            return this;
        }

        /**
         *设置此视图的启用状态
         * @param viewId
         * @param enable
         * @return
         */
        public ViewHolder setEnabled(@IdRes int viewId, boolean enable) {
            View view = getView(viewId);
            view.setEnabled(enable);
            return this;
        }

        /**
         *设置tag
         * @param viewId
         * @param key
         * @param tag
         * @return
         */
        public ViewHolder setTag(@IdRes int viewId, int key, Object tag) {
            View view = getView(viewId);
            view.setTag(key, tag);
            return this;
        }

        /**
         * 设置tag
         * @param viewId
         * @param tag
         * @return
         */
        public ViewHolder setTag(@IdRes int viewId, Object tag) {
            View view = getView(viewId);
            view.setTag(tag);
            return this;
        }

        /**
         * 设置item背景
         * @param color
         * @return
         */
        public ViewHolder setBackgroundColor(@ColorInt int color) {
            itemView.setBackgroundColor(color);
            return this;
        }

        /**
         * 设置item背景
         * @param resId
         * @return
         */
        public ViewHolder setBackgroundResource(@DrawableRes int resId) {
            itemView.setBackgroundResource(resId);
            return this;
        }

        /**
         * 设置item背景
         * @param background
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public ViewHolder setBackground(Drawable background) {
            itemView.setBackground(background);
            return this;
        }

        /**
         * 设置指定view的背景
         * @param viewId
         * @param color
         * @return
         */
        public ViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
            View view = getView(viewId);
            view.setBackgroundColor(color);
            return this;
        }

        /**
         * 设置指定view的背景
         * @param viewId
         * @param resId
         * @return
         */
        public ViewHolder setBackgroundResource(@IdRes int viewId,@DrawableRes int resId) {
            View view = getView(viewId);
            view.setBackgroundResource(resId);
            return this;
        }

        /**
         * 设置指定view的背景
         * @param viewId
         * @param background
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        public ViewHolder setBackground(@IdRes int viewId, Drawable background) {
            View view = getView(viewId);
            view.setBackground(background);
            return this;
        }

        /**
         * 设置文字颜色
         * @param viewId
         * @param color
         * @return
         */
        public ViewHolder setTextColor(@IdRes int viewId,@ColorInt int color){
            TextView textView = getView(viewId);
            textView.setTextColor(color);
            return this;
        }

        /**
         * 设置 setVisibility
         * @param viewId
         * @param visible One of {@link View#VISIBLE}, {@link  View#INVISIBLE}, or {@link  View#GONE}.
         * @return
         */
        public ViewHolder setVisibility(@IdRes int viewId, int visible) {
            View view = getView(viewId);
            view.setVisibility(visible);
            return this;
        }

        /**
         * 设置字体
         * @param viewId
         * @param typeface
         * @return
         */
        public ViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
            return this;
        }

        /**
         * 设置进度
         * @param viewId
         * @param progress
         * @return
         */
        public ViewHolder setProgress(@IdRes int viewId, int progress) {
            ProgressBar view = getView(viewId);
            view.setProgress(progress);
            return this;
        }

        /**
         * 设置进度
         * @param viewId
         * @param progress
         * @param max
         * @return
         */
        public ViewHolder setProgress(@IdRes int viewId, int progress, int max) {
            ProgressBar view = getView(viewId);
            view.setMax(max);
            view.setProgress(progress);
            return this;
        }
    }



}
