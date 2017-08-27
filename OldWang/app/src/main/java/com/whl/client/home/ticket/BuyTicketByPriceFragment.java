//package com.cssweb.shankephone.home.ticket;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseFragment;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.app.IEvent;
//import com.cssweb.shankephone.db.SingleTicketDBManager;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GenerateTicketorderRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetBuySinlgeTicketMaxNumRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.LineCode;
//import com.cssweb.shankephone.gateway.model.singleticket.SingelTicketFixedPricePool;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.wallet.GetPanchanTokenRs;
//import com.cssweb.shankephone.home.OnFragmentCallBack;
//import com.cssweb.shankephone.pay.PanChanManager;
//import com.cssweb.shankephone.home.ticket.detail.STPaySuccessActivity;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//
//import org.apache.http.Header;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.ArrayList;
//
///**
// * Created by liwx on 2015/10/27.
// */
//public class BuyTicketByPriceFragment extends BaseFragment implements View.OnClickListener {
//    private static final String TAG = "BuyTicketByPriceFragment";
//
//    private View mRootView;
//    private TextView mTvStartStation;
//    private TextView mTvTotalAmount;
//    private TextView mBtnBuy;
//    private ImageView ticketCountDown;
//    private ImageView ticketCountUp;
//    private TextView mTvCount;
//    private TextView mTvTicketDes;
//
//    private BuyTicketByPriceActivity mActivity;
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
//
//    //    private PanChanManager mPanchanManager;
//    private STGateway mSTGateway;
//    private WalletGateway mWalletGateway;
//    private SingleTicketManager mSingleTicketManager;
//    //    private SingleTicketDBManager mSingleTicketDBManager;
//
//    private GenerateTicketorderRs mGenerateCustomerorderRs;
//
//    private int mSelectedPricePosition = 0;
//    private String mResultLineCode = "";
//    private String mServiceId;
//    private String mCityCode;
//    private int mMaxCount;
//    private boolean isBuyAgain = false;
//
//    private OnFragmentCallBack mCallBack;
//
//    public static BuyTicketByPriceFragment getInstance(String serviceId, String cityCode) {
//        BuyTicketByPriceFragment f = new BuyTicketByPriceFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(CssConstant.KEY_SERVICE_ID, serviceId);
//        bundle.putString(CssConstant.KEY_CITY_CODE, cityCode);
//        f.setArguments(bundle);
//        return f;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mActivity = (BuyTicketByPriceActivity) getActivity();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //        MyHandler mHandler = new MyHandler(this);
//        //        mPanchanManager = new PanChanManager(mActivity, this);
//        mSTGateway = new STGateway(mActivity);
//        mWalletGateway = new WalletGateway(mActivity);
//        mSingleTicketManager = new SingleTicketManager(mActivity);
//        if ((savedInstanceState = getArguments()) != null) {
//            if (TextUtils.isEmpty(mServiceId))
//                mServiceId = savedInstanceState.getString(CssConstant.KEY_SERVICE_ID);
//            mCityCode = savedInstanceState.getString(CssConstant.KEY_CITY_CODE);
//            MLog.e(TAG, "mServiceId = " + mServiceId);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        MLog.d(TAG, "%% onCreateView");
//        if (mRootView == null) {
//            MLog.d(TAG, "%% new onCreateView");
//            initView(inflater, container);
//        }
//        EventBus.getDefault().register(this);
//        setStationInfo();
//        getPriceUnit(mResultLineCode);
//        return mRootView;
//    }
//
//    private void initView(LayoutInflater inflater, ViewGroup container) {
//        mRootView = inflater.inflate(R.layout.fragment_book_by_price, container, false);
//
//        ticketCountDown = (ImageView) mRootView.findViewById(R.id.ticket_count_down);
//        ticketCountUp = (ImageView) mRootView.findViewById(R.id.ticket_count_up);
//        mTvCount = (TextView) mRootView.findViewById(R.id.tv_count);
//        mTvTicketDes = (TextView) mRootView.findViewById(R.id.tv_ticket_type_description);
//        GridView priceGridView = (GridView) mRootView.findViewById(R.id.gv_price);
//        mPriceAdatpter = new UnitPriceAdapter();
//        priceGridView.setAdapter(mPriceAdatpter);
//        priceGridView.setOnItemClickListener(OnPriceItemClickedListener);
//        mTvStartStation = (TextView) mRootView.findViewById(R.id.tv_start_station);
//        mTvStartStation.setOnClickListener(this);
//        mTvStartStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//
//        mTvTotalAmount = (TextView) mRootView.findViewById(R.id.tv_total_price);
//        renminbi = getResources().getString(R.string.st_renminbi);
//        mBtnBuy = (TextView) mRootView.findViewById(R.id.buy_confirm);
//        mBtnBuy.setOnClickListener(this);
//        ticketCountDown.setOnClickListener(this);
//        ticketCountUp.setOnClickListener(this);
//        updateServiceType(mServiceId);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        MLog.d(TAG, "onStart");
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        MLog.d(TAG, "onStop");
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageStart(getString(R.string.statistic_STBookByPriceFragment));
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageEnd(getString(R.string.statistic_STBookByPriceFragment));
//    }
//
//    private void moveToDetailPage(final int status, final GenerateTicketorderRs res) {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mGenerateCustomerorderRs != null) {
//                    Intent intent = new Intent(mActivity, STPaySuccessActivity.class);
//                    intent.putExtra(SingleTicketManager.ST_ORDER_ID, res.getPanchanPayInfo().getOrderNo());
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, mCityCode);
//                    intent.putExtra(CssConstant.KEY_SERVICE_ID, mServiceId);
//                    intent.putExtra(CssConstant.KEY_GATE_STATUS, CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY);//电子单程票进闸标识
//                    startActivity(intent);
////                    mActivity.finish();
//                }
//            }
//        }, 300);
//    }
//
//    private void generateTicketOrder(final String account) {
//        showProgressView();
//        String ticketType;
//        if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_TICKET)) {
//            ticketType = CssConstant.SINGLE_TICKET.TICKET_TYPE_COMMON_ONLY_START_STATION;
//        } else {
//            ticketType = CssConstant.SINGLE_TICKET.TICKET_TYPE_QR_CODE_ONLY_START_STATION;
//        }
//        mSTGateway.generateTicketOrder(mServiceId, ticketType, account, mCityCode, mStartStation.getStationCode(), "", mPriceList.get(mSelectedPricePosition).getSingleTicketFixedPrice(), mTicketCount, new MobileGateway.MobileGatewayListener<GenerateTicketorderRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(mActivity, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(mActivity);
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
//                CommonToast.onNoNetwork(mActivity);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                generateTicketOrder(account);
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgressView();
//                //                mActivity.handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    private void payOrder(final GenerateTicketorderRs res) {
//        if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//            getPanchanToken(res);
//        } else {
//            //            mMerchantCert = res.getMerchantCert();
//            PanChanManager.getInstance(mActivity).pay(res.getPanchanPayInfo(), BizApplication.getInstance().getPanchanToken(), new PanChanManager.HandlePanchanPayResultListener() {
//                @Override
//                public void onPaySuccess(String msg, String orderNumber) {
//                    //支付成功跳转到支付完成页面
//                    MLog.d(TAG, "PaySucess=" + msg);
//                    moveToDetailPage(CssConstant.ORDER.STATUS_SINGLE_TICKET_PAY_SUCCESS, res);
//                }
//
//                @Override
//                public void onPayFailed(String msg) {
//                    MLog.d(TAG, "PayFailed=" + msg);
//                    PanChanManager.getInstance(mActivity).handleCommonFailedMsg(msg);
//                    //moveToDetailPage(SingleTicketManager.STATUS_WITHOUT_PAY);
//                }
//            });
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
//                    mTvTotalAmount.setText(renminbi + " " + Utils.parseMoney(mTicketAmount));
//                }
//            }
//        }, 100);
//    }
//
//    private void getPriceUnit(final String lineCode) {
//        mSTGateway.getTicketPriceList(mCityCode, lineCode, new MobileGateway.MobileGatewayListener<GetTicketPriceListRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(BizApplication.getInstance(), result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(BizApplication.getInstance());
//            }
//
//            @Override
//            public void onSuccess(GetTicketPriceListRs response) {
//                mPriceList.clear();
//                mSelectedPricePosition = 0;
//                mPriceList.addAll(response.getTicketPriceList());
//                mPriceAdatpter.notifyDataSetChanged();
//                getTicketCount();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(BizApplication.getInstance());
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
//                mActivity.handleAutoLoginFailed(result);
//            }
//        });
//
//    }
//
//    private void getTicketCount() {
//        mSTGateway.getBuySinlgeTicketMaxNum(mCityCode, new MobileGateway.MobileGatewayListener<GetBuySinlgeTicketMaxNumRs>() {
//            @Override
//            public void onFailed(Result result) {
//                CommonToast.onFailed(BizApplication.getInstance(), result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                CommonToast.onHttpFailed(mActivity);
//            }
//
//            @Override
//            public void onSuccess(GetBuySinlgeTicketMaxNumRs response) {
//                mCountList.clear();
//                mMaxCount = response.getBuySinlgeTicketMaxNum();
//                if (mMaxCount > 0) {
//                    mTicketCount = 1;
//                } else {
//                    mTicketCount = 0;
//                }
//                mTvCount.setText(mTicketCount + "");
//                computeTotalCount();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                CommonToast.onNoNetwork(BizApplication.getInstance());
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getTicketCount();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                mActivity.handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    @Override
//    public void onDestroyView() {
//        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
//        super.onDestroyView();
//        MLog.d(TAG, "%% onDestroyView");
//        EventBus.getDefault().unregister(this);
//    }
//
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_start_station:
//                //                startActivityForResult(new Intent(mActivity, STSelectStaionActivity.class), SingleTicketManager.SELECT_START_STATION_REQUEST);
//                String currentSelectCity = MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE);
//                if (mCityCode.equals(currentSelectCity)) {
//                    //再次购买时城市可能不一样
//                    Intent intent2 = new Intent(mActivity, STSelectStaionActivity.class);
//                    intent2.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_START_STATION_REQUEST);
//                    if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                        intent2.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, true);
//                    } else {
//                        intent2.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, false);
//                    }
//                    startActivityForResult(intent2, SingleTicketManager.SELECT_START_STATION_REQUEST);
//                } else {
//                    CommonToast.toast(mActivity, getString(R.string.buy_again_prompt));
//                }
//                break;
//            case R.id.buy_confirm:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                confirmBuy();
//                break;
//            case R.id.ticket_count_down:
//                if (mTicketCount <= 1) {
//                    return;
//                }
//                mTicketCount--;
//                mTvCount.setText(mTicketCount + "");
//                computeTotalCount();
//                break;
//            case R.id.ticket_count_up:
//                if (mTicketCount >= mMaxCount) {
//                    CommonToast.toast(BizApplication.getInstance(), String.format(getString(R.string.max_buy_ticket), String.valueOf(mMaxCount)));
//                    return;
//                }
//                mTicketCount++;
//                mTvCount.setText(mTicketCount + "");
//                computeTotalCount();
//
//                break;
//        }
//    }
//
//    private void confirmBuy() {
//        if (mStartStation == null) {
//            Toast.makeText(BizApplication.getInstance(), getResources().getString(R.string.st_please_select_start_station), Toast.LENGTH_SHORT).show();
//            return;
//        } else if (mTicketCount < 1 || mTicketAmount < 1) {
//            Toast.makeText(BizApplication.getInstance(), getResources().getString(R.string.st_not_pay_ticket), Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (BizApplication.getInstance().isLoginClient())
//            if (LoginManager.hasPhoneNumber(getActivity()))
//                generateTicketOrder(LoginManager.getPhoneNumber(getActivity()));
//            else
//                mActivity.showBindPhoneNumberDialog(mActivity);
//        else
//            startActivity(new Intent(mActivity, LoginActivity.class));
//    }
//
//    private void showProgressView() {
//        //        BizApplication.getInstance().getProgressDialog(mActivity, true).show();
//        showProgress();
//    }
//
//    private void dismissProgressView() {
//        //        BizApplication.getInstance().dismissProgressDialog();
//        dismissProgress();
//    }
//
//    private void getPanchanToken(final GenerateTicketorderRs res) {
//        showProgressView();
//        mWalletGateway.getPanchanToken(new MobileGateway.MobileGatewayListener<GetPanchanTokenRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                Toast.makeText(BizApplication.getInstance(), getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(BizApplication.getInstance());
//            }
//
//            @Override
//            public void onSuccess(GetPanchanTokenRs response) {
//                dismissProgressView();
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    Toast.makeText(BizApplication.getInstance(), getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                } else {
//                    //                    if (mGenerateCustomerorderRs != null) {
//                    payOrder(res);
//                    //                    }
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(BizApplication.getInstance());
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                getPanchanToken(res);
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgressView();
//                mActivity.handleAutoLoginFailed(result);
//            }
//
//        });
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
//        public View getView(int position, View convertView, ViewGroup parent) {
//            SingelTicketFixedPricePool pricePool = (SingelTicketFixedPricePool) getItem(position);
//            MLog.d(TAG, "pricePool " + pricePool.getSingleTicketFixedPrice());
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(mActivity, R.layout.item_price_list, null);
//                holder.price = (TextView) convertView.findViewById(R.id.tv_price);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (mSelectedPricePosition == position)
//                holder.price.setSelected(true);
//            else
//                holder.price.setSelected(false);
//            holder.price.setText(renminbi + Utils.parseMoney(pricePool.getSingleTicketFixedPrice()));
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
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            mSelectedPricePosition = position;
//            //            mPriceAdatpter.setSeclecedItem(position);
//
//            computeTotalCount();
//            mPriceAdatpter.notifyDataSetChanged();
//        }
//    };
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        MLog.d(TAG, "requestCode = " + requestCode + " resultCode = " + requestCode);
//        if (requestCode == SingleTicketManager.SELECT_START_STATION_REQUEST && resultCode == SingleTicketManager.SELECT_STATION_RESULT_OK) {
//            if (data != null) {
//                mStartStation = (StationCode) data.getSerializableExtra(SingleTicketManager.STATION);
//                setStationInfo();
//
//            }
//        }
//    }
//
//    public void clearStationInfoAndUpdatePrice() {
//        clearStationInfo();
//        getPriceUnit("");
//    }
//
//    public void setFragmentCallback(OnFragmentCallBack callback) {
//        this.mCallBack = callback;
//    }
//
//    public void updatePriceInfo() {
//        //        getPriceUnit("");
//        clearStationInfoAndUpdatePrice();
//    }
//
//    public void clearStationInfo() {
//        mStartStation = null;
//        if (mTvStartStation != null) {
//            mTvStartStation.setText(getString(R.string.st_start_station_hint));
//            mTvStartStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//        }
//        mSelectedPricePosition = 0;
//    }
//
//    /**
//     * EventBus
//     * 选择站点页面发送此事件
//     */
//    @Subscribe
//    public void onEventMainThread(IEvent.SelectStationEvent event) {
//        MLog.d(TAG, "%% onEventMainThread");
//        if (event != null) {
//            mStartStation = event.getStation();
//            int type = event.getType();
//            if (type == SingleTicketManager.SELECT_START_STATION_REQUEST) {
//                setStationInfo();
//            }
//        } else
//            MLog.d(TAG, "onEventMainThread event is null");
//    }
//
//    private void setStationInfo() {
//        if (mStartStation != null) {
//            mTvStartStation.setTextColor(getResources().getColor(R.color.st_order_price_text));
//            mTvStartStation.setText(mStartStation.getStationNameZH());
//            mResultLineCode = mStartStation.getLineCode();
//            //                    mSingleTicketDBManager = new SingleTicketDBManager(getActivity());
//            LineCode lineInfo = SingleTicketDBManager.getLineInfoByLineCode(mResultLineCode);
//            if (lineInfo != null && lineInfo.getToAirportYn().equals("Y")) {
//                getPriceUnit(mStartStation.getLineCode());
//            }
//        }
//    }
//
//    public void setStationInfo(StationCode startStation) {
//        mStartStation = startStation;
//    }
//
//    public void setIsBuyAgain(boolean isBuyAgain) {
//        this.isBuyAgain = isBuyAgain;
//    }
//
//    public void updateServiceType(String serviceId) {
//        mServiceId = serviceId;
//        if (ticketCountDown != null && ticketCountUp != null && mTvCount != null && mTvTicketDes != null && mTvTotalAmount != null) {
//            if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                ticketCountDown.setVisibility(View.GONE);
//                ticketCountUp.setVisibility(View.GONE);
//                mTicketCount = 1;
//                mTvCount.setBackgroundResource(0);
//                mTvTicketDes.setText(getString(R.string.st_qr_ticket_introduction));
//                mTvCount.setText(mTicketCount + "");
//                computeTotalCount();
//            } else {
//                ticketCountDown.setVisibility(View.VISIBLE);
//                ticketCountUp.setVisibility(View.VISIBLE);
//                mTicketCount = 1;
//                mTvCount.setBackgroundResource(R.drawable.bg_middle_nor);
//                mTvTicketDes.setText(getString(R.string.st_common_ticket_introduction));
//                mTvCount.setText(mTicketCount + "");
//                computeTotalCount();
//            }
//        } else
//            MLog.d(TAG, "updateServiceType  view is not initialize");
//
//    }
//
//    public StationCode getStartStation() {
//        return mStartStation;
//    }
//
//}
