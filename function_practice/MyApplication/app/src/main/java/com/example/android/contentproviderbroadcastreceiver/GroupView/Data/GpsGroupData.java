package com.example.android.contentproviderbroadcastreceiver.GroupView.Data;

import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.GpsData;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmGpsObject;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-11.
 */

public class GpsGroupData extends RealmObject implements MyRealmGpsObject {

    @PrimaryKey
    private long id;

    private long start = -1, end = -1;
    private String comment, place;
    private boolean highlight = false;
    private long endId = -1, startId = -1;
    private double lat, lng;


    private RealmList<GpsData> gpsDatas;

    public RealmList<GpsData> getGpsDatas() {
        return gpsDatas;
    }

    public void setGpsDatas(RealmList<GpsData> gpsDatas) {
        this.gpsDatas = gpsDatas;
    }
    public boolean isStart() {
        if (start > 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public void setHighlight() {
        highlight=!highlight;
    }

    @Override
    public boolean getHighlight() {
        return highlight;
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public long getDate() {
        if (isStart()) {
            return start;
        } else {
            return end;
        }
    }

    @Override
    public long getId() {
        return id;
    }

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

    public long getEndId() {
        return endId;
    }

    public void setEndId(long endId) {
        this.endId = endId;
    }

    public long getStartId() {
        return startId;
    }

    public void setStartId(long startId) {
        this.startId = startId;
    }

    @Override
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Override
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @Override
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }


    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public String select() {
        return null;
    }
}
