//package com.cssweb.shankephone.home.ticket.detail;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.view.OrderView;
//import android.widget.Button;
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
//import com.cssweb.shankephone.dialog.NoticeDialog;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GetRefundAmountRs;
//import com.cssweb.shankephone.gateway.model.singleticket.PurchaseOrder;
//import com.cssweb.shankephone.gateway.model.singleticket.RequestRefundTicketRs;
//import com.cssweb.shankephone.home.order.OrderManager;
//import com.cssweb.shankephone.home.ticket.STHomeActivity;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
///**
// * Created by liwx on 2015/11/13.
// */
//public class STRefundTicketDetailActivity extends BaseActivity implements OrderView.OnClickListener, TitleBarView.OnTitleBarClickListener {
//    private static final String TAG = "STPreGetTicketDetailActivity";
//    private TextView mTvPayAmount;
//    private TextView mTvLableRate;
//    private TextView mTvLableRefndAmount;
//    private TextView mTvRefundRate;
//    private TextView mTvRefundAmount;
//    private TextView mTvOrderTime;
//    private TextView mTvPayTime;
//    private OrderView mRateView;
//    private OrderView mRefundHintView;
//    private OrderView mCommonLineView;
//
//    private Button mBtnRight;
//    private Button mBtnLeft;
//
//    private STGateway mStGateway;
//    private String mOrderId;
//    private String mCityCode;
//    private int mRefundAmount;
//    private int mOrderAmount;
//    private String WhereGet;
//    private PurchaseOrder order;
//
//    private SingleTicketManager mSingleTicketManager;
//    private FragmentManager mFragmentManager;
//    private STCommonDetailFragment mStCommonDetailFragment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_st_refund_detail);
//        BizApplication.getInstance().addActivity(this);
//
//        mFragmentManager = getSupportFragmentManager();
//        mStGateway = new STGateway(STRefundTicketDetailActivity.this);
//        mSingleTicketManager = new SingleTicketManager(this);
//        mOrderId = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        initView();
//        if (savedInstanceState == null) {
//            mStCommonDetailFragment = STCommonDetailFragment.getInstance();
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            transaction.add(R.id.lly_content, mStCommonDetailFragment, SingleTicketManager.COMMON_FRAGMENT);
//            transaction.commitAllowingStateLoss();
//        } else {
//            mStCommonDetailFragment = (STCommonDetailFragment) mFragmentManager.findFragmentByTag(SingleTicketManager.COMMON_FRAGMENT);
//        }
//
//        getOrderInfo();
//        //        //TODO TEST START
//        //        showOrderInfo((PurchaseOrder) getIntent().getSerializableExtra(SingleTicketManager.ST_ORDER));
//        //        //TODO TEST END
//
//    }
//
//    private void initView() {
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setOnTitleBarClickListener(this);
//        titleBarView.setTitle(getString(R.string.st_refund_money));
//        mTvPayAmount = (TextView) findViewById(R.id.tv_real_amount);
//
//        mTvLableRate = (TextView) findViewById(R.id.tv_lable_one);
//        mTvLableRefndAmount = (TextView) findViewById(R.id.tv_lable_two);
//        mTvRefundRate = (TextView) findViewById(R.id.tv_remain_ticket);
//        mTvRefundAmount = (TextView) findViewById(R.id.tv_order_status);
//        mTvOrderTime = (TextView) findViewById(R.id.tv_order_time);
//        mTvPayTime = (TextView) findViewById(R.id.tv_pay_time);
//        mRateView = findViewById(R.id.rll_remain_layout);
//        mRefundHintView = findViewById(R.id.rlv_refund_hint);
//        mCommonLineView = findViewById(R.id.rlv_common_line);
//
//        mBtnRight = (Button) findViewById(R.id.btn_ok);
//        mBtnLeft = (Button) findViewById(R.id.btn_cancel);
//        mBtnRight.setOnClickListener(this);
//        mBtnLeft.setOnClickListener(this);
//
//        mBtnRight.setText(getString(R.string.ok));
//        mBtnRight.setTextColor(getResources().getColor(R.color.E14343));
//        mBtnLeft.setText(getString(R.string.cancel));
//        mBtnRight.setBackgroundResource(R.drawable.selector_btn_red_line_confirm);
//        mBtnLeft.setBackgroundResource(R.drawable.selector_btn_gray_line_confirm);
//
//        mCommonLineView.setVisibility(OrderView.GONE);
//        mRefundHintView.setVisibility(OrderView.VISIBLE);
//        mRateView.setVisibility(OrderView.VISIBLE);
//
//    }
//
//    public void showProgressView() {
//        BizApplication.getInstance().getProgressDialog(STRefundTicketDetailActivity.this, true).show();
//    }
//
//    public void dismissProgressView() {
//        BizApplication.getInstance().dismissProgressDialog();
//    }
//
//    private void getOrderInfo() {
//        if (TextUtils.isEmpty(mOrderId) || TextUtils.isEmpty(mCityCode)) {
//            MLog.e(TAG, "orderId is null or mCityCode is null");
//            return;
//        }
//        showProgressView();
//        mStGateway.getRefundAmount(mOrderId, mCityCode, new MobileGateway.MobileGatewayListener<GetRefundAmountRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STRefundTicketDetailActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STRefundTicketDetailActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetRefundAmountRs response) {
//                dismissProgressView();
//                order = response.getPurchaseOrder();
//                //                //TODO TEST CODE START
//                //                PurchaseOrder amount = new PurchaseOrder();
//                //                amount.setRefundRate(10);
//                //                amount.setRefundAmount(124);
//                //                //TODO TEST CODE END
//                mRefundAmount = order.getRefundAmount();
//                mOrderAmount = order.getTicketAmount();
//                showOrderInfo(order);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STRefundTicketDetailActivity.this);
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
//            @Override
//            public void onAutoLoginChangeCard() {
//                dismissProgressView();
//                handleChangeCard();
//            }
//        });
//    }
//
//    private void showOrderInfo(final PurchaseOrder order) {
//        //                mTvOrderNumber.setText(order.getOrderNo());
//        //                mTvTicketPrice.setText(new StringBuffer().append(getString(R.string.st_renminbi)).append(order.getSingleTicketNum()).append("/").append(getString(R.string.st_order_detail_zhang)).toString());
//        //                mTvTicketCount.setText(order.getTicketPrice() + getString(R.string.st_order_detail_zhang));
//        //                mTvRefundRate.setText(order.getRefundRate() + "%");
//        //                mTvRefundAmount.setText(getString(R.string.st_renminbi) + order.getRefundAmount());
//        MLog.d(TAG, "orderType = " + order.getSingleTicketType());
//        MLog.d(TAG, "endLine info = " + order.getGetoffLineCode() + "  " + order.getGetoffStationNameZH());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mStCommonDetailFragment.updateOrderInfo(order);
//            }
//        }, 150);
//
//
//        mTvLableRate.setText(getString(R.string.st_order_detail_refund_rate));
//        mTvLableRefndAmount.setText(getString(R.string.st_order_detail_refund_amount));
//
//        mTvPayAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(mOrderAmount).toString()));
//        mTvOrderTime.setText(order.getOrderDate());
//        mTvPayTime.setText(order.getPaymentDate());
//        mTvRefundRate.setText(new StringBuffer(10).append(Utils.formatDouble2f(order.getRefundRate() * 100)).append("%"));
//        mTvRefundAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(mRefundAmount).toString()));
//
//        //        int mStatus = order.getOrderStatus();
//        //        mSingleTicketManager.handleSingleTicketOrderStatus(mTvOrderStatus, mStatus, order);
//        //        switch (mStatus) {
//        //            case SingleTicketManager.STATUS_PREGET_TICKET:
//        //                mPayTimeView.setVisibility(OrderView.VISIBLE);
//        //                mRemainView.setVisibility(OrderView.VISIBLE);
//        //                mTvRemainTicket.setText(new StringBuffer(10).append(order.getCompleteTicketNum()).append("/").append(order.getSingleTicketNum()));
//        //                mBtnLeft.setText(getString(R.string.st_refund_money));
//        //                mBtnRight.setText(getString(R.string.st_getticket));
//        //                mGetTicketToken = order.getTakeTicketToken();
//        //                break;
//        //            case SingleTicketManager.STATUS_WITHOUT_PAY:
//        //                mRemainView.setVisibility(OrderView.GONE);
//        //                mPayTimeView.setVisibility(OrderView.GONE);
//        //                mBtnLeft.setText(getString(R.string.st_order_detail_cancel_order));
//        //                mBtnRight.setText(getString(R.string.st_order_detail_pay_order));
//        //                break;
//        //        }
//
//    }
//
//    @Override
//    public void onClick(OrderView v) {
//        switch (v.getId()) {
//            case R.id.btn_ok:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                refundTicket(mOrderId, mOrderAmount, mRefundAmount, mCityCode);
//                break;
//            case R.id.btn_cancel:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                finish();
//                break;
//        }
//    }
//
//    @Override
//    public void onBackClicked(OrderView view) {
//        finish();
//    }
//
//    @Override
//    public void onMenuClicked(OrderView view) {
//
//    }
//
//    private void refundTicket(final String orderId, int orderAmount, int refundAmount, final String cityCode) {
//        showProgressView();
//        mStGateway.requestRefundTicket(orderId, orderAmount, refundAmount, cityCode, new MobileGateway.MobileGatewayListener<RequestRefundTicketRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STRefundTicketDetailActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STRefundTicketDetailActivity.this);
//            }
//
//            @Override
//            public void onSuccess(RequestRefundTicketRs response) {
//                dismissProgressView();
//                refundSuccess();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STRefundTicketDetailActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                refundTicket(orderId, mOrderAmount, mRefundAmount, cityCode);
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgressView();
//                handleAutoLoginFailed(result);
//            }
//
//            @Override
//            public void onAutoLoginChangeCard() {
//                dismissProgressView();
//                handleChangeCard();
//            }
//        });
//    }
//
//    private void refundSuccess() {
//        NoticeDialog dialog = new NoticeDialog(STRefundTicketDetailActivity.this, NoticeDialog.ONE_BUTTON);
//        dialog.setButtonName(getString(R.string.ok), "");
//        dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//            @Override
//            public void onLeftButtonClicked(OrderView view) {
//                backHomePage();
//            }
//
//            @Override
//            public void onRightButtonClicked(OrderView view) {
//            }
//        });
//        dialog.show(getString(R.string.st_order_refund_success));
//    }
//
//    /**
//     * 返回home页面
//     */
//    private void backHomePage() {
//        Intent intent = new Intent(getApplicationContext(), STHomeActivity.class);
//        //        intent.setAction(SingleTicketManager.ACTION_LAUNCH_ORDERLIST_BY_PAYTICKETCOMPLETE);
//        intent.setAction(OrderManager.SINGLETICKET);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        intent.putExtra(CssConstant.KEY_CATEGORYCODE, order.getCategoryCode());
//        intent.putExtra(OrderManager.TICKETTITLE, order.getCategoryName());
//        intent.putExtra(CssConstant.KEY_CITY_CODE, order.getCityCode());
//        intent.putExtra(CssConstant.KEY_CITY_NAME, order.getCityName());
//        startActivity(intent);
//        finish();
//    }
//
//}
