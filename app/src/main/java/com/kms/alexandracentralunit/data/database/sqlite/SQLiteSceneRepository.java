package com.kms.alexandracentralunit.data.database.sqlite;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteSceneRepository implements SceneRepository {

    private ConfigurationDatabaseHelper databaseHelper;

    public SQLiteSceneRepository(Context context) {
        this.databaseHelper = ConfigurationDatabaseHelper.getInstance(context);
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
    public Scene find(long id) {
        return null;
    }

    @Override
    public List<Scene> getAll() {
        return null;
    }

    @Override
    public List<Scene> getAllByRoom(long roomID) {
        return null;
    }

    @Override
    public List<Scene> getAllSubscenes(long id) {
        return null;
    }
}
