package com.zhoug.common.content.interfaces;

/**
 * 回掉
 * @param <T>
 */
public interface Callback<T> {
    /**
     * 成功
     */
    int CODE_SUCESS=100;
    /**
     * 失败
     */
    int CODE_FAILURE=101;
    /**
     * 取消
     */
    int CODE_CANCEL = 103;

    void onResult(int code, T data);


}
