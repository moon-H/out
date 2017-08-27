//package com.cssweb.shankephone.home.ticket;
//
//import android.animation.ObjectAnimator;
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.CountDownTimer;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.DeviceInfo;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseFragment;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.app.IEvent;
//import com.cssweb.shankephone.db.CommonDBManager;
//import com.cssweb.shankephone.db.SingleTicketDBManager;
//import com.cssweb.shankephone.gateway.EventGateway;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.Affiche;
//import com.cssweb.shankephone.gateway.model.GetAfficheListRs;
//import com.cssweb.shankephone.gateway.model.ProductCategory;
//import com.cssweb.shankephone.gateway.model.singleticket.CityCode;
//import com.cssweb.shankephone.gateway.model.singleticket.GetMetroMapListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.LineCode;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMap;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMapCheck;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.spservice.GetSupportServiceYnRs;
//import com.cssweb.shankephone.home.OnFragmentCallBack;
//import com.cssweb.shankephone.journey.JourneyActivity;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.order.presenter.OrderListContract;
//import com.cssweb.shankephone.order.presenter.OrderListPresenter;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.settings.CustomerServiceActivity;
//import com.cssweb.shankephone.tileview.StationClickListener;
//import com.cssweb.shankephone.tileview.TileView;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.ViewManager;
//import com.cssweb.shankephone.view.metro.BitmapProviderDisk;
//import com.cssweb.shankephone.view.metro.MapPopupView;
//import com.skyfishjy.library.RippleBackground;
//
//import org.apache.http.Header;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.TimerTask;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by liwx on 2015/10/27
// * 通过路网图选择站点页面
// */
//public class STMapFragment extends BaseFragment implements View.OnClickListener, OrderListContract.JourneyView {
//    private static final String TAG = "STMapFragment";
//
//    private static final int CHECK_START_STATION = 1;
//    private static final int CHECK_END_STATION = 2;
//
//    private static final int BUY_TICKET_BY_PRICE = 1;
//    private static final int BUY_TICKET_BY_STATION = 2;
//    private static final int SELECT_START_STATION = 3;
//    private static final int MOVE_TO_STATION_DETAIL = 4;
//
//    private static STMapFragment mFragment;
//    private STHomeActivity mActivity;
//    private TextView mTvStartStation;
//    private TextView mTvEndStation;
//    //    private MapLayout mMapLayout;
//    private TileView mTileView;
//    private View selectStationView;
//    private View mPriceView;
//    private RippleBackground mLocationRipple;
//
//    private StationCode mStartStation;
//    private StationCode mEndStation;
//
//    private boolean isSupportSingleTicket = false;
//    boolean hasReadAffiche = false;
//    private boolean readyAddMarker = false;
//    private boolean readyGetMapInfo = false;
//
//    private MapManager mMapManager;
//    private STGateway mSTGateway;
//    private SingleTicketDBManager mSingleTicketDBManager;
//
//    private View mRootView;
//    private MapPopupView mapPopupView;
//    private ImageView mStartMarkView;
//    private ImageView mEndMarkView;
//    private View mAfficheParentView;
//    private TextView mAfficheView;
//    private ImageView mDeleteAfficheView;
//
//    private OnFragmentCallBack mCallBack;
//    private ExecutorService mExecutor = Executors.newCachedThreadPool();
//    private List<ImageView> mMarkerList = new ArrayList<>();
//    private CommonDBManager mCommonDBManager;
//    private Handler mHandler;
//
//    private MetroMap mMetroMap;
//    private AddMarkerTask addMarkerTask;
//    private CountDownTimer mHideAfficheTimer;
//
//    private OrderListContract.Presenter mPresenter;
//
//    private boolean isSupportQrCodeTicket = false;
//
//    public static STMapFragment getInstance() {
//        mFragment = new STMapFragment();
//        return mFragment;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        mActivity = (STHomeActivity) getActivity();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mMapManager = BizApplication.getInstance().getMapManager();
//        mSTGateway = new STGateway(mActivity);
//        mSingleTicketDBManager = new SingleTicketDBManager(getActivity());
//        mCommonDBManager = new CommonDBManager(BizApplication.getInstance());
//        mHandler = new Handler();
//        addMarkerTask = new AddMarkerTask();
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CssConstant.Action.ACTION_ADD_MAP_MARKER);
//        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(receiver, filter);
//
//        CityCode currentCity = getCityCode();
//        ProductCategory productCategory = new ProductCategory();
//        productCategory.setCategoryCode(CssConstant.ORDER.CATEGORYCODE_ALL);
//        OrderListPresenter presenter = new OrderListPresenter(mActivity, this, CssConstant.ORDER.COM_STATUS_UN_FINISH);
//        presenter.onSetCity(currentCity);
//        presenter.onSetProduct(productCategory);
//    }
//
//    @NonNull
//    private CityCode getCityCode() {
//        CityCode currentCity = new CityCode();
//        currentCity.setCityCode(MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE));
//        currentCity.setCityName(MPreference.getString(mActivity, MPreference.CHOICE_CITY_NAME));
//        return currentCity;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        MLog.d(TAG, "%% onCreateView");
//        if (mRootView == null) {
//            MLog.d(TAG, "%% onCreateView 2 ");
//            mRootView = inflater.inflate(R.layout.fragment_map, container, false);
//            mTvStartStation = (TextView) mRootView.findViewById(R.id.tv_start_station);
//            mTvEndStation = (TextView) mRootView.findViewById(R.id.tv_end_station);
//            mTvStartStation.setOnClickListener(this);
//            mTvEndStation.setOnClickListener(this);
//            mPriceView = mRootView.findViewById(R.id.price);
//            mAfficheParentView = mRootView.findViewById(R.id.lly_affiche);
//            mAfficheView = (TextView) mRootView.findViewById(R.id.tv_affiche);
//            mDeleteAfficheView = (ImageView) mRootView.findViewById(R.id.img_delete);
//            mDeleteAfficheView.setOnClickListener(this);
//            mPriceView.setOnClickListener(this);
//            mRootView.findViewById(R.id.img_customer_service).setOnClickListener(this);
//            mRootView.findViewById(R.id.img_journey).setOnClickListener(this);
//            mTileView = (TileView) mRootView.findViewById(R.id.metro_view);
//
//            View rippleParent = inflater.inflate(R.layout.map_marker_ripple, null, false);
//            mLocationRipple = (RippleBackground) rippleParent.findViewById(R.id.ripple_content);
//            selectStationView = mRootView.findViewById(R.id.lly_select_station);
//            if (isSupportSingleTicket) {
//                selectStationView.setVisibility(View.VISIBLE);
//                mPriceView.setVisibility(View.VISIBLE);
//            } else {
//                selectStationView.setVisibility(View.GONE);
//                mPriceView.setVisibility(View.GONE);
//            }
//            mapPopupView = new MapPopupView(mActivity);
//            mapPopupView.setListener(onMapPopupClickedListener);
//            mStartMarkView = new ImageView(mActivity);
//            mEndMarkView = new ImageView(mActivity);
//            updateMap();
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    cacheMapInfo();
//                    getAfficheList();
//                }
//
//            }, 500);
//            mHideAfficheTimer = new CountDownTimer(30 * 1000, 1000) {
//                @Override
//                public void onTick(long millisUntilFinished) {
//
//                }
//
//                @Override
//                public void onFinish() {
//                    mAfficheParentView.setVisibility(View.GONE);
//                }
//            };
//        }
//        getSupportServiceYn(-1, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE), false, null);
//        return mRootView;
//    }
//
//    private void updateMap() {
//        int width = BizApplication.getInstance().getMapManager().getMapWidth();
//        int height = BizApplication.getInstance().getMapManager().getMapHeight();
//        MLog.d(TAG, "### width = " + width + " ## " + height);
//        mTileView.setSize(width, height);
//        mTileView.setBitmapProvider(new BitmapProviderDisk());
//        mTileView.setBackgroundColor(getResources().getColor(R.color.EEEEEE));
//        //        mTileView.addDetailLevel(1.000f, "4/%d_%d.png");
//        mTileView.addDetailLevel(1.000f, "4/%d_%d.png");
//        mTileView.addDetailLevel(0.500f, "3/%d_%d.png");
//        //        mTileView.addDetailLevel(0.250f, "2/%d_%d.png");
//        mTileView.setMarkerAnchorPoints(-0.5f, -1.0f);
//        mTileView.setScale(0.25f);
//        mTileView.setScaleLimits(0, 1.3f);
//        mTileView.setViewportPadding(256);
//        mTileView.setShouldRenderWhilePanning(true);
//
//        mTileView.setOnStationClickListener(new StationClickListener() {
//            @Override
//            public void onStationClicked(int x, int y) {
//                MLog.d(TAG, "x = " + x + " y = " + y);
//                if (!isSupportSingleTicket) {
//                    MLog.d(TAG, "isSupportSingleTicket = " + isSupportSingleTicket);
//                    return;
//                }
//                //                StationCode stationCode = mMapManager.getClickedStation(x, y);
//                //                MLog.d(TAG, "getDotX = " + stationCode.getDotX() + " getDotY = " + stationCode.getDotY());
//                List<StationCode> list = mMapManager.getClickedStation(x, y);
//                if (list != null && list.size() > 0) {
//                    if (mTileView != null) {
//                        MLog.d(TAG, "stationName = " + list.get(0).getStationNameZH());
//                        //                        if (mTileView.getScale() < 1.0)
//                        //                            mTileView.setScale(1);
//                        if (mTileView.isMarkerAdded(mapPopupView))
//                            mTileView.removeMarker(mapPopupView);
//                        StationCode tempStationCode = list.get(0);
//                        //                        mTileView.addMarker(mapPopupView, tempStationCode.getDotX(), tempStationCode.getDotY(), null, null);
//                        mTileView.addCallout(mapPopupView, tempStationCode.getDotX(), tempStationCode.getDotY(), -0.5f, -1.0f);
//                        mapPopupView.setSupportServiceYn(isSupportQrCodeTicket);
//                        mapPopupView.setStationList(list);
//                        mapPopupView.transitionIn();
//                        mTileView.slideToAndCenterWithScale(tempStationCode.getDotX(), tempStationCode.getDotY(), 1.0f);
//                    }
//                } else {
//                    hidMapPopup();
//                }
//            }
//
//        });
//        //        addMarker();
//        frameTo(width / 2, 0);
//    }
//
//    private void addMarker() {
//        for (ImageView marker : mMarkerList) {
//            mTileView.removeMarker(marker);
//        }
//        if (mMetroMap != null) {
//            if (mMetroMap.getFullOpeningYN() != null && mMetroMap.getFullOpeningYN().equalsIgnoreCase("Y")) {
//                MLog.d(TAG, "all station is supported");
//                return;
//            }
//        }
//        if (mMetroMap != null) {
//            if (mMetroMap.getSingleTicketServiceYn() != null && mMetroMap.getSingleTicketServiceYn().equalsIgnoreCase("N")) {
//                MLog.d(TAG, "city not support singleTicket");
//                return;
//            }
//        }
//        List<StationCode> list = SingleTicketDBManager.getAllStationCode();
//        mMarkerList.clear();
//        MLog.d(TAG, "station list sile " + list.size());
//        for (final StationCode stationCode : list) {
//            if (stationCode.getUseYn().equalsIgnoreCase("Y")) {
//                final ImageView marker = new ImageView(getActivity());
//                mMarkerList.add(marker);
//                marker.setImageResource(R.drawable.skf_icon1);
//                if (mTileView != null) {
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            mTileView.addMarker(marker, stationCode.getDotX(), stationCode.getDotY(), null, null);
//                        }
//                    }, 150);
//                    //                    mTileView.getMarkerLayout().setMarkerTapListener(markerTapListener);
//                }
//            }
//        }
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
//        if (mTileView != null) {
//            mTileView.resume();
//        }
//        UMengShare.countPageStart(getString(R.string.statistic_STBookByStationFragment));
//        //加载路网图页面后发起定位
//        LocalBroadcastManager.getInstance(mActivity).sendBroadcast(new Intent(CssConstant.Action.ACTION_START_LOCATION));
//        getJourneyCount();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageEnd(getString(R.string.statistic_STBookByStationFragment));
//        hidMapPopup();
//    }
//
//    @Override
//    public void onDestroyView() {
//        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
//        super.onDestroyView();
//        MLog.d(TAG, "%% onDestroyView");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //        if (mTileView != null)
//        //            mTileView.recycleBmp();
//        MLog.d(TAG, "onDestroy");
//        if (mTileView != null)
//            mTileView.pause();
//        if (mTileView != null)
//            mTileView.destroy();
//        mTileView = null;
//        if (mMapManager != null)
//            mMapManager.clearMemoryCache();
//        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(receiver);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_start_station:
//                registEventBus();
//                getSupportServiceYn(SELECT_START_STATION, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE), true, null);
//                break;
//            case R.id.tv_end_station:
//                registEventBus();
//                Intent endIntent = new Intent(mActivity, STSelectStaionActivity.class);
//                endIntent.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_END_STATION_REQUEST);
//                endIntent.putExtra(CssConstant.KEY_CITY_CODE, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE));
//                startActivityForResult(endIntent, SingleTicketManager.SELECT_END_STATION_REQUEST);
//                break;
//            case R.id.price:
//                getSupportServiceYn(BUY_TICKET_BY_PRICE, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE), true, null);
//
//                //-------------------todo 用于路径规划用 请勿删除 start----------
//                //                if (!testFlag) {
//                //                    testFlag = true;
//                //                    mTileView.setAlphaMask(true, 50);
//                //                } else {
//                //                    testFlag = false;
//                //                    mTileView.setAlphaMask(false, 0);
//                //
//                //                }
//                //                mTileView.getTileCanvasViewGroup().clear();
//                //                mTileView.getDetailLevelManager().resetDetailLevels();
//                //                updateMap();
//                //-------------------用于路径规划用 end----------
//                break;
//            case R.id.img_delete:
//                //删除通知消息
//                Affiche affiche = (Affiche) v.getTag();
//                if (affiche != null) {
//                    MLog.d(TAG, "affiche = " + affiche.toString());
//                    //                    CommonDBManager dbManager = new CommonDBManager(BizApplication.getInstance());
//                    affiche.save();
//                }
//                mAfficheParentView.setVisibility(View.GONE);
//                mHideAfficheTimer.cancel();
//                break;
//            case R.id.img_customer_service:
//                //客服中心
//                startActivity(new Intent(mActivity, CustomerServiceActivity.class));
//                break;
//            case R.id.img_journey:
//                //行程
//                if (BizApplication.getInstance().isLoginClient())
//                    if (LoginManager.hasPhoneNumber(mActivity)) {
//                        startActivity(new Intent(mActivity, JourneyActivity.class));
//                    } else
//                        showBindPhoneNumberDialog(mActivity);
//                else
//                    startActivity(new Intent(mActivity, LoginActivity.class));
//
//                break;
//
//
//        }
//    }
//
//    private void registEventBus() {
//        if (!EventBus.getDefault().isRegistered(STMapFragment.this))
//            EventBus.getDefault().register(STMapFragment.this);
//    }
//
//    public void clearAllMarkView() {
//        MLog.d(TAG, "clear all marker size = " + mMarkerList.size());
//        for (ImageView marker : mMarkerList) {
//            mTileView.removeMarker(marker);
//        }
//        //        mMarkerList.clear();
//    }
//
//    public void updateView(MetroMap metroMap) {
//        MLog.d(TAG, "updateView");
//        if (mAfficheParentView != null)
//            mAfficheParentView.setVisibility(View.GONE);
//        if (metroMap != null) {
//            MLog.d(TAG, "updateView mapInfo = " + metroMap.toString());
//        }
//        mMetroMap = metroMap;
//        mTileView.getTileCanvasViewGroup().clear();
//        mTileView.getDetailLevelManager().resetDetailLevels();
//        removeStartEndMarkerView();
//        updateMap();
//        getAfficheList();
//        CityCode currentCity = getCityCode();
//        if (mPresenter != null) {
//            mPresenter.onSetCity(currentCity);
//        }
//        if (metroMap != null) {
//            setSupportSingleTicketYn(metroMap.getSingleTicketServiceYn());
//        }
//        readyGetMapInfo = true;
//        //        startAddMarkerTimer();
//        addMarker();
//        clearLocationMarker();
//    }
//
//    /**
//     * 清楚定位图标
//     */
//    private void clearLocationMarker() {
//        MLog.d(TAG, "clearLocationMarker");
//        if (mTileView.isMarkerAdded(mLocationRipple)) {
//            mLocationRipple.stopRippleAnimation();
//            mTileView.removeMarker(mLocationRipple);
//        }
//    }
//
//    //    public void updateMetroView() {
//    //        //        if (mTileView != null)
//    //        //            mTileView.resetBitmap();
//    //    }
//
//    /**
//     * 显示最近的地铁站
//     */
//    public void updateLocationInfo(StationCode stationCode) {
//        if (mTileView != null) {
//            if (mTileView.isMarkerAdded(mLocationRipple))
//                mTileView.removeMarker(mLocationRipple);
//            mTileView.addMarker(mLocationRipple, stationCode.getDotX(), stationCode.getDotY() + 10, null, null);
//            mLocationRipple.startRippleAnimation();
//        } else {
//            MLog.d(TAG, "updateLocationInfo tileView is null");
//        }
//    }
//
//    public void clearStationInfo() {
//        mStartStation = null;
//        mEndStation = null;
//        if (mTvStartStation != null) {
//            mTvStartStation.setText(getString(R.string.st_start_station2));
//            mTvStartStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//        }
//        if (mTvEndStation != null) {
//            mTvEndStation.setText(getString(R.string.st_end_station));
//            mTvEndStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//
//        }
//        if (mTileView != null) {
//            //            mTileView.removeMarker(mapPopupView);
//            hidMapPopup();
//            mTileView.removeMarker(mStartMarkView);
//            mTileView.removeMarker(mEndMarkView);
//        }
//    }
//
//    private void hidMapPopup() {
//        if (mTileView != null)
//            //            mTileView.removeMarker(mapPopupView);
//            mTileView.removeCallout(mapPopupView);
//
//        //        if (mapPopupView != null)
//        //            mapPopupView.setVisibility(OrderView.GONE);
//    }
//
//    private MapPopupView.OnMapPopupClickedListener onMapPopupClickedListener = new MapPopupView.OnMapPopupClickedListener() {
//        @Override
//        public void onStartStationClicked(StationCode stationCode) {
//            //            MLog.d(TAG," onStartStationClicked = "+stationCode.getStationNameZH());
//            if (stationCode != null) {
//                if (!isSupportQrCodeTicket) {
//                    if (!stationCode.getUseYn().equalsIgnoreCase("Y")) {
//                        CommonToast.toast(mActivity, getString(R.string.st_station_can_not_set_start_station));
//                        return;
//                    }
//                }
//
//                if (mEndStation != null) {
//                    if (isSameStation(mEndStation, stationCode)) {
//                        removeStartEndMarkerView();
//                        clearEndStation();
//                        setStartStation(stationCode);
//                    } else {
//                        setStartStation(stationCode);
//                        hidMapPopup();
//                    }
//                } else {
//                    setStartStation(stationCode);
//                    hidMapPopup();
//                }
//            }
//        }
//
//        @Override
//        public void onEndStationClicked(StationCode stationCode) {
//            if (stationCode != null) {
//                if (mStartStation != null) {
//                    if (isSameStation(mStartStation, stationCode)) {
//                        removeStartEndMarkerView();
//                        //                        Toast.makeText(mActivity, getString(R.string.st_slelect_different_station), Toast.LENGTH_SHORT).show();
//                        clearStartStation();
//                        setEndStation(stationCode);
//                    } else {
//                        setEndStation(stationCode);
//                        hidMapPopup();
//                    }
//                } else {
//                    setEndStation(stationCode);
//                    hidMapPopup();
//                }
//            }
//        }
//
//        @Override
//        public void onStationDetailClicked(StationCode stationCode) {
//            registEventBus();
//            getSupportServiceYn(MOVE_TO_STATION_DETAIL, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE), true, stationCode);
//        }
//    };
//
//    private void removeStartEndMarkerView() {
//        if (mTileView.isMarkerAdded(mStartMarkView)) {
//            mTileView.removeMarker(mStartMarkView);
//        }
//        if (mTileView.isMarkerAdded(mEndMarkView)) {
//            mTileView.removeMarker(mEndMarkView);
//        }
//    }
//
//    //    @Override
//    //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    //        super.onActivityResult(requestCode, resultCode, data);
//    //        if (resultCode == SingleTicketManager.SELECT_STATION_RESULT_OK) {
//    //            StationCode stationCode = (StationCode) data.getSerializableExtra(SingleTicketManager.STATION);
//    //            if (stationCode == null)
//    //                return;
//    //            if (requestCode == SingleTicketManager.SELECT_START_STATION_REQUEST) {
//    //                setStartStation(stationCode);
//    //            } else if (requestCode == SingleTicketManager.SELECT_END_STATION_REQUEST) {
//    //                setEndStation(stationCode);
//    //            }
//    //        }
//    //    }
//
//
//    public void removeMarker() {
//        if (mTileView != null) {
//            hidMapPopup();
//            mTileView.removeMarker(mStartMarkView);
//            mTileView.removeMarker(mEndMarkView);
//        }
//    }
//
//    private void setStartStation(StationCode stationCode) {
//        if (mEndStation != null) {
//            if (mEndStation.getToAirportYn().equalsIgnoreCase("Y")) {
//                LineCode lineCode = SingleTicketDBManager.getLineInfoByLineCode(mEndStation.getLineCode());
//                if (!stationCode.getToAirportYn().equalsIgnoreCase("Y")) {
//                    if (lineCode != null)
//                        CommonToast.toast(mActivity, String.format(getString(R.string.start_station_must_belong_to_this_line), lineCode.getLineNameZH()));
//                    return;
//                }
//            } else if (stationCode.getToAirportYn().equalsIgnoreCase("Y")) {
//                LineCode lineCode = SingleTicketDBManager.getLineInfoByLineCode(stationCode.getLineCode());
//                if (lineCode != null)
//                    CommonToast.toast(mActivity, String.format(getString(R.string.start_station_must_not_belong_to_this_line), lineCode.getLineNameZH()));
//                return;
//            }
//        }
//
//        //        //广州apm线属于云闸机业务，点击后直接进入云闸机相关页面
//        //        if (stationCode.getLineCode().equals(SingleTicketManager.LINE_CODE_APM) && BizApplication.getInstance().getCityCode().equals(BizApplication.CITY_CODE_GUANGZHOU)) {
//        //            Intent intent = new Intent();
//        //            intent.putExtra(SingleTicketManager.STATION, stationCode);
//        //            intent.setClass(mActivity, STCloudGateActivity.class);
//        //            intent.putExtra(CssConstant.KEY_CITY_CODE, BizApplication.getInstance().getCityCode());
//        //            startActivity(intent);
//        //            return;
//        //        }
//
//        mStartStation = stationCode;
//        checkStaionAvailable(CHECK_START_STATION);
//        if (mStartStation != null) {
//            if (mTileView != null) {
//                if (mTileView.isMarkerAdded(mStartMarkView)) {
//                    mTileView.moveMarker(mStartMarkView, stationCode.getDotX(), stationCode.getDotY());
//                } else {
//                    mStartMarkView.setBackgroundResource(R.drawable.ic_st_start);
//                    mTileView.addMarker(mStartMarkView, stationCode.getDotX(), stationCode.getDotY(), null, null);
//                }
//                hidMapPopup();
//            }
//            mTvStartStation.setText(stationCode.getStationNameZH());
//            mTvStartStation.setTextColor(getResources().getColor(R.color.st_order_price_text));
//            moveToBookByStationDetailPage();
//        }
//    }
//
//    private void setEndStation(StationCode stationCode) {
//        if (mStartStation != null) {
//            if (mStartStation.getToAirportYn().equalsIgnoreCase("Y")) {
//                if (!stationCode.getToAirportYn().equalsIgnoreCase("Y")) {
//                    LineCode lineCode = SingleTicketDBManager.getLineInfoByLineCode(mStartStation.getLineCode());
//                    if (lineCode != null)
//                        CommonToast.toast(mActivity, String.format(getString(R.string.end_station_must_belong_to_this_line), lineCode.getLineNameZH()));
//                    return;
//                }
//            } else if (stationCode.getToAirportYn().equalsIgnoreCase("Y")) {
//                LineCode lineCode = SingleTicketDBManager.getLineInfoByLineCode(stationCode.getLineCode());
//                if (lineCode != null)
//                    CommonToast.toast(mActivity, String.format(getString(R.string.end_station_must_not_belong_to_this_line), lineCode.getLineNameZH()));
//                return;
//            }
//        }
//        //        //广州apm线属于云闸机业务，点击后直接进入云闸机相关页面
//        //        if (stationCode.getLineCode().equals(SingleTicketManager.LINE_CODE_APM) && BizApplication.getInstance().getCityCode().equals(BizApplication.CITY_CODE_GUANGZHOU)) {
//        //            Intent intent = new Intent();
//        //            intent.putExtra(SingleTicketManager.STATION, stationCode);
//        //            intent.setClass(mActivity, STCloudGateActivity.class);
//        //            intent.putExtra(CssConstant.KEY_CITY_CODE, BizApplication.getInstance().getCityCode());
//        //            startActivity(intent);
//        //            return;
//        //        }
//
//        mEndStation = stationCode;
//        checkStaionAvailable(CHECK_END_STATION);
//        if (mEndStation != null) {
//            if (mTileView != null) {
//                if (mTileView.isMarkerAdded(mEndMarkView)) {
//                    mTileView.moveMarker(mEndMarkView, stationCode.getDotX(), stationCode.getDotY());
//                } else {
//                    mEndMarkView.setBackgroundResource(R.drawable.ic_st_end);
//                    mTileView.addMarker(mEndMarkView, stationCode.getDotX(), stationCode.getDotY(), null, null);
//                }
//                hidMapPopup();
//            }
//            mTvEndStation.setText(stationCode.getStationNameZH());
//            mTvEndStation.setTextColor(getResources().getColor(R.color.st_order_price_text));
//            moveToBookByStationDetailPage();
//        }
//    }
//
//    private void checkStaionAvailable(int flag) {
//        switch (flag) {
//            case CHECK_START_STATION:
//                if (mEndStation != null && mStartStation != null) {
//                    if (isSameStation(mEndStation, mStartStation)) {
//                        mStartStation = null;
//                        Toast.makeText(mActivity, getString(R.string.st_slelect_different_station), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//            case CHECK_END_STATION:
//                if (mEndStation != null && mStartStation != null) {
//                    if (isSameStation(mStartStation, mEndStation)) {
//                        mEndStation = null;
//                        Toast.makeText(mActivity, getString(R.string.st_slelect_different_station), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                break;
//        }
//    }
//
//    private void clearStartStation() {
//        mStartStation = null;
//        mTvStartStation.setText(getString(R.string.st_start_station2));
//        mTvStartStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//    }
//
//    private void clearEndStation() {
//        mEndStation = null;
//        mTvEndStation.setText(getString(R.string.st_end_station));
//        mTvEndStation.setTextColor(getResources().getColor(R.color.select_station_hint));
//    }
//
//    private boolean isSameStation(StationCode stationCode1, StationCode stationCode2) {
//        if (stationCode1 != null && stationCode2 != null) {
//            if (stationCode1.getStationNameZH().trim().equals(stationCode2.getStationNameZH().trim())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 跳转至购票信息页面
//     */
//    private void moveToBookByStationDetailPage() {
//        if (mStartStation != null && mEndStation != null) {
//            getSupportServiceYn(BUY_TICKET_BY_STATION, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE), true, null);
//        }
//    }
//
//    /**
//     * 设置是否支持普通单程票，检查是否支持二维码单程票
//     */
//    public void setSupportSingleTicketYn(String supportYn) {
//        isSupportSingleTicket = !TextUtils.isEmpty(supportYn) && supportYn.equalsIgnoreCase("Y");
//        MLog.d(TAG, " isSupportSingleTicket = " + isSupportSingleTicket);
//        if (selectStationView != null && mPriceView != null) {
//            selectStationView.setVisibility(isSupportSingleTicket ? View.VISIBLE : View.GONE);
//            mPriceView.setVisibility(isSupportSingleTicket ? View.VISIBLE : View.GONE);
//            getSupportServiceYn(-1, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE), false, null);
//        }
//    }
//
//    public void frameTo(final double x, final double y) {
//        if (mTileView != null) {
//            mTileView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mTileView.slideToAndCenter(x, y);
//                }
//            });
//        }
//    }
//
//    public void addStartMarkerView(double dotX, double dotY) {
//        if (mTileView != null) {
//            if (mTileView.isMarkerAdded(mStartMarkView)) {
//                mTileView.moveMarker(mStartMarkView, dotX, dotY);
//            } else {
//                mStartMarkView.setBackgroundResource(R.drawable.ic_st_start);
//                mTileView.addMarker(mStartMarkView, dotX, dotY, null, null);
//            }
//            hidMapPopup();
//        }
//    }
//
//    public void addEndMarkerView(double dotX, double dotY) {
//        if (mTileView != null) {
//            if (mTileView.isMarkerAdded(mEndMarkView)) {
//                mTileView.moveMarker(mEndMarkView, dotX, dotY);
//            } else {
//                mEndMarkView.setBackgroundResource(R.drawable.ic_st_end);
//                mTileView.addMarker(mEndMarkView, dotX, dotY, null, null);
//            }
//            hidMapPopup();
//        }
//    }
//
//    public void setFragmentCallback(OnFragmentCallBack callback) {
//        this.mCallBack = callback;
//    }
//
//    /**
//     * 缓存路网图信息,宽、高、是否支持单程票等
//     **/
//    public void cacheMapInfo() {
//        mSTGateway.getMetroMapList(new ArrayList<MetroMapCheck>(), new MobileGateway.MobileGatewayListener<GetMetroMapListRs>() {
//                @Override
//                public void onFailed(Result result) {
//                    //                    CommonToast.onFailed(getActivity(), result);
//                    //                launchHomePage();
//                }
//
//                @Override
//                public void onHttpFailed(int statusCode, Header[] headers) {
//                    //                    CommonToast.onHttpFailed(getActivity());
//                    //                launchHomePage();
//                }
//
//                @Override
//                public void onSuccess(GetMetroMapListRs response) {
//                    handleGetMeroMapRes(response);
//                }
//
//                @Override
//                public void onNoNetwork() {
//                    //                    CommonToast.onNoNetwork(getActivity());
//                    //                launchHomePage();
//                }
//
//                @Override
//                public void onAutoLoginSuccess() {
//                }
//
//                @Override
//                public void onAutoLoginFailed(Result result) {
//                }
//
//            }
//
//        );
//    }
//
//    private void handleGetMeroMapRes(GetMetroMapListRs response) {
//        final List<MetroMap> list = response.getMetroMapList();
//        if (list == null)
//            return;
//        mExecutor.execute(new Runnable() {
//                              @Override
//                              public void run() {
//                                  mSingleTicketDBManager.removeAllData(MetroMap.class);
//                                  mSingleTicketDBManager.saveAllData(list);
//                                  final String cacheCityCode = MPreference.getString(getActivity(), MPreference.CHOICE_CITY_CODE);
//                                  getActivity().runOnUiThread(new Runnable() {
//                                                                  @Override
//                                                                  public void run() {
//                                                                      MetroMap metroMap = null;
//                                                                      if (!TextUtils.isEmpty(cacheCityCode)) {
//                                                                          metroMap = mSingleTicketDBManager.getMetroMap(cacheCityCode);
//                                                                      } else {
//                                                                          metroMap = mSingleTicketDBManager.getMetroMap(BizApplication.CITY_CODE_GUANGZHOU);
//                                                                      }
//                                                                      mMetroMap = metroMap;
//                                                                      if (metroMap != null)
//                                                                          setSupportSingleTicketYn(metroMap.getSingleTicketServiceYn());
//                                                                      //
//                                                                      readyGetMapInfo = true;
//                                                                      addMarker();
//                                                                  }
//
//                                                              }
//
//                                  );
//                              }
//                          }
//
//        );
//    }
//
//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            MLog.d(TAG, "action  = " + action);
//            if (TextUtils.isEmpty(action)) {
//                return;
//            }
//            if (action.equals(CssConstant.Action.ACTION_ADD_MAP_MARKER)) {
//                //                clearAllMarkView();
//                readyAddMarker = true;
//            }
//        }
//    };
//
//    /**
//     * 获取底部公告内容
//     */
//    private void getAfficheList() {
//        EventGateway eventGateway = new EventGateway(getActivity());
//        eventGateway.getAfficheList(MPreference.getString(BizApplication.getInstance(), MPreference.CHOICE_CITY_CODE), new MobileGateway.MobileGatewayListener<GetAfficheListRs>() {
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
//            public void onSuccess(GetAfficheListRs response) {
//                if (response != null) {
//                    List<Affiche> list = response.getAfficheList();
//                    if (list != null && list.size() > 0) {
//                        final Affiche onlineAfiche = list.get(0);
//                        mExecutor.execute(new Runnable() {
//                            @Override
//                            public void run() {
//                                hasReadAffiche = false;
//                                List<Affiche> localList = mCommonDBManager.getLocalAfficheList();
//                                for (Affiche affiche : localList) {
//                                    if (affiche.getAfficheId() == onlineAfiche.getAfficheId()) {
//                                        hasReadAffiche = true;
//                                        break;
//                                    }
//                                }
//                                mHandler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (hasReadAffiche) {
//                                            mAfficheParentView.setVisibility(View.GONE);
//                                        } else {
//                                            mAfficheParentView.setVisibility(View.GONE);
//                                            mAfficheView.setText(onlineAfiche.getTitle());
//                                            mDeleteAfficheView.setTag(onlineAfiche);
//                                            mAfficheParentView.post(new Runnable() {
//                                                @Override
//                                                public void run() {
//                                                    float curTranslationX = mAfficheParentView.getTranslationX();
//                                                    float startX = DeviceInfo.getScreenWidth(BizApplication.getInstance());
//                                                    float dtX = startX - mAfficheParentView.getWidth();
//                                                    MLog.d(TAG, "OrderView width = " + mAfficheParentView.getWidth());
//                                                    MLog.d(TAG, "startX =" + startX + "curTranslationX = " + curTranslationX + "  dtx = " + dtX);
//                                                    //从A点到B点再到C点
//                                                    ObjectAnimator animator = ObjectAnimator.ofFloat(mAfficheParentView, "translationX", startX, 0);
//                                                    animator.setInterpolator(null);
//                                                    animator.setDuration(3000);
//                                                    animator.start();
//                                                    mAfficheParentView.setVisibility(View.VISIBLE);
//
//                                                    ViewManager.expandViewTouchDelegate(mDeleteAfficheView, 40, 40, 40, 40);
//                                                    mHideAfficheTimer.start();
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//                            }
//                        });
//                    } else {
//                        mAfficheParentView.setVisibility(View.GONE);
//                    }
//
//
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
//    @Override
//    public void setPresenter(OrderListContract.Presenter presenter) {
//        mPresenter = presenter;
//    }
//
//    @Override
//    public void onNoNetwork() {
//        dismissProgress();
//    }
//
//    @Override
//    public void onLogicFailed(Result result) {
//        dismissProgress();
//
//    }
//
//    @Override
//    public void onAutoLoginFailed(Result result) {
//        dismissProgress();
//    }
//
//    @Override
//    public void onShowProgress(String msg) {
//        showProgress(msg);
//    }
//
//    @Override
//    public void onDismissProgress() {
//        dismissProgress();
//    }
//
//    @Override
//    public void onGetJourneyComplete(List<MultiItemEntity> list) {
//        MLog.d(TAG, "onGetJourneyComplete ");
//
//        if (list != null && list.size() > 0) {
//            mRootView.findViewById(R.id.img_badge).setVisibility(View.VISIBLE);
//        } else {
//            mRootView.findViewById(R.id.img_badge).setVisibility(View.GONE);
//        }
//    }
//
//    private class AddMarkerTask extends TimerTask {
//
//        @Override
//        public void run() {
//            MLog.d(TAG, "## wait result  " + readyAddMarker + "  " + readyGetMapInfo);
//            if (readyAddMarker && readyGetMapInfo) {
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        addMarker();
//                        readyAddMarker = false;
//                        readyGetMapInfo = false;
//                        addMarkerTask.cancel();
//                    }
//                });
//            }
//        }
//
//    }
//
//    /**
//     * 获取当前城市是否开通二维码单程票业务
//     */
//    private void getSupportServiceYn(final int type, final String cityCode, boolean showProgress, final StationCode stationCode) {
//        if (showProgress)
//            showProgress();
//        mSTGateway.getSupportServiceYn(CssConstant.SP_SERVICE.SERVICE_ID_QR_CODE_TICKET, cityCode, new MobileGateway.MobileGatewayListener<GetSupportServiceYnRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                CommonToast.onFailed(mActivity, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgress();
//                CommonToast.onHttpFailed(mActivity);
//            }
//
//            @Override
//            public void onSuccess(GetSupportServiceYnRs response) {
//                dismissProgress();
//                isSupportQrCodeTicket = response.getSupportServiceYn().equalsIgnoreCase("Y");
//                if (type == BUY_TICKET_BY_STATION) {
//                    Intent intent = new Intent(mActivity, BuyTicketByStationActivity.class);
//                    intent.putExtra(SingleTicketManager.START_STATION, mStartStation);
//                    intent.putExtra(SingleTicketManager.END_STATION, mEndStation);
//                    //                    intent.putExtra(SpServiceManager.SERVICE_ID, SpServiceManager.SERVICE_ID_TICKET);
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE));
//                    intent.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, response.getSupportServiceYn());
//                    startActivity(intent);
//                    clearStationInfo();
//                } else if (type == BUY_TICKET_BY_PRICE) {
//                    Intent intent = new Intent(mActivity, BuyTicketByPriceActivity.class);
//                    intent.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, response.getSupportServiceYn());
//                    intent.putExtra(CssConstant.KEY_CITY_CODE, BizApplication.getInstance().getCityCode());
//                    startActivity(intent);
//                    clearStationInfo();
//                } else if (type == SELECT_START_STATION) {
//                    Intent intent2 = new Intent(mActivity, STSelectStaionActivity.class);
//                    intent2.putExtra(SingleTicketManager.INDEX_SELECT_STATION_TYPE, SingleTicketManager.SELECT_START_STATION_REQUEST);
//                    intent2.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, response.getSupportServiceYn().equalsIgnoreCase("y"));
//                    intent2.putExtra(CssConstant.KEY_CITY_CODE, MPreference.getString(mActivity, MPreference.CHOICE_CITY_CODE));
//                    startActivityForResult(intent2, SingleTicketManager.SELECT_START_STATION_REQUEST);
//                } else if (type == MOVE_TO_STATION_DETAIL) {
//                    Intent intent = new Intent(mActivity, StationDetailActivity.class);
//                    if (stationCode != null)
//                        intent.putExtra(CssConstant.KEY_STATION_CODE, stationCode.getStationCode());
//                    intent.putExtra(CssConstant.KEY_IS_SUPPORT_QR_CODE_TICKET_YN, response.getSupportServiceYn().equalsIgnoreCase("y"));
//                    startActivity(intent);
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgress();
//                CommonToast.onNoNetwork(mActivity);
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
//     * 获取行程数量
//     */
//    private void getJourneyCount() {
//        if (mPresenter != null && BizApplication.getInstance().isLoginClient())
//            mPresenter.onGetJourneyList();
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
//            EventBus.getDefault().unregister(STMapFragment.this);
//            StationCode stationCode = event.getStation();
//            int type = event.getType();
//            if (stationCode == null) {
//                MLog.d(TAG, "onEventMainThread STATION is null");
//                return;
//            }
//            if (type == SingleTicketManager.SELECT_START_STATION_REQUEST) {
//                setStartStation(stationCode);
//            } else if (type == SingleTicketManager.SELECT_END_STATION_REQUEST) {
//                setEndStation(stationCode);
//            }
//        } else
//            MLog.d(TAG, "onEventMainThread event is null");
//    }
//}
