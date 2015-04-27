package com.gtrj.docdeal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gc.materialdesigndemo.R;

import java.util.List;

/**
 * Created by zhang77555 on 2015/4/24.
 */
public class SearchConditionAdapter extends BaseAdapter {

    public Context context;
    public List<String> list;
    public int viewId;

    public SearchConditionAdapter(Context context, List<String> list, int viewId) {
        this.context = context;
        this.list = list;
        this.viewId = viewId;
    }

    @Override
    public int getCount() {
        return list.size();
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
        SearchConditionViewHolder searchConditionViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(viewId, null);
            searchConditionViewHolder = new SearchConditionViewHolder(convertView);
            convertView.setTag(searchConditionViewHolder);
        }else {
            searchConditionViewHolder = (SearchConditionViewHolder) convertView.getTag();
        }
        searchConditionViewHolder.conditionName.setText(list.get(position));
        return convertView;
    }

    public static class SearchConditionViewHolder {
        private TextView conditionName;
       // private BootstrapEditText conditionText;

        public SearchConditionViewHolder(View itemView) {
            conditionName = (TextView) itemView.findViewById(R.id.search_condition_name);
           //conditionText = (BootstrapEditText) itemView.findViewById(R.id.condition_edit);
        }
    }
}
