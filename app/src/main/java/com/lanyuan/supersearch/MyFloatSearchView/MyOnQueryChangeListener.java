package com.lanyuan.supersearch.MyFloatSearchView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.lanyuan.supersearch.Util.HistoryHelper;
import com.lanyuan.supersearch.Util.HistorySuggestion;
import com.lanyuan.supersearch.View.MainActivity;

import java.util.Collections;
import java.util.List;

public class MyOnQueryChangeListener implements FloatingSearchView.OnQueryChangeListener {

    private MainActivity mainActivity;

    public MyOnQueryChangeListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onSearchTextChanged(String oldQuery, String newQuery) {
        if (!oldQuery.equals("") && newQuery.equals("")) {
            mainActivity.searchView.swapSuggestions(HistoryHelper.history_list);
            Collections.reverse(HistoryHelper.history_list);
        } else {
            mainActivity.searchView.showProgress();
            HistoryHelper.findSuggestions(newQuery, 6, mainActivity.FIND_SUGGESTION_SIMULATED_DELAY, new HistoryHelper.OnFindSuggestionsListener() {

                @Override
                public void onResults(List<HistorySuggestion> results) {
                    mainActivity.searchView.swapSuggestions(results);
                    mainActivity.searchView.hideProgress();
                }
            });
        }
    }
}
