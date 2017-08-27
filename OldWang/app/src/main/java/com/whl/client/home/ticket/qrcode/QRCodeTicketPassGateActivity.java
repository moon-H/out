//package com.cssweb.shankephone.home.ticket.qrcode;
//
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothGatt;
//import android.bluetooth.BluetoothGattCharacteristic;
//import android.bluetooth.BluetoothGattService;
//import android.bluetooth.le.ScanRecord;
//import android.bluetooth.le.ScanResult;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.os.ParcelUuid;
//import android.support.annotation.NonNull;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.ByteConverter;
//import com.cssweb.framework.utils.HexConverter;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.dialog.NoticeDialog;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderInfoRs;
//import com.cssweb.shankephone.gateway.model.wallet.GetPanchanTokenRs;
//import com.cssweb.shankephone.pay.PanChanManager;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.cssweb.shankephone.settings.zxing.encoding.EncodingHandler;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.UUID;
//
///**
// * Created by lenovo on 2017/4/13.
// * 电子单程票二维码页面、刷码进出站页面
// */
//
//public class QRCodeTicketPassGateActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, View.OnClickListener {
//    private static final String TAG = "QRCodeTicketPassGateActivity";
//
//    private static final long DELAY_TIME_GET_QR_CODE_INFO = 1000 * 60 * 10;
//    private static final long DELAY_TIME_UPDATE_TIME_STAMP = 2500;
//    private static final long DELAY_TIME_GET_QR_CODE_STATUS = 1000 * 2;
//    private static final int ROTATION_REFRESH = 300;
//    private static final long DELAY_TIME_SCAN_BLUETOOTH_DEICE_TASK = 900;
//    private static final int SCAN_DEVICE_TIME = 700;
//
//    private static final String CUSTOM_TYPE_ENTRY = "1";//进站
//    private static final String CUSTOM_TYPE_EXIT = "2";//出站
//    private static final String CUSTOM_TYPE_OVER_TIME = "3";//超时
//    private static final String CUSTOM_TYPE_OVER_JOURNEY = "4";//超乘
//    private static final String CUSTOM_TYPE_OVER_JOURNEY_AND_TIME = "5";//超时+超乘
//
//    private static final String ORG_TYPE_ENTRY = "04";//进站
//    private static final String ORG_TYPE_EXIT = "05";//出站
//    private static final String ORG_TYPE_OVER_TIME = "30";//超时
//    private static final String ORG_TYPE_OVER_JOURNEY = "31";//超乘
//    private static final String ORG_TYPE_OVER_JOURNEY_AND_TIME = "32";//超时+超乘
//
//
//    private Bitmap mQrCodeBmp;
//
//    private ImageView mImgQrcode;
//    private ImageView mRefresh;
//    private NoticeDialog mExternalDialog;
//
//    private Handler mHandler = new Handler();
//    private STGateway mSTGateway;
//    //    private PanChanManager mPanchanManager;
//    private WalletGateway mWalletGateway;
//    private GetTicketOrderInfoRs mGetTicketOrderInfoRs;
//    private SingleTicketManager mSingleTicketManager;
//
//    private String mOrderNO;
//    private String mCityCode;
//    private String externalSubId = null;
//    private String externalOrderId = null;
//    private String mQrCodeData;//二维码数据
//    private String timestamp;//有效时间
//    //    private int mCurrentStatus;
//    private int mQrCodeSize;
//    private int mGateStatus;//状态-进闸、出闸
//    private int mSjtId;
//    private boolean isBackground = false;
//    private long mServerTimeDiff = 0;
//
//    private Timer mQrCodeInfoTimer = new Timer();//获取二维码token定时器
//    private Timer mUpdateTimeStampTimer = new Timer();//刷新时间戳定时器
//    private Timer mScanDeviceTimer = new Timer();//扫描PAD定时器
//    private Timer mStatusTimer = new Timer();//获取单程票状态定时器
//    private TimerTask mGetQrCodeInfoTask;//刷新二维码内容task
//    private TimerTask mUpdateLocalTimeTask;//刷新本地时间戳task
//    private TimerTask mScanBlueToothDeviceTask;//扫描蓝牙设备任务
//    private static final SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
//
//    private BluetoothAdapter mBluetoothAdapter;
//    private CenterManager mCenterManager;
//    private List<ScanResult> mScanResultList = new ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_qrcode_pass_gate);
//        MLog.d(TAG, "onCreate");
//        BizApplication.getInstance().addActivity(this);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//不锁屏 高亮
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);//防止屏幕截屏
//        registerQrCodeBroadcast();//注册广播
//        initData();
//        initView();
//        initQRCode();
//
//        initBluetoothOpt();
//    }
//
//    /**
//     * 初始化蓝牙扫描
//     */
//    private void initBluetoothOpt() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //提示用户开启蓝牙
//            if (!isBluetoothOpened()) {
//                Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivityForResult(mIntent, 99);
//            } else if (isBluetoothOpened()) {
//                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                mCenterManager = new CenterManager(QRCodeTicketPassGateActivity.this);
//                mCenterManager.setOnCenterCallback(mCenterCallback);
//                startScanDeviceTask();
//            } else {
//                MLog.d(TAG, "UNKNOWN bluetooth state");
//            }
//        } else {
//            MLog.d(TAG, "current OS version = " + Build.VERSION.SDK_INT);
//        }
//    }
//
//    /**
//     * 开始扫描蓝牙设备
//     */
//    private void startScanDevice() {
//        if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY) {
//            mCenterManager.startScanLeDevice(CssConstant.SINGLE_TICKET.UUID_SERVICE_ENTRY_GATE, SCAN_DEVICE_TIME);
//        } else if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT) {
//            mCenterManager.startScanLeDevice(CssConstant.SINGLE_TICKET.UUID_SERVICE_EXIT_GATE, SCAN_DEVICE_TIME);
//        } else {
//            MLog.d(TAG, "UNKNOWN gate status");
//        }
//    }
//
//    private void initData() {
//        mSTGateway = new STGateway(this);
//        mWalletGateway = new WalletGateway(this);
//        mSingleTicketManager = new SingleTicketManager(this);
//
//        mOrderNO = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        mQrCodeData = getIntent().getStringExtra(CssConstant.KEY_QR_CODE_TOKEN);
//        mGateStatus = getIntent().getIntExtra(CssConstant.KEY_GATE_STATUS, -1);
//        mSjtId = getIntent().getIntExtra(CssConstant.KEY_SJTID, -1);
//        MLog.d(TAG, "mOrderNO = " + mOrderNO + "  gate status = " + mGateStatus + "  sjtId= " + mSjtId);
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        mQrCodeSize = getResources().getDimensionPixelSize(R.dimen.st_qr_code_size);
//        //监听蓝牙状态变化
//        registerReceiver(mBluetoothReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
//
//    }
//
//    private void initView() {
//        TitleBarView mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
//        mTitleBar.setTitle(getString(R.string.qrcode_ticket));
//        mTitleBar.setOnTitleBarClickListener(this);
//        mImgQrcode = (ImageView) findViewById(R.id.iv_qrcode);
//        mRefresh = (ImageView) findViewById(R.id.iv_refresh);
//        //        mRefresh.setOnClickListener(this);
//        findViewById(R.id.ll_refresh).setOnClickListener(this);
//
//        mExternalDialog = new NoticeDialog(this, NoticeDialog.TWO_BUTTON);
//        mExternalDialog.setButtonName(getString(R.string.ok), getString(R.string.qrcode_over_pay_cancel));
//        mExternalDialog.setTitle(getString(R.string.dialog_head));
//        mExternalDialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//            @Override
//            public void onLeftButtonClicked(View view) {
//                getOrderInfo();
//            }
//
//            @Override
//            public void onRightButtonClicked(View view) {
//                mExternalDialog.dismiss();
//                //                jumpToOrder();
//                //                moveToOrderPage(CssConstant.Action.ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER);
//            }
//        });
//    }
//
//    /**
//     * 获取订单详情
//     */
//    private void getOrderInfo() {
//        if (TextUtils.isEmpty(externalSubId) || TextUtils.isEmpty(mCityCode)) {
//            MLog.e(TAG, "externalSubId is null or CityCode is null");
//            return;
//        }
//        showProgress();
//        mSTGateway.getTicketOrderInfo(externalSubId, mCityCode, new MobileGateway.MobileGatewayListener<GetTicketOrderInfoRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                CommonToast.onFailed(getApplicationContext(), result);
//                finish();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgress();
//                CommonToast.onHttpFailed(getApplicationContext());
//                finish();
//            }
//
//            @Override
//            public void onSuccess(GetTicketOrderInfoRs response) {
//                dismissProgress();
//                mGetTicketOrderInfoRs = response;
//                //超乘补票下单
//                externalPay(response);
//                //                order = response.getPurchaseOrder();
//                //                showOrderInfo(order);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgress();
//                CommonToast.onNoNetwork(getApplicationContext());
//                finish();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgress();
//                getOrderInfo();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgress();
//                logoutApp();
//            }
//
//        });
//    }
//
//    /**
//     * 超乘/超时下单
//     *
//     * @param response
//     */
//    private void externalPay(GetTicketOrderInfoRs response) {
//        if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//            getPanchanToken();
//        } else {
//            PanChanManager.getInstance(QRCodeTicketPassGateActivity.this).pay(response.getPanchanPayInfo(), BizApplication.getInstance().getPanchanToken(), new PanChanManager.HandlePanchanPayResultListener() {
//                @Override
//                public void onPaySuccess(String msg, String orderNum) {
//                    //支付成功跳转到支付完成页面
//                    SingleTicketManager.moveToPaySuccessPage(QRCodeTicketPassGateActivity.this, CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, mOrderNO, mCityCode, CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT, true, externalSubId);
//                    finish();
//                }
//
//                @Override
//                public void onPayFailed(String msg) {
//                    MLog.d(TAG, "PayFailed=" + msg);
//                    PanChanManager.getInstance(QRCodeTicketPassGateActivity.this).handleCommonFailedMsg(msg);
//                }
//
//            });
//        }
//    }
//
//
//    private void registerQrCodeBroadcast() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CssConstant.Action.ACTION_QRCODE_ENTRY_STATION_SUCCESS);
//        filter.addAction(CssConstant.Action.ACTION_QRCODE_EXIT_STATION_SUCCESS);
//        filter.addAction(CssConstant.Action.ACTION_QRCODE_OVER_TIME);//二维码单程票超时
//        filter.addAction(CssConstant.Action.ACTION_QRCODE_OVER_RIDE);//二维码单程票超乘
//        filter.addAction(CssConstant.Action.ACTION_QRCODE_OVER_TIME_RIDE);//二维码单程票超时和超乘
//        filter.addAction(CssConstant.Action.ACTION_QRCODE_MULTI_ENTRY);//二维码单程票重复进站
//        LocalBroadcastManager.getInstance(this).registerReceiver(mPushReceiver, filter);
//    }
//
//    //接收推送相关
//    private BroadcastReceiver mPushReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            //            String mTicketStatus = intent.getStringExtra(SingleTicketManager.CLOUD_TICKET_STATUS);
//            MLog.d(TAG, "action = " + action);
//            if (!TextUtils.isEmpty(action)) {
//                if (action.equals(CssConstant.Action.ACTION_QRCODE_ENTRY_STATION_SUCCESS)) {
//                    //                    CommonToast.toast(QRCodeTicketShowActivity.this, getString(R.string.register_qr_data_in_station));
//                    //                    CommonToast.toast(getApplicationContext(), "推送进站~~~~");
//                    String snapshotUrl = intent.getStringExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL);
//                    String eventUrl = intent.getStringExtra(CssConstant.KEY_EVENT_URL);
//                    stopBleOpt();
//                    entryGateSuccess(snapshotUrl, eventUrl);
//                } else if (action.equals(CssConstant.Action.ACTION_QRCODE_EXIT_STATION_SUCCESS)) {
//                    //                    CommonToast.toast(QRCodeTicketShowActivity.this, getString(R.string.register_qr_data_out_station));
//                    //                    CommonToast.toast(getApplicationContext(), "推送出站~~~~");
//                    stopBleOpt();
//                    exitGateSuccess();
//                } else if (action.equals(CssConstant.Action.ACTION_QRCODE_OVER_TIME)) {//二维码单程票超时
//                    externalOrderId = intent.getStringExtra(CssConstant.KEY_ORDERID);
//                    externalSubId = intent.getStringExtra(CssConstant.KEY_SUB_ORDER_ID);
//                    String amount = Utils.parseMoney(intent.getStringExtra(CssConstant.KEY_AMOUNT));
//                    mExternalDialog.show(getString(R.string.qrcode_over_time) + amount + getString(R.string.yes_or_no_pay));
//                    getOrderInfo();
//                } else if (action.equals(CssConstant.Action.ACTION_QRCODE_OVER_RIDE)) {//二维码单程票超乘
//                    externalOrderId = intent.getStringExtra(CssConstant.KEY_ORDERID);
//                    externalSubId = intent.getStringExtra(CssConstant.KEY_SUB_ORDER_ID);
//                    String amount = Utils.parseMoney(intent.getStringExtra(CssConstant.KEY_AMOUNT));
//                    mExternalDialog.show(getString(R.string.qrcode_over_ride) + amount + getString(R.string.yes_or_no_pay));
//
//                } else if (action.equals(CssConstant.Action.ACTION_QRCODE_OVER_TIME_RIDE)) {//二维码单程票超时和超乘
//                    externalOrderId = intent.getStringExtra(CssConstant.KEY_ORDERID);
//                    externalSubId = intent.getStringExtra(CssConstant.KEY_SUB_ORDER_ID);
//                    String amount = Utils.parseMoney(intent.getStringExtra(CssConstant.KEY_AMOUNT));
//                    mExternalDialog.show(getString(R.string.qrcode_over_time_ride) + amount + getString(R.string.yes_or_no_pay));
//                } else if (action.equals(CssConstant.Action.ACTION_QRCODE_MULTI_ENTRY)) {
//                    //重复进站
//                    NoticeDialog dialog = new NoticeDialog(QRCodeTicketPassGateActivity.this, NoticeDialog.ONE_BUTTON);
//                    String msg = intent.getStringExtra(CssConstant.KEY_PUSH_MSG);
//                    dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//                        @Override
//                        public void onLeftButtonClicked(View view) {
//                            moveToOrderPage(CssConstant.Action.ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER);
//                        }
//
//                        @Override
//                        public void onRightButtonClicked(View view) {
//
//                        }
//                    });
//                    dialog.show(msg);
//                }
//            }
//        }
//    };
//    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            //            String mTicketStatus = intent.getStringExtra(SingleTicketManager.CLOUD_TICKET_STATUS);
//            MLog.d(TAG, "action = " + action);
//            if (!TextUtils.isEmpty(action)) {
//                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
//                MLog.d(TAG, " bluetooth state  = " + blueState);
//                if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
//                    switch (blueState) {
//                        case BluetoothAdapter.STATE_TURNING_ON:
//                            break;
//                        case BluetoothAdapter.STATE_ON:
//                            //开始扫描广播
//                            initBluetoothOpt();
//                            break;
//                        case BluetoothAdapter.STATE_TURNING_OFF:
//                            break;
//                        case BluetoothAdapter.STATE_OFF:
//                            break;
//                    }
//                }
//            }
//        }
//    };
//
//    /**
//     * 跳转至进站成功页面
//     */
//    private void entryGateSuccess(String snapshotUrl, String eventUrl) {
//        Intent inIntent = new Intent(getApplicationContext(), EntryExitSuccessActivity.class);
//        inIntent.putExtra(SingleTicketManager.ST_ORDER_ID, mOrderNO);
//        inIntent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//        inIntent.putExtra(CssConstant.KEY_GATE_STATUS, CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY);
//        inIntent.putExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL, snapshotUrl);
//        inIntent.putExtra(CssConstant.KEY_EVENT_URL, eventUrl);
//        startActivity(inIntent);
//        finish();
//    }
//
//    /**
//     * 跳转至进站成功页面
//     */
//    private void exitGateSuccess() {
//        Intent outIntent = new Intent(getApplicationContext(), EntryExitSuccessActivity.class);
//        outIntent.putExtra(SingleTicketManager.ST_ORDER_ID, mOrderNO);
//        outIntent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//        outIntent.putExtra(CssConstant.KEY_GATE_STATUS, CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT);
//        startActivity(outIntent);
//        finish();
//    }
//
//    private void initQRCode() {
//        if (!TextUtils.isEmpty(mQrCodeData)) {
//            showQrCode();
//        } else {
//            getQrCodeSjt(mOrderNO, mCityCode);
//        }
//    }
//
//    /**
//     * 显示二维码
//     */
//    private void showQrCode() {
//        BizApplication.getInstance().getThreadPool().execute(new Runnable() {
//            @Override
//            public void run() {
//                //                Calendar cal = Calendar.getInstance();
//                //                cal.set(1970, 0, 1, 0, 0, 0);
//                //                MLog.d(TAG, "1970  : " + mDateFormat.format(cal.getTimeInMillis()));
//                long ntpServerDiff = BizApplication.getInstance().getNtpTimeDiff();
//                MLog.d(TAG, "ntp time diff = " + ntpServerDiff);
//                long expireTime = System.currentTimeMillis() - ntpServerDiff + 5000;
//                int localTimeInfo = (int) (expireTime / 1000);
//                //                MLog.d(TAG, "currentTime = " + Utils.addLeftChar(Integer.toHexString((int) (System.currentTimeMillis()) / 1000), 8, "0"));
//                String localTimeInfoStr = Utils.addLeftChar(Integer.toHexString(localTimeInfo), 8, "0");
//                String md5Info = Utils.encodeByMD5(String.valueOf(localTimeInfo));
//                String buildLocalInfo = String.valueOf(localTimeInfoStr) + md5Info.substring(0, md5Info.length() / 4);
//                String qrCodeData = mQrCodeData + buildLocalInfo;
//                MLog.d(TAG, "expireTime = " + expireTime + " int" + localTimeInfo + " qrCodeData = " + qrCodeData);
//                //                MLog.d(TAG, "secure info1 = " + localTimeInfoStr);
//                //                MLog.d(TAG, "secure info2 = " + md5Info);
//                //                MLog.d(TAG, "buildLocalInfo = " + buildLocalInfo);
//                //                MLog.d(TAG, "qrCodeData = " + qrCodeData);
//                try {
//                    mQrCodeBmp = EncodingHandler.createQRCode(qrCodeData, mQrCodeSize);
//                    if (mQrCodeBmp != null) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mImgQrcode.setImageBitmap(mQrCodeBmp);
//                            }
//                        });
//
//                    }
//                } catch (Exception e) {
//                    MLog.d(TAG, "show qrCode occur error : ", e);
//                }
//            }
//        });
//
//    }
//
//    /**
//     * 获取二维码单程票
//     *
//     * @param orderNo
//     * @param cityCode
//     */
//    private void getQrCodeSjt(String orderNo, String cityCode) {
//        mSTGateway.getQrCodeSjt(cityCode, orderNo, CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, new MobileGateway.MobileGatewayListener<GetQrCodeSjtRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                if (result.getCode() == 100) {
//                    // 已出站
//                    //                    jumpOutStationAct();
//                } else {
//                    CommonToast.onFailed(getApplicationContext(), result);
//                }
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgress();
//                CommonToast.onHttpFailed(getApplicationContext());
//            }
//
//            @Override
//            public void onSuccess(GetQrCodeSjtRs response) {
//                if (response.getResult().getCode() == Result.OK) {
//                    if (!TextUtils.isEmpty(response.getQrCodeData())) {
//                        mQrCodeData = response.getQrCodeData();
//                        showQrCode();
//                    } else {
//                        CommonToast.toast(getApplicationContext(), getString(R.string.qr_data_is_null));
//                    }
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgress();
//                CommonToast.onNoNetwork(getApplicationContext());
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getQrCodeSjt(mOrderNO, mCityCode);
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgress();
//                logoutApp();
//            }
//
//        });
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        finish();
//    }
//
//    @Override
//    public void onBackPressed() {
//        finish();
//    }
//
//    @Override
//    public void onMenuClicked(View view) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.ll_refresh:
//                rotationRefresh();
//                restartQrCodeAndTimer();
//                break;
//        }
//    }
//
//    private void restartQrCodeAndTimer() {
//        getQrCodeSjt(mOrderNO, mCityCode);
//    }
//
//    private void rotationRefresh() {
//        RotateAnimation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setFillAfter(true);
//        animation.setDuration(ROTATION_REFRESH);
//        mRefresh.startAnimation(animation);
//    }
//
//    @Override
//    protected void onDestroy() {
//        stopTimer();
//        super.onDestroy();
//        MLog.d(TAG, "onDestroy");
//        BizApplication.getInstance().removeActivity(this);
//        if (mPushReceiver != null) {
//            LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushReceiver);
//        }
//        unregisterReceiver(mBluetoothReceiver);
//        if (mExternalDialog.isShowing()) {
//            mExternalDialog.dismiss();
//            mExternalDialog = null;
//        }
//        //        startStopBluetoothOpt();
//        stopBleOpt();
//    }
//
//    //开始定时任务
//    private void startScheduleTask() {
//        mGetQrCodeInfoTask = new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        rotationRefresh();
//                        getQrCodeSjt(mOrderNO, mCityCode);
//                    }
//                });
//            }
//        };
//        mUpdateLocalTimeTask = new TimerTask() {
//            @Override
//            public void run() {
//                mExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        BizApplication.getInstance().setNtpTimeDiff(mSingleTicketManager.getNtpTime());
//                    }
//                });
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        showQrCode();
//                    }
//                });
//            }
//        };
//        //        //获取二维码单程票状态
//        //        mGetQrCodeStatusTask = new TimerTask() {
//        //            @Override
//        //            public void run() {
//        //                mHandler.post(new Runnable() {
//        //                    @Override
//        //                    public void run() {
//        //                        getQrCodeSjtStatus(mOrderNO, mCityCode);
//        //                    }
//        //                });
//        //            }
//        //        };
//        //定时获取二维码信息
//        mQrCodeInfoTimer.schedule(mGetQrCodeInfoTask, DELAY_TIME_GET_QR_CODE_INFO, DELAY_TIME_GET_QR_CODE_INFO);
//        //定时本地刷新二维码
//        mUpdateTimeStampTimer.schedule(mUpdateLocalTimeTask, DELAY_TIME_UPDATE_TIME_STAMP, DELAY_TIME_UPDATE_TIME_STAMP);
//
//    }
//
//    //定时扫描PAD广播
//    private void startScanDeviceTask() {
//        if (mScanBlueToothDeviceTask != null) {
//            mScanBlueToothDeviceTask.cancel();
//        }
//        mScanBlueToothDeviceTask = new TimerTask() {
//            @Override
//            public void run() {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        startScanDevice();
//                    }
//                });
//            }
//        };
//        mScanDeviceTimer.schedule(mScanBlueToothDeviceTask, 0, DELAY_TIME_SCAN_BLUETOOTH_DEICE_TASK);
//    }
//
//    private void stopTimer() {
//        if (mQrCodeInfoTimer != null)
//            mQrCodeInfoTimer.cancel();
//        if (mStatusTimer != null) {
//            mStatusTimer.cancel();
//        }
//        if (mUpdateTimeStampTimer != null) {
//            mUpdateTimeStampTimer.cancel();
//        }
//    }
//
//    private void stopScheduleTask() {
//        if (mGetQrCodeInfoTask != null) {
//            mGetQrCodeInfoTask.cancel();
//        }
//        if (mUpdateLocalTimeTask != null) {
//            mUpdateLocalTimeTask.cancel();
//        }
//    }
//
//    //    private void startTimer() {
//    //        mQrCodeInfoTimer = new Timer();
//    //        mStatusTimer = new Timer();
//    //        startScheduleTask();
//    //
//    //        mQrCodeInfoTimer.schedule(mGetQrCodeInfoTask, DELAY_TIME_GET_QR_CODE_INFO, DELAY_TIME_GET_QR_CODE_INFO);
//    //        //        mStatusTimer.schedule(mGetQrCodeStatusTask, DELAY_TIME_GET_QR_CODE_STATUS, DELAY_TIME_GET_QR_CODE_STATUS);
//    //    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //        restartQrCodeAndTimer();
//        //屏幕高亮
//        MLog.d(TAG, "onResume");
//        Window window = this.getWindow();
//        WindowManager.LayoutParams lp = window.getAttributes();
//        lp.screenBrightness = 1;
//        window.setAttributes(lp);
//        startScheduleTask();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        //        stopTimer();
//        MLog.d(TAG, "onPause");
//        stopScheduleTask();
//    }
//
//    private void getPanchanToken() {
//        showProgress();
//        mWalletGateway.getPanchanToken(new MobileGateway.MobileGatewayListener<GetPanchanTokenRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                Toast.makeText(getApplicationContext(), getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgress();
//                handleHttpFaild(statusCode);
//            }
//
//            @Override
//            public void onSuccess(GetPanchanTokenRs response) {
//                dismissProgress();
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    Toast.makeText(getApplicationContext(), getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                } else {
//                    if (mGetTicketOrderInfoRs != null)
//                        externalPay(mGetTicketOrderInfoRs);
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgress();
//                CommonToast.onNoNetwork(getApplicationContext());
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgress();
//                getPanchanToken();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgress();
//                handleAutoLoginFailed(result);
//            }
//        });
//    }
//
//    private boolean isBluetoothOpened() {
//        if (mBluetoothAdapter != null && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 蓝牙Center角色回调。处理扫描、连接、发现服务等操作
//     */
//
//    private CenterManager.OnCenterCallback mCenterCallback = new CenterManager.OnCenterCallback() {
//        @Override
//        public void onStartScan() {
//            MLog.d(TAG, "onStartScan");
//
//        }
//
//        @Override
//        public void onScanComplete(final ArrayList<ScanResult> list) {
//            MLog.d(TAG, "onScanComplete");
//            //扫描完成，检查已扫描到的设备中是否有与当前sjtId相同的广播，有相同的发起连接
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        for (ScanResult result : list) {
//                            BluetoothDevice device = result.getDevice();
//                            if (device != null) {
//                                if (mScanResultList.size() == 0) {
//                                    mScanResultList.add(result);
//                                } else {
//                                    boolean isAdd = false;
//                                    for (ScanResult scanResult : mScanResultList) {
//                                        BluetoothDevice deviceAdded = scanResult.getDevice();
//                                        if (device.getAddress().equals(deviceAdded.getAddress())) {
//                                            isAdd = true;
//                                        }
//                                    }
//                                    if (!isAdd) {
//                                        mScanResultList.add(result);
//                                    }
//                                }
//                            }
//                        }
//                        MLog.d(TAG, "scan result list size = " + mScanResultList.size());
//                        //----连接pad获取数据 start----
//                        //                        for (ScanResult result : mScanResultList) {
//                        //                            ScanRecord scanRecord = result.getScanRecord();
//                        //                            int sjtId = parseSjtId(scanRecord);
//                        //                            MLog.d(TAG, "current sjtId = " + mSjtId);
//                        //                            if (sjtId != -1 && sjtId == mSjtId) {
//                        //                                stopBleOpt();
//                        //                                mCenterManager.connect(result.getDevice().getAddress());
//                        //                                break;
//                        //                            }
//                        //                        }
//                        //----连接pad获取数据 end----
//
//                        //----通过扫描pad获取数据 start----
//                        for (ScanResult result : mScanResultList) {
//
//                            if (result != null) {
//                                ScanRecord scanRecord = result.getScanRecord();
//                                //                                int sjtId = parseSjtId(scanRecord);
//                                MLog.d(TAG, "current sjtId = " + mSjtId);
//                                //                                if (sjtId != -1 && sjtId == mSjtId) {
//                                handleScanResult(getAdvData(scanRecord));
//                                //                                    break;
//                                //                                }
//                            }
//                        }
//                        mScanResultList.clear();
//                        //----通过扫描pad获取数据 start----
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onScanFailed(int code) {
//            MLog.d(TAG, "onScanFailed");
//        }
//
//        @Override
//        public void onGattServiceDiscovered(final BluetoothGatt gatt) {
//            MLog.d(TAG, "onGattServiceDiscovered");
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        List<BluetoothGattService> list = null;
//                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                            list = gatt.getServices();
//                            if (list != null) {
//                                //                            StringBuilder stringBuilder = new StringBuilder();
//                                MLog.d(TAG, "BluetoothGattService list size = " + list.size());
//                                for (BluetoothGattService service : list) {
//                                    UUID uuid = service.getUuid();
//                                    UUID currentGateUUID;
//                                    if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY) {
//                                        currentGateUUID = CssConstant.SINGLE_TICKET.UUID_SERVICE_ENTRY_GATE;
//                                    } else if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT) {
//                                        currentGateUUID = CssConstant.SINGLE_TICKET.UUID_SERVICE_EXIT_GATE;
//                                    } else {
//                                        MLog.d(TAG, "UNKNOWN gate status");
//                                        return;
//                                    }
//                                    MLog.d(TAG, "CURRENT UUID  =" + currentGateUUID.toString());
//                                    MLog.d(TAG, "remote uuid = " + uuid.toString());
//                                    if (uuid.toString().equalsIgnoreCase(currentGateUUID.toString())) {
//                                        MLog.d(TAG, "readCharacteristic");
//                                        BluetoothGattCharacteristic characteristic = service.getCharacteristic(CssConstant.SINGLE_TICKET.UUID_CHARACTERISTIC_DATA_SHARE);
//                                        gatt.setCharacteristicNotification(characteristic, true);
//                                        //                                    characteristic.setValue(HexConverter.hexStringToBytes("9520"));
//                                        //                                    BluetoothGattDescriptor descriptor = characteristic.getDescriptor(CssConstant.SINGLE_TICKET.UUID_CHARACTERISTIC_DATA_SHARE);
//                                        //                                    if (descriptor != null) {
//                                        //                                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                                        //                                        descriptor.setValue(HexConverter.hexStringToBytes("9520"));
//                                        //                                        //                                    final boolean result = gatt.writeDescriptor(descriptor);
//                                        //                                        //                                    Log.d(TAG, "writeDescriptor result = " + result);
//                                        //                                    } else
//                                        //                                        MLog.d(TAG, "descriptor is null");
//                                        gatt.readCharacteristic(characteristic);
//                                        break;
//                                    } else {
//                                        MLog.d(TAG, "NO MATCHED UUID");
//                                        startScanDeviceTask();
//                                    }
//
//                                    //                                stringBuilder.append("UUID = " + uuid.toString()).append("\n");
//                                }
//                                //                        CustomDialog.showDialog(ScanDeviceActivity.this, stringBuilder.toString());
//                            }
//                        }
//                    } catch (Exception e) {
//                        MLog.d(TAG, "onGattServiceDiscovered occur error : ", e);
//                    }
//                }
//            });
//
//        }
//
//        @Override
//        public void onGattServiceDiscoveredFailed(int code) {
//            MLog.d(TAG, "onGattServiceDiscoveredFailed " + code);
//            //            dissMissProgress();
//        }
//
//        @Override
//        public void onConnectSuccess(final BluetoothGatt gatt) {
//            //            showProgress("查找服务");
//            MLog.d(TAG, "onConnectSuccess");
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                        boolean startDiscoverResult = gatt.discoverServices();
//                    }
//
//                }
//            });
//        }
//
//        @Override
//        public void onConnectFailed(int code) {
//            MLog.d(TAG, "onConnectFailed " + code);
//            startScanDevice();
//            //            startScanDeviceTask();
//        }
//
//        @Override
//        public void onWriteDataSuccess() {
//            MLog.d(TAG, "onWriteDataSuccess ");
//
//        }
//
//        @Override
//        public void onWriteDataFailed(int code) {
//            MLog.d(TAG, "onWriteDataFailed " + code);
//
//        }
//
//        @Override
//        public void onCenterReceiveData(final String data) {
//            MLog.d(TAG, "onCenterReceiveData  = " + data);
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (!TextUtils.isEmpty(data) && data.length() == 40) {
//                        try {
//                            //解析PAD传过来的数据，比对sjtId是否相同;
//                            byte[] bytesData = HexConverter.hexStringToBytes(data);
//                            if (bytesData != null) {
//                                byte[] byteSjtId = Arrays.copyOfRange(bytesData, 0, 4);
//                                int remoteSjtId = ByteConverter.byteToInt(byteSjtId);
//                                if (mSjtId != -1 && mSjtId == remoteSjtId) {
//                                    //                                    CommonToast.toast(getApplicationContext(), "收到消息啦~~~~~~~");
//                                    byte[] byteSjtStatus = Arrays.copyOfRange(bytesData, 4, 5);
//                                    byte[] byteDeviceCode = Arrays.copyOfRange(bytesData, 5, 10);
//                                    byte[] byteTrxType = Arrays.copyOfRange(bytesData, 10, 11);
//                                    byte[] byteTrxAmount = Arrays.copyOfRange(bytesData, 11, 13);
//                                    byte[] byteHandleDate = Arrays.copyOfRange(bytesData, 13, 20);
//
//                                    String sjtStatus = ByteConverter.bcd2Str(byteSjtStatus);
//                                    String deviceCode = ByteConverter.bcd2Str(byteDeviceCode);
//                                    String trxType = ByteConverter.bcd2Str(byteTrxType);
//                                    String trxAmountStr = HexConverter.bytesToHexString(byteTrxAmount);
//                                    String trxAmountStr2 = Utils.addLeftChar(trxAmountStr, 8, "0");
//                                    int trxAmount = ByteConverter.byteToInt(HexConverter.hexStringToBytes(trxAmountStr2));
//                                    String handleDate = ByteConverter.bcd2Str(byteHandleDate);
//                                    MLog.d(TAG, "remoteSjtId = " + remoteSjtId + " sjtStatus = " + sjtStatus + " deviceCode = " + deviceCode + " trxType = " + trxType + " trxAmount = " + trxAmount + " handleDate =" + handleDate);
//                                    stopBleOpt();
//                                    mSTGateway.qrStatusUpdateNotice(mCityCode, deviceCode, mSjtId, sjtStatus, handleDate, trxType, trxAmount, null);
//                                } else {
//                                    MLog.d(TAG, "sjtId not matched");
//                                }
//                            } else {
//                                MLog.d(TAG, "bluetooth data is null");
//                            }
//                        } catch (Exception e) {
//                            MLog.d(TAG, " parse bluetooth data occur error: ", e);
//                        }
//                    }
//                }
//            });
//        }
//    };
//
//    private void handleScanResult(String advData) {
//        if (!TextUtils.isEmpty(advData) && advData.length() == 32) {
//            MLog.d(TAG, "Broad cast type 1");
//            byte[] bytesData = HexConverter.hexStringToBytes(advData);
//            if (bytesData != null) {
//                byte[] byteSjtId = Arrays.copyOfRange(bytesData, 0, 4);
//                int remoteSjtId = ByteConverter.byteToInt(byteSjtId);
//                if (mSjtId != -1 && mSjtId == remoteSjtId) {
//                    //                                    CommonToast.toast(getApplicationContext(), "收到消息啦~~~~~~~");
//                    byte[] byteSjtStatus = Arrays.copyOfRange(bytesData, 4, 5);
//                    byte[] byteDeviceCode = Arrays.copyOfRange(bytesData, 5, 10);
//                    byte[] byteTrxType = Arrays.copyOfRange(bytesData, 10, 11);
//                    byte[] byteTrxAmount = Arrays.copyOfRange(bytesData, 11, 13);
//                    byte[] byteHandleDate = Arrays.copyOfRange(bytesData, 13, 16);
//
//                    String sjtStatus = ByteConverter.bcd2Str(byteSjtStatus);
//                    String deviceCode = ByteConverter.bcd2Str(byteDeviceCode);
//                    String trxType = ByteConverter.bcd2Str(byteTrxType);
//                    String trxAmountStr = HexConverter.bytesToHexString(byteTrxAmount);
//                    String trxAmountStr2 = Utils.addLeftChar(trxAmountStr, 8, "0");
//                    int trxAmount = ByteConverter.byteToInt(HexConverter.hexStringToBytes(trxAmountStr2));
//                    String handleDate = format2.format(System.currentTimeMillis()) + ByteConverter.bcd2Str(byteHandleDate);
//                    MLog.d(TAG, "remoteSjtId = " + remoteSjtId + " sjtStatus = " + sjtStatus + " deviceCode = " + deviceCode + " trxType = " + trxType + " trxAmount = " + trxAmount + " handleDate =" + handleDate);
//                    if (!TextUtils.isEmpty(sjtStatus) && sjtStatus.equalsIgnoreCase("31")) {
//                        try {
//                            Date date = dateFormat.parse(handleDate);
//                            long currentTimes = System.currentTimeMillis();
//                            long handleDateTimeMillis = date.getTime();
//                            long timeDiff = currentTimes - handleDateTimeMillis;
//                            MLog.d(TAG, "timeDiff = " + currentTimes + "  " + handleDateTimeMillis + " " + timeDiff);
//                            if (timeDiff <= 10000) {
//                                stopBleOpt();
//                                mSTGateway.qrStatusUpdateNotice(mCityCode, deviceCode, mSjtId, sjtStatus, handleDate, trxType, trxAmount, null);
//                            }
//                        } catch (Exception e) {
//                            MLog.d(TAG, "parse date occur error : ", e);
//                            stopBleOpt();
//                        }
//                    } else {
//                        stopBleOpt();
//                        mSTGateway.qrStatusUpdateNotice(mCityCode, deviceCode, mSjtId, sjtStatus, handleDate, trxType, trxAmount, null);
//                    }
//                } else {
//                    MLog.d(TAG, "sjtId not matched");
//                }
//            } else {
//                MLog.d(TAG, "bluetooth data is null");
//            }
//        } else if (!TextUtils.isEmpty(advData) && advData.length() == 34) {
//            //第二版蓝牙广播
//            MLog.d(TAG, "Broad cast type 2");
//            try {
//                byte[] bytesData = HexConverter.hexStringToBytes(advData);
//                byte[] type = Arrays.copyOfRange(bytesData, 0, 1);
//                String typeStr = HexConverter.bytesToHexString(type);
//                byte[] sjtIdByte = Arrays.copyOfRange(bytesData, 1, 5);
//                String sjt1Type = convertCustomTypeToS5Type(typeStr.substring(0, 1));
//                int sjt1Id = ByteConverter.byteToInt(sjtIdByte);
//
//                String sjt1StationCode = buildStationCode(HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 5, 9)));
//                String sjt1handleDate = dateFormat.format(System.currentTimeMillis());
//
//                String sjt2Type = convertCustomTypeToS5Type(typeStr.substring(1, 2));
//                int sjt2Id = ByteConverter.byteToInt(Arrays.copyOfRange(bytesData, 9, 13));
//                String sjt2StationCode = buildStationCode(HexConverter.bytesToHexString(Arrays.copyOfRange(bytesData, 13, 17)));
//                String sjt2handleDate = dateFormat.format(System.currentTimeMillis());
//
//                int finalSjtId = -1;//单程票id
//                String finalSjtStatus = "";//单程票状态
//                String finalStationCode = "";//车站编码
//                String finalHandleDate = "";//处理时间
//                String finalTrxType = "";//进出闸类型
//                if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY) {
//                    finalTrxType = "01";
//                } else if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT) {
//                    finalTrxType = "02";
//                }
//                if (mSjtId != -1) {
//                    if (mSjtId == sjt1Id) {
//                        finalSjtId = sjt1Id;
//                        finalSjtStatus = sjt1Type;
//                        finalStationCode = sjt1StationCode;
//                        finalHandleDate = sjt1handleDate;
//                    } else if (mSjtId == sjt2Id) {
//                        finalSjtId = sjt2Id;
//                        finalSjtStatus = sjt2Type;
//                        finalStationCode = sjt2StationCode;
//                        finalHandleDate = sjt2handleDate;
//                    } else {
//                        MLog.d(TAG, " no matched sjt");
//                        return;
//                    }
//                    MLog.d(TAG, "finalSjtStatus = " + finalSjtStatus + " finalHandleDate = " + finalHandleDate + " finalStationCode = " + " finalTrxType = " + finalTrxType);
//
//                    if (!TextUtils.isEmpty(finalSjtStatus) && !TextUtils.isEmpty(finalHandleDate) && !TextUtils.isEmpty(finalStationCode) && !TextUtils.isEmpty(finalTrxType)) {
//                        if (finalSjtStatus.equalsIgnoreCase("31")) {
//                            try {
//                                Date date = dateFormat.parse(finalHandleDate);
//                                long currentTimes = System.currentTimeMillis();
//                                long handleDateTimeMillis = date.getTime();
//                                long timeDiff = currentTimes - handleDateTimeMillis;
//                                MLog.d(TAG, "timeDiff = " + currentTimes + "  " + handleDateTimeMillis + " " + timeDiff);
//                                if (timeDiff <= 10000) {
//                                    stopBleOpt();
//                                    mSTGateway.qrStatusUpdateNotice(mCityCode, finalStationCode, mSjtId, finalSjtStatus, finalHandleDate, finalTrxType, 0, null);
//                                }
//                            } catch (Exception e) {
//                                MLog.d(TAG, "parse date occur error : ", e);
//                                stopBleOpt();
//                            }
//                        } else {
//                            stopBleOpt();
//                            mSTGateway.qrStatusUpdateNotice(mCityCode, finalStationCode, mSjtId, finalSjtStatus, finalHandleDate, finalTrxType, 0, null);
//                        }
//                    } else {
//                        MLog.d(TAG, " final data is invalid");
//                    }
//                } else {
//                    MLog.d(TAG, "current sjtId is invalid");
//                }
//
//
//            } catch (Exception e) {
//                MLog.d(TAG, "PARSE broadcast2 occur error :", e);
//            }
//
//        }
//    }
//
//
//    /**
//     * 获取设备编码
//     */
//    private int parseSjtId(ScanRecord scanRecord) {
//        MLog.d(TAG, "parseSjtId-----");
//        int sjtId = -1;
//        if (scanRecord != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                if (scanRecord.getServiceData() != null) {
//                    //共9个字节，前5个字节-设备编号 bcd
//                    //后4个字节-sjtId 16进制
//                    byte[] broadcastData = scanRecord.getServiceData().get(new ParcelUuid(CssConstant.SINGLE_TICKET.UUID_CHARACTERISTIC_DATA_SHARE));
//                    if (broadcastData != null && broadcastData.length == 16) {
//                        sjtId = ByteConverter.byte2Int(Arrays.copyOfRange(broadcastData, 0, 4));
//                        MLog.d(TAG, "broadcast sjtId = " + sjtId);
//                    } else {
//                        MLog.d(TAG, "broadcast data is null or length is invalid " + (broadcastData != null) + " length = " + (broadcastData != null ? broadcastData.length : 0));
//                    }
//                } else {
//                    MLog.d(TAG, "ServiceData is null ");
//                }
//            } else {
//                MLog.d(TAG, "current os version is = " + Build.VERSION.SDK_INT);
//            }
//        } else {
//            MLog.d(TAG, "scanRecord is null ");
//        }
//        return sjtId;
//    }
//
//    /**
//     * 获取广播数据
//     */
//    private String getAdvData(ScanRecord scanRecord) {
//        String data = "";
//        if (scanRecord != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                if (scanRecord.getServiceData() != null) {
//                    byte[] deviceCode = scanRecord.getServiceData().get(new ParcelUuid(CssConstant.SINGLE_TICKET.UUID_CHARACTERISTIC_DATA_SHARE));
//                    if (deviceCode != null) {
//                        data = HexConverter.bytesToHexString(deviceCode);
//                        MLog.d(TAG, "findDeviceCode = " + data);
//                    } else {
//                        MLog.d(TAG, "getAdvData data is null or length is invalid " + (deviceCode != null) + " length = " + (deviceCode != null ? deviceCode.length : 0));
//                    }
//                }
//            }
//        }
//        return data;
//    }
//
//    /**
//     * 停止扫描蓝牙设备
//     */
//    private void stopScanTask() {
//        mScanBlueToothDeviceTask.cancel();
//    }
//
//    private void startStopBluetoothOpt() {
//        MLog.d(TAG, "startStopBluetoothOpt ");
//        new CountDownTimer(2000, 1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                MLog.d(TAG, "startStopBluetoothOpt finish");
//            }
//        }.start();
//    }
//
//    /**
//     * 停止蓝牙相关操作
//     */
//    private void stopBleOpt() {
//        if (mCenterManager != null) {
//            stopScanTask();
//            mCenterManager.stopScan();
//            mCenterManager.disconnect();
//            mCenterManager.close();
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        MLog.d(TAG, "onActivityResult " + " requestCode = " + requestCode + " resultCode = " + resultCode);
//    }
//
//    /**
//     * 3字节byte转换成int
//     */
//    private int sjtId3ByteToInt(byte[] byteSjtId) {
//        byte[] sjtId = new byte[4];
//        System.arraycopy(byteSjtId, 0, sjtId, 1, byteSjtId.length);
//        return ByteConverter.byteToInt(sjtId);
//    }
//
//    /**
//     * 半个字节自定义类型转换成S5通用类型
//     */
//    private String convertCustomTypeToS5Type(@NonNull String type) {
//        if (!TextUtils.isEmpty(type)) {
//            if (type.equals(CUSTOM_TYPE_ENTRY)) {
//                //进站
//                return ORG_TYPE_ENTRY;
//            } else if (type.equals(CUSTOM_TYPE_EXIT)) {
//                //进站
//                return ORG_TYPE_EXIT;
//            } else if (type.equals(CUSTOM_TYPE_OVER_TIME)) {
//                //超时
//                return ORG_TYPE_OVER_TIME;
//            } else if (type.equals(CUSTOM_TYPE_OVER_JOURNEY)) {
//                //超乘
//                return ORG_TYPE_OVER_JOURNEY;
//            } else if (type.equals(CUSTOM_TYPE_OVER_JOURNEY_AND_TIME)) {
//                //超乘+超时
//                return ORG_TYPE_OVER_JOURNEY_AND_TIME;
//            }
//
//        }
//        return null;
//    }
//
//    private String buildStationCode(String orgDeviceCode) {
//        String deviceCode = "";
//        if (!TextUtils.isEmpty(orgDeviceCode)) {
//            deviceCode = orgDeviceCode.substring(0, 4) + "04" + orgDeviceCode.substring(4, 8);
//        }
//        return deviceCode;
//    }
//}
