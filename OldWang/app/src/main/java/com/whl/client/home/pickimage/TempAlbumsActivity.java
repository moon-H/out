package com.whl.client.home.pickimage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.app.CommonToast;
import com.whl.client.app.CssConstant;

import java.util.ArrayList;
import java.util.List;


/**
 * 入相册显示所有图片的界面
 */
public class TempAlbumsActivity extends BaseActivity {
    private static final String TAG = "TempAlbumsActivity";

    //显示手机里的所有图片的列表控件
    private GridView gridView;
    //当手机里没有图片时，提示用户没有图片的控件
    private TextView tv;
    //gridView的adapter
    private AlbumGridViewAdapter gridImageAdapter;
    //完成按钮
    private Button okButton;
    // 返回按钮
    private Button back;
    // 取消按钮
    private Button cancel;
    private Intent intent;
    // 预览按钮
    private Button preview;
    private Context mContext;
    private ArrayList<TempImageItem> dataList;
    private AlbumHelper helper;
    public static List<ImageBucket> contentList;
    public static Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MLog.d(TAG, "onCreate");
        setContentView(R.layout.plugin_camera_album);
        //        PublicWay.activityList.add(this);
        BizApplication.getInstance().addActivity(this);
        mContext = this;
        //注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
        IntentFilter filter = new IntentFilter("data.broadcast.action");
        registerReceiver(broadcastReceiver, filter);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plugin_camera_no_pictures);
        init();
        initListener();
        //这个函数主要用来控制预览和完成按钮的状态
        isShowOkBt();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            MLog.d(TAG, "onReceive action = " + action);
            //mContext.unregisterReceiver(this);
            gridImageAdapter.notifyDataSetChanged();
        }
    };

    // 预览按钮的监听
    private class PreviewListener implements OnClickListener {
        public void onClick(View v) {
            MLog.d(TAG, "PreviewListener  onclick");
            //            if (PickPhotoManager.tempSelectBitmap.size() > 0) {
            //                intent.putExtra("position", "1");
            //                intent.setClass(AlbumActivity.this, GalleryActivity.class);
            //                startActivity(intent);
            //            }
        }

    }

    // 完成按钮的监听
    private class AlbumSendListener implements OnClickListener {
        public void onClick(View v) {
            MLog.d(TAG, "AlbumSendListener  onclick");
            //            overridePendingTransition(R.anim.activity_translate_in, R.anim.activity_translate_out);
            //            intent.setClass(mContext, MainActivity.class);
            //            startActivity(intent);
            //            finish();
        }

    }

    // 返回按钮监听
    private class BackListener implements OnClickListener {
        public void onClick(View v) {
            MLog.d(TAG, "BackListener  onclick");
            //            intent.setClass(AlbumActivity.this, ImageFile.class);
            //            startActivity(intent);
        }
    }

    // 取消按钮的监听
    private class CancelListener implements OnClickListener {
        public void onClick(View v) {
            MLog.d(TAG, "CancelListener  onclick");
            //            PickPhotoManager.tempSelectBitmap.clear();
            //            intent.setClass(mContext, MainActivity.class);
            //            startActivity(intent);
        }
    }


    // 初始化，给一些对象赋值
    private void init() {
        helper = AlbumHelper.getHelper();
        helper.init(getApplicationContext());

        contentList = helper.getImagesBucketList(false);
        dataList = new ArrayList<TempImageItem>();
        for (int i = 0; i < contentList.size(); i++) {
            dataList.addAll(contentList.get(i).imageList);
        }
        MLog.d(TAG, "### PATH = " + dataList.get(0).getImagePath());
        MLog.d(TAG, "### PATH = " + dataList.get(0).getThumbnailPath());

        back = (Button) findViewById(R.id.back);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new CancelListener());
        back.setOnClickListener(new BackListener());
        preview = (Button) findViewById(R.id.preview);
        preview.setOnClickListener(new PreviewListener());
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList, PickPhotoManager.getSelectedBmpList());
        gridView.setAdapter(gridImageAdapter);
        tv = (TextView) findViewById(R.id.myText);
        gridView.setEmptyView(tv);
        okButton = (Button) findViewById(R.id.ok_button);
        okButton.setText(getString(R.string.complete));
    }

    private void initListener() {

        gridImageAdapter.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(final ToggleButton toggleButton, int position, boolean isChecked, Button chooseBt) {
                if (PickPhotoManager.getSelectedBmpList().size() >= CssConstant.BBS.IMG_MAX_NUM) {
                    toggleButton.setChecked(false);
                    chooseBt.setVisibility(View.GONE);
                    if (!removeOneData(dataList.get(position))) {
                        CommonToast.toast(getApplicationContext(), "123123");
                        //                        Toast.makeText(AlbumActivity.this, Res.getString("only_choose_num"), 200).show();
                    }
                    return;
                }
                if (isChecked) {
                    chooseBt.setVisibility(View.VISIBLE);
                    PickPhotoManager.saveSelectedBmp(dataList.get(position));
                    okButton.setText(getString(R.string.complete));
                } else {
                    PickPhotoManager.remove(dataList.get(position));
                    chooseBt.setVisibility(View.GONE);
                    okButton.setText(getString(R.string.complete) + "(" + PickPhotoManager.getSelectedBmpList().size() + "/" + CssConstant.BBS.IMG_MAX_NUM + ")");
                }
                isShowOkBt();
            }
        });

        okButton.setOnClickListener(new AlbumSendListener());

    }

    private boolean removeOneData(TempImageItem imageItem) {
        if (PickPhotoManager.getSelectedBmpList().contains(imageItem)) {
            PickPhotoManager.remove(imageItem);
            okButton.setText(getString(R.string.complete) + "(" + PickPhotoManager.getSelectedBmpList().size() + "/" + CssConstant.BBS.IMG_MAX_NUM + ")");
            return true;
        }
        return false;
    }

    public void isShowOkBt() {
        if (PickPhotoManager.getSelectedBmpList().size() > 0) {
            okButton.setText(getString(R.string.complete) + "(" + PickPhotoManager.getSelectedBmpList().size() + "/" + CssConstant.BBS.IMG_MAX_NUM + ")");
            preview.setPressed(true);
            okButton.setPressed(true);
            preview.setClickable(true);
            okButton.setClickable(true);
            okButton.setTextColor(Color.WHITE);
            preview.setTextColor(Color.WHITE);
        } else {
            okButton.setText(getString(R.string.complete) + "(" + PickPhotoManager.getSelectedBmpList().size() + "/" + CssConstant.BBS.IMG_MAX_NUM + ")");
            preview.setPressed(false);
            preview.setClickable(false);
            okButton.setPressed(false);
            okButton.setClickable(false);
            okButton.setTextColor(Color.parseColor("#E1E0DE"));
            preview.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        MLog.d(TAG, "onKeyDown");
        //        if (keyCode == KeyEvent.KEYCODE_BACK) {
        //            intent.setClass(AlbumActivity.this, ImageFile.class);
        //            startActivity(intent);
        //        }
        return false;

    }

    @Override
    protected void onRestart() {
        isShowOkBt();
        super.onRestart();
    }
}
