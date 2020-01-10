package com.zhoug.common.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

/**
 * 短信工具
 */
public class SmsUtils {
    private static final String TAG = "SmsUtils";

    /**
     * 发送短信是否成功的requestCode
     */
    private static final int SEND_RESULT_CODE=101;
    /**
     *  发送的短信是否被对方成功接受的requestCode
     */
    private static final int SEND_ACCEPT_CODE=102;



    /**
     * 调用系统短信界面发送短信
     * @param phoneNumber
     * @param message
     */
    public static void sendSmsUseSystemUi(Context context, String phoneNumber, String message) {
        IntentUtils.sendSms(context, phoneNumber, message);
    }

    /**
     * 直接发送短信,后台悄悄调用,现在大部分手机都会提示用户密码验证等操作防止恶意发送短信
     * 可以监控发送状态和对方接收状态
     * 如果需要监听发送和对方接受与否请注册广播SendSmsBroadcastReceiver
     * {@link #registerReceiver(Context, OnSendSmsListener) }
     * {@link #unregisterReceiver(Context, SendSmsBroadcastReceiver)}
     * @param phoneNumber
     * @param message
     */
    public static void sendSms(Context context,String phoneNumber,String message ){
        SmsManager smsManager=SmsManager.getDefault();
        //拆分内容，有长度限制
        ArrayList<String> divideMessage = smsManager.divideMessage(message);
        for(String msg : divideMessage){
            //参数1:目标电话
            // 参数2:发送者的电话,
            // 参数3:短信内容
            // 参数4:处理发送是否成功,
            // 参数5:处理用户是否接受成功
            smsManager.sendTextMessage(phoneNumber, null, msg, getSendResultPendingIntent(context), getSendAcceptPendingIntent(context));
        }
    }

    private static PendingIntent getSendResultPendingIntent(Context context){
        Intent intent=new Intent(SendSmsBroadcastReceiver.ACTION_SEND_SMS_RESULT);
        return PendingIntent.getBroadcast(context, SEND_RESULT_CODE, intent, 0);

    }

    private static PendingIntent getSendAcceptPendingIntent(Context context){
        Intent intent=new Intent(SendSmsBroadcastReceiver.ACTION_SEND_SMS_ACCEPT);
        return PendingIntent.getBroadcast(context, SEND_ACCEPT_CODE, intent, 0);

    }

    /**
     * 注册监听发送短信成功与否的广播
     * @param context
     * @return
     */
    public static SendSmsBroadcastReceiver registerReceiver(Context context,OnSendSmsListener onSendSmsListener){
        SendSmsBroadcastReceiver receiver=new SendSmsBroadcastReceiver();
        receiver.setOnSendSmsListener(onSendSmsListener);
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(SendSmsBroadcastReceiver.ACTION_SEND_SMS_RESULT);
        intentFilter.addAction(SendSmsBroadcastReceiver.ACTION_SEND_SMS_ACCEPT);
        context.registerReceiver(receiver,intentFilter );
        Log.d(TAG, "registerReceiver:注册SendSmsBroadcastReceiver");
        return receiver;
    }

    /**
     * 取消注册监听发送短信成功与否的广播
     * @param context
     * @param receiver
     */
    public static void unregisterReceiver(Context context,SendSmsBroadcastReceiver receiver){
        if(receiver!=null){
            context.unregisterReceiver(receiver);
            Log.d(TAG, "unregisterReceiver:取消注册SendSmsBroadcastReceiver");
        }
    }

    /**
     * 发送短信的广播,处理是否发送成功和对方是否接受成功
     */
    public static class SendSmsBroadcastReceiver extends BroadcastReceiver{
        private OnSendSmsListener onSendSmsListener;
        /**
         * 发送短信是否成功的action
         */
        private static final String ACTION_SEND_SMS_RESULT="action_send_sms_result";
        /**
         * 发送的短信是否被对方成功接受的action
         */
        private static final String ACTION_SEND_SMS_ACCEPT="action_send_sms_accept";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null){
                String action=intent.getAction();
                int resultCode = getResultCode();
                Log.d(TAG, "onReceive:action="+action);
                if(ACTION_SEND_SMS_RESULT.equals(action)){
                    //发送短信是否成功
                    if(resultCode==Activity.RESULT_OK){
                        Log.d(TAG, "onReceive:发送短信成功");
                        if(onSendSmsListener!=null){
                            onSendSmsListener.onSendResult(true);
                        }
                    }else{
                        Log.d(TAG, "onReceive:发送短信失败");
                        if(onSendSmsListener!=null){
                            onSendSmsListener.onSendResult(false);
                        }
                    }



                }else if(ACTION_SEND_SMS_ACCEPT.equals(action)){
                    //发送的短信是否被对方成功接受
                    if(resultCode==Activity.RESULT_OK){
                        Log.d(TAG, "onReceive:对方成功接受");
                        if(onSendSmsListener!=null){
                            onSendSmsListener.onAccept(true);
                        }
                    }else{
                        Log.d(TAG, "onReceive:对方接受失败");
                        if(onSendSmsListener!=null){
                            onSendSmsListener.onAccept(false);
                        }
                    }
                }
            }
        }

        public OnSendSmsListener getOnSendSmsListener() {
            return onSendSmsListener;
        }

        public void setOnSendSmsListener(OnSendSmsListener onSendSmsListener) {
            this.onSendSmsListener = onSendSmsListener;
        }
    }

    /**
     * 发送短信回掉
     */
    public  interface OnSendSmsListener{
        void onSendResult(boolean success);
        void onAccept(boolean success);
    }


}
