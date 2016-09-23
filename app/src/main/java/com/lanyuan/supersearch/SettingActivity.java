package com.lanyuan.supersearch;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    EditText editText;
    Button button,button2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferences = getSharedPreferences("Sites_Book", 0);
        editor = preferences.edit();
        String sites = preferences.getString("sites","");

        editText = (EditText) findViewById(R.id.editText);
        sites = sites.replaceAll(":","\n");
        editText.setText(sites);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(onClickListener);
        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(SettingActivity.this);
                ab.setTitle("注意");
                ab.setMessage("真的要清空搜索历史吗？\n清空后无法找回!");
                ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UtilSet.clearHistory();
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String sites = editText.getText().toString().trim();
            if(!sites.isEmpty()){
                String reg = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
                Pattern pattern = Pattern.compile(reg);
                Matcher matcher = null;
                int flag = 0;
                String[] site = sites.split("\n");
                String a_sites = new String();
                for (String s : site) {
                    matcher = pattern.matcher(s);
                    if(!matcher.matches()){
                        flag = 1;
                        break;
                    }
                    a_sites += s + ":";
                }
                if (flag == 0){
                    a_sites = a_sites.substring(0,a_sites.length()-1);
                    Snackbar.make(getWindow().getDecorView(), a_sites, Snackbar.LENGTH_SHORT).show();
                    editor.putString("sites",a_sites);
                    editor.commit();

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("SITES",site);
                    intent.putExtra("Bundle",bundle);
                    setResult(MainActivity.RESULT_SITES,intent);
                    finish();
                }else if(flag == 1){
                    Snackbar.make(getWindow().getDecorView(),"输入的网址不合法",Snackbar.LENGTH_SHORT).show();
                }
            }else if(sites.isEmpty()){
                editor.putString("sites","");
                editor.commit();

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                String[] temp = {"baidu.com"};
                bundle.putStringArray("SITES",temp);
                intent.putExtra("Bundle",bundle);
                setResult(MainActivity.RESULT_SITES,intent);
                finish();
            }
        }
    };

}
