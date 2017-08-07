package com.example.android.contentproviderbroadcastreceiver.Adapter;

import android.content.Context;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Interface.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Main.DayActivity;
import com.example.android.contentproviderbroadcastreceiver.R;

import org.w3c.dom.Comment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHCall extends DayViewHolder {
    @BindView(R.id.call_iv)
    ImageView iv;
    @BindView(R.id.call_date)
    TextView date;
    @BindView(R.id.call_person)
    TextView person;
    @BindView(R.id.call_duration)
    TextView duration;
    @BindView(R.id.call_number)
    TextView number;
    @BindView(R.id.call_comment)
    TextView comment;
    @BindView(R.id.call_button)
    Button button;
    @BindView(R.id.call_cv)
    View view;

    private Context context;
    public VHCall(View view,Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context=context;
        setmListener(context);
    }

    @Override
    public void bindType(final MyRealmObject item) {
        final CallData callData = (CallData) item;
        DateFormat sdFormat = new SimpleDateFormat("hh : mm");
        Date d = new Date(callData.getDate());
        String tempDate = sdFormat.format(d);
        date.setText(tempDate);
        person.setText(callData.getPerson());
comment.setText(callData.getComment());
        long dur = callData.getDuration();
        int hour = (int) (dur / 60 / 60);
        int min = (int) (dur / 60 % 60);
        int sec = (int) (dur % 60);
        duration.setText(hour + "시간 " + min + "분 " + sec + "초");
        switch (callData.getCallState()) {
            case 1: //incoming
                duration.append(" 수신");
                break;
            case 2: //outgoing
                duration.append(" 발신");
                break;
            default:
                break;

        }

        number.setText(callData.getNumber());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentUtil.getInstance().show((DayActivity)context,callData);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallItemClick(item);
            }
        });

    }
}
