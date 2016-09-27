package com.lanyuan.supersearch.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lanyuan.supersearch.R;
import com.lanyuan.supersearch.Util.HistoryHelper;
import com.lanyuan.supersearch.Util.UtilSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        button = (Button)findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(SettingActivity.this);
                ab.setTitle("注意");
                ab.setMessage("真的要清空搜索历史吗？\n清空后无法找回!");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HistoryHelper.clearHistory();
                        Snackbar.make(getWindow().getDecorView(),"搜索记录已清空",Snackbar.LENGTH_SHORT).show();
                    }
                });
                ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.show();
            }
        });

        UtilSet.setTranslucentDecor(SettingActivity.this);

    }

}
