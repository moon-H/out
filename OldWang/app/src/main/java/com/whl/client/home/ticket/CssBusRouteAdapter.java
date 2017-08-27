//package com.cssweb.shankephone.home.ticket;
//
//import android.content.Context;
//
//import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.chad.library.adapter.base.entity.MultiItemEntity;
//import com.cssweb.shankephone.R;
//
//import java.util.List;
//
///**
// * Created by liwx on 2017/4/3.
// * 公交路径
// */
//
//public class CssBusRouteAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
//    public static final int TYPE_LEVEL_0 = 0;
//    public static final int TYPE_LEVEL_1 = 1;
//
//    /**
//     * Same as QuickAdapter#QuickAdapter(Context,int) but with
//     * some initialization data.
//     *
//     * @param data A new list is created out of this one to avoid mutable list
//     */
//    public CssBusRouteAdapter(Context context, List<MultiItemEntity> data) {
//        super(data);
//        addItemType(TYPE_LEVEL_0, R.layout.item_bus_route_lv0);
//        addItemType(TYPE_LEVEL_1, R.layout.item_bus_route_lv1);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder holder, MultiItemEntity item) {
//        switch (holder.getItemViewType()) {
//            case TYPE_LEVEL_0:
//                BusRouteLv0Item lv0Item = (BusRouteLv0Item) item;
//                SchemeBusStep step = lv0Item.getSchemeBusStep();
//                if (step.isWalk() && step.getWalk() != null && step.getWalk().getDistance() > 0) {
//                    holder.setText(R.id.tv_step_name, "步行" + (int) step.getWalk().getDistance() + "米");
//                } else if (step.isBus() && step.getBusLines().size() > 0) {
//                    holder.setText(R.id.tv_step_name, step.getBusLines().get(0).getBusLineName());
//                    holder.setText(R.id.tv_sub_station_count, step.getBusLines().get(0).getPassStationNum() + 1 + "");
//                }
//                break;
//        }
//    }
//}
