package com.whl.client.home.ticket;

import android.content.Context;
import android.view.View;

import com.amap.api.services.core.PoiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whl.framework.utils.MLog;
import com.whl.client.R;

import java.util.List;

/**
 * Created by lenovo on 2017/6/20.
 */

public class StationBusInfoAdapter extends BaseQuickAdapter<PoiItem, BaseViewHolder> {
    private List<PoiItem> datas;
    private OnBusInfoItemClickedListener listener;

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param mContext
     * @param data     A new list is created out of this one to avoid mutable list
     */
    public StationBusInfoAdapter(Context mContext, List<PoiItem> data) {
        super(R.layout.item_bus_info, data);
        this.datas = data;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int positions) {
        super.onBindViewHolder(holder, positions);
        holder.setText(R.id.tv_location_name, datas.get(positions).getTitle());
        holder.setText(R.id.tv_bus_station, datas.get(positions).getSnippet());
        holder.setText(R.id.tv_meter, datas.get(positions).getDistance() + "米");
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PoiItem item) {
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = helper.getAdapterPosition();
                MLog.d(TAG, " item pos: " + pos);
                if (listener != null) {
                    listener.onBusInfoItemClicked(pos, item);
                }
            }
        });
    }

    public void setBusInfoItemClickedListener(OnBusInfoItemClickedListener listener) {
        this.listener = listener;
    }

    public interface OnBusInfoItemClickedListener {
        void onBusInfoItemClicked(int position, PoiItem item);
    }
}