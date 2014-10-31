package com.kms.alexandracentralunit.data;


import com.kms.alexandracentralunit.data.model.Scene;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public class SceneBuilder {

    public Scene buildScene() {
        Scene scene = new Scene(UUID.randomUUID(), "s", 1);

        return scene;
    }

    private void addSubscenes() {

    }

    private void addActions() {

    }
}
