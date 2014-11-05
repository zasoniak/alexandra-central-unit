package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-02.
 */
public interface TriggerRepository {

    public boolean add(Trigger trigger);
    public boolean delete(Trigger trigger);
    public List<Trigger> getAll();
    public List<Trigger> getAllByScene(UUID sceneID);

}
