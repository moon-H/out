//package com.cssweb.shankephone.view;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.text.TextUtils;
//import android.util.AttributeSet;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.HorizontalScrollView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.db.CommonDBManager;
//import com.cssweb.shankephone.gateway.model.spservice.Service;
//import com.cssweb.shankephone.gateway.model.spservice.ServiceCity;
//import com.cssweb.shankephone.preference.MPreference;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by lenovo on 2016/9/17.
// */
//public class HomeTabIndicator extends LinearLayout implements View.OnClickListener {
//    private static final String TAG = "HomeTabIndicator";
//    private View mParentView;
//    //    private ViewPager mViewPager;
//    private int mSelectedTabIndex = 0;
//
//    //    private TextView tvTab0;
//    //    private ImageView imgIndicator0;
//    //    private OrderView tab0;
//
//    private HorizontalScrollView mScrollView;
//    private LinearLayout mTabLayout;
//
//    private ArrayList<RelativeLayout> mTabTextList = new ArrayList<>();
//    //    private ArrayList<ImageView> mTabImgList = new ArrayList<>();
//    private ArrayList<View> mTabList = new ArrayList<>();
//    private ArrayList<Service> serviceList = new ArrayList<>();
//
//    private int mSelectedColor;
//    private int mNormalColor;
//    private int mNotSupportColor;
//    private int mScrollTabSize;
//    private int mFixedItemWidth;
//    private int mCommonMargin;
//    //    private FragmentPagerAdapter mFragmentPageAdapter;
//
//    private Context mContext;
//
//    private int mTabItemWidth;
//    private int mTabItemHeight;
//    private int mTabItemTextSize;
//    private int mTabItemIndicatorWidth;
//    private int mItemMarginSmall;
//    private int mItemMarginMiddle;
//    private int mItemBgHeight;
//
//    private String notSupport;
//    private int mIndicatorColors;
//    private OnHomeTabClickListener listener;
//    private CommonDBManager mCommonDBManager;
//
//    public HomeTabIndicator(Context context) {
//        super(context);
//        init(context);
//    }
//
//    public HomeTabIndicator(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    private void init(Context context) {
//        mContext = context;
//        Resources resources = getResources();
//        mParentView = inflate(context, R.layout.layout_tab_pageindicator, this);
//        mSelectedColor = resources.getColor(R.color.FFFFFF);
//        mNormalColor = resources.getColor(R.color.st_order_text_nor);
//        mNotSupportColor = resources.getColor(R.color._999999);
//        mFixedItemWidth = resources.getDimensionPixelOffset(R.dimen.st_tab_item_indicator_width);
//        mCommonMargin = resources.getDimensionPixelOffset(R.dimen.common_margin_left);
//        notSupport = resources.getString(R.string.current_city_not_support);
//
//        mScrollView = (HorizontalScrollView) findViewById(R.id.hsv_scroll);
//        mTabLayout = (LinearLayout) findViewById(R.id.lly_item_parent);
//        //        tvTab0 = (TextView) mParentView.findViewById(R.id.tv_tab1);
//        //        imgIndicator0 = (ImageView) findViewById(R.id.img_inicator1);
//        //        tab0 = findViewById(R.id.rll_tab1);
//        mTabItemWidth = resources.getDimensionPixelOffset(R.dimen.home_tab_item_width);
//        mTabItemHeight = resources.getDimensionPixelOffset(R.dimen.home_tab_item_height);
//        mTabItemTextSize = resources.getDimensionPixelSize(R.dimen.home_tab_item_text_size);
//        mTabItemIndicatorWidth = resources.getDimensionPixelOffset(R.dimen.st_tab_item_indicator_height);
//        mItemMarginSmall = resources.getDimensionPixelOffset(R.dimen.home_tab_item_margin_small);
//        mItemMarginMiddle = resources.getDimensionPixelOffset(R.dimen.home_tab_item_margin_middle);
//        mItemBgHeight = resources.getDimensionPixelOffset(R.dimen.home_tab_item_bg_height);
//
//        mIndicatorColors = resources.getColor(R.color.st_order_text_selected);
//
//        mScrollTabSize = serviceList.size();
//        mCommonDBManager = new CommonDBManager(context);
////        mTabLayout.getBackground().setAlpha(10);
//    }
//
//    private void notifyDataSetChanged() {
//        setCurrentItem(mSelectedTabIndex);
//    }
//
//    public void setTabClickedListener(OnHomeTabClickListener listener) {
//        this.listener = listener;
//    }
//
//    public void setCurrentItem(String serviceId) {
//        if (TextUtils.isEmpty(serviceId)) {
//            MLog.d(TAG, "service id is null");
//            return;
//        }
//        for (int i = 0; i < serviceList.size(); i++) {
//            if (serviceId.equals(serviceList.get(i).getServiceId())) {
//                mSelectedTabIndex = i;
//                break;
//            }
//        }
//        mSelectedTabIndex = mSelectedTabIndex + 1;
//        switchTab(mSelectedTabIndex);
//    }
//
//    public void setCurrentItem(int item) {
//        MLog.d(TAG, "setCurrentItem = " + item);
//        //        if (mViewPager == null) {
//        //            throw new IllegalStateException("ViewPager has not been bound.");
//        //        }
//        mSelectedTabIndex = item+1;
//        switchTab(mSelectedTabIndex);
//    }
//
//    private void switchTab(int item) {
//        int count = mTabLayout.getChildCount();
//        for (int i = 0; i < count; i++) {
//            RelativeLayout layout = (RelativeLayout) mTabLayout.getChildAt(i);
//            RelativeLayout second = (RelativeLayout) layout.getChildAt(0);
//            TextView tv = (TextView) second.getChildAt(0);
//            Service service = (Service) layout.getTag();
//            boolean isCurrentCitySupport = isCurrentCitySupport(service);
//            if (isCurrentCitySupport) {
//                if (mSelectedTabIndex - 1 == i) {
//                    MLog.d(TAG, "trace 2");
//                    tv.setTextColor(mSelectedColor);
//                    second.setBackgroundResource(R.drawable.nav_select);
//                } else {
//                    MLog.d(TAG, "trace 3");
//                    second.setBackgroundResource(R.color.transparent);
//                    tv.setTextColor(mNormalColor);
//                }
//            } else {
//                MLog.d(TAG, "trace 4");
//                tv.setTextColor(mNotSupportColor);
//                second.setBackgroundResource(R.color.transparent);
//            }
//        }
//        animateToTab(item);
//    }
//
//    @Override
//    public void onClick(View v) {
//
//        RelativeLayout view = (RelativeLayout) v;
//        Service tag = (Service) view.getTag();
//        RelativeLayout rv = (RelativeLayout) view.getChildAt(0);
//        MLog.d(TAG, " TAG = " + tag);
//        int position = serviceList.indexOf(tag);
//        //        int position2 = serviceList.indexOf("haha");
//        MLog.d(TAG, " TAG position = " + position);
//        //        MLog.d(TAG, " TAG position2 = " + position2);
//
//        //        boolean isSupportCity = isCurrentCitySupport(tag);
//        //        if (!isSupportCity) {
//        //            CommonToast.toast(mContext, notSupport);
//        //            return;
//        //        }
//
//        //        if (position == -1) {
//        //        } else {
//        //            //按下选中效果，业务需要先注释
//        //            //                        setCurrentItem(position + 1);
//        //        }
//        if (listener != null)
//            listener.onTabClicked(tag);
//    }
//
//    private Runnable mTabSelector;
//
//    private void animateToTab(final int position) {
//        int item;
//        if (position != 0) {
//            item = position - 1;
//        } else
//            item = position;
//        final View tabView = mTabLayout.getChildAt(item);
//        if (mTabSelector != null) {
//            removeCallbacks(mTabSelector);
//        }
//        mTabSelector = new Runnable() {
//            public void run() {
//                //                MLog.d(TAG, "getleft1 = " + tabView.getLeft());
//                //                MLog.d(TAG, "getleft2 = " + tabView.getWidth());
//                //                MLog.d(TAG, "getleft3 = " + getWidth() + " mCommonMargin = " + mCommonMargin);
//                try {
//                    final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
//                    //                MLog.d(TAG, "scrollPos = " + scrollPos);
//                    mScrollView.smoothScrollTo(scrollPos, 0);
//                    mTabSelector = null;
//                } catch (Exception e) {
//                    MLog.d(TAG, "animateToTab occur an error ", e);
//                }
//            }
//        };
//        post(mTabSelector);
//    }
//
//    private RelativeLayout getTabItem(int position, Service service, int totalItem) {
//        RelativeLayout layout = new RelativeLayout(mContext);
//        RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mTabItemHeight);
//        layout.setLayoutParams(parentParams);
//        layout.setTag(service);
//        layout.setBackgroundColor(getResources().getColor(R.color.transparent));
//
//        TextView textView = new TextView(mContext);
//        RelativeLayout.LayoutParams tvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tvParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        textView.setLayoutParams(tvParams);
//        textView.setText(service.getServiceName());
//        textView.setTextSize(Utils.px2sp(mContext, mTabItemTextSize));
//        textView.setPadding(mItemMarginSmall, 0, mItemMarginSmall, 0);
//
//        RelativeLayout parent = new RelativeLayout(mContext);
//        RelativeLayout.LayoutParams rvParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mItemBgHeight);
//        parent.setLayoutParams(rvParams);
//        parent.setBackgroundResource(R.drawable.nav_select);
//
//        //        RelativeLayout.LayoutParams childParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        rvParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//        if (position == 0) {
//            //                        childParams.setMargins(mItemMarginSmall, 0, 0, 0);
//            rvParams.leftMargin = mItemMarginSmall;
//            rvParams.rightMargin = 0;
//
//            //            textView.setPadding(mItemMarginSmall, 0, 0, 0);
//        } else if (position == totalItem - 1) {
//            //            textView.setPadding(mItemMarginMiddle, 0, mItemMarginSmall, 0);
//            rvParams.rightMargin = mItemMarginSmall;
//            //            childParams2.leftMargin = mItemMarginSmall;
//        } else {
//            rvParams.leftMargin = mItemMarginSmall;
//            rvParams.rightMargin = 0;
//            //            childParams.setMargins(mItemMarginMiddle, 0, 0, 0);
//            //            textView.setPadding(mItemMarginMiddle, 0, 0, 0);
//        }
//
//
//        parent.setLayoutParams(rvParams);
//        parent.addView(textView);
//        layout.addView(parent);
//        //        ImageView imageView = new ImageView(mContext);
//        //        RelativeLayout.LayoutParams childParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mTabItemIndicatorWidth);
//        //        childParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        //        childParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
//        //        imageView.setLayoutParams(childParams2);
//        //        imageView.setBackgroundColor(mIndicatorColors);
//        //        imageView.setVisibility(OrderView.GONE);
//        //        layout.addView(imageView);
//        return layout;
//    }
//
//    private boolean isCurrentCitySupport(Service service) {
//        boolean isSupport = false;
//        if (service.getServiceId().equals(SpServiceManager.SERVICE_ID_TICKET)) {
//            return true;
//        }
//        String currentCity = MPreference.getString(mContext, MPreference.CHOICE_CITY_CODE);
//        List<ServiceCity> cityList = mCommonDBManager.getCityListByServiceId(service.getServiceId());
//        //        List<ServiceCity> citysList = service.getServiceCity();
//        if (cityList != null && cityList.size() > 0 && !TextUtils.isEmpty(currentCity)) {
//            for (ServiceCity city : cityList) {
//                if (currentCity.equals(city.getCityCode())) {
//                    isSupport = true;
//                    break;
//                }
//            }
//        }
//        return isSupport;
//    }
//
//    public void setTabList(List<Service> list) {
//        if (list != null && list.size() > 0) {
//            serviceList.clear();
//            serviceList.addAll(list);
//            mTabList.clear();
//            mTabLayout.removeAllViews();
//        }
//        int size = serviceList.size();
//        for (int i = 0; i < size; i++) {
//            RelativeLayout tabView = getTabItem(i, serviceList.get(i), size);
//            RelativeLayout textView = (RelativeLayout) tabView.getChildAt(0);
//            //            ImageView imageView = (ImageView) tabView.getChildAt(1);
//            mTabList.add(tabView);
//            mTabTextList.add(textView);
//            //            mTabImgList.add(imageView);
//            mTabLayout.addView(tabView);
//            tabView.setOnClickListener(this);
//        }
//    }
//
//    public interface OnHomeTabClickListener {
//        void onTabClicked(Service service);
//    }
//
//    public void updateCityInfo() {
//        clearAllTab();
//        List<Service> tempList = new ArrayList<>();
//        for (int i = 0; i < serviceList.size(); i++) {
//            Service service = serviceList.get(0);
//            String currentCity = MPreference.getString(mContext, MPreference.CHOICE_CITY_CODE);
//            List<ServiceCity> cityList = service.getServiceCity();
//            if (cityList != null && cityList.size() > 0) {
//                boolean isSupport = false;
//                for (ServiceCity city : cityList) {
//                    if (currentCity.equals(city.getCityCode())) {
//                        isSupport = true;
//                        break;
//                    }
//                }
//                if (isSupport) {
//                    tempList.add(service);
//                }
//
//            }
//        }
//        serviceList.clear();
//        switchTab(mSelectedTabIndex);
//    }
//
//    private void clearAllTab() {
//        mTabList.clear();
//        mTabLayout.removeAllViews();
//    }
//
//}
