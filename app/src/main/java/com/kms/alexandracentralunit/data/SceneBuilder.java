package com.kms.alexandracentralunit.data;


import android.content.ContentValues;
import android.content.Context;

import com.kms.alexandracentralunit.data.database.ActionRepository;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.database.TriggerRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteActionRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteSceneRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteTriggerRepository;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SceneBuilder {

    SceneRepository sceneRepository;
    ActionRepository actionRepository;
    TriggerRepository triggerRepository;
    Scene scene;

    //TODO: przemyslec co z triggerami

    public SceneBuilder(Context context, SceneRepository sceneRepository) {
        this.sceneRepository = sceneRepository;
        this.actionRepository = new SQLiteActionRepository(context);
        this.triggerRepository = new SQLiteTriggerRepository(context);
    }

    public void buildScene(ContentValues values) {

        UUID id = UUID.fromString(values.getAsString(SQLiteSceneRepository.KEY_SCENE_ID));
        long system_id = values.getAsLong(SQLiteSceneRepository.KEY_SCENE_SYSTEM);
        String name = values.getAsString(SQLiteSceneRepository.KEY_SCENE_NAME);
        int offset = values.getAsInteger(SQLiteSceneRepository.KEY_SUBSCENE_OFFSET);
        this.scene = new Scene(id, system_id, name, offset);
    }

    public void addSubscenes() {
        this.scene.children.addAll(sceneRepository.getAllSubscenes(this.scene.getId()));
    }

    public void addActions() {
        this.scene.children.addAll(actionRepository.getAllByScene(this.scene.getId()));
    }

    public void addTriggers() {
        this.scene.triggers.addAll(triggerRepository.getAllByScene(this.scene.getId()));
    }

    public Scene getScene() {
        return this.scene;
    }
}
