//package com.cssweb.shankephone.home.ticket;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
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
//import com.cssweb.shankephone.gateway.model.singleticket.GetStationDetailRs;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.home.BrowserActivity;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//
//import java.util.ArrayList;
//
///**
// * Created by liwx on 2015/11/12.
// * 购票信息页面，选择起点、终点后进入此页面
// */
//public class BuyTicketByStationActivity extends BaseActivity implements View.OnClickListener {
//    private static final String TAG = "BuyTicketByStationActivity";
//
//    private static final int STATION_TYPE_START = 1;
//    private static final int STATION_TYPE_END = 2;
//    private StationCode mStartStation;
//    private StationCode mEndStation;
//
//    private BuyTicketByStationFragment mCommonSingleTicketFragment;//普通单程票
//    //    private BuyTicketByStationFragment mQrCodeSingleTicketFragment;//二维码单程票
//    private TextView mTvQrCodeTicket;
//    private TextView mTvCommonTicket;
//    private ImageView mImgQrCodeTicketLine;
//    private ImageView mImgCommonTicketLine;
//    private TitleBarView titleBarView;
//
//    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
//    private String mCityCode;
//    private String isSupportQrCodeTicketYn = "n";
//    private boolean isBuyAgain = false;//是否是再次购买
//    private String mServiceId;//再次购买逻辑时使用此字段
//
//    private STGateway mSTGateway;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        BizApplication.getInstance().addActivity(this);
//        setContentView(R.layout.activity_book_by_station_detail);
//        mFragmentList.clear();
//        mSTGateway = new STGateway(this);
//        try {
//            Intent intent = getIntent();
//            mStartStation = (StationCode) intent.getSerializableExtra(SingleTicketManager.START_STATION);
//            mEndStation = (StationCode) intent.getSerializableExtra(SingleTicketManager.END_STATION);
//            mCityCode = intent.getStringExtra(CssConstant.KEY_CITY_CODE);
//            isSupportQrCodeTicketYn = intent.getStringExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN);
//            MLog.d(TAG, "isSupportQrCodeTicketYn:" + isSupportQrCodeTicketYn);
//            isBuyAgain = intent.getBooleanExtra(CssConstant.KEY_IS_BUY_AGAIN, false);
//            mServiceId = intent.getStringExtra(CssConstant.KEY_SERVICE_ID);
//            if (isBuyAgain) {
//                getStationDetail(STATION_TYPE_START, mCityCode, mStartStation.getStationCode());
//                getStationDetail(STATION_TYPE_END, mCityCode, mEndStation.getStationCode());
//            }
//            MLog.d(TAG, "CityCode===" + mCityCode);
//        } catch (Exception e) {
//            MLog.d(TAG, "parse intent ", e);
//        }
//        if (isSupportQrCodeTicketYn.equals("Y")) {
//            mCommonSingleTicketFragment = BuyTicketByStationFragment.getInstance(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, mCityCode);
//        } else {
//            mCommonSingleTicketFragment = BuyTicketByStationFragment.getInstance(CssConstant.SP_SERVICE.SERVICE_ID_TICKET, mCityCode);
//        }
//        //        mQrCodeSingleTicketFragment = BuyTicketByStationFragment.getInstance(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, mCityCode);
//        mCommonSingleTicketFragment.setIsBuyAgain(isBuyAgain);
//        mFragmentList.add(mCommonSingleTicketFragment);
//        //        mFragmentList.add(mQrCodeSingleTicketFragment);
//        if (!isBuyAgain)
//            initStationData();
//        initView();
//        MLog.d(TAG, " start=" + mStartStation.getStationNameZH() + " end=" + mEndStation.getStationNameZH());
//
//    }
//
//    public void initView() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        //        if (!TextUtils.isEmpty(isSupportQrCodeTicketYn) && isSupportQrCodeTicketYn.equalsIgnoreCase("Y")) {
//        //            transaction.add(R.id.lly_ticket_detail, mQrCodeSingleTicketFragment);
//        //        }
//        transaction.add(R.id.lly_ticket_detail, mCommonSingleTicketFragment);
//        //        transaction.hide(mCommonSingleTicketFragment);
//        transaction.commitAllowingStateLoss();
//
//        titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        if (!TextUtils.isEmpty(isSupportQrCodeTicketYn) && isSupportQrCodeTicketYn.equalsIgnoreCase("Y")) {
//            titleBarView.setMenu(true);
//        } else {
//            titleBarView.setMenu(false);
//        }
//        titleBarView.setTitle(getString(R.string.st_ticket_information));
//        titleBarView.setMenuName(getString(R.string.help));
//        titleBarView.setOnTitleBarClickListener(new TitleBarView.OnTitleBarClickListener() {
//            @Override
//            public void onBackClicked(View view) {
//                finish();
//            }
//
//            @Override
//            public void onMenuClicked(View view) {
//                Intent intent = new Intent(BuyTicketByStationActivity.this, BrowserActivity.class);
//                intent.putExtra(CssConstant.KEY_QR_HELP_TITLE, getString(R.string.qr_tickty_help));
//                intent.putExtra(CssConstant.KEY_QR_HELP_URL_NAME, CssConstant.KEY_QR_HELP_URL_VALUE);
//                startActivity(intent);
//            }
//        });
//
//        mTvQrCodeTicket = (TextView) findViewById(R.id.tv_qrcode_ticket);
//        mTvCommonTicket = (TextView) findViewById(R.id.tv_common_ticket);
//        mImgQrCodeTicketLine = (ImageView) findViewById(R.id.img_qrcode_ticket_line);
//        mImgCommonTicketLine = (ImageView) findViewById(R.id.img_common_ticket_line);
//
//        mTvQrCodeTicket.setOnClickListener(this);
//        mTvCommonTicket.setOnClickListener(this);
//        mImgCommonTicketLine.setVisibility(View.GONE);
//
//        if (!TextUtils.isEmpty(isSupportQrCodeTicketYn) && isSupportQrCodeTicketYn.equalsIgnoreCase("Y")) {
//            findViewById(R.id.lly_fragment_tab).setVisibility(View.VISIBLE);
//            if (isBuyAgain) {
//                //再次购买时根据serviceId切换tab
//                if (mServiceId.equals(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET)) {
//                    switchToQrCodeTicketFragment();
//                } else {
//                    switchToCommonTicketFragment();
//                }
//            } else
//                switchToQrCodeTicketFragment();
//        } else {
//            findViewById(R.id.lly_fragment_tab).setVisibility(View.GONE);
//            switchToCommonTicketFragment();
//        }
//    }
//
//    private void initStationData() {
//        mCommonSingleTicketFragment.setStationInfo(mStartStation, mEndStation);
//        //        mQrCodeSingleTicketFragment.setStationInfo(mStartStation, mEndStation);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_BuyStationDetailFragment));
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_BuyStationDetailFragment));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        switch (v.getId()) {
//            case R.id.tv_common_ticket:
//                StationCode startStation = mCommonSingleTicketFragment.getStartStation();
//                if (startStation != null) {
//                    if (!TextUtils.isEmpty(startStation.getUseYn())) {
//                        if (startStation.getUseYn().equalsIgnoreCase("Y")) {
//                            switchToCommonTicketFragment();
//                        } else {
//                            CommonToast.toast(getApplicationContext(), String.format(getString(R.string.no_get_ticket_device), startStation.getStationNameZH()));
//                        }
//                    } else {
//                        MLog.d(TAG, "station info is not full");
//                    }
//                } else {
//                    switchToCommonTicketFragment();
//                }
//                break;
//            case R.id.tv_qrcode_ticket:
//                switchToQrCodeTicketFragment();
//                break;
//        }
//    }
//
//    /**
//     * 切换至普通单程票
//     */
//    private void switchToCommonTicketFragment() {
//        mImgCommonTicketLine.setVisibility(View.VISIBLE);
//        mImgQrCodeTicketLine.setVisibility(View.GONE);
//        mTvCommonTicket.setTextColor(getResources().getColor(R.color.FF9605));
//        mTvQrCodeTicket.setTextColor(getResources().getColor(R.color._333333));
//        mCommonSingleTicketFragment.updateServiceType(CssConstant.SP_SERVICE.SERVICE_ID_TICKET);
//        titleBarView.setMenu(false);
//    }
//
//    /**
//     * 切换至二维码单程票
//     */
//    private void switchToQrCodeTicketFragment() {
//        mImgCommonTicketLine.setVisibility(View.GONE);
//        mImgQrCodeTicketLine.setVisibility(View.VISIBLE);
//        mTvCommonTicket.setTextColor(getResources().getColor(R.color._333333));
//        mTvQrCodeTicket.setTextColor(getResources().getColor(R.color.FF9605));
//        mCommonSingleTicketFragment.updateServiceType(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET);
//        titleBarView.setMenu(true);
//    }
//
//    private void getStationDetail(final int stationType, String cityCode, final String stationCode) {
//        mSTGateway.getStationDetail(cityCode, stationCode, new MobileGateway.MobileGatewayListener<GetStationDetailRs>() {
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
//                if (stationType == STATION_TYPE_START) {
//                    mCommonSingleTicketFragment.setStartStation(response.getStationCode());
//                } else if (stationType == STATION_TYPE_END) {
//                    mCommonSingleTicketFragment.setEndStation(response.getStationCode());
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
//
//        });
//    }
//
//}
