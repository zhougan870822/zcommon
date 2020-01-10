package com.zhoug.common.adapters;

import java.util.List;

/**
 * 创建日期:2017/9/6 on 16:07
 * 描述：use for{@link android.widget.ListView}
 * 作者: zhougan
 */
public abstract class BaseListViewAdapter<T> extends android.widget.BaseAdapter {
    private List<T> data;

    public BaseListViewAdapter() {

    }

    public BaseListViewAdapter(List<T> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data==null){
            return 0;
        }else{
            return data.size();
        }
    }

    @Override
    public T getItem(int i) {
        if(data==null){
            return null;
        }else{
            return data.get(i);
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
