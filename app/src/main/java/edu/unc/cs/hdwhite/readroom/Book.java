package edu.unc.cs.hdwhite.readroom;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.media.session.ParcelableVolumeInfo;

import java.io.Serializable;

/**
 * Created by hdwhite on 10/10/2015.
 */
public class Book implements Serializable {
    private String title;
    private String author;
    private int date;

    public Book (String _title, String _author){
        title = _title;
        author = _author;
        date = 0;
    }

    public Book (String _title, String _author, int _date) {
        title = _title;
        author = _author;
        date = _date;
    }

    @Override
    public String toString(){
        if (date == 0) {
            return title + " by " + author;
        } else {
            return title + " (" + date + ") by " + author;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {

    }
}
