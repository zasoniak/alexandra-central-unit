package com.kms.alexandra.centralunit;


import android.app.IntentService;
import android.content.Intent;

import com.kms.alexandra.data.model.ScheduledScene;


/**
 * service to run scheduled scene
 * <p/>
 * prepared to run as WakefulService
 * allowing to start even during sleep state
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ScheduleService extends IntentService {

    public ScheduleService() {
        super("Schedule service");
    }

    /**
     * @param intent contains sceneId
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Control.getInstance().run(intent.getStringExtra(ScheduledScene.EXTRA_SCENE_ID));
        ScheduleReceiver.completeWakefulIntent(intent);
    }
}
