//package com.cssweb.shankephone.db;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.gateway.model.spservice.Service;
//
///**
// * Created by liwx on 2015/8/24.
// */
//public class ServiceDBManager {
//    private static final String TAG = "ServiceDBManager";
//
//    private Context context;
//
//    public static final String COLUMN_SERVICE_ID = "SERVICE_ID";
//    public static final String COLUMN_SERVICE_NAME = "SERVICE_NAME";
//    public static final String COLUMN_SERVICE_DISPLAY_CATEGORY_ID = "SERVICE_DISPLAY_CATEGORY_ID";
//    public static final String COLUMN_SERVICE_PROVIDER_ID = "SERVICE_PROVIDER_ID";
//    public static final String COLUMN_SERVICE_PROVIDER_NAME = "SERVICE_PROVIDER_NAME";
//    public static final String COLUMN_NFC_SERVICE_YN = "NFC_SERVICE_YN";
//    public static final String COLUMN_REG_NEEDED_YN = "REG_NEEDED_YN";
//    public static final String COLUMN_SERVICE_STATE = "SERVICE_STATE";
//    public static final String COLUMN_SERVIC_EDESC = "SERVIC_EDESC";
//    public static final String COLUMN_APPLET_AID = "APPLET_AID";
//    public static final String COLUMN_TNC = "TNC";
//    public static final String COLUMN_SUBSCRIPTION_PREREQUISITE_DESC = "SUBSCRIPTION_PREREQUISITE_DESC";
//    public static final String COLUMN_SUBSCRIPTION_PROCESS_DESC = "SUBSCRIPTION_PROCESS_DESC";
//    public static final String COLUMN_CONTACT_PHONE_NUM = "CONTACT_PHONE_NUM";
//    public static final String COLUMN_CONTACT_EMAIL = "CONTACT_EMAIL";
//    public static final String COLUMN_CONTACT_URL = "CONTACT_URL";
//    public static final String COLUMN_SP_DEVICE_APP_USE_YN = "SP_DEVICE_APP_USE_YN";
//    public static final String COLUMN_APP_DOWNLOAD_URL = "APP_DOWNLOAD_URL";
//    public static final String COLUMN_APP_PACKAGE_NAME = "APP_PACKAGE_NAME";// 0 is not set, 1 is set
//    public static final String COLUMN_APP_MAIN_IMAGE_URL = "APP_MAIN_IMAGE_URL";
//    public static final String COLUMN_APP_ICON_IMAGE_URL = "APP_ICON_IMAGE_URL";
//    public static final String COLUMN_APP_DETAIL_IMAGE_URL = "APP_DETAIL_IMAGE_URL";
//    public static final String COLUMN_SERVICE_TYPE = "SERVICE_TYPE";
//    public static final String COLUMN_SERVICE_AVAILABLE_YN = "SERVICE_AVAILABLE_YN";
//    public static final String COLUMN_SERVICE_INSTALL_YN = "SERVICE_INSTALL_YN";
//    public static final String COLUMN_FIRST_OPEN_YN = "FIRST_OPEN_YN";
//    public static final String COLUMN_SERVICE_SUBSCRIPTION_STATE = "SERVICE_SUBSCRIPTION_STATE";
//    public static final String COLUMN_LAUNCHER_CLASS = "LAUNCHER_CLASS";
//    public static final String COLUMN_ORDER_NUM = "ORDER_NUM";
//    public static final String COLUMN_APP_VERSION_NAME = "APP_VERSION_NAME";
//    public static final String COLUMN_UPGRADE_MANDATORY_YN = "UPGRADE_MANDATORY_YN";
//    public static final String COLUMN_APP_VERSION_DESC = "APP_VERSION_DESC";
//
//
//    public ServiceDBManager(Context context) {
//        this.context = context;
//    }
//    public String getCreateTableSql(String tableName) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("CREATE TABLE ").append(tableName).append(" (")
//            .append(COLUMN_SERVICE_ID).append(" TEXT PRIMARY KEY,")
//            .append(COLUMN_SERVICE_NAME).append(" TEXT,")
//            .append(COLUMN_SERVICE_DISPLAY_CATEGORY_ID).append(" TEXT,")
//            .append(COLUMN_SERVICE_PROVIDER_ID).append(" TEXT,")
//            .append(COLUMN_SERVICE_PROVIDER_NAME).append(" TEXT,")
//            .append(COLUMN_NFC_SERVICE_YN).append(" TEXT CHECK").append("(").append(COLUMN_NFC_SERVICE_YN).append(" IN (\'Y\',\'N\')) DEFAULT \'N\',")
//            .append(COLUMN_REG_NEEDED_YN).append(" TEXT,")
//            .append(COLUMN_SERVICE_STATE).append(" TEXT,")
//            .append(COLUMN_SERVIC_EDESC).append(" TEXT,")
//            .append(COLUMN_APPLET_AID).append(" TEXT,")
//            .append(COLUMN_TNC).append(" TEXT,")
//            .append(COLUMN_SUBSCRIPTION_PREREQUISITE_DESC).append(" TEXT,")
//            .append(COLUMN_SUBSCRIPTION_PROCESS_DESC).append(" TEXT,")
//            .append(COLUMN_CONTACT_PHONE_NUM).append(" TEXT,")
//            .append(COLUMN_CONTACT_EMAIL).append(" TEXT,")
//            .append(COLUMN_CONTACT_URL).append(" TEXT,")
//            .append(COLUMN_SP_DEVICE_APP_USE_YN).append(" TEXT,")
//            .append(COLUMN_APP_DOWNLOAD_URL).append(" TEXT,")
//            .append(COLUMN_APP_PACKAGE_NAME).append(" TEXT,")
//            .append(COLUMN_APP_MAIN_IMAGE_URL).append(" TEXT,")
//            .append(COLUMN_APP_ICON_IMAGE_URL).append(" TEXT,")
//            .append(COLUMN_APP_DETAIL_IMAGE_URL).append(" TEXT,")
//            .append(COLUMN_SERVICE_TYPE).append(" TEXT,")
//            .append(COLUMN_SERVICE_AVAILABLE_YN).append(" TEXT,")
//            .append(COLUMN_SERVICE_INSTALL_YN).append(" TEXT,")
//            .append(COLUMN_FIRST_OPEN_YN).append(" TEXT,")
//            .append(COLUMN_SERVICE_SUBSCRIPTION_STATE).append(" TEXT,")
//            .append(COLUMN_LAUNCHER_CLASS).append(" TEXT,")
//            .append(COLUMN_ORDER_NUM).append(" INTEGER DEFAULT 9999,")
//            .append(COLUMN_APP_VERSION_NAME).append(" TEXT,")
//            .append(COLUMN_UPGRADE_MANDATORY_YN).append(" TEXT,")
//            .append(COLUMN_APP_VERSION_DESC)
//            .append(" TEXT)");
//        MLog.d(TAG, "Create DB: " + sb.toString());
//        return sb.toString();
//    }
//    public StringBuilder getSelectServiceSQL(String tableName) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("SELECT ").append(COLUMN_SERVICE_ID).append(",")
//            .append(COLUMN_SERVICE_NAME).append(",")
//            .append(COLUMN_SERVICE_DISPLAY_CATEGORY_ID).append(",")
//            .append(COLUMN_SERVICE_PROVIDER_ID).append(",")
//            .append(COLUMN_SERVICE_PROVIDER_NAME).append(",")
//            .append(COLUMN_NFC_SERVICE_YN).append(",")
//            .append(COLUMN_REG_NEEDED_YN).append(",")
//            .append(COLUMN_SERVICE_STATE).append(",")
//            .append(COLUMN_SERVIC_EDESC).append(",")
//            .append(COLUMN_APPLET_AID).append(",")
//            .append(COLUMN_TNC).append(",")
//            .append(COLUMN_SUBSCRIPTION_PREREQUISITE_DESC).append(",")
//            .append(COLUMN_SUBSCRIPTION_PROCESS_DESC).append(",")
//            .append(COLUMN_CONTACT_PHONE_NUM).append(",")
//            .append(COLUMN_CONTACT_EMAIL).append(",")
//            .append(COLUMN_CONTACT_URL).append(",")
//            .append(COLUMN_SP_DEVICE_APP_USE_YN).append(",")
//            .append(COLUMN_APP_DOWNLOAD_URL).append(",")
//            .append(COLUMN_APP_PACKAGE_NAME).append(",")
//            .append(COLUMN_APP_MAIN_IMAGE_URL).append(",")
//            .append(COLUMN_APP_ICON_IMAGE_URL).append(",")
//            .append(COLUMN_APP_DETAIL_IMAGE_URL).append(",")
//            .append(COLUMN_SERVICE_TYPE).append(",")
//            .append(COLUMN_SERVICE_AVAILABLE_YN).append(",")
//            .append(COLUMN_SERVICE_INSTALL_YN).append(",")
//            .append(COLUMN_FIRST_OPEN_YN).append(",")
//            .append(COLUMN_SERVICE_SUBSCRIPTION_STATE).append(",")
//            .append(COLUMN_LAUNCHER_CLASS).append(",")
//            .append(COLUMN_ORDER_NUM).append(",")
//            .append(COLUMN_APP_VERSION_NAME).append(",")
//            .append(COLUMN_UPGRADE_MANDATORY_YN).append(",")
//            .append(COLUMN_APP_VERSION_DESC)
//            .append(" FROM ").append(tableName);
//        return sb;
//    }
//    public ContentValues getServiceContentValues(Service myService) {
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_SERVICE_ID, myService.getServiceId());
//        values.put(COLUMN_SERVICE_NAME, myService.getServiceName());
//        values.put(COLUMN_SERVICE_DISPLAY_CATEGORY_ID, myService.getServiceDisplayCategoryId());
//        values.put(COLUMN_SERVICE_PROVIDER_ID, myService.getServiceProviderId());
//        values.put(COLUMN_SERVICE_PROVIDER_NAME, myService.getServiceProviderName());
//        values.put(COLUMN_NFC_SERVICE_YN, myService.getNfcServiceYn().toUpperCase());
//        values.put(COLUMN_REG_NEEDED_YN, myService.getRegNeededYn());
//        values.put(COLUMN_SERVICE_STATE, myService.getServiceState());
//        values.put(COLUMN_SERVIC_EDESC, myService.getServiceDesc());
//        values.put(COLUMN_APPLET_AID, myService.getAppletAid());
//        values.put(COLUMN_TNC, myService.getTnc());
//        values.put(COLUMN_SUBSCRIPTION_PREREQUISITE_DESC, myService.getSubscriptionPrerequisiteDesc());
//        values.put(COLUMN_SUBSCRIPTION_PROCESS_DESC, myService.getSubscriptionProcessDesc());
//        values.put(COLUMN_CONTACT_PHONE_NUM, myService.getContactPhoneNum());
//        values.put(COLUMN_CONTACT_EMAIL, myService.getContactEmail());
//        values.put(COLUMN_CONTACT_URL, myService.getContactUrl());
//        values.put(COLUMN_SP_DEVICE_APP_USE_YN, myService.getSpDeviceAppUseYn());
//        values.put(COLUMN_APP_DOWNLOAD_URL, myService.getAppDownloadUrl());
//        values.put(COLUMN_APP_PACKAGE_NAME, myService.getAppPackageName());
//        values.put(COLUMN_APP_MAIN_IMAGE_URL, myService.getAppMainImageUrl());
//        values.put(COLUMN_APP_ICON_IMAGE_URL, myService.getAppIconImageUrl());
//        values.put(COLUMN_APP_DETAIL_IMAGE_URL, myService.getAppDetailImageUrl());
//        values.put(COLUMN_SERVICE_TYPE, myService.getServiceType());
//        values.put(COLUMN_SERVICE_AVAILABLE_YN, myService.getServiceAvailableYn());
//        values.put(COLUMN_SERVICE_INSTALL_YN, myService.getServiceInstallYn());
//        values.put(COLUMN_FIRST_OPEN_YN, myService.getFirstOpenYn());
//        values.put(COLUMN_SERVICE_SUBSCRIPTION_STATE, myService.getServiceSubscriptionState());
//        values.put(COLUMN_LAUNCHER_CLASS, myService.getLauncherClass());
//        values.put(COLUMN_ORDER_NUM, myService.getOrderNum());
//        values.put(COLUMN_APP_VERSION_NAME, myService.getAppVersionName());
//        values.put(COLUMN_UPGRADE_MANDATORY_YN, myService.getUpgradeMandatoryYn());
//        values.put(COLUMN_APP_VERSION_DESC, myService.getAppVersionDesc());
//        return values;
//    }
//
//    public Service buildService(Cursor c) {
//        Service myService = new Service();
//        myService.setServiceId(c.getString(0));
//        myService.setServiceName(c.getString(1));
//        myService.setServiceDisplayCategoryId(c.getString(2));
//        myService.setServiceProviderId(c.getString(3));
//        myService.setServiceProviderName(c.getString(4));
//        myService.setNfcServiceYn(c.getString(5));
//        myService.setRegNeededYn(c.getString(6));
//        myService.setServiceState(c.getString(7));
//        myService.setServiceDesc(c.getString(8));
//        myService.setAppletAid(c.getString(9));
//        myService.setTnc(c.getString(10));
//        myService.setSubscriptionPrerequisiteDesc(c.getString(11));
//        myService.setSubscriptionProcessDesc(c.getString(12));
//        myService.setContactPhoneNum(c.getString(13));
//        myService.setContactEmail(c.getString(14));
//        myService.setContactUrl(c.getString(15));
//        myService.setSpDeviceAppUseYn(c.getString(16));
//        myService.setAppDownloadUrl(c.getString(17));
//        myService.setAppPackageName(c.getString(18));
//        myService.setAppMainImageUrl(c.getString(19));
//        myService.setAppIconImageUrl(c.getString(20));
//        myService.setAppDetailImageUrl(c.getString(21));
//        myService.setServiceType(c.getString(22));
//        myService.setServiceAvailableYn(c.getString(23));
//        myService.setServiceInstallYn(c.getString(24));
//        myService.setFirstOpenYn(c.getString(25));
//        myService.setServiceSubscriptionState(c.getString(26));
//        myService.setLauncherClass(c.getString(27));
//        myService.setOrderNum(c.getInt(28));
//        myService.setAppVersionName(c.getString(29));
//        myService.setUpgradeMandatoryYn(c.getString(30));
//        myService.setAppVersionDesc(c.getString(31));
//        MLog.d(TAG, "serviceId = " + myService.getServiceId() + " status = " + myService.getServiceSubscriptionState() + " orderNum = " + myService.getOrderNum());
//        return myService;
//    }
//    public String getDropTableSql(String tableName) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("DROP TABLE IF EXISTS ").append(tableName);
//        MLog.d(TAG, "Drop table: " + sb.toString());
//        return sb.toString();
//    }
//}
