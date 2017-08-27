//package com.cssweb.shankephone.home.ticket;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.OrderView;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.GridView;
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
//import com.cssweb.shankephone.gateway.model.singleticket.GetBuySinlgeTicketMaxNumRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.SingelTicketFixedPricePool;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.wallet.GetPanchanTokenRs;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.home.card.PanChanManager;
//import com.cssweb.shankephone.home.card.SpServiceManager;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.RoundTextView;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
//import java.util.ArrayList;
//
///**
// * Created by liwx on 2015/10/27.
// * 再次购买页面、通过票价
// */
//public class STBookByPriceAgainActivity extends BaseActivity implements OrderView.OnClickListener, PanChanManager.HandlePanchanPayResultListener, TitleBarView.OnTitleBarClickListener {
//    private static final String TAG = "STBookByPriceAgainActivity";
//
//    private TextView mTvStartStation;
//    private TextView mTvTotalAmount;
//    private Button mBtnBuy;
//    private ImageView ticketCountDown;
//    private ImageView ticketCountUp;
//    private TextView tvCount;
//    private TitleBarView mTitleBar;
//    private RoundTextView mLineName;
//
//    private UnitPriceAdapter mPriceAdatpter;
//
//    private ArrayList<SingelTicketFixedPricePool> mPriceList = new ArrayList<>();
//    private ArrayList<Integer> mCountList = new ArrayList<>();
//
//    private int mTicketAmount;
//    private String renminbi;
//    private int mTicketCount;
//
//    private StationCode mStartStation;
//    private String mCityCode;
//    private String mServiceId;
//
//    private PanChanManager mPanchanManager;
//    private STGateway mSTGateway;
//    private WalletGateway mWalletGateway;
//    private SingleTicketManager mSingleTicketManager;
//
//
//    private GenerateTicketorderRs mGenerateCustomerorderRs;
//
//    private int mSelectedPricePosition = 0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        BizApplication.getInstance().addActivity(this);
//        setContentView(R.layout.activity_book_by_price);
//        mPanchanManager = new PanChanManager(STBookByPriceAgainActivity.this, this);
//        mSTGateway = new STGateway(STBookByPriceAgainActivity.this);
//        mWalletGateway = new WalletGateway(STBookByPriceAgainActivity.this);
//        mSingleTicketManager = new SingleTicketManager(STBookByPriceAgainActivity.this);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        mServiceId = getIntent().getStringExtra(SpServiceManager.SERVICE_ID);
//        mStartStation = (StationCode) getIntent().getSerializableExtra(SingleTicketManager.START_STATION);
//        mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
//        mTvStartStation = (TextView) findViewById(R.id.tv_start_station);
//        ticketCountDown = (ImageView) findViewById(R.id.ticket_count_down);
//        ticketCountUp = (ImageView) findViewById(R.id.ticket_count_up);
//        tvCount = (TextView) findViewById(R.id.tv_count);
//        mLineName = (RoundTextView) findViewById(R.id.tv_start_line_name);
//        mTvTotalAmount = (TextView) findViewById(R.id.tv_total_price);
//        GridView priceGridview = (GridView) findViewById(R.id.gv_price);
//        mTitleBar.setTitle(R.string.ticket_price);
//        mTitleBar.setOnTitleBarClickListener(this);
//        mPriceAdatpter = new UnitPriceAdapter();
//        priceGridview.setAdapter(mPriceAdatpter);
//        priceGridview.setOnItemClickListener(OnPriceItemClickedListener);
//        //        mTvStartStation.setOnClickListener(this);
//        if (!TextUtils.isEmpty(mStartStation.getLineBgColor())) {
//            mLineName.setText(mStartStation.getLineShortName());
//            mLineName.setmBgColor(Utils.getColorFromString(mStartStation.getLineBgColor()));
//        }
//        mTvStartStation.setText(mStartStation.getStationNameZH());
//        renminbi = getResources().getString(R.string.st_renminbi);
//        mBtnBuy = (Button) findViewById(R.id.buy_confirm);
//        mBtnBuy.setOnClickListener(this);
//        ticketCountDown.setOnClickListener(this);
//        ticketCountUp.setOnClickListener(this);
//
//        getPriceUnit(mStartStation.getLineCode());
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_STBookByPriceActivity));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_STBookByPriceActivity));
//    }
//
//    @Override
//    public void onPaySuccess(Message msg, String orderNumber) {
//        //支付成功跳转到支付完成页面
//        MLog.d(TAG, "PaySucess=" + msg);
//        //        moveToDetailPage(SingleTicketManager.STATUS_PAY_SUCCESS);
//        moveToDetailPage(CssConstant.ORDER.STATUS_SINGLE_TICKET_PAY_SUCCESS);
//    }
//
//    @Override
//    public void onPayFailed(Message msg) {
//        MLog.d(TAG, "PayFailed=" + msg);
//        mPanchanManager.handleCommonFailedMsg(msg);
//        //moveToDetailPage(SingleTicketManager.STATUS_WITHOUT_PAY);
//    }
//
//    @Override
//    public void onSignatureInvalid() {
//        MLog.d(TAG, "SignatureInvalid");
//        mPanchanManager.toastSignatureInvalid();
//        //moveToDetailPage(SingleTicketManager.STATUS_WITHOUT_PAY);
//    }
//
//    @Override
//    public void onOrderStatusError(Message msg, String orderNumber) {
//        MLog.d(TAG, "OrderStatusError");
//        mPanchanManager.handleCommonFailedMsg(msg);
//        // moveToDetailPage(SingleTicketManager.STATUS_WITHOUT_PAY);
//    }
//
//
//    private void moveToDetailPage(final int status) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mGenerateCustomerorderRs != null) {
//                    MLog.d("STBOOK的值是===BizApplication.getInstance().getCityCode() ", BizApplication.getInstance().getCityCode());
//                    mSingleTicketManager.moveToDetailPage(status, mGenerateCustomerorderRs.getPanchanPayInfo().getOrderNo(), BizApplication.getInstance().getCityCode());
//                }
//            }
//        }, 300);
//    }
//
//    private void generateTicketOrder() {
//        showProgressView();
//        mSTGateway.generateTicketOrder(mServiceId, SingleTicketManager.TICKET_TYPE_BY_PRICE_COMMON, LoginManager.getPhoneNumber(STBookByPriceAgainActivity.this), mCityCode, mStartStation.getStationCode(), "", mPriceList.get(mSelectedPricePosition).getSingleTicketFixedPrice(), mTicketCount, new MobileGateway.MobileGatewayListener<GenerateTicketorderRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STBookByPriceAgainActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GenerateTicketorderRs res) {
//                MLog.d(TAG, "res=" + res);
//                dismissProgressView();
//                mGenerateCustomerorderRs = res;
//                payOrder(res);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                generateTicketOrder();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgressView();
//                //                handleAutoLoginFailed(result);
//            }
//
//            @Override
//            public void onAutoLoginChangeCard() {
//                dismissProgressView();
//                handleChangeCard();
//
//            }
//        });
//    }
//
//    private void payOrder(GenerateTicketorderRs res) {
//        if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//            getPanchanToken();
//        } else {
//            //            mMerchantCert = res.getMerchantCert();
//            mPanchanManager.pay(res.getPanchanPayInfo(), BizApplication.getInstance().getPanchanToken());
//        }
//
//    }
//
//    private void computeTotalCount() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mPriceList != null && mPriceList.size() > 0) {
//                    mTicketAmount = mTicketCount * mPriceList.get(mSelectedPricePosition).getSingleTicketFixedPrice();
//                    mTvTotalAmount.setText(renminbi + " " + Utils.formatDouble2f(mTicketAmount / 100.00));
//                }
//            }
//        }, 100);
//    }
//
//    private void getPriceUnit(final String lineCode) {
//        mSTGateway.getTicketPriceList(BizApplication.getInstance().getCityCode(), lineCode, new MobileGateway.MobileGatewayListener<GetTicketPriceListRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(STBookByPriceAgainActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetTicketPriceListRs response) {
//                mPriceList.clear();
//                mPriceList.addAll(response.getTicketPriceList());
//                mPriceAdatpter.notifyDataSetChanged();
//                getTicketCount();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getPriceUnit(lineCode);
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
//
//    }
//
//    int max;
//
//    private void getTicketCount() {
//        mSTGateway.getBuySinlgeTicketMaxNum(BizApplication.getInstance().getCityCode(), new MobileGateway.MobileGatewayListener<GetBuySinlgeTicketMaxNumRs>() {
//            @Override
//            public void onFailed(Result result) {
//                CommonToast.onFailed(STBookByPriceAgainActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                CommonToast.onHttpFailed(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetBuySinlgeTicketMaxNumRs response) {
//                mCountList.clear();
//                max = response.getBuySinlgeTicketMaxNum();
//                if (max > 0) {
//                    mTicketCount = 1;
//                } else {
//                    mTicketCount = 0;
//                }
//                tvCount.setText(mTicketCount + "");
//                computeTotalCount();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                CommonToast.onNoNetwork(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getTicketCount();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                handleAutoLoginFailed(result);
//            }
//
//            @Override
//            public void onAutoLoginChangeCard() {
//                handleChangeCard();
//            }
//        });
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//    }
//
//    @Override
//    public void onClick(OrderView v) {
//        switch (v.getId()) {
//            //            case R.id.tv_start_station:
//            //                //                startActivityForResult(new Intent(mActivity, STSelectStaionActivity.class), SingleTicketManager.SELECT_START_STATION_REQUEST);
//            //                Intent intent2 = new Intent(STBookByPriceAgainActivity.this, STSelectStaionActivity.class);
//            //                intent2.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_START_STATION_REQUEST);
//            //                startActivityForResult(intent2, SingleTicketManager.SELECT_START_STATION_REQUEST);
//            //                break;
//            case R.id.buy_confirm:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                confirmBuy();
//                break;
//            case R.id.ticket_count_down:
//                if (mTicketCount > 1) {
//                    mTicketCount--;
//                    tvCount.setText(mTicketCount + "");
//                    computeTotalCount();
//                }
//                break;
//            case R.id.ticket_count_up:
//                if (mTicketCount < max) {
//                    mTicketCount++;
//                    tvCount.setText(mTicketCount + "");
//                    computeTotalCount();
//                }
//
//                break;
//        }
//    }
//
//    private void confirmBuy() {
//        if (mStartStation == null) {
//            Toast.makeText(STBookByPriceAgainActivity.this, getResources().getString(R.string.st_please_select_start_station), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (mTicketCount < 1 || mTicketAmount < 1) {
//            Toast.makeText(STBookByPriceAgainActivity.this, getResources().getString(R.string.st_not_pay_ticket), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (BizApplication.getInstance().isLoginClient())
//            if (LoginManager.hasPhoneNumber(getApplicationContext()))
//                generateTicketOrder();
//            else
//                showBindPhoneNumberDialog(STBookByPriceAgainActivity.this);
//        else
//            startActivity(new Intent(STBookByPriceAgainActivity.this, LoginActivity.class));
//
//    }
//
//    private void showProgressView() {
//        BizApplication.getInstance().getProgressDialog(STBookByPriceAgainActivity.this, true).show();
//    }
//
//    private void dismissProgressView() {
//        BizApplication.getInstance().dismissProgressDialog();
//    }
//
//    private void getPanchanToken() {
//        showProgressView();
//        mWalletGateway.getPanchanToken(new MobileGateway.MobileGatewayListener<GetPanchanTokenRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                Toast.makeText(STBookByPriceAgainActivity.this, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(STBookByPriceAgainActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetPanchanTokenRs response) {
//                dismissProgressView();
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    Toast.makeText(STBookByPriceAgainActivity.this, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                } else {
//                    if (mGenerateCustomerorderRs != null) {
//                        payOrder(mGenerateCustomerorderRs);
//                    }
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(STBookByPriceAgainActivity.this);
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
//    private class UnitPriceAdapter extends BaseAdapter {
//        private int selectedPosition;
//
//        //        public void setSeclecedItem(int posion) {
//        //            this.selectedPosition = posion;
//        //            notifyDataSetChanged();
//        //        }
//
//        //        public int getSelecedItem() {
//        //            return selectedPosition;
//        //        }
//
//        @Override
//        public int getCount() {
//            return mPriceList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mPriceList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public OrderView getView(int position, OrderView convertView, ViewGroup parent) {
//            SingelTicketFixedPricePool pricePool = (SingelTicketFixedPricePool) getItem(position);
//            MLog.d(TAG, "pricePool " + pricePool.getSingleTicketFixedPrice());
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = OrderView.inflate(STBookByPriceAgainActivity.this, R.layout.item_price_list, null);
//                holder.price = (TextView) convertView.findViewById(R.id.tv_price);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (mSelectedPricePosition == position)
//                holder.price.setSelected(true);
//            else
//                holder.price.setSelected(false);
//            holder.price.setText(renminbi + Utils.formatDouble2f(pricePool.getSingleTicketFixedPrice() / 100.00));
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView price;
//        }
//
//    }
//
//
//    private AdapterView.OnItemClickListener OnPriceItemClickedListener = new AdapterView.OnItemClickListener() {
//        @Override
//        public void onItemClick(AdapterView<?> parent, OrderView view, int position, long id) {
//            mSelectedPricePosition = position;
//            //            mPriceAdatpter.setSeclecedItem(position);
//
//            computeTotalCount();
//            mPriceAdatpter.notifyDataSetChanged();
//        }
//    };
//
//
//    //    @Override
//    //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    //        super.onActivityResult(requestCode, resultCode, data);
//    //        MLog.d(TAG, "requestCode = " + requestCode + " resultCode = " + requestCode);
//    //        if (requestCode == SingleTicketManager.SELECT_START_STATION_REQUEST && resultCode == SingleTicketManager.SELECT_STATION_RESULT_OK) {
//    //            if (data != null) {
//    //                mStartStation = (StationCode) data.getSerializableExtra(SingleTicketManager.STATION);
//    //                if (mStartStation != null) {
//    //                    mTvStartStation.setTextColor(getResources().getColor(R.color.st_order_price_text));
//    //                    mTvStartStation.setText(mStartStation.getStationNameZH());
//    //                }
//    //            }
//    //        }
//    //    }
//
//    //    public void clearStationInfo() {
//    //        mStartStation = null;
//    //        if(mTvStartStation !=null){
//    //            mTvStartStation.setText(getString(R.string.st_start_station_hint));
//    //            mTvStartStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//    //        }
//    //        mSelectedPricePosition = 0;
//    //        getPriceUnit();
//    //
//    //
//    //    }
//}
