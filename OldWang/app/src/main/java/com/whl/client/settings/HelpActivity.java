package com.whl.client.settings;


import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whl.framework.http.model.Result;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.app.BaseActivity;
import com.whl.client.app.BizApplication;
import com.whl.client.gateway.MobileGateway;
import com.whl.client.gateway.MobileGateway.MobileGatewayListener;
import com.whl.client.gateway.model.wallet.GetWalletFaqListRs;
import com.whl.client.gateway.model.wallet.WalletFaq;
import com.whl.client.settings.adapter.HelpItemAdapter;
import com.whl.client.umengShare.UMengShare;
import com.whl.client.view.PullDownExpandView;
import com.whl.client.view.PullDownExpandView.OnPullDownListener;
import com.whl.client.view.TitleBarView;
import com.whl.client.view.TitleBarView.OnTitleBarClickListener;

import org.apache.http.Header;

import java.util.List;


public class HelpActivity extends BaseActivity implements OnTitleBarClickListener, OnPullDownListener {

    private TextView mVersionView;
    private TextView mTvContactTel;
    private ExpandableListView listView;
    private int pageNumber = 1;
    private PullDownExpandView mPullDownView;
    private final int PAGE_SIZE = 15;
    private int totalPage;
    private TextView mEmptyView;
    private final int REFRESH = 1, MORE = 2;
    private static final String TAG = "HelpActivity";
    private List<WalletFaq> list;
    private HelpItemAdapter adapter;
    private MobileGateway mGateway;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_help);
        BizApplication.getInstance().addActivity(this);

        mGateway = new MobileGateway(getApplicationContext());

        TitleBarView mTitleBarView = (TitleBarView) findViewById(R.id.title_bar);
        mTitleBarView.setTitle(getString(R.string.settings_help));
        mTitleBarView.setMenuName(getString(R.string.more));
        mTitleBarView.setOnTitleBarClickListener(this);

        mPullDownView = (PullDownExpandView) findViewById(R.id.lvQa);
        mEmptyView = (TextView) findViewById(R.id.emptyview);
        mPullDownView.setOnPullDownListener(this);
        listView = mPullDownView.getListView();
        mPullDownView.setShowHeader();
        listView.setHeaderDividersEnabled(false);
        listView.setSelector(R.drawable.selector_lservice_list_item);

        listView.setDivider(new ColorDrawable(getResources().getColor(R.color.search_listview_divider)));
        listView.setChildDivider(new ColorDrawable(getResources().getColor(R.color.search_listview_divider)));
        listView.setDividerHeight(1);
        listView.setGroupIndicator(null);
        listView.setSelector(new ColorDrawable());
        getWalletFaqList(0, PAGE_SIZE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MLog.d(TAG, "onResume");
        UMengShare.countPageAndTimeStart(this, getString(R.string.statistic_HelpActivity));
    }

    @Override
    protected void onPause() {
        super.onPause();
        MLog.d(TAG, "onPause");
        UMengShare.countPageAndTimeEnd(this, getString(R.string.statistic_HelpActivity));
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        BizApplication.getInstance().removeActivity(this);
    }

    @Override
    public void onBackClicked(View view) {
        finish();
    }

    @Override
    public void onMenuClicked(View view) {

    }

    @Override
    public void onRefresh() {
        MLog.d(TAG, "onRefresh");
        getWalletFaqList(REFRESH, PAGE_SIZE);
    }

    @Override
    public void onMore() {
        MLog.d(TAG, "onMore");
        getWalletFaqList(MORE, PAGE_SIZE);
    }

    private void getWalletFaqList(final int requestType, int pageSize) {
        if (requestType == REFRESH || requestType == 0) {
            pageNumber = 1;
        }
        MLog.d(TAG, "getWalletFaqList=pageNumber=" + pageNumber);
        mGateway.getWalletFaqList(pageNumber, pageSize, new MobileGatewayListener<GetWalletFaqListRs>() {

            @Override
            public void onSuccess(GetWalletFaqListRs response) {
                if (requestType == MORE) {
                    mPullDownView.notifyDidMore();
                    if (response.getWalletFaqList() != null) {
                        list.addAll(response.getWalletFaqList());
                    } else {
                        mPullDownView.setHideFooter();
                    }
                } else {
                    if (requestType == REFRESH)
                        mPullDownView.RefreshComplete();
                    list = response.getWalletFaqList();
                    adapter = new HelpItemAdapter(HelpActivity.this, list);
                    listView.setAdapter(adapter);
                }
                totalPage = response.getPageInfo().getTotalPage();
                pageNumber = response.getPageInfo().getPageNumber();
                MLog.d(TAG, "totalPage=" + totalPage);
                MLog.d(TAG, "pageNumber=" + pageNumber);
                if (totalPage > pageNumber) {
                    mPullDownView.enableAutoFetchMore(true, 1);
                    pageNumber++;
                } else {
                    mPullDownView.setHideFooter();
                }
                adapter.updateListView(list);
                if (adapter.isEmpty()) {
                    mEmptyView.setVisibility(View.VISIBLE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNoNetwork() {
                MLog.d(TAG, "onNoNetwork");
                if (requestType == REFRESH) {
                    mPullDownView.RefreshComplete();
                } else {
                    mPullDownView.notifyDidMore();
                }
                Toast.makeText(getApplication(), getString(R.string.network_exception), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onHttpFailed(int statusCode, Header[] headers) {
                MLog.d(TAG, "onHttpFailed");
                if (requestType == REFRESH) {
                    mPullDownView.RefreshComplete();
                } else {
                    mPullDownView.notifyDidMore();
                }
                handleHttpFaild(statusCode);
            }

            @Override
            public void onFailed(Result result) {
                MLog.d(TAG, "onFailed");
                if (requestType == REFRESH) {
                    mPullDownView.RefreshComplete();
                } else {
                    mPullDownView.notifyDidMore();
                }
            }

            @Override
            public void onAutoLoginSuccess() {
                getWalletFaqList(requestType, PAGE_SIZE);
            }

            @Override
            public void onAutoLoginFailed(Result result) {
                handleAutoLoginFailed(result);

            }

        });
    }
}
