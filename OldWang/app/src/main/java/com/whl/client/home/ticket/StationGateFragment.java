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
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseFragment;
import com.whl.client.gateway.model.singleticket.ExitPoi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Created by liwx on 2017/4/8.
 * 进出口页面
 */

public class StationGateFragment extends BaseFragment {
    private static final String TAG = "StationGateFragment";
    private final static String LATLONPOINT = "latLonPoint";
    private final static String TITLENAME = "titleName";
    private Context mContext;
    private View mRootView;
    private StationGateAdapter mStationGateAdapter;
    private List<ExitPoi> exitList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = getActivity();
    }


    private ExecutorService mExcutor = Executors.newCachedThreadPool();

    public static StationGateFragment getInstance() {
        return new StationGateFragment();
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
            mRootView = inflater.inflate(R.layout.fragment_station_gate, container, false);
            initView(mRootView);
            initData();
        }
        return mRootView;
    }

    public void setPoiListExit(final List<ExitPoi> exitPoiListGet) {
        Collections.sort(exitPoiListGet, new FileComparator());//排序
        if (exitList != null) {
            exitList.clear();
            this.exitList.addAll(exitPoiListGet);
            mStationGateAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 清空数据
     */
    public void clearData() {
        if (exitList != null) {
            exitList.clear();
            mStationGateAdapter.notifyDataSetChanged();
        }
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
    public void onStop() {
        super.onStop();
        MLog.d(TAG, "onStop");
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


    public void initView(View rootView) {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mStationGateAdapter = new StationGateAdapter(mContext, exitList);
        recyclerView.setAdapter(mStationGateAdapter);
        mStationGateAdapter.setGateItemClickedListener(new StationGateAdapter.OnGateItemClickedListener() {
            @Override
            public void onGateItemClicked(int position, ExitPoi item) {
                double ExitLat = exitList.get(position).getExitData().getExitLat();
                double ExitLong = exitList.get(position).getExitData().getExitLong();
                LatLonPoint latLonPoint = new LatLonPoint(ExitLat, ExitLong);
                Intent intent = new Intent(mContext, LocationActivity.class);
                intent.putExtra(LATLONPOINT, latLonPoint);
                intent.putExtra(TITLENAME, item.getExitData().getExitName());
                startActivity(intent);
            }
        });
    }

    private void initData() {

    }

    /**
     * 排序list
     */
    public class FileComparator implements Comparator<ExitPoi> {
        public int compare(ExitPoi file1, ExitPoi file2) {
            return file1.getExitData().getExitName().compareTo(file2.getExitData().getExitName());
        }
    }
}
