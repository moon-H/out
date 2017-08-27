//package com.cssweb.shankephone.home.ticket.detail;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.TextView;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GetQrCodeSjtRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderInfoRs;
//import com.cssweb.shankephone.gateway.model.singleticket.PurchaseOrder;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.cssweb.shankephone.home.ticket.qrcode.QRCodeTicketPassGateActivity;
//import com.cssweb.shankephone.view.RoundLineNameView;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
///**
// * Created by lh on 2015/11/24.
// * 支付成功页面
// */
//public class STPaySuccessActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, View.OnClickListener {
//    private static final String TAG = "STPaySuccessActivity";
//    //    public static STPaySuccessActivity mSTPayTicketCompleteActivity;
//
//    private TextView mTvPayAmount;
//    private TextView mTvOrderTime;
//    private TextView mTvPayTime;
//    private TextView mTvOrderNumber;
//    private RoundLineNameView mTvStartLineName;
//    private TextView mTvStartStationName;
//    private RoundLineNameView mTvEndLineName;
//    private TextView mTvEndStationName;
//    private TextView mTvConfirm;
//    private View mRlyEndLineParent;
//    private TextView mTvStartLineLabel;
//
//    private STGateway mStGateway;
//
//    private int mStatus;
//    private String mGetTicketToken;
//    private String mParentOrderNumber;
//    private String mCityCode;
//    private String mTempOrderNumber;//临时订单号
//    private String mCategoryCode;
//    private String mServiceId;
//    private int mGateStatus;//状态-进闸、出闸
//    private boolean mIsRepayTicket;//是否是补票订单
//    private String mSubOrderId;
//
//    private PurchaseOrder mPurchaseOrder;
//    private SingleTicketManager mSingleTicketManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_st_pay_success);
//        BizApplication.getInstance().addActivity(this);
//
//        //        mSTPayTicketCompleteActivity = this;
//        //        mFragmentManager = getSupportFragmentManager();
//        initData();
//        initView();
//        getOrderInfo();
//    }
//
//    private void initData() {
//        mStGateway = new STGateway(STPaySuccessActivity.this);
//        mParentOrderNumber = getIntent().getStringExtra(CssConstant.KEY_ORDERID);//主订单号
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        mServiceId = getIntent().getStringExtra(CssConstant.KEY_SERVICE_ID);
//        mGateStatus = getIntent().getIntExtra(CssConstant.KEY_GATE_STATUS, -1);//电子单程票进出闸标识，普通单程票不用
//        mIsRepayTicket = getIntent().getBooleanExtra(CssConstant.KEY_IS_REPAY_TICKET, false);
//        mSubOrderId = getIntent().getStringExtra(CssConstant.KEY_SUB_ORDER_ID);
//        mSingleTicketManager = new SingleTicketManager(STPaySuccessActivity.this);
//        MLog.d(TAG, "mParentOrderNumber = " + mParentOrderNumber + " cityCode=  " + mCityCode + " serviceId = " + mServiceId + " gateStatus = " + mGateStatus + " isRepayTicket = " + mIsRepayTicket + " mSubOrderId =" + mSubOrderId);
//    }
//
//    private void initView() {
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setOnTitleBarClickListener(this);
//        titleBarView.setTitle(getString(R.string.st_order_pay_complete));
//        mTvPayAmount = (TextView) findViewById(R.id.tv_pay_amount);
//        mTvOrderTime = (TextView) findViewById(R.id.tv_order_time);
//        mTvPayTime = (TextView) findViewById(R.id.tv_pay_time);
//        mTvOrderNumber = (TextView) findViewById(R.id.tv_order_number);
//        mTvStartLineName = (RoundLineNameView) findViewById(R.id.tv_start_line);
//        mTvStartStationName = (TextView) findViewById(R.id.tv_start_station);
//        mTvEndLineName = (RoundLineNameView) findViewById(R.id.tv_end_line);
//        mTvEndStationName = (TextView) findViewById(R.id.tv_end_station);
//        mRlyEndLineParent = findViewById(R.id.rly_end_line_parent);
//        mTvStartLineLabel = (TextView) findViewById(R.id.tv_start_label);
//
//        mTvConfirm = (TextView) findViewById(R.id.btn_ok);
//        mTvConfirm.setOnClickListener(this);
//        if (SingleTicketManager.isQrCodeTicket(mServiceId)) {
//            //            mTvConfirm.setTextColor(getResources().getColor(R.color.FFFFFF));
//            mTvConfirm.setText(getString(R.string.st_entry_gate));
//            if (mIsRepayTicket) {
//                mTvStartLineLabel.setText(getString(R.string.st_repay_ticket_station));
//                mRlyEndLineParent.setVisibility(View.GONE);
//                mTvConfirm.setText(getString(R.string.exit_gate));
//            }
//        } else {
//            mRlyEndLineParent.setVisibility(View.VISIBLE);
//            mTvConfirm.setText(getString(R.string.st_getticket));
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//    }
//
//    public void showProgressView() {
//        showProgress();
//    }
//
//    public void dismissProgressView() {
//        //        BizApplication.getInstance().dismissProgressDialog();
//        dismissProgress();
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        finish();
//        //        backOrderPage();
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
//            case R.id.btn_ok:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                //                onConfirmBtnClicked();
//                getQrCodeInfo();
//                break;
//        }
//    }
//
//    /**
//     * 获取支付成功订单信息
//     */
//    private void getOrderInfo() {
//        if (TextUtils.isEmpty(mParentOrderNumber) || TextUtils.isEmpty(mCityCode)) {
//            MLog.e(TAG, "orderId is null or CityCode is null");
//            return;
//        }
//        //        showProgressView();
//        String orderNumber = mParentOrderNumber;
//        if (mIsRepayTicket) {
//            orderNumber = mSubOrderId;
//        }
//        mStGateway.getTicketOrderInfo(orderNumber, mCityCode, new MobileGateway.MobileGatewayListener<GetTicketOrderInfoRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(getApplicationContext(), result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(getApplicationContext());
//            }
//
//            @Override
//            public void onSuccess(GetTicketOrderInfoRs response) {
//                dismissProgressView();
//                mPurchaseOrder = response.getPurchaseOrder();
//                showOrderInfo(mPurchaseOrder);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(getApplicationContext());
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                getOrderInfo();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgressView();
//                logoutApp();
//            }
//
//        });
//    }
//
//    /**
//     * 展示支付订单信息
//     */
//    private void showOrderInfo(final PurchaseOrder order) {
//        MLog.d(TAG, "orderType = " + order.getSingleTicketType());
//        MLog.d(TAG, "order = " + order);
//        mGetTicketToken = order.getTakeTicketToken();
//        mTempOrderNumber = order.getOrderNo();
//        mTvPayAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(" ").append(Utils.parseMoney(order.getTicketTotalAmount())).toString());
//        mTvOrderTime.setText(order.getOrderDate());
//        mTvPayTime.setText(order.getPaymentDate());
//        mTvOrderNumber.setText(mTempOrderNumber);
//
//        mSingleTicketManager.setLineName(mTvStartLineName, order.getSlineShortName(), order.getSlineBgColor());
//        //非补票订单显示终点信息
//        if (order.getElineShortName() != null & order.getElineBgColor() != null) {
//            if (!mIsRepayTicket)
//                mSingleTicketManager.setLineName(mTvEndLineName, order.getElineShortName(), order.getElineBgColor());
//        }
//
//        mTvStartStationName.setText(order.getPickupStationNameZH());
//        //非补票订单显示终点信息
//        if (!mIsRepayTicket)
//            mTvEndStationName.setText(order.getGetoffStationNameZH());
//
//        mStatus = order.getOrderStatus();
//        mCategoryCode = order.getCategoryCode();
//        if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_TICKET)) {
//            mTvConfirm.setText(getString(R.string.st_getticket));
//        } else if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_TICKET)) {
//            mTvConfirm.setText(getString(R.string.st_entry_gate));
//        }
//
//    }
//
//    //    private void onLeftBtnClicked() {
//    //        getRefundRate();
//    //    }
//
//    //    private void onConfirmBtnClicked() {
//    //        if (SingleTicketManager.isQrCodeTicket(mServiceId)) {
//    //            //电子单程票
//    //        } else {
//    //            //普通单程票
//    //            Intent intent = new Intent(STPaySuccessActivity.this, STGetTicketActivity.class);
//    //            intent.putExtra(CssConstant.KEY_QR_CODE_TOKEN, mGetTicketToken);
//    //            intent.putExtra(SingleTicketManager.ST_ORDER_ID, mParentOrderNumber);
//    //            intent.putExtra(CssConstant.KEY_CATEGORY_CODE, mCategoryCode);
//    //            intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//    //            startActivity(intent);
//    //        }
//    //
//    //        //        finish();
//    //    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//        //        backOrderPage();
//    }
//
//    /**
//     * 返回订单页面
//     */
//    private void backOrderPage() {
//        if (mPurchaseOrder != null) {
//            //            if (SingleTicketManager.isQrCodeTicket(mServiceId)) {
//            //                moveToOrderPage(CssConstant.Action.ACTION_SWITCH_FRAGMENT_QR_CODE_ORDER);
//            //            } else {
//            //                moveToOrderPage(CssConstant.Action.ACTION_SWITCH_FRAGMENT_COMMON_TICKET_ORDER);
//            //            }
//            finish();
//        }
//    }
//
//    /**
//     * 获取二维码信息
//     */
//
//    private void getQrCodeInfo() {
//        showProgress();
//        mStGateway.getQrCodeSjt(mCityCode, mParentOrderNumber, mServiceId, new MobileGateway.MobileGatewayListener<GetQrCodeSjtRs>() {
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
//                dismissProgress();
//                if (SingleTicketManager.isQrCodeTicket(mServiceId)) {
//                    //电子单程票
//                    Intent intent = new Intent(STPaySuccessActivity.this, QRCodeTicketPassGateActivity.class);
//                    intent.putExtra(CssConstant.KEY_QR_CODE_TOKEN, response.getQrCodeData());
//                    intent.putExtra(SingleTicketManager.ST_ORDER_ID, mParentOrderNumber);
//                    intent.putExtra(CssConstant.KEY_CATEGORY_CODE, mCategoryCode);
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//                    intent.putExtra(CssConstant.KEY_GATE_STATUS, mGateStatus);
//                    intent.putExtra(CssConstant.KEY_SJTID, response.getSjtId());
//                    startActivity(intent);
//                    finish();
//                } else {
//                    //普通单程票
//                    Intent intent = new Intent(STPaySuccessActivity.this, STGetTicketActivity.class);
//                    intent.putExtra(CssConstant.KEY_QR_CODE_TOKEN, response.getQrCodeData());
//                    intent.putExtra(SingleTicketManager.ST_ORDER_ID, mTempOrderNumber);
//                    intent.putExtra(CssConstant.KEY_CATEGORY_CODE, mCategoryCode);
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//                    startActivity(intent);
//                    finish();
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
//                getQrCodeInfo();
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
//}
