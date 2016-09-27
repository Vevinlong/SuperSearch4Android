package com.lanyuan.supersearch.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Filter;

import com.lanyuan.supersearch.View.MainActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HistoryHelper {
    public static List<HistorySuggestion> history_list = new ArrayList<>();

    public static void setHistoryList(String history) {
        history_list.removeAll(history_list);
        if (history.equals("")) return;
        for (String s : history.split(":")) {
            history_list.add(new HistorySuggestion(s));
        }
        Log.e("eye", String.valueOf(history_list.size()));
    }

    public static void addToHistory(String currentQuery) {
        history_list.remove(new HistorySuggestion(currentQuery));
        Collections.reverse(history_list);
        history_list.add(new HistorySuggestion(currentQuery));
        Collections.reverse(history_list);
        if (history_list.size() > 6) history_list.remove(history_list.get(history_list.size() - 1));
    }

    public static void saveHistoryList(MainActivity activity) {
        SharedPreferences.Editor editor = activity.preferences.edit();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < (history_list.size() > 6 ? 6 : history_list.size()); i++) {
            if (i != history_list.size() - 1) sb.append(history_list.get(i).getBody() + ":");
            else sb.append(history_list.get(i).getBody());
        }
        editor.putString("history", sb.toString());
        editor.commit();
    }

    public static void clearHistory() {
        history_list.removeAll(history_list);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<HistorySuggestion> results);
    }

    public static List<HistorySuggestion> getHistory(Context context, int count) {

        List<HistorySuggestion> suggestionList = new ArrayList<>();
        HistorySuggestion historySuggestion;
        for (int i = 0; i < history_list.size(); i++) {
            historySuggestion = history_list.get(i);
            historySuggestion.setmIsHistory(true);
            suggestionList.add(historySuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (HistorySuggestion historySuggestion : history_list) {
            historySuggestion.setmIsHistory(false);
        }
    }

    public static void findSuggestions(String newQuery, final int limit, final long SimulatedDelay, final HistoryHelper.OnFindSuggestionsListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.e("eye", "findSuggestions");
                try {
                    Thread.sleep(SimulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                HistoryHelper.resetSuggestionsHistory();
                List<HistorySuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (HistorySuggestion suggestion : history_list) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<HistorySuggestion>() {
                    @Override
                    public int compare(HistorySuggestion lhs, HistorySuggestion rhs) {
                        return lhs.ismIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<HistorySuggestion>) results.values);
                }
            }
        }.filter(newQuery);
    }

}
