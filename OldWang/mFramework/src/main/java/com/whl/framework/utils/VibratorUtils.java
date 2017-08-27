package com.whl.framework.utils;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

/**
 * Created by liwx on 2016/6/27.
 * 操作震动相关
 */
public class VibratorUtils {
    public static void Vibrate(final Context context, long milliseconds) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public static void Vibrate(final Context context, long[] pattern, boolean isRepeat) {
        Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(pattern, isRepeat ? 1 : -1);
    }
}
