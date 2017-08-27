package com.whl.client.download;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.whl.framework.downloadlibrary.CallBack;
import com.whl.framework.downloadlibrary.DownloadConstants;
import com.whl.framework.downloadlibrary.DownloadException;
import com.whl.framework.downloadlibrary.DownloadManager;
import com.whl.framework.downloadlibrary.DownloadRequest;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.gateway.model.singleticket.MetroMap;

import java.io.File;

/**
 * Created by liwx on 2016/1/5.
 */
public class DownloadResService extends Service {
    private static final String TAG = "DownloadResService";

    public static final String ACTION_DOWNLOAD_BROAD_CAST = "com.aspsine.multithreaddownload.demo:action_download_broad_cast";

    public static final String ACTION_DOWNLOAD = "com.aspsine.multithreaddownload.demo:action_download";

    public static final String ACTION_PAUSE = "com.aspsine.multithreaddownload.demo:action_pause";

    public static final String ACTION_CANCEL = "com.aspsine.multithreaddownload.demo:action_cancel";

    public static final String ACTION_PAUSE_ALL = "com.aspsine.multithreaddownload.demo:action_pause_all";

    public static final String ACTION_CANCEL_ALL = "com.aspsine.multithreaddownload.demo:action_cancel_all";

    public static final String EXTRA_POSITION = "extra_position";

    public static final String EXTRA_TAG = "extra_tag";

    public static final String EXTRA_APP_INFO = "extra_app_info";

    /**
     * Dir: /files
     */
    private File mDownloadDir;

    private DownloadManager mDownloadManager;

    private NotificationManagerCompat mNotificationManager;

