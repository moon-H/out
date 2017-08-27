package com.whl.client.preference;


import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.whl.framework.preference.MSharedPreference;
import com.whl.framework.security.DES3;


public class MPreference extends MSharedPreference {
    public final static String IS_SE_PROCEEDING = "isSeProceeding";
    public static final String ICCID = "iccid";
    public final static String CHOICE_CITY_CODE = "choiceCityCode";//地图城市列表选择城市
    public final static String CHOICE_CITY_NAME = "choiceCityName";
    public final static String LOCAL_CITY_CODE = "localCityCode";//订单列表选择城市
    public final static String LOCAL_CITY_NAME = "localCityName";
    public final static String LOCAL_IS_PROMPT = "localisprompt";//是否提示过用户切换城市
    public final static String LOCAL_IS_NOPROMPT = "localisnoprompt";//下次不再提示是否被选中
    public final static String ORDER_SELECT_CITY_CODE = "order_select_city_code";
    public final static String ORDER_SELECT_CITY_NAME = "order_select_city_name";

    public final static String ORDER_CONTROLLER_LAST_CITY_CODE = "order_controller_last_city_code";
    public final static String ORDER_CONTROLLER_LAST_CITY_NAME = "order_controller_last_city_name";

    public final static String FLAG_HOME_SHADE = "flag_home_shade";

    public final static String THIRD_LOGIN_STATUE = "THIRD_LOGIN_STATUE";//10004 10002 10001
    public final static String THIRD_LOGIN_UID = "THIRD_LOGIN_UID";//uid,nickName,gender,iconUrl
    public final static String THIRD_LOGIN_NICKNAME = "THIRD_LOGIN_NICKNAME";
    public final static String THIRD_LOGIN_GENDER = "THIRD_LOGIN_GENDER";
    public final static String THIRD_LOGIN_ICONURL = "THIRD_LOGIN_ICONURL";
    public final static String THIRD_LOGIN_UIDTYPE = "THIRD_LOGIN_UIDTYPE";
    public final static String THIRD_RIGESTER_PHONENUMBER = "THIRD_RIGESTER_PHONENUMBER";

    public final static String VERSION_LINE_CODE = "version_line_code";
    public final static String VERSION_STATION_CODE = "version_station_code";

    public final static String MAP_GET_FROM_DISK = "map_get_from_disk";

    public final static String PUSH_CLIENT_ID = "push_client_id";

    public final static String SHOW_SWITCH_CITY_DIALOG = "show_switch_city_dialog";

    public final static String LAST_LOCATION_CITY_CODE = "last_location_city_code";//最后定位城市

    public final static String GUIDE_VIEW_GUANGZHOU_DISPLAYED = "guide_view_guangzhou_displayed";//广州引导页是否显示过
    public final static String GUIDE_VIEW_CHANGSHA_DISPLAYED = "guide_view_changsha_displayed";//长沙引导页是否显示过

    public final static String USER_TOKEN = "skp_token";//用户token


    /**
     * 删除所有缓存数据
     */
    public static void clearCacheData(Context context) {
        removeAppDownLoadUrl(context);
        removeMobileId(context);
        MSharedPreference.removeCookie(context);
    }

    // -----------------------------------------------------------------
    /**
     * 客户端下载地址
     */
    private final static String APP_DOWNLOAD_URL = "app_download_url";

    /**
     * 保存客户端下载地址
     */
    public static void saveAppDownLoadUrl(Context context, String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(APP_DOWNLOAD_URL, url).commit();
    }

