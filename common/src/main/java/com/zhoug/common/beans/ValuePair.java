package com.zhoug.common.beans;

import android.support.annotation.NonNull;

import java.io.Serializable;


/**
 * 封装ui上显示和实际值不一样的数据:例如:{"男","1"}{"女","2"}
 *
 * @Author HK-LJJ
 * @Date 2020/1/10
 * @Description
 */
public class ValuePair<T> implements Serializable {
    /**
     * 展示的文字
     */
    private @NonNull
    String label = "";
    /**
     * 实际值
     */
    private T value;

    public ValuePair() {
    }

    public ValuePair(@NonNull String label, T value) {
        this.label = label;
        this.value = value;
    }

    @NonNull
    public String getLabel() {
        return label;
    }

    public void setLabel(@NonNull String label) {
        this.label = label;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }



}
