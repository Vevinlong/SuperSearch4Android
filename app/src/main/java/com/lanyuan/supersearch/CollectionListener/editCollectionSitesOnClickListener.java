package com.lanyuan.supersearch.CollectionListener;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lanyuan.supersearch.Dao.CollectionDao;
import com.lanyuan.supersearch.ListAdpter.CollectionListAdapter;
import com.lanyuan.supersearch.Pojo.Collection;
import com.lanyuan.supersearch.Util.DatabaseHelper;
import com.lanyuan.supersearch.Util.SiteSetHelper;
import com.lanyuan.supersearch.View.SiteSetActivity;

import java.lang.reflect.Field;
import java.util.List;

public class editCollectionSitesOnClickListener implements View.OnClickListener {

    Collection collection;
    EditText editText;
    SiteSetActivity siteSetActivity;
    AlertDialog alertDialog;

    public editCollectionSitesOnClickListener(Collection collection, EditText public_edit, SiteSetActivity siteSetActivity,AlertDialog alertDialog) {
        this.collection = collection;
        this.editText = public_edit;
        this.siteSetActivity = siteSetActivity;
        this.alertDialog = alertDialog;
    }

    @Override
    public void onClick(View v) {
        List<String> sites = SiteSetHelper.EncodeSitesT2A(editText.getText().toString().trim());
        for(String s: sites){
            if (!SiteSetHelper.isCorrected(s)){
                Snackbar.make(siteSetActivity.getWindow().getDecorView(),"请填写有效网址 ",Snackbar.LENGTH_SHORT).show();
                return;
            }
        }
        collection.setCsite(SiteSetHelper.EncodeSitesA2D(sites));
        Log.e("eye",String.valueOf(SiteSetHelper.EncodeSitesA2D(sites)));
        CollectionDao.updateCollectionCname(collection);
        siteSetActivity.collections = CollectionDao.queryAllFromCollection();
        siteSetActivity.listView_collection.setAdapter(new CollectionListAdapter(siteSetActivity.collections, siteSetActivity.getApplicationContext()));
        alertDialog.dismiss();
    }
}
