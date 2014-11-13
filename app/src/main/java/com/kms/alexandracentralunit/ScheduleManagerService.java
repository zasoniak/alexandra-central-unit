package com.kms.alexandracentralunit;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kms.alexandracentralunit.data.database.ScheduleRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteScheduleRepository;
import com.kms.alexandracentralunit.data.model.ScheduledScene;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class ScheduleManagerService extends IntentService {

    private AlarmManager alarmManager;
    private ArrayList<PendingIntent> alarmIntents = new ArrayList<PendingIntent>();

    private ScheduleRepository repository;
    private List<ScheduledScene> scheduledScenes = new ArrayList<ScheduledScene>();

    public ScheduleManagerService() {
        super("Schedule manager");
        repository = new SQLiteScheduleRepository(CoreService.getContext());
        alarmManager = (AlarmManager) CoreService.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("ScheduleManagerService", "started");
        if(alarmManager == null)
        {
            alarmManager = (AlarmManager) CoreService.getContext().getSystemService(Context.ALARM_SERVICE);
        }
        scheduledScenes = CoreService.getHome().getSchedule();
        Log.d("ilosc scheduledScenes:", String.valueOf(scheduledScenes.size()));
        for(ScheduledScene scheduledScene : scheduledScenes)
        {
            //TODO: obsługa czasu i interwałów
            Intent newIntent = new Intent(CoreService.getContext(), ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_TIME, String.valueOf(scheduledScene.getTime())).putExtra(ScheduledScene.EXTRA_REPEAT_INTERVAL, scheduledScene.getRepeatInterval()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());
            final int id = (int) System.currentTimeMillis();
            PendingIntent alarmIntent = PendingIntent.getBroadcast(CoreService.getContext(), id, newIntent, 0);
            //remember for the future
            alarmIntents.add(alarmIntent);

            //   Calendar scheduleTime = Calendar.getInstance();
            // scheduleTime.setTimeInMillis(scheduledScene.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //            calendar.set(Calendar.HOUR_OF_DAY, scheduleTime.get(Calendar.HOUR_OF_DAY));
            //            calendar.set(Calendar.MINUTE, scheduleTime.get(Calendar.MINUTE));

            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 11);

            Log.d("obecny czas", String.valueOf(id));
            Log.d("zaplanowana scena:", scheduledScene.getId());
            Log.d("czas wystartowania:", String.valueOf((int) calendar.getTimeInMillis()));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, alarmIntent);
            //alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +5*1000, alarmIntent);
        }

    }
}
