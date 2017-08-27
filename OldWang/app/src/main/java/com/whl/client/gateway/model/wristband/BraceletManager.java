//package com.cssweb.shankephone.gateway.model.wristband;
//
//import android.app.Activity;
//import android.content.ActivityNotFoundException;
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Toast;
//
//import com.cssweb.framework.download.DownloadInfo;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.DMReceiver;
//import com.cssweb.shankephone.dialog.NoticeDialog;
//import com.cssweb.shankephone.home.card.seservice.SpNotInstalledException;
//import com.cssweb.shankephone.upgrade.DownLoadActivity;
//import com.cssweb.shankephone.upgrade.DownLoadService;
//
//public class BraceletManager {
//    private static final String TAG = "BraceletManager";
//    private Activity mContext;
//
//    private String pckName;
//
//    public BraceletManager(Activity context) {
//        this.mContext = context;
//    }
//
//    public void showDownloadPluginDialog(final String name, final String url) {
//        pckName = name;
//        NoticeDialog noticeDialog = new NoticeDialog(mContext, NoticeDialog.TWO_BUTTON);
//        noticeDialog.setButtonName(mContext.getString(R.string.download), mContext.getString(R.string.cancel));
//        noticeDialog.setContent(mContext.getString(R.string.need_download) + "\"" + name + "\"" + mContext.getString(R.string.complete_aciton));
//        noticeDialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//
//            @Override
//            public void onRightButtonClicked(View view) {
//
//            }
//
//            @Override
//            public void onLeftButtonClicked(View view) {
//                startDownload(false, name, url, true);
//            }
//        });
//        noticeDialog.show();
//    }
//
//    /**
//     * 下载插件 1.是否强制更新 2.下载apk名称 3.下载url 4.完成后是否隐藏安装按钮
//     */
//    public void startDownload(boolean isMendatory, String appName, String downloadUrl, boolean hideInstallBtn) {
//        DownloadInfo downloadInfo = new DownloadInfo();
//        downloadInfo.setUrl(downloadUrl);
//        downloadInfo.setAppName(appName);
//        downloadInfo.setMendetory(isMendatory);
//        downloadInfo.setHideInstallBtn(hideInstallBtn);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(DownLoadService.DOWNLOAD_DOWNLOADINFO, downloadInfo);
//        Intent intent = new Intent(mContext, DownLoadActivity.class);
//        intent.putExtra(DownLoadService.DOWNLOAD_DOWNLOADINFO, bundle);
//        intent.putExtra(DownLoadActivity.IS_MENDETORY, isMendatory);
//        mContext.startActivity(intent);
//    }
//
//    /**
//     * 通过包名启动插件
//     */
//    public void launchPluginByPackageName(String packageName) throws SpNotInstalledException {
//        try {
//            Intent intent;
//            PackageManager packageManager = mContext.getPackageManager();
//            intent = packageManager.getLaunchIntentForPackage(packageName);
//            if (intent != null)
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            mContext.startActivity(intent);
//        } catch (Exception e) {
//            MLog.e(TAG, "launchPluginByPackageName occur an error :: ", e);
//            e.printStackTrace();
//            throw new SpNotInstalledException();
//        }
//    }
//
//    /**
//     * 通过包名类名启动插件
//     */
//    public void launchPluginByClassName(String packageName, String launchClassName) throws SpNotInstalledException {
//        try {
//            Intent intent = new Intent();
//            ComponentName componentName;
//            componentName = new ComponentName(packageName, launchClassName);
//            intent.setComponent(componentName);
//            mContext.startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            MLog.e(TAG, "launchPluginByClassName :: " + e.getMessage(), e);
//            throw new SpNotInstalledException();
//        } catch (Exception e) {
//            MLog.e(TAG, "launchPluginByClassName occur an error :: ", e);
//            e.printStackTrace();
//        }
//    }
//
//    public void registerDownloadPluginReceiver() {
//        MLog.d(TAG, "registerDownloadPluginReceiver");
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(DMReceiver.ACTION_DM_SP_PLUGIN_INSTALLED);
//        LocalBroadcastManager.getInstance(mContext).registerReceiver(spPunginDownloadReceiver, filter);
//    }
//
//    public void unRegisterDownloadPluginReceiver() {
//        MLog.d(TAG, "unRegisterDownloadPluginReceiver");
//        LocalBroadcastManager.getInstance(mContext).unregisterReceiver(spPunginDownloadReceiver);
//    }
//
//    public BroadcastReceiver spPunginDownloadReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            MLog.d(TAG, "action = " + action);
//            if (action.equals(DMReceiver.ACTION_DM_SP_PLUGIN_INSTALLED)) {
//                String packageName = intent.getStringExtra(DMReceiver.PACKAGE_NAME);
//                MLog.d(TAG, "onReceive packageName = " + packageName);
//                if (pckName != null && !TextUtils.isEmpty(packageName) && packageName.equals(pckName)) {
//                    Toast.makeText(mContext, "\"" + pckName + "\"" + mContext.getString(R.string.plugin_download_complete), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    };
//}
