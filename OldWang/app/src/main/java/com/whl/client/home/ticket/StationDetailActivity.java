//package com.cssweb.shankephone.home.ticket;
//
//import android.annotation.TargetApi;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.TypedValue;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.amap.api.services.core.PoiItem;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.app.IEvent;
//import com.cssweb.shankephone.bdlocationservice.ThirdMapService;
//import com.cssweb.shankephone.db.SingleTicketDBManager;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.ExitData;
//import com.cssweb.shankephone.gateway.model.singleticket.ExitPoi;
//import com.cssweb.shankephone.gateway.model.singleticket.GetStationDetailRs;
//import com.cssweb.shankephone.gateway.model.singleticket.LineCode;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.spservice.GetSupportServiceYnRs;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//import org.greenrobot.eventbus.EventBus;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//
///**
// * Created by liwx on 2017/4/7.
// * 站点详情页面
// */
//
//public class StationDetailActivity extends BaseActivity implements View.OnClickListener {
//    private static final String TAG = "StationDetailActivity";
//    private ExecutorService mExecutor;
//    private StationCode mCurrentStation;
//    private SingleTicketDBManager mSingleTicketDBManager;
//    private StationListAdapter mStationListAdapter;
//
//    private RecyclerView mRecyclerView;
//    private LinearLayout mLlyLineNameParent;
//    private TextView mTvStationName;
//    private TextView mTvgate, mTvBusInfo;
//    private RelativeLayout mRlGate, mRlBusInfo;
//    private ImageView mLineOne, mLineTwo;
//    private int mRequestType;
//    private TextView mSetStart;
//    private STGateway mSTGateway;
//    private TextView mBuyTicketByPrice;
//    private boolean isSupportQrCodeTicket = false;
//
//    private StationGateFragment mStationGateFragment;//出入口fragment
//    private StationBusInfoFragment mStationBusInfoFragment;//附近公交fragment
//
//
//    private List<StationCode> mStationCodeList = new ArrayList<>();
//    private List<StationCode> mTmpStationList = new ArrayList<>();
//    private List<LineCode> mTmpLineCodeList = new ArrayList<>();//获取换乘站的线路代码用
//    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
//    private List<StationCode> tempStationList = new ArrayList<>();
//
//    private String mTmpLineCode;
//    private StationCode stationCodeRe;
//    private static StationDetailActivity activity;
//    private int transPosition;
//    private String mCurLineName;
//    private String stationCode;
//    private String stationCodePoi;
//    private List<ExitData> exitDataList = new ArrayList<>();
//    private int doubleClick = 1;
//    private boolean mQrCOdeSjtServerYN;
//
//    /**
//     * ATTENTION: This was auto-generated to implement the App Indexing API.
//     * See https://g.co/AppIndexing/AndroidStudio for more information.
//     */
//    //    private GoogleApiClient client;
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_station_detail);
//        mSTGateway = new STGateway(this);
//        initView();
//        initData();
//        switchFragment(mStationGateFragment);
//        getGateBusInfo(1);
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        //        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
//    }
//
//    public static StationDetailActivity getInstance() {
//        activity = new StationDetailActivity();
//        return activity;
//    }
//
//    private void initView() {
//        mStationGateFragment = StationGateFragment.getInstance();
//        mStationBusInfoFragment = StationBusInfoFragment.getInstance();
//        mFragmentList.add(mStationGateFragment);
//        mFragmentList.add(mStationBusInfoFragment);
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.lly_station_gate_info, mStationGateFragment);
//        transaction.add(R.id.lly_station_gate_info, mStationBusInfoFragment);
//        transaction.hide(mStationBusInfoFragment);
//        transaction.show(mStationGateFragment);
//        transaction.commitAllowingStateLoss();
//
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setTitle(getString(R.string.st_station_detail));
//        titleBarView.setOnTitleBarClickListener(new TitleBarView.OnTitleBarClickListener() {
//            @Override
//            public void onBackClicked(View view) {
//                finish();
//            }
//
//            @Override
//            public void onMenuClicked(View view) {
//
//            }
//        });
//        mBuyTicketByPrice = (TextView) findViewById(R.id.tv_buy_by_price);
//        mBuyTicketByPrice.setOnClickListener(this);
//        mTvStationName = (TextView) findViewById(R.id.tv_station_name);
//        mLlyLineNameParent = (LinearLayout) findViewById(R.id.lly_line_parent);
//
//        mSetStart = (TextView) findViewById(R.id.tv_set_start);
//        mSetStart.setOnClickListener(this);
//        findViewById(R.id.tv_set_end).setOnClickListener(this);
//        mLineTwo = (ImageView) findViewById(R.id.iv_line_two);
//        mLineOne = (ImageView) findViewById(R.id.iv_line_one);
//        mTvgate = (TextView) findViewById(R.id.tv_gate);
//        mRlGate = (RelativeLayout) findViewById(R.id.rl_gate);
//        mRlBusInfo = (RelativeLayout) findViewById(R.id.rl_bus_info);
//        mRlGate.setOnClickListener(this);
//        mRlBusInfo.setOnClickListener(this);
//        mTvBusInfo = (TextView) findViewById(R.id.tv_bus_info);
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(StationDetailActivity.this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mRecyclerView.setLayoutManager(linearLayoutManager);
//        mStationListAdapter = new StationListAdapter(StationDetailActivity.this, mStationCodeList);
//        mRecyclerView.setAdapter(mStationListAdapter);
//        mStationListAdapter.setOnStationClickListener(new StationListAdapter.OnStationClickListener() {
//            @Override
//            public void onStationClicked(StationCode station) {
//                //                CommonToast.toast(getApplicationContext(), "点击了站点 =" + station.getStationNameZH());
//                if (!station.getStationNameZH().equals(mCurrentStation.getStationNameZH())) {
//                    stationCodePoi = station.getStationCode();
//                    stationCodeRe = station;
//                    setStartBtn();
//                    mStationListAdapter.setSelectedStation(station);
//                    mStationListAdapter.notifyDataSetChanged();
//                    updateStationListByStationCode(station.getStationCode(), 0);
//                    if (mStationGateFragment.isVisible()) {
//                        mStationGateFragment.clearData();
//                        getGateBusInfo(1);
//                        switchFragment(mStationGateFragment);
//                    } else {
//                        mStationBusInfoFragment.clearData();
//                        getGateBusInfo(0);
//                        switchFragment(mStationBusInfoFragment);
//                    }
//                    MLog.d(TAG, "stationCode:" + stationCodeRe.getStationCode());
//                }
//            }
//        });
//    }
//
//    /**
//     * 设置设为起点站按钮
//     */
//    private void setStartBtn() {
//        if (!mQrCOdeSjtServerYN) {
//            if (!stationCodeRe.getUseYn().equalsIgnoreCase("Y")) {
//                mSetStart.setBackgroundResource(R.drawable.btn_start_dis);
//                mBuyTicketByPrice.setBackgroundColor(this.getResources().getColor(R.color.about_app_version));
//            } else {
//                mSetStart.setBackgroundResource(R.drawable.selector_set_start_station);
//                mBuyTicketByPrice.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_by_price));
//            }
//        } else {
//            mSetStart.setBackgroundResource(R.drawable.selector_set_start_station);
//            mBuyTicketByPrice.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.selector_by_price));
//        }
//
//    }
//
//    private void initData() {
//        mExecutor = BizApplication.getInstance().getThreadPool();
//        mSingleTicketDBManager = new SingleTicketDBManager(getApplicationContext());
//        stationCode = getIntent().getStringExtra(CssConstant.KEY_STATION_CODE);
//        mQrCOdeSjtServerYN = (boolean) getIntent().getExtras().get(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN);
//        MLog.d(TAG, "mQrCOdeSjtServerYN:" + mQrCOdeSjtServerYN);
//        if (stationCode != null) {
//            stationCodePoi = stationCode;
//        }
//        //        mCurLineName = getIntent().getStringExtra(CssConstant.KEY_LINE_NAME);
//        stationCodeRe = SingleTicketDBManager.getStationInfoByStationCode(stationCode);
//        LineCode lineCodes = SingleTicketDBManager.getLineInfoByLineCode(stationCodeRe.getLineCode());
//        if (lineCodes != null) {
//            mCurLineName = lineCodes.getLineNameZH();
//        }
//        setStartBtn();
//        mStationListAdapter.setSelectedStation(stationCodeRe);
//        updateStationListByStationCode(stationCode, 1);
//    }
//
//    private void updateStationListByStationCode(final String stationCode, final int tag) {
//        mExecutor.execute(new Runnable() {
//                              @Override
//                              public void run() {
//                                  mCurrentStation = SingleTicketDBManager.getStationInfoByStationCode(stationCode);
//                                  mTmpLineCodeList.clear();
//                                  if (mCurrentStation != null) {
//                                      mTmpLineCode = mCurrentStation.getLineCode();
//                                      if (mCurrentStation.getTransferYn().equalsIgnoreCase("N")) {
//                                          mTmpStationList = SingleTicketDBManager.getStationCodeListByLineCode(mTmpLineCode);
//                                          mTmpLineCodeList.add(SingleTicketDBManager.getLineInfoByLineCode(mTmpLineCode));
//                                      } else {
//                                          if (tag == 1) {
//                                              mTmpStationList = SingleTicketDBManager.getStationCodeListByLineCode(mTmpLineCode);
//                                          }
//                                          tempStationList = SingleTicketDBManager.getStationInfoListByStationName(mCurrentStation.getStationNameZH());
//                                          if (tempStationList != null) {
//                                              mTmpLineCodeList.clear();
//                                              for (StationCode station : tempStationList) {
//                                                  LineCode lineCode = SingleTicketDBManager.getLineInfoByLineCode(station.getLineCode());
//                                                  mTmpLineCodeList.add(lineCode);
//                                              }
//
//                                          }
//
//                                      }
//                                      mHandler.post(new Runnable() {
//                                          @Override
//                                          public void run() {
//                                              /**
//                                               * 显示站点
//                                               * */
//                                              mLlyLineNameParent.removeAllViews();
//                                              if (mCurrentStation.getTransferYn().equalsIgnoreCase("n")) {
//                                                  mLlyLineNameParent.addView(getLineNameView(mTmpLineCodeList.get(0).getLineNameZH()));
//                                              } else {
//                                                  for (LineCode lineCode : mTmpLineCodeList) {
//                                                      mLlyLineNameParent.addView(getLineNameView(lineCode.getLineNameZH()));
//                                                      mLlyLineNameParent.invalidate();
//                                                  }
//                                              }
//                                              if (mLlyLineNameParent.getChildCount() > 1) {//两个以上的站换成
//                                                  for (int count = 0; count < mLlyLineNameParent.getChildCount(); count++) {
//                                                      final int finalCount = count;
//                                                      if (((TextView) mLlyLineNameParent.getChildAt(count)).getText().toString().equals(mCurLineName)) {
//                                                          setLineTextColor(finalCount);
//                                                      }
//                                                      mLlyLineNameParent.getChildAt(count).setOnClickListener(new View.OnClickListener() {
//                                                          @Override
//                                                          public void onClick(View view) {
//                                                              String stationCode = tempStationList.get(finalCount).getLineCode();
//                                                              mTmpStationList = SingleTicketDBManager.getStationCodeListByLineCode(stationCode);
//                                                              mStationCodeList.clear();
//                                                              mStationCodeList.addAll(mTmpStationList);
//                                                              mCurLineName = ((TextView) mLlyLineNameParent.getChildAt(finalCount)).getText().toString();
//                                                              mTvStationName.setText(mCurrentStation.getStationNameZH());
//                                                              setLineTextColor(finalCount);
//                                                              mStationListAdapter.notifyDataSetChanged();
//
//                                                          }
//                                                      });
//                                                  }
//                                              }
//                                              mStationCodeList.clear();
//                                              mStationCodeList.addAll(mTmpStationList);
//                                              mTvStationName.setText(mCurrentStation.getStationNameZH());
//                                              setStationNameTextSize(mCurrentStation.getStationNameZH());
//                                              mStationListAdapter.notifyDataSetChanged();
//                                          }
//                                      });
//                                  } else
//                                      MLog.d(TAG, "local station info is null");
//                              }
//                          }
//
//        );
//    }
//
//    private void setStationNameTextSize(String stationName) {
//        if (!TextUtils.isEmpty(stationName)) {
//            if (stationName.length() > 4) {
//                mTvStationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//            } else {
//                mTvStationName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//            }
//        }
//    }
//
//    public void setLineTextColor(int flag) {
//        for (int i = 0; i < mLlyLineNameParent.getChildCount(); i++) {
//            if (i == flag) {
//                ((TextView) mLlyLineNameParent.getChildAt(flag)).setTextColor(getResources().getColor(R.color.bottom_bg_selected));
//            } else {
//                ((TextView) mLlyLineNameParent.getChildAt(i)).setTextColor(getResources().getColor(R.color.black));
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_StationDetailActivity));
//        //        switchColor(1);
//        //        switchFragment(mStationGateFragment);
//        //        mStationGateFragment.getStationExitData(BizApplication.getInstance().getCityCode(), stationCodeRe.getStationCode());
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_StationDetailActivity));
//    }
//
//    private TextView getLineNameView(String lineName) {
//        TextView textView = new TextView(StationDetailActivity.this);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
//        textView.setLayoutParams(layoutParams);
//        textView.setText(lineName);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextColor(getResources().getColor(R.color.bottom_bg_selected));
//        return textView;
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rl_gate:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                if (mStationGateFragment.isVisible()) {
//                    return;
//                }
//                switchColor(1);
//                switchFragment(mStationGateFragment);
//                getGateBusInfo(1);
//                break;
//            case R.id.rl_bus_info:
//                if (Utils.isFastDoubleClick()) {
//                    return;
//                }
//                if (mStationBusInfoFragment.isVisible()) {
//                    return;
//                }
//                switchColor(0);
//                switchFragment(mStationBusInfoFragment);
//                getGateBusInfo(0);
//                break;
//            case R.id.tv_set_start:
//                if (!mQrCOdeSjtServerYN) {
//                    if (!stationCodeRe.getUseYn().equalsIgnoreCase("Y")) {
//                        CommonToast.toast(getApplicationContext(), String.format(getString(R.string.no_get_ticket_device), stationCodeRe.getStationNameZH()));
//                        return;
//                    }
//                }
//                mRequestType = SingleTicketManager.SELECT_START_STATION_REQUEST;
//                returnSelectedStation();
//                //                STMapFragment.getInstance().setStartStaion(stationCode);
//                finish();
//                break;
//            case R.id.tv_set_end:
//                mRequestType = SingleTicketManager.SELECT_END_STATION_REQUEST;
//                //                STMapFragment.getInstance().setEndStations(stationCode);
//                returnSelectedStation();
//                finish();
//                break;
//            case R.id.tv_buy_by_price:
//                if (!mQrCOdeSjtServerYN) {
//                    if (!stationCodeRe.getUseYn().equalsIgnoreCase("Y")) {
//                        CommonToast.toast(getApplicationContext(), String.format(getString(R.string.no_get_ticket_device), stationCodeRe.getStationNameZH()));
//                        return;
//                    }
//                }
//                getSupportServiceYn(BizApplication.getInstance().getCityCode(), true);
//                break;
//        }
//    }
//
//    private void returnSelectedStation() {
//        EventBus.getDefault().post(new IEvent.SelectStationEvent(mRequestType, stationCodeRe));
//        finish();
//    }
//
//    /**
//     * 是否支持二维码单程票接口
//     *
//     * @param cityCode
//     * @param showProgress
//     */
//    private void getSupportServiceYn(final String cityCode, boolean showProgress) {
//        if (showProgress)
//            showProgress();
//        mSTGateway.getSupportServiceYn(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, cityCode, new MobileGateway.MobileGatewayListener<GetSupportServiceYnRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                CommonToast.onFailed(StationDetailActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgress();
//                CommonToast.onHttpFailed(StationDetailActivity.this);
//            }
//
//            @Override
//            public void onSuccess(GetSupportServiceYnRs response) {
//                dismissProgress();
//                Intent buyPriceIntent = new Intent(StationDetailActivity.this, BuyTicketByPriceActivity.class);
//                buyPriceIntent.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, response.getSupportServiceYn());
//                buyPriceIntent.putExtra(CssConstant.KEY_CITY_CODE, BizApplication.getInstance().getCityCode());
//                buyPriceIntent.putExtra(SingleTicketManager.START_STATION, stationCodeRe);
//                startActivity(buyPriceIntent);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgress();
//                CommonToast.onNoNetwork(StationDetailActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgress();
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
//     * 获取高德信息
//     *
//     * @param flag
//     */
//    private void getGateBusInfo(final int flag) {
//        MLog.d(TAG, "stationCodePoi:" + stationCodePoi + "getCityCode:" + BizApplication.getInstance().getCityCode());
//        mSTGateway.getStationDetail(BizApplication.getInstance().getCityCode(), stationCodePoi, new MobileGateway.MobileGatewayListener<GetStationDetailRs>() {
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
//            public void onSuccess(GetStationDetailRs response) {
//                exitDataList = response.getExitDataList();
//                if (flag == 0) {
//                    ThirdMapService.getInstance(StationDetailActivity.this).getExitBusStation(response.getStationCode(), new ThirdMapService.OnGetExitPoiListener() {
//                        @Override
//                        public void onGetExitPoiComplete(List<ExitPoi> poiItemList) {
//                            MLog.d(TAG, "onGetExitPoiComplete:" + poiItemList);
//                        }
//
//                        @Override
//                        public void onGetExitBusStationComplete(List<PoiItem> poiItemList) {
//                            mStationBusInfoFragment.setPoiList(poiItemList);
//                        }
//                    });
//                } else if (flag == 1) {
//                    ThirdMapService.getInstance(StationDetailActivity.this).getExitPoi(response.getExitDataList(), new ThirdMapService.OnGetExitPoiListener() {
//                        @Override
//                        public void onGetExitPoiComplete(List<ExitPoi> poiItemList) {
//                            MLog.d(TAG, "出入口poiItemList:" + poiItemList);
//                            mStationGateFragment.setPoiListExit(poiItemList);
//                        }
//
//                        @Override
//                        public void onGetExitBusStationComplete(List<PoiItem> poiItemList) {
//
//                        }
//                    });
//                }
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
//        });
//    }
//
//    private void switchColor(int index) {
//        mTvgate.setTextColor(index == 0 ? this.getResources().getColor(R.color.black) : this.getResources().getColor(R.color.bottom_bg_selected));
//        mTvBusInfo.setTextColor(index == 1 ? this.getResources().getColor(R.color.black) : this.getResources().getColor(R.color.bottom_bg_selected));
//
//        mLineOne.setVisibility(index == 1 ? View.VISIBLE : View.GONE);
//        mLineTwo.setVisibility(index == 0 ? View.VISIBLE : View.GONE);
//    }
//
//    private void switchFragment(Fragment targetFragment) {
//        if (targetFragment != null) {
//            if (targetFragment.isVisible()) {
//                MLog.d(TAG, "targetFragment is visible");
//                return;
//            }
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            for (Fragment fragment : mFragmentList) {
//                transaction.hide(fragment);
//            }
//            transaction.show(targetFragment);
//            transaction.commitAllowingStateLoss();
//        }
//    }
//
//    //    /**
//    //     * ATTENTION: This was auto-generated to implement the App Indexing API.
//    //     * See https://g.co/AppIndexing/AndroidStudio for more information.
//    //     */
//    //    public Action getIndexApiAction() {
//    //        Thing object = new Thing.Builder().setName("StationDetail Page") // TODO: Define a title for the content shown.
//    //            // TODO: Make sure this auto-generated URL is correct.
//    //            .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]")).build();
//    //        return new Action.Builder(Action.TYPE_VIEW).setObject(object).setActionStatus(Action.STATUS_TYPE_COMPLETED).build();
//    //    }
//    //
//    //    @Override
//    //    public void onStart() {
//    //        super.onStart();
//    //
//    //        // ATTENTION: This was auto-generated to implement the App Indexing API.
//    //        // See https://g.co/AppIndexing/AndroidStudio for more information.
//    //        client.connect();
//    //        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    //    }
//    //
//    //    @Override
//    //    public void onStop() {
//    //        super.onStop();
//    //
//    //        // ATTENTION: This was auto-generated to implement the App Indexing API.
//    //        // See https://g.co/AppIndexing/AndroidStudio for more information.
//    //        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//    //        client.disconnect();
//    //    }
//
//}
