package com.lanyuan.supersearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    public static final int RESULT_FROM_SETTING = 1;
    public static final int RESULT_SITES = 2;
    public static int IS_NEXT = 0;

    FloatingSearchView searchView;
    List<SearchSuggestion> searchSuggestions;
    PullToRefreshListView listView;
    Handler handler;
    BaiduListAdapter adapter;
    List<Baidu> baiduList;
    String q_keyword = "";
    String[] sites;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("Sites_Book", 0);
        searchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        listView = (PullToRefreshListView) findViewById(R.id.list_v);

        UtilSet.updateInfo(MainActivity.this);
        UtilSet.setTranslucentDecor(MainActivity.this);

        NetworkJudgment();

        init();

        searchView.setOnSearchListener(searchListener);
        searchView.setOnMenuItemClickListener(menuItemClickListener);
        searchView.setOnQueryChangeListener(queryChangeListener);
        searchView.setOnFocusChangeListener(focusChangeListener);

        listView.setOnItemClickListener(itemClickListener);
        listView.setMode(PullToRefreshBase.Mode.DISABLED);
        listView.setOnRefreshListener(onRefreshListener);
    }

    private void NetworkJudgment() {
        if (!UtilSet.isNetworkAvailable(MainActivity.this)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("错误！");
            ab.setMessage("网络似乎没有连接……\n请确认后再重启应用……");
            ab.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            ab.show();
        }
    }

    private void init() {
        String temp = preferences.getString("sites", "");
        if (temp.equals("")) temp = "baidu.com";
        sites = temp.split(":");
        Log.e("eye","123");
        String history = preferences.getString("history","");
        Log.e("eye",history);
        UtilSet.setHistoryList(history);

        ILoadingLayout layout = listView.getLoadingLayoutProxy(false, true);
        layout.setPullLabel("上拉加载更多");
        layout.setRefreshingLabel("正在加载");
        layout.setReleaseLabel("松开立即加载");
    }

    FloatingSearchView.OnMenuItemClickListener menuItemClickListener = new FloatingSearchView.OnMenuItemClickListener() {
        @Override
        public void onActionMenuItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.setting:
                    startActivityForResult(new Intent(MainActivity.this, SettingActivity.class), RESULT_FROM_SETTING);
                    break;
                case R.id.about:
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case R.id.cacel:
                    UtilSet.saveHistoryList(MainActivity.this);
                    System.exit(0);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_SITES && requestCode == RESULT_FROM_SETTING) {
            Bundle bundle = data.getBundleExtra("Bundle");
            sites = bundle.getStringArray("SITES");
        }
    }

    FloatingSearchView.OnFocusChangeListener focusChangeListener = new FloatingSearchView.OnFocusChangeListener() {
        @Override
        public void onFocus() {
            searchView.swapSuggestions(UtilSet.history_list);
            Collections.reverse(UtilSet.history_list);
        }

        @Override
        public void onFocusCleared() {
            //searchView.clearSuggestions();
        }
    };

    FloatingSearchView.OnQueryChangeListener queryChangeListener = new FloatingSearchView.OnQueryChangeListener() {
        @Override
        public void onSearchTextChanged(String oldQuery, String newQuery) {
            if (!oldQuery.equals("") && newQuery.equals("")) {
                searchView.swapSuggestions(UtilSet.history_list);
                Collections.reverse(UtilSet.history_list);
            } else {
                searchView.showProgress();
                UtilSet.findSuggestions(newQuery, 6, FIND_SUGGESTION_SIMULATED_DELAY, new UtilSet.OnFindSuggestionsListener() {

                    @Override
                    public void onResults(List<MySearchSuggestion> results) {
                        searchView.swapSuggestions(results);
                        searchView.hideProgress();
                    }
                });
            }
        }
    };

    FloatingSearchView.OnSearchListener searchListener = new FloatingSearchView.OnSearchListener() {
        @Override
        public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            Log.e("eye", searchSuggestion.getBody());
            onSearchAction(searchSuggestion.getBody());
        }

        @Override
        public void onSearchAction(String currentQuery) {
            q_keyword = currentQuery;
            if (!currentQuery.equals("")) {
                MainActivity.IS_NEXT = 0;
                UtilSet.addToHistory(currentQuery);
                getBaiduList(currentQuery);
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
            } else{
                baiduList.removeAll(baiduList);
                adapter.notifyDataSetChanged();
            }
        }
    };

    PullToRefreshBase.OnRefreshListener2 onRefreshListener = new PullToRefreshBase.OnRefreshListener2() {
        @Override
        public void onPullDownToRefresh(PullToRefreshBase refreshView) {

        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase refreshView) {
            if (!q_keyword.isEmpty()) {
                new getUpdateDataTask().execute();
            }
        }
    };

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(baiduList.get(position - 1).getReal_url()));
            startActivity(intent);
        }
    };

    private void getBaiduList(final String keyword) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        baiduList = (List<Baidu>) msg.obj;
                        Log.e("eye", String.valueOf(baiduList.size()));
                        if (baiduList.size() == 0)
                            Snackbar.make(getWindow().getDecorView(), R.string.no_more_result, Snackbar.LENGTH_SHORT).show();
                        adapter = new BaiduListAdapter(baiduList, MainActivity.this);
                        listView.setAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                GetBaiduList.pn = 0;
                Message msg = new Message();
                msg.what = 1;
                msg.obj = GetBaiduList.getBaiduResult(keyword, sites);
                MainActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    private class getUpdateDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 1:
                            if (msg.obj != null) {
                                baiduList.addAll((List<Baidu>) msg.obj);
                            } else {
                                listView.setMode(PullToRefreshBase.Mode.DISABLED);
                                Snackbar.make(getWindow().getDecorView(), R.string.no_more_result, Snackbar.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();
                            listView.onRefreshComplete();
                    }
                    super.handleMessage(msg);
                }
            };
        }

        @Override
        protected String[] doInBackground(Void... params) {
            Message msg = new Message();
            msg.what = 1;
            msg.obj = GetBaiduList.updaterBaiduResult();
            MainActivity.this.handler.sendMessage(msg);
            return new String[0];
        }

        @Override
        protected void onPostExecute(String[] strings) {

        }
    }
    long LastTime = 0, NowTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            NowTime = System.currentTimeMillis();
            if (NowTime - LastTime < 1000) {
                UtilSet.saveHistoryList(MainActivity.this);
                System.exit(0);
            }
            else {
                Snackbar.make(getWindow().getDecorView(),"再点一次返回键退出",Snackbar.LENGTH_SHORT).show();
            }
            LastTime = NowTime;
        }

        return false;
    }
}
