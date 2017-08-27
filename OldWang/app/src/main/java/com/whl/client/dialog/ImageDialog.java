package com.whl.client.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.gateway.model.Event;

/**
 * Created by liwx on 2017/6/20.
 * 用于展示图片
 */

public class ImageDialog extends Dialog implements View.OnClickListener {
    private static final String TAG = "ImageDialog";
    private Activity mActivity;
    private ImageView mImgSnapshot;
    private ImageView mBtnCancel;
    private String mEventUrl;

    public ImageDialog(Activity activity) {
        super(activity, R.style.DialogTheme);
        init(activity);
    }

    private void init(Activity activity) {
        setContentView(R.layout.image_dialog);
        mActivity = activity;
        mImgSnapshot = (ImageView) findViewById(R.id.img_event);
        mBtnCancel = (ImageView) findViewById(R.id.btn_cancel);
        mBtnCancel.setOnClickListener(this);
        mImgSnapshot.setOnClickListener(this);
        setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.img_event:
                Event event = new Event();
                event.setEventUrl(mEventUrl);
                dismiss();
                break;
        }
    }

    /**
     * 展示活动图片
     *
     * @param snapshotUrl 活动快照
     * @param eventUrl    活动链接
     */
    public void displayImage(@NonNull String snapshotUrl, @NonNull String eventUrl) {
        mEventUrl = eventUrl;

        Glide.with(mActivity).load(snapshotUrl).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                MLog.d(TAG, " ### onException  ");
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                MLog.d(TAG, " ### onResourceReady ");
                return false;
            }
        }).into(mImgSnapshot);
        show();
    }
}
