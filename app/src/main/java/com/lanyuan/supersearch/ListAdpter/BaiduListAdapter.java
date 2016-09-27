package com.lanyuan.supersearch.ListAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lanyuan.supersearch.Pojo.Baidu;
import com.lanyuan.supersearch.R;

import java.util.List;

public class BaiduListAdapter extends BaseAdapter {
    private List<Baidu> list;
    private LayoutInflater mInflater = null;

    public BaiduListAdapter(List<Baidu> list, Context context) {
        this.list = list;
        mInflater = LayoutInflater.from(context);
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

    static class BaiduViewHolder {
        public TextView result_title;
        public TextView result_message;
        public TextView result_url;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaiduViewHolder holder = null;
        if(convertView==null){
            holder = new BaiduViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_card,null);
            holder.result_title = (TextView)convertView.findViewById(R.id.result_title);
            holder.result_message = (TextView)convertView.findViewById(R.id.result_message);
            holder.result_url = (TextView)convertView.findViewById(R.id.result_url);
            convertView.setTag(holder);
        }else {
            holder = (BaiduViewHolder)convertView.getTag();
        }
        holder.result_title.setText(list.get(position).getTitle());
        holder.result_message.setText(list.get(position).getInfo());
        holder.result_url.setText(list.get(position).getUrl());

        return convertView;
    }
}
