package com.whl.client.app;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

import com.whl.framework.utils.MLog;
import com.whl.client.BuildConfig;

/**
 * Created by lenovo on 2016/4/30.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Context mContext;

    public CrashHandler(Context context) {
        this.mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        MLog.e(TAG, "uncaughtException = ", ex);
        //        BizApplication.getInstance().exit();
        if (!handleException(ex)) {
            MLog.d(TAG, "## trace1");
        } else {
            MLog.d(TAG, "## trace2");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CrashManager.startSendLogService(mContext);
            //            if (!BuildConfig.LOG_DEBUG)
            BizApplication.getInstance().exit();
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {

            @Override
            public void run() {
                Looper.prepare();
                if (BuildConfig.LOG_DEBUG)
                    Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        CrashManager.collectDeviceInfo(mContext);
        // 保存日志文件
        CrashManager.saveCrashInfo2File(mContext, ex, TAG);
        return true;
    }

}
