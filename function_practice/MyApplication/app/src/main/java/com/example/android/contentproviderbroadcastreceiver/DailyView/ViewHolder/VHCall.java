package com.example.android.contentproviderbroadcastreceiver.DailyView.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.contentproviderbroadcastreceiver.DailyView.Adapter.DayAdapter;
import com.example.android.contentproviderbroadcastreceiver.DetailView.Data.CallData;
import com.example.android.contentproviderbroadcastreceiver.Helper.DateHelper;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmHelper;
import com.example.android.contentproviderbroadcastreceiver.Interface.MyRealmObject;
import com.example.android.contentproviderbroadcastreceiver.Helper.CommentUtil;
import com.example.android.contentproviderbroadcastreceiver.Helper.RealmDataHelper;
import com.example.android.contentproviderbroadcastreceiver.DailyView.DayActivity;
import com.example.android.contentproviderbroadcastreceiver.Interface.NotifyListener;
import com.example.android.contentproviderbroadcastreceiver.R;
import com.github.vipulasri.timelineview.LineType;
import com.github.vipulasri.timelineview.TimelineView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.button;

/**
 * Created by samsung on 2017-07-26.
 */

public class VHCall extends DayViewHolder {

    //공통
    @BindView(R.id.item_am_pm)
    TextView ampm;
    @BindView(R.id.item_date)
    TextView date;
    @BindView(R.id.item_time_marker)
    TimelineView tlv;

    //공통 버튼
    @BindView(R.id.item_highlight)
    ImageButton highlightBtn;
    @BindView(R.id.item_delete)
    ImageButton deleteBtn;
    @BindView(R.id.item_edit)
    ImageButton editBtn;

    //공통 메뉴
    @BindView(R.id.item_people)
    View peopleView;
    @BindView(R.id.item_hide_menu)
    View hideMenu;

    //고유 레이아웃
    @BindView(R.id.call_cv)
    View view;

    //고유 뷰
    @BindView(R.id.call_iv)
    ImageView iv;
    @BindView(R.id.call_person)
    TextView person;
    @BindView(R.id.call_duration)
    TextView duration;
    @BindView(R.id.call_number)
    TextView number;
    @BindView(R.id.call_comment)
    TextView comment;

    private Context context;
    private boolean isExpanded = false;

    public VHCall(View view, Context context) {
        super(view);
        ButterKnife.bind(this, view); //없애고 돌려보기
        this.context = context;
        setmListener(context, nListener);
        tlv.initLine(LineType.NORMAL);
    }


    @Override
    public void bindType(final MyRealmObject item) {
        final CallData callData = (CallData) item;

        date.setText(DateHelper.getInstance().toDateString("hh:mm",callData.getDate()));
        ampm.setText(DateHelper.getInstance().isAm(callData.getDate()));

        person.setText(callData.getPerson());
        comment.setText(callData.getComment());
        duration.setText(DateHelper.getInstance().toDurationString(callData.getDuration()));
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
        highlightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callData.getHighlight()) {
                    highlightBtn.setColorFilter(Color.BLACK);
                } else {

                    highlightBtn.setColorFilter(Color.YELLOW);
                }

                CommentUtil.getInstance().highlight(callData);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealmHelper.DataDelete(callData);
            }
        });
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCallItemClick(item);
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    hideMenu.setVisibility(View.GONE);
                } else {
                    hideMenu.setVisibility(View.VISIBLE);
                }
                isExpanded = !isExpanded;
            }
        });


    }
}
