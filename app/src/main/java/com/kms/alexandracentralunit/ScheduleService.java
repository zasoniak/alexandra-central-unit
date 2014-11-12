package com.kms.alexandracentralunit;


import android.app.IntentService;
import android.content.Intent;

import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteSceneRepository;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class ScheduleService extends IntentService {

    SceneRepository repository;
    Scene scene;

    public ScheduleService() {
        super("Schedule service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //build scene
        repository = new SQLiteSceneRepository(CoreService.getContext());
        scene = repository.find(intent.getStringExtra(ScheduledScene.EXTRA_SCENE_ID));
        //start scene
        scene.start();
        //completed
        ScheduleReceiver.completeWakefulIntent(intent);
    }
}
