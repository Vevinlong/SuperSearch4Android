package com.lanyuan.supersearch.Pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "collection")
public class Collection {

    @DatabaseField(generatedId = true)
    private int cid;
    @DatabaseField(defaultValue = "cname")
    private String cname;
    @DatabaseField(defaultValue = "csite")
    private String csite;
    @DatabaseField(defaultValue = "isSelected")
    private boolean isSelected;

    public Collection() {

    }

    public Collection(String cname, String csite) {
        this.cname = cname;
        this.csite = csite;
        this.isSelected = false;
    }

    public Collection(String cname) {
        this.cname = cname;
        this.csite = "";
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
