package com.lanyuan.supersearch.CollectionListener;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.widget.EditText;

import com.lanyuan.supersearch.Dao.CollectionDao;
import com.lanyuan.supersearch.ListAdpter.CollectionListAdapter;
import com.lanyuan.supersearch.Pojo.Collection;
import com.lanyuan.supersearch.View.SiteSetActivity;

public class updateCollectionNameOnClickListener implements DialogInterface.OnClickListener {
    Collection collection;
    EditText editText;
    SiteSetActivity siteSetActivity;

    public updateCollectionNameOnClickListener(Collection collection, EditText editText, SiteSetActivity siteSetActivity) {
        this.collection = collection;
        this.editText = editText;
        this.siteSetActivity = siteSetActivity;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        String cname = editText.getText().toString().trim();
        if (!cname.equals("")) {
            Snackbar.make(siteSetActivity.getWindow().getDecorView(), "修改成功", Snackbar.LENGTH_SHORT).show();
            collection.setCname(cname);
            CollectionDao.updateCollectionCname(collection);
            siteSetActivity.collections = CollectionDao.queryAllFromCollection();
            siteSetActivity.listView_collection.setAdapter(new CollectionListAdapter(siteSetActivity.collections, siteSetActivity.getApplicationContext()));
        }
    }
}
