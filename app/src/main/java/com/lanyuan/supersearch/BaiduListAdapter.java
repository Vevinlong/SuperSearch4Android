package com.lanyuan.supersearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

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

    static class ViewHolder {
        public TextView line1;
        public TextView line2;
        public TextView line3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_card,null);
            holder.line1 = (TextView)convertView.findViewById(R.id.line1);
            holder.line2 = (TextView)convertView.findViewById(R.id.line2);
            holder.line3 = (TextView)convertView.findViewById(R.id.line3);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        holder.line1.setText(list.get(position).getTitle());
        holder.line2.setText(list.get(position).getInfo());
        holder.line3.setText(list.get(position).getUrl());

        return convertView;
    }
}
