//package com.cssweb.shankephone.home.ticket;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.CityCode;
//import com.cssweb.shankephone.gateway.model.singleticket.GetMetroMapListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMap;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMapCheck;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.TitleBarView;
//import com.cssweb.shankephone.view.XListView;
//import com.cssweb.shankephone.view.ptr.PtrClassicFrameLayout;
//import com.cssweb.shankephone.view.ptr.PtrDefaultHandler;
//import com.cssweb.shankephone.view.ptr.PtrFrameLayout;
//import com.cssweb.shankephone.view.ptr.PtrHandler;
//
//import org.apache.http.Header;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by liwx on 2015/11/2.
// */
//public class STSelectCityActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, AdapterView.OnItemClickListener, XListView.IXListViewListener, View.OnClickListener {
//    private static final String TAG = "STHomeActivity";
//
//    public static final int SELECT_CITY_OK = 100;
//    private static final int FLAG_UPDATE_STATION_LIST = 101;
//    public static final String CITY = "city";
//
//    private XListView mCityListView;
//
//    private ArrayList<MetroMap> mCityList = new ArrayList<>();
//
//    private CityListAdapter mCityListAdapter;
//    private CityCode mCNCityCode;
//    private STGateway mSTGateway;
//    private PtrClassicFrameLayout mPtrFrame;
//    private String mCityCode = "";
//
//    private TextView mEmptyView;
//    private LinearLayout mLocationLayout;
//    private TextView mLocationCity;
//
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        setContentView(R.layout.activity_select_city);
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setTitle(getString(R.string.se_select_city));
//        titleBarView.setOnTitleBarClickListener(this);
//        //        OrderView headView = LayoutInflater.from(this).inflate(R.layout.select_city_header, null);//定位城市-目前开通城市
//
//        mCityCode = getIntent().getStringExtra(CssConstant.KEY_CITY_CODE);
//        mEmptyView = (TextView) findViewById(R.id.emptyview);
//        //        mLocationLayout = (LinearLayout) headView.findViewById(R.id.lv_location_layout);
//        //        mLocationCity = (TextView) headView.findViewById(R.id.location_city);
//        mCityListView = (XListView) findViewById(R.id.lv_city);
//        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_web_view_frame);
//        //        mCityListView.addHeaderView(headView);
//        mCityListView.setHeaderDividersEnabled(false);
//        mCityListView.setSelector(R.drawable.selector_lservice_list_transparent_item);
//        mCityListAdapter = new CityListAdapter();
//        mCityListView.setAdapter(mCityListAdapter);
//        mCityListView.setOnItemClickListener(this);
//        mCityListView.setXListViewListener(this);
//        mCityListView.setPullLoadEnable(false);
//        mSTGateway = new STGateway(STSelectCityActivity.this);
//        mPtrFrame.setLastUpdateTimeRelateObject(this);
//        mPtrFrame.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mCityListView, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                MLog.d(TAG, "onRefresh Begin");
//                getCityCode();
//            }
//
//        });
//        BizApplication.getInstance().configPullToRefresh(mPtrFrame);
//        mPtrFrame.autoRefresh();
//        //        if (!MPreference.getString(STSelectCityActivity.this, MPreference.LOCAL_CITY_CODE).isEmpty()) {
//        //            mLocationCity.setText(MPreference.getString(STSelectCityActivity.this, MPreference.LOCAL_CITY_NAME));
//        //            mLocationCity.setTextColor(getResources().getColor(R.color.black));
//        //            mLocationLayout.setEnabled(true);
//        //        } else {
//        //            mLocationCity.setText(getString(R.string.se_location_fail));
//        //            mLocationCity.setTextColor(getResources().getColor(R.color.red));
//        //            mLocationLayout.setEnabled(false);
//        //        }
//        //        mLocationLayout.setOnClickListener(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_STSelectCityActivity));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_STSelectCityActivity));
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
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (Utils.isFastDoubleClick()) {
//            return;
//        }
//
//        CityCode cityCode = new CityCode();
//        cityCode.setCityCode(mCityList.get(position).getCityCode());
//        cityCode.setCityName(mCityList.get(position).getCityName());
//        mCNCityCode = cityCode;
//        MPreference.putString(STSelectCityActivity.this, MPreference.ORDER_SELECT_CITY_CODE, mCNCityCode.getCityCode());
//        MPreference.putString(STSelectCityActivity.this, MPreference.ORDER_SELECT_CITY_NAME, mCNCityCode.getCityName());
//        mCityListAdapter.notifyDataSetChanged();
//        Intent intent = new Intent();
////        intent.putExtra(CITY, mCNCityCode);
//        setResult(SELECT_CITY_OK, intent);
//        finish();
//
//    }
//
//    @Override
//    public void onLoadMore() {
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            //            case R.id.lv_location_layout: //点击定位城市头--北京
//            //                CityCode mLCityCode = new CityCode();
//            //                mLCityCode.setCityCode(MPreference.getString(STSelectCityActivity.this, MPreference.LOCAL_CITY_CODE));
//            //                mLCityCode.setCityName(MPreference.getString(STSelectCityActivity.this, MPreference.LOCAL_CITY_NAME));
//            //                mCNCityCode = mLCityCode;
//            //                MPreference.putString(STSelectCityActivity.this, MPreference.ORDER_SELECT_CITY_CODE, mCNCityCode.getCityCode());
//            //                MPreference.putString(STSelectCityActivity.this, MPreference.ORDER_SELECT_CITY_NAME, mCNCityCode.getCityName());
//            //                Intent intent = new Intent();
//            //                intent.putExtra(CITY, mCNCityCode);
//            //                setResult(SELECT_CITY_OK, intent);
//            //                finish();
//            //                break;
//        }
//    }
//
//
//    private class CityListAdapter extends BaseAdapter {
//        @Override
//        public int getCount() {
//            return mCityList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return mCityList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(STSelectCityActivity.this, R.layout.item_city_list, null);//listview一个条目item
//                holder.cityName = (TextView) convertView.findViewById(R.id.tv_city_name);
//                holder.mSelcetImage = (ImageView) convertView.findViewById(R.id.tv_city_select);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            MLog.d(TAG, "cityName = " + mCityList.get(position).getCityName());
//            holder.cityName.setText(mCityList.get(position).getCityName());
//            if (mCityList.get(position).getCityCode().equals(mCityCode)) {
//                holder.mSelcetImage.setVisibility(View.VISIBLE);
//            } else {
//                holder.mSelcetImage.setVisibility(View.GONE);
//            }
//            return convertView;
//        }
//
//        class ViewHolder {
//            TextView cityName;
//            ImageView mSelcetImage;
//        }
//    }
//
//
//    private void getCityCode() {
//
//        mSTGateway.getMetroMapList(new ArrayList<MetroMapCheck>(), new MobileGateway.MobileGatewayListener<GetMetroMapListRs>() {
//            @Override
//            public void onFailed(Result result) {
//                refreshComplete();
//                showEmptyView();
//                CommonToast.onFailed(STSelectCityActivity.this, result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                refreshComplete();
//                showEmptyView();
//                handleHttpFaild(statusCode);
//            }
//
//            @Override
//            public void onSuccess(final GetMetroMapListRs response) {
//                refreshComplete();
//                mCityList.clear();
//                if (response.getMetroMapList() != null) {
//                    //                    CityCode fCityCode = new CityCode();
//                    //                    fCityCode.setCityCode("");
//                    //                    fCityCode.setCityName(getString(R.string.se_all_city));
//                    //                    mCityList.add(fCityCode);
//                    List<MetroMap> tempList = new ArrayList<>();
//                    for (int i = 0; i < response.getMetroMapList().size(); i++) {
//                        MetroMap metroMap = response.getMetroMapList().get(i);
//                        if (metroMap.getSingleTicketServiceYn().equalsIgnoreCase("Y")) {
//                            tempList.add(metroMap);
//                        }
//                    }
//                    mCityList.addAll(tempList);
//                }
//                mCityListAdapter.notifyDataSetChanged();
//                showEmptyView();
//
//            }
//
//            @Override
//            public void onNoNetwork() {
//                refreshComplete();
//                showEmptyView();
//                CommonToast.onNoNetwork(STSelectCityActivity.this);
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                getCityCode();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                handleAutoLoginFailed(result);
//                showEmptyView();
//            }
//
//        });
//    }
//
//    private void showEmptyView() {
//        if (mCityListAdapter.isEmpty()) {
//            mCityListView.setVisibility(View.GONE);
//            mEmptyView.setVisibility(View.VISIBLE);
//        } else {
//            mCityListView.setVisibility(View.VISIBLE);
//            mEmptyView.setVisibility(View.GONE);
//        }
//    }
//
//    private void refreshComplete() {
//        mPtrFrame.refreshComplete();
//    }
//
//}
