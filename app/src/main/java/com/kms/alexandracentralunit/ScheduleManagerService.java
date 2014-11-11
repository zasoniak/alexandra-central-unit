package com.kms.alexandracentralunit;


import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

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
        if(alarmManager == null)
        {
            alarmManager = (AlarmManager) CoreService.getContext().getSystemService(Context.ALARM_SERVICE);
        }
        scheduledScenes = repository.getAll();
        for(ScheduledScene scheduledScene : scheduledScenes)
        {
            //TODO: obsługa czasu i interwałów
            Intent newIntent = new Intent(CoreService.getContext(), ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_TIME, scheduledScene.getTime().toString()).putExtra(ScheduledScene.EXTRA_REPEAT_INTERVAL, scheduledScene.getRepeatInterval()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());
            PendingIntent alarmIntent = PendingIntent.getBroadcast(CoreService.getContext(), 0, newIntent, 0);
            //remember for the future
            alarmIntents.add(alarmIntent);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 30);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, alarmIntent);
        }

    }
}
