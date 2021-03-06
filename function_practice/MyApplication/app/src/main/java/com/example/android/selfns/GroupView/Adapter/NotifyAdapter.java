package com.example.android.selfns.GroupView.Adapter;

import android.content.Context;
import android.support.annotation.IntRange;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.android.selfns.Data.DTO.Detail.NotifyDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyGroupDTO;
import com.example.android.selfns.Data.DTO.Group.NotifyUnitDTO;
import com.example.android.selfns.GroupView.ViewHolder.VHNotify;
import com.example.android.selfns.GroupView.ViewHolder.VHNotifyChild;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmCommentableObject;
import com.example.android.selfns.Data.RealmData.interfaceRealmData.MyRealmObject;
import com.example.android.selfns.ExtraView.Comment.CommentBtnClickListener;
import com.example.android.selfns.GroupView.UnitActivity;
import com.example.android.selfns.R;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by samsung on 2017-08-02.
 */

public class NotifyAdapter extends AbstractExpandableItemAdapter<VHNotify, VHNotifyChild> implements CommentBtnClickListener {

    private NotifyGroupDTO items;
    private Context context;
    private Realm realm;


    public NotifyAdapter(Context context, Realm realm) {
        setHasStableIds(true);
        this.context = context;
        this.realm = realm;
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                notifyDataSetChanged();
            }
        });
    }

    public void setItems(NotifyGroupDTO items) {
        this.items = items;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);

        return new VHNotify(v, this, context);
    }

    @Override
    public VHNotifyChild onCreateChildViewHolder(ViewGroup parent, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sms_child, parent, false);
        return new VHNotifyChild(v);
    }

    @Override
    public void onBindGroupViewHolder(VHNotify holder, int groupPosition, @IntRange(from = -8388608L, to = 8388607L) int viewType) {
        NotifyUnitDTO unitdata = items.getUnits().get(groupPosition);

        holder.bindType(unitdata);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.fade_in_item);
        holder.itemView.startAnimation(animation);

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
        NotifyDTO child = items.getUnits().get(groupPosition).getNotifys().get(childPosition);

        holder.bindType(child);

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(VHNotify holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }


    @Override
    public void onClick(final Class c, final MyRealmObject item, final String text) {
        Log.d("comments", "onClick");
        UnitActivity detail = (UnitActivity) context;
        detail.setEditTextVisibility(View.VISIBLE);


        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MyRealmCommentableObject result = (MyRealmCommentableObject) realm.where(c).equalTo("id", item.getId()).findFirst();
                if (result != null) {
                    Log.d("comments", "result");

                    result.setComment(text);

                }
            }
        });
    }
}
