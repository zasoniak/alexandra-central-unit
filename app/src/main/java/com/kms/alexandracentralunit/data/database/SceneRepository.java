package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Scene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface SceneRepository {

    public boolean add(Scene scene);
    public boolean delete(Scene scene);
    public boolean update(Scene scene);
    public Scene find(String id);
    public List<Scene> getAll();
    public List<Scene> getAllByRoom(String roomId);
    public List<Scene> getAllSubscenes(String id);
}
