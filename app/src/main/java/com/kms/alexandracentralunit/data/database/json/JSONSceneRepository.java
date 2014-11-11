package com.kms.alexandracentralunit.data.database.json;


import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class JSONSceneRepository implements SceneRepository {

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
