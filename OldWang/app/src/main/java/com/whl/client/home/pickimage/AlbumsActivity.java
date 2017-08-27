package com.whl.client.home.pickimage;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CssConstant;
import com.whl.client.app.IEvent;
import com.whl.client.view.DividerGridItemDecoration;
import com.whl.client.view.ViewManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liwx on 2017/3/2.
 * 图片列表页面
 */
public class AlbumsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AlbumsActivity";
    private PickImgGirdAdapter mPickImgGirdAdapter;
    private TextView mTvCancel;
    private TextView mTvComplete;
    private TextView mTvPreView;

    private ContentResolver cr;
    private List<ImageItem> mAllImageItemList = new ArrayList<>();
    private List<ImageItem> mBeforeSelectedPhotoList = new ArrayList<>();
    private HashMap<String, String> thumbnailMap = new HashMap<>();

    private Handler mHandler = new Handler();

    private int mCallPage;//从哪个页面跳转过来
    private int mPickType;//选择图片用途（头像或论坛发帖）

    private int mMaxSelectableCount = CssConstant.BBS.IMG_MAX_NUM;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_albums);
        BizApplication.getInstance().addActivity(this);
        List<ImageItem> tempList = (List<ImageItem>) getIntent().getSerializableExtra(CssConstant.BBS.KEY_PHOTO_LIST);
        mCallPage = getIntent().getIntExtra(CssConstant.BBS.KEY_LAUNCH_PREVIEW_FLAG, -1);
        mPickType = getIntent().getIntExtra(CssConstant.KEY_PICK_PHOTO_TYPE, -1);
        if (tempList != null) {
            mMaxSelectableCount = CssConstant.BBS.IMG_MAX_NUM - tempList.size();
            mBeforeSelectedPhotoList.addAll(tempList);
        }
        MLog.d(TAG, "launch flag = " + mCallPage + " max selectable = " + mMaxSelectableCount);

        //        if (tempList != null) {
        //        }
        initView();
        cr = getContentResolver();
        if (mPickType == CssConstant.PICK_PHOTO_TYPE_BBS_IMG) {
            mTvPreView.setVisibility(View.VISIBLE);
            mTvComplete.setVisibility(View.VISIBLE);
        } else {
            mTvPreView.setVisibility(View.GONE);
            mTvComplete.setVisibility(View.GONE);
        }
        getImageItem();
        EventBus.getDefault().register(this);
    }

    private void initView() {
        mPickImgGirdAdapter = new PickImgGirdAdapter(getApplicationContext(), mAllImageItemList, mMaxSelectableCount, mPickType);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        recyclerView.setAdapter(mPickImgGirdAdapter);
        //        mRecyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerGridItemDecoration(this, getResources().getDimensionPixelOffset(R.dimen.bbs_album_list_item_horizontal_space)));

        mTvCancel = (TextView) findViewById(R.id.tv_cancel);
        mTvComplete = (TextView) findViewById(R.id.tv_complete);
        mTvPreView = (TextView) findViewById(R.id.tv_preview);
        mTvCancel.setOnClickListener(this);
        mTvComplete.setOnClickListener(this);
        mTvPreView.setOnClickListener(this);
        setBtnStatus(false, 0);
        mTvPreView.post(new Runnable() {
            @Override
            public void run() {
                ViewManager.expandViewTouchDelegate(mTvPreView, 60, 60, 60, 60);
                ViewManager.expandViewTouchDelegate(mTvComplete, 60, 60, 60, 60);
                ViewManager.expandViewTouchDelegate(mTvCancel, 60, 60, 60, 60);
            }
        });

        mPickImgGirdAdapter.setOnItemClickedListener(new PickImgGirdAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(int position, ImageItem item) {
                //                CommonToast.toast(getApplicationContext(), "点击了位置 = " + position);
                if (mPickType == CssConstant.PICK_PHOTO_TYPE_BBS_IMG) {
                    launchAlbumsPreviewActivity(position, mAllImageItemList);
                } else {
                    //处理选择头像
                    EventBus.getDefault().post(new IEvent.PickUserHeadEvent(item));
                    finish();
                }
            }

        });
        mPickImgGirdAdapter.setOnItemCheckChangedListener(new PickImgGirdAdapter.OnItemCheckChangedListener() {
            @Override
            public void onItemCheckChanged(int totalCount) {
                MLog.d(TAG, "selected size = " + totalCount);
                if (totalCount > 0) {
                    setBtnStatus(true, totalCount);
                } else {
                    setBtnStatus(false, 0);
                }
            }
        });
    }

    /**
     * 设置预览按钮和完成按钮状态
     */
    private void setBtnStatus(boolean showCount, int totalCount) {
        if (showCount) {
            mTvComplete.setClickable(true);
            mTvPreView.setClickable(true);
            mTvComplete.setText(getString(R.string.complete) + "(" + totalCount + "/" + mMaxSelectableCount + ")");
            mTvPreView.setText(getString(R.string.preview) + "(" + totalCount + ")");
            mTvPreView.setTextColor(getResources().getColor(R.color.FFFFFF));
            mTvComplete.setTextColor(getResources().getColor(R.color.FFFFFF));
        } else {
            mTvComplete.setClickable(false);
            mTvPreView.setClickable(false);
            mTvComplete.setText(getString(R.string.complete));
            mTvPreView.setTextColor(getResources().getColor(R.color._797979));
            mTvComplete.setTextColor(getResources().getColor(R.color._797979));
            mTvPreView.setText(getString(R.string.preview));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
        EventBus.getDefault().unregister(this);
    }

    //获取缩略图信息
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

            do {
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailMap.put("" + image_id, image_path);
            } while (cur.moveToNext());
            cur.close();
        }
    }

    private void getThumbnail() {
        String[] projection = {MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID, MediaStore.Images.Thumbnails.DATA};
        Cursor cursor =
            //            = cr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);

            MediaStore.Images.Thumbnails.queryMiniThumbnails(cr, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, MediaStore.Images.Thumbnails.MINI_KIND, projection);

        getThumbnailColumnData(cursor);
    }

    private void getImageItem() {
        BizApplication.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                getThumbnail();
                String columns[] = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID, MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE, MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
                //                    cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
                if (cur != null) {
                    if (cur.moveToFirst()) {
                        int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
                        int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        int photoNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                        int photoTitleIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
                        int photoSizeIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
                        int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
                        int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
                        int picasaIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.PICASA_ID);
                        int totalNum = cur.getCount();
                        MLog.d(TAG, "total photo num = " + totalNum);

                        do {

                            if (cur.getString(photoPathIndex).substring(cur.getString(photoPathIndex).lastIndexOf("/") + 1, cur.getString(photoPathIndex).lastIndexOf(".")).replaceAll(" ", "").length() <= 0) {
                                MLog.d(TAG, "exception photo " + cur.getString(photoPathIndex));
                            } else {


                                String _id = cur.getString(photoIDIndex);
                                String name = cur.getString(photoNameIndex);
                                String path = cur.getString(photoPathIndex);
                                String title = cur.getString(photoTitleIndex);
                                String size = cur.getString(photoSizeIndex);
                                String bucketName = cur.getString(bucketDisplayNameIndex);
                                String bucketId = cur.getString(bucketIdIndex);
                                String picasaId = cur.getString(picasaIdIndex);

                                MLog.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: " + picasaId + " name:" + name + " path:" + path + " title: " + title + " size: " + size + " bucket: " + bucketName + "---");

                                //                    ImageBucket bucket = bucketList.get(bucketId);
                                //                    if (bucket == null) {
                                //                        bucket = new ImageBucket();
                                //                        bucketList.put(bucketId, bucket);
                                //                        bucket.imageList = new ArrayList<TempImageItem>();
                                //                        bucket.bucketName = bucketName;
                                //                    }
                                //                    bucket.count++;
                                ImageItem imageItem = new ImageItem();
                                imageItem.setImageId(_id);
                                imageItem.setImagePath(path);
                                imageItem.setThumbnailPath(thumbnailMap.get(_id));
                                //                    bucket.imageList.add(imageItem);
                                mAllImageItemList.add(imageItem);
                            }
                        } while (cur.moveToNext());
                    }
                } else
                    MLog.d(TAG, "cursor is null");
                if (cur != null) {
                    cur.close();
                }
                long endTime = System.currentTimeMillis();
                MLog.d(TAG, "get image use time: " + (endTime - startTime) + " ms");
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        MLog.d(TAG, " image list size = " + mAllImageItemList.size());
                        updateList();
                    }
                });
            }
        });
    }

    private void updateList() {
        mPickImgGirdAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_complete:
                List<ImageItem> list = mPickImgGirdAdapter.getSelectedItemList();
                //                list.add(item);
                mBeforeSelectedPhotoList.addAll(list);
                EventBus.getDefault().post(new IEvent.PickImageEventRs(mBeforeSelectedPhotoList));
                finish();
                break;
            case R.id.tv_preview:
                launchAlbumsPreviewActivity(-1, mPickImgGirdAdapter.getSelectedItemList());
                break;
        }
    }

    private void launchAlbumsPreviewActivity(int position, List<ImageItem> allImageItemList) {
        //        List<ImageItem> list = ;
        //        mBeforeSelectedPhotoList.addAll(list);
        Intent intent = new Intent(AlbumsActivity.this, AlbumPreviewActivity.class);
        intent.putExtra(CssConstant.BBS.KEY_PHOTO_LIST, (Serializable) allImageItemList);
        intent.putExtra(CssConstant.BBS.KEY_SELECTED_PHOTO_LIST, (Serializable) mPickImgGirdAdapter.getSelectedItemList());
        intent.putExtra(CssConstant.BBS.KEY_IMAGE_POSITION, position);
        intent.putExtra(CssConstant.BBS.KEY_LAUNCH_PREVIEW_FLAG, mCallPage);
        intent.putExtra(CssConstant.BBS.KEY_MAX_SELECTABLE_COUNT, mMaxSelectableCount);
        intent.putExtra(CssConstant.BBS.KEY_BEFORE_SELECTED_PHOTO_LIST, (Serializable) mBeforeSelectedPhotoList);
        startActivity(intent);
    }

    /**
     * EventBus
     * 选择图片发送此事件
     */
    @Subscribe
    public void onEventMainThread(IEvent.PickPreviewImageEvent event) {
        MLog.d(TAG, "%% onEventMainThread");
        if (event != null) {
            List<ImageItem> list = event.getImageItemList();
            MLog.d(TAG, "SELECT LIST SIZE = " + list.size());
            //            mBeforeSelectedPhotoList.addAll(list);
            //            mSelectedImageAdapter.notifyDataSetChanged();
            mPickImgGirdAdapter.updateSelectedList(list);
            if (mPickImgGirdAdapter.getSelectedItemList().size() > 0) {
                setBtnStatus(true, mPickImgGirdAdapter.getSelectedItemList().size());
            } else {
                setBtnStatus(false, 0);
            }
        } else
            MLog.d(TAG, "onEventMainThread event is null");
    }

}
