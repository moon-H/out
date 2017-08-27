package com.whl.framework.downloadlibrary.architecture;


import com.whl.framework.downloadlibrary.DownloadException;

/**
 * Created by Aspsine on 2015/10/29.
 */
public interface ConnectTask extends Runnable {

    public interface OnConnectListener {
        void onConnecting();

        void onConnected(long time, long length, boolean isAcceptRanges);

        void onConnectCanceled();

        void onConnectFailed(DownloadException de);
    }

    void cancel();

    boolean isConnecting();

    boolean isConnected();

    boolean isCanceled();

    boolean isFailed();

    @Override
    void run();
}
