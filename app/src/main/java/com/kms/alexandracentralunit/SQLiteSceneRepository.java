package com.kms.alexandracentralunit;


import com.kms.alexandracentralunit.model.Scene;
import com.kms.alexandracentralunit.repository.SceneRepository;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SQLiteSceneRepository implements SceneRepository {

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
}
