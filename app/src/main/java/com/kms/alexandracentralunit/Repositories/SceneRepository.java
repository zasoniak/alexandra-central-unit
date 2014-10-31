package com.kms.alexandracentralunit.Repositories;


import com.kms.alexandracentralunit.Models.Scene;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface SceneRepository {

    public boolean add(Scene scene);
    public boolean delete(Scene scene);
    public boolean update(Scene scene);
    public Scene find(long id);
    public List<Scene> getAll();
    public List<Scene> getAllByRoom(long roomID);
}
