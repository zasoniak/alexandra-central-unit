package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteSceneRepository implements SceneRepository {

    private ConfigurationDatabaseHelper databaseHelper;
    private Context context;

    public SQLiteSceneRepository(Context context) {
        this.databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
        this.context = context;
    }

    @Override
    public boolean add(Scene scene) {
        return false;
    }

    @Override
    public boolean delete(Scene scene) {
        return false;
    }

    @Override
    public boolean update(Scene scene) {
        return false;
    }

    @Override
    public Scene find(UUID id) {

        return null;
    }

    @Override
    public List<Scene> getAll() {
        return null;
    }

    @Override
    public List<Scene> getAllByRoom(UUID roomID) {
        return null;
    }

    @Override
    public List<Scene> getAllSubscenes(UUID id) {
        return null;
    }
}
