package com.lanyuan.supersearch.Util;

import com.lanyuan.supersearch.Pojo.Baidu;
import com.lanyuan.supersearch.View.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetBaiduList {

    private static boolean FLAG = true;
    public static int pn = 10;
    static List<Baidu> old_list = new ArrayList<>();
    static List<Baidu> new_list = new ArrayList<>();
    static List<String> site_list;
    static String last_Keyword;
    static OkHttpClient client;

    static {
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    public static List<Baidu> getBaiduResult(String keyword) {
        //Log.e("eyey","getBaiduResult"+"--keyword:"+keyword);
        List<Baidu> list;
        if(FLAG){
            list = old_list;
            FLAG = false;
        }else {
            list = new_list;
            FLAG = true;
        }

        pn = 10;
        site_list = SiteSetHelper.sites;
        if (!keyword.equals(last_Keyword)) {
            list.removeAll(list);
            last_Keyword = keyword;
        }
        List<Baidu> tempList = new ArrayList<>();
        Iterator<String> iterator = site_list.iterator();
        while (iterator.hasNext()) {
            List<Baidu> tempplist = getSingleBaiduResult(keyword, iterator.next(), 0);
            tempList.addAll(tempplist);
            if (tempplist.size() < 10) iterator.remove();
        }
        for (int i = tempList.size(); i > 0; i--) {
            int tempnum = new Random().nextInt(i);
            list.add(tempList.get(tempnum));
            tempList.remove(tempnum);
        }
        //Log.e("eye", String.valueOf(site_list.size()));
        List<Baidu> tempList2 = new ArrayList<>();
        if (list.size() < 10) {
            for (int j = 0; j < list.size(); j++) {
                tempList2.add(list.get(list.size() - 1));
                list.remove(list.size() - 1);
            }
            MainActivity.IS_NEXT = 1;
        } else {
            for (int j = 0; j < 10; j++) {
                tempList2.add(list.get(list.size() - 1));
                list.remove(list.size() - 1);
            }
        }
        return tempList2;
    }

    public static List<Baidu> updaterBaiduResult() {
        //Log.e("eyey","updaterBaiduResult"+"--keyword:"+last_Keyword);
        boolean staticFlag;
        List<Baidu> list;
        if(!FLAG){
            list = old_list;
        }else {
            list = new_list;
        }

        if (site_list.size() == 0) {
            MainActivity.IS_NEXT = 1;
            //Log.e("eye",String.valueOf("MainActivity.IS_NEXT:"+MainActivity.IS_NEXT));
            return null;
        } else if (list.size() < 10) {
            //Log.e("eye",String.valueOf("ok2"));
            List<Baidu> tempList = new ArrayList<>();
            Iterator<String> iterator = site_list.iterator();
            while (iterator.hasNext()) {
                List<Baidu> tempplist = getSingleBaiduResult(last_Keyword, iterator.next(), pn);
                tempList.addAll(tempplist);
                if (tempplist.size() < 10) iterator.remove();
            }
            for (int i = tempList.size(); i > 0; i--) {
                int tempnum = new Random().nextInt(i);
                list.add(tempList.get(tempnum));
                tempList.remove(tempnum);
            }
            //Log.e("eye", String.valueOf(list.size()));
            pn += 10;
            //Log.e("eye", String.valueOf(pn));
            return updaterBaiduResult();
        } else {
            //Log.e("eye",String.valueOf("ok3"));
            List<Baidu> tempList = new ArrayList<>();
            for (int j = 0; j < 10; j++) {
                tempList.add(list.get(list.size() - 1));
                list.remove(list.size() - 1);
            }
            //Log.e("eye",String.valueOf("ok4::"+tempList.size()+"::"+MainActivity.IS_NEXT));
            if (MainActivity.IS_NEXT==1)return null;
            else return tempList;
        }
    }

    private static List<Baidu> getSingleBaiduResult(String keyword, String site, int pn) {
        String url = "https://www.baidu.com/s?wd=" + keyword + " site:" + site + "&pn=" + pn;
        List<Baidu> Single_Result = new ArrayList<>();

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
            } else {
                return Single_Result;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Single_Result;
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

        return list;
    }

}
