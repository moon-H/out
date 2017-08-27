package com.whl.client.home.ticket;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiSearch;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liwx on 2017/4/8.
 * 附近公交站
 */

public class StationBusInfoFragment extends BaseFragment {
    private static final String TAG = "StationBusInfoFragment";
    private final static String LATLONPOINT = "latLonPoint";
    private final static String TITLENAME = "titleName";
    private final static String STATION_INFO = "station_info";
    private Context mContext;
    private View mRootView;
    private StationBusInfoAdapter mStationGateAdapter;
    private PoiSearch.Query query;// Poi查询条件类
    private LatLonPoint lp = new LatLonPoint(39.992894, 116.337742);// 116.472995,39.993743
    private PoiSearch poiSearch;
    private List<PoiItem> poiItemList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }


    private ExecutorService mExcutor = Executors.newCachedThreadPool();

    public static StationBusInfoFragment getInstance() {
        return new StationBusInfoFragment();
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        MLog.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MLog.d(TAG, "onCreateView");
        if (mRootView == null) {
            MLog.d(TAG, "newCreateView");
            mRootView = inflater.inflate(R.layout.fragment_station_bus, container, false);
            initView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MLog.d(TAG, "onDestroy");
        mRootView = null;
    }

    @Override
    public void onDestroyView() {
        ((ViewGroup) mRootView.getParent()).removeView(mRootView);
        super.onDestroyView();
        MLog.d(TAG, "onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setPoiList(final List<PoiItem> poiItemLists) {
        if (poiItemList != null) {
            poiItemList.clear();
            this.poiItemList.addAll(poiItemLists);
            mStationGateAdapter.notifyDataSetChanged();
        }
    }

    public void initView(View rootView) {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mStationGateAdapter = new StationBusInfoAdapter(mContext, poiItemList);
        recyclerView.setAdapter(mStationGateAdapter);
        mStationGateAdapter.setBusInfoItemClickedListener(new StationBusInfoAdapter.OnBusInfoItemClickedListener() {
            @Override
            public void onBusInfoItemClicked(int position, PoiItem item) {
                LatLonPoint latLonPoint;
                latLonPoint = poiItemList.get(position).getLatLonPoint();
                Intent intent = new Intent(mContext, LocationActivity.class);
                intent.putExtra(STATION_INFO, item.getSnippet());
                intent.putExtra(LATLONPOINT, latLonPoint);
                intent.putExtra(TITLENAME, item.getTitle());
                startActivity(intent);
            }
        });
    }

    /**
     * 清空数据
     */
    public void clearData() {
        if (poiItemList != null) {
            poiItemList.clear();
            mStationGateAdapter.notifyDataSetChanged();
        }
    }
}
