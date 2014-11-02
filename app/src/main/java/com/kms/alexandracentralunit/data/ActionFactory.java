package com.kms.alexandracentralunit.data;


import android.content.ContentValues;

import com.kms.alexandracentralunit.data.database.sqlite.SQLiteActionRepository;
import com.kms.alexandracentralunit.data.model.Action;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-11-02.
 */
public class ActionFactory {

    public static Action create(ContentValues values) {

        String actionCode = values.getAsString(SQLiteActionRepository.KEY_ACTION_GADGET);
        int offset = values.getAsInteger(SQLiteActionRepository.KEY_ACTION_OFFSET);
        Gadget gadget = GadgetLinker.find((UUID) values.get(SQLiteActionRepository.KEY_ACTION_GADGET));

        return new Action(gadget, actionCode, offset);
    }

}
