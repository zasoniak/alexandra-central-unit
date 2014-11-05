package com.kms.alexandracentralunit.data;


import android.content.ContentValues;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteTriggerRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-05.
 */
public class TriggerFactory {

    public static Trigger create(ContentValues values) {
        UUID scene = (UUID) values.get(SQLiteTriggerRepository.KEY_TRIGGER_SCENE);
        String action = values.getAsString(SQLiteTriggerRepository.KEY_TRIGGER_ACTION);
        Gadget gadget = GadgetLinker.getInstance(CoreService.getContext()).find((UUID) values.get(SQLiteTriggerRepository.KEY_TRIGGER_GADGET));
        return new Trigger(scene, gadget, action);
    }
}
