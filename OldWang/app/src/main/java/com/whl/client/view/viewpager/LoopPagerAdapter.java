package com.whl.client.view.viewpager;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whl.framework.utils.DeviceInfo;
import com.whl.client.R;
import com.whl.client.gateway.model.Event;

import java.util.ArrayList;

/**
 *
 */
public class LoopPagerAdapter extends PagerAdapter {

    private Context mContext = null;

    private LayoutInflater mInflater;

    /**
     */
    private ArrayList<Event> mListData;

    private OnEventClickedListener listener;

    private int mScreenWidth;
    private int mItemHeight;

    /**
     * @param context
     * @param data
     */
    public LoopPagerAdapter(Context context, ArrayList<Event> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mListData = data;
        this.mScreenWidth = DeviceInfo.getScreenWidth(context);
        this.mItemHeight = context.getResources().getDimensionPixelOffset(R.dimen.app_advertise_height);
    }

    public void setListener(OnEventClickedListener listener) {
        this.listener = listener;
    }

    /**
     */
    public void setData(ArrayList<Event> data) {
        this.mListData = data;
    }


    @Override
    public Object instantiateItem(View view, int position) {
        View view_ = mInflater.inflate(R.layout.viewpager_view, null);

        ImageView img = (ImageView) view_.findViewById(R.id.img_event);

        final Event event = mListData.get(position);

        if (null != event) {
            Glide.with(mContext).load(event.getEventImageUrl()).override(mScreenWidth, mItemHeight).into(img);
        }
        ((ViewPager) view).addView(view_, 0);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                mContext.startActivity(new Intent(mContext, EventActivity.class));

                if (listener != null) {
                    listener.onEventClicked(event);
                }
            }
        });


        return view_;
    }


    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == (View) obj;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager) pager).removeView((View) view);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public void finishUpdate(View container) {
    }

    @Override
    public void startUpdate(View container) {
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public interface OnEventClickedListener {
        void onEventClicked(Event event);
    }

}
