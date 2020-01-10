package com.zhoug.common.annotation;

import android.support.annotation.IntDef;


import com.zhoug.common.content.FileType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * 文件类型
 * @Author HK-LJJ
 * @Date 2020/1/3
 * @Description
 */
@IntDef({FileType.UNKNOWN,FileType.IMAGE,FileType.VIDEO,FileType.AUDIO})
@Retention(SOURCE)
@Target({ElementType.FIELD,ElementType.PARAMETER})
public @interface Filetype {
}
