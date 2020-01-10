package com.zhoug.common.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


/**
 * 网络连接状态改变实时监听广播
 * 7.0以后只能动态注册
 */
public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = "NetworkReceiver";

    //监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
    private static final String ACTION_CONNECTIVITY="android.net.conn.CONNECTIVITY_CHANGE";
    // 监听wifi的打开与关闭，与wifi的连接无关
    private static final String ACTION_WIFI_STATE_CHANGED="android.net.wifi.WIFI_STATE_CHANGED";

    /**
     * 网络变化监听接口
     */
    public OnNetworkChangeListener onNetworkChangeListener;

    /**
     * 注册实时监听网络变化的广播
     * @param context
     * @param onNetworkChangeListener 网络变化实时监听器
     * @return
     */
    public static NetworkReceiver registerReceiver(Context context, OnNetworkChangeListener onNetworkChangeListener){
        NetworkReceiver netWorkBroadcastReceiver=new NetworkReceiver();
        netWorkBroadcastReceiver.setOnNetworkChangeListener(onNetworkChangeListener);
        IntentFilter filter=new IntentFilter();
        filter.addAction(NetworkReceiver.ACTION_CONNECTIVITY);
        context.registerReceiver(netWorkBroadcastReceiver, filter);
        Log.d(TAG, "registerReceiver:注册NetWorkBroadcastReceiver");
        return netWorkBroadcastReceiver;
    }

    /**
     * 取消注册实时监听网络变化的广播
     * @param context
     * @param receiver
     */
    public static void unregisterReceiver(Context context, NetworkReceiver receiver){
        if(receiver!=null){
            context.unregisterReceiver(receiver);
            Log.d(TAG, "unregisterReceiver:取消注册NetWorkBroadcastReceiver");
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent!=null ){
            if(ACTION_CONNECTIVITY.equals(intent.getAction())){
                if(onNetworkChangeListener!=null){
                    onNetworkChangeListener.onChangeListener(NetworkUtils.getNetWorkState(context));
                }
            }
        }
    }

    /**
     * 网络变化监听接口
     * @return
     */
    public OnNetworkChangeListener getOnNetworkChangeListener() {
        return onNetworkChangeListener;
    }

    /**
     * 网络变化监听接口
     */
    public void setOnNetworkChangeListener(OnNetworkChangeListener onNetworkChangeListener) {
        this.onNetworkChangeListener = onNetworkChangeListener;
    }

    /**
     * 自定义网络变化监听接口
     */
    public interface OnNetworkChangeListener {
        /**
         *
         * @param state {@link NetworkUtils#STATE_MOBILE,#STATE_MOBILE,#STATE_MOBILE}
         */
        void onChangeListener(int state);
    }


}
