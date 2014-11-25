package com.kms.alexandracentralunit;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.kms.alexandracentralunit.data.model.ScheduledScene;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 * ScheduleReceiver - receives alarm broadcast from android alarm manager
 */
public class ScheduleReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ScheduleReceiver", "started");

        //TODO: check condition
        Intent service = new Intent(context, ScheduleService.class);
        service.putExtra(ScheduledScene.EXTRA_SCENE_ID, intent.getStringExtra(ScheduledScene.EXTRA_SCENE_ID));
        startWakefulService(context, service);
    }
}
