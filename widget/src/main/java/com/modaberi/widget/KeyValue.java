package com.modaberi.widget;

import android.os.Parcel;
import android.os.Parcelable;

public class KeyValue implements Parcelable {

    public long itemKey;
    public String itemValue;
    public String itemType;
    public boolean isSelected;

    public KeyValue() {
    }

    protected KeyValue(Parcel in) {
        itemKey = in.readLong();
        itemValue = in.readString();
        itemType = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<KeyValue> CREATOR = new Creator<KeyValue>() {
        @Override
        public KeyValue createFromParcel(Parcel in) {
            return new KeyValue(in);
        }

        @Override
        public KeyValue[] newArray(int size) {
            return new KeyValue[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(itemKey);
        dest.writeString(itemValue);
        dest.writeString(itemType);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }
}