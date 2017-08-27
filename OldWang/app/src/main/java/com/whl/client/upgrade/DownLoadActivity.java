package com.whl.client.upgrade;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whl.framework.download.DownloadInfo;
import com.whl.framework.download.Downloader;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.upgrade.DownLoadService.DownloadBinder;
import com.whl.client.upgrade.DownLoadService.OnDownLoadListener;

import java.io.File;


public class DownLoadActivity extends FragmentActivity implements OnClickListener {

    private static final String TAG = "DownLoadActivity";
    public static final String ACTION_DOWNLOAD_CLICK_RUN_IN_BACKGROUND = "com.cssweb.subticket.action.DOWNLOAD_CLICK_RUN_IN_BACKGROUND";
    public static final String ACTION_DOWNLOAD_CLICK_CANCEL = "com.cssweb.subticket.action.DOWNLOAD_CLICK_CANCEL";
    public static final String URL = "download_url";
    public static final String CLICK_POSITION = "click_position";
    public static final String IS_MENDETORY = "is_mendetory";
    public static final int CLICK_RUN_IN_BACKGROUND = 1;
    public static final int CLICK_CANCEL = 2;
    public static final int CLICK_INSTALL = 3;

    private ProgressBar mProgressBar;
    private TextView mPercenTextView;
    private TextView mTotalTextView;
    private TextView mHeadTextView;
    private Button mLeftBtn;
    private Button mCancelBtn;
    private Button mOneCancelBtn;
    private View mTwoBtnView;
    private View mBackgroundView;

    private double ONE_MB = 1024 * 1024;

    private DownLoadService.DownloadBinder binder;
    private DownloadInfo downloadInfo;

