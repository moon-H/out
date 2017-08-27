package com.whl.framework.utils;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;


public class DeviceInfo {

    /**
     * 此方法返回imsi
     *
     * @return
     */
    public static String getICCID(Context context) {
        //        String iccid = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        //        if (TextUtils.isEmpty(iccid)) {
        //            return "";
        //        }
        //        return iccid.toUpperCase();
        // return "89860111111111111144";
        String imsi = getImsiReal(context);
        if (TextUtils.isEmpty(imsi))
            return "";
        else if (imsi.length() / 2 == 0)
            return imsi;
        else
            return "0" + imsi;
    }

    /**
     * Returns the device model name.
     *
     * @return
     */
    public static String getDeviceModelName() {
        String model = android.os.Build.MODEL;
        if (TextUtils.isEmpty(model))
            return "";
        return model;
    }

    /**
     * Returns the IMEI.
     *
     * @return
     */
    public static String getIMEI(Context context) {
        String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (TextUtils.isEmpty(imei))
            return "";
        return imei;
    }

    /**
     * 此方法返回ICCID
     *
     * @return
     */
    public static String getIMSI(Context context) {
        //        String imsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        //        if (TextUtils.isEmpty(imsi))
        //            return "";
        //        return imsi;
        String iccid = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        if (TextUtils.isEmpty(iccid)) {
            return "";
        }
        return iccid.toUpperCase();
    }

    /**
     * Returns the  Real IMSI
     *
     * @return
     */
    public static String getImsiReal(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (TextUtils.isEmpty(imsi))
            return "";
        return imsi;

    }

    /**
     * Returns the Real ICCID
     *
     * @return
     */
    public static String getIccidReal(Context context) {
        String iccid = ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
        if (TextUtils.isEmpty(iccid)) {
            return "";
        }
        return iccid.toUpperCase();
    }

    /**
     * Returns the versionCode.
     *
     * @param context
     * @return AppVersionCode
     */
    public static int getAppVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }

    /**
     * Returns the versionName.
     *
     * @param context
     * @return AppVersionName
     */
    public static String getAppVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionName;
    }

    public static String getSignature(Context context) {
        String signature = "";
        android.content.pm.Signature[] callerSigs = getSignature(context, context.getApplicationInfo().uid);
        try {
            signature = HexConverter.bytesToHexString(MessageDigest.getInstance("SHA1").digest(callerSigs[0].toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static String getSignatureMD5(Context context) {
        String signature = "";
        android.content.pm.Signature[] callerSigs = getSignature(context, context.getApplicationInfo().uid);
        try {
            signature = HexConverter.bytesToHexString(MessageDigest.getInstance("MD5").digest(callerSigs[0].toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static android.content.pm.Signature[] getSignature(Context context, int uid) {
        PackageManager pm = context.getPackageManager();
        String[] packages = pm.getPackagesForUid(uid);
        if (packages != null) {
            {
                try {
                    return pm.getPackageInfo(packages[0], PackageManager.GET_SIGNATURES).signatures;
                } catch (NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    public static int getDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        Class<?> localClass;
        try {
            localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int statusBarHeight = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            statusHeight = context.getResources().getDimensionPixelSize(statusBarHeight);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static boolean isNfcAvailable(Context context) {
        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter nfcAdapter = manager.getDefaultAdapter();
        return nfcAdapter == null ? false : true;
    }

    public static boolean isOMApiAvailable() {
        // org.simalliance.openmobileapi.SEService;
        // org.simalliance.openmobileapi.Session;
        // org.simalliance.openmobileapi.Channel;
        // org.simalliance.openmobileapi.Reader;
        Class<?> SEService = null;
        Class<?> session = null;
        Class<?> channel = null;
        Class<?> reader = null;

        try {
            SEService = Class.forName("org.simalliance.openmobileapi.SEService");
            session = Class.forName("org.simalliance.openmobileapi.Session");
            channel = Class.forName("org.simalliance.openmobileapi.Channel");
            reader = Class.forName("org.simalliance.openmobileapi.Reader");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        ;
        if (SEService != null && session != null && channel != null && reader != null) {
            return true;
        }
        return false;
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> netIEnumeration = NetworkInterface.getNetworkInterfaces(); netIEnumeration.hasMoreElements(); ) {
                NetworkInterface networkInterface = netIEnumeration.nextElement();
                for (Enumeration<InetAddress> netEnumeration = networkInterface.getInetAddresses(); netEnumeration.hasMoreElements(); ) {
                    InetAddress inetAddress = netEnumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        String ipAddress = inetAddress.getHostAddress();
                        if (TextUtils.isEmpty(ipAddress)) {
                            return "";
                        }
                        return ipAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";

    }

    public static int getNavigationBarHeight(Context context) {
        int height = -1;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId); // 获取高度
        }
        return height;
    }

    public static boolean hasNavigationBar(Context context) {
        Resources resources = context.getResources();
        boolean hasNavigationBar = false;
        int resourceId = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId > 0) {
            hasNavigationBar = resources.getBoolean(resourceId);
        }
        return hasNavigationBar;
    }

    public static boolean hasSimCard(Context context) {
        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int simState = telMgr.getSimState();
        boolean result = true;
        switch (simState) {
            case TelephonyManager.SIM_STATE_ABSENT:
                result = false; // 没有SIM卡
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result = false;
                break;
        }
        MLog.d("DeviceInfo", "hasSimCard = " + result);
        return result;
    }
    /**
     * 是否root
     */
    public static boolean isRooted() {
        boolean bool = false;
        try {
            if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
            MLog.d("DeviceInfo", "是否root = " + bool);
        } catch (Exception e) {

        }
        return bool;
    }
}
