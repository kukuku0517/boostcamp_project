package com.example.android.selfns.Data.DTO.Group;
import com.example.android.selfns.Data.DTO.Detail.SmsDTO;
import com.example.android.selfns.Data.DTO.interfaceDTO.CommentableDTO;
import com.example.android.selfns.Data.RealmData.GroupData.SmsUnitData;
import com.example.android.selfns.Data.RealmData.UnitData.SmsData;
import com.example.android.selfns.Helper.RealmClassHelper;

import org.parceler.Parcel;

import java.util.ArrayList;

import io.realm.annotations.PrimaryKey;

/**
 * Created by samsung on 2017-08-15.
 */

@Parcel
public class SmsUnitDTO implements CommentableDTO {

   long id;

    public SmsUnitDTO(SmsUnitData data) {
        this.id = data.getId();
        this.count = data.getCount();
        this.start = data.getStart();
        this.end = data.getEnd();
        this.name = data.getName();
        this.content = data.getContent();
        this.comment = data.getComment();
        this.highlight = data.getHighlight();

        this.smsGroupId = data.getId();
        this.address = data.getAddress();


        for(SmsData d:data.getSmss()){
            this.smss.add(new SmsDTO(d));
        }
    }

    int highlight = 0;
    @Override
    public int getHighlight() {
        return highlight;
    }
    @Override
    public void setHighlight(int highlight) {
        this.highlight = highlight;
    }

    ArrayList<SmsDTO> smss=new ArrayList<>();
    int count;
    long start, end;
    String name;
    String content = "";
    String comment;

    long smsGroupId;

    public long getSmsGroupId() {
        return smsGroupId;
    }

    public void setSmsGroupId(long smsGroupId) {
        this.smsGroupId = smsGroupId;
    }


    public SmsUnitDTO() {

    }

    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getType() {
        return type;
    }

    int type=RealmClassHelper.SMS_UNIT_DATA;
    @Override
    public long getDate() {
        return start;
    }

    @Override
    public long getId() {
        return id;
    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = this.start > start ? start : this.start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = this.end < end ? end : this.end;
    }

    public ArrayList<SmsDTO> getSmss() {
        return smss;
    }

    public void setSmss(ArrayList<SmsDTO> smss) {
        this.smss = smss;
    }


    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

}
