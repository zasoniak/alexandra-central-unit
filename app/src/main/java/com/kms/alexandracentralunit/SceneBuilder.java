package com.kms.alexandracentralunit;


import com.kms.alexandracentralunit.model.Scene;

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
