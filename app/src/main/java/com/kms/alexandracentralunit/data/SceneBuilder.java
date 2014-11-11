package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.SceneComponent;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SceneBuilder {

    public long id;

    public String name;
    public int offset;

    public List<SceneComponent> children;
    public List<Trigger> triggers;

    public SceneBuilder() {
    }

    public void create(long id, String name) {
        this.id = id;
        this.name = name;
    }



}

