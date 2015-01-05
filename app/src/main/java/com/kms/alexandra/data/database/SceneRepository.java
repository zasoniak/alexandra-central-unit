package com.kms.alexandra.data.database;


import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.Scene;

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

    public void setHome(Home home);
}
