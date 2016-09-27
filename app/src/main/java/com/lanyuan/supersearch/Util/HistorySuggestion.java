package com.lanyuan.supersearch.Util;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

public class HistorySuggestion implements SearchSuggestion {

    private String searchKey;
    private boolean mIsHistory = false;

    public HistorySuggestion(String searchKey) {
        this.searchKey = searchKey;
    }

    public boolean ismIsHistory() {
        return mIsHistory;
    }

    public void setmIsHistory(boolean mIsHistory) {
        this.mIsHistory = mIsHistory;
    }

    public HistorySuggestion(Parcel source) {
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
            return new HistorySuggestion(source) {
            };
        }

        @Override
        public SearchSuggestion[] newArray(int size) {
            return new HistorySuggestion[size];
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
        HistorySuggestion ms = (HistorySuggestion) o;
        if (ms.getBody().equals(this.getBody())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
