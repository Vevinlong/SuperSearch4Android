package com.lanyuan.supersearch;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilSet {

    public static List<MySearchSuggestion> history_list = new ArrayList<>();

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isAvailable());
    }

    public static void updateInfo(MainActivity activity) {
        Context context = activity.getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version_code = packageInfo.versionName;
            String last_version = activity.preferences.getString("last_version", "0.0");
            SharedPreferences.Editor editor = activity.preferences.edit();
            if (!version_code.equals(last_version)) {
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setTitle("更新 " + version_code);
                ab.setMessage(R.string.update_info);
                ab.setPositiveButton("OK", null);
                ab.show();
                editor.putString("last_version", version_code);
                editor.commit();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setTranslucentDecor(Activity activity) {
        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    public static void setHistoryList(String history) {
        history_list.removeAll(history_list);
        if (history.equals("")) return;
        for (String s : history.split(":")) {
            history_list.add(new MySearchSuggestion(s));
        }
        Log.e("eye", String.valueOf(history_list.size()));
    }

    public static void addToHistory(String currentQuery) {
        history_list.remove(new MySearchSuggestion(currentQuery));
        Collections.reverse(history_list);
        history_list.add(new MySearchSuggestion(currentQuery));
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
        void onResults(List<MySearchSuggestion> results);
    }

    public static List<MySearchSuggestion> getHistory(Context context, int count) {

        List<MySearchSuggestion> suggestionList = new ArrayList<>();
        MySearchSuggestion mySearchSuggestion;
        for (int i = 0; i < history_list.size(); i++) {
            mySearchSuggestion = history_list.get(i);
            mySearchSuggestion.setmIsHistory(true);
            suggestionList.add(mySearchSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (MySearchSuggestion mySearchSuggestion : history_list) {
            mySearchSuggestion.setmIsHistory(false);
        }
    }

    public static void findSuggestions(String newQuery, final int limit, final long SimulatedDelay, final OnFindSuggestionsListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.e("eye", "findSuggestions");
                try {
                    Thread.sleep(SimulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UtilSet.resetSuggestionsHistory();
                List<MySearchSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (MySearchSuggestion suggestion : history_list) {
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
                Collections.sort(suggestionList, new Comparator<MySearchSuggestion>() {
                    @Override
                    public int compare(MySearchSuggestion lhs, MySearchSuggestion rhs) {
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
                    listener.onResults((List<MySearchSuggestion>) results.values);
                }
            }
        }.filter(newQuery);
    }
}
