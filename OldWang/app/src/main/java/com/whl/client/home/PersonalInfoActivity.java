//package com.cssweb.shankephone.home;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.app.IEvent;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.WalletGateway;
//import com.cssweb.shankephone.gateway.model.wallet.GetUserHeadPicRs;
//import com.cssweb.shankephone.gateway.model.wallet.UploadUserHeadPicRs;
//import com.cssweb.shankephone.home.bbs.BBSManager;
//import com.cssweb.shankephone.home.pickimage.ImageItem;
//import com.cssweb.shankephone.login.LoginActivity;
//import com.cssweb.shankephone.login.LoginManager;
//import com.cssweb.shankephone.login.register.RegisterThirdUserActivity;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.utils.compress.CustomLuBan;
//import com.cssweb.shankephone.view.TitleBarView;
//import com.cssweb.shankephone.view.glide.GlideCircleTransform;
//
//import org.apache.http.Header;
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.InputStream;
//
///**
// * Created by lenovo on 2016/10/17.
// * 个人信息页面
// */
//public class PersonalInfoActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, View.OnClickListener {
//    private static final String TAG = "PersonalInfoActivity";
//    private TextView mNumber;
//    private ImageView mBindArrow;
//    private ImageView mImgArrow;
//    private BBSManager mBBSManager;
//    private ImageView mImgUserHead;
//    private WalletGateway mWalletGateway;
//    private Uri mCroppedImgUri;
//    private File mUserHeadFile;
//    private InputStream mPhotoInputstream = null;
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_personna_info);
//        MLog.d(TAG, "onCreate");
//        initView();
//        if (LoginManager.hasPhoneNumber(PersonalInfoActivity.this)) {
//            mBindArrow.setVisibility(View.GONE);
//        } else
//            mBindArrow.setVisibility(View.VISIBLE);
//        mBBSManager = new BBSManager(PersonalInfoActivity.this);
//        mWalletGateway = new WalletGateway(PersonalInfoActivity.this);
//        EventBus.getDefault().register(this);
//        getUserHeadPic();
//        displayDefaultHead();
//    }
//
//    private void displayDefaultHead() {
//        Glide.with(PersonalInfoActivity.this).load(R.drawable.icon_my_head).transform(new GlideCircleTransform(PersonalInfoActivity.this)).into(mImgUserHead);
//    }
//
//    private void initView() {
//        TitleBarView mTitleBar = (TitleBarView) findViewById(R.id.title_bar);
//        mTitleBar.setTitle(getString(R.string.title_activity_personal_info));
//        mTitleBar.setOnTitleBarClickListener(this);
//        mNumber = (TextView) findViewById(R.id.tv_phone_number);
//        mBindArrow = (ImageView) findViewById(R.id.ic_bind_arrow);
//        View bindPhoneNumber = findViewById(R.id.ll_register_number);
//        bindPhoneNumber.setOnClickListener(this);
//        findViewById(R.id.lly_user_head).setOnClickListener(this);
//        mImgUserHead = (ImageView) findViewById(R.id.img_user_head);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        checkRegisterNumber();
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_Personal_info));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_Personal_info));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        MLog.d(TAG, "onDestroy");
//        EventBus.getDefault().unregister(this);
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        finish();
//    }
//
//    @Override
//    public void onMenuClicked(View view) {
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (Utils.isFastDoubleClick()) {
//            return;
//        }
//        switch (v.getId()) {
//            case R.id.ll_register_number:
//                if (BizApplication.getInstance().isLoginClient()) {
//                    if (!LoginManager.hasPhoneNumber(PersonalInfoActivity.this)) {
//                        Intent intent = new Intent(this, RegisterThirdUserActivity.class);
//                        startActivity(intent);
//                        //                        showBindPhoneNumberDialog(PersonalInfoActivity.this);
//                    }
//                } else {
//                    launchLoginPage();
//                }
//                break;
//            case R.id.lly_user_head:
//                if (BizApplication.getInstance().isLoginClient()) {
//                    mBBSManager.pickUserHead(PersonalInfoActivity.this, findViewById(R.id.parent));
//                } else {
//                    launchLoginPage();
//                }
//                break;
//        }
//    }
//
//    private void launchLoginPage() {
//        startActivity(new Intent(PersonalInfoActivity.this, LoginActivity.class));
//    }
//
//    private void checkRegisterNumber() {
//        //        MLog.d(TAG, LoginManager.getPhoneNumber(this));
//        if (BizApplication.getInstance().isLoginClient()) {
//            if (TextUtils.isEmpty(LoginManager.getPhoneNumber(this))) {
//                mBindArrow.setVisibility(View.VISIBLE);
//            } else {
//                mBindArrow.setVisibility(View.GONE);
//            }
//            if (LoginManager.hasShankePhoneId(this)) {
//                mNumber.setText(MPreference.getLoginId(this));
//            } else {
//                if (!TextUtils.isEmpty(MPreference.getString(getApplicationContext(), MPreference.THIRD_RIGESTER_PHONENUMBER)))
//                    //                    showBindPhoneNumberDialog(this);
//                    //                else{
//                    mNumber.setText(LoginManager.getPhoneNumber(this));
//                //                }
//            }
//        }
//    }
//
//    /**
//     * EventBus
//     * 选择图片发送此事件
//     */
//    @Subscribe
//    public void onEventMainThread(IEvent.PickUserHeadEvent event) {
//        MLog.d(TAG, "%% onEventMainThread");
//        if (event != null) {
//            ImageItem imageItem = event.getImageItem();
//            cropUserHead(imageItem.getImagePath());
//        } else
//            MLog.d(TAG, "onEventMainThread event is null");
//    }
//
//    /**
//     * c裁剪头像
//     */
//    private void cropUserHead(String orgFilePath) {
//        Uri uri = Uri.fromFile(new File(orgFilePath));
//        File file = Utils.getImgCacheDirectory(PersonalInfoActivity.this, BBSManager.PATH);
//        MLog.d(TAG, "path2= " + file.getPath());
//        mUserHeadFile = new File(file.getPath() + "/" + "userHead" + System.currentTimeMillis() + ".png");
//        mCroppedImgUri = Uri.fromFile(mUserHeadFile);
//        MLog.d(TAG, " location path = " + mUserHeadFile.getPath());
//        cropImageUri(uri, CssConstant.REQUEST_CODE_CROP_PHOTO);
//    }
//
//    //裁剪拍照后得到的图片
//    private void cropImageUri(Uri uri, int requestCode) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", 200);
//        intent.putExtra("outputY", 200);
//        intent.putExtra("scale", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCroppedImgUri);
//        intent.putExtra("return-data", false);
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true); // no face detection
//        intent = Intent.createChooser(intent, "裁剪图片");
//        startActivityForResult(intent, requestCode);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        MLog.d(TAG, "onActivityResult requestCode = " + requestCode);
//        switch (requestCode) {
//            case CssConstant.REQUEST_CODE_TAKE_PHOTO:
//                //拍照完成
//                File captureFile = new File(mBBSManager.getCaptureImageName());
//                if (captureFile.exists()) {
//                    MLog.d(TAG, "fileName = " + mBBSManager.getCaptureImageName());
//                    cropUserHead(mBBSManager.getCaptureImageName());
//                } else {
//                    MLog.d(TAG, "file not exist");
//                }
//                break;
//            case CssConstant.REQUEST_CODE_CROP_PHOTO:
//                //裁剪图片完成
//                uploadUserHeadPic();
//                break;
//        }
//
//    }
//
//    private void uploadUserHeadPic() {
//        showProgress();
//        String thirdPartyYn = LoginManager.hasThirdPartyId(PersonalInfoActivity.this) ? "Y" : "N";
//        String thirdPartyType = null;
//        String userName;
//        if (thirdPartyYn.equalsIgnoreCase("y")) {
//            thirdPartyType = LoginManager.getThirdPartyType(PersonalInfoActivity.this);
//            userName = LoginManager.getThirdPartyID(PersonalInfoActivity.this);
//        } else {
//            userName = LoginManager.getShankePhoneId(PersonalInfoActivity.this);
//
//        }
//        CustomLuBan luBan = CustomLuBan.get(PersonalInfoActivity.this).load(mUserHeadFile);
//        File compressFile = null;
//        try {
//            compressFile = luBan.syncCompress();
//            MLog.d(TAG, "org size = " + mUserHeadFile.length() / 1024 + " new file = " + " size = " + compressFile.length() / 1024 + "k   " + CustomLuBan.get(getApplicationContext()).getImageSize(compressFile.getPath())[0] + " * " + CustomLuBan.get(getApplicationContext()).getImageSize(compressFile.getPath())[1]);
//        } catch (Exception e) {
//            MLog.d(TAG, "uploadUserHeadPic occurError1 ", e);
//            dismissProgress();
//            return;
//        }
//        try {
//            mPhotoInputstream = new FileInputStream(compressFile);
//        } catch (Exception e) {
//            dismissProgress();
//            MLog.d(TAG, "uploadUserHeadPic occurError2 ", e);
//            return;
//        }
//        mWalletGateway.uploadUserPhoto(CssConstant.IMAGE_TYPE_USER_HEAD, thirdPartyYn, thirdPartyType, userName, mPhotoInputstream, new MobileGateway.MobileGatewayListener<UploadUserHeadPicRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgress();
//                CommonToast.onFailed(PersonalInfoActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgress();
//                CommonToast.onHttpFailed(PersonalInfoActivity.this);
//            }
//
//            @Override
//            public void onSuccess(UploadUserHeadPicRs response) {
//                dismissProgress();
//                Glide.with(PersonalInfoActivity.this).load(mCroppedImgUri).transform(new GlideCircleTransform(PersonalInfoActivity.this)).into(mImgUserHead);
//                LocalBroadcastManager.getInstance(PersonalInfoActivity.this).sendBroadcast(new Intent(CssConstant.Action.ACTION_UPDATE_USER_HEAD));
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgress();
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgress();
//                uploadUserHeadPic();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgress();
//                handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    /**
//     * 更新用户头像
//     */
//    private void getUserHeadPic() {
//        if (!BizApplication.getInstance().isLoginClient()) {
//            MLog.d(TAG, "getUserHeadPic not login");
//            displayDefaultHead();
//            return;
//        }
//
//        mWalletGateway.getUserHeadPic(new MobileGateway.MobileGatewayListener<GetUserHeadPicRs>() {
//            @Override
//            public void onFailed(Result result) {
//
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//
//            }
//
//            @Override
//            public void onSuccess(GetUserHeadPicRs response) {
//                if (!TextUtils.isEmpty(response.getImageUrl()))
//                    Glide.with(PersonalInfoActivity.this).load(response.getImageUrl()).transform(new GlideCircleTransform(PersonalInfoActivity.this)).into(mImgUserHead);
//                else
//                    displayDefaultHead();
//            }
//
//            @Override
//            public void onNoNetwork() {
//
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
//
//        });
//    }
//}
