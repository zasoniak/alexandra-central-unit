package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Action;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface ActionRepository {

    public boolean add(Action action);
    public boolean delete(Action action);
    public boolean update(Action action);
    public List<Action> getAllByScene(UUID sceneID);

}
