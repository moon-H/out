//package com.cssweb.shankephone.home.ticket;
//
//import android.animation.Animator;
//import android.animation.ObjectAnimator;
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.amap.api.services.route.BusPath;
//import com.amap.api.services.route.BusRouteResult;
//import com.amap.api.services.route.BusStep;
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.DeviceInfo;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseFragment;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.app.IEvent;
//import com.cssweb.shankephone.bdlocationservice.ThirdMapService;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GenerateTicketorderRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetBuySinlgeTicketMaxNumRs;
//import com.cssweb.shankephone.gateway.model.singleticket.GetTicketPriceByStationRs;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.wallet.GetPanchanTokenRs;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.pay.PanChanManager;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.view.RoundLineNameView;
//
//import org.apache.http.Header;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import static com.cssweb.shankephone.R.id.tv_end_station_name;
//
//
///**
// * Created by liwx on 2017/4/5.
// * 通过选站购票
// */
//
//public class BuyTicketByStationFragment extends BaseFragment implements View.OnClickListener {
//    private static final String TAG = "BuyTicketByStationFragment";
//    private Activity mContext;
//    private String mServiceId;
//    private View mRootView;
//    private RoundLineNameView mTvStartLineName;
//    private RoundLineNameView mTvEndLineName;
//    private TextView mTvStartStation;
//    private TextView mTvEndStation;
//    private TextView mTvTicketPrice;
//    private TextView mTvTicketTotalPrice;
//    private TextView mTvTicketCount;
//    private TextView mTvTicketIntroduct;
//    private ImageView mImgSwitchStation;
//    private LinearLayout mLlyLeft;
//    private LinearLayout mLlyRight;
//    private RelativeLayout mRlyStationParent;
//
//    private ImageView mImgCountUp;
//    private ImageView mImgCountDown;
//
//    private StationCode mStartStation;
//    private StationCode mEndStation;
//    private StationCode mTempStation;
//
//    private STGateway mSTGateway;
//    private SingleTicketManager mSingleTicketManager;
//    private WalletGateway mWalletGateway;
//    //    private PanChanManager mPanchanManager;
//    private CssBusRouteAdapter mCssBusRouteAdapter;
//    private GenerateTicketorderRs mGenerateCustomerOrderRs;
//
//    private List<MultiItemEntity> mBusRouteLv0ItemList = new ArrayList<>();
//
//    private int mTicketAmount;//总价
//    private int mTicketCount = 1;//数量
//    private int mTicketPrice;//单价
//    private int mMaxCount = -1;
//
//    private boolean isSwitched = false;
//    private String mCityCode;
//    private String renminbi;
//    private boolean isBuyAgain = false;
//
//    private ExecutorService mExcutor = Executors.newCachedThreadPool();
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mContext = getActivity();
//    }
//
//
//    public static BuyTicketByStationFragment getInstance(String serviceId, String cityCode) {
//        BuyTicketByStationFragment f = new BuyTicketByStationFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(CssConstant.KEY_SERVICE_ID, serviceId);
//        bundle.putString(CssConstant.KEY_CITY_CODE, cityCode);
//        f.setArguments(bundle);
//        return f;
//    }
//
//
//    @Override
//    public void onCreate(Bundle bundle) {
//        super.onCreate(bundle);
//        MLog.d(TAG, "onCreate");
//        if ((bundle = getArguments()) != null) {
//            if (TextUtils.isEmpty(mServiceId))
//                mServiceId = bundle.getString(CssConstant.KEY_SERVICE_ID);
//            mCityCode = bundle.getString(CssConstant.KEY_CITY_CODE);
//            MLog.e(TAG, "mServiceId = " + mServiceId + " mCityCode = " + mCityCode);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        MLog.d(TAG, "onCreateView");
//        if (mRootView == null) {
//            MLog.d(TAG, "newCreateView");
//            mRootView = inflater.inflate(R.layout.fragment_buy_station_detail, container, false);
//            initView(mRootView);
//            initData();
//        }
//        getAvailableMaxCount();
//        getTicketPriceByStation();
//        //        getBusPath();
//        return mRootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        //        UMengShare.countPageEnd(getString(R.string.statistic_BuyStationDetailFragment));
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        MLog.d(TAG, "onDestroy");
//        mRootView = null;
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onDestroyView() {
//        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
//        super.onDestroyView();
//        MLog.d(TAG, "onDestroyView");
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (Utils.isFastDoubleClick()) {
//            return;
//        }
//        switch (view.getId()) {
//            case R.id.btn_buy_ticket:
//                if (mTicketAmount < 1 || mTicketCount < 1) {
//                    Toast.makeText(mContext, R.string.cant_buy_ticket, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (BizApplication.getInstance().isLoginClient())
//                    if (LoginManager.hasPhoneNumber(mContext))
//                        generateOrder();
//                    else
//                        showBindPhoneNumberDialog((BuyTicketByStationActivity) mContext);
//                else
//                    startActivity(new Intent(mContext, LoginActivity.class));
//
//                break;
//            case R.id.img_switch_station:
//                if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                    switchStation();
//                } else {
//                    if (!TextUtils.isEmpty(mEndStation.getUseYn()))
//                        if (mEndStation.getUseYn().equalsIgnoreCase("N")) {
//                            CommonToast.toast(mContext, String.format(getString(R.string.no_get_ticket_device), mEndStation.getStationNameZH()));
//                        } else {
//                            switchStation();
//                        }
//                }
//                break;
//            case R.id.ticket_count_down:
//                if (mTicketCount <= 1) {
//                    return;
//                }
//                mTicketCount--;
//                mTvTicketCount.setText(mTicketCount + "");
//                calculatePrice();
//                break;
//            case R.id.ticket_count_up:
//                if (mTicketCount >= mMaxCount) {
//                    CommonToast.toast(mContext, String.format(getString(R.string.max_buy_ticket), String.valueOf(mMaxCount)));
//                    return;
//                }
//                mTicketCount++;
//                mTvTicketCount.setText(mTicketCount + "");
//                calculatePrice();
//                break;
//            case R.id.lly_left:
//                EventBus.getDefault().unregister(this);
//                EventBus.getDefault().register(this);
//                //                CommonToast.toast(mContext, "left");
//                Intent intent2 = new Intent(mContext, STSelectStaionActivity.class);
//                if (isSwitched) {
//                    intent2.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_END_STATION_REQUEST);
//                } else {
//                    if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                        intent2.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, true);
//                    } else
//                        intent2.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, false);
//                    intent2.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_START_STATION_REQUEST);
//                }
//                intent2.putExtra(CssConstant.KEY_CITY_CODE, MPreference.getString(mContext, MPreference.CHOICE_CITY_CODE));
//                startActivity(intent2);
//                break;
//            case R.id.lly_right:
//                EventBus.getDefault().unregister(this);
//                EventBus.getDefault().register(this);
//                //                CommonToast.toast(mContext, "right");
//                Intent endIntent = new Intent(mContext, STSelectStaionActivity.class);
//                if (isSwitched) {
//                    if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                        endIntent.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, true);
//                    } else {
//                        endIntent.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, false);
//                    }
//                    endIntent.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_START_STATION_REQUEST);
//                } else {
//                    endIntent.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_END_STATION_REQUEST);
//                }
//                endIntent.putExtra(CssConstant.KEY_CITY_CODE, MPreference.getString(mContext, MPreference.CHOICE_CITY_CODE));
//                startActivity(endIntent);
//                break;
//            default:
//                break;
//
//        }
//    }
//
//    public void initView(View rootView) {
//        mLlyLeft = (LinearLayout) rootView.findViewById(R.id.lly_left);
//        mLlyRight = (LinearLayout) rootView.findViewById(R.id.lly_right);
//        mTvStartLineName = (RoundLineNameView) rootView.findViewById(R.id.tv_start_line_name);
//        mTvEndLineName = (RoundLineNameView) rootView.findViewById(R.id.tv_end_line_name);
//        mTvStartStation = (TextView) rootView.findViewById(R.id.tv_start_station_name);
//        mTvEndStation = (TextView) rootView.findViewById(tv_end_station_name);
//        mTvTicketPrice = (TextView) rootView.findViewById(R.id.tv_ticket_price);
//        mTvTicketCount = (TextView) rootView.findViewById(R.id.tv_ticket_count);
//        mTvTicketTotalPrice = (TextView) rootView.findViewById(R.id.tv_ticket_total_amount);
//        mTvTicketIntroduct = (TextView) rootView.findViewById(R.id.tv_ticket_introduction);
//        mImgCountUp = (ImageView) rootView.findViewById(R.id.ticket_count_up);
//        mImgCountDown = (ImageView) rootView.findViewById(R.id.ticket_count_down);
//        mRlyStationParent = (RelativeLayout) rootView.findViewById(R.id.lly_parent);
//        mImgCountDown.setOnClickListener(this);
//        mImgCountUp.setOnClickListener(this);
//        TextView btn_buy_ticket = (TextView) rootView.findViewById(R.id.btn_buy_ticket);
//        btn_buy_ticket.setOnClickListener(this);
//
//        mLlyLeft.setOnClickListener(this);
//        mLlyRight.setOnClickListener(this);
//
//        mImgSwitchStation = (ImageView) rootView.findViewById(R.id.img_switch_station);
//        mImgSwitchStation.setOnClickListener(this);
//
//
//        RecyclerView recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        mCssBusRouteAdapter = new CssBusRouteAdapter(mContext, mBusRouteLv0ItemList);
//        recyclerView.setAdapter(mCssBusRouteAdapter);
//        updateServiceType(mServiceId);
//    }
//
//    private void initData() {
//        renminbi = getResources().getString(R.string.st_renminbi);
//        mSTGateway = new STGateway(mContext);
//        mSingleTicketManager = new SingleTicketManager(mContext);
//        mWalletGateway = new WalletGateway(mContext);
//
//        mTvTicketCount.setText(String.valueOf(mTicketCount));
//        setStartStationInfo(mStartStation);
//        setEndStationInfo(mEndStation);
//    }
//
//    /**
//     * 显示起点信息
//     */
//    private void setStartStationInfo(StationCode startStation) {
//        if (mStartStation != null) {
//            mSingleTicketManager.setLineName(mTvStartLineName, startStation.getLineShortName(), startStation.getLineBgColor());
//            mTvStartStation.setVisibility(View.VISIBLE);
//            mTvStartStation.setText(startStation.getStationNameZH());
//            if (startStation.getStationNameZH().length() <= 5) {
//                mTvStartStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            } else if (startStation.getStationNameZH().length() == 6) {
//                mTvStartStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            } else if (startStation.getStationNameZH().length() == 7) {
//                mTvStartStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            }
//        } else {
//            mTvStartStation.setText("");
//            mTvStartLineName.setText("");
//        }
//    }
//
//    /**
//     * 显示终点信息
//     */
//    private void setEndStationInfo(StationCode endStation) {
//        if (mEndStation != null) {
//            mSingleTicketManager.setLineName(mTvEndLineName, endStation.getLineShortName(), endStation.getLineBgColor());
//            mTvEndStation.setText(endStation.getStationNameZH());
//            mTvEndStation.setVisibility(View.VISIBLE);
//            MLog.d(TAG, "endStation.getStationNameZH().length():" + endStation.getStationNameZH().length());
//            if (endStation.getStationNameZH().length() <= 5) {
//                mTvEndStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            } else if (endStation.getStationNameZH().length() == 6) {
//                mTvEndStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
//            } else if (endStation.getStationNameZH().length() == 7) {
//                mTvEndStation.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//            }
//        } else {
//            mTvEndStation.setText("");
//            mTvEndLineName.setText("");
//        }
//    }
//
//    public void setStationInfo(StationCode startStation, StationCode endStation) {
//        mStartStation = startStation;
//        mEndStation = endStation;
//    }
//
//    public void setStartStation(StationCode startStation) {
//        mStartStation = startStation;
//        getTicketPriceByStation();
//        setStartStationInfo(mStartStation);
//
//    }
//
//    public void setEndStation(StationCode endStation) {
//        mEndStation = endStation;
//        getTicketPriceByStation();
//        setEndStationInfo(mEndStation);
//    }
//
//
//    private void showProgressView() {
//        //        BizApplication.getInstance().getProgressDialog(BuyTicketByStationActivity.this, true).show();
//        showProgress();
//    }
//
//    private void dismissProgressView() {
//        //        BizApplication.getInstance().dismissProgressDialog();
//        dismissProgress();
//    }
//
//    /**
//     * 调换起点和终点
//     */
//    private void switchStation() {
//        int horizontalPadding = getResources().getDimensionPixelOffset(R.dimen.st_book_by_station_padding1);
//
//        int[] locationLeft = new int[2];
//        mLlyLeft.getLocationOnScreen(locationLeft);
//        int leftX = locationLeft[0];
//        int[] locationRight = new int[2];
//        mLlyRight.getLocationOnScreen(locationRight);
//        int rightX = locationRight[0];
//
//
//        //        int moveDistance = switchBtnWidth + stationViewWidth;
//        //        MLog.d(TAG, "switch btn width = " + switchBtnWidth + " stationView width " + leftViewWidth);
//        //        LinearLayout.LayoutParams left = (LinearLayout.LayoutParams) mLlyLeft.getLayoutParams();
//        //        LinearLayout.LayoutParams right = (LinearLayout.LayoutParams) mLlyRight.getLayoutParams();
//        int rightMoveDistance = rightX - leftX;
//        int leftViewWidth = mLlyLeft.getWidth();
//        int leftMoveDistance = DeviceInfo.getScreenWidth(mContext) - horizontalPadding * 2 * 2 - mLlyLeft.getWidth();
//        MLog.d(TAG, "leftX=" + leftX + " rightX=" + rightX + " leftMoveDistance=" + leftMoveDistance + " rightMoveDistance=" + rightMoveDistance + " leftWidth = " + leftViewWidth);
//        if (!isSwitched) {
//            mLlyLeft.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
//            mLlyRight.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            isSwitched = true;
//            mTempStation = mStartStation;
//            mStartStation = mEndStation;
//            mEndStation = mTempStation;
//            animateView(mLlyLeft, 0, leftMoveDistance);
//            animateView(mLlyRight, 0, -rightMoveDistance);
//        } else {
//            mLlyLeft.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            mLlyRight.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
//            isSwitched = false;
//            mTempStation = mStartStation;
//            mStartStation = mEndStation;
//            mEndStation = mTempStation;
//            animateView(mLlyLeft, Math.abs(rightMoveDistance), 0);
//            animateView(mLlyRight, rightMoveDistance, 0);
//            //            mLlyLeft.setGravity(Gravity.LEFT);
//            //            mLlyRight.setGravity(Gravity.RIGHT);
//        }
//
//        RotateAnimation animation = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//        animation.setFillEnabled(true);
//        animation.setFillAfter(true);
//        animation.setDuration(300);
//        mImgSwitchStation.startAnimation(animation);
//
//    }
//
//    /**
//     * @param view          将要移动的view
//     * @param startPosition 起始位置
//     * @param endPosition   结束位置
//     */
//    private void animateView(final View view, int startPosition, int endPosition) {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", startPosition, endPosition);
//        animator.setInterpolator(null);
//        animator.setDuration(500);
//        animator.start();
//        animator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                int x = location[0];
//                int y = location[1];
//                //                mImgSwitchStation.invalidate();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//    }
//
//    /**
//     * 根据站点获取单价
//     */
//    private void getTicketPriceByStation() {
//        showProgressView();
//        if (mStartStation == null || mEndStation == null) {
//            dismissProgress();
//            return;
//        }
//
//        mSTGateway.getTicketPriceByStation(mCityCode, mStartStation.getStationCode(), mEndStation.getStationCode(), new MobileGateway.MobileGatewayListener<GetTicketPriceByStationRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(mContext, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(mContext);
//            }
//
//            @Override
//            public void onSuccess(GetTicketPriceByStationRs response) {
//                dismissProgressView();
//                //mTicketPrice=100;
//                mTicketPrice = response.getTicketPrice();
//                MLog.d(TAG, "mTicketPrice=" + mTicketPrice);
//                mTvTicketPrice.setText(renminbi + " " + Utils.parseMoney(mTicketPrice));
//                calculatePrice();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(mContext);
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
//        });
//    }
//
//    /**
//     * 计算票价
//     */
//    private void calculatePrice() {
//        mTicketAmount = mTicketPrice * mTicketCount;
//        mTvTicketTotalPrice.setText(getString(R.string.st_renminbi) + "  " + Utils.parseMoney(mTicketAmount));
//    }
//
//    public StationCode getStartStation() {
//        return mStartStation;
//    }
//
//    public StationCode getEndStation() {
//        return mEndStation;
//    }
//
//    private void generateOrder() {
//        showProgressView();
//        String numberLoginId = LoginManager.getPhoneNumber(mContext);
//        String ticketType;
//        if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_TICKET)) {
//            ticketType = CssConstant.SINGLE_TICKET.TICKET_TYPE_COMMON_COMPLETE_STATION;
//        } else {
//            ticketType = CssConstant.SINGLE_TICKET.TICKET_TYPE_QR_CODE_COMPLETE_STATION;
//        }
//        mSTGateway.generateTicketOrder(mServiceId, ticketType, numberLoginId, mCityCode, mStartStation.getStationCode(), mEndStation.getStationCode(), mTicketPrice, mTicketCount, new MobileGateway.MobileGatewayListener<GenerateTicketorderRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(mContext, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(mContext);
//            }
//
//            @Override
//            public void onSuccess(GenerateTicketorderRs response) {
//                MLog.d(TAG, "PayOrderSuccess");
//                mGenerateCustomerOrderRs = response;
//                dismissProgressView();
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    getPanchanToken(response);
//                } else {
//                    payOrder(response);
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(mContext);
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
//                //                handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    private void payOrder(GenerateTicketorderRs response) {
//
//        if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//            getPanchanToken(response);
//        } else {
//            PanChanManager.getInstance(mContext).pay(response.getPanchanPayInfo(), BizApplication.getInstance().getPanchanToken(), new PanChanManager.HandlePanchanPayResultListener() {
//                @Override
//                public void onPaySuccess(String msg, String orderNumber) {
//                    //支付成功跳转到支付完成页面
//                    MLog.d(TAG, "PaySucess=" + msg);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (mGenerateCustomerOrderRs != null) {
//                                SingleTicketManager.moveToPaySuccessPage(mContext, mServiceId, mGenerateCustomerOrderRs.getPanchanPayInfo().getOrderNo(), mCityCode, CssConstant.SINGLE_TICKET.GATE_STATUS_ENTRY, false, "");
//                            }
//                        }
//                    }, 300);
//                }
//
//                @Override
//                public void onPayFailed(String msg) {
//                    MLog.d(TAG, "PayFailed=" + msg);
//                    PanChanManager.getInstance(mContext).handleCommonFailedMsg(msg);
//                }
//
//            });
//        }
//    }
//
//    private void getPanchanToken(final GenerateTicketorderRs res) {
//        showProgressView();
//        mWalletGateway.getPanchanToken(new MobileGateway.MobileGatewayListener<GetPanchanTokenRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                Toast.makeText(mContext, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                CommonToast.onHttpFailed(mContext);
//            }
//
//            @Override
//            public void onSuccess(GetPanchanTokenRs response) {
//                dismissProgressView();
//                if (TextUtils.isEmpty(BizApplication.getInstance().getPanchanToken())) {
//                    Toast.makeText(mContext, getString(R.string.get_token_failed), Toast.LENGTH_SHORT).show();
//                } else {
//                    //                    if (mGenerateCustomerOrderRs != null)
//                    payOrder(res);
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(mContext);
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
//                handleAutoLoginFailed(result);
//            }
//        });
//    }
//
//    /**
//     * 获取可购买最大数量
//     **/
//    private void getAvailableMaxCount() {
//        mSTGateway.getBuySinlgeTicketMaxNum(mCityCode, new MobileGateway.MobileGatewayListener<GetBuySinlgeTicketMaxNumRs>() {
//            @Override
//            public void onFailed(Result result) {
//
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//
//            }
//
//            @Override
//            public void onSuccess(GetBuySinlgeTicketMaxNumRs response) {
//                mMaxCount = response.getBuySinlgeTicketMaxNum();
//            }
//
//            @Override
//            public void onNoNetwork() {
//
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//
//            }
//
//        });
//    }
//
//    /**
//     * 获取公交路径
//     */
//    private void getBusPath() {
//        ThirdMapService.getInstance(mContext).setOnGetBusPathListener(new ThirdMapService.OnGetBusPathListener() {
//            @Override
//            public void onGetBusPathSuccess(BusPath busPath, BusRouteResult busRouteResult) {
//                MLog.d(TAG, "onGetBusPathSuccess");
//                List<SchemeBusStep> busStepList = new ArrayList<>();
//
//                List<BusStep> list = busPath.getSteps();
//                for (BusStep busStep : list) {
//                    if (busStep.getWalk() != null && busStep.getWalk().getDistance() > 0) {
//                        SchemeBusStep walk = new SchemeBusStep(busStep);
//                        walk.setWalk(true);
//                        busStepList.add(walk);
//                    }
//                    if (busStep.getBusLines().get(0) != null) {
//                        SchemeBusStep bus = new SchemeBusStep(busStep);
//                        bus.setBus(true);
//                        busStepList.add(bus);
//                        MLog.d(TAG, "### bus = " + bus.getBusLines().get(0).getBusLineName() + "" + (bus.getBusLines().get(0).getPassStationNum() + 1) + "站");
//                    }
//                    if (busStep.getRailway() != null) {
//                        SchemeBusStep railway = new SchemeBusStep(busStep);
//                        railway.setRailway(true);
//                        busStepList.add(railway);
//                    }
//                    if (busStep.getTaxi() != null) {
//                        SchemeBusStep taxi = new SchemeBusStep(busStep);
//                        taxi.setTaxi(true);
//                        busStepList.add(taxi);
//                    }
//                }
//                mBusRouteLv0ItemList.clear();
//                for (int i = 0; i < busStepList.size(); i++) {
//                    BusRouteLv0Item lv0Item = new BusRouteLv0Item();
//                    lv0Item.setSchemeBusStep(busStepList.get(i));
//                    mBusRouteLv0ItemList.add(lv0Item);
//                }
//                mCssBusRouteAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onGetBusPathFailed(int errorCode) {
//                MLog.d(TAG, "onGetBusPathFailed");
//            }
//        });
//        double startLat = 39.992894;
//        double startLon = 116.337742;
//        double endLat = 39.957904;
//        double endLon = 116.32322;
//        ThirdMapService.getInstance(mContext).busRouteQuery(startLat, startLon, endLat, endLon, "010");
//        //        ThirdMapService.getInstance(BuyTicketByStationActivity.this).busRouteQuery();
//    }
//
//    private boolean isSameStation(StationCode stationCode1, StationCode stationCode2) {
//        if (mEndStation != null && mStartStation != null) {
//            if (stationCode1 != null && stationCode2 != null) {
//                if (stationCode1.getStationNameZH().trim().equals(stationCode2.getStationNameZH().trim())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 设置是否是再次购买
//     */
//    public void setIsBuyAgain(boolean isBuyAgain) {
//        this.isBuyAgain = isBuyAgain;
//    }
//
//    /**
//     * 切换二维码单程票和普通单程票
//     */
//    public void updateServiceType(String serviceId) {
//        mServiceId = serviceId;
//        if (mImgCountDown != null && mImgCountUp != null && mTvTicketCount != null && mTvTicketIntroduct != null && mTvTicketTotalPrice != null) {
//            if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                mImgCountDown.setVisibility(View.GONE);
//                mImgCountUp.setVisibility(View.GONE);
//                mTvTicketCount.setBackgroundResource(0);
//                mTvTicketIntroduct.setText(getString(R.string.st_qr_ticket_introduction));
//                mTicketCount = 1;
//                mTvTicketCount.setText(String.valueOf(mTicketCount));
//                calculatePrice();
//            } else {
//                mImgCountDown.setVisibility(View.VISIBLE);
//                mImgCountUp.setVisibility(View.VISIBLE);
//                mTvTicketCount.setBackgroundResource(R.drawable.bg_middle_nor);
//                mTvTicketIntroduct.setText(getString(R.string.st_common_ticket_introduction));
//                mTicketCount = 1;
//                mTvTicketCount.setText(String.valueOf(mTicketCount));
//                calculatePrice();
//            }
//        } else {
//            MLog.d(TAG, "updateServiceType  view is not initialize");
//        }
//    }
//
//    /**
//     * EventBus
//     * 选择站点页面发送此事件
//     */
//    @Subscribe
//    public void onEventMainThread(IEvent.SelectStationEvent event) {
//        MLog.d(TAG, "%% onEventMainThread ");
//        if (event != null) {
//            StationCode stationCode = event.getStation();
//            int type = event.getType();
//            MLog.d(TAG, "isSwitch =" + isSwitched + "  type =" + type);
//            if (stationCode == null) {
//                MLog.d(TAG, "onEventMainThread STATION is null");
//                return;
//            }
//            if (type == SingleTicketManager.SELECT_START_STATION_REQUEST) {
//                //                setStartStation(stationCode);
//                if (isSwitched) {
//                    if (isSameStation(mEndStation, stationCode)) {
//                        CommonToast.toast(mContext, getString(R.string.st_slelect_different_station));
//                        return;
//                    } else {
//                        mStartStation = stationCode;
//                        setEndStationInfo(mStartStation);
//                        getTicketPriceByStation();
//                    }
//                } else {
//                    if (isSameStation(mEndStation, stationCode)) {
//                        CommonToast.toast(mContext, getString(R.string.st_slelect_different_station));
//                        return;
//                    } else {
//                        mStartStation = stationCode;
//                        setStartStationInfo(mStartStation);
//                        getTicketPriceByStation();
//                    }
//                }
//            } else if (type == SingleTicketManager.SELECT_END_STATION_REQUEST) {
//                //                setEndStation(stationCode);
//                if (isSwitched) {
//                    if (isSameStation(mStartStation, stationCode)) {
//                        CommonToast.toast(mContext, getString(R.string.st_slelect_different_station));
//                        return;
//                    } else {
//                        mEndStation = stationCode;
//                        setStartStationInfo(mEndStation);
//                        getTicketPriceByStation();
//                    }
//                } else {
//                    if (isSameStation(mStartStation, stationCode)) {
//                        CommonToast.toast(mContext, getString(R.string.st_slelect_different_station));
//                        return;
//                    } else {
//                        mEndStation = stationCode;
//                        setEndStationInfo(mEndStation);
//                        getTicketPriceByStation();
//                    }
//                }
//            }
//            //            CommonToast.toast(getActivity(), "选择站点： " + type);
//        } else
//            MLog.d(TAG, "onEventMainThread event is null");
//
//        EventBus.getDefault().unregister(this);
//    }
//
//}
