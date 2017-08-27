package com.whl.client.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.whl.framework.utils.Utils;
import com.whl.client.R;

import java.util.ArrayList;
import java.util.Arrays;


public class DeletePopupDialog extends Dialog implements OnItemClickListener {

    private Context mContext;
    private OnDeleteDialogItemClickListener listener;
    private LayoutInflater mInflater;
    private ListView mListView;
    private ArrayList<String> mMenuList = new ArrayList<String>();
    private DeleteAdapter mAdapter;

    public DeletePopupDialog(Context context, OnDeleteDialogItemClickListener listener) {
        super(context, R.style.DialogTheme);
        this.listener = listener;
        mContext = context;
        setContentView(R.layout.layout_delete_pop);
        initViews(context);
    }

    private void initViews(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mListView = (ListView) findViewById(R.id.lv_delete);
        mAdapter = new DeleteAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setSelector(R.drawable.selector_add_service);
        mMenuList.addAll(Arrays.asList(context.getResources().getStringArray(R.array.delete_menu)));
    }

    private class DeleteAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mMenuList.size();
        }

        @Override
        public Object getItem(int i) {
            // TODO Auto-generated method stub
            return mMenuList.get(i);
        }

        @Override
        public long getItemId(int i) {
            // TODO Auto-generated method stub
            return i;
        }

        @Override
        public View getView(int index, View convertView, ViewGroup viewgroup) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_delete_menu, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(mMenuList.get(index));
            return convertView;
        }

        class ViewHolder {

            TextView name;
        }
    }

    public interface OnDeleteDialogItemClickListener {

        void onDialogItemClicked(int index);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int index, long l) {
        if (Utils.isFastDoubleClick())
            return;
        if (listener != null) {
            listener.onDialogItemClicked(index);
            dismiss();
        }
    }
}