    /**
     * 获取客户端下载地址
     */
    public static String getAppDownLoadUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(APP_DOWNLOAD_URL, "");
    }

    /**
     * 删除客户端下载地址
     */
    public static void removeAppDownLoadUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(APP_DOWNLOAD_URL).commit();
    }

    // -----------------------------------------------------------------
    private static final String LOGIN_INFO = "login_info";
    private static final String LOGIN_ID = "login_id";
    private static final String LOGIN_PWD = "login_pwd";

    /**
     * 保存账号
     */
    public static void saveLoginID(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        String encrypt = DES3.encryptMode(context, DES3.getKeyByte(context), id);
        sharedPreferences.edit().putString(LOGIN_ID, encrypt).commit();
    }

    /**
     * 保存密码
     */
    public static void saveLoginPwd(Context context, String pwd) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        String encrypt = DES3.encryptMode(context, DES3.getKeyByte(context), pwd);
        sharedPreferences.edit().putString(LOGIN_PWD, encrypt).commit();
    }

    /**
     * 获取Login_ID
     */
    public static String getLoginId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(LOGIN_ID, "");
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        return DES3.decryptMode(context, DES3.getKeyByte(context), id);
    }

    /**
     * 获取Login_PWD
     */
    public static String getLoginPwd(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        String pwd = sharedPreferences.getString(LOGIN_PWD, "");
        if (TextUtils.isEmpty(pwd)) {
            return "";
        }
        return DES3.decryptMode(context, DES3.getKeyByte(context), pwd);
    }

    /**
     * 删除Login_PWD
     */
    public static void removeLoginPwd(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(LOGIN_PWD).commit();
    }

    /**
     * 删除Login_ID
     */
    public static void removeLoginID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(LOGIN_ID).commit();
    }

    // ---------------------------------
    private static final String DOWNLOAD_NOT_REMIND = "download_not_remind";
    private static final String DOWNLOAD_NOT_REMIND_VERSION = "download_not_remind_version";

    public static void saveRemindDownloadFlag(Context context, boolean flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(DOWNLOAD_NOT_REMIND, flag).commit();
    }

    public static boolean getRemindDownloadFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(DOWNLOAD_NOT_REMIND, false);
    }

    public static void removeRemindDownloadFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(DOWNLOAD_NOT_REMIND).commit();
    }

    public static void saveNotRemindVersion(Context ctx, String versionName) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(DOWNLOAD_NOT_REMIND_VERSION, versionName).commit();
    }

    public static String getNotRemindVersion(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(DOWNLOAD_NOT_REMIND_VERSION, "");
    }

    public static void removeNotRemindVersion(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(ctx.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(DOWNLOAD_NOT_REMIND_VERSION).commit();
    }

    // ---------------------------------
    private static final String MOBILE_ID = "mobile_id";

    public static void saveMobileId(Context context, String mobileId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(MOBILE_ID, mobileId).commit();
    }

    public static void removeMobileId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(MOBILE_ID).commit();
    }

    public static String getMobileId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getString(MOBILE_ID, "");
    }

    public static final String MASK_MORE_DETAIL = "inbox_detail";
    public static final String MASK_PULLDOWN = "pulldownview";

    /**
     * 隐藏遮罩状态
     */
    public static void hideMask(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, false).commit();
    }

    /**
     * 获取遮罩状态
     */
    public static boolean getMaskEnable(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, true);
    }


    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return pref.getBoolean(key, defValue);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    public static void removeString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }

    /***
     * 删除所有用户信息
     */
    public static void removeLoginInfo(Context context) {
        MPreference.removeLoginID(context);
        MPreference.removeLoginPwd(context);
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(THIRD_LOGIN_STATUE);
        editor.remove(THIRD_LOGIN_UID);
        editor.remove(THIRD_LOGIN_UIDTYPE);
        editor.remove(THIRD_RIGESTER_PHONENUMBER);
        editor.remove(THIRD_LOGIN_NICKNAME);
        editor.remove(THIRD_LOGIN_ICONURL);
        editor.remove(THIRD_LOGIN_GENDER);
        editor.remove(USER_TOKEN);
        editor.apply();
    }

    /***
     * 删除第三方登录信息
     */
    public static void removeThirdLoginInfo(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(THIRD_LOGIN_STATUE);
        editor.remove(THIRD_LOGIN_UID);
        editor.remove(THIRD_LOGIN_UIDTYPE);
        editor.remove(THIRD_RIGESTER_PHONENUMBER);
        editor.remove(THIRD_LOGIN_NICKNAME);
        editor.remove(THIRD_LOGIN_ICONURL);
        editor.remove(THIRD_LOGIN_GENDER);
        editor.apply();
    }

    /***
     * 删除所有用户信息
     */
    public static void removeShankePhoneLoginInfo(Context context) {
        MPreference.removeLoginID(context);
        MPreference.removeLoginPwd(context);
    }

}
