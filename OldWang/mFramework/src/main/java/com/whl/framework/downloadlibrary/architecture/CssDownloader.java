package com.whl.framework.downloadlibrary.architecture;

/**
 * Created by Aspsine on 2015/10/29.
 */
public interface CssDownloader {

    public interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, CssDownloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();

}
