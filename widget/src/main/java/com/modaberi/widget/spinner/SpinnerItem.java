package com.modaberi.widget.spinner;

import android.os.Parcel;
import android.os.Parcelable;

public class SpinnerItem implements Parcelable {
    public String title;
    public long id;
    public boolean isSelected;

    public SpinnerItem(String title, long id, boolean isSelected) {
        this.title = title;
        this.id = id;
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeLong(this.id);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected SpinnerItem(Parcel in) {
        this.title = in.readString();
        this.id = in.readLong();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<SpinnerItem> CREATOR = new Creator<SpinnerItem>() {
        @Override
        public SpinnerItem createFromParcel(Parcel source) {
            return new SpinnerItem(source);
        }

        @Override
        public SpinnerItem[] newArray(int size) {
            return new SpinnerItem[size];
        }
    };
}
