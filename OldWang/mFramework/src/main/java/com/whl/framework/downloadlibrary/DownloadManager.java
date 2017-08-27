package com.whl.framework.downloadlibrary;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.whl.framework.downloadlibrary.architecture.CssDownloader;
import com.whl.framework.downloadlibrary.architecture.DownloadResponse;
import com.whl.framework.downloadlibrary.architecture.DownloadStatusDelivery;
import com.whl.framework.downloadlibrary.core.DownloadResponseImpl;
import com.whl.framework.downloadlibrary.core.DownloadStatusDeliveryImpl;
import com.whl.framework.downloadlibrary.core.DownloaderImpl;
import com.whl.framework.downloadlibrary.db.DataBaseManager;
import com.whl.framework.downloadlibrary.db.ThreadInfo;
import com.whl.framework.utils.MLog;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Aspsine on 2015/7/14.
 */
public class DownloadManager implements CssDownloader.OnDownloaderDestroyedListener {

    public static final String TAG = "DownloadManager";

    /**
     * singleton of DownloadManager
     */
    private static DownloadManager sDownloadManager;

    private DataBaseManager mDBManager;

    private Map<String, CssDownloader> mDownloaderMap;

    private DownloadConfiguration mConfig;

    private ExecutorService mExecutorService;

    private DownloadStatusDelivery mDelivery;

    public static DownloadManager getInstance() {
        if (sDownloadManager == null) {
            synchronized (DownloadManager.class) {
                sDownloadManager = new DownloadManager();
            }
        }
        return sDownloadManager;
    }

    /**
     * private construction
     */
    private DownloadManager() {
        mDownloaderMap = new LinkedHashMap<String, CssDownloader>();
    }

    public void init(Context context) {
        init(context, new DownloadConfiguration());
    }

    public void init(Context context, DownloadConfiguration config) {
        if (config.getThreadNum() > config.getMaxThreadNum()) {
            throw new IllegalArgumentException("thread num must < max thread num");
        }
        mConfig = config;
        mDBManager = DataBaseManager.getInstance(context);
        mExecutorService = Executors.newFixedThreadPool(mConfig.getMaxThreadNum());
        mDelivery = new DownloadStatusDeliveryImpl(new Handler(Looper.getMainLooper()));
    }

    @Override
    public void onDestroyed(String key, CssDownloader downloader) {
        if (mDownloaderMap.containsKey(key)) {
            mDownloaderMap.remove(key);
        }
    }

    public void download(DownloadRequest request, String tag, CallBack callBack) {
        final String key = createKey(tag);
        if (check(key)) {
            DownloadResponse response = new DownloadResponseImpl(mDelivery, callBack);
            CssDownloader downloader = new DownloaderImpl(request, response, mExecutorService, mDBManager, key, mConfig, this);
            mDownloaderMap.put(key, downloader);
            downloader.start();
        }
    }

    public void pause(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            CssDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
            mDownloaderMap.remove(key);
        }
    }

    public void cancel(String tag) {
        String key = createKey(tag);
        if (mDownloaderMap.containsKey(key)) {
            CssDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                downloader.cancel();
            }
            mDownloaderMap.remove(key);
        }
    }

    public void pauseAll() {
        for (CssDownloader downloader : mDownloaderMap.values()) {
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.pause();
                }
            }
        }
    }

    public void cancelAll() {
        for (CssDownloader downloader : mDownloaderMap.values()) {
            if (downloader != null) {
                if (downloader.isRunning()) {
                    downloader.cancel();
                }
            }
        }
    }

    //    public void delete(String tag) {
    //        String key = createKey(tag);
    //        if (mDownloaderMap.containsKey(key)) {
    //            CssDownloader downloader = mDownloaderMap.get(key);
    //            downloader.cancel();
    //        } else {
    //            List<CssDownloadInfo> infoList = mDBManager.getDownloadInfos(tag);
    //            for (CssDownloadInfo info : infoList) {
    //                FileUtils.delete(info.getD);
    //            }
    //        }
    //    }
    //
    //    public void deleteAll() {
    //
    //    }

    public CssDownloadInfo getDownloadProgress(String tag) {
        String key = createKey(tag);
        List<ThreadInfo> threadInfos = mDBManager.getThreadInfos(key);
        CssDownloadInfo downloadInfo = null;
        if (!threadInfos.isEmpty()) {
            int finished = 0;
            int progress = 0;
            int total = 0;
            for (ThreadInfo info : threadInfos) {
                finished += info.getFinished();
                total += (info.getEnd() - info.getStart());
            }
            progress = (int) ((long) finished * 100 / total);
            downloadInfo = new CssDownloadInfo();
            downloadInfo.setFinished(finished);
            downloadInfo.setLength(total);
            downloadInfo.setProgress(progress);
        }
        return downloadInfo;
    }

    private boolean check(String key) {
        if (mDownloaderMap.containsKey(key)) {
            CssDownloader downloader = mDownloaderMap.get(key);
            if (downloader != null) {
                if (downloader.isRunning()) {
                    MLog.w(TAG, "Task has been started!");
                    return false;
                } else {
                    throw new IllegalStateException("CssDownloader instance with same tag has not been destroyed!");
                }
            }
        }
        return true;
    }

    private static String createKey(String tag) {
        if (tag == null) {
            throw new NullPointerException("Tag can't be null!");
        }
        return String.valueOf(tag.hashCode());
    }


}
