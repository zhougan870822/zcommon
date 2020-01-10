package com.zhoug.common.adapters.recycler;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * 加载更多视图
 */
public abstract class LoadMoreView {
    public static final int STATUS_GONE= 0;//隐藏
    public static final int STATUS_DEFAULT = 1;//显示上拉加载
    public static final int STATUS_LOADING = 2;//显示正在加载
    public static final int STATUS_FAIL = 3;//显示错误
    public static final int STATUS_END = 4;//显示没有更多
    public static final int STATUS_COMPLETE = 5;//显示加载完成

    private int mLoadMoreStatus = STATUS_GONE;

    private Drawable background;
    private int backgroundColor;
    private int backgroundResId;



    public int getLoadMoreStatus() {
        return mLoadMoreStatus;
    }

    public void setLoadMoreStatus(int loadMoreStatus) {
        this.mLoadMoreStatus = loadMoreStatus;
    }

    public void convert(BaseViewHolder holder){
        if (background != null) {
            holder.setBackground(background);
        } else if (backgroundColor != 0) {
            holder.setBackgroundColor(backgroundColor);
        } else if (backgroundResId != 0) {
            holder.setBackgroundResource(backgroundResId);
        }

        switch (mLoadMoreStatus){
            case STATUS_GONE:
                holder.setVisibility(getLoadingDefaultId(),View.GONE);
                holder.setVisibility(getLoadingViewId(),View.GONE);
                holder.setVisibility(getLoadEndViewId(),View.GONE);
                holder.setVisibility(getLoadFailViewId(),View.GONE);

                break;
            case STATUS_DEFAULT:
                holder.setVisibility(getLoadingDefaultId(),View.VISIBLE);
                holder.setVisibility(getLoadingViewId(),View.GONE);
                holder.setVisibility(getLoadEndViewId(),View.GONE);
                holder.setVisibility(getLoadFailViewId(),View.GONE);

                break;
            case STATUS_COMPLETE:
                holder.setVisibility(getLoadingDefaultId(),View.VISIBLE);
                holder.setVisibility(getLoadingViewId(),View.GONE);
                holder.setVisibility(getLoadEndViewId(),View.GONE);
                holder.setVisibility(getLoadFailViewId(),View.GONE);

                break;
            case STATUS_LOADING:
                holder.setVisibility(getLoadingDefaultId(),View.GONE);
                holder.setVisibility(getLoadingViewId(),View.VISIBLE);
                holder.setVisibility(getLoadEndViewId(),View.GONE);
                holder.setVisibility(getLoadFailViewId(),View.GONE);
                break;
            case STATUS_END:
                holder.setVisibility(getLoadingDefaultId(),View.GONE);
                holder.setVisibility(getLoadingViewId(),View.GONE);
                holder.setVisibility(getLoadEndViewId(),View.VISIBLE);
                holder.setVisibility(getLoadFailViewId(),View.GONE);
                break;
            case STATUS_FAIL:
                holder.setVisibility(getLoadingDefaultId(),View.GONE);
                holder.setVisibility(getLoadingViewId(),View.GONE);
                holder.setVisibility(getLoadEndViewId(),View.GONE);
                holder.setVisibility(getLoadFailViewId(),View.VISIBLE);
                break;
        }
    }

    /**
     *
     *
     * @return 布局文件id
     */
    public abstract @LayoutRes int getLayoutId();

    /**
     *
     *正在加载
     * @return 布局文件中正在加载的View的id
     */
    protected abstract @IdRes int getLoadingDefaultId();

    /**
     *
     *正在加载
     * @return 布局文件中正在加载的View的id
     */
    protected abstract @IdRes int getLoadingViewId();

    /**
     *加载失败
     * @return 布局文件中加载失败的View的id
     */
    protected abstract @IdRes int getLoadFailViewId();

    /**
     *没有更多
     * @return 布局文件中没有更多了的View的id
     */
    protected abstract @IdRes int getLoadEndViewId();


    public void setBackground(Drawable background) {
        this.background = background;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setBackgroundResource(int resId) {
        this.backgroundResId = resId;
    }
}
