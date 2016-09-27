package com.lanyuan.supersearch.Util;

import android.app.Activity;
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

import com.lanyuan.supersearch.R;
import com.lanyuan.supersearch.View.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UtilSet {

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

}
