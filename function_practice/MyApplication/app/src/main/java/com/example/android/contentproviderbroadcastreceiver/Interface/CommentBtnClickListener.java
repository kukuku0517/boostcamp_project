package com.example.android.contentproviderbroadcastreceiver.Interface;

import android.view.View;

import com.example.android.contentproviderbroadcastreceiver.Data.MyRealmObject;

/**
 * Created by samsung on 2017-08-05.
 */

public interface CommentBtnClickListener{
    void onClick(Class c, MyRealmObject item,String text);
}
