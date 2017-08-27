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
// * Created by liwx on 2017/4/11.
// * 通过固定票价购票
// */
//
//public class BuyTicketByPriceActivity extends BaseActivity implements View.OnClickListener {
//    private static final String TAG = "BuyTicketByPriceActivity";
//
//    private BuyTicketByPriceFragment mCommonTicketPriceFragment;//普通单程票
//    //    private BuyTicketByPriceFragment mQrCodeTicketPriceFragment;//二维码单程票
//    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
//    private TextView mTvQrCodeTicket;
//    private TextView mTvCommonTicket;
//    private ImageView mImgQrCodeTicketLine;
//    private ImageView mImgCommonTicketLine;
//    private TitleBarView titleBarView;
//
//    private String isSupportQrCodeTicketYn = "n";
//    private StationCode mStartStation;
//    private boolean isBuyAgain = false;//是否是再次购买
//    private String mServiceId;//再次购买逻辑时使用此字段
//    private String mCityCode;
//    private STGateway mSTGateway;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_buy_ticket_by_price);
//        mSTGateway = new STGateway(this);
//
//        isSupportQrCodeTicketYn = getIntent().getStringExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN);
//        MLog.d(TAG, "isSupportQrCodeTicketYn:" + isSupportQrCodeTicketYn);
//        mStartStation = (StationCode) getIntent().getSerializableExtra(SingleTicketManager.START_STATION);
//        isBuyAgain = getIntent().getBooleanExtra(CssConstant.KEY_IS_BUY_AGAIN, false);
//        mServiceId = getIntent().getStringExtra(CssConstant.KEY_SERVICE_ID);
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        if (isBuyAgain) {
//            getStationDetail(mCityCode, mStartStation.getStationCode());
//        }
//
//        if (isSupportQrCodeTicketYn.equals("Y")) {
//            mCommonTicketPriceFragment = BuyTicketByPriceFragment.getInstance(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, mCityCode);
//        } else {
//            mCommonTicketPriceFragment = BuyTicketByPriceFragment.getInstance(CssConstant.SP_SERVICE.SERVICE_ID_TICKET, mCityCode);
//
//        }
//        mFragmentList.add(mCommonTicketPriceFragment);
//        //        mFragmentList.add(mQrCodeTicketPriceFragment);
//        if (mStartStation != null) {
//            //再次购买时 将起点终点信息带过来
//            mCommonTicketPriceFragment.setStationInfo(mStartStation);
//            //            mQrCodeTicketPriceFragment.setStationInfo(mStartStation);
//        }
//
//        initView();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_STBookByPriceFragment));
//
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_STBookByPriceFragment));
//    }
//
//    public void initView() {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        //
//        //        if (!TextUtils.isEmpty(isSupportQrCodeTicketYn) && isSupportQrCodeTicketYn.equalsIgnoreCase("Y")) {
//        //            transaction.add(R.id.lly_buy_ticket_price, mQrCodeTicketPriceFragment);
//        //        }
//        transaction.add(R.id.lly_buy_ticket_price, mCommonTicketPriceFragment);
//        transaction.commitAllowingStateLoss();
//
//
//        titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        if (!TextUtils.isEmpty(isSupportQrCodeTicketYn) && isSupportQrCodeTicketYn.equalsIgnoreCase("Y")) {
//            titleBarView.setMenu(true);
//        } else {
//            titleBarView.setMenu(false);
//        }
//        titleBarView.setTitle(getString(R.string.st_buy_ticket_by_price));
//        titleBarView.setMenuName(getString(R.string.help));
//        titleBarView.setOnTitleBarClickListener(new TitleBarView.OnTitleBarClickListener() {
//            @Override
//            public void onBackClicked(View view) {
//                finish();
//            }
//
//            @Override
//            public void onMenuClicked(View view) {
//                Intent intent = new Intent(BuyTicketByPriceActivity.this, BrowserActivity.class);
//                intent.putExtra(CssConstant.KEY_QR_HELP_TITLE, getString(R.string.qr_tickty_help));
//                intent.putExtra(CssConstant.KEY_QR_HELP_URL_NAME, CssConstant.KEY_QR_HELP_URL_VALUE);
//                startActivity(intent);
//            }
//        });
//        mTvQrCodeTicket = (TextView) findViewById(R.id.tv_qrcode_ticket);
//        mTvCommonTicket = (TextView) findViewById(R.id.tv_common_ticket);
//        mImgQrCodeTicketLine = (ImageView) findViewById(R.id.img_qrcode_ticket_line);
//        mImgCommonTicketLine = (ImageView) findViewById(R.id.img_common_ticket_line);
//
//        mTvQrCodeTicket.setOnClickListener(this);
//        mTvCommonTicket.setOnClickListener(this);
//        mImgCommonTicketLine.setVisibility(View.GONE);
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
//        setIsBuyAgain();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        switch (v.getId()) {
//            case R.id.tv_common_ticket:
//                if (isBuyAgain) {
//                    if (isSupportQrCodeTicketYn.equalsIgnoreCase("Y")) {
//                        CommonToast.toast(getApplicationContext(), getString(R.string.buy_again_can_not_switch_type));
//                        return;
//                    }
//                }
//                StationCode startStation = mCommonTicketPriceFragment.getStartStation();
//                if (startStation != null) {
//                    if (startStation.getUseYn().equalsIgnoreCase("Y")) {
//                        switchToCommonTicketFragment();
//                    } else {
//                        CommonToast.toast(getApplicationContext(), String.format(getString(R.string.no_get_ticket_device), startStation.getStationNameZH()));
//                    }
//                } else {
//                    switchToCommonTicketFragment();
//                }
//                break;
//            case R.id.tv_qrcode_ticket:
//                if (isBuyAgain) {
//                    if (isSupportQrCodeTicketYn.equalsIgnoreCase("y")) {
//                        CommonToast.toast(getApplicationContext(), getString(R.string.buy_again_can_not_switch_type));
//                        return;
//                    }
//                }
//                switchToQrCodeTicketFragment();
//                break;
//
//        }
//    }
//
//    /**
//     * 切换至普通单程票
//     */
//
//    private void switchToCommonTicketFragment() {
//        mImgCommonTicketLine.setVisibility(View.VISIBLE);
//        mImgQrCodeTicketLine.setVisibility(View.GONE);
//        mTvCommonTicket.setTextColor(getResources().getColor(R.color.FF9605));
//        mTvQrCodeTicket.setTextColor(getResources().getColor(R.color._333333));
//        //        switchFragment(mCommonTicketPriceFragment);
//        mCommonTicketPriceFragment.updateServiceType(CssConstant.SP_SERVICE.SERVICE_ID_TICKET);
//        titleBarView.setMenu(false);
//    }
//
//    /**
//     * 切换至电子单程票
//     */
//    private void switchToQrCodeTicketFragment() {
//        mImgCommonTicketLine.setVisibility(View.GONE);
//        mImgQrCodeTicketLine.setVisibility(View.VISIBLE);
//        mTvCommonTicket.setTextColor(getResources().getColor(R.color._333333));
//        mTvQrCodeTicket.setTextColor(getResources().getColor(R.color.FF9605));
//        mCommonTicketPriceFragment.updateServiceType(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET);
//        //                switchFragment(mQrCodeTicketPriceFragment);
//        titleBarView.setMenu(true);
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
//    private void setIsBuyAgain() {
//        if (mCommonTicketPriceFragment != null) {
//            mCommonTicketPriceFragment.setIsBuyAgain(isBuyAgain);
//        }
//        //        if (mQrCodeTicketPriceFragment != null) {
//        //            mQrCodeTicketPriceFragment.setIsBuyAgain(isBuyAgain);
//        //        }
//    }
//
//    private void getStationDetail(String cityCode, final String stationCode) {
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
//                mCommonTicketPriceFragment.setStationInfo(response.getStationCode());
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
