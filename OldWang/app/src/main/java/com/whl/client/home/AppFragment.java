//package com.cssweb.shankephone.home;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentTransaction;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.cssweb.framework.http.model.Response;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.DeviceInfo;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseFragment;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.coffee.store.ShopFragment;
//import com.cssweb.shankephone.db.CommonDBManager;
//import com.cssweb.shankephone.gateway.InboxGateway;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.SpServiceGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.coffee.TTasteGoodApp;
//import com.cssweb.shankephone.gateway.model.inbox.GetMessageUnreadCntRs;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMap;
//import com.cssweb.shankephone.gateway.model.singleticket.StationCode;
//import com.cssweb.shankephone.gateway.model.spservice.GetTabServiceListRs;
//import com.cssweb.shankephone.gateway.model.spservice.Service;
//import com.cssweb.shankephone.gateway.model.spservice.ServiceCity;
//import com.cssweb.shankephone.gateway.model.wallet.GetUserHeadPicRs;
//import com.cssweb.shankephone.home.hce.HceTicketFragment;
//import com.cssweb.shankephone.home.inbox.InboxListActivity;
//import com.cssweb.shankephone.home.ticket.DownloadMapTilesActivity;
//import com.cssweb.shankephone.home.ticket.STHomeActivity;
//import com.cssweb.shankephone.home.ticket.STMapFragment;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.order.OrderListActivity;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.settings.InviteFriendActivity;
//import com.cssweb.shankephone.settings.SettingsActivity;
//import com.cssweb.shankephone.view.HomeTabIndicator;
//import com.cssweb.shankephone.view.MaterialBadgeTextView;
//import com.cssweb.shankephone.view.glide.GlideCircleTransform;
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//
//import org.apache.http.Header;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by lenovo on 2016/9/23.
// */
//public class AppFragment extends BaseFragment implements HomeTabIndicator.OnHomeTabClickListener, View.OnClickListener, OnFragmentCallBack {
//
//    private static final String TAG = "AppFragment";
//    STHomeActivity mActivity;
//
//    private static final int FLAG_LAUNCH_PANCHAN = 101;
//    private static final int FLAG_LAUNCH_COUPON = 103;
//    private View mRootView;
//    private TextView mTvCityName;
//    private SlidingMenu mLeftMenu;
//    private HomeTabIndicator indicator;
//    private TextView mTvPhoneNumber;
//    private View mMenuBtnView;
//    private View mMenuInfoView;
//    private View mLocationView;
//    private ImageView mImgUserHead;
//    private MaterialBadgeTextView mBadgeTextView;
//
//    private STMapFragment mMapViewFragment;
//    private HceTicketFragment mHceTicketFragment;
//    private ErrorFragment mErrorFragment;
//    private ShopFragment mCoffeeShopFragment;
//
//    private ArrayList<Fragment> mFragmentList = new ArrayList<>();
//    private ArrayList<Service> serviceArrayList = new ArrayList<Service>();
//
//    private SpServiceGateway mSpServiceGateway;
//    private WalletGateway mWalletGateway;
//    private InboxGateway mInboxGateway;
//
//    private MetroMap mMetroMap;
//
//    private ExecutorService mExecutor = Executors.newCachedThreadPool();
//    private CommonDBManager mCommonDBManager;
//    private Handler mHandler = new Handler();
//    private STHomeActivity.OnLeftMenuScrollListener mOnLeftMenuScrollListener;
//
//
//    public static AppFragment getInstance() {
//        return new AppFragment();
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        MLog.d(TAG, "onCreate :" + (savedInstanceState == null));
//        mFragmentList.clear();
//        mMapViewFragment = STMapFragment.getInstance();
//        mHceTicketFragment = HceTicketFragment.getInstance();       //HCE单程票
//
//        mErrorFragment = ErrorFragment.getInstance();
//        mCoffeeShopFragment = ShopFragment.getInstance();
//        mCommonDBManager = new CommonDBManager(BizApplication.getInstance());
//        mMapViewFragment.setFragmentCallback(this);
//
//        mSpServiceGateway = new SpServiceGateway(mActivity);
//        mWalletGateway = new WalletGateway(mActivity);
//        mInboxGateway = new InboxGateway(getActivity());
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        MLog.d(TAG, "onSaveInstanceState");
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        MLog.d(TAG, "onAttach");
//        mActivity = (STHomeActivity) getActivity();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        MLog.d(TAG, "onCreateView");
//        if (mRootView == null) {
//            MLog.d(TAG, "newCreateView");
//            mRootView = inflater.inflate(R.layout.fragment_app, container, false);
//            initView();
//            initFragment();
//            initMenu();
//            getTabServiceList();
//            handleSelectStationView(mMetroMap);
//        }
//
//        return mRootView;
//
//    }
//
//    private void initView() {
//        mRootView.findViewById(R.id.img_back).setOnClickListener(this);
//        mLocationView = mRootView.findViewById(R.id.lly_location);
//        mLocationView.setOnClickListener(this);
//        mTvCityName = (TextView) mRootView.findViewById(R.id.tv_city);
//        TextView titleName = (TextView) mRootView.findViewById(R.id.title_name);
//        titleName.setText(getString(R.string.shankephone_app_name));
//
//        indicator = (HomeTabIndicator) mRootView.findViewById(R.id.indicator);
//        ArrayList<Service> list = getLocalServices();
//        indicator.setTabList(list);
//        indicator.setTabClickedListener(this);
//        indicator.setCurrentItem(1);
//
//        String localCity = MPreference.getString(getActivity(), MPreference.CHOICE_CITY_NAME);
//        mTvCityName.setText(localCity);
//    }
//
//    private void initFragment() {
//        mFragmentList.clear();
//        mFragmentList.add(mMapViewFragment);
//        mFragmentList.add(mHceTicketFragment);
//        mFragmentList.add(mErrorFragment);
//        mFragmentList.add(mCoffeeShopFragment);
//        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//        transaction.add(R.id.lly_content2, mMapViewFragment);
//        transaction.add(R.id.lly_content2, mHceTicketFragment);
//        transaction.add(R.id.lly_content2, mErrorFragment);
//        transaction.add(R.id.lly_content2, mCoffeeShopFragment);
//        for (Fragment fragment : mFragmentList) {
//            if (!(fragment instanceof STMapFragment)) {
//                transaction.detach(fragment);
//            }
//        }
//        transaction.commitAllowingStateLoss();
//
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        if (mLeftMenu != null && mLeftMenu.isMenuShowing()) {
//            setUserId();
//            updateAuthInfo();
//        }
//        getUnreadMsg();
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        MLog.d(TAG, "onDestroy");
//        mRootView = null;
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
//        MLog.d(TAG, "onDetach");
//    }
//
//    @Override
//    public void onTabClicked(Service service) {
//        String serviceId = service.getServiceId();
//        if (TextUtils.isEmpty(serviceId)) {
//            MLog.e(TAG, "service id is null");
//            return;
//        }
//    }
//
//    private void switchFragment(Fragment targetFragment, String serviceId) {
//        if (targetFragment != null) {
//            if (targetFragment.isVisible()) {
//                MLog.d(TAG, "targetFragment is visible");
//                return;
//            }
//            if (indicator != null && !TextUtils.isEmpty(serviceId)) {
//                indicator.setCurrentItem(serviceId);
//            }
//
//            mLocationView.setVisibility(View.VISIBLE);
//            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
//            for (Fragment fragment : mFragmentList) {
//                transaction.detach(fragment);
//            }
//            transaction.attach(targetFragment);
//            transaction.commitAllowingStateLoss();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick())
//            return;
//        switch (v.getId()) {
//            case R.id.img_back:
//                getUnreadMsg();
//                mLeftMenu.showMenu();
//                break;
//            case R.id.lly_location:
//                startActivity(new Intent(getActivity(), DownloadMapTilesActivity.class));
//                if (mMapViewFragment != null) {
//                    mMapViewFragment.clearStationInfo();
//                }
//                break;
//            case R.id.btn_login:
//                launchLoginPage();
//                break;
//            case R.id.lly_personal_info:
//                startActivity(new Intent(mActivity, PersonalInfoActivity.class));
//                break;
//            case R.id.lly_panchan:
//                if (BizApplication.getInstance().isLoginClient())
//                    if (LoginManager.hasPhoneNumber(mActivity)) {
//                    } else
//                        showBindPhoneNumberDialog(mActivity);
//                else
//                    launchLoginPage();
//                break;
//            case R.id.lly_coupon:
//                if (BizApplication.getInstance().isLoginClient())
//                    if (LoginManager.hasPhoneNumber(mActivity)) {
//                    } else
//                        mActivity.showBindPhoneNumberDialog(mActivity);
//                else
//                    startActivity(new Intent(mActivity, LoginActivity.class));
//                break;
//            case R.id.lly_inbox:
//                if (!BizApplication.getInstance().isLoginClient()) {
//                    launchLoginPage();
//                    return;
//                }
//                startActivity(new Intent(mActivity, InboxListActivity.class));
//                break;
//            case R.id.lly_share:
//                //邀请好友
//                if (!BizApplication.getInstance().isLoginClient()) {
//                    launchLoginPage();
//                } else {
//                    if (LoginManager.hasPhoneNumber(mActivity)) {
//                        startActivity(new Intent(mActivity, InviteFriendActivity.class));
//                    } else {
//                        mActivity.showBindPhoneNumberDialog(mActivity);
//                    }
//                }
//                break;
//            case R.id.lly_settings:
//                startActivity(new Intent(getActivity(), SettingsActivity.class));
//                break;
//            case R.id.img_head:
//                startActivity(new Intent(mActivity, PersonalInfoActivity.class));
//                break;
//            case R.id.lly_my_order:
//                //我的订单
//                if (!BizApplication.getInstance().isLoginClient()) {
//                    launchLoginPage();
//                } else {
//                    if (LoginManager.hasPhoneNumber(mActivity)) {
//                        startActivity(new Intent(mActivity, OrderListActivity.class));
//                    } else {
//                        mActivity.showBindPhoneNumberDialog(mActivity);
//                    }
//                }
//                break;
//        }
//    }
//
//    @Override
//    public void onFragmentCallBack(int index) {
//        MLog.d(TAG, "index = " + index);
//    }
//
//    private void launchLoginPage() {
//        startActivity(new Intent(mActivity, LoginActivity.class));
//    }
//
//    /**
//     * 初始化侧滑菜单
//     */
//    private void initMenu() {
//        mLeftMenu = new SlidingMenu(getActivity());
//        mLeftMenu.setMode(SlidingMenu.LEFT);
//        // 设置触摸屏幕的模式
//        mLeftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
//        //        mLeftMenu.setShadowWidthRes(R.dimen.shadow_width);
//        mLeftMenu.setShadowDrawable(R.drawable.shadow);
//        // 设置滑动菜单视图的宽度
//        mLeftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        // 设置渐入渐出效果的值
//        mLeftMenu.setFadeDegree(0);
//        // 把滑动菜单添加进所有的Activity中，可选值SLIDING_CONTENT ， SLIDING_WINDOW
//        mLeftMenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
//        // 为侧滑菜单设置布局
//        mLeftMenu.setMenu(R.layout.leftmenu);
//        mLeftMenu.getMenu().findViewById(R.id.btn_login).setOnClickListener(this);
//        //        mLeftMenu.getMenu().findViewById(R.id.btn_register).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_personal_info).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_panchan).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_coupon).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_inbox).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_share).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_settings).setOnClickListener(this);
//        mLeftMenu.getMenu().findViewById(R.id.lly_my_order).setOnClickListener(this);
//        //        mBadgeView.setTargetView(mLeftMenu.getMenu().findViewById(R.id.tv_inbox));
//        mImgUserHead = (ImageView) mLeftMenu.getMenu().findViewById(R.id.img_head);
//        mImgUserHead.setOnClickListener(this);
//
//        mTvPhoneNumber = (TextView) mLeftMenu.getMenu().findViewById(R.id.tv_phone_number);
//        //        mTvAuth = (TextView) mLeftMenu.getMenu().findViewById(R.id.tv_auth);
//        mMenuBtnView = mLeftMenu.getMenu().findViewById(R.id.lly_btn_view);
//        mMenuInfoView = mLeftMenu.getMenu().findViewById(R.id.lly_info_view);
//        mBadgeTextView = (MaterialBadgeTextView) mLeftMenu.getMenu().findViewById(R.id.tv_badge);
//        mLeftMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
//            @Override
//            public void onClosed() {
//                MLog.d(TAG, "%%% onClosed");
//                //                mMaskView.setVisibility(OrderView.GONE);
//                if (mOnLeftMenuScrollListener != null) {
//                    mOnLeftMenuScrollListener.onLeftMenuClosed();
//                }
//            }
//        });
//        mLeftMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
//            @Override
//            public void onOpened() {
//                MLog.d(TAG, "%%% onOpened");
//                setUserId();
//                updateAuthInfo();
//                //                requestLoginBBS();
//                if (mOnLeftMenuScrollListener != null) {
//                    mOnLeftMenuScrollListener.onLeftMenuOpened();
//                }
//                getUserHeadPic();
//            }
//
//        });
//        mLeftMenu.setOnScrollListener(new SlidingMenu.OnScrolledListener() {
//            @Override
//            public void onScroll(int dtx) {
//                if (mOnLeftMenuScrollListener != null) {
//                    mOnLeftMenuScrollListener.onLeftMenuScroll(dtx);
//                }
//            }
//        });
//    }
//
//    private void setUserId() {
//        if (BizApplication.getInstance().isLoginClient()) {
//            mMenuBtnView.setVisibility(View.GONE);
//            mMenuInfoView.setVisibility(View.VISIBLE);
//            if (LoginManager.hasShankePhoneId(getActivity()))
//                mTvPhoneNumber.setText(LoginManager.getPhoneNumber(mActivity));
//            else if (LoginManager.hasThirdPartyId(mActivity)) {
//                //                mTvPhoneNumber.setText(MPreference.getString(getActivity(), MPreference.THIRD_LOGIN_NICKNAME));
//                //                byte[] nickNameByte = Base64.decode(MPreference.getString(getActivity(), MPreference.THIRD_LOGIN_NICKNAME), Base64.DEFAULT);
//                String NICK = MPreference.getString(getActivity(), MPreference.THIRD_LOGIN_NICKNAME);
//                MLog.d(TAG, "nick =" + NICK);
//                mTvPhoneNumber.setText(NICK);
//            }
//        } else {
//            mMenuBtnView.setVisibility(View.VISIBLE);
//            mMenuInfoView.setVisibility(View.GONE);
//        }
//    }
//
//    public void updateMap(MetroMap metroMap) {
//        MLog.d(TAG, "updateView");
//        if (mMapViewFragment != null) {
//            mMapViewFragment.updateView(metroMap);
//        }
//        mMetroMap = metroMap;
//        handleSelectStationView(metroMap);
//    }
//
//    public void updateLocation(StationCode stationCode) {
//        if (mMapViewFragment != null) {
//            mMapViewFragment.updateLocationInfo(stationCode);
//        }
//    }
//
//    public void clearMarkView() {
//        MLog.d(TAG, "clearMarkView");
//        if (mMapViewFragment != null) {
//            mMapViewFragment.clearAllMarkView();
//        }
//    }
//
//    public void updateTabMenu() {
//        getTabServiceList();
//    }
//
//    public void setOnLefMenuScrollListener(STHomeActivity.OnLeftMenuScrollListener lefMenuScrollListener) {
//        mOnLeftMenuScrollListener = lefMenuScrollListener;
//    }
//
//    private void handleSelectStationView(MetroMap metroMap) {
//        if (metroMap != null && mTvCityName != null) {
//            mTvCityName.setText(metroMap.getCityName());
//            if (mMapViewFragment != null) {
//                mMapViewFragment.setSupportSingleTicketYn(metroMap.getSingleTicketServiceYn());
//            }
//        }
//    }
//
//    /**
//     * 处理HCE单程票
//     */
//    private void handleHceTicket() {
//        if (BizApplication.getInstance().isLoginClient()) {
//            if (LoginManager.hasPhoneNumber(mActivity)) {
//                launchHceDetail();
//            } else {
//                dismissProgress();
//                mActivity.showBindPhoneNumberDialog(getActivity());
//            }
//        } else {
//            dismissProgress();
//            startActivity(new Intent(mActivity, LoginActivity.class));
//        }
//    }
//
//    /**
//     * 关闭‘我的’菜单
//     */
//    public void closeLeftMenu() {
//        if (mLeftMenu != null && mLeftMenu.isMenuShowing()) {
//            mLeftMenu.toggle();
//        }
//    }
//
//
//    private void switchErrorFragment(int errorCode, String msg) {
//        if (mErrorFragment != null) {
//            mErrorFragment.setErrorMsg(errorCode, msg);
//            switchFragment(mErrorFragment, "");
//        } else {
//            CommonToast.toast(getActivity(), msg + "");
//        }
//    }
//
//    @NonNull
//    private ArrayList<Service> getLocalServices() {
//        ArrayList<Service> list = new ArrayList<>();
//        Service service = new Service();
//        service.setServiceName(getString(R.string.single_tickeys));
//        list.add(service);
//        return list;
//    }
//
//    /**
//     * 判断手机是否支持HCE
//     */
//    private void launchHceDetail() {
//        //        dismissProgressView();
//        //是否支持HCE hasNfcHce
//        PackageManager pm = mActivity.getPackageManager();
//        boolean hasNfcHce = false;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            hasNfcHce = pm.hasSystemFeature(PackageManager.FEATURE_NFC_HOST_CARD_EMULATION);
//        }
//        //是否root
//        if (DeviceInfo.isRooted()) {
//            dismissProgress();
//            switchErrorFragment(CssConstant.ERROR_CODE_DEFAULT, getString(R.string.already_rooted));
//        } else {
//        }
//    }
//
//
//    private void updateServiceStatus(final String serviceId, final String status) {
//        MLog.d(TAG, "updateServiceStatus serviceId = " + serviceId + " status = " + status);
//        if (!BizApplication.getInstance().isLoginClient()) {
//            MLog.d(TAG, "not login");
//            return;
//        }
//        mSpServiceGateway.updateServiceStatus(serviceId, status, new MobileGateway.MobileGatewayListener() {
//            @Override
//            public void onFailed(Result result) {
//                CommonToast.onFailed(mActivity, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                CommonToast.onHttpFailed(mActivity);
//            }
//
//            @Override
//            public void onSuccess(Response response) {
//                //                getMyServiceListOnLine();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                CommonToast.onNoNetwork(mActivity);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                updateServiceStatus(serviceId, status);
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
//    public void handleMnoRequest() {
//        MLog.d(TAG, "handleMnoRequest");
//        if (serviceArrayList.size() < 2) {
//            MLog.d(TAG, "ChangSha service not support");
//            return;
//        }
//    }
//
//    private void getTabServiceList() {
//        serviceArrayList.clear();
//        mExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                final List<Service> list = mCommonDBManager.getLocalHomeTab();
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        updateHomeTab(list);
//                        handleMnoRequest();
//                    }
//                });
//
//            }
//        });
//
//        mSpServiceGateway.getTabServiceList(new MobileGateway.MobileGatewayListener<GetTabServiceListRs>() {
//            @Override
//            public void onFailed(Result result) {
//                //                CommonToast.onFailed(getActivity(), result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                //                CommonToast.onHttpFailed(getActivity());
//            }
//
//            @Override
//            public void onSuccess(GetTabServiceListRs response) {
//                serviceArrayList.clear();
//                final List<Service> list = response.getServiceList();
//                if (list != null) {
//                    mExecutor.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            mCommonDBManager.removeAllData(Service.class);
//                            mCommonDBManager.removeAllData(ServiceCity.class);
//                            mCommonDBManager.saveAllData(list);
//                            for (Service service : list) {
//                                List<ServiceCity> cityList = service.getServiceCity();
//                                if (cityList != null)
//                                    mCommonDBManager.saveAllData(cityList);
//                            }
//                            mHandler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    updateHomeTab(list);
//                                }
//                            });
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onNoNetwork() {
//                //                CommonToast.onNoNetwork(getActivity());
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
//    private void updateHomeTab(List<Service> list) {
//        serviceArrayList.clear();
//        serviceArrayList.addAll(list);
//        if (serviceArrayList != null && serviceArrayList.size() > 0) {
//            List<Service> tempList = new ArrayList<>();
//            for (int i = 0; i < serviceArrayList.size(); i++) {
//                Service service = serviceArrayList.get(i);
//                String currentCity = MPreference.getString(BizApplication.getInstance(), MPreference.CHOICE_CITY_CODE);
//                List<ServiceCity> cityList = mCommonDBManager.getCityListByServiceId(service.getServiceId());
//                if (cityList != null && cityList.size() > 0) {
//                    boolean isSupport = false;
//                    for (ServiceCity city : cityList) {
//                        if (currentCity.equals(city.getCityCode())) {
//                            isSupport = true;
//                            break;
//                        }
//                    }
//                    if (isSupport) {
//                        tempList.add(service);
//                    }
//
//                }
//            }
//            serviceArrayList.clear();
//            serviceArrayList.addAll(tempList);
//            if (serviceArrayList.size() == 0) {
//                serviceArrayList.addAll(getLocalServices());
//            }
//            indicator.setTabList(serviceArrayList);
//            indicator.setCurrentItem(0);
//        }
//    }
//
//    private void updateAuthInfo() {
//        MLog.d(TAG, "getAuthInfo isLogin = " + BizApplication.getInstance().isLoginClient() + " loginId = " + LoginManager.hasShankePhoneId(getActivity()));
//        if (!BizApplication.getInstance().isLoginClient() || !LoginManager.hasShankePhoneId(getActivity())) {
//            return;
//        }
//
//    }
//
//    /**
//     * 更新用户头像
//     */
//    public void getUserHeadPic() {
//        if (!BizApplication.getInstance().isLoginClient()) {
//            MLog.d(TAG, "getUserHeadPic not login");
//            return;
//        }
//
//        mWalletGateway.getUserHeadPic(new MobileGateway.MobileGatewayListener<GetUserHeadPicRs>() {
//            @Override
//            public void onFailed(Result result) {
//                displayDefaultHead();
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                displayDefaultHead();
//            }
//
//            @Override
//            public void onSuccess(GetUserHeadPicRs response) {
//                if (TextUtils.isEmpty(response.getImageUrl())) {
//                    displayDefaultHead();
//                } else
//                    Glide.with(mActivity).load(response.getImageUrl()).transform(new GlideCircleTransform(mActivity)).into(mImgUserHead);
//            }
//
//            @Override
//            public void onNoNetwork() {
//                displayDefaultHead();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getUserHeadPic();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                displayDefaultHead();
//            }
//
//        });
//    }
//
//    private void displayDefaultHead() {
//        Glide.with(mActivity).load(R.drawable.icon_my_head).transform(new GlideCircleTransform(mActivity)).into(mImgUserHead);
//    }
//
//    public void setBadge(int count) {
//        mBadgeTextView.setBadgeCount(count);
//        if (mRootView != null) {
//            if (count > 0) {
//                mRootView.findViewById(R.id.img_badge).setVisibility(View.VISIBLE);
//            } else {
//                mRootView.findViewById(R.id.img_badge).setVisibility(View.GONE);
//            }
//        }
//    }
//
//    private void getUnreadMsg() {
//        if (BizApplication.getInstance().isLoginClient()) {
//            mInboxGateway.getMessageUnreadCnt(new MobileGateway.MobileGatewayListener<GetMessageUnreadCntRs>() {
//
//                @Override
//                public void onSuccess(GetMessageUnreadCntRs response) {
//                    setBadge(response.getUnreadCount());
//                }
//
//                @Override
//                public void onNoNetwork() {
//                    CommonToast.onNoNetwork(mActivity);
//                }
//
//                @Override
//                public void onHttpFailed(int statusCode, Header[] headers) {
//                    MLog.d(TAG, "Get unread msg onHttpFailed::" + statusCode);
//                }
//
//                @Override
//                public void onFailed(Result result) {
//                    MLog.d(TAG, "Get unread msg failed::" + result.getMessage());
//                }
//
//                @Override
//                public void onAutoLoginSuccess() {
//                    getUnreadMsg();
//                }
//
//                @Override
//                public void onAutoLoginFailed(Result result) {
//                    handleAutoLoginFailed(result);
//                }
//
//            });
//        } else {
//            mBadgeTextView.setVisibility(View.GONE);
//            mRootView.findViewById(R.id.img_badge).setVisibility(View.GONE);
//        }
//    }
//
//    public void displayDefaultUserHead() {
//        Glide.with(mActivity).load(R.drawable.icon_my_head).transform(new GlideCircleTransform(mActivity)).into(mImgUserHead);
//    }
//
//    public void setHotSaleInfo(int type, TTasteGoodApp goodsApp) {
//        if (mCoffeeShopFragment != null) {
//            mCoffeeShopFragment.setCheckGoodsOffice(goodsApp, type);
//            switchFragment(mCoffeeShopFragment, CssConstant.SP_SERVICE.SERVICE_ID_COFFEE);
//        }
//
//
//    }
//
//}
