package com.lanyuan.supersearch;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetBaiduList {

    static int pn = 0;
    static List<Baidu> list = new ArrayList<>();
    static String last_Keyword;

    public static List<Baidu> getBaiduResult(String keyword, String[] sites) {
        if(!keyword.equals(last_Keyword)){
            list.removeAll(list);
            last_Keyword = keyword;
        }
        if (list.size() < 10) {
            List<Baidu> tempList = new ArrayList<>();
            for (String site : sites) {
                tempList.addAll(getSingleBaiduResult(keyword, site, pn));
            }
            for (int i = tempList.size(); i > 0; i--) {
                int tempnum = new Random().nextInt(i);
                list.add(tempList.get(tempnum));
                tempList.remove(tempnum);
            }
            pn += 10;
            return GetBaiduList.getBaiduResult(keyword,sites);
        } else {
            List<Baidu> tempList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                tempList.add(list.get(list.size()-1));
                list.remove(list.size()-1);
            }
            return tempList;
        }
    }

    private static List<Baidu> getSingleBaiduResult(String keyword, String site, int pn) {
        String url = "https://www.baidu.com/s?wd=" + keyword + " site:" + site + "&pn=" + pn;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
                .addHeader("Accept", "*/*")
                .addHeader("Accept-Encoding", "deflate, sdch")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8")
                .addHeader("Connection", "keep-alive")
                .addHeader("Host", "sp0.baidu.com")
                .addHeader("Referer", "https://www.baidu.com/")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.86 Safari/537.36")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return GetBaiduList.paraseBaiduResult(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<Baidu> paraseBaiduResult(String page) {
        List<Baidu> list = new ArrayList<>();
        Document document = Jsoup.parse(page);
        Elements elements = document.getElementsByClass("c-container");
        if (elements.size() > 0) {
            for (Element e : elements) {
                Baidu b = new Baidu();
                b.setTitle(e.select("h3 a").text());
                b.setReal_url(e.select("h3 a").attr("href"));
                b.setInfo(e.select(".c-abstract,.c-span-last p:eq(0)").text());
                b.setUrl(e.select(".c-showurl").text());
                list.add(b);
            }
            return list;
        }

        return null;
    }

}
