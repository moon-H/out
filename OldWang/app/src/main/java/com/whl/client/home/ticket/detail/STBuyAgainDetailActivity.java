//package com.cssweb.shankephone.home.ticket.detail;
//
//import android.content.Context;
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
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderInfoRs;
//import com.cssweb.shankephone.gateway.model.singleticket.PurchaseOrder;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.home.card.SpServiceManager;
//import com.cssweb.shankephone.home.ticket.BuyTicketByStationActivity;
//import com.cssweb.shankephone.home.ticket.STBookByPriceAgainActivity;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
///**
// * Created by liwx on 2015/11/2.
// */
//public class STBuyAgainDetailActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, OrderView.OnClickListener {
//    private static final String TAG = "STBuyAgainDetailActivity";
//    private STCommonDetailFragment mStCommonDetailFragment;
//    private FragmentManager mFragmentManager;
//
//    private TextView mTvPayAmount;
//    private TextView mTvOrderStatus;
//    private TextView mTvOrderTime;
//    private TextView mSecondTime;
//    private OrderView mRefundTimeView;
//    private OrderView mCancelTimeView;
//    private OrderView mPayTimeView;
//    private TextView mTvRefundTime;
//    private TextView mTvCancelTime;
//    private TextView mTvRefundAmount;
//    private OrderView mRateView;
//    private TextView mTvLableRefndAmount;
//    private Button mBtnBuyAgain;
//
//    private STGateway mStGateway;
//    private SingleTicketManager mSingleTicketManager;
//    private Context mContext;
//
//    private String mOrderId;
//    private String mCityCode;
//    private int mStatus;
//    //    private String serviceId;
//
//    private PurchaseOrder order;
//    private StationCode mStartStation;
//    private StationCode mEndStation;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_st_buy_again_detail);
//        BizApplication.getInstance().addActivity(this);
//        mContext = this;
//        initView();
//
//        //        serviceId=getIntent().getStringExtra(SpServiceManager.SERVICE_ID);
//        mOrderId = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        MLog.d(TAG, "mOrderId====" + mOrderId);
//        MLog.d(TAG, "mCityCode====" + mCityCode);
//        mFragmentManager = getSupportFragmentManager();
//        mStGateway = new STGateway(STBuyAgainDetailActivity.this);
//        mSingleTicketManager = new SingleTicketManager(this);
//
//        if (savedInstanceState == null) {
//            mStCommonDetailFragment = STCommonDetailFragment.getInstance();
//            FragmentTransaction transaction = mFragmentManager.beginTransaction();
//            transaction.add(R.id.lly_content, mStCommonDetailFragment, SingleTicketManager.COMMON_FRAGMENT);
//            transaction.commitAllowingStateLoss();
//        } else {
//            mStCommonDetailFragment = (STCommonDetailFragment) mFragmentManager.findFragmentByTag(SingleTicketManager.COMMON_FRAGMENT);
//        }
//        getOrderInfo();
//
//
//    }
//
//    private void initView() {
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setTitle(getString(R.string.st_order_detail));
//        titleBarView.setOnTitleBarClickListener(this);
//
//        try {
//            mTvPayAmount = (TextView) findViewById(R.id.tv_real_amount);
//            mTvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
//            mTvOrderTime = (TextView) findViewById(R.id.tv_order_time);
//            mSecondTime = (TextView) findViewById(R.id.tv_pay_time);
//            mRefundTimeView = findViewById(R.id.rll_refund_time);
//            mTvRefundTime = (TextView) findViewById(R.id.tv_refund_time);
//            mCancelTimeView = findViewById(R.id.rll_cancel_time);
//            mTvCancelTime = (TextView) findViewById(R.id.tv_cancel_time);
//            mPayTimeView = findViewById(R.id.rll_pay_time);
//            mTvRefundAmount = (TextView) findViewById(R.id.tv_lable_one);
//            mRateView = findViewById(R.id.rll_remain_layout);
//            mTvLableRefndAmount = (TextView) findViewById(R.id.tv_remain_ticket);
//
//            mBtnBuyAgain = (Button) findViewById(R.id.btn_again);
//            mBtnBuyAgain.setOnClickListener(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//        dismissProgressView();
//    }
//
//    private void getOrderInfo() {
//        if (TextUtils.isEmpty(mOrderId) || TextUtils.isEmpty(mCityCode)) {
//            MLog.e(TAG, "orderId is null or cityCode is null");
//            return;
//        }
//        showProgressView();
//        mStGateway.getTicketOrderInfo(mOrderId, mCityCode, new MobileGateway.MobileGatewayListener<GetTicketOrderInfoRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(mContext, result);
//                finish();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(mContext);
//                finish();
//            }
//
//            @Override
//            public void onSuccess(GetTicketOrderInfoRs response) {
//                dismissProgressView();
//                order = response.getPurchaseOrder();
//                mStartStation = new StationCode();
//                mStartStation.setStationCode(order.getPickupStationCode());
//                mStartStation.setStationNameZH(order.getPickupStationNameZH());
//                mStartStation.setLineCode(order.getPickupLineCode());
//                mStartStation.setLineBgColor(order.getSlineBgColor());
//                mStartStation.setLineShortName(order.getSlineShortName());
//                mEndStation = new StationCode();
//                mEndStation.setStationCode(order.getGetoffStationCode());
//                mEndStation.setStationNameZH(order.getGetoffStationNameZH());
//                mEndStation.setLineCode(order.getGetoffLineCode());
//                mEndStation.setLineBgColor(order.getElineBgColor());
//                mEndStation.setLineShortName(order.getElineShortName());
//                showOrderInfo(order);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(mContext);
//                finish();
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
//        MLog.d(TAG, "orderType = " + order.getSingleTicketType());
//        MLog.d(TAG, "endLine info = " + order.getGetoffLineCode() + "  " + order.getGetoffStationNameZH());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mStCommonDetailFragment.updateOrderInfo(order);
//            }
//        }, 150);
//
//        mTvPayAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(order.getTicketTotalAmount())).toString());
//        mTvOrderTime.setText(order.getOrderDate());
//        mStatus = order.getOrderStatus();
//        //        mSingleTicketManager.handleSingleTicketOrderStatus(mTvOrderStatus, mStatus, order);
//        switch (mStatus) {
//            //            case SingleTicketManager.STATUS_CANCELED:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_CANCELED:
//                mPayTimeView.setVisibility(OrderView.GONE);
//                mCancelTimeView.setVisibility(OrderView.VISIBLE);
//                mTvCancelTime.setText(order.getCancelOrderDate());
//                break;
//            //            case SingleTicketManager.STATUS_COMPLETE:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_COMPLETE:
//                mSecondTime.setText(order.getPaymentDate());
//                break;
//            //            case SingleTicketManager.STATUS_CGCOMPLETE:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_CGCOMPLETE:
//                mSecondTime.setText(order.getPaymentDate());
//                break;
//            //            case SingleTicketManager.STATUS_REFUND:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_REFUND:
//                mRateView.setVisibility(OrderView.VISIBLE);
//                mTvRefundAmount.setText(getString(R.string.st_order_refund_amount));
//                mTvLableRefndAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(order.getRefundTicketAmount())).toString());
//                mSecondTime.setText(order.getPaymentDate());
//                mRefundTimeView.setVisibility(OrderView.VISIBLE);
//                mTvRefundTime.setText(order.getRefundTicketDate());
//                break;
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_REFUNDING:
//                //            case SingleTicketManager.STATUS_REFUNDING:
//                mRateView.setVisibility(OrderView.VISIBLE);
//                mTvRefundAmount.setText(getString(R.string.st_order_refund_amount));
//                mTvLableRefndAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(order.getRefundTicketAmount())).toString());
//                mSecondTime.setText(order.getPaymentDate());
//                mRefundTimeView.setVisibility(OrderView.VISIBLE);
//                mTvRefundTime.setText(order.getRefundTicketDate());
//        }
//
//    }
//
//    private void showProgressView() {
//        BizApplication.getInstance().getProgressDialog(STBuyAgainDetailActivity.this, true).show();
//    }
//
//    private void dismissProgressView() {
//        BizApplication.getInstance().dismissProgressDialog();
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
//    @Override
//    public void onClick(OrderView v) {
//        switch (v.getId()) {
//            case R.id.btn_again:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                Intent intent = new Intent();
//                if (order.getSingleTicketType().equals(SingleTicketManager.TICKET_TYPE_BY_STATION_COMMON)) {
//                    //                    if (order.getCategoryCode().equals(OrderManager.CLOUDGATE_CATEGORYCODE)) {
//                    //                        intent.setClass(STBuyAgainDetailActivity.this, STCloudGateActivity.class);
//                    //                        intent.putExtra(SingleTicketManager.STATION, mStartStation);
//                    //                    } else {
//                    intent.setClass(STBuyAgainDetailActivity.this, BuyTicketByStationActivity.class);
//                    intent.putExtra(SingleTicketManager.END_STATION, mEndStation);
//                    intent.putExtra(SpServiceManager.SERVICE_ID, SpServiceManager.SERVICE_ID_TICKET);
//                    intent.putExtra(SingleTicketManager.START_STATION, mStartStation);
//                    //                    }
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, order.getCityCode());
//                    startActivity(intent);
//                } else if (order.getSingleTicketType().equals(SingleTicketManager.TICKET_TYPE_BY_PRICE_COMMON)) {
//                    //                    if (order.getCategoryCode().equals(OrderManager.CLOUDGATE_CATEGORYCODE)) {
//                    //                        intent.setClass(STBuyAgainDetailActivity.this, STCloudGateActivity.class);
//                    //                        intent.putExtra(SingleTicketManager.STATION, mStartStation);
//                    //                    } else {
//                    intent.setClass(STBuyAgainDetailActivity.this, STBookByPriceAgainActivity.class);
//                    intent.putExtra(SpServiceManager.SERVICE_ID, SpServiceManager.SERVICE_ID_TICKET);
//                    intent.putExtra(SingleTicketManager.START_STATION, mStartStation);
//                    //                    }
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, order.getCityCode());
//                    startActivity(intent);
//
//                }
//
//                break;
//        }
//
//    }
//}
