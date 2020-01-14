package com.zhoug.common.content.interfaces;

/**
 * 加载进度的接口
 * @Author HK-LJJ
 * @Date 2019/12/20
 * @Description
 */
public interface ILoadProgress {
    /**
     * 显示加载窗口
     */
    void showLoading();

    /**
     * 关闭加载窗口
     */
    void cancelLoading();

}
