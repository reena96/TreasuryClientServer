package com.example.reenamaryputhota.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by reenamaryputhota on 5/1/18.
 */

public class DailyCash implements Parcelable {
    int ID;
    int mDay = 25 ;
    int mMonth = 4 ;
    int mYear = 2018 ;
    String mDayOfWeek = "Wednesday" ;
    int mdayOpenAmount = 23432;
    int mdayCloseAmount = 23442;    int mCash = 8988 ;

    int index = 0;

    public DailyCash(int ID, int year, int month, int day, String dayOfWeek, int dayOpenAmt, int dayCloseAmt) {
        this.ID = ID;
        this.mDay = day;
        this.mMonth = month;
        this.mYear = year;
        this.mDayOfWeek = dayOfWeek;
        this.mdayOpenAmount = dayOpenAmt;
        this.mdayCloseAmount = dayCloseAmt;
    }

    //unparcelling the data
    public DailyCash(Parcel in) {
        ID = in.readInt();
        mYear = in.readInt() ;
        mMonth = in.readInt() ;
        mDay = in.readInt() ;
        mDayOfWeek = in.readString();
        mdayOpenAmount = in.readInt();
        mdayCloseAmount = in.readInt();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(ID);
        out.writeInt(mYear);
        out.writeInt(mMonth) ;
        out.writeInt(mDay) ;
        out.writeString(mDayOfWeek) ;
        out.writeInt(mdayOpenAmount) ;
        out.writeInt(mdayCloseAmount) ;
    }


    public static final Parcelable.Creator<DailyCash> CREATOR
            = new Parcelable.Creator<DailyCash>() {

        public DailyCash createFromParcel(Parcel in) {
            return new DailyCash(in) ;
        }

        public DailyCash[] newArray(int size) {
            return new DailyCash[size];
        }
    };

    public int describeContents()  {
        return 0 ;
    }



    public int getYear(){
        return mYear;
    }

    public int getMonth(){
        return mMonth;
    }

    public int getDay(){
        return mDay;
    }

    public String getDayOfWeek(){
        return mDayOfWeek;
    }

    public int getDayOpenAmt(){
        return mdayOpenAmount;
    }

    public int getDayCloseAmt(){
        return mdayCloseAmount;
    }
}
