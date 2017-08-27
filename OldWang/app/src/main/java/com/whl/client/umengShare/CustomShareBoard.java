/**
 * 
 */

package com.whl.client.umengShare;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.whl.client.R;
import com.umeng.socialize.Config;
import com.umeng.socialize.bean.SHARE_MEDIA;


/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

    private Activity mActivity;
    private String packageName = "com.tencent.mobileqq";
    private OnShareItemsListener mOnShareItemsListener;

    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
//        mController.getConfig().closeToast();
        Config.IsToastTip = false;
        View rootView = LayoutInflater.from(context).inflate(R.layout.custom_board, null);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.sina).setOnClickListener(this);
        setContentView(rootView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.wechat:
                mOnShareItemsListener.clickShare(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wechat_circle:
                mOnShareItemsListener.clickShare(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.qq:
                if(isApkInstalled(mActivity,packageName) == false) {
                    Toast.makeText(mActivity, "你还没有安装QQ", Toast.LENGTH_SHORT).show();
                    this.dismiss();
                    return;
                }else{
                    mOnShareItemsListener.clickShare(SHARE_MEDIA.QQ);
                }
                break;
            case R.id.sina:
                mOnShareItemsListener.clickShare(SHARE_MEDIA.SINA);
                break;
            default:
                break;
        }
        this.dismiss();
    }

    //判断手机中是否安装QQ或者某个应用
    public static final boolean isApkInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public void setOnShareItemsListener(OnShareItemsListener listener){
        this.mOnShareItemsListener = listener;
    }
    //点击接口
    public interface OnShareItemsListener{
        void clickShare(SHARE_MEDIA platform);
    }

}
