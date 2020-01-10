package com.zhoug.common.utils;

/**
 * 颜色工具
 */
public class ColorUtils {
    private static final String TAG = "ColorUtils";
    /**
     *判断颜色是否属于亮色调
     * @param color
     * @return
     */
    public static boolean isLightColor(int color) {
        return android.support.v4.graphics.ColorUtils.calculateLuminance(color) >= 0.5;
    }


}
