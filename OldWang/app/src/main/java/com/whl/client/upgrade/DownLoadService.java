package com.whl.client.upgrade;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.whl.framework.download.DownloadInfo;
import com.whl.framework.download.Downloader;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.app.BizApplication;


public class DownLoadService extends Service {

    private static final String TAG = "DownLoadService";

    public static final String ACTION_DOWNLOAD_IN_BACKGROUND = "com.cssweb.shankephone.action.DOWNLOAD_IN_BACKGROUND";
    public static final String DOWNLOAD_FILE_SIZE = "download_file_size";
    public static final String DOWNLOAD_COMPLETE_SIZE = "download_complete_size";
    public static final String DOWNLOAD_STATUS = "download_status";
    public static final String DOWNLOAD_DOWNLOADINFO = "download_download_info";

    private BizApplication mApp;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private static int DOWNLOAD_ID = 1;
    private OnDownLoadListener mDownLoadListener;
    private DownloadBinder binder = new DownloadBinder();
    private DownloadHandler mDownloadHandler;

    @Override
    public boolean onUnbind(Intent intent) {
        MLog.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        MLog.d(TAG, "onBind");
        // return new DownLoadBinder();
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MLog.d(TAG, "onCreate");
        mApp = (BizApplication) getApplication();
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setAutoCancel(true);
        mDownloadHandler = new DownloadHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MLog.d(TAG, "onStartCommand");

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
    }

    public void startDownLoad(DownloadInfo downloadInfo) {
        if (!mApp.downloadMap.containsKey(downloadInfo.getUrl())) {
            MLog.d(TAG, "startDownLoad");
            Downloader downloader = new Downloader(getApplicationContext(), downloadInfo, mDownloadHandler);
            mApp.downloadMap.put(downloadInfo.getUrl(), downloader);
            downloader.startDownload();
            DOWNLOAD_ID++;
        } else {
            MLog.d(TAG, "Download url has contained");
        }
    }

    /**
     * what:status arg1:fileSize arg2:completeSize
     * <p/>
     * if status is STATUS_COMPLETE,the msg.obj is CssDownloadInfo,otherwise msg.obj is url;
     */
    class DownloadHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.obj == null) {
                return;
            }
            String url = msg.obj.toString();

            Downloader downloader = mApp.downloadMap.get(url);
            DownloadInfo downloadInfo = downloader.getDownloadInfo();
            switch (msg.what) {
                case Downloader.STATUS_DOWNLOADING:
                    int fileSize = msg.arg1;
                    int completeSize = msg.arg2;
                    if (mDownLoadListener != null) {
                        mDownLoadListener.onDownLoading(url, fileSize, completeSize);
                    }
                    //					showDownloadNotification(downloadInfo, CssDownloader.STATUS_DOWNLOADING, msg.arg1, msg.arg2);
                    break;
                case Downloader.STATUS_CANCEL:
                    if (mDownLoadListener != null) {
                        mDownLoadListener.onDownLoadCancel(url);
                    }
                    showDownloadNotification(downloadInfo, Downloader.STATUS_CANCEL, 0, 0);
                    mApp.downloadMap.remove(url);
                    break;
                case Downloader.STATUS_COMPLETE:
                    mApp.downloadMap.remove(url);
                    if (mDownLoadListener != null) {
                        mDownLoadListener.onDownLoadComplete(url, downloader.getDownloadInfo().getInstallPath());
                    }
                    showDownloadNotification(downloadInfo, Downloader.STATUS_COMPLETE, msg.arg1, msg.arg2);
                    break;
                case Downloader.STATUS_ERROR:
                    MLog.d(TAG, "STATUS_ERROR");
                    mApp.downloadMap.remove(url);
                    if (mDownLoadListener != null) {
                        MLog.d(TAG, "call listener");
                        mDownLoadListener.onDownLoadError(url);
                    }
                    showDownloadNotification(downloadInfo, Downloader.STATUS_ERROR, 0, 0);
                    break;
                default:
                    break;
            }
        }
    }

    public class DownloadBinder extends Binder {

        public void setOnDownLoadListener(OnDownLoadListener listener, DownloadInfo downloadInfo) {
            mDownLoadListener = listener;
            Downloader downloader = mApp.downloadMap.get(downloadInfo.getUrl());
            if (downloader != null) {
                downloader.updateHadler(mDownloadHandler);
            }

        }

        public void startDownload(DownloadInfo downloadInfo) {
            startDownLoad(downloadInfo);
        }

        public void stopDownload(String url) {
            Downloader downloader = mApp.downloadMap.get(url);
            if (downloader != null) {
                downloader.stopDownload();
            }
        }
    }

    private void showDownloadNotification(final DownloadInfo downloadInfo, int status, final int fileSize, final int completeSize) {

        mBuilder.setContentTitle(downloadInfo.getAppName()).setContentText(getString(R.string.download_loading)).setSmallIcon(R.mipmap.ic_launcher);

        if (status == Downloader.STATUS_COMPLETE) {

            mBuilder.setContentText(getString(R.string.download_complete));
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(downloadInfo.getId(), mBuilder.build());
            mBuilder.setContentIntent(getPendingIntent(downloadInfo, completeSize, fileSize));
            mBuilder.setAutoCancel(true);

        } else if (status == Downloader.STATUS_DOWNLOADING) {

            double totle = fileSize;
            String percent = Utils.getDecimal2(completeSize / totle * 100);
            mBuilder.setContentText(percent + " %");
            mBuilder.setProgress(fileSize, completeSize, false);
            mBuilder.setContentIntent(getPendingIntent(downloadInfo, completeSize, fileSize));
            mNotifyManager.notify(downloadInfo.getId(), mBuilder.build());

        } else if (status == Downloader.STATUS_CANCEL) {

            mBuilder.setContentIntent(null);
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.cancel(downloadInfo.getId());

        } else if (status == Downloader.STATUS_ERROR) {

            MLog.d(TAG, "DOWNLOAD_STATE_EXCEPTION");
            mBuilder.setContentIntent(getPendingIntent(downloadInfo, completeSize, fileSize));
            mBuilder.setContentText(getString(R.string.download_failed));
            mBuilder.setProgress(0, 0, false);
            mNotifyManager.notify(downloadInfo.getId(), mBuilder.build());
            mBuilder.setAutoCancel(true);
        }
    }

    private PendingIntent getPendingIntent(DownloadInfo downloadInfo, int completeSize, int fileSize) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(DOWNLOAD_DOWNLOADINFO, downloadInfo);

        Intent resultIntent = new Intent(DownLoadService.this, DownLoadActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        resultIntent.putExtra(DOWNLOAD_FILE_SIZE, fileSize);
        resultIntent.putExtra(DOWNLOAD_COMPLETE_SIZE, completeSize);
        resultIntent.putExtra(DOWNLOAD_DOWNLOADINFO, bundle);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return resultPendingIntent;
    }

    public interface OnDownLoadListener {

        void onDownLoading(String url, int fileSize, int completeSize);

        void onDownLoadComplete(String url, String installPath);

        void onDownLoadError(String url);

        void onDownLoadCancel(String url);
    }
}
