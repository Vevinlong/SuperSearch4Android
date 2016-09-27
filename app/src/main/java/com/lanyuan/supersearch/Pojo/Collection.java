package com.lanyuan.supersearch.Pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "collection")
public class Collection {

    @DatabaseField(generatedId = true)
    int cid;
    @DatabaseField(defaultValue = "cname")
    String cname;
    @DatabaseField(defaultValue = "csite")
    String csite;

    public Collection() {

    }

    public Collection(String cname, String csite) {
        this.cname = cname;
        this.csite = csite;
    }

    public Collection(String cname){
        this.cname = cname;
        this.csite = "";
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getCsite() {
        return csite;
    }

    public void setCsite(String csite) {
        this.csite = csite;
    }
}
