package com.lanyuan.supersearch;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class MySearchSuggestion implements SearchSuggestion {

    private String searchKey;
    private boolean mIsHistory = false;

    public MySearchSuggestion(String searchKey) {
        this.searchKey = searchKey;
    }

    public boolean ismIsHistory() {
        return mIsHistory;
    }

    public void setmIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }

    public MySearchSuggestion(Parcel source) {
        this.searchKey = source.readString();
        this.mIsHistory = source.readInt() != 0;

    }

    @Override
    public String getBody() {
        return searchKey;
    }

    public static final Creator<SearchSuggestion> CREATOR = new Creator<SearchSuggestion>() {
        @Override
        public SearchSuggestion createFromParcel(Parcel source) {
            return new MySearchSuggestion(source) {
            };
        }

        @Override
        public SearchSuggestion[] newArray(int size) {
            return new MySearchSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(searchKey);
        dest.writeInt(mIsHistory ? 1 : 0);
    }

    @Override
    public boolean equals(Object o) {
        MySearchSuggestion ms = (MySearchSuggestion)o;
        if (ms.getBody().equals(this.getBody()))return true;
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
