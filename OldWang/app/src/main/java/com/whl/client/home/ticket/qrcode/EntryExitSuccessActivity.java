//package com.cssweb.shankephone.home.ticket.qrcode;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.framework.utils.VibratorUtils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.dialog.ImageDialog;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderInfoRs;
//import com.cssweb.shankephone.gateway.model.singleticket.PurchaseOrder;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.cssweb.shankephone.view.RoundLineNameView;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
///**
// * Created by lenovo on 2017/4/14.
// * 进站、出站成功页面
// */
//
//public class EntryExitSuccessActivity extends BaseActivity implements View.OnClickListener, TitleBarView.OnTitleBarClickListener {
//    private static final String TAG = "EntryExitSuccessActivity";
//    private TextView mTvOrderNumber;
//    private RoundLineNameView mTvStartLineName;
//    private TextView mTvStartStationName;
//    private RoundLineNameView mTvEndLineName;
//    private TextView mTvEndStationName;
//    private TextView mTvEntryGateTime;
//    private TextView mTvExitGateTime;
//    private TextView mTvTicketStatus;
//    private RoundLineNameView mTvActualEndLineName;//实际下车线路
//    private TextView mTvActualEndStationName;//实际下车站点
//    private TextView mBtnConfirm;
//    private RelativeLayout mRlyActualExitInfo;
//    private View mViewExitGateTime;//出站时间
//    private TitleBarView mTitleBarView;
//
//    private STGateway mSTGateway;
//    private SingleTicketManager mSingleTicketManager;
//
//    private String mOrderId;
//    private String mCityCode;
//    private int mGateStatus;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_entry_gate_success);
//        initView();
//        onNewIntent(getIntent());
//    }
//
//    private void initData(Intent intent) {
//        mSTGateway = new STGateway(EntryExitSuccessActivity.this);
//        mOrderId = intent.getStringExtra(SingleTicketManager.ST_ORDER_ID);
//        MLog.d(TAG, "orderId = " + mOrderId);
//        mCityCode = intent.getStringExtra(CssConstant.KEY_CITY_CODE);
//        //当前二维码操作状态。进站或出站
//        mGateStatus = intent.getIntExtra(CssConstant.KEY_GATE_STATUS, -1);
//        MLog.d(TAG, "mGageStatus = " + mGateStatus);
//        mSingleTicketManager = new SingleTicketManager(EntryExitSuccessActivity.this);
//        String snapshotUrl = intent.getStringExtra(CssConstant.KEY_EVENT_SNAPSHOT_URL);
//        String eventUrl = intent.getStringExtra(CssConstant.KEY_EVENT_URL);
//        MLog.d(TAG, "snapshotUrl = " + snapshotUrl + " eventUrl = " + eventUrl);
//        if (!TextUtils.isEmpty(snapshotUrl) && !TextUtils.isEmpty(eventUrl)) {
//            ImageDialog dialog = new ImageDialog(EntryExitSuccessActivity.this);
//            dialog.displayImage(snapshotUrl, eventUrl);
//        }
//    }
//
//    private void initView() {
//        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        mTitleBarView.setOnTitleBarClickListener(this);
//        mTvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
//        mTvStartLineName = (RoundLineNameView) findViewById(R.id.tv_start_line_name);
//        mTvStartStationName = (TextView) findViewById(R.id.tv_start_station_name);
//        mTvEndLineName = (RoundLineNameView) findViewById(R.id.tv_end_line_name);
//        mTvEndStationName = (TextView) findViewById(R.id.tv_end_station_name);
//        mTvEntryGateTime = (TextView) findViewById(R.id.tv_entry_gate_time);
//        mBtnConfirm = (TextView) findViewById(R.id.btn_confirm);
//        mTvTicketStatus = (TextView) findViewById(R.id.tv_ticket_status);
//        mTvExitGateTime = (TextView) findViewById(R.id.tv_exit_gate_time);
//        mTvActualEndLineName = (RoundLineNameView) findViewById(R.id.tv_actual_end_line_name);
//        mTvActualEndStationName = (TextView) findViewById(R.id.tv_actual_end_station_name);
//        mRlyActualExitInfo = (RelativeLayout) findViewById(R.id.lly_actual_exit_info);
//        mViewExitGateTime = findViewById(R.id.lly_exit_gate_time);
//
//        mBtnConfirm.setOnClickListener(this);
//
//    }
//
//    /**
//     * 处理进出闸状态
//     */
//    private void handleGateStatus() {
//        if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY) {
//            //进站成功
//            mTvTicketStatus.setText(getString(R.string.entry_gate_success));
//            mViewExitGateTime.setVisibility(View.GONE);
//            mRlyActualExitInfo.setVisibility(View.GONE);
//            mBtnConfirm.setText(getString(R.string.exit_gate));
//            mTitleBarView.setTitle(getString(R.string.entry_gate_success));
//        } else {
//            //出站成功
//            mTitleBarView.setTitle(getString(R.string.exit_gate_success));
//            mTvTicketStatus.setText(getString(R.string.exit_gate_success));
//            mViewExitGateTime.setVisibility(View.VISIBLE);
//            mRlyActualExitInfo.setVisibility(View.VISIBLE);
//            mBtnConfirm.setText(getString(R.string.back_home));
//            mBtnConfirm.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        switch (v.getId()) {
//            case R.id.btn_confirm:
//                if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY)
//                    getQrCodeDataInfo();
//                else {
//                    moveToHomePage();
//                    finish();
//                }
//                break;
//        }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        MLog.d(TAG, "onNewIntent");
//        VibratorUtils.Vibrate(EntryExitSuccessActivity.this, new long[]{100, 100, 0, 0}, false);
//        initData(intent);
//        handleGateStatus();
//        getOrderInfo();
//    }
//
//    /**
//     * 获取进出站二维码内容
//     */
//    private void getQrCodeDataInfo() {
//        showProgress();
//        mSTGateway.getQrCodeSjt(mCityCode, mOrderId, CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, new MobileGateway.MobileGatewayListener<GetQrCodeSjtRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                CommonToast.onFailed(getApplicationContext(), result);
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
//                //跳转到出站二维码页面
//                dismissProgress();
//                Intent intent = new Intent(EntryExitSuccessActivity.this, QRCodeTicketPassGateActivity.class);
//                intent.putExtra(CssConstant.KEY_QR_CODE_TOKEN, response.getQrCodeData());
//                intent.putExtra(SingleTicketManager.ST_ORDER_ID, mOrderId);
//                intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//                intent.putExtra(CssConstant.KEY_SJTID, response.getSjtId());
//                int gateStatus = -1;
//                if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY) {
//                    //当前是进站成功状态,则将 出站 状态传给二维码页面
//                    gateStatus = CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT;
//                } else {
//                    //出站成功，回到订单列表
//                }
//                intent.putExtra(CssConstant.KEY_GATE_STATUS, gateStatus);
//                startActivity(intent);
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
//                getQrCodeDataInfo();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgress();
//                handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    /**
//     * 获取订单信息,并显示
//     */
//    private void getOrderInfo() {
//        if (TextUtils.isEmpty(mOrderId) || TextUtils.isEmpty(mCityCode)) {
//            MLog.e(TAG, "orderId is null or CityCode is null");
//            return;
//        }
//        mSTGateway.getTicketOrderInfo(mOrderId, mCityCode, new MobileGateway.MobileGatewayListener<GetTicketOrderInfoRs>() {
//            @Override
//            public void onFailed(Result result) {
//                CommonToast.onFailed(EntryExitSuccessActivity.this, result);
//                finish();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                CommonToast.onHttpFailed(EntryExitSuccessActivity.this);
//                finish();
//            }
//
//            @Override
//            public void onSuccess(GetTicketOrderInfoRs response) {
//                mTvOrderNumber.setText(mOrderId);
//                PurchaseOrder order = response.getPurchaseOrder();
//                //起点线路简写
//                //                mTvStartLineName.setText(order.getSlineShortName());
//
//                mSingleTicketManager.setLineName(mTvStartLineName, order.getSlineShortName(), order.getSlineBgColor());
//                if (order.getElineShortName() != null && order.getElineBgColor() != null) {
//                    mSingleTicketManager.setLineName(mTvEndLineName, order.getElineShortName(), order.getElineBgColor());
//                }
//                mTvStartStationName.setText(order.getPickupStationNameZH());
//                //                mTvEndLineName.setText(order.getElineShortName());
//                mTvEndStationName.setText(order.getGetoffStationNameZH());
//                mTvEntryGateTime.setText(order.getEntryDatetime());
//                MLog.d(TAG, "gate status = " + mGateStatus + " time = " + order.getExitDatetime());
//                if (mGateStatus == CssConstant.SINGLE_TICKET.GATE_STATUS_EXIT) {
//                    mTvExitGateTime.setText(order.getExitDatetime());
//                    if (order.getGetoffStationCode().equals(order.getActualExitStationCode())) {
//                        mRlyActualExitInfo.setVisibility(View.GONE);
//                    } else {
//                        mRlyActualExitInfo.setVisibility(View.VISIBLE);
//                        mSingleTicketManager.setLineName(mTvActualEndLineName, order.getActualExitlineShortName(), order.getActualExitlineBgColor());
//                        mTvActualEndStationName.setText(order.getActualExitStationName());
//                        //                        mTvActualEndLineName.setText(order.getActualExitStationName());
//                    }
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                CommonToast.onNoNetwork(EntryExitSuccessActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getOrderInfo();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        //        moveToOrderPage(CssConstant.Action.ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER);
//        finish();
//    }
//
//    @Override
//    public void onMenuClicked(View view) {
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        //        moveToOrderPage(CssConstant.Action.ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER);
//        finish();
//    }
//}
