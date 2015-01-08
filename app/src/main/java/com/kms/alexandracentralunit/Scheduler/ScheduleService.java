package com.kms.alexandracentralunit.Scheduler;


import android.app.IntentService;
import android.content.Intent;

import com.kms.alexandracentralunit.ControlService;
import com.kms.alexandracentralunit.data.model.ScheduledScene;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 * SchedulerService - IntentService responsible for running scheduled scene
 */
public class ScheduleService extends IntentService {

    public ScheduleService() {
        super("Schedule service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ControlService.getInstance().run(intent.getStringExtra(ScheduledScene.EXTRA_SCENE_ID));
        ScheduleReceiver.completeWakefulIntent(intent);
    }
}
