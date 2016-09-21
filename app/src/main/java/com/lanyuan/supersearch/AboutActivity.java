package com.lanyuan.supersearch;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity {

    Button button2,button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(AboutActivity.this);
                ab.setTitle("版权声明");
                ab.setMessage("　　本项目所有搜索的结果和内容版权均归百度公司所有，本项目不对任何搜索结果进行修改。\n　　这是一个实验性的，非营利的，开源的个人项目，如发现本项目有任何侵犯任何组织，公司或个人的行为，请联系作者，作者在此保证任何情况下都将积极配合整改，坚决维护被侵犯者的权益。任何通过本应用获取的信息本身导致法律问题，本应用不承担其法律责任。本应用不承担对信息本身的安全性，正确性，时效性等属性的审核责任。任何由开源项目复制本应用的一切行为与作者无关。本声明的最终解释权归作者所有。\n　　邮箱：lanyuanxiaoyao@qq.com\n　　(发送邮件请尽量在邮件标题表明来意，不然有可能被忽略)\n\n　　以下为百度知识产权声明：\n\n　　百度拥有本网站内所有资料的版权，各分频道权利声明有特殊规定的，从其规定。任何被授权的浏览、复制、打印和传播属于本网站内的资料必须符合以下条件：\n\n　　所有的资料和图象均以获得信息为目的；\n　　所有的资料和图象均不得用于商业目的；\n　　所有的资料、图象及其任何部分都必须包括此版权声明；\n\n　　本网站（www.baidu.com）所有的产品、技术与所有程序均属于百度知识产权，在此并未授权。“Baidu”、 “百度”及相关图形等为百度的注册商标。 \n\n　　未经百度许可，任何人不得擅自（包括但不限于：以非法的方式复制、传播、展示、镜像、上载、下载）使用，或通过非常规方式（如：恶意干预百度数据）影响百度的正常服务，任何人不得擅自以软件程序自动获得百度数据。否则，百度将依法追究法律责任。");
                ab.setPositiveButton("我知道了",null);
                ab.show();
            }
        });
        button3 = (Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(AboutActivity.this);
                ab.setTitle("更新说明");
                ab.setMessage(R.string.update_info);
                ab.setPositiveButton("我知道了",null);
                ab.show();
            }
        });
    }
}
