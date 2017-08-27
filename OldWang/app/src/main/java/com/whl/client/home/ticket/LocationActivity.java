package com.whl.client.home.ticket;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.CommonToast;
import com.whl.client.view.TitleBarView;

/**
 * Created by lenovo on 2017/6/20.
 * 高德，进出口，公交站详情
 */

public class LocationActivity extends Activity
        implements GeocodeSearch.OnGeocodeSearchListener, View.OnClickListener, AMap.OnMarkerClickListener, TitleBarView.OnTitleBarClickListener {
    private final static String TAG = "LocationActivity";
    private final static String LATLONPOINT = "latLonPoint";
    private final static String TITLENAME = "titleName";
    private final static String STATION_INFO = "station_info";
    private GeocodeSearch geocoderSearch;
    private String addressName;
    private AMap aMap;
    private MapView mapView;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private Marker regeoMarker;
    private TitleBarView mTitleBarView;
    private String mTitleName;
    private TextView mTvDetail;
    private String mStationInfo = null;
    private ScrollView mSlBus_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence_new);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        mSlBus_info = (ScrollView) findViewById(R.id.sv_bus_info);
        mTvDetail = (TextView) findViewById(R.id.tv_result);
        if (!TextUtils.isEmpty((CharSequence) getIntent().getExtras().get(STATION_INFO))) {
            mStationInfo = (String) getIntent().getExtras().get(STATION_INFO);
            mTvDetail.setText(mStationInfo);
        } else {
            mSlBus_info.setVisibility(View.GONE);
        }
        latLonPoint = (LatLonPoint) getIntent().getExtras().get(LATLONPOINT);
        MLog.d(TAG, "latLonPoint:" + latLonPoint);
        mTitleName = (String) getIntent().getExtras().get(TITLENAME);
        mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setTitle(mTitleName);
        mTitleBarView.setOnTitleBarClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);
//            aMap.setOnInfoWindowClickListener(listener);
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        if (latLonPoint != null) {
            getAddress(latLonPoint);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                MarkerOptions marker;
                if (mTitleName != null) {
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icon_map_location));
                    marker = new MarkerOptions()
                            .position(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()))
                            .title(mTitleName);
                    aMap.addMarker(marker);//设置title，点击的时候显示
                }
//                addressName = result.getRegeocodeAddress().getFormatAddress()
//                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertToLatLng(latLonPoint), 30));
//                regeoMarker.setPosition(convertToLatLng(latLonPoint));
//                CommonToast.toast(LocationActivity.this, addressName);
            } else {
                CommonToast.toast(LocationActivity.this, "没数据");
            }
        } else {
            CommonToast.toast(this, rCode + "");
        }
    }

    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.isInfoWindowShown()) {
            if (!TextUtils.isEmpty(mStationInfo)) {
                mSlBus_info.setVisibility(View.VISIBLE);
            } else {
                mSlBus_info.setVisibility(View.GONE);
            }
            marker.showInfoWindow();
        } else {
            mSlBus_info.setVisibility(View.GONE);
            marker.hideInfoWindow();
        }
        return true;
    }

    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {

    }
}