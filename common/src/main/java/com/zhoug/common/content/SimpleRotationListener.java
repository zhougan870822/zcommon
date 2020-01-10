package com.zhoug.common.content;

import android.content.Context;
import android.view.OrientationEventListener;

/**
 * 手机方向改变监听
 * enable() 开启监听
 * disable() 销毁监听(请在不用时调用)
 */
public abstract class SimpleRotationListener extends OrientationEventListener {
    //当前角度
    private int mPhoneRotation=0;

    public SimpleRotationListener(Context context) {
        super(context);
    }

    public SimpleRotationListener(Context context, int phoneRotation) {
        super(context);
        this.mPhoneRotation = phoneRotation;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        //计算手机当前方向的角度值
        int phoneDegree = 0;
        if (((orientation >= 0) && (orientation <= 45))|| (orientation > 315) &&(orientation<=360)) {
            phoneDegree = 0;
        } else if ((orientation > 45) && (orientation <= 135)) {
            phoneDegree = 90;
        } else if ((orientation > 135) && (orientation <= 225)) {
            phoneDegree = 180;
        } else if ((orientation > 225) && (orientation <= 315)) {
            phoneDegree = 270;
        }
        //手机方向改变了计算图片旋转角度
        if(mPhoneRotation!=phoneDegree){
            mPhoneRotation=phoneDegree;
            onPhoneRotationChange(mPhoneRotation);
        }
    }

    /**
     * 方向变化监听
     * @param phoneRotation 角度(逆时针) 0,90.180,270
     */
    public abstract void onPhoneRotationChange(int phoneRotation);


}
