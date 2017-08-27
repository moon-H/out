package com.whl.client.view;


import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.whl.client.R;

public class WristbandMenuPopupView extends PopupWindow implements OnItemClickListener{
	private ListView listView;
	private LayoutInflater mInflater;
	private View mAnchorView;
	private OnMenuItemClickedListener listener;
	private boolean isVisible = false;
	private String[] arrayOptions;
	private int[] icons = new int[]{R.drawable.key, R.drawable.equipment};
	private Context context;
	
	public WristbandMenuPopupView(Context context, View anchor) {
		init(context, anchor);
	}

	public WristbandMenuPopupView(Context context) {
		init(context, null);
	}

	public WristbandMenuPopupView(Context context, View anchor, OnMenuItemClickedListener listener) {
		init(context, anchor);
		this.listener = listener;
	}
	
	private void init(Context context, View anchor) {
		this.context = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mAnchorView = anchor;
		View view = mInflater.inflate(R.layout.layout_menu2, null);
		listView = (ListView) view.findViewById(R.id.option_list);
		arrayOptions = context.getResources().getStringArray(R.array.arrayOptions);
		OptionAdapter adapter = new OptionAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
       
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable());
		setOutsideTouchable(false);
		setContentView(view);
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean visible) {
		isVisible = visible;
	}

	public void show() {
		if (this.isShowing()) {
			this.dismiss();
		} else {
			if (mAnchorView != null) {
				this.showAsDropDown(mAnchorView, 0, 0);
			}
		}

	};

	public interface OnMenuItemClickedListener {

		void onMenuItemClicked(int position);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		if (listener != null) {
			System.out.println("onMenuItemClicked"+position);
			dismiss();
			listener.onMenuItemClicked(position);
		}
		
	}
	
	class OptionAdapter extends BaseAdapter{
		public OptionAdapter() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int getCount() {
			return arrayOptions.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = LayoutInflater.from(context).inflate(R.layout.view_options_item, null);
				holder = new ViewHolder();
				holder.tv = (TextView) convertView.findViewById(R.id.tips);
				holder.img = (ImageView) convertView.findViewById(R.id.icon);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv.setText(arrayOptions[position]);
			holder.img.setImageResource(icons[position]);
			return convertView;
		}
		
		  class ViewHolder{
			TextView tv;
			ImageView img;
		}
		
	}
	
	
}
