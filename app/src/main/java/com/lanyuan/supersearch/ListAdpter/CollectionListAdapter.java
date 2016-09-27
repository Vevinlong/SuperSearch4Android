package com.lanyuan.supersearch.ListAdpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lanyuan.supersearch.Pojo.Collection;
import com.lanyuan.supersearch.R;
import com.lanyuan.supersearch.Util.SiteSetHelper;

import java.util.List;

public class CollectionListAdapter extends BaseAdapter {

    List<Collection> collectionList;
    LayoutInflater inflater = null;

    public CollectionListAdapter(List<Collection> list, Context context) {
        collectionList = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return collectionList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class CollectionViewHolder {
        public TextView collection_name;
        public TextView collection_sites;
        public CheckBox collection_checkbox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollectionViewHolder holder = null;
        if (convertView == null) {
            holder = new CollectionViewHolder();
            convertView = inflater.inflate(R.layout.list_collection_card, null);
            holder.collection_name = (TextView) convertView.findViewById(R.id.collection_name);
            holder.collection_sites = (TextView) convertView.findViewById(R.id.collection_sites);
            holder.collection_checkbox = (CheckBox) convertView.findViewById(R.id.collection_checkbox);
            convertView.setTag(holder);
        } else {
            holder = (CollectionViewHolder) convertView.getTag();
        }
        Collection collection = collectionList.get(position);
        holder.collection_name.setText(collection.getCname());
        holder.collection_sites.setText(SiteSetHelper.EncodeSitesD2T(collection.getCsite()));
        if (collection.isSelected()){
            holder.collection_checkbox.setChecked(true);
        }else {
            holder.collection_checkbox.setChecked(false);
        }
        return convertView;
    }
}
