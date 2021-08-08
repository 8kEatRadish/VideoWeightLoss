package com.eatradish.videoweightloss;

import android.util.Log;
import androidx.annotation.IntDef;

import com.eatradish.videoweightloss.utils.ThreadPoolUtil;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author suihongwei
 */
public class VideoWeightLoss {
    static {
        System.loadLibrary("native-lib");
    }
    private final static String TAG = "VideoWeightLoss";
    private final static int STATE_INIT = 0;
    private final static int STATE_RUNNING = 1;
    private final static int STATE_FINISH = 2;
    private final static int STATE_ERROR = 3;
    private static OnHandleListener onHandleListener;

    private final static String MSG_STARTS_WITH = "silence";
    private final static int PROGRESS_MAX = 100;

    public static void execute(final String[] commands, final OnHandleListener onHandleListener) {
        VideoWeightLoss.onHandleListener = onHandleListener;
        ThreadPoolUtil.INSTANCE.executeSingleThreadPool(() -> {
            if (onHandleListener != null) {
                onHandleListener.onBegin();
            }
            int result = handle(commands);
            if (onHandleListener != null) {
                onHandleListener.onEnd(result, "");
            }
            VideoWeightLoss.onHandleListener = null;
        });
    }


    public static void cancelTask(boolean cancel) {
        cancelTaskJni(cancel ? 1 : 0);
    }

    private native static void cancelTaskJni(int i);

    @Documented
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_INIT, STATE_RUNNING, STATE_FINISH, STATE_ERROR})
    public @interface FFmpegState {
    }

    private static native int handle(String[] commands);

    private static void onProgressCallback(int position, int duration, @FFmpegState int state) {
        Log.e(TAG, "onProgress position=" + position + "--duration=" + duration + "--state=" + state);
        if (position > duration && duration > 0) {
            return;
        }
        if (onHandleListener != null) {
            if (position > 0 && duration > 0) {
                int progress = position * 100 / duration;
                if (progress < PROGRESS_MAX || state == STATE_FINISH || state == STATE_ERROR) {
                    onHandleListener.onProgress(progress, duration);
                }
            } else {
                onHandleListener.onProgress(position, duration);
            }
        }
    }

    private static void onMsgCallback(String msg){
        if (msg != null && !msg.isEmpty()) {
            Log.e(TAG, "from native msg=" + msg);
            if (msg.startsWith(MSG_STARTS_WITH) && onHandleListener != null) {
                onHandleListener.onMsg(msg);
            }
        }
    }

    interface OnHandleListener {
        /**
         * 命令执行开始时回调
         */
        void onBegin();

        /**
         * 执行信息回调
         * @param msg 命令执行信息
         */
        void onMsg(String msg);

        /**
         * 执行进度
         * @param position 执行进度
         * @param duration 总进度
         */
        void onProgress(int position, int duration);

        /**
         * 命令执行完成回调
         * @param resultCode 执行结果
         * @param resultMsg 执行结果信息
         */
        void onEnd(int resultCode, String resultMsg);
    }
}
