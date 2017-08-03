package com.example.android.contentproviderbroadcastreceiver.Data.GroupData;

import android.util.Log;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-01.
 */

public class SmsGroupData extends RealmObject implements MyRealmObject {
    public RealmList<SmsUnitData> units;
    long start,end;

    @PrimaryKey
   long id;


    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public RealmList<SmsUnitData> getUnits() {
        return units;
    }

    public void setUnits(RealmList<SmsUnitData> units) {
        this.units = units;
    }

    //custom
    public int checkName(String s){

        for(int i=0;i<units.size();i++){
            Log.d("grouping",units.get(i).getName() + " : "+s);
            if(units.get(i).getName().equals(s)){
                return i;
            }
        }
        return -1;
    }

    public void setTime(long start, long end){
        setStart(start);
        setEnd(end);
    }

    @Override
    public int getType() {
        return 4;
    }

    @Override
    public long getDate() {
        return getEnd();
    }

    @Override
    public long getId() {
        return id;
    }
}