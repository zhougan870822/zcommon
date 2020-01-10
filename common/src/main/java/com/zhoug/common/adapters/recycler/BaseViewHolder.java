package com.zhoug.common.adapters.recycler;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedHashSet;

/**
 *
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    /**
     * 存储布局中的字组件item.findViewById集合
     */
    private SparseArray<View> mViews;
    /**
     * 储存item中需要绑定单击事件的子组件
     */
    private final LinkedHashSet<Integer> childClickViewIds;
    /**储存item中需要绑定长按事件的子组件
     *
     */
    private final LinkedHashSet<Integer> childLongClickViewIds;

    /**
     * 关联的BaseRecyclerViewAdapter
     */
    private BaseRecyclerViewAdapter adapter;


    public BaseViewHolder(View itemView) {
        super(itemView);
        mViews=new SparseArray<>();
        this.childClickViewIds = new LinkedHashSet<>();
        this.childLongClickViewIds = new LinkedHashSet<>();
    }

    public BaseViewHolder(View itemView,BaseRecyclerViewAdapter adapter) {
        super(itemView);
        mViews=new SparseArray<>();
        this.childClickViewIds = new LinkedHashSet<>();
        this.childLongClickViewIds = new LinkedHashSet<>();
        this.adapter=adapter;
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
    public BaseViewHolder setText(@IdRes int viewId,String value){
        TextView textView=getView(viewId);
        textView.setText(value);
        return this;
    }

    /**
     * 给TextView即TextView的子类设置文字
     * @param viewId 组件id
     * @param value
     */
    public BaseViewHolder setText(@IdRes int viewId,CharSequence value){
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
    public BaseViewHolder setImageDrawable(@IdRes int viewId, Drawable drawable) {
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
    public BaseViewHolder setImageBitmap(@IdRes int viewId, Bitmap bitmap) {
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
    public BaseViewHolder setImageResource(@IdRes int viewId, @DrawableRes int imageResId) {
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
    public BaseViewHolder setChecked(@IdRes int viewId, boolean checked) {
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
    public BaseViewHolder setEnabled(@IdRes int viewId, boolean enable) {
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
    public BaseViewHolder setTag(@IdRes int viewId, int key, Object tag) {
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
    public BaseViewHolder setTag(@IdRes int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * 设置item背景
     * @param color
     * @return
     */
    public BaseViewHolder setBackgroundColor(@ColorInt int color) {
        itemView.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置item背景
     * @param resId
     * @return
     */
    public BaseViewHolder setBackgroundResource(@DrawableRes int resId) {
        itemView.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置item背景
     * @param background
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public BaseViewHolder setBackground(Drawable background) {
        itemView.setBackground(background);
        return this;
    }

    /**
     * 设置指定view的背景
     * @param viewId
     * @param color
     * @return
     */
    public BaseViewHolder setBackgroundColor(@IdRes int viewId, @ColorInt int color) {
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
    public BaseViewHolder setBackgroundResource(@IdRes int viewId,@DrawableRes int resId) {
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
    public BaseViewHolder setBackground(@IdRes int viewId, Drawable background) {
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
    public BaseViewHolder setTextColor(@IdRes int viewId,@ColorInt int color){
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
    public BaseViewHolder setVisibility(@IdRes int viewId, int visible) {
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
    public BaseViewHolder setTypeface(@IdRes int viewId, Typeface typeface) {
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
    public BaseViewHolder setProgress(@IdRes int viewId, int progress) {
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
    public BaseViewHolder setProgress(@IdRes int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * 绑定item中给定id的子组件的单击事件,并把id加入集合childClickViewIds
     * @param viewIds
     * @return
     */
    public BaseViewHolder addOnClickListener(@IdRes final int ...viewIds) {
        for (int viewId : viewIds) {
            childClickViewIds.add(viewId);
            final View view = getView(viewId);
            if (view != null) {
                if (!view.isClickable()) {
                    view.setClickable(true);
                }
                view.setOnClickListener(v -> {
                    if (adapter.getOnItemChildClickListener() != null) {
                        int position = getAdapterPosition();
                        if (position == RecyclerView.NO_POSITION) {
                            return;
                        }

                        adapter.getOnItemChildClickListener().onItemChildClick(adapter, v, position);
                    }
                });
            }
        }

        return this;
    }

    /**
     * 绑定item中给定id的子组件的长按事件,并把id加入集合childLongClickViewIds
     * @param viewIds
     * @return
     */
    public BaseViewHolder addOnLongClickListener(@IdRes final int ... viewIds) {
        for (int viewId : viewIds) {
            childLongClickViewIds.add(viewId);
            final View view = getView(viewId);
            if (view != null) {
                if (!view.isLongClickable()) {
                    view.setLongClickable(true);
                }
                view.setOnLongClickListener(v -> {
                    if (adapter.getOnItemChildLongClickListener() == null) {
                        return false;
                    }
                    int position = getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return false;
                    }
                    return adapter.getOnItemChildLongClickListener().onItemChildLongClick(adapter, v, position);
                });
            }
        }
        return this;
    }

    public BaseRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
