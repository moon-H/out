package com.whl.client.app;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.whl.framework.app.MApplication;
import com.whl.framework.download.Downloader;
import com.whl.framework.downloadlibrary.DownloadConfiguration;
import com.whl.framework.downloadlibrary.DownloadManager;
import com.whl.framework.http.BaseGateway;
import com.whl.framework.preference.MSharedPreference;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.BuildConfig;
import com.whl.client.R;
import com.whl.client.db.CommonDBManager;
import com.whl.client.gateway.WalletGateway;
import com.whl.client.home.ticket.MapManager;
import com.whl.client.login.SplashActivity;
import com.whl.client.preference.MPreference;
import com.whl.client.push.CssPushManager;
import com.whl.client.view.ptr.PtrClassicFrameLayout;
import com.igexin.sdk.PushManager;
import com.umeng.socialize.common.SocializeConstants;

import org.litepal.LitePalApplication;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class BizApplication extends MApplication {
    private static final String TAG = "BizApplication";
    /**
     * SERVER ADDRESS
     **********************************************************************/
    public static final String BASE_ADDRESS = BuildConfig.WALLET_SERVER_URL;
    public static final String BBS_BASE_ADDRESS = BuildConfig.BBS_SERVER_URL;//
    /*****************************************************************************************/

    public static final String ACTION_EXIT_APP = "com.cssweb.shankephone.ACTION_EXIT_APP";
    public static final String ACTION_RELOGIN_APP = "com.cssweb.shankephone.ACTION_RELOGIN_APP";

    private static BizApplication mInstance;
    private CssPushManager mCssPushManager;
    private MapManager mapManager;
    //    private LakaLaManager lakaLaManager;
    private boolean isBluetoothEnabled;
    private CommonDBManager mCommonDBManager;

    private boolean isOMApiAvailable = false;
    private boolean isSeProceeding = false;

    public static final String WALLET_ID = BuildConfig.WALLET_ID;
    public static final String WALLET_ID_CHECK_UPDATE = BuildConfig.WALLET_ID_CHECK_UPDATE;
    public static final String OS_NAME = "ANDROID";
    public static final String CITY_CODE_CHANGSHA = "4100";
    public static final String CITY_CODE_GUANGZHOU = "4401";

    public static final String MNO_CM = "CM";
    public static final String MNO_CU = "CU";
    public static final String MNO_CT = "CT";
    public static final String MNO_NA = "NA";

    private ExecutorService mExecutor;

    //    private static DisplayImageOptions options;
    private static BaseGateway sBbsBaseGateway;
    private WalletGateway mWalletGateway;
    /**
     * 当前运营商信息
     */
    private String currentMno = MNO_NA;

    private RotateAnimation animation;
    protected Dialog mProgressDialog;
    private View mProgress;

    public HashMap<String, Downloader> downloadMap = new HashMap<String, Downloader>();
    private int nonNfcItemHeight;
    private int nonNfcItemWidth;
    private int maxMemory;

    private String panchanToken;
    private boolean realAuth;

    private String cityCode;

    private boolean isTopupBracelet = false;

    private String cuServiceId;
    private int cuOptionFlag = -1;
    private static List<Activity> sActContainer = new LinkedList<Activity>();

    private String ctAID;
    private int ctstartMode;

    private boolean isAppResume = false;
    /**
     * 是否已登录
     */
    private boolean isLoginClient = false;

    private long ntpTimeDiff;//NTP服务器时间差

    private double longitude;
    private double latitude;

    @Override
    public void onCreate() {
        super.onCreate();
        /*代码添加Appkey，如果设置了非null值，SocialSDK将使用该值.*/
        //        MLog.setDebugAble(BuildConfig.LOG_DEBUG);
        MLog.init(PARENT_CACHE_DIR + "/Log", LOG_FILE_NAME, LOG_FILE_SIZE_LIMIT, LOG_FILE_MAX_COUNT, BuildConfig.LOG_DEBUG);

        SocializeConstants.APPKEY = "565bfdbde0f55a42e400289b";
        mInstance = this;
        maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;

        if (BuildConfig.LOG_DEBUG) {
            MLog.d(TAG, "## onCreate ");
            MLog.d(TAG, "### density = " + DeviceInfo.getDensity(this));
            MLog.d(TAG, "### screen w = " + DeviceInfo.getScreenWidth(this) + " height = " + DeviceInfo.getScreenHeight(this));
            MLog.d(TAG, "### dpi = " + DeviceInfo.getDpi(this));
            MLog.d(TAG, "### has sim  = " + DeviceInfo.hasSimCard(this));
            MLog.d(TAG, "### Mno = " + currentMno);
            MLog.d(TAG, "###Channel = " + Utils.getMetaDataValue(this, "UMENG_CHANNEL"));

        }

        initRotateAnimation();

        caculateNonNfcItemHeight();
        caculateNonNfcItemWidth();

        CommonExceptionHandler crashHandler = CommonExceptionHandler.getInstance();
        crashHandler.init(this);

        setOMApiAvailable(DeviceInfo.isOMApiAvailable());

        LitePalApplication.initialize(this);

        initDownloader();
    }

    public boolean isLoginClient() {
        return isLoginClient;
    }

    public void setIsLoginClient(boolean isLoginClient) {
        this.isLoginClient = isLoginClient;
    }

    public void addActivity(Activity activity) {
        sActContainer.add(activity);
    }

    public void removeActivity(Activity activity) {
        sActContainer.remove(activity);
    }


    public CssPushManager getCssPushManager() {
        if (mCssPushManager == null)
            mCssPushManager = new CssPushManager(getApplicationContext());
        return mCssPushManager;
    }

    public void setRealAuth(boolean realAuth) {
        this.realAuth = realAuth;
    }

    public boolean isRealAuth() {
        return this.realAuth;
    }

    public void setPanchanToken(String token) {
        this.panchanToken = token;
    }

    public String getPanchanToken() {
        return this.panchanToken;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityCode() {
        //        return this.cityCode;
        return MPreference.getString(getApplicationContext(), MPreference.CHOICE_CITY_CODE);
    }

    public static BizApplication getInstance() {
        return mInstance;
    }

    /**
     * 普通关闭应用
     */
    public void exitApp(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setAction(ACTION_EXIT_APP);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public int getNonNfcItemHeight() {
        return nonNfcItemHeight;
    }

    public int getNonNfcItemWidth() {
        return nonNfcItemWidth;
    }

    private void caculateNonNfcItemHeight() {
        //        homeMenuItemHeight = (int) (DeviceInfo.getScreenHeight(getApplicationContext()) * 0.25);
        nonNfcItemHeight = getResources().getDimensionPixelOffset(R.dimen.app_item_height);
    }

    private void caculateNonNfcItemWidth() {
        //        homeMenuItemHeight = (int) (DeviceInfo.getScreenHeight(getApplicationContext()) * 0.25);
        nonNfcItemWidth = getResources().getDimensionPixelOffset(R.dimen.app_item_width);
    }

    public void setBluetoothState(boolean isOn) {
        isBluetoothEnabled = isOn;
    }

    public boolean isOMApiAvailable() {
        //        if (seManager != null)
        //            return seManager.isConnected();
        return isOMApiAvailable;
        //        return false;
    }

    public void setOMApiAvailable(boolean isOMApiAvailable) {
        this.isOMApiAvailable = isOMApiAvailable;
    }

    public boolean isSeProceeding() {
        return isSeProceeding;
    }

    public void setSeProceeding(boolean isSeProceeding) {
        this.isSeProceeding = isSeProceeding;
    }

    public String getCurrentMno() {
        return currentMno;
    }

    public void setCurrentMno(String mno) {
        MLog.d(TAG, "setCurrentMno = " + mno);
        //        openSeConnection();
        this.currentMno = mno;
    }

    public boolean isAppResume() {
        return isAppResume;
    }

    public void setIsAppResume(boolean isAppResume) {
        this.isAppResume = isAppResume;
    }

    private void initRotateAnimation() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1300);//设置动画持续时间
        animation.setStartOffset(0);//执行前的等待时间
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);//设置重复次数
    }

    public void startRotateView(View view) {
        if (view != null) {
            //            view.setVisibility(OrderView.VISIBLE);
            view.findViewById(R.id.img_progress).startAnimation(animation);
        }
    }

    public void stopRotateView(View view) {
        if (view != null) {
            view.findViewById(R.id.img_progress).clearAnimation();
            //            view.setVisibility(OrderView.GONE);
        }

    }

    public Dialog getProgressDialog(Activity activity, String message, boolean cancelAble) {
        initProgressView(activity, message, cancelAble);
        return mProgressDialog;
    }

    public Dialog getProgressDialog(Activity activity, boolean cancelAble) {
        initProgressView(activity, null, cancelAble);
        return mProgressDialog;
    }

    public Dialog getProgressDialog(Activity activity, String msg) {
        initProgressView(activity, msg, false);
        return mProgressDialog;
    }

    public Dialog getProgressDialog(Activity activity) {
        initProgressView(activity, null, false);
        return mProgressDialog;
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgress.findViewById(R.id.img_progress).clearAnimation();
        }
    }

    private void initProgressView(Activity activity, String msg, boolean cancelAble) {
        //        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
        mProgressDialog = new Dialog(activity, R.style.theme_dialog_alert);
        mProgress = View.inflate(activity, R.layout.progressbar_inverse, null);
        startRotateView(mProgress);
        if (!TextUtils.isEmpty(msg)) {
            TextView mProText = (TextView) mProgress.findViewById(R.id.tv_pro_text);
            mProText.setText(msg);
        }
        mProgressDialog.setContentView(mProgress);
        mProgressDialog.setCancelable(cancelAble);
        //        }
    }

    public boolean isProgressViewShowing() {
        if (mProgressDialog != null)
            return mProgressDialog.isShowing();
        return false;
    }

    public MapManager getMapManager() {
        if (mapManager == null)
            mapManager = new MapManager(getApplicationContext());
        return mapManager;
    }

    public CommonDBManager getCommonDBManager() {
        if (mCommonDBManager == null) {
            mCommonDBManager = new CommonDBManager(this);
        }
        return mCommonDBManager;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setIsTopupBracelet(boolean isTopup) {
        this.isTopupBracelet = isTopup;
    }

    public boolean isTopupBracelet() {
        return isTopupBracelet;
    }

    public void configPullToRefresh(PtrClassicFrameLayout layout) {
        layout.setResistance(1.7f);
        layout.setRatioOfHeaderHeightToRefresh(1.2f);
        layout.setDurationToClose(200);
        layout.setDurationToCloseHeader(800);
        // default is false
        layout.setPullToRefresh(false);
        // default is true
        layout.setKeepHeaderWhenRefresh(true);
        layout.disableWhenHorizontalMove(true);
    }

    public int getCtstartMode() {
        return ctstartMode;
    }

    public void setCtstartMode(int ctstartMode) {
        this.ctstartMode = ctstartMode;
    }

    public String getCtAID() {
        return ctAID;
    }

    public void setCtAID(String ctAID) {
        this.ctAID = ctAID;
    }


    public void setCuServiceId(String serviceId) {
        this.cuServiceId = serviceId;
    }

    public String getCuServiceId() {
        return cuServiceId;
    }

    public void setCuOptionFlag(int flag) {
        cuOptionFlag = flag;
    }

    public int getCuOptionFlag() {
        return cuOptionFlag;
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(10);
        configuration.setThreadNum(3);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }

    /**
     * 程序异常时使用方法关闭应用
     */
    public void exit() {
        MSharedPreference.clearCacheData(mInstance);
        try {
            for (Activity activity : sActContainer) {
                if (activity != null && !activity.isFinishing())
                    activity.finish();
            }
            sActContainer.clear();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //            MobclickAgent.onKillProcess(this);
            System.exit(0);
        }
    }

    /**
     * 关闭闪客蜂所有页面
     */
    public void exitShankePhone() {
        MSharedPreference.clearCacheData(mInstance);
        try {
            for (Activity activity : sActContainer) {
                if (activity != null && !activity.isFinishing())
                    activity.finish();
            }
            sActContainer.clear();
        } catch (Exception e) {
            MLog.e(TAG, "exitShankePhone occur error", e);
        }
    }

    public ExecutorService getThreadPool() {
        if (mExecutor == null)
            mExecutor = Executors.newCachedThreadPool();
        return mExecutor;
    }

    public boolean isLaunchByCT() {
        return !TextUtils.isEmpty(BizApplication.getInstance().getCtAID());
    }


    public String getPushId() {
        return PushManager.getInstance().getClientid(BizApplication.getInstance());
    }

    public void setNtpTimeDiff(long timeDiff) {
        ntpTimeDiff = timeDiff;
    }

    public long getNtpTimeDiff() {
        return ntpTimeDiff;
    }

    public String getLoginId() {
        return MPreference.getLoginId(this);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
