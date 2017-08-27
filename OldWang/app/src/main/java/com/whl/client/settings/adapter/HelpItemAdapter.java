package com.whl.client.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.whl.client.R;
import com.whl.client.gateway.model.wallet.WalletFaq;

import java.util.ArrayList;
import java.util.List;


public class  HelpItemAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private List<String> listQuestion;
	private List<String> listAnswer;
	private GroupViewHolder groupViewHolder;
	private ChildViewHolder childViewHolder;
	private List<WalletFaq> list;
	
	public HelpItemAdapter(Context context, List<WalletFaq> list) {
		this.context = context;
		this.list = list;
		loadData();
	}
	
	private void loadData(){
		listQuestion = new ArrayList<String>();
		listAnswer = new ArrayList<String>();
		for(WalletFaq item:list){
			listQuestion.add(item.getQuestion());
			listAnswer.add(item.getAnswer());
		}
	}
	
	public void updateListView(List<WalletFaq> list) {
		this.list = list;
		loadData();
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return listQuestion.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return listAnswer.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return listAnswer.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.help_list, null);
			groupViewHolder = new GroupViewHolder();
			groupViewHolder.tvFaq = (TextView) convertView.findViewById(R.id.tvFaq);
			groupViewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(groupViewHolder);
		}else{
			groupViewHolder = (GroupViewHolder) convertView.getTag();
		}
		
		groupViewHolder.tvFaq.setText(listQuestion.get(groupPosition));
		if(isExpanded){
			groupViewHolder.icon.setImageResource(R.drawable.arrow_set);
		}else{
			groupViewHolder.icon.setImageResource(R.drawable.ic_arrow);
		}
		
		/*if(groupPosition == 0 ){
			convertView.setBackgroundResource(R.drawable.selector_settings_bg1);
		}else if(groupPosition == arrList.length-1){
			convertView.setBackgroundResource(R.drawable.selector_settings_bg4);
		}else{
			convertView.setBackgroundResource(R.drawable.selector_settings_bg2);
		}
		
		if(arrList.length == 1){
			convertView.setBackgroundResource(R.drawable.selector_settings_bg3);
		}*/
		
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.help_items, null);
			childViewHolder = new ChildViewHolder();
			childViewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
			convertView.setTag(childViewHolder);
		}else{
			childViewHolder = (ChildViewHolder) convertView.getTag();
		}
		
		childViewHolder.tvContent .setText(listAnswer.get(groupPosition));
		
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	static class GroupViewHolder{
		TextView tvFaq;
		ImageView icon;
	}

	static class ChildViewHolder{
		TextView tvContent;
	}

}
