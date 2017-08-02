package com.example.android.contentproviderbroadcastreceiver.Adapter.Expandable;

import android.content.Context;
import android.support.annotation.IntRange;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.NotifyUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsGroupData;
import com.example.android.contentproviderbroadcastreceiver.Data.GroupData.SmsUnitData;
import com.example.android.contentproviderbroadcastreceiver.Data.NotifyData;
import com.example.android.contentproviderbroadcastreceiver.Data.SmsData;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import io.realm.Realm;

/**
 * Created by samsung on 2017-08-02.
 */

public class NotifyAdapter extends AbstractExpandableItemAdapter<VHNotify,VHNotifyChild> {


    private NotifyGroupData items;
    private Context context;
    private Realm realm;


    public NotifyAdapter(Context context, Realm realm) {
        setHasStableIds(true);
        this.context = context;
        this.realm=realm;
    }

    public void setItems(NotifyGroupData items)
    {
        this.items=items;
    }
    @Override
    public int getGroupCount() {
        return items.getUnits().size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return items.getUnits().get(groupPosition).getNotifys().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return items.getUnits().get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return items.getUnits().get(groupPosition).getNotifys().get(childPosition).getId();
    }

    @Override
    public VHNotify onCreateGroupViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_item, parent, false);
        return new VHNotify(v);



    }

    @Override
    public VHNotifyChild onCreateChildViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_child_item, parent, false);
        return new VHNotifyChild(v);


    }

    @Override
    public void onBindGroupViewHolder(VHNotify holder, int groupPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {


        NotifyUnitData unitdata =items.getUnits().get(groupPosition);
        holder.bindType(unitdata);

    }

    @Override
    public int getChildItemViewType(int groupPosition, int childPosition) {
        return items.getUnits().get(groupPosition).getNotifys().get(childPosition).getType();
    }

    @Override
    public int getGroupItemViewType(int groupPosition) {
        return items.getUnits().get(groupPosition).getType();
    }

    @Override
    public void onBindChildViewHolder(VHNotifyChild holder, int groupPosition, int childPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        NotifyData child = items.getUnits().get(groupPosition).getNotifys().get(childPosition);
        holder.bindType(child);
    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(VHNotify holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }
}