    public static void intentDownload(Context context, int position, String tag, MetroMap info) {
        Intent intent = new Intent(context, DownloadResService.class);
        intent.setAction(ACTION_DOWNLOAD);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_TAG, tag);
        intent.putExtra(EXTRA_APP_INFO, info);
        context.startService(intent);
    }

    public static void intentPause(Context context, String tag) {
        Intent intent = new Intent(context, DownloadResService.class);
        intent.setAction(ACTION_PAUSE);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentPauseAll(Context context) {
        Intent intent = new Intent(context, DownloadResService.class);
        context.startService(intent);
    }

    public static void intentCancel(Context context, String tag) {
        Intent intent = new Intent(context, DownloadResService.class);
        intent.setAction(ACTION_CANCEL);
        intent.putExtra(EXTRA_TAG, tag);
        context.startService(intent);
    }

    public static void intentCancelAll(Context context) {
        Intent intent = new Intent(context, DownloadResService.class);
        intent.setAction(ACTION_CANCEL_ALL);
        context.startService(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            int position = intent.getIntExtra(EXTRA_POSITION, 0);
            MetroMap appInfo = (MetroMap) intent.getSerializableExtra(EXTRA_APP_INFO);
            String tag = intent.getStringExtra(EXTRA_TAG);
            switch (action) {
                case ACTION_DOWNLOAD:
                    download(position, appInfo, tag);
                    break;
                case ACTION_PAUSE:
                    pause(tag);
                    break;
                case ACTION_CANCEL:
                    cancel(tag);
                    break;
                case ACTION_PAUSE_ALL:
                    pauseAll();
                    break;
                case ACTION_CANCEL_ALL:
                    cancelAll();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void download(final int position, final MetroMap appInfo, String tag) {
        final DownloadRequest request = new DownloadRequest.Builder().setTitle(appInfo.getCityCode() + ".zip").setUri(appInfo.getDownloadMetroMapUrl()).setFolder(mDownloadDir).build();
        mDownloadManager.download(request, tag, new DownloadCallBack(position, appInfo, mNotificationManager, getApplicationContext()));
    }

    private void pause(String tag) {
        mDownloadManager.pause(tag);
    }

    private void cancel(String tag) {
        mDownloadManager.cancel(tag);
    }

    private void pauseAll() {
        mDownloadManager.pauseAll();
    }

    private void cancelAll() {
        mDownloadManager.cancelAll();
    }

    public static class DownloadCallBack implements CallBack {

        private int mPosition;

        private MetroMap mDownloadInfo;

        private LocalBroadcastManager mLocalBroadcastManager;

        //        private NotificationCompat.Builder mBuilder;

        //        private NotificationManagerCompat mNotificationManager;

        private long mLastTime;

        public DownloadCallBack(int position, MetroMap appInfo, NotificationManagerCompat notificationManager, Context context) {
            mPosition = position;
            mDownloadInfo = appInfo;
            //            mNotificationManager = notificationManager;
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(context);
            //            mBuilder = new NotificationCompat.Builder(context);
        }

        @Override
        public void onStarted() {
            MLog.d(TAG, "onStart()");
            //            mBuilder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle(mDownloadInfo.getName()).setContentText("Init Download").setProgress(100, 0, true).setTicker("Start download " + mDownloadInfo.getName());
            updateNotification();
        }

        @Override
        public void onConnecting() {
            MLog.d(TAG, "onConnecting()");
            //            mBuilder.setContentText("Connecting").setProgress(100, 0, true);
            updateNotification();

            mDownloadInfo.setStatus(DownloadConstants.STATUS_CONNECTING);
            sendBroadCast(mDownloadInfo);
        }

        @Override
        public void onConnected(long total, boolean isRangeSupport) {
            MLog.d(TAG, "onConnected()");
            //            mBuilder.setContentText("Connected").setProgress(100, 0, true);
            updateNotification();
        }

        @Override
        public void onProgress(long finished, long total, int progress) {

            if (mLastTime == 0) {
                mLastTime = System.currentTimeMillis();
            }

            mDownloadInfo.setStatus(DownloadConstants.STATUS_DOWNLOADING);
            mDownloadInfo.setProgress(progress);
            //            mDownloadInfo.setDownloadPerSize(Utils.getDownloadPerSize(finished, total));
            mDownloadInfo.setTotalSize(Utils.getTotalPerSize(total));

            long currentTime = System.currentTimeMillis();
            if (currentTime - mLastTime > 500) {
                MLog.d(TAG, "onProgress()");
                //                mBuilder.setContentText("Downloading");
                //                mBuilder.setProgress(100, progress, false);
                updateNotification();

                sendBroadCast(mDownloadInfo);

                mLastTime = currentTime;
            }
        }

        @Override
        public void onCompleted() {
            MLog.d(TAG, "onCompleted");
            //            mBuilder.setContentText("Download Complete");
            //            mBuilder.setProgress(0, 0, false);
            //            mBuilder.setTicker(mDownloadInfo.getName() + " download Complete");
            updateNotification();

            mDownloadInfo.setStatus(DownloadConstants.STATUS_COMPLETE);
            mDownloadInfo.setProgress(100);
            sendBroadCast(mDownloadInfo);
        }

        @Override
        public void onDownloadPaused() {
            MLog.d(TAG, "onDownloadPaused()");
            //            mBuilder.setContentText("Download Paused");
            //            mBuilder.setTicker(mDownloadInfo.getName() + " download Paused");
            updateNotification();

            mDownloadInfo.setStatus(DownloadConstants.STATUS_PAUSED);
            sendBroadCast(mDownloadInfo);
        }

        @Override
        public void onDownloadCanceled() {
            MLog.d(TAG, "onDownloadCanceled()");
            //            mBuilder.setContentText("Download Canceled");
            //            mBuilder.setTicker(mDownloadInfo.getName() + " download Canceled");
            updateNotification();
            //            mNotificationManager.cancel(mPosition);
            mDownloadInfo.setStatus(DownloadConstants.STATUS_CANCLED);
            sendBroadCast(mDownloadInfo);

        }

        @Override
        public void onFailed(DownloadException e) {
            MLog.d(TAG, "onFailed()");
            e.printStackTrace();
            //            mBuilder.setContentText("Download Failed");
            //            mBuilder.setTicker(mDownloadInfo.getName() + " download failed");
            //            mBuilder.setProgress(100, mDownloadInfo.getProgress(), false);
            updateNotification();

            mDownloadInfo.setStatus(DownloadConstants.STATUS_DOWNLOAD_ERROR);
            sendBroadCast(mDownloadInfo);
        }

        private void updateNotification() {
            //            mNotificationManager.notify(mPosition + 1000, mBuilder.build());
        }

        private void sendBroadCast(MetroMap appInfo) {
            Intent intent = new Intent();
            intent.setAction(DownloadResService.ACTION_DOWNLOAD_BROAD_CAST);
            intent.putExtra(EXTRA_POSITION, mPosition);
            intent.putExtra(EXTRA_APP_INFO, appInfo);
            mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = DownloadManager.getInstance();
        mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
        mDownloadDir = DownloadConstants.getMetroCacheDir(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDownloadManager.pauseAll();
    }
}
