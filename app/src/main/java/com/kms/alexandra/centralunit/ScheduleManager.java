package com.kms.alexandra.centralunit;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.kms.alexandra.data.model.ScheduledScene;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 * ScheduleManagerService - service responsible for setting up all scheduled scene
 */
public class ScheduleManager {

    private static ScheduleManager instance;
    private AlarmManager alarmManager;
    private ArrayList<PendingIntent> alarmIntents = new ArrayList<PendingIntent>();

    private ScheduleManager() {
        alarmManager = (AlarmManager) CoreService.getContext().getSystemService(Context.ALARM_SERVICE);
        List<ScheduledScene> scheduledScenes = CoreService.getHome().getSchedule();
        for(ScheduledScene scheduledScene : scheduledScenes)
        {
            Intent newIntent = new Intent(CoreService.getContext(), ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

            final int id = (int) System.currentTimeMillis();
            scheduledScene.setIntentId(id);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(CoreService.getContext(), id, newIntent, 0);
            //remember for the future
            alarmIntents.add(alarmIntent);

            Calendar scheduleTime = Calendar.getInstance();

            scheduleTime.setTimeInMillis(System.currentTimeMillis());
            scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
            scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduleTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    public static synchronized ScheduleManager getInstance() {
        if(instance == null)
        {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void update(ScheduledScene scheduledScene) {
        alarmManager.cancel(alarmIntents.get(scheduledScene.getIntentId()));

        Intent newIntent = new Intent(CoreService.getContext(), ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

        final int id = (int) System.currentTimeMillis();
        scheduledScene.setIntentId(id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(CoreService.getContext(), id, newIntent, 0);
        //remember for the future
        alarmIntents.add(alarmIntent);

        Calendar scheduleTime = Calendar.getInstance();

        scheduleTime.setTimeInMillis(System.currentTimeMillis());
        scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
        scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduleTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void add(ScheduledScene scheduledScene) {
        Intent newIntent = new Intent(CoreService.getContext(), ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

        final int id = (int) System.currentTimeMillis();
        scheduledScene.setIntentId(id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(CoreService.getContext(), id, newIntent, 0);
        //remember for the future
        alarmIntents.add(alarmIntent);

        Calendar scheduleTime = Calendar.getInstance();

        scheduleTime.setTimeInMillis(System.currentTimeMillis());
        scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
        scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduleTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void delete(ScheduledScene updatedScene) {
        alarmManager.cancel(alarmIntents.get(updatedScene.getIntentId()));
    }

}
