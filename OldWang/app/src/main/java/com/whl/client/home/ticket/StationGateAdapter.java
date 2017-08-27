package com.whl.client.home.ticket;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whl.framework.utils.MLog;
import com.whl.client.R;
import com.whl.client.gateway.model.singleticket.ExitPoi;

import java.util.List;

public class StationGateAdapter extends BaseQuickAdapter<ExitPoi, BaseViewHolder> {
    private final static String TAG = "StationGateAdapter";
    private List<ExitPoi> data;
    OnGateItemClickedListener listener;
    Context mContext;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public StationGateAdapter(Context context, List<ExitPoi> data) {
        super(R.layout.item_station_gate, data);
        this.data = data;
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        holder.setText(R.id.tv_exit_entry, (data.get(positions).getExitData().getExitName()));
    }

    @Override
    protected void convert(final BaseViewHolder holder, final ExitPoi item) {
        String itemSecond = null;
        String itemFirst = null;
        String result = null;
        String mTitleOne = null;
        String mTitleTwo = null;
        MLog.d(TAG, "item.getPoiItemList().size():" + item.getPoiItemList().size());
        if (item.getPoiItemList().size() >= 2) {
            mTitleOne = item.getPoiItemList().get(0).getTitle();
            mTitleTwo = item.getPoiItemList().get(1).getTitle();
            if (!TextUtils.isEmpty(mTitleTwo)) {//第二个不味空
                if (!TextUtils.isEmpty(mTitleOne)) {//当前俩item都不为空
                    itemFirst = mTitleOne;
                    itemSecond = mTitleTwo;
                    result = itemFirst + "," + itemSecond;
                    holder.setText(R.id.tv_location, result);
                } else {//第二个不空，第一个空
                    itemSecond = mTitleTwo;
                    result = itemSecond;
                    holder.setText(R.id.tv_location, result);
                }
            } else {//第二个为空
                if (!TextUtils.isEmpty(mTitleOne)) {//第一个不为空
                    itemFirst = mTitleOne;
                    result = itemFirst;
                    holder.setText(R.id.tv_location, result);
                } else {
                    result = "";
                    holder.setText(R.id.tv_location, result);
                }
            }
        } else if (item.getPoiItemList().size() == 1) {
            mTitleOne = item.getPoiItemList().get(0).getTitle();//当只有一个item的时候
            if (!TextUtils.isEmpty(mTitleOne)) {
                itemFirst = mTitleOne;
                result = itemFirst;
                holder.setText(R.id.tv_location, result);
            } else {
                result = "";
                holder.setText(R.id.tv_location, result);
            }
        } else {//当item<1时候，也就是空
            result = "";
            holder.setText(R.id.tv_location, result);
        }
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                MLog.d(TAG, " item pos: " + pos);
                if (listener != null && item != null) {
                    listener.onGateItemClicked(pos, item);
                }
            }
        });
    }

    public void setGateItemClickedListener(OnGateItemClickedListener listener) {
        this.listener = listener;
    }

    public interface OnGateItemClickedListener {
        void onGateItemClicked(int position, ExitPoi item);
    }

}