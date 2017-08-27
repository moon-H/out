//package com.cssweb.shankephone.db;
//
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.cssweb.framework.db.BaseSQLiteOpenHelper;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.shankephone.gateway.model.spservice.Service;
//
//import org.litepal.crud.DataSupport;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class SpServiceDB extends BaseSQLiteOpenHelper {
//
//    private static final String TAG = "SpServiceDB";
//
//    private static final String DB_TABLE_NAME = "tblMyService";
//    private static final String DB_NAME = "myservice.db";
//    private static final int DB_VERSION = 15;
//
//    private ServiceDBManager mServiceDBManager;
//
//    public SpServiceDB(Context context) {
//        super(context, DB_NAME, null, DB_VERSION);
//        mServiceDBManager = new ServiceDBManager(context);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        MLog.d(TAG, "onUpgrade");
//        db.execSQL(mServiceDBManager.getDropTableSql(DB_TABLE_NAME));
//        onCreate(db);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        MLog.d(TAG, "onCreate");
//
//        db.execSQL(mServiceDBManager.getCreateTableSql(DB_TABLE_NAME));
//
//    }
//
//    public synchronized List<Service> getMyServiceList(String nfcYn) {
//        MLog.d(TAG, "getMyServiceList");
//        ArrayList<Service> myServiceList = new ArrayList<>();
//        myServiceList.addAll(DataSupport.where("nfcServiceYn = ?", nfcYn).order("orderNum asc").find(Service.class));
//        MLog.d(TAG, "myServiceListSize = " + myServiceList.size());
//        return myServiceList;
//    }
//
//    public Service getMyService(String serviceId) {
//        MLog.d(TAG, "getNoNfcService : " + serviceId);
//        List<Service> list = DataSupport.where("serviceId = ?", serviceId).find(Service.class);
//        if (list != null && list.size() > 0) {
//            MLog.d(TAG, "getMyService list size = " + list.size());
//            return list.get(0);
//        } else {
//            MLog.d(TAG, "getMyService list is null  ");
//            return null;
//        }
//    }
//
//    public synchronized boolean insertMyService(Service myService) {
//        MLog.d(TAG, "insertOrUpdateMyService: " + myService.toString());
//        if (getMyService(myService.getServiceId()) != null) {
//            MLog.e(TAG, "This service is already exist!");
//            return false;
//        }
//        boolean isSaved = myService.save();
//        MLog.d(TAG, "insert rows: " + isSaved);
//        return isSaved;
//    }
//
//    public synchronized int updateMyservice(Service myService) {
//        Service localService = getMyService(myService.getServiceId());
//        myService.setOrderNum(localService.getOrderNum());
//        MLog.d(TAG, "updateMyservice = " + myService.toString());
//        int rows = myService.updateAll("serviceId = ? ", myService.getServiceId());
//        MLog.d(TAG, "updateMyservice rows = " + rows);
//        return rows;
//    }
//
//    public synchronized int removeMyService(String serviceId) {
//        MLog.d(TAG, "removeMyService: Service Id=" + serviceId);
//        Service service = getMyService(serviceId);
//        int rows = -1;
//        if (service != null) {
//            rows = service.delete();
//        }
//        MLog.d(TAG, "removeMyService: rows=" + rows);
//        return rows;
//    }
//
//    public synchronized int removeMyServiceByType(String nfcYn) {
//        MLog.d(TAG, "removeMyService: nfcYn=" + nfcYn);
//        List<Service> serviceList = getMyServiceList(nfcYn);
//        int rows = -1;
//        if (serviceList != null && serviceList.size() > 0) {
//            for (Service service : serviceList) {
//                rows = service.delete();
//                MLog.d(TAG, "removeMyServiceByType row= " + rows);
//            }
//        }
//        return rows;
//    }
//
//    public synchronized int removeAllItems() {
//        MLog.d(TAG, "removeAllItems");
//        int rows = DataSupport.deleteAll(Service.class);
//        MLog.d(TAG, "removeAllItems rows = " + rows);
//        return rows;
//    }
//
//    public synchronized void sortLocalService(Service service) {
//        int rows = service.updateAll("serviceId = ? ", service.getServiceId());
//        MLog.d(TAG, "sortLocalService rows = " + rows);
//    }
//
//
//}
