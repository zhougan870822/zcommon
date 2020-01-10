package com.zhoug.common.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * 音量控制工具
 */
public class AudioUtils {

    private static AudioUtils singleInstance;
    private static AudioManager audioManager;

    private String readme="看下面";

    /**
     *      streamType:
     *      AudioManager.STREAM_ALARM 警报
     *      AudioManager.STREAM_MUSIC 媒体音量
     *      AudioManager.STREAM_VOICE_CALL 通话
     *      AudioManager.STREAM_RING 铃声
     *      AudioManager.STREAM_NOTIFICATION 窗口顶部状态栏Notification
     *      AudioManager.STREAM_SYSTEM 系统
     *       >>>>>>>>>>>>>>>
     *      direction:
     *       AudioManager.ADJUST_LOWER 减少
     *       AudioManager.ADJUST_RAISE 增大
     *       AudioManager.ADJUST_SAME 保持不变
     *       AudioManager.ADJUST_MUTE 静音
     *       AudioManager.ADJUST_UNMUTE 取消静音
     *       AudioManager.ADJUST_TOGGLE_MUTE 改变静音状态(静音则改为非静音,非静音则改为静音)
     *      >>>>>>>>>>>>>>>>>>>
     *       flags:
     *      AudioManager.FLAG_SHOW_UI  调整时显示音量条,就是按音量键出现的界面
     *      AudioManager.FLAG_ALLOW_RINGER_MODES  在更改音量时是否尽可能包括振铃器模式选项(调整到最小时进入震动模式)
     *      AudioManager.FLAG_PLAY_SOUND  调整音量时播放声音
     *      AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE  无振动无声音(调整时如果正在播放音乐则停止播放)
     *      AudioManager.FLAG_VIBRATE  震动(调整到震动时震动)
     */



    private  AudioUtils(Context context){
         audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static AudioUtils instance(Context context){
        if(singleInstance==null){
            synchronized (AudioUtils.class){
                if(singleInstance==null){
                    singleInstance=new AudioUtils(context);
                }
            }
        }
        return singleInstance;
    }

    /**
     * 渐进式调节音量,每次增减1
     * @param streamType {@link #readme}
     * @param direction
     * @param flags
     */
    public void adjustStreamVolume(int streamType, int direction, int flags){
        audioManager.adjustStreamVolume(streamType,direction ,flags );
    }

    /**
     * 直接设置音量
     * @param streamType {@link #readme}
     * @param index 音量 [0-{@link AudioManager#getStreamMaxVolume(int)}之间]
     * @param flags
     */
    public void setStreamVolume (int streamType, int index, int flags){
        //获取最大音量
        int maxVolume = audioManager.getStreamMaxVolume(streamType);
        //现在的音量
//        int streamVolume = audioManager.getStreamVolume(streamType);
        if(index<0){
            index=0;
        }else if(index>maxVolume){
            index=maxVolume;
        }
        audioManager.setStreamVolume(streamType, index, flags);
    }



}
