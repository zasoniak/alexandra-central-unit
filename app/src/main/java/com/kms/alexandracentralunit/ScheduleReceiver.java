package com.kms.alexandracentralunit;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.kms.alexandracentralunit.data.model.ScheduledScene;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class ScheduleReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ScheduleReceiver", "started");
        Intent service = new Intent(context, ScheduleService.class);
        service.putExtra(ScheduledScene.EXTRA_SCENE_ID, intent.getStringExtra(ScheduledScene.EXTRA_SCENE_ID));
        startWakefulService(context, service);
    }
}
