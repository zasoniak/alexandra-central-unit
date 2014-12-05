package com.kms.alexandracentralunit;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 * ScheduleManagerService - service responsible for setting up all scheduled scene
 */
public class ScheduleManagerService extends IntentService {

    private AlarmManager alarmManager;
    private ArrayList<PendingIntent> alarmIntents = new ArrayList<PendingIntent>();

    public ScheduleManagerService() {
        super("Schedule manager");
        alarmManager = (AlarmManager) CoreService.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //TODO: obsłużyć wyłączanie alarmu
        Log.d("ScheduleManagerService", "started");
        if(alarmManager == null)
        {
            alarmManager = (AlarmManager) CoreService.getContext().getSystemService(Context.ALARM_SERVICE);
        }
        List<ScheduledScene> scheduledScenes = CoreService.getHome().getSchedule();
        Log.d("ilosc scheduledScenes:", String.valueOf(scheduledScenes.size()));
        for(ScheduledScene scheduledScene : scheduledScenes)
        {
            //TODO: obsługa czasu i interwałów
            Intent newIntent = new Intent(CoreService.getContext(), ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_TIME, String.valueOf(scheduledScene.getTime())).putExtra(ScheduledScene.EXTRA_REPEAT_INTERVAL, scheduledScene.getRepeatInterval()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());
            final int id = (int) System.currentTimeMillis();
            PendingIntent alarmIntent = PendingIntent.getBroadcast(CoreService.getContext(), id, newIntent, 0);
            //remember for the future
            alarmIntents.add(alarmIntent);

            Calendar scheduleTime = Calendar.getInstance();
            scheduleTime.setTimeInMillis(scheduledScene.getTime());

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.YEAR, 2014);
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 54);
            calendar.set(Calendar.SECOND, 0);

            Log.d("czas:", String.valueOf(System.currentTimeMillis()));
            Log.d("zaplanowana scena:", scheduledScene.getId()+" czas: "+scheduleTime.getTimeInMillis()+"slownie: "+scheduleTime.getTime().toString());
            //            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduleTime.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, alarmIntent);
            //   alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+60*1000, alarmIntent);
        }

    }
}
