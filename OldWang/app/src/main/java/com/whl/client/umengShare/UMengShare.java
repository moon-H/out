package com.whl.client.umengShare;

import android.app.Activity;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

/**
 * Created by liwx on 2016/4/26.
 * 普通页面只由Activity构成的，只调用countPageStart和countPageEnd统计页面和时间。
 * 由Fragment构成的，在FragmentActivity中调用countTimeResume和countTimePause统计时间，在Fragment中调用
 * countPageStart和countPageEnd
 */
public class UMengShare {
    private static final String TAG = "UMengShare";

    public static void countPageAndTimeStart(Activity activity, String page) {
        //        if (BuildConfig.LOG_DEBUG)
        //            return;
        countPageStart(page);
        countTimeResume(activity);
    }

    public static void countPageAndTimeEnd(Activity activity, String page) {
        //        if (BuildConfig.LOG_DEBUG)
        //            return;
        countPageEnd(page);
        countTimePause(activity);
    }

    public static void countTimeResume(Activity activity) {
        //        if (BuildConfig.LOG_DEBUG)
        //            return;
        MobclickAgent.onResume(activity);//统计时长
    }

    public static void countTimePause(Activity activity) {
        //        if (BuildConfig.LOG_DEBUG)
        //            return;
        MobclickAgent.onPause(activity);
    }

    public static void countPageStart(String page) {
        //        if (BuildConfig.LOG_DEBUG)
        //            return;
        MobclickAgent.onPageStart(page);//统计页面
    }

    public static void countPageEnd(String page) {
        //        if (BuildConfig.LOG_DEBUG)
        //            return;
        MobclickAgent.onPageEnd(page);
    }

    /**
     * 友盟分享活动
     */
    public static void shareEvent(Activity activity, String title, String content, String imgUrl, String targetUrl, SHARE_MEDIA platform) {
        UMImage image = new UMImage(activity, imgUrl);
        new ShareAction(activity).setPlatform(platform).withMedia(image).withTitle(title).withText(content).withTargetUrl(targetUrl).share();
    }

    /**
     * 配置平台信息
     */
    public static void configPlatform() {

        //新浪
        PlatformConfig.setSinaWeibo("1560313043", "1c910d5a27222fa089d1bd45927ae832");
        //        微信
        PlatformConfig.setWeixin("wx369ab6b5d13e3283", "bde0aebb527ff1a204c6834f58e338b0");
        //QQ
        PlatformConfig.setQQZone("1104917864", "lCqslST1M3PDYJgR");
    }

}
