package com.whl.framework.app;


import com.whl.framework.http.BaseGateway;
import com.whl.framework.http.NetworkManager;
import com.whl.framework.utils.Utils;

import org.litepal.LitePalApplication;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;


public class MApplication extends LitePalApplication {

    private static final String TAG = "MApplication";
    private static BaseGateway sBaseGateway;
    private static NetworkManager sNetworkManager;
    private static MApplication mInstance;
    private boolean needLockscreen;
    private File logCashDir;
    private String logFilePath;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");

    //-------LOG-------------
    public static final String PARENT_CACHE_DIR = "CssWeb";
    public static final int LOG_FILE_SIZE_LIMIT = 1024 * 1024 * 10;
    public static final int LOG_FILE_MAX_COUNT = 1;
    public static final String LOG_FILE_NAME = "CssLog%g.txt";
    //-------LOG-------------

    @Override
    public void onCreate() {
        super.onCreate();
        logCashDir = Utils.getCacheDirectory(this, PARENT_CACHE_DIR + "/log");
        logFilePath = logCashDir.getAbsolutePath() + "/" + "css_log";
        mInstance = this;

        //        MLog.d(TAG, "## onCreate");
//        initImageLoader();
    }

    public boolean isNeedLockscreen() {
        return needLockscreen;
    }

    public void setNeedLockscreen(boolean needLockscreen) {
        this.needLockscreen = needLockscreen;
    }

    public static MApplication getInstance() {
        return mInstance;
    }

    public BaseGateway getBaseGateway(String url) {
        if (sBaseGateway == null) {
            sBaseGateway = new BaseGateway(this, url);
        }
        return sBaseGateway;
    }

    public NetworkManager getNetworkManager() {
        if (sNetworkManager == null) {
            sNetworkManager = new NetworkManager(this);
        }
        return sNetworkManager;
    }


    public void writeLog(String tag, String msg) {
        if (!logCashDir.exists()) {
            boolean result = logCashDir.mkdir();
        }
        String logcontent = new StringBuffer().append("[").append(dateFormat.format(System.currentTimeMillis())).append(" ").append(tag).append("]").append(":").append(msg).toString();
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(logFilePath, "rw");
            raf.seek(raf.length());
            raf.writeBytes("\n" + new String(logcontent.getBytes("GBK"), "ISO8859_1"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //    public File getImgCacheDirectory(String parentCacheDir) {
    //        File appCacheDir = null;
    //        if ("mounted".equals(Environment.getExternalStorageState()) && Utils.hasExternalStoragePermission(getApplicationContext())) {
    //            appCacheDir = new File(getExternalFilesDir(""), parentCacheDir);
    //        }
    //        if (appCacheDir == null || !appCacheDir.exists() && !appCacheDir.mkdirs()) {
    //            appCacheDir = getCacheDir();
    //            // appCacheDir = context.getDir(fileName, Context.MODE_PRIVATE);
    //        }
    //        return appCacheDir;
    //    }

}
