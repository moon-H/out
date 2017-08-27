//package com.cssweb.shankephone.home.ticket;
//
//import android.content.BroadcastReceiver;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.BaseAdapter;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.cssweb.framework.downloadlibrary.DownloadConstants;
//import com.cssweb.framework.http.model.Result;
//import com.cssweb.framework.utils.MLog;
//import com.cssweb.framework.utils.Utils;
//import com.cssweb.framework.utils.ZipUtils;
//import com.cssweb.shankephone.R;
//import com.cssweb.shankephone.app.BaseActivity;
//import com.cssweb.shankephone.app.BizApplication;
//import com.cssweb.shankephone.app.CommonToast;
//import com.cssweb.shankephone.app.CssConstant;
//import com.cssweb.shankephone.db.SingleTicketDBManager;
//import com.cssweb.shankephone.dialog.NoticeDialog;
//import com.cssweb.shankephone.download.DownloadResService;
//import com.cssweb.shankephone.gateway.MobileGateway;
//import com.cssweb.shankephone.gateway.STGateway;
//import com.cssweb.shankephone.gateway.model.singleticket.GetMetroMapListRs;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMap;
//import com.cssweb.shankephone.gateway.model.singleticket.MetroMapCheck;
//import com.cssweb.shankephone.preference.MPreference;
//import com.cssweb.shankephone.umengShare.UMengShare;
//import com.cssweb.shankephone.view.PinnedSectionListView;
//import com.cssweb.shankephone.view.RoundProgressBarView;
//import com.cssweb.shankephone.view.TitleBarView;
//
//import org.apache.http.Header;
//import org.litepal.crud.DataSupport;
//
//import java.io.File;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * Created by liwx on 2015/11/2.
// */
//public class DownloadMapTilesActivity extends BaseActivity implements TitleBarView.OnTitleBarClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
//    private static final String TAG = "DownloadMapTilesActivity";
//
//    private static final int FLAG_UNZIP_SUCCESS = 101;
//    private static final int FLAG_UNZIP_FAILED = 102;
//    private static final int FLAG_GET_MAP_INFO_ONLINE = 103;
//    private static final int FLAG_UPDATE_LIST = 104;
//    private static final int FLAG_SYNC_CACHE_LIST = 105;
//
//    private ArrayList<MetroItem> mMapDownloaderArrayList = new ArrayList<>();
//    private ArrayList<MetroItem> mTmpMapDownloaderArrayList = new ArrayList<>();
//    private ArrayList<MetroMapCheck> mDiskCacheList = new ArrayList<>();
//
//    private MetroMapAdapter mMetroMapAdapter;
//    private DownloadReceiver mReceiver;
//    private STGateway mSTGateway;
//    private DownloadHandler mDownloadHandler;
//    private MapManager mMapmanager;
//    private SingleTicketManager mSingleTicketManager;
//    private SingleTicketDBManager mSingleTicketDBManager;
//
//    private PinnedSectionListView mCityListView;
//    private TextView mEmptyView;
//
//    private static final int CITY_SECTION = 2;
//    private ExecutorService mExecutor = Executors.newCachedThreadPool();
//
//    @Override
//    protected void onCreate(Bundle arg0) {
//        super.onCreate(arg0);
//        MLog.d(TAG, "onCreate");
//        setContentView(R.layout.activity_download_map_tiles);
//        BizApplication.getInstance().addActivity(this);
//        TitleBarView titleBarView = (TitleBarView) findViewById(R.id.title_bar);
//        titleBarView.setTitle(getString(R.string.se_select_city));
//        titleBarView.setOnTitleBarClickListener(this);
//        mEmptyView = (TextView) findViewById(R.id.emptyview);
//        mCityListView = (PinnedSectionListView) findViewById(R.id.lv_city);
//
//        mCityListView.setHeaderDividersEnabled(false);
//        mCityListView.setSelector(R.drawable.selector_lservice_list_transparent_item);
//        mMetroMapAdapter = new MetroMapAdapter();
//        mCityListView.setAdapter(mMetroMapAdapter);
//        mCityListView.setOnItemClickListener(this);
//        mCityListView.setOnItemLongClickListener(this);
//
//        mSTGateway = new STGateway(DownloadMapTilesActivity.this);
//        mReceiver = new DownloadReceiver();
//        mDownloadHandler = new DownloadHandler(this);
//        mSingleTicketManager = new SingleTicketManager(DownloadMapTilesActivity.this);
//        mSingleTicketDBManager = new SingleTicketDBManager(DownloadMapTilesActivity.this);
//
//        mMapmanager = BizApplication.getInstance().getMapManager();
//        //        getMetroMapList();
//        getLocalCachInfo();
//
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        MLog.d(TAG, "onResume");
//        registerReceiver();
//        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_DownloadMapTilesActivity));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        MLog.d(TAG, "onPause");
//        unRegister();
//        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_DownloadMapTilesActivity));
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        BizApplication.getInstance().removeActivity(this);
//    }
//
//    @Override
//    public void onBackClicked(View view) {
//        if (TextUtils.isEmpty(MPreference.getString(DownloadMapTilesActivity.this, MPreference.CHOICE_CITY_CODE))) {
//            backHomePage();
//            return;
//        }
//        finish();
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (TextUtils.isEmpty(MPreference.getString(DownloadMapTilesActivity.this, MPreference.CHOICE_CITY_CODE))) {
//            backHomePage();
//            return;
//        }
//        super.onBackPressed();
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
//        final MetroMap info = mMetroMapAdapter.getItem(position).metroMap;
//        if (info != null) {
//            int status = info.getStatus();
//            MLog.d(TAG, "onItemClick status = " + status);
//            switch (status) {
//                case DownloadConstants.STATUS_CONNECTING:
//                    cancelDownload(info.getDownloadMetroMapUrl());
//                    break;
//                case DownloadConstants.STATUS_DOWNLOADING:
//                    cancelDownload(info.getDownloadMetroMapUrl());
//                    break;
//                case DownloadConstants.STATUS_COMPLETE:
//                    File file = new File(DownloadConstants.getMetroCacheDir(getApplicationContext()), info.getCityCode());
//                    File[] fileList = file.listFiles();
//                    if (fileList != null)
//                        MLog.d(TAG, "onItemClick download success, file list size = " + fileList.length);
//                    showProgressView(getString(R.string.st_init));
//
//                    MLog.d(TAG, "complete all version = " + Utils.readFileContent(DownloadConstants.getMetroCacheDir(getApplicationContext()).getPath() + File.separator + info.getCityCode() + File.separator + "version"));
//                    MLog.d(TAG, "onItemClick cityCode = " + info.getCityCode());
//                    MLog.d(TAG, "onItemClick mapInfo = " + info.toString());
//                    //                mMapmanager.clearMemoryCache();
//                    //                    if (STHomeActivity.instance != null)
//                    //                        STHomeActivity.instance.finish();
//
//                    //                final int versionInt = Utils.converVersionStrToInt(info.getMetroMapVersion());
//                    MLog.d(TAG, "### width = " + info.getWidth() + " ## " + info.getHeight());
//                    String path = DownloadConstants.getMetroCacheDir(getApplicationContext()).getPath() + File.separator + info.getCityCode() + File.separator + "tiles";
//                    File cacheDir = new File(path);
//                    if (cacheDir.isDirectory()) {
//                        File[] files = cacheDir.listFiles();
//                        if (files != null && files.length > 0) {
//                            MLog.d(TAG, "set get tiles from disk");
//                            MPreference.putBoolean(getApplicationContext(), MPreference.MAP_GET_FROM_DISK, true);
//                        } else {
//                            MLog.d(TAG, "set get tiles from assets");
//                            MPreference.putBoolean(getApplicationContext(), MPreference.MAP_GET_FROM_DISK, false);
//                        }
//                    } else {
//                        MPreference.putBoolean(getApplicationContext(), MPreference.MAP_GET_FROM_DISK, false);
//                        MLog.d(TAG, "not directory = " + path);
//                    }
//                    mSingleTicketManager.cacheSingleTicketData(DownloadMapTilesActivity.this, info.getWidth(), info.getHeight(), info.getCityCode(), new SingleTicketManager.OnCacheSingletTicketDataListener() {
//                        @Override
//                        public void onCacheComplete() {
//                            dismissProgressView();
//                            //                            if (TextUtils.isEmpty(MPreference.getString(getApplicationContext(), MPreference.CHOICE_CITY_CODE))) {
//                            //                                Intent intent = new Intent(DownloadMapTilesActivity.this, STHomeActivity.class);
//                            //                                intent.putExtra(SpServiceManager.SERVICE_ID, SpServiceManager.SERVICE_ID_TICKET);
//                            //                                intent.putExtra(SpServiceManager.CHOICE_NUM, SpServiceManager.CHOICE_F);
//                            //                                intent.putExtra(STHomeActivity.METRO_MAP, info);
//                            //                                startActivity(intent);
//                            //                                finish();
//                            //                            } else {
//                            Intent intent = new Intent(getApplicationContext(), STHomeActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            intent.setAction(CssConstant.Action.ACTION_SWITCH_MAP);
//                            intent.putExtra(STHomeActivity.METRO_MAP, info);
//                            startActivity(intent);
//                            finish();
//
//                            //                            }
//                            MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_CODE, info.getCityCode());
//                            MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_NAME, info.getCityName());
//                        }
//
//                        @Override
//                        public void onCacheFailed() {
//                            dismissProgressView();
//                        }
//                    });
//                    break;
//                case DownloadConstants.STATUS_UNZIPING:
//                case DownloadConstants.STATUS_UNZIP_SUCCESS:
//                case DownloadConstants.STATUS_UNZIP_FAILED:
//                    MLog.d(TAG, "### handling zip file");
//                    break;
//                default:
//                    download(position, info.getDownloadMetroMapUrl(), info);
//                    break;
//            }
//        } else {
//            MLog.d(TAG, "title clicked");
//        }
//
//    }
//
//    private void getLocalCachInfo() {
//        mExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                mDiskCacheList.clear();
//                File file = DownloadConstants.getMetroCacheDir(getApplicationContext());
//                if (file != null) {
//                    File[] fileList = file.listFiles();
//                    if (fileList != null && fileList.length > 0) {
//                        int cacheCitySize = fileList.length;
//                        MLog.d(TAG, "#cacheCitySize = " + cacheCitySize);
//                        for (int i = 0; i < cacheCitySize; i++) {
//                            if (fileList[i].isDirectory()) {
//                                String fileName = fileList[i].getName().trim();
//                                String versionInfo = Utils.readFileContent(file.getPath() + File.separator + fileName + File.separator + "version");
//                                MLog.d(TAG, "file name = " + fileList[i].getName() + "#versionInfo = " + versionInfo);
//                                if (!TextUtils.isEmpty(versionInfo)) {
//                                    MetroMapCheck check = new MetroMapCheck();
//                                    check.setCityCode(fileName);
//                                    check.setCurrentMetroMapVersion(versionInfo);
//                                    mDiskCacheList.add(check);
//                                }
//                            }
//                        }
//                        //上送本地数据
//                        mDownloadHandler.sendEmptyMessage(FLAG_GET_MAP_INFO_ONLINE);
//                    } else
//                        //请求全部数据
//                        mDownloadHandler.sendEmptyMessage(FLAG_GET_MAP_INFO_ONLINE);
//                } else
//                    //请求全部数据
//                    mDownloadHandler.sendEmptyMessage(FLAG_GET_MAP_INFO_ONLINE);
//            }
//        });
//    }
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//        final MetroMap info = mMetroMapAdapter.getItem(position).metroMap;
//        if (info != null) {
//            if (info.getStatus() == DownloadConstants.STATUS_COMPLETE) {
//                NoticeDialog dialog = new NoticeDialog(DownloadMapTilesActivity.this, NoticeDialog.TWO_BUTTON);
//                dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//                                                          @Override
//                                                          public void onLeftButtonClicked(View view) {
//                                                              deleteLocalFile(info);
//                                                          }
//
//                                                          @Override
//                                                          public void onRightButtonClicked(View view) {
//
//                                                          }
//                                                      }
//
//                );
//                boolean isIntegrationCity = false;
//                String[] integrationCity = mSingleTicketManager.getLocalCityFromAsset();
//                for (String cityCode : integrationCity) {
//                    if (cityCode.equals(info.getCityCode())) {
//                        isIntegrationCity = true;
//                        break;
//                    }
//                }
//                if (isIntegrationCity) {
//                    dialog.show(getString(R.string.delete_integration_download_map));
//                } else {
//                    dialog.show(getString(R.string.delete_download_map));
//                }
//            }
//        } else {
//            MLog.d(TAG, "title long click");
//        }
//
//        return true;
//    }
//
//    private void deleteLocalFile(final MetroMap info) {
//        mExecutor.execute(new Runnable() {
//                              @Override
//                              public void run() {
//                                  Utils.recursionDeleteFile(new File(DownloadConstants.getMetroCacheDir(DownloadMapTilesActivity.this).getPath() + File.separator + info.getCityCode()));
//                                  String cachedCity = MPreference.getString(DownloadMapTilesActivity.this, MPreference.CHOICE_CITY_CODE);
//                                  if (!TextUtils.isEmpty(cachedCity)) {
//                                      if (cachedCity.equals(info.getCityCode())) {
//                                          MLog.d(TAG, "delete current visible map info");
//                                          MPreference.removeString(DownloadMapTilesActivity.this, MPreference.CHOICE_CITY_CODE);
//                                          mMapmanager.clearCachedStationInfo();
//                                      } else {
//                                          MLog.d(TAG, "delete other map info");
//                                      }
//                                      //所有城市地图信息被清除时,默认显示广州
//                                  }
//                                  //                                  mSingleTicketManager.saveLocalVersionInfo();
//                                  String[] integrationCity = mSingleTicketManager.getLocalCityFromAsset();
//                                  if (integrationCity != null && integrationCity.length > 0) {
//                                      for (String cityCode : integrationCity) {
//                                          if (cityCode.equals(info.getCityCode())) {
//                                              mSingleTicketManager.saveLocalVersionInfo();
//                                              break;
//                                          }
//                                      }
//                                  }
//                                  mSingleTicketDBManager.removeAllData(MetroMap.class);
//                                  mDownloadHandler.sendEmptyMessage(FLAG_SYNC_CACHE_LIST);
//                              }
//                          }
//
//        );
//    }
//
//    private class MetroMapAdapter extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {
//        private FrameLayout.LayoutParams sectionParentParams;
//        private FrameLayout.LayoutParams itemParentParams;
//
//        public MetroMapAdapter() {
//            itemParentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.download_map_tiles_item_height));
//            sectionParentParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelOffset(R.dimen.map_tiles_section_layout_size));
//        }
//
//        @Override
//        public int getCount() {
//            return mMapDownloaderArrayList.size();
//        }
//
//        @Override
//        public MetroItem getItem(int position) {
//            return mMapDownloaderArrayList.get(position);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            MetroMap item = getItem(position).metroMap;
//            ViewHolder holder;
//            if (convertView == null) {
//                holder = new ViewHolder();
//                convertView = View.inflate(DownloadMapTilesActivity.this, R.layout.item_download_map_tiles, null);
//                holder.cityName = (TextView) convertView.findViewById(R.id.tv_city_name);
//                holder.progressBar = (RoundProgressBarView) convertView.findViewById(R.id.progressbar);
//                holder.status = (ImageView) convertView.findViewById(R.id.img_status);
//                holder.version = (TextView) convertView.findViewById(R.id.tv_version);
//                holder.fileSize = (TextView) convertView.findViewById(R.id.tv_size);
//                holder.parent = (LinearLayout) convertView.findViewById(R.id.lly_item_parent);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            if (item != null) {
//                holder.parent.setLayoutParams(itemParentParams);
//                holder.parent.setBackgroundResource(R.drawable.selector_search_result);
//                holder.cityName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.st_city_item_textsize));
//                holder.cityName.setText(item.getCityName());
//                holder.progressBar.setProgress(item.getProgress());
//                holder.version.setText(item.getMetroMapVersion());
//                holder.fileSize.setText(Utils.getTotalPerSize(item.getMetroMapZipfileSize()));
//                int status = item.getStatus();
//                MLog.d(TAG, "cityName = " + item.getCityName() + " status = " + status);
//                switch (status) {
//                    case DownloadConstants.STATUS_CONNECTING:
//                    case DownloadConstants.STATUS_DOWNLOADING:
//                        holder.progressBar.setVisibility(View.VISIBLE);
//                        holder.status.setVisibility(View.GONE);
//                        break;
//                    case DownloadConstants.STATUS_COMPLETE:
//                        holder.progressBar.setVisibility(View.GONE);
//                        holder.status.setVisibility(View.VISIBLE);
//                        holder.status.setBackgroundResource(R.drawable.icon_complete);
//                        break;
//                    case DownloadConstants.STATUS_NEED_UPDATE:
//                        holder.progressBar.setVisibility(View.GONE);
//                        holder.status.setVisibility(View.VISIBLE);
//                        holder.status.setBackgroundResource(R.drawable.icon_update);
//                        break;
//                    case DownloadConstants.STATUS_CANCLED:
//                    case DownloadConstants.STATUS_NOT_DOWNLOAD:
//                    default:
//                        holder.progressBar.setVisibility(View.GONE);
//                        holder.status.setVisibility(View.VISIBLE);
//                        holder.status.setBackgroundResource(R.drawable.download);
//                        break;
//                }
//            } else {
//                holder.parent.setLayoutParams(sectionParentParams);
//                holder.parent.setBackgroundResource(R.color.F5F5F5);
//                holder.cityName.setText(getItem(position).name);
//                holder.progressBar.setVisibility(View.GONE);
//                holder.status.setVisibility(View.GONE);
//                holder.cityName.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.map_tiles_section_name_size));
//            }
//            return convertView;
//        }
//
//        @Override
//        public int getViewTypeCount() {
//            return 2;
//        }
//
//        @Override
//        public int getItemViewType(int position) {
//            return getItem(position).type;
//        }
//
//        @Override
//        public boolean isItemViewTypePinned(int viewType) {
//            return viewType == MetroItem.SECTION;
//        }
//
//        class ViewHolder {
//            LinearLayout parent;
//            TextView cityName;
//            TextView version;
//            TextView fileSize;
//            ImageView status;
//            RoundProgressBarView progressBar;
//        }
//    }
//
//
//    private void showProgressView(String msg) {
//        BizApplication.getInstance().getProgressDialog(DownloadMapTilesActivity.this, msg).show();
//    }
//
//    private void dismissProgressView() {
//        BizApplication.getInstance().dismissProgressDialog();
//    }
//
//    private void updateListView() {
//        mMapDownloaderArrayList.clear();
//        mMapDownloaderArrayList.addAll(mTmpMapDownloaderArrayList);
//        mMetroMapAdapter.notifyDataSetChanged();
//    }
//
//    private void getMetroMapList() {
//        showProgressView("");
//        mSTGateway.getMetroMapList(mDiskCacheList, new MobileGateway.MobileGatewayListener<GetMetroMapListRs>() {
//            @Override
//            public void onFailed(Result result) {
//                dismissProgressView();
//                CommonToast.onFailed(getApplicationContext(), result);
//            }
//
//            @Override
//            public void onHttpFailed(int statusCode, Header[] headers) {
//                dismissProgressView();
//                handleHttpFaild(statusCode);
//            }
//
//            @Override
//            public void onSuccess(final GetMetroMapListRs response) {
//                dismissProgressView();
//
//                //1.本地list
//                //2.服务端list，双层for循环，如果有citycode相同的将本地数据更新成已添加
//
//                updateLocalDB(response);
//
//
//                //                List<MetroMap> list = response.getMetroMapList();
//                //                mMapDownloaderArrayList.clear();
//                //                int size = list.size();
//                //                for (int i = 0; i < size; i++) {
//                //                    MetroMap info = new MetroMap();
//                //                    info.setId(i);
//                //                    info.setCityName(list.get(i).getCityName());
//                //                    info.setDownloadMetroMapUrl("http://61.50.125.146:8180/corpay/zip/bjdt.zip");
//                //                    mMapDownloaderArrayList.add(info);
//                //                }
//                //                mMetroMapAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onNoNetwork() {
//                dismissProgressView();
//                CommonToast.onNoNetwork(getApplicationContext());
//            }
//
//            @Override
//            public void onAutoLoginSuccess() {
//                dismissProgressView();
//                getMetroMapList();
//            }
//
//            @Override
//            public void onAutoLoginFailed(Result result) {
//                dismissProgressView();
//                handleAutoLoginFailed(result);
//            }
//
//        });
//    }
//
//    //    private void showEmptyView() {
//    //        if (mMetroMapAdapter.isEmpty()) {
//    //            mEmptyView.setVisibility(OrderView.VISIBLE);
//    //        } else {
//    //            mEmptyView.setVisibility(OrderView.GONE);
//    //        }
//    //    }
//
//    class DownloadReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            final String action = intent.getAction();
//            MLog.d(TAG, "action = " + action);
//            if (TextUtils.isEmpty(action) || !action.equals(DownloadResService.ACTION_DOWNLOAD_BROAD_CAST)) {
//                return;
//            }
//            final int position = intent.getIntExtra(DownloadResService.EXTRA_POSITION, -1);
//            final MetroMap tmpInfo = (MetroMap) intent.getSerializableExtra(DownloadResService.EXTRA_APP_INFO);
//            if (tmpInfo == null || position == -1) {
//                MLog.d(TAG, "temp info is null");
//                return;
//            }
//            if (position > mMapDownloaderArrayList.size()) {
//                MLog.d(TAG, "size is not match");
//                return;
//            }
//            final MetroMap mapDownloader = mMetroMapAdapter.getItem(position).metroMap;
//            final int status = tmpInfo.getStatus();
//            switch (status) {
//                case DownloadConstants.STATUS_CONNECTING:
//                    MLog.d(TAG, "###正在连接");
//                    mapDownloader.setStatus(DownloadConstants.STATUS_CONNECTING);
//                    mMetroMapAdapter.notifyDataSetChanged();
//                    break;
//                case DownloadConstants.STATUS_DOWNLOADING:
//                    MLog.d(TAG, "###正在下载 = " + tmpInfo.getProgress());
//                    mapDownloader.setStatus(DownloadConstants.STATUS_DOWNLOADING);
//                    mapDownloader.setProgress(tmpInfo.getProgress());
//                    mapDownloader.setTotalSize(tmpInfo.getTotalSize());
//                    if (isCurrentListViewItemVisible(position)) {
//                        MetroMapAdapter.ViewHolder holder = getViewHolder(position);
//                        holder.cityName.setText(mapDownloader.getCityName());
//                        holder.progressBar.setProgress(mapDownloader.getProgress());
//                        holder.fileSize.setText(mapDownloader.getTotalSize());
//                    }
//                    break;
//                case DownloadConstants.STATUS_COMPLETE:
//                    MLog.d(TAG, "###下载完成 ");
//                    if (isCurrentListViewItemVisible(position)) {
//                        MetroMapAdapter.ViewHolder holder = getViewHolder(position);
//                        holder.progressBar.setProgress(100);
//                    }
//                    mapDownloader.setStatus(DownloadConstants.STATUS_UNZIPING);
//                    mExecutor.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            String path = DownloadConstants.getMetroCacheDir(getApplicationContext()).getPath();
//                            String orgPath = path + File.separator + tmpInfo.getCityCode() + ".zip";
//                            MLog.d(TAG, "parentPath = " + path + "\norgPath = " + orgPath + "\nunZipPath = " + path);
//                            try {
//                                ZipUtils.UnZipFolder(orgPath, path);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                                MLog.e(TAG, "UnZip file occur an error::", e);
//                                Utils.deleteFile(orgPath);
//                                mDownloadHandler.sendEmptyMessage(FLAG_UNZIP_FAILED);
//                                mapDownloader.setStatus(DownloadConstants.STATUS_NOT_DOWNLOAD);
//                                return;
//                            }
//                            Utils.deleteFile(orgPath);
//                            MLog.d(TAG, "complete all version = " + Utils.readFileContent(path + File.separator + tmpInfo.getCityCode() + File.separator + "version"));
//                            mapDownloader.setStatus(DownloadConstants.STATUS_COMPLETE);
//                            mDownloadHandler.sendEmptyMessage(FLAG_UNZIP_SUCCESS);
//                        }
//                    });
//                    break;
//                case DownloadConstants.STATUS_PAUSED:
//                    MLog.d(TAG, "###下载暂停");
//                    break;
//                case DownloadConstants.STATUS_DOWNLOAD_ERROR:
//                    MLog.d(TAG, "###下载错误");
//                    mapDownloader.setStatus(DownloadConstants.STATUS_DOWNLOAD_ERROR);
//                    mapDownloader.setProgress(0);
//                    mMetroMapAdapter.notifyDataSetChanged();
//                    break;
//                case DownloadConstants.STATUS_CANCLED:
//                    MLog.d(TAG, "###下载取消");
//                    mapDownloader.setStatus(DownloadConstants.STATUS_CANCLED);
//                    mapDownloader.setProgress(0);
//                    mMetroMapAdapter.notifyDataSetChanged();
//                    break;
//            }
//        }
//    }
//
//    private void download(int position, String tag, MetroMap info) {
//        DownloadResService.intentDownload(getApplicationContext(), position, tag, info);
//    }
//
//    private void cancelDownload(final String tag) {
//        NoticeDialog noticeDialog = new NoticeDialog(DownloadMapTilesActivity.this, NoticeDialog.TWO_BUTTON);
//        noticeDialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//            @Override
//            public void onLeftButtonClicked(View view) {
//                DownloadResService.intentCancel(getApplicationContext(), tag);
//            }
//
//            @Override
//            public void onRightButtonClicked(View view) {
//
//            }
//        });
//        noticeDialog.show(getString(R.string.cacel_download_map));
//    }
//
//    private void registerReceiver() {
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(DownloadResService.ACTION_DOWNLOAD_BROAD_CAST);
//        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, intentFilter);
//    }
//
//    private void unRegister() {
//        if (mReceiver != null) {
//            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
//        }
//    }
//
//    private void pause(String tag) {
//        DownloadResService.intentPause(getApplicationContext(), tag);
//    }
//
//    private boolean isCurrentListViewItemVisible(int position) {
//        int first = mCityListView.getFirstVisiblePosition();
//        int last = mCityListView.getLastVisiblePosition();
//        return first <= position && position <= last;
//    }
//
//    private MetroMapAdapter.ViewHolder getViewHolder(int position) {
//        int childPosition = position - mCityListView.getFirstVisiblePosition();
//        View view = mCityListView.getChildAt(childPosition);
//        return (MetroMapAdapter.ViewHolder) view.getTag();
//    }
//
//    private static class DownloadHandler extends Handler {
//        WeakReference<DownloadMapTilesActivity> wrfDownloadActivity;
//
//        DownloadHandler(DownloadMapTilesActivity downloadMapTilesActivity) {
//            wrfDownloadActivity = new WeakReference<>(downloadMapTilesActivity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            DownloadMapTilesActivity context = wrfDownloadActivity.get();
//            switch (msg.what) {
//                case FLAG_UNZIP_SUCCESS:
//                    context.notifyDataSetChange(FLAG_UNZIP_SUCCESS);
//                    break;
//                case FLAG_UNZIP_FAILED:
//                    context.notifyDataSetChange(FLAG_UNZIP_FAILED);
//                    break;
//                case FLAG_GET_MAP_INFO_ONLINE:
//                    context.getMetroMapList();
//                    break;
//                case FLAG_UPDATE_LIST:
//                    context.updateListView();
//                    break;
//                case FLAG_SYNC_CACHE_LIST:
//                    context.getLocalCachInfo();
//                    break;
//            }
//        }
//    }
//
//    private void notifyDataSetChange(int unzipResult) {
//        switch (unzipResult) {
//            case FLAG_UNZIP_SUCCESS:
//                MLog.d(TAG, "unzip success");
//                mMetroMapAdapter.notifyDataSetChanged();
//                break;
//            case FLAG_UNZIP_FAILED:
//                MLog.d(TAG, "unzip failed");
//                mMetroMapAdapter.notifyDataSetChanged();
//                break;
//        }
//    }
//
//    private void updateLocalDB(final GetMetroMapListRs response) {
//        mExecutor.execute(new Runnable() {
//            @Override
//            public void run() {
//                List<MetroMap> listOnline = response.getMetroMapList();
//
//                if (listOnline != null && listOnline.size() > 0) {
//                    mSingleTicketDBManager.removeAllData(MetroMap.class);
//                    mSingleTicketDBManager.saveAllData(response.getMetroMapList());
//                    MLog.d(TAG, "##count = " + DataSupport.count(MetroMap.class));
//                    for (MetroMapCheck cacheMetro : mDiskCacheList) {
//                        List<MetroMap> metroMaps = DataSupport.where("cityCode = ? ", cacheMetro.getCityCode().trim()).find(MetroMap.class);
//                        if (metroMaps != null && metroMaps.size() > 0) {
//                            MetroMap map = metroMaps.get(0);
//                            ContentValues values = new ContentValues();
//                            if (map.getCityCode().equalsIgnoreCase(cacheMetro.getCityCode().trim())) {
//                                values.put("status", DownloadConstants.STATUS_COMPLETE);
//                            } else {
//                                values.put("status", DownloadConstants.STATUS_NOT_DOWNLOAD);
//                            }
//                            int i = DataSupport.update(MetroMap.class, values, map.getId());
//                            MLog.d(TAG, "update result = " + i);
//                        }
//                    }
//                    for (MetroMap metroMap : listOnline) {
//                        metroMap.updateAll("cityCode = ?", metroMap.getCityCode());
//                        if (metroMap.getExistsMetroUpdateVesion().equalsIgnoreCase("y")) {
//                            ContentValues values = new ContentValues();
//                            values.put("status", DownloadConstants.STATUS_NEED_UPDATE);
//                            int i = DataSupport.update(MetroMap.class, values, metroMap.getId());
//                            MLog.d(TAG, "update result = " + i);
//                        }
//                    }
//                } else
//                    DataSupport.deleteAll(MetroMap.class);
//
//                mTmpMapDownloaderArrayList.clear();
//                List<MetroMap> tempList = DataSupport.order("orderIndex asc").find(MetroMap.class);
//
//                for (int i = 0; i < CITY_SECTION; i++) {
//                    MetroItem section;
//                    if (i == 0) {
//                        section = new MetroItem(MetroItem.SECTION, getString(R.string.support_ticket_city), null);
//                    } else {
//                        section = new MetroItem(MetroItem.SECTION, getString(R.string.unsupport_ticket_city), null);
//                    }
//                    mTmpMapDownloaderArrayList.add(section);
//                    for (int j = 0; j < tempList.size(); j++) {
//                        MetroMap metroMap = tempList.get(j);
//                        if (i == 0 && metroMap.getServiceStatus().equalsIgnoreCase("Y")) {
//                            MetroItem item = new MetroItem(MetroItem.ITEM, metroMap.getCityName(), metroMap);
//                            mTmpMapDownloaderArrayList.add(item);
//                        } else if (i == 1 && metroMap.getServiceStatus().equalsIgnoreCase("N")) {
//                            MetroItem item = new MetroItem(MetroItem.ITEM, metroMap.getCityName(), metroMap);
//                            mTmpMapDownloaderArrayList.add(item);
//                        }
//                    }
//                }
//                mDownloadHandler.sendEmptyMessage(FLAG_UPDATE_LIST);
//            }
//        });
//    }
//
//    private void backHomePage() {
//        NoticeDialog dialog = new NoticeDialog(DownloadMapTilesActivity.this, NoticeDialog.TWO_BUTTON);
//        dialog.setOnNoticeDialogClickListener(new NoticeDialog.OnNoticeDialogClickListener() {
//            @Override
//            public void onLeftButtonClicked(View view) {
//                mExecutor.execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        MapManager mapManager = BizApplication.getInstance().getMapManager();
//                        mapManager.clearMemoryCache();
//                    }
//                });
//                MPreference.putBoolean(getApplicationContext(), MPreference.MAP_GET_FROM_DISK, false);
//                MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_CODE, CssConstant.SINGLE_TICKET.CITY_CODE_GUANGZHOU);
//
//                MPreference.putInt(getApplicationContext(), MPreference.VERSION_LINE_CODE, 0);
//                MPreference.putInt(getApplicationContext(), MPreference.VERSION_STATION_CODE, 0);
//
//                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(CssConstant.Action.ACTION_ADD_MAP_MARKER));
//                Intent intent = new Intent(getApplicationContext(), STHomeActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.setAction(CssConstant.Action.ACTION_SINGLETICKET_LOAD_DEFAULT_METRO_INFO);
//                startActivity(intent);
//                finish();
//                //                mSingleTicketManager.cacheSingleTicketData(DownloadMapTilesActivity.this, SingleTicketManager.MAP_WIDTH_GUANGZHOU, SingleTicketManager.MAP_HEIGHT_GUANGZHOU, CssConstant.SINGLE_TICKET.CITY_CODE_GUANGZHOU, new SingleTicketManager.OnCacheSingletTicketDataListener() {
//                //                    @Override
//                //                    public void onCacheComplete() {
//                //                        dismissProgressView();
//                //                        Intent intent = new Intent(getApplicationContext(), STHomeActivity.class);
//                //                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                //                        intent.setAction(CssConstant.Action.ACTION_SWITCH_MAP);
//                //                        //                intent.putExtra(STHomeActivity.METRO_MAP, info);
//                //                        startActivity(intent);
//                //                        finish();
//                //
//                //                        //                            }
//                //                        MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_CODE, CssConstant.SINGLE_TICKET.CITY_CODE_GUANGZHOU);
//                //                        //                MPreference.putString(getApplicationContext(), MPreference.CHOICE_CITY_NAME, info.getCityName());
//                //                    }
//                //
//                //                    @Override
//                //                    public void onCacheFailed() {
//                //                        dismissProgressView();
//                //                    }
//                //                });
//            }
//
//            @Override
//            public void onRightButtonClicked(View view) {
//
//            }
//        });
//        dialog.show(getString(R.string.no_selected_city));
//    }
//
//    static class MetroItem {
//
//        public static final int ITEM = 0;
//        public static final int SECTION = 1;
//
//        public final int type;
//        public final MetroMap metroMap;
//        public final String name;
//
//        public int sectionPosition;
//        public int listPosition;
//
//        public MetroItem(int type, String name, MetroMap metroMap) {
//            this.type = type;
//            this.metroMap = metroMap;
//            this.name = name;
//        }
//
//        @Override
//        public String toString() {
//            return metroMap.toString();
//        }
//
//    }
//}
