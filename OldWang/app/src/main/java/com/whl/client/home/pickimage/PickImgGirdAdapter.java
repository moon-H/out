package com.whl.client.home.pickimage;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whl.framework.utils.DeviceInfo;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.CommonToast;
import com.whl.client.app.CssConstant;
import com.whl.client.view.ViewManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liwx on 2017/3/2.
 */
public class PickImgGirdAdapter extends BaseQuickAdapter<ImageItem, BaseViewHolder> {
    private static final String TAG = "PickImgGirdAdapter";
    //    public static final int MAX_SELECT_COUNT = 3;
    private Context mContext;
    private SparseBooleanArray mSelectedPositions = new SparseBooleanArray();
    private OnItemClickedListener mOnItemClickedListener;
    private OnItemCheckChangedListener mOnItemCheckChangedListener;
    private int itemSize;
    private List<ImageItem> mSelectedItemList = new ArrayList<>();
    private int mMaxSelectableCount = CssConstant.BBS.IMG_MAX_NUM;
    private int mPickType;

    public PickImgGirdAdapter(Context context, List<ImageItem> data, int selectableCount, int pickType) {
        super(R.layout.item_pick_image, data);
        mContext = context;
        itemSize = (DeviceInfo.getScreenWidth(mContext) -
            mContext.getResources().getDimensionPixelOffset(R.dimen.bbs_album_list_item_horizontal_space) * 2 - mContext.getResources().getDimensionPixelOffset(R.dimen.bbs_album_list_item_horizontal_space) * 2) / 3;
        //        mSelectedItemList.addAll(selectedItemList);
        mMaxSelectableCount = selectableCount;
        mPickType = pickType;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ImageItem item) {
        //        MLog.d(TAG, "#convert");
        ImageView imageView = holder.getView(R.id.img_photo);
        final ImageButton checkBox = holder.getView(R.id.chk_pick_img);
        View parentView = holder.getView(R.id.parent);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickedListener != null) {
                    mOnItemClickedListener.onItemClicked(holder.getAdapterPosition(), item);
                }
            }
        });
        if (mPickType == CssConstant.PICK_PHOTO_TYPE_BBS_IMG) {
            checkBox.setVisibility(View.VISIBLE);
            if (isContainItem(item)) {
                checkBox.setSelected(true);
            } else {
                checkBox.setSelected(false);
            }
        } else {
            checkBox.setVisibility(View.GONE);
        }

        parentView.post(new Runnable() {
            @Override
            public void run() {
                ViewManager.expandViewTouchDelegate(checkBox, 80, 80, 80, 80);
            }
        });
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isSelected()) {
                    if (getSelectedItemList().size() < mMaxSelectableCount) {
                        setItemChecked(item, true);
                        checkBox.setSelected(true);
                    } else {
                        checkBox.setSelected(false);
                        //                        CommonToast.toast(mContext, mContext.getString(R.string.max_album_size));
                        CommonToast.toast(mContext, String.format(mContext.getString(R.string.max_album_size), mMaxSelectableCount));

                    }
                } else {
                    checkBox.setSelected(false);
                    setItemChecked(item, false);
                }
                if (mOnItemCheckChangedListener != null) {
                    mOnItemCheckChangedListener.onItemCheckChanged(getSelectedItemList().size());
                }
            }
        });

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) imageView.getLayoutParams();
        params.width = itemSize;
        params.height = itemSize;
        MLog.d(TAG, "width = " + itemSize);
        imageView.setLayoutParams(params);

        if (!TextUtils.isEmpty(item.getThumbnailPath()))
            Glide.with(mContext).load(item.getThumbnailPath()).centerCrop().listener(mGlideDrawableRequestListener).into(imageView);
        else {
            Glide.with(mContext).load(item.getImagePath()).centerCrop().listener(mGlideDrawableRequestListener).into(imageView);
        }

    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        MLog.d(TAG, "#onBindViewHolder");
    }

    private boolean isContainItem(ImageItem imageItem) {
        for (ImageItem item : mSelectedItemList) {
            if (imageItem.getImageId().equals(item.getImageId())) {
                return true;
            }
        }
        return false;
    }


    RequestListener<String, GlideDrawable> mGlideDrawableRequestListener = new RequestListener<String, GlideDrawable>() {
        @Override
        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
            MLog.d(TAG, " ### onException = " + model);
            return false;
        }

        @Override
        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
            MLog.d(TAG, " ### onResourceReady ");
            return false;
        }
    };

    public void updateSelectedList(List<ImageItem> list) {
        mSelectedItemList.clear();
        mSelectedItemList.addAll(list);
        notifyDataSetChanged();
    }

    //获得选中条目的结果
    public List<ImageItem> getSelectedItemList() {
        return mSelectedItemList;
    }

    private void setItemChecked(ImageItem imageItem, boolean isChecked) {
        //        mSelectedPositions.put(position, isChecked);
        if (isChecked) {
            mSelectedItemList.add(imageItem);
        } else {
            int position = -1;
            for (int i = 0; i < mSelectedItemList.size(); i++) {
                ImageItem item = mSelectedItemList.get(i);
                if (imageItem.getImageId().equals(item.getImageId())) {
                    position = i;
                    break;
                }
            }
            if (position != -1)
                mSelectedItemList.remove(position);
        }
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mOnItemClickedListener = listener;
    }

    public void setOnItemCheckChangedListener(OnItemCheckChangedListener listener) {
        mOnItemCheckChangedListener = listener;
    }

    public interface OnItemClickedListener {
        void onItemClicked(int position, ImageItem item);
    }

    public interface OnItemCheckChangedListener {
        void onItemCheckChanged(int totalCount);
    }
}
