package com.zhoug.common.adapters.recycler;


import com.zhoug.common.R;

/**
 * 默认的加载更多视图
 */
public class SimpleLoadMoreView extends LoadMoreView{

    @Override
    public int getLayoutId() {
        return R.layout.common_load_more_view;
    }

    @Override
    protected int getLoadingDefaultId() {
        return R.id.load_more_default;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_error;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_end;
    }

}
