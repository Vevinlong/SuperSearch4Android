package com.lanyuan.supersearch.Util;

import android.util.Log;

import com.lanyuan.supersearch.Dao.CollectionDao;
import com.lanyuan.supersearch.Pojo.Collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SiteSetHelper {

    public static List<String> sites = new ArrayList<>();

    public static void initSiteSetHelper(){
        List<Collection> collections = CollectionDao.queryAllFromCollection();
        for (Collection collection : collections) {
            if (collection.isSelected())
                SiteSetHelper.addToSites(SiteSetHelper.EncodeSitesD2A(collection.getCsite()));
        }
        Log.e("eye", String.valueOf(SiteSetHelper.sites));
    }

    public static void removeAllFromSites() {
        sites.removeAll(sites);
    }

    public static void addToSites(List<String> list) {
        if (list == null || list.isEmpty()) return;
        for (String s : list) {
            if (!isExist(s)) sites.add(s);
        }
    }

    private static boolean isExist(String site) {
        for (String s : sites) {
            if (s.equals(site)) return true;
        }
        return false;
    }

    public static boolean isCorrected(String site) {
        String reg = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(site);
        if (matcher.matches()) return true;
        else return false;
    }

    public static List<String> EncodeSitesT2A(String site) {
        if (site.equals("")) return null;
        String[] temp = site.split("\n");
        return new ArrayList<>(Arrays.asList(temp));
    }

    public static List<String> EncodeSitesD2A(String site) {
        if (site.equals("")) return null;
        String[] temp = site.split(":");
        return new ArrayList<>(Arrays.asList(temp));
    }

    public static String EncodeSitesA2D(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) sb.append(list.get(i));
            else sb.append(list.get(i) + ":");
        }
        return sb.toString();
    }

    public static String EncodeSitesA2T(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) sb.append(list.get(i));
            else sb.append(list.get(i) + "\n");
        }
        return sb.toString();
    }

    public static String EncodeSitesD2T(String site) {
        return EncodeSitesA2T(EncodeSitesD2A(site));
    }

}
