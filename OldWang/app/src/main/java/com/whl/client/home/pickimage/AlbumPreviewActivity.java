package com.whl.client.home.pickimage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CommonToast;
import com.whl.client.app.CssConstant;
import com.whl.client.app.IEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwx on 2017/3/5.
 * 预览照片页面
 */
public class AlbumPreviewActivity extends BaseActivity {
    private static final String TAG = "AlbumPreviewActivity";
    private List<Fragment> mFragmentsList = new ArrayList<Fragment>();
    private ViewPager mViewPager;
    private List<ImageItem> mAllImageItemList = new ArrayList<>();
    private List<ImageItem> mCurrentSelectedItemList = new ArrayList<>();
    private List<ImageItem> mBeforeSelectedItemList = new ArrayList<>();
    private ImageButton mBtnSelect;
    private ImageItem mCurrentImageItem;
    private TextView mBtnBack;
    private TextView mTvTitle;
    private TextView mTvComplete;

    private int mCurrentPosition;
    private int mLaunchFlag;
    private int mMaxSelectableCount = CssConstant.BBS.IMG_MAX_NUM;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        MLog.d(TAG, "onCreate");
        BizApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_album_preview);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mBtnBack = (TextView) findViewById(R.id.img_back);
        mBtnSelect = (ImageButton) findViewById(R.id.chk_pick_img);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvComplete = (TextView) findViewById(R.id.tv_complete);

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSelectPhotoListByEventBus();
                finish();
            }
        });

        initData();

        if (mAllImageItemList != null && mAllImageItemList.size() > 0) {
            for (int i = 0; i < mAllImageItemList.size(); i++) {
                mFragmentsList.add(AlbumPreviewFragment.getInstance(mAllImageItemList.get(i)));
            }
        }

        initAdapter();

        if (mCurrentPosition != -1) {
            mViewPager.setCurrentItem(mCurrentPosition);
        } else {
            checkBtnSelectStatus(mAllImageItemList.get(0));
        }

        if (mCurrentPosition == -1) {
            mCurrentPosition = 1;
            mCurrentImageItem = mAllImageItemList.get(0);
        } else {
            mCurrentImageItem = mAllImageItemList.get(mCurrentPosition);
            mCurrentPosition = mCurrentPosition + 1;
        }

        mTvTitle.setText(mCurrentPosition + "/" + mAllImageItemList.size());

        setCompleteBtnText();

        mBtnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBtnSelect.isSelected()) {
                    if (mCurrentSelectedItemList.size() < mMaxSelectableCount) {
                        setItemChecked(mCurrentImageItem, true);
                        mBtnSelect.setSelected(true);
                    } else {
                        mBtnSelect.setSelected(false);
                        //                        CommonToast.toast(AlbumPreviewActivity.this, getString(R.string.max_album_size));
                        CommonToast.toast(AlbumPreviewActivity.this, String.format(getString(R.string.max_album_size), mMaxSelectableCount + ""));
                    }
                } else {
                    mBtnSelect.setSelected(false);
                    setItemChecked(mCurrentImageItem, false);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        BizApplication.getInstance().removeActivity(this);
    }

    private void initData() {
        List<ImageItem> tempList = (List<ImageItem>) getIntent().getSerializableExtra(CssConstant.BBS.KEY_PHOTO_LIST);
        mLaunchFlag = getIntent().getIntExtra(CssConstant.BBS.KEY_LAUNCH_PREVIEW_FLAG, -1);
        MLog.d(TAG, "launch flag = " + mLaunchFlag);
        mAllImageItemList.clear();
        mAllImageItemList.addAll(tempList);

        List<ImageItem> list = (List<ImageItem>) getIntent().getSerializableExtra(CssConstant.BBS.KEY_SELECTED_PHOTO_LIST);
        mCurrentSelectedItemList.clear();
        mCurrentSelectedItemList.addAll(list);
        mMaxSelectableCount = getIntent().getIntExtra(CssConstant.BBS.KEY_MAX_SELECTABLE_COUNT, CssConstant.BBS.IMG_MAX_NUM);
        MLog.d(TAG, "selected size = " + mCurrentSelectedItemList.size() + " max selectable count = " + mMaxSelectableCount);


        List<ImageItem> beforeList = (List<ImageItem>) getIntent().getSerializableExtra(CssConstant.BBS.KEY_BEFORE_SELECTED_PHOTO_LIST);
        mBeforeSelectedItemList.clear();
        mBeforeSelectedItemList.addAll(beforeList);
        mCurrentPosition = getIntent().getIntExtra(CssConstant.BBS.KEY_IMAGE_POSITION, -1);
    }

    private void setCompleteBtnText() {
        if (mCurrentSelectedItemList.size() > 0) {
            mTvComplete.setText(getString(R.string.complete) + "(" + mCurrentSelectedItemList.size() + "/" + mMaxSelectableCount + ")");
            mTvComplete.setTextColor(getResources().getColor(R.color.FFFFFF));
            mTvComplete.setClickable(true);
        } else {
            mTvComplete.setClickable(false);
            mTvComplete.setTextColor(getResources().getColor(R.color._797979));
            mTvComplete.setText(getString(R.string.complete));
        }
    }

    private void initAdapter() {
        FragmentPagerAdapter adapter = new PreviewFragmentAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentImageItem = mAllImageItemList.get(position);
                checkBtnSelectStatus(mCurrentImageItem);
                mTvTitle.setText(position + 1 + "/" + mAllImageItemList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void checkBtnSelectStatus(ImageItem imageItem) {
        if (isContainItem(imageItem)) {
            mBtnSelect.setSelected(true);
        } else {
            mBtnSelect.setSelected(false);
        }
    }

    private void setItemChecked(ImageItem imageItem, boolean isChecked) {
        //        mSelectedPositions.put(position, isChecked);
        if (isChecked) {
            mCurrentSelectedItemList.add(imageItem);
        } else {
            int position = -1;
            for (int i = 0; i < mCurrentSelectedItemList.size(); i++) {
                ImageItem item = mCurrentSelectedItemList.get(i);
                if (imageItem.getImageId().equals(item.getImageId())) {
                    position = i;
                    break;
                }
            }
            if (position != -1)
                mCurrentSelectedItemList.remove(position);
        }
        setCompleteBtnText();
    }

    private boolean isContainItem(ImageItem imageItem) {
        for (ImageItem item : mCurrentSelectedItemList) {
            if (imageItem != null && imageItem.getImageId().equals(item.getImageId())) {
                return true;
            }
        }
        return false;
    }

    private class PreviewFragmentAdapter extends FragmentPagerAdapter {

        public PreviewFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentsList.size();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        sendSelectPhotoListByEventBus();
    }

    private void sendSelectPhotoListByEventBus() {
        EventBus.getDefault().post(new IEvent.PickPreviewImageEvent(mCurrentSelectedItemList));
    }


}
