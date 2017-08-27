package com.whl.client.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.client.R;

/**
 * Created by lenovo on 2016/9/2.
 */
public class CssProgressDialog extends Dialog {
    //    static Dialog sProgressDialog;
    View sProgress;
    TextView progressText;
    RotateAnimation animation;
    ImageView imgProgress;
    private Context context;
    String loading = "";

    public CssProgressDialog(Context context) {
        super(context, R.style.theme_dialog_alert);
        this.context = context;
        //        sProgressDialog = new Dialog(context, R.style.theme_dialog_alert);
        initAnimtaion();
        sProgress = View.inflate(context, R.layout.progressbar_inverse3, null);
        imgProgress = (ImageView) sProgress.findViewById(R.id.img_progress);

        //        if (!TextUtils.isEmpty(msg)) {
        progressText = (TextView) sProgress.findViewById(R.id.tv_pro_text);
        //            progressText.setText(msg);
        setCancelable(false);
        //        }
        setContentView(sProgress);
        //        sProgressDialog.setCancelable(cancelAble);
        loading = context.getString(R.string.loading);
    }

    public void setContent(String msg) {
        if (!TextUtils.isEmpty(msg))
            progressText.setText(msg);
        else
            progressText.setText(loading);
    }

    private RotateAnimation initAnimtaion() {
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1300);//设置动画持续时间
        animation.setStartOffset(0);//执行前的等待时间
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(-1);//设置重复次数
        return animation;
    }


    public void show() {
        imgProgress.startAnimation(animation);
        super.show();
    }

    public void dismiss() {
        imgProgress.clearAnimation();
        super.dismiss();
    }
}
