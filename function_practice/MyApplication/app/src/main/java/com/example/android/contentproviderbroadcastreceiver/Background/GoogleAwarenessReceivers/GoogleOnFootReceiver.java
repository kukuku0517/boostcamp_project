package com.example.android.contentproviderbroadcastreceiver.Background.GoogleAwarenessReceivers;

import com.example.android.contentproviderbroadcastreceiver.Data.RealmHelper;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;

public class GoogleOnFootReceiver extends MyFenceReceiver {





    @Override
    public String getFenceType() {
        return ReceiverConstants.ON_FOOT_KEY;
    }

    @Override
    public void onStateTrue(double lat, double lng, CharSequence place, int type) {
        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat,lng,1, (String) place , 0);

    }

    @Override
    public void onStateFalse(double lat, double lng, CharSequence place, int type) {
        RealmHelper.gpsDataSave(System.currentTimeMillis(),lat,lng,1, (String) place ,1);

    }
}
