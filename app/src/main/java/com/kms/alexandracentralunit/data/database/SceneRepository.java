package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface SceneRepository {

    public boolean add(Scene scene);
    public boolean delete(Scene scene);
    public boolean update(Scene scene);
    public Scene find(UUID id);
    public List<Scene> getAll();
    public List<Scene> getAllByRoom(UUID roomID);
    public List<Scene> getAllSubscenes(UUID id);
}
