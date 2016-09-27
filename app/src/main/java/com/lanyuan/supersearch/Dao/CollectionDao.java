package com.lanyuan.supersearch.Dao;

import android.content.Context;
import android.database.SQLException;

import com.j256.ormlite.dao.Dao;
import com.lanyuan.supersearch.Pojo.Collection;
import com.lanyuan.supersearch.Util.DatabaseHelper;

import java.util.Collections;
import java.util.List;

public class CollectionDao {

    private static Dao<Collection, Integer> collectionDao;

    public static void initCollectionDao(Context context) throws SQLException {
        if (collectionDao == null){
            try {
                collectionDao = DatabaseHelper.getHelper(context).getDao(Collection.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addToCollection(Collection collection){
        try {
            collectionDao.create(collection);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateCollectionCname(Collection collection){
        try {
            collectionDao.update(collection);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    public static void queryFromCollectionByCname(String cname){

    }

    public static List<Collection> queryAllFromCollection(){
        try {
            List<Collection> list = collectionDao.queryForAll();
            Collections.reverse(list);
            return list;
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteCollection(Collection collection) {
        try {
            collectionDao.delete(collection);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }
}
