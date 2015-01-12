package com.kms.alexandra.centralunit;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kms.alexandra.data.model.ScheduledScene;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 * ScheduleManagerService - service responsible for setting up all scheduled scene
 */
public class ScheduleManager {

    public static final String TAG = "ScheduleManager";
    private static ScheduleManager instance;
    private AlarmManager alarmManager;
    private Map<String, PendingIntent> alarmIntents = new HashMap<String, PendingIntent>();
    private List<ScheduledScene> scheduledScenes;
    private Context context;

    private ScheduleManager(Context context) {
        Log.d(TAG, "started");
        this.context = context.getApplicationContext();
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        scheduledScenes = ((Alexandra) context).getHome().getSchedule();
        Log.d(TAG, "currently "+scheduledScenes.size()+" elements to schedule");
        for(ScheduledScene scheduledScene : scheduledScenes)
        {
            Intent newIntent = new Intent(context, ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

            final int id = (int) System.currentTimeMillis();
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, newIntent, 0);
            //remember for the future
            alarmIntents.put(scheduledScene.getId(), alarmIntent);

            Calendar scheduleTime = Calendar.getInstance();

            scheduleTime.setTimeInMillis(System.currentTimeMillis());
            int hour = scheduleTime.get(Calendar.HOUR_OF_DAY);
            int minutes = scheduleTime.get(Calendar.MINUTE);
            if(scheduledScene.getHour() < hour || (scheduledScene.getHour() == hour && scheduledScene.getMinutes() < minutes))
            {
                scheduleTime.set(Calendar.DAY_OF_YEAR, scheduleTime.get(Calendar.DAY_OF_YEAR)+1);
            }
            scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
            scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());
            scheduleTime.set(Calendar.SECOND, 0);

            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, scheduleTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
            alarmManager.setExact(AlarmManager.RTC, scheduleTime.getTimeInMillis(), alarmIntent);
            Log.d(TAG, "alarm set up "+scheduledScene.getId()+" at "+scheduleTime.getTime().toString());
        }
    }

    public static synchronized ScheduleManager getInstance(Context context) {
        if(instance == null)
        {
            instance = new ScheduleManager(context);
        }
        return instance;
    }

    public void update(ScheduledScene scheduledScene) {
        Log.d(TAG, "update "+scheduledScene.getId());
        alarmManager.cancel(alarmIntents.get(scheduledScene.getId()));
        Intent newIntent = new Intent(context, ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

        final int id = (int) System.currentTimeMillis();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, newIntent, 0);
        //remember for the future
        alarmIntents.put(scheduledScene.getId(), alarmIntent);

        Calendar scheduleTime = Calendar.getInstance();

        scheduleTime.setTimeInMillis(System.currentTimeMillis());
        int hour = scheduleTime.get(Calendar.HOUR_OF_DAY);
        int minutes = scheduleTime.get(Calendar.MINUTE);
        if(scheduledScene.getHour() < hour || (scheduledScene.getHour() == hour && scheduledScene.getMinutes() < minutes))
        {
            scheduleTime.set(Calendar.DAY_OF_YEAR, scheduleTime.get(Calendar.DAY_OF_YEAR)+1);
        }
        scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
        scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());
        scheduleTime.set(Calendar.SECOND, 0);

        alarmManager.setExact(AlarmManager.RTC, scheduleTime.getTimeInMillis(), alarmIntent);
        Log.d(TAG, "alarm set up "+scheduledScene.getId()+" at "+scheduleTime.getTime().toString());
    }

    public void add(ScheduledScene scheduledScene) {
        Log.d(TAG, "add "+scheduledScene.getId());
        Intent newIntent = new Intent(context, ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

        final int id = (int) System.currentTimeMillis();
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, newIntent, 0);
        //remember for the future
        alarmIntents.put(scheduledScene.getId(), alarmIntent);

        Calendar scheduleTime = Calendar.getInstance();

        scheduleTime.setTimeInMillis(System.currentTimeMillis());
        int hour = scheduleTime.get(Calendar.HOUR_OF_DAY);
        int minutes = scheduleTime.get(Calendar.MINUTE);
        if(scheduledScene.getHour() < hour || (scheduledScene.getHour() == hour && scheduledScene.getMinutes() < minutes))
        {
            scheduleTime.set(Calendar.DAY_OF_YEAR, scheduleTime.get(Calendar.DAY_OF_YEAR)+1);
        }
        scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
        scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());
        scheduleTime.set(Calendar.SECOND, 0);

        alarmManager.setExact(AlarmManager.RTC, scheduleTime.getTimeInMillis(), alarmIntent);
        Log.d(TAG, "alarm set up "+scheduledScene.getId()+" at "+scheduleTime.getTime().toString());
    }

    public void delete(ScheduledScene scheduledScene) {
        Log.d(TAG, "delete "+scheduledScene.getId());
        alarmManager.cancel(alarmIntents.get(scheduledScene.getId()));
    }

    public void relaunch(String scheduledSceneId) {
        alarmManager.cancel(alarmIntents.get(scheduledSceneId));
        for(ScheduledScene scheduledScene : scheduledScenes)
        {
            if(scheduledScene.getId().equals(scheduledSceneId))
            {
                alarmManager.cancel(alarmIntents.get(scheduledScene.getId()));
                Intent newIntent = new Intent(context, ScheduleReceiver.class).putExtra(ScheduledScene.EXTRA_ID, scheduledScene.getId()).putExtra(ScheduledScene.EXTRA_SCENE_ID, scheduledScene.getScene()).putExtra(ScheduledScene.EXTRA_HOUR, String.valueOf(scheduledScene.getHour())).putExtra(ScheduledScene.EXTRA_MINUTES, scheduledScene.getMinutes()).putExtra(ScheduledScene.EXTRA_DAYS_OF_WEEK, scheduledScene.getDaysOfWeek()).putExtra(ScheduledScene.EXTRA_CONDITIONS, scheduledScene.getConditions());

                final int id = (int) System.currentTimeMillis();
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, id, newIntent, 0);
                //remember for the future
                alarmIntents.put(scheduledScene.getId(), alarmIntent);

                Calendar scheduleTime = Calendar.getInstance();

                scheduleTime.setTimeInMillis(System.currentTimeMillis());
                scheduleTime.set(Calendar.DAY_OF_YEAR, scheduleTime.get(Calendar.DAY_OF_YEAR)+1);
                scheduleTime.set(Calendar.HOUR_OF_DAY, scheduledScene.getHour());
                scheduleTime.set(Calendar.MINUTE, scheduledScene.getMinutes());
                scheduleTime.set(Calendar.SECOND, 0);

                alarmManager.setExact(AlarmManager.RTC, scheduleTime.getTimeInMillis(), alarmIntent);
                Log.d(TAG, "alarm set up "+scheduledScene.getId()+" at "+scheduleTime.getTime().toString());
            }
        }

    }

}
