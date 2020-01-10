package com.zhoug.common.utils;

import java.math.BigDecimal;

/**
 * 描述：数学工具
 * zhougan
 * 2019/3/16
 **/
public class MathUtils {
    /**
     * 四舍五入 保留几位小数
     * @param value 值
     * @param xiaoshu 小数位数
     * @return
     */
    public static double getDecimal(double value,int xiaoshu){
        BigDecimal bd=new BigDecimal(value);
        BigDecimal bigDecimal = bd.setScale(xiaoshu, BigDecimal.ROUND_HALF_UP);
        return bigDecimal.doubleValue();//2位double
    }



}
