package com.kms.alexandracentralunit.data;


import android.content.ContentValues;

import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteActionRepository;
import com.kms.alexandracentralunit.data.model.Action;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-02.
 */
public class ActionFactory {

    public static Action create(ContentValues values) {

        UUID scene = (UUID) values.get(SQLiteActionRepository.KEY_ACTION_SCENE);
        String actionCode = values.getAsString(SQLiteActionRepository.KEY_ACTION_GADGET);
        int offset = values.getAsInteger(SQLiteActionRepository.KEY_ACTION_OFFSET);
        Gadget gadget = GadgetLinker.getInstance(CoreService.getContext()).find((UUID) values.get(SQLiteActionRepository.KEY_ACTION_GADGET));

        return new Action(scene, gadget, actionCode, offset);
    }

}
