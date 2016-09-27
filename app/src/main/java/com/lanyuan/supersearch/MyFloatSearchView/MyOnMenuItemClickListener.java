package com.lanyuan.supersearch.MyFloatSearchView;

import android.content.Intent;
import android.view.MenuItem;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.lanyuan.supersearch.R;
import com.lanyuan.supersearch.Util.HistoryHelper;
import com.lanyuan.supersearch.View.AboutActivity;
import com.lanyuan.supersearch.View.MainActivity;
import com.lanyuan.supersearch.View.SettingActivity;
import com.lanyuan.supersearch.View.SiteSetActivity;

public class MyOnMenuItemClickListener implements FloatingSearchView.OnMenuItemClickListener {

    private MainActivity mainActivity;

    public MyOnMenuItemClickListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onActionMenuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                mainActivity.startActivityForResult(new Intent(mainActivity, SettingActivity.class), mainActivity.RESULT_FROM_SETTING);
                break;
            case R.id.about:
                mainActivity.startActivity(new Intent(mainActivity, AboutActivity.class));
                break;
            case R.id.cacel:
                HistoryHelper.saveHistoryList(mainActivity);
                System.exit(0);
                break;
            case R.id.site_set:
                mainActivity.startActivity(new Intent(mainActivity, SiteSetActivity.class));
        }
    }
}
