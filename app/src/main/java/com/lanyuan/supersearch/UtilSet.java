package com.lanyuan.supersearch;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class UtilSet {

    public static boolean isNetworkAvailable(Activity activity) {
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) return false;
        else {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null && networkInfos.length > 0) {
                for (int i = 0; i < networkInfos.length; i++) {
                    Log.e("eye", i + "===状态===" + networkInfos[i].getState());
                    Log.e("eye", i + "===状态===" + networkInfos[i].getTypeName());
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void updateInfo(MainActivity activity) {
        Context context = activity.getApplicationContext();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            String version_code = packageInfo.versionName;
            String last_version = activity.preferences.getString("last_version", "0.0");
            SharedPreferences.Editor editor = activity.preferences.edit();
            if (!version_code.equals(last_version)){
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                ab.setTitle("更新 "+version_code);
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

}
