//package com.cssweb.shankephone.home.ticket;
//
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.OrderView;
//import android.widget.Button;
//import android.widget.ImageView;
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
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GenerateTicketorderRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceByStationRs;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.wallet.GetPanchanTokenRs;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.home.card.PanChanManager;
//import com.cssweb.shankephone.home.card.SpServiceManager;
//import com.cssweb.shankephone.view.RoundTextView;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
///**
// * Created by LH on 2016/1/14.
// */
//public class STCloudGateActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, OrderView.OnClickListener, PanChanManager.HandlePanchanPayResultListener {
//
//    private static final String TAG = "STCloudGateActivity";
//
//    private TitleBarView mTitleBar;
//    private RoundTextView mStartLineName;
//    private TextView mStartStationName;
//    private TextView mUnitPrice;
//    private TextView mTotalPrice;
//    private ImageView mCGdescribe;
//    private Button mBuyTicket;
//    private String renminbi;
//    private int mTicketPrice;
//    private String mCityCode;
//
//    private StationCode mStartStation;
//    private STGateway mSTGateway;
//    private GenerateTicketorderRs mGenerateCustomerorderRs;
//    private WalletGateway mWalletGateway;
//    private PanChanManager mPanchanManager;
//    private SingleTicketManager mSingleTicketManager;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        BizApplication.getInstance().addActivity(this);
//        setContentView(R.layout.st_cloud_gate);
//        initView();
//        mStartStation = (StationCode) getIntent().getSerializableExtra(SingleTicketManager.STATION);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        mSTGateway = new STGateway(STCloudGateActivity.this);
//        mWalletGateway = new WalletGateway(STCloudGateActivity.this);
//        mPanchanManager = new PanChanManager(STCloudGateActivity.this, this);
//        mSingleTicketManager = new SingleTicketManager(STCloudGateActivity.this);
//        mTitleBar.setTitle(R.string.st_ticket_information);
//        mTitleBar.setOnTitleBarClickListener(this);
//        mBuyTicket.setOnClickListener(this);
//        renminbi = getResources().getString(R.string.st_renminbi);
//        if (!TextUtils.isEmpty(mStartStation.getLineBgColor())) {
//            mStartLineName.setText(mStartStation.getLineShortName());
//            mStartLineName.setmBgColor(Utils.getColorFromString(mStartStation.getLineBgColor()));
//        }
//        mStartStationName.setText(mStartStation.getStationNameZH());
//        getTicketPriceByStation();
//    }
//
//    public void initView() {
//        mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
//        mStartLineName = (RoundTextView) findViewById(R.id.tv_start_line_name);
//        mUnitPrice = (TextView) findViewById(R.id.ti_unit_price);
//        mTotalPrice = (TextView) findViewById(R.id.ti_total_price);
//        mCGdescribe = (ImageView) findViewById(R.id.cgdescribe);
//        mBuyTicket = (Button) findViewById(R.id.btn_buy_ticket);
//        mStartStationName = (TextView) findViewById(R.id.tv_start_station_name);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
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
//    public void onClick(OrderView view) {
//        switch (view.getId()) {
//            case R.id.btn_buy_ticket:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                if (mTicketPrice < 1) {
//                    Toast.makeText(STCloudGateActivity.this, "暂时无法购票", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                generateOrder();
//                break;
//        }
//    }
//
//    private void getTicketPriceByStation() {
//        showProgressView();
//
//        mSTGateway.getTicketPriceByStation(mCityCode, mStartStation.getStationCode(), "", new MobileGateway.MobileGatewayListener<GetTicketPriceByStationRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STCloudGateActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STCloudGateActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetTicketPriceByStationRs response) {
//                dismissProgressView();
//                //mTicketPrice=100;
//                mTicketPrice = response.getTicketPrice();
//                MLog.d(TAG, "mTicketPrice=" + mTicketPrice);
//                mUnitPrice.setText(renminbi + " " + Utils.formatDouble2f(mTicketPrice / 100.00));
//                mTotalPrice.setText(renminbi + " " + Utils.formatDouble2f(mTicketPrice / 100.00));
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STCloudGateActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                getTicketPriceByStation();
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
//    private void generateOrder() {
//        showProgressView();
//        mSTGateway.generateTicketOrder(SpServiceManager.SERVICE_ID_TICKET, SingleTicketManager.TICKET_TYPE_BY_STATION_COMMON, LoginManager.getPhoneNumber(this), mCityCode, mStartStation.getStationCode(), "", mTicketPrice, 1, new MobileGateway.MobileGatewayListener<GenerateTicketorderRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STCloudGateActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STCloudGateActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GenerateTicketorderRs response) {
//                MLog.d(TAG, "PayOrderSuccess");
//                dismissProgressView();
//                mGenerateCustomerorderRs = response;
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    getPanchanToken();
//                } else {
//                    payOrder(response);
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STCloudGateActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                generateOrder();
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
//    private void getPanchanToken() {
//        showProgressView();
//        mWalletGateway.getPanchanToken(new MobileGateway.MobileGatewayListener<GetPanchanTokenRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                Toast.makeText(STCloudGateActivity.this, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                handleHttpFaild(statusCode);
//            }
//
//            @Override
//            public void onSuccess(GetPanchanTokenRs response) {
//                dismissProgressView();
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    Toast.makeText(STCloudGateActivity.this, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                } else {
//                    if (mGenerateCustomerorderRs != null)
//                        payOrder(mGenerateCustomerorderRs);
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STCloudGateActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                getPanchanToken();
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
//    private void payOrder(GenerateTicketorderRs response) {
//
//        if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//            getPanchanToken();
//        } else {
//            if (mPanchanManager != null) {
//                mPanchanManager.pay(response.getPanchanPayInfo(), BizApplication.getInstance().getPanchanToken());
//                MLog.d(TAG, "PayPost");
//            }
//        }
//    }
//
//    private void showProgressView() {
//        BizApplication.getInstance().getProgressDialog(STCloudGateActivity.this, true).show();
//    }
//
//    private void dismissProgressView() {
//        BizApplication.getInstance().dismissProgressDialog();
//    }
//
//    @Override
//    public void onPaySuccess(Message msg, String orderNumber) {
//        //支付成功跳转到支付完成页面
//        MLog.d(TAG, "PaySucess=" + msg);
//        moveToDetailPage(CssConstant.ORDER.STATUS_SINGLE_TICKET_PAY_SUCCESS);
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
//        mPanchanManager.handleCommonFailedMsg(msg);
//    }
//
//    private void moveToDetailPage(final int status) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mGenerateCustomerorderRs != null) {
//                    mSingleTicketManager.moveToDetailPage(status, mGenerateCustomerorderRs.getPanchanPayInfo().getOrderNo(), mCityCode);
//                    finish();
//                }
//            }
//        }, 300);
//    }
//}
