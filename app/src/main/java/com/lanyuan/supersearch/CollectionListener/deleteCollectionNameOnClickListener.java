package com.lanyuan.supersearch.CollectionListener;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.lanyuan.supersearch.Dao.CollectionDao;
import com.lanyuan.supersearch.ListAdpter.CollectionListAdapter;
import com.lanyuan.supersearch.Pojo.Collection;
import com.lanyuan.supersearch.View.SiteSetActivity;

public class deleteCollectionNameOnClickListener implements DialogInterface.OnClickListener {

    Collection collection;
    SiteSetActivity siteSetActivity;

    public deleteCollectionNameOnClickListener(Collection collection, SiteSetActivity siteSetActivity) {
        this.collection = collection;
        this.siteSetActivity = siteSetActivity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Snackbar.make(siteSetActivity.getWindow().getDecorView(), "删除成功", Snackbar.LENGTH_SHORT).show();
        CollectionDao.deleteCollection(collection);
        siteSetActivity.collections = CollectionDao.queryAllFromCollection();
        Log.e("eye",String.valueOf(siteSetActivity.collections.size()));
        siteSetActivity.listView_collection.setAdapter(new CollectionListAdapter(siteSetActivity.collections, siteSetActivity.getApplicationContext()));
    }
}
