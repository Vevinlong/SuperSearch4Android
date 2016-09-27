package com.lanyuan.supersearch.View;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.lanyuan.supersearch.CollectionListener.deleteCollectionNameOnClickListener;
import com.lanyuan.supersearch.CollectionListener.editCollectionSitesOnClickListener;
import com.lanyuan.supersearch.CollectionListener.updateCollectionNameOnClickListener;
import com.lanyuan.supersearch.Dao.CollectionDao;
import com.lanyuan.supersearch.ListAdpter.CollectionListAdapter;
import com.lanyuan.supersearch.Pojo.Collection;
import com.lanyuan.supersearch.R;
import com.lanyuan.supersearch.Util.SiteSetHelper;
import com.lanyuan.supersearch.Util.UtilSet;

import java.util.List;

public class SiteSetActivity extends AppCompatActivity {

    public ListView listView_collection;
    public List<Collection> collections;
    Button button_add;
    EditText public_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_set);

        initCollectionList();
        UtilSet.setTranslucentDecor(this);

        listView_collection = (ListView) findViewById(R.id.collection_listview);
        listView_collection.setAdapter(new CollectionListAdapter(collections, getApplicationContext()));

        listView_collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.collection_checkbox);
                Collection collection = collections.get(position);
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                    collection.setSelected(false);
                    CollectionDao.updateCollectionIsSelected(collection);
                } else {
                    checkBox.setChecked(true);
                    collection.setSelected(true);
                    CollectionDao.updateCollectionIsSelected(collection);
                }
            }
        });

        listView_collection.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder ab = new AlertDialog.Builder(SiteSetActivity.this);
                ab.setItems(new String[]{"编辑网址集", "修改网址集名字", "删除网址集"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                editCollectionSites(collections.get(position));
                                break;
                            case 1:
                                updateCollectionName(collections.get(position));
                                break;
                            case 2:
                                deleteCollection(collections.get(position));
                                break;
                        }
                    }
                });
                ab.show();
                return false;
            }
        });

        button_add = (Button) findViewById(R.id.collection_add);
        button_add.setOnClickListener(new View.OnClickListener() {
            EditText add_edit;

            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(SiteSetActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SiteSetActivity.this);
                View view = inflater.inflate(R.layout.input_dialog, null);
                add_edit = (EditText) view.findViewById(R.id.add_edit);
                add_edit.setHint("输入网址集名");
                ab.setTitle("新建网址集");
                ab.setView(view);
                ab.setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cname = add_edit.getText().toString().trim();
                        Snackbar.make(getWindow().getDecorView(), cname + "创建成功", Snackbar.LENGTH_SHORT).show();
                        CollectionDao.addToCollection(new Collection(cname));
                        collections = CollectionDao.queryAllFromCollection();
                        listView_collection.setAdapter(new CollectionListAdapter(collections, getApplicationContext()));
                    }
                });
                ab.setNegativeButton("取消", null);
                ab.show();
            }
        });
    }

    private void editCollectionSites(Collection collection) {
        AlertDialog.Builder ab = new AlertDialog.Builder(SiteSetActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SiteSetActivity.this);
        View view = inflater.inflate(R.layout.input_sites_dialog, null);
        public_edit = (EditText) view.findViewById(R.id.add_sites_edit);
        public_edit.setText(SiteSetHelper.EncodeSitesD2T(collection.getCsite()));
        ab.setTitle("编辑网址集");
        ab.setView(view);
        ab.setCancelable(false);
        ab.setPositiveButton("修改", null);
        ab.setNegativeButton("取消", null);
        AlertDialog alert = ab.create();
        alert.show();
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new editCollectionSitesOnClickListener(collection, public_edit, SiteSetActivity.this, alert));
    }

    private void deleteCollection(Collection collection) {
        AlertDialog.Builder ab = new AlertDialog.Builder(SiteSetActivity.this);
        ab.setTitle("注意");
        ab.setMessage("删除网址集后无法恢复，请确认");
        ab.setPositiveButton("删除", new deleteCollectionNameOnClickListener(collection, SiteSetActivity.this));
        ab.setNegativeButton("取消", null);
        ab.show();
    }

    private void updateCollectionName(Collection collection) {
        AlertDialog.Builder ab = new AlertDialog.Builder(SiteSetActivity.this);
        LayoutInflater inflater = LayoutInflater.from(SiteSetActivity.this);
        View view = inflater.inflate(R.layout.input_dialog, null);
        public_edit = (EditText) view.findViewById(R.id.add_edit);
        public_edit.setHint(collection.getCname());
        ab.setTitle("修改网址集名");
        ab.setView(view);
        ab.setPositiveButton("修改", new updateCollectionNameOnClickListener(collection, public_edit, SiteSetActivity.this));
        ab.setNegativeButton("取消", null);
        ab.show();
    }

    private void initCollectionList() {
        CollectionDao.initCollectionDao(getApplicationContext());
        collections = CollectionDao.queryAllFromCollection();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int i = 0;
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            SiteSetHelper.removeAllFromSites();
            for (Collection collection : collections) {
                if (collection.isSelected())
                    SiteSetHelper.addToSites(SiteSetHelper.EncodeSitesD2A(collection.getCsite()));
            }
            Log.e("eye", String.valueOf(SiteSetHelper.sites));
        }
        return super.onKeyDown(keyCode, event);
    }
}
