package com.kms.alexandra.centralunit;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.kms.alexandra.data.model.ScheduledScene;

import java.util.Calendar;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 * ScheduleReceiver - receives alarm broadcast from android alarm manager
 */
public class ScheduleReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ScheduleReceiver", "started");

        //TODO: check condition
        boolean[] daysOfWeek = intent.getBooleanArrayExtra(ScheduledScene.DAYS_OF_WEEK);

        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        if(daysOfWeek[day-1])
        {
            Intent service = new Intent(context, ScheduleService.class);
            service.putExtra(ScheduledScene.EXTRA_SCENE_ID, intent.getStringExtra(ScheduledScene.EXTRA_SCENE_ID));
            startWakefulService(context, service);
        }
    }
}