    private int status;
    private int completeSize;
    private int fileSize;
    private int mClickedPosition = 0;
    private String mUrl;
    private String mInstallPath;
    private String mTotalStr;
    private boolean isBinded = false;
    private boolean isMendetory;
    private boolean hideInstallBtn = false;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.dlg_upgrade);
        MLog.d(TAG, "onCreate");
        Intent intent = getIntent();
        // isMendetory = intent.getBooleanExtra(IS_MENDETORY, false);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mPercenTextView = (TextView) findViewById(R.id.percent);
        mTotalTextView = (TextView) findViewById(R.id.total);
        mLeftBtn = (Button) findViewById(R.id.run_in_background);
        mCancelBtn = (Button) findViewById(R.id.cancel);
        mHeadTextView = (TextView) findViewById(R.id.head);
        mOneCancelBtn = (Button) findViewById(R.id.one_btn_cancel);
        mTwoBtnView = findViewById(R.id.two_btn_layout);
        mBackgroundView = findViewById(R.id.background);

        mLeftBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);
        mOneCancelBtn.setOnClickListener(this);
        mBackgroundView.getBackground().setAlpha(90);

        parseIntent(intent);
        updateButtonView(isMendetory ? 1 : 2, null);
        bindDownLoadService(mUrl);
        // onNewIntent(getIntent());
        MLog.d(TAG, "url = " + mUrl);
    }

    private void parseIntent(Intent intent) {
        downloadInfo = (DownloadInfo) intent.getBundleExtra(DownLoadService.DOWNLOAD_DOWNLOADINFO).getSerializable(DownLoadService.DOWNLOAD_DOWNLOADINFO);
        mUrl = downloadInfo.getUrl();
        status = downloadInfo.getStatus();
        mInstallPath = downloadInfo.getInstallPath();
        isMendetory = downloadInfo.isMendetory();
        hideInstallBtn = downloadInfo.isHideInstallBtn();
        fileSize = intent.getIntExtra(DownLoadService.DOWNLOAD_FILE_SIZE, 0);
        completeSize = intent.getIntExtra(DownLoadService.DOWNLOAD_COMPLETE_SIZE, 0);

        switch (status) {
            case Downloader.STATUS_DOWNLOADING:
                setTotal(fileSize);
                setDownLoadProgress(fileSize, completeSize);
                updateTitleText(Downloader.STATUS_DOWNLOADING, downloadInfo);
                break;
            case Downloader.STATUS_COMPLETE:
                if (hideInstallBtn) {
                    finish();
                }
                setTotal(fileSize);
                setDownLoadProgress(fileSize, completeSize);
                updateButtonView(2, getString(R.string.download_install));
                updateTitleText(Downloader.STATUS_COMPLETE, downloadInfo);
                break;
            case Downloader.STATUS_ERROR:
                updateButtonView(2, getString(R.string.download_retry));
                updateTitleText(Downloader.STATUS_ERROR, downloadInfo);
                break;
            default:
                break;
        }
    }

    private void updateTitleText(int status, DownloadInfo downloadInfo) {
        switch (status) {
            case Downloader.STATUS_DOWNLOADING:
                mHeadTextView.setText(getString(R.string.download_loading) + " : " + downloadInfo.getAppName());
                break;
            case Downloader.STATUS_COMPLETE:
                mHeadTextView.setText(getString(R.string.download_complete) + " : " + downloadInfo.getAppName());
                break;
            case Downloader.STATUS_ERROR:
                mHeadTextView.setText(getString(R.string.download_failed) + " : " + downloadInfo.getAppName());
                break;
            default:
                break;
        }
    }

    private void updateButtonView(int buttonCount, String str) {
        if (buttonCount == 1) {
            mTwoBtnView.setVisibility(View.GONE);
            mOneCancelBtn.setVisibility(View.VISIBLE);
            // if (!TextUtils.isEmpty(str)) {
            // mOneCancelBtn.setText(str);
            // }
        } else if (buttonCount == 2) {
            mTwoBtnView.setVisibility(View.VISIBLE);
            mOneCancelBtn.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(str)) {
                mLeftBtn.setText(str);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
    }

    @Override
    public void onClick(View v) {
        MLog.d(TAG, "status = " + status);
        switch (v.getId()) {
            case R.id.run_in_background:
                mClickedPosition = CLICK_RUN_IN_BACKGROUND;
                if (status == Downloader.STATUS_DOWNLOADING) {
                    finishView();
                } else if (status == Downloader.STATUS_COMPLETE) {
                    mClickedPosition = CLICK_INSTALL;
                    if (!TextUtils.isEmpty(mInstallPath)) {
                        install(mInstallPath);
                    }
                } else if (status == Downloader.STATUS_ERROR) {
                    MLog.d(TAG, "STATUS_ERROR");
                    setDownLoadProgress(fileSize, 0);
                    // bindDownLoadService(mUrl);
                    if (binder != null) {
                        status = Downloader.STATUS_DOWNLOADING;
                        MLog.d(TAG, "start DownLoad");
                        binder.startDownload(downloadInfo);
                    }
                    mHeadTextView.setText(getString(R.string.download_loading));
                    updateButtonView(isMendetory ? 1 : 2, getString(R.string.download_background));
                } else if (status == 0) {
                    finish();
                }
                break;
            case R.id.cancel:
                mClickedPosition = CLICK_CANCEL;
                stopDownLoad();
                finishView();
                break;
            case R.id.one_btn_cancel:
                mClickedPosition = CLICK_CANCEL;
                stopDownLoad();
                finishView();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        MLog.d(TAG, "onNewIntent");
        parseIntent(intent);
        // bindDownLoadService(mUrl);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        if (isBinded) {
            unbindService(connection);
        }
    }

    private void install(String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void bindDownLoadService(String url) {
        bindService(new Intent(DownLoadActivity.this, DownLoadService.class), connection, Context.BIND_AUTO_CREATE);
        MLog.d(TAG, "bindDownLoadService");
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            MLog.d(TAG, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MLog.d(TAG, "onServiceConnected");
            isBinded = true;
            binder = (DownloadBinder) service;
            binder.setOnDownLoadListener(downLoadListener, downloadInfo);
            if (status != Downloader.STATUS_DOWNLOADING && status != Downloader.STATUS_COMPLETE) {
                MLog.d(TAG, "pre start DownLoad");
                if (binder != null) {
                    MLog.d(TAG, "start DownLoad");
                    binder.startDownload(downloadInfo);
                }
            }
        }
    };
    private OnDownLoadListener downLoadListener = new OnDownLoadListener() {

        @Override
        public void onDownLoading(String url, int fileSize, int completeSize) {
            if (!mUrl.equals(url))
                return;
            status = Downloader.STATUS_DOWNLOADING;
            setTotal(fileSize);
            setDownLoadProgress(fileSize, completeSize);
            updateTitleText(Downloader.STATUS_DOWNLOADING, downloadInfo);
        }

        @Override
        public void onDownLoadError(String url) {
            MLog.d(TAG, "onDownLoadError");
            if (!mUrl.equals(url))
                return;
            status = Downloader.STATUS_ERROR;
            updateButtonView(2, getString(R.string.download_retry));
            updateTitleText(Downloader.STATUS_ERROR, downloadInfo);
        }

        @Override
        public void onDownLoadComplete(String url, String installPath) {
            if (!mUrl.equals(url))
                return;
            if (hideInstallBtn) {
                finish();
            }
            status = Downloader.STATUS_COMPLETE;
            mInstallPath = installPath;
            updateButtonView(2, getString(R.string.download_install));
            updateTitleText(Downloader.STATUS_COMPLETE, downloadInfo);
        }

        @Override
        public void onDownLoadCancel(String url) {
            if (!mUrl.equals(url))
                return;
            status = Downloader.STATUS_CANCEL;
        }
    };

    private void stopDownLoad() {
        MLog.d(TAG, "stopDownLoadService");
        if (binder != null) {
            binder.stopDownload(mUrl);
        } else {
            MLog.d(TAG, "binder is null");
        }
    }

    private void setDownLoadProgress(double fileSize, int completeSize) {
        mProgressBar.setProgress(completeSize);
        if (completeSize != 0) {
            String percent = Utils.getDecimal2(completeSize / fileSize * 100);
            mPercenTextView.setText(percent + "%");
            mTotalTextView.setText(Utils.getDecimal2(completeSize / ONE_MB) + " MB" + "/" + mTotalStr);
        } else {
            mPercenTextView.setText("0 %");
            mTotalTextView.setText("0 MB" + "/" + "0 MB");
        }
    }

    private void setTotal(int fileSize) {
        mProgressBar.setMax(fileSize);
        mTotalStr = Utils.getDecimal2(fileSize / ONE_MB) + " MB";
        mTotalTextView.setText("0.0 MB" + "/" + mTotalStr);
    }

    private void finishView() {
        MLog.d(TAG, "finishView");
        Intent intent = null;
        if (mClickedPosition == CLICK_RUN_IN_BACKGROUND) {
            intent = new Intent(ACTION_DOWNLOAD_CLICK_RUN_IN_BACKGROUND);
            finish();
            overridePendingTransition(0, R.anim.anim_fade_exit);
        } else if (mClickedPosition == CLICK_CANCEL) {
            intent = new Intent(ACTION_DOWNLOAD_CLICK_CANCEL);
            finish();
            overridePendingTransition(0, R.anim.anim_fade_exit);
        } else {
            intent = new Intent();
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

}
