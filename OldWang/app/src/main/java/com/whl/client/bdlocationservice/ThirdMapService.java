package com.whl.client.bdlocationservice;

import android.content.Context;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.whl.framework.utils.MLog;
import com.whl.client.gateway.model.singleticket.ExitData;
import com.whl.client.gateway.model.singleticket.ExitPoi;
import com.whl.client.gateway.model.singleticket.StationCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 第三方地图相关
 */
public class ThirdMapService {
    private static final String TAG = "ThirdMapService";
    private static Context sContext;
    private static ThirdMapService sLocationService;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    //--------公交路径规划
    private RouteSearch mRouteSearch;
    //--------公交路径规划
    private OnGetBusPathListener mOnGetBusPathListener;

    //Poi搜索相关
    private PoiSearch mPoiSearch;
    private PoiSearch.Query mPoiSearchQuery;
    private int mGetPoiFlag = 0;
    private List<ExitPoi> mExitPoiArrayList = new ArrayList<>();

    public static ThirdMapService getInstance(Context context) {
        if (sLocationService == null) {
            sLocationService = new ThirdMapService();
        }
        sContext = context;
        return sLocationService;
    }

    /**
     * 开始定位
     **/
    public void startLocation(AMapLocationListener locationListener) {
        MLog.d(TAG, "startLocation");
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(sContext);
        }
        mLocationClient.stopLocation();
        mLocationOption = new AMapLocationClientOption();
        //        mLocationOption.setOnceLocationLatest(true);
        //        mLocationOption.setOnceLocation(true);
        mLocationOption.setInterval(1000);
        mLocationOption.setGpsFirst(true);
        mLocationOption.setLocationCacheEnable(false);
        //设置定位监听
        mLocationClient.setLocationListener(locationListener);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //        }
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        MLog.d(TAG, "stopLocation");
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }

    }

    /**
     * 公交路径规划
     */
    public void busRouteQuery(double startLat, double startLon, double endLat, double endLon, String cityCode) {
        mRouteSearch = new RouteSearch(sContext);
        LatLonPoint mStartPoint = new LatLonPoint(startLat, startLon);//起点 五道口，116.337742,39.992894
        LatLonPoint mEndPoint = new LatLonPoint(endLat, endLon);//终点 魏公村，116.32322,39.957904
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, cityCode, 0);
        mRouteSearch.setRouteSearchListener(mOnRouteSearchListener);
        mRouteSearch.calculateBusRouteAsyn(query);
    }

    private RouteSearch.OnRouteSearchListener mOnRouteSearchListener = new RouteSearch.OnRouteSearchListener() {
        @Override
        public void onBusRouteSearched(BusRouteResult result, int errorCode) {
            if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
                if (result != null && result.getPaths() != null) {
                    if (result.getPaths().size() > 0) {
                        MLog.d(TAG, "path size= " + result.getPaths().size());
                        //                        mBusRouteResult = result;
                        //                        BusResultListAdapter mBusResultListAdapter = new BusResultListAdapter(mContext, mBusRouteResult);
                        //                        mBusResultList.setAdapter(mBusResultListAdapter);
                        BusPath busPath = result.getPaths().get(0);//第一条应该是地铁路径规划
                        if (mOnRouteSearchListener != null) {
                            mOnGetBusPathListener.onGetBusPathSuccess(busPath, result);
                        }
                    } else if (result.getPaths() == null) {
                        MLog.d(TAG, " no route path. errorCode=" + errorCode);
                        if (mOnRouteSearchListener != null) {
                            mOnGetBusPathListener.onGetBusPathFailed(errorCode);
                        }
                    }
                } else {
                    MLog.d(TAG, " no search result. errorCode=" + errorCode);
                    if (mOnRouteSearchListener != null) {
                        mOnGetBusPathListener.onGetBusPathFailed(errorCode);
                    }
                }
            } else {
                MLog.d(TAG, " search bus route failed. errorCode=" + errorCode);
                if (mOnRouteSearchListener != null) {
                    mOnGetBusPathListener.onGetBusPathFailed(errorCode);
                }
            }
        }

        @Override
        public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

        }

        @Override
        public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

        }

        @Override
        public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

        }
    };

    public void setOnGetBusPathListener(OnGetBusPathListener listener) {
        mOnGetBusPathListener = listener;
    }

    /**
     * 获取公交路径回调
     */
    public interface OnGetBusPathListener {
        void onGetBusPathSuccess(BusPath busPath, BusRouteResult busRouteResult);

        void onGetBusPathFailed(int errorCode);
    }

    public float getDistance(double sLat, double sLong, double eLat, double eLong) {
        //        MLog.d(TAG, "name = " + sLat + " " + sLong + " " + eLat + " " + eLong);
        LatLng start = new LatLng(sLat, sLong);
        LatLng end = new LatLng(eLat, eLong);
        return AMapUtils.calculateLineDistance(start, end);
    }

    /**
     * 获取地铁出口POI信息
     */
    public void getExitPoi(final List<ExitData> exitDataList, final OnGetExitPoiListener listener) {
        if (exitDataList != null && exitDataList.size() > 0 && listener != null) {
            mGetPoiFlag = 0;
            mExitPoiArrayList.clear();
            for (final ExitData exitData : exitDataList) {
                //Poi查询条件类 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
                mPoiSearchQuery = new PoiSearch.Query("", "050000|060100|060101|060102|060103", "");
                mPoiSearchQuery.setPageSize(10);// 设置每页最多返回多少条poiitem
                mPoiSearchQuery.setPageNum(0);// 设置查第一页
                LatLonPoint lp = new LatLonPoint(exitData.getExitLat(), exitData.getExitLong());

                mPoiSearch = new PoiSearch(sContext, mPoiSearchQuery);
                // 设置搜索区域为以lp点为圆心，其周围5000米范围
                mPoiSearch.setBound(new PoiSearch.SearchBound(lp, 5000, true));//
                mPoiSearch.searchPOIAsyn();// 异步搜索
                mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                    @Override
                    public void onPoiSearched(PoiResult poiResult, int resultCode) {
                        MLog.d(TAG, "onPoiSearched result code = " + resultCode);
                        if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
                            mGetPoiFlag++;
                            if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                                List<PoiItem> poiItemList = poiResult.getPois();
                                MLog.d(TAG, "poiItemList" + poiItemList);
                                MLog.d(TAG, "poiItemListSize" + poiItemList.size());
                                if (poiItemList != null && poiItemList.size() > 0) {
                                    ExitPoi exitPoi = new ExitPoi();
                                    exitPoi.setExitData(exitData);
                                    List<PoiItem> poiItems = new ArrayList<>();
                                    exitPoi.setPoiItemList(poiItems);
                                    for (int i = 0; i < poiItemList.size(); i++) {
                                        poiItems.add(poiItemList.get(i));
                                    }
                                    mExitPoiArrayList.add(exitPoi);
                                }
                            }
                            MLog.d(TAG, "mGetPoiFlag = " + mGetPoiFlag);
                            if (mGetPoiFlag == exitDataList.size()) {
                                listener.onGetExitPoiComplete(mExitPoiArrayList);
                            }
                        }

                    }

                    @Override
                    public void onPoiItemSearched(PoiItem poiItem, int i) {

                    }
                });
            }
        }
    }

    /**
     * 获取地铁出口POI信息
     */
    public void getExitBusStation(final StationCode stationCode, final OnGetExitPoiListener listener) {
        if (stationCode != null && listener != null) {
            mExitPoiArrayList.clear();
            //Poi查询条件类 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
            mPoiSearchQuery = new PoiSearch.Query("", "150700", "");
            mPoiSearchQuery.setPageSize(30);// 设置每页最多返回多少条poiitem
            mPoiSearchQuery.setPageNum(0);// 设置查第一页
            LatLonPoint lp = new LatLonPoint(stationCode.getStationLat(), stationCode.getStationLong());

            mPoiSearch = new PoiSearch(sContext, mPoiSearchQuery);
            // 设置搜索区域为以lp点为圆心，其周围5000米范围
            mPoiSearch.setBound(new PoiSearch.SearchBound(lp, 1000, true));//
            mPoiSearch.searchPOIAsyn();// 异步搜索
            mPoiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
                @Override
                public void onPoiSearched(PoiResult poiResult, int resultCode) {
                    MLog.d(TAG, "onPoiSearched result code = " + resultCode);
                    if (resultCode == AMapException.CODE_AMAP_SUCCESS) {
                        if (poiResult != null && poiResult.getQuery() != null) {// 搜索poi的结果
                            List<PoiItem> poiItemList = poiResult.getPois();
                            listener.onGetExitBusStationComplete(poiItemList);
                        }
                    }

                }

                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }
            });
        }
    }

    public interface OnGetExitPoiListener {
        /**
         * 获取出口周边POI
         */
        void onGetExitPoiComplete(List<ExitPoi> poiItemList);

        /**
         * 获取周边公交站
         */
        void onGetExitBusStationComplete(List<PoiItem> poiItemList);

    }

}
