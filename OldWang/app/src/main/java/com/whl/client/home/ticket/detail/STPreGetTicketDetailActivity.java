//package com.cssweb.shankephone.home.ticket.detail;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.view.OrderView;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
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
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.CancelOrderRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetRefundAmountRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketOrderInfoRs;
//import com.cssweb.shankephone.gateway.model.singleticket.PurchaseOrder;
//import com.cssweb.shankephone.gateway.model.spservice.PanchanPayInfo;
//import com.cssweb.shankephone.gateway.model.wallet.GetPanchanTokenRs;
//import com.cssweb.shankephone.home.card.PanChanManager;
//import com.cssweb.shankephone.home.card.SpServiceManager;
//import com.cssweb.shankephone.home.ticket.SingleTicketManager;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
///**
// * Created by liwx on 2015/11/2.
// */
//public class STPreGetTicketDetailActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, OrderView.OnClickListener, PanChanManager.HandlePanchanPayResultListener {
//    private static final String TAG = "STPreGetTicketDetailActivity";
//    public static STPreGetTicketDetailActivity mDetailActivity;
//
//    //    private TextView mTvOrderNumber;
//    //    private TextView mTvTicketCount;
//    //    private TextView mTvTicketPrice;
//    //    private TextView mTvStartStation;
//    //    private TextView mTvEndStation;
//    //    private TextView mTvStartLine;
//    //    private TextView mTvEndLine;
//    private TextView mTvPayAmount;
//    private TextView mTvRemainTicket;
//    private TextView mTvOrderStatus;
//    private TextView mTvOrderTime;
//    private TextView mTvPayTime;
//    private OrderView mRemainView;
//    private OrderView mPayTimeView;
//
//    private Button mBtnRight;
//    private Button mBtnLeft;
//
//    private STGateway mStGateway;
//    private WalletGateway mWalletGateway;
//
//    private SingleTicketManager mSingleTicketManager;
//    private FragmentManager mFragmentManager;
//    private STCommonDetailFragment mStCommonDetailFragment;
//    private PanChanManager mPanchanManager;
//
//    private PanchanPayInfo mPanchanPayInfo;
//
//    private int mStatus;
//    private String mGetTicketToken;
//    private String mOrderId;
//    private String mCityCode;
//    private String serviceId;
//    private String mOrderNO;
//    private String mCategoryCode;
//
//    private PurchaseOrder order;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_st_refund_detail);
//        BizApplication.getInstance().addActivity(this);
//        mDetailActivity = this;
//        mFragmentManager = getSupportFragmentManager();
//        mStGateway = new STGateway(STPreGetTicketDetailActivity.this);
//        mSingleTicketManager = new SingleTicketManager(this);
//        mPanchanManager = new PanChanManager(STPreGetTicketDetailActivity.this, this);
//        mWalletGateway = new WalletGateway(STPreGetTicketDetailActivity.this);
//
//        mOrderId = getIntent().getStringExtra(SingleTicketManager.ST_ORDER_ID);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        serviceId = getIntent().getStringExtra(SpServiceManager.SERVICE_ID);
//        initView();
//
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
//        //showOrderInfo((PurchaseOrder) getIntent().getSerializableExtra(SingleTicketManager.ST_ORDER));
//        //        //TODO TEST END
//
//    }
//
//    private void initView() {
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setOnTitleBarClickListener(this);
//        titleBarView.setTitle(getString(R.string.st_order_detail));
//        mTvPayAmount = (TextView) findViewById(R.id.tv_real_amount);
//        mTvRemainTicket = (TextView) findViewById(R.id.tv_remain_ticket);
//        mTvOrderStatus = (TextView) findViewById(R.id.tv_order_status);
//        mTvOrderTime = (TextView) findViewById(R.id.tv_order_time);
//        mTvPayTime = (TextView) findViewById(R.id.tv_pay_time);
//        mRemainView = findViewById(R.id.rll_remain_layout);
//        mPayTimeView = findViewById(R.id.rll_pay_time);
//
//        mBtnRight = (Button) findViewById(R.id.btn_ok);
//        mBtnLeft = (Button) findViewById(R.id.btn_cancel);
//        mBtnRight.setOnClickListener(this);
//        mBtnLeft.setOnClickListener(this);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        mFragmentManager.beginTransaction().remove(mStCommonDetailFragment).commitAllowingStateLoss();
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//    }
//
//    public void showProgressView() {
//        BizApplication.getInstance().getProgressDialog(STPreGetTicketDetailActivity.this, true).show();
//    }
//
//    public void dismissProgressView() {
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
//            case R.id.btn_ok:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                onRightBtnClicked();
//                break;
//            case R.id.btn_cancel:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                onLeftBtnClicked();
//                break;
//        }
//    }
//
//    private void getOrderInfo() {
//        if (TextUtils.isEmpty(mOrderId) || TextUtils.isEmpty(mCityCode)) {
//            MLog.e(TAG, "orderId is null or mCityCode is null");
//            return;
//        }
//        showProgressView();
//        mStGateway.getTicketOrderInfo(mOrderId, mCityCode, new MobileGateway.MobileGatewayListener<GetTicketOrderInfoRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STPreGetTicketDetailActivity.this, result);
//                finish();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STPreGetTicketDetailActivity.this);
//                finish();
//            }
//
//            @Override
//            public void onSuccess(GetTicketOrderInfoRs response) {
//                dismissProgressView();
//                order = response.getPurchaseOrder();
//                String cityCode = order.getCityCode();
//                mPanchanPayInfo = response.getPanchanPayInfo();
//                showOrderInfo(order);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STPreGetTicketDetailActivity.this);
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
//        mTvPayAmount.setText(new StringBuffer(10).append(getString(R.string.st_renminbi)).append(Utils.parseMoney(order.getTicketTotalAmount())).toString());
//        mTvOrderTime.setText(order.getOrderDate());
//        mTvPayTime.setText(order.getPaymentDate());
//        mStatus = order.getOrderStatus();
//
//        //        mSingleTicketManager.handleSingleTicketOrderStatus(mTvOrderStatus, mStatus, order);
//        mBtnLeft.setBackgroundResource(R.drawable.selector_btn_gray_line_confirm);
//        switch (mStatus) {
//            //            case SingleTicketManager.STATUS_PREGET_TICKET:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_PREGET_TICKET:
//                mBtnRight.setBackgroundResource(R.drawable.selector_btn_orange_line_confirm);
//                mBtnRight.setTextColor(getResources().getColor(R.color.FF9605));
//
//                mPayTimeView.setVisibility(OrderView.VISIBLE);
//                mRemainView.setVisibility(OrderView.VISIBLE);
//                int surplus = order.getSingleTicketNum() - order.getCompleteTicketNum();
//                mTvRemainTicket.setText(new StringBuffer(10).append(surplus).append("/").append(order.getSingleTicketNum()));
//                mBtnLeft.setText(getString(R.string.st_refund_money));
//                mBtnRight.setText(getString(R.string.st_getticket));
//                mGetTicketToken = order.getTakeTicketToken();
//                mOrderNO = order.getOrderNo();
//                mCategoryCode = order.getCategoryCode();
//                MLog.d(TAG, "orderNO==" + mOrderNO);
//
//                break;
//            //            case SingleTicketManager.STATUS_WITHOUT_PAY:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_WITHOUT_PAY:
//                mRemainView.setVisibility(OrderView.GONE);
//                mPayTimeView.setVisibility(OrderView.GONE);
//                mBtnRight.setBackgroundResource(R.drawable.selector_btn_red_line_confirm);
//                mBtnRight.setTextColor(getResources().getColor(R.color.E14343));
//                mBtnLeft.setText(getString(R.string.st_order_detail_cancel_order));
//                mBtnRight.setText(getString(R.string.st_order_detail_pay_order));
//                break;
//        }
//
//    }
//
//    private void onLeftBtnClicked() {
//        switch (mStatus) {
//            //            case SingleTicketManager.STATUS_PREGET_TICKET://待取票
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_PREGET_TICKET:
//
//                getRefundRate();
//                break;
//            //            case SingleTicketManager.STATUS_WITHOUT_PAY://未支付
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_WITHOUT_PAY:
//                cancelOrder();
//                break;
//        }
//    }
//
//    private void onRightBtnClicked() {
//        switch (mStatus) {
//            //            case SingleTicketManager.STATUS_PREGET_TICKET:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_PREGET_TICKET:
//                Intent intent = new Intent(STPreGetTicketDetailActivity.this, STGetTicketActivity.class);
//                intent.putExtra(SingleTicketManager.ST_GET_TICKET_TOKEN, mGetTicketToken);
//                intent.putExtra(SingleTicketManager.ST_ORDER_ID, mOrderNO);
//                intent.putExtra(CssConstant.KEY_CATEGORYCODE, mCategoryCode);
//                intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//                startActivity(intent);
//                break;
//            //            case SingleTicketManager.STATUS_WITHOUT_PAY:
//            case CssConstant.ORDER.STATUS_SINGLE_TICKET_WITHOUT_PAY:
//                startPay();
//                break;
//        }
//    }
//
//    private void getRefundRate() { //点击退款
//        showProgressView();
//        mStGateway.getRefundAmount(mOrderId, mCityCode, new MobileGateway.MobileGatewayListener<GetRefundAmountRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STPreGetTicketDetailActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STPreGetTicketDetailActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetRefundAmountRs response) {
//                dismissProgressView();
//                //                //TODO TEST CODE START
//                //                GetRefundAmountRs res = new GetRefundAmountRs();
//                //                PurchaseOrder amount = new PurchaseOrder();
//                //                amount.setOrderNo("01201511131112100102");
//                //                amount.setSingleTicketType("1");
//                //                amount.setRefundRate(10);
//                //                amount.setRefundAmount(124);
//                //                res.setPurchaseOrder(amount);
//                //                //TODO TEST CODE END
//                showRefundRateDialog(response);
//
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STPreGetTicketDetailActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                getRefundRate();
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
//    private void getPanchanToken() {
//        showProgressView();
//        mWalletGateway.getPanchanToken(new MobileGateway.MobileGatewayListener<GetPanchanTokenRs>() {
//                                           @Override
//                                           public void onFailed(Result result) {
//                                               dismissProgressView();
//                                               Toast.makeText(STPreGetTicketDetailActivity.this, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                                           }
//
//                                           @Override
//                                           public void onHttpFailed(int statusCode, Header[] headers) {
//                                               dismissProgressView();
//                                               handleHttpFaild(statusCode);
//                                           }
//
//                                           @Override
//                                           public void onSuccess(GetPanchanTokenRs response) {
//                                               dismissProgressView();
//                                               if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                                                   Toast.makeText(STPreGetTicketDetailActivity.this, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                                               } else {
//                                                   payOrder();
//                                               }
//                                           }
//
//                                           @Override
//                                           public void onNoNetwork() {
//                                               dismissProgressView();
//                                               CommonToast.onNoNetwork(STPreGetTicketDetailActivity.this);
//                                           }
//
//                                           @Override
//                                           public void onAutoLoginSuccess() {
//                                               dismissProgressView();
//                                               getPanchanToken();
//                                           }
//
//                                           @Override
//                                           public void onAutoLoginFailed(Result result) {
//                                               dismissProgressView();
//                                               handleAutoLoginFailed(result);
//                                           }
//
//                                           @Override
//                                           public void onAutoLoginChangeCard() {
//                                               dismissProgressView();
//                                               handleChangeCard();
//                                           }
//                                       }
//
//        );
//    }
//
//    private void startPay() {
//        if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//            getPanchanToken();
//        } else {
//            payOrder();
//        }
//    }
//
//    private void payOrder() {
//
//        if (mPanchanPayInfo != null) {
//            mPanchanManager.pay(mPanchanPayInfo, BizApplication.getInstance().getPanchanToken());
//        }
//    }
//
//    private void showRefundRateDialog(GetRefundAmountRs response) {
//        int color = getResources().getColor(R.color.FD0001);
//        final PurchaseOrder order = response.getPurchaseOrder();
//        String rate = String.valueOf(Utils.formatDouble2f(order.getRefundRate() * 100));
//        String ruleContent = String.format(getString(R.string.st_refund_ticket_rule), rate);
//        SpannableString spannableString = mSingleTicketManager.getSpananbleString(color, ruleContent, rate);
//
//        NoticeDialog dialog = new NoticeDialog(STPreGetTicketDetailActivity.this, NoticeDialog.TWO_BUTTON);
//        dialog.setButtonName(getString(R.string.str_continue), getString(R.string.cancel));
//        dialog.setContent(spannableString);
//        dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//            @Override
//            public void onLeftButtonClicked(OrderView view) {
//                Intent intent = new Intent(STPreGetTicketDetailActivity.this, STRefundTicketDetailActivity.class);
//                intent.putExtra(SingleTicketManager.ST_ORDER_ID, order.getOrderNo());
//                intent.putExtra(CssConstant.KEY_CITY_CODE, order.getCityCode());
//                //                finish();
//                startActivity(intent);
//            }
//
//            @Override
//            public void onRightButtonClicked(OrderView view) {
//
//            }
//        });
//        dialog.show();
//    }
//
//    private void cancelOrder() {
//        showProgressView();
//        mStGateway.cancelOrder(order.getOrderNo(), order.getCityCode(), new MobileGateway.MobileGatewayListener<CancelOrderRs>() {
//
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STPreGetTicketDetailActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STPreGetTicketDetailActivity.this);
//                finish();
//            }
//
//            @Override
//            public void onSuccess(CancelOrderRs response) {
//                dismissProgressView();
//                Result result = response.getResult();
//                showCancelInfo(result);
//
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STPreGetTicketDetailActivity.this);
//                finish();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                cancelOrder();
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
//
//    }
//
//    private void showCancelInfo(Result result) {
//        int code = result.getCode();
//        if (code == 0) {
//            Toast.makeText(STPreGetTicketDetailActivity.this, getString(R.string.st_order_cancel_success), Toast.LENGTH_SHORT).show();
//            finish();
//        } else {
//            Toast.makeText(STPreGetTicketDetailActivity.this, getString(R.string.st_order_cancel_failed), Toast.LENGTH_SHORT).show();
//        }
//
//    }
//
//    @Override
//    public void onPaySuccess(Message msg, String orderNumber) {
//        //支付成功跳转到支付完成页面
//        MLog.d(TAG, "PaySucess=" + msg);
//        //        moveToDetailPage(SingleTicketManager.STATUS_PAY_SUCCESS);
//        moveToDetailPage(CssConstant.ORDER.STATUS_SINGLE_TICKET_PAY_SUCCESS);
//        finish();
//    }
//
//    @Override
//    public void onPayFailed(Message msg) {
//        MLog.d(TAG, "PayFailed=" + msg);
//        mPanchanManager.handleCommonFailedMsg(msg);
//    }
//
//    @Override
//    public void onSignatureInvalid() {
//        MLog.d(TAG, "SignatureInvalid");
//        mPanchanManager.toastSignatureInvalid();
//    }
//
//    @Override
//    public void onOrderStatusError(Message msg, String orderNumber) {
//        MLog.d(TAG, "OrderStatusError");
//        mPanchanManager.handleCommonFailedMsg(msg);
//    }
//
//    private void moveToDetailPage(final int status) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mPanchanPayInfo != null) {
//                    mSingleTicketManager.moveToDetailPage(status, mPanchanPayInfo.getOrderNo(), mCityCode);
//                }
//            }
//        }, 300);
//    }
//}
