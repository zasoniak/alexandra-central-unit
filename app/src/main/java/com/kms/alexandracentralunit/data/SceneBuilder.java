package com.kms.alexandracentralunit.data;


import android.content.ContentValues;
import android.content.Context;

import com.kms.alexandracentralunit.data.database.ActionRepository;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteActionRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteSceneRepository;
import com.kms.alexandracentralunit.data.model.Action;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SceneBuilder {

    SceneRepository sceneRepository;
    ActionRepository actionRepository;

    //TODO: przemyslec co z triggerami

    public SceneBuilder(Context context) {
        sceneRepository = new SQLiteSceneRepository(context);
        actionRepository = new SQLiteActionRepository(context);
    }

    public Scene buildScene(ContentValues values) {
        //TODO: konstruktor scene oparty na content values z bazy
        Scene scene = new Scene(UUID.randomUUID(), "s", 1);
        addActions(scene);
        addSubscenes(scene);
        addTriggers(scene);

        return scene;
    }

    private void addSubscenes(Scene scene) {
        List<Scene> subscenes = sceneRepository.getAllSubscenes(scene.id);
        for(Scene subscene : subscenes)
        {
            scene.children.add(subscene);
        }
    }

    private void addActions(Scene scene) {
        List<Action> actions = actionRepository.getAllByScene(scene.id);
        for(Action action : actions)
        {
            scene.children.add(action);
        }
    }

    private void addTriggers(Scene scene) {

    }
}
