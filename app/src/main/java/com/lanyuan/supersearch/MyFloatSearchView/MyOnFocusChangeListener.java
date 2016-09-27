package com.lanyuan.supersearch.MyFloatSearchView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.lanyuan.supersearch.Util.HistoryHelper;
import com.lanyuan.supersearch.View.MainActivity;

import java.util.Collections;

public class MyOnFocusChangeListener implements FloatingSearchView.OnFocusChangeListener {

    private MainActivity mainActivity;

    public MyOnFocusChangeListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onFocus() {
        mainActivity.searchView.swapSuggestions(HistoryHelper.history_list);
        Collections.reverse(HistoryHelper.history_list);
    }

    @Override
    public void onFocusCleared() {

    }
}
