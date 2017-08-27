package com.whl.client.home.ticket;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.whl.framework.utils.Utils;
import com.whl.client.R;
import com.whl.client.gateway.model.singleticket.StationCode;

import java.util.List;

public class StationListAdapter extends BaseQuickAdapter<StationCode, BaseViewHolder> {
    private OnStationClickListener mListener;
    private StationCode mSelectedStationCode;
    private List<StationCode> data;
    private boolean isLast;
    private Context context;

    StationListAdapter(Context context, List<StationCode> data) {
        super(R.layout.item_station_detail_list, data);
        this.data = data;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder holder, final StationCode item) {
        TextView stationName = holder.getView(R.id.tv_station_name);
        ImageView changeStation = holder.getView(R.id.img_station_icon);
        ImageView ivLine = holder.getView(R.id.img_line);
        LinearLayout mLlver = holder.getView(R.id.ll_ver);
        //根据position判断。需要加一，因为getPosition从0开始
        if ((holder.getPosition() + 1) == data.size()) {
            ivLine.setVisibility(View.GONE);
        } else {
            ivLine.setVisibility(View.VISIBLE);
        }
        if (mSelectedStationCode != null && item.getStationNameZH().trim().equals(mSelectedStationCode.getStationNameZH().trim())) {
            stationName.setTextColor(mContext.getResources().getColor(R.color.FF9605));
        } else {
            stationName.setTextColor(mContext.getResources().getColor(R.color.bottom_text_nor));
        }
        if (item.getTransferYn().equalsIgnoreCase("Y")) {
            ViewGroup.LayoutParams para;
            int widthAheight = Utils.dip2px(context, 14);
            int cycleheight5 = Utils.dip2px(context, 1);
            para = changeStation.getLayoutParams();
            para.height = widthAheight;
            para.width = widthAheight;
            changeStation.setLayoutParams(para);
            changeStation.setImageResource(R.drawable.icon_change2);
            setMargins(stationName, 0, 8, 0, 0);
            setMargins(mLlver, 0, cycleheight5, 0, 0);
        } else {
            ViewGroup.LayoutParams para;
            int widthAheight = Utils.dip2px(context, 9);
            int cycleheight3 = Utils.dip2px(context, 3);
            para = changeStation.getLayoutParams();
            para.height = widthAheight;
            para.width = widthAheight;
            changeStation.setLayoutParams(para);
            changeStation.setImageResource(R.drawable.shape_circle);
            setMargins(stationName, 0, 10, 0, 0);
            setMargins(mLlver, 0, cycleheight3, 0, 0);
        }
        holder.setText(R.id.tv_station_name, item.getStationNameZH());

        stationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onStationClicked(item);
                }
            }
        });
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public void setOnStationClickListener(OnStationClickListener listener) {
        mListener = listener;
    }

    interface OnStationClickListener {
        void onStationClicked(StationCode station);
    }

    public void setSelectedStation(StationCode stationCode) {
        mSelectedStationCode = stationCode;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

}