package com.whl.client.view;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.framework.utils.Utils;
import com.whl.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwx on 2015/10/29.
 */
public class TabPageIndicator extends LinearLayout implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private static final String TAG = "TabPageIndicator";
    private View mParentView;
    private ViewPager mViewPager;
    private int mSelectedTabIndex = 0;


    private HorizontalScrollView mScrollView;
    private LinearLayout mTabLayout;

    private ArrayList<TextView> mTabTextList = new ArrayList<>();
    private ArrayList<ImageView> mTabImgList = new ArrayList<>();
    private ArrayList<View> mTabList = new ArrayList<>();
    private ArrayList<String> nameList = new ArrayList<>();

    private int mSelectedColor;
    private int mNormalColor;
    private int mScrollTabSize;
    private int mFixedItemWidth;
    private int mCommonMargin;
    private FragmentPagerAdapter mFragmentPageAdapter;

    private Context mContext;

    private int mTabItemWidth;
    private int mTabItemHeight;
    private int mTabItemTextSize;
    private int mTabItemIndicatorheigh;
    private int mTabItemIndicatorWidth;

    private int mIndicatorColors;

    public TabPageIndicator(Context context) {
        super(context);
        init(context);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        Resources resources = getResources();
        mParentView = inflate(context, R.layout.layout_tab_pageindicator, this);
        mSelectedColor = resources.getColor(R.color.st_order_text_selected);
        mNormalColor = resources.getColor(R.color.st_order_text_nor);
        mFixedItemWidth = resources.getDimensionPixelOffset(R.dimen.st_tab_item_indicator_width);
        mCommonMargin = resources.getDimensionPixelOffset(R.dimen.common_margin_left);

        mScrollView = (HorizontalScrollView) findViewById(R.id.hsv_scroll);
        mTabLayout = (LinearLayout) findViewById(R.id.lly_item_parent);

        mTabItemWidth = resources.getDimensionPixelOffset(R.dimen.order_line);
        mTabItemHeight = resources.getDimensionPixelOffset(R.dimen.order_title_height);
        mTabItemTextSize = resources.getDimensionPixelSize(R.dimen.order_indicator_textsize);
        mTabItemIndicatorheigh = resources.getDimensionPixelOffset(R.dimen.st_tab_item_indicator_height);
        mTabItemIndicatorWidth = resources.getDimensionPixelOffset(R.dimen.order_line_text);
        mIndicatorColors = resources.getColor(R.color.st_order_text_selected);

        mScrollTabSize = nameList.size();
    }

    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.clearOnPageChangeListeners();
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        mFragmentPageAdapter = (FragmentPagerAdapter) mViewPager.getAdapter();
        view.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        setCurrentItem(mSelectedTabIndex);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (mFragmentPageAdapter != null) {
            Fragment fragment = mFragmentPageAdapter.getItem(position);
            if (fragment != null)
                fragment.onResume();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void setCurrentItem(int item) {
        MLog.d(TAG, "setCurrentItem = " + item);
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(item);
        MLog.d(TAG, "dddd" + item);
        int count = mTabLayout.getChildCount();

        if (mSelectedTabIndex == -1) {
            for (int i = 0; i < count; i++) {
                RelativeLayout layout = (RelativeLayout) mTabLayout.getChildAt(i);
                TextView tv = (TextView) layout.getChildAt(0);
                tv.setTextColor(mNormalColor);
                ImageView img = (ImageView) layout.getChildAt(1);
                img.setVisibility(View.GONE);
            }
        } else {
            for (int i = 0; i < count; i++) {
                RelativeLayout layout = (RelativeLayout) mTabLayout.getChildAt(i);
                TextView tv = (TextView) layout.getChildAt(0);
                ImageView img = (ImageView) layout.getChildAt(1);
                if (mSelectedTabIndex == i) {
                    //                    mTabTextList.get(i).setTextColor(mSelectedColor);
                    //                    mTabImgList.get(i).setVisibility(OrderView.VISIBLE);
                    tv.setTextColor(mSelectedColor);
                    img.setVisibility(View.VISIBLE);
                } else {
                    //                    mTabTextList.get(i).setTextColor(mNormalColor);
                    //                    mTabImgList.get(i).setVisibility(OrderView.GONE);
                    tv.setTextColor(mNormalColor);
                    img.setVisibility(View.GONE);
                }


            }
        }
        animateToTab(item);
    }

    @Override
    public void onClick(View v) {

        RelativeLayout view = (RelativeLayout) v;
        String tag = (String) view.getTag();
        TextView tv = (TextView) view.getChildAt(0);
        MLog.d(TAG, " TAG = " + tag);
        int position = nameList.indexOf(tag);
        int position2 = nameList.indexOf("haha");
        MLog.d(TAG, " TAG position = " + position);
        MLog.d(TAG, " TAG position2 = " + position2);
        setCurrentItem(position);
    }

    private Runnable mTabSelector;

    private void animateToTab(final int position) {
        int item;
        item = position;
        final View tabView = mTabLayout.getChildAt(item);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2 + mFixedItemWidth + mCommonMargin * 2;
                mScrollView.smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    private RelativeLayout getTabItem(String tabName) {
        RelativeLayout layout = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams parentParams = new RelativeLayout.LayoutParams(mTabItemWidth, mTabItemHeight);
        layout.setLayoutParams(parentParams);
        layout.setTag(tabName);

        TextView textView = new TextView(mContext);
        RelativeLayout.LayoutParams childParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        childParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        textView.setLayoutParams(childParams);
        textView.setText(tabName);
        textView.setTextSize(Utils.px2sp(mContext, mTabItemTextSize));
        layout.addView(textView);

        ImageView imageView = new ImageView(mContext);
        RelativeLayout.LayoutParams childParams2 = new RelativeLayout.LayoutParams(mTabItemIndicatorWidth, mTabItemIndicatorheigh);
        childParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        childParams2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        imageView.setLayoutParams(childParams2);
        imageView.setBackgroundColor(mIndicatorColors);
        imageView.setVisibility(View.GONE);
        layout.addView(imageView);
        return layout;
    }

    public void setTabList(List<String> list) {
        if (list != null && list.size() > 0) {
            nameList.clear();
            nameList.addAll(list);
        }
        int size = nameList.size();
        for (int i = 0; i < size; i++) {
            RelativeLayout tabView = getTabItem(nameList.get(i));
            TextView textView = (TextView) tabView.getChildAt(0);
            ImageView imageView = (ImageView) tabView.getChildAt(1);
            mTabList.add(tabView);
            mTabTextList.add(textView);
            mTabImgList.add(imageView);
            mTabLayout.addView(tabView);
            tabView.setOnClickListener(this);
        }
    }
}
