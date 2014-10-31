package com.kms.alexandracentralunit.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteGadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 * <p/>
 * class providing gadget objects linkage between main gadget list and other references
 * use it instead of using
 */
public class GadgetLinker {

    /**
     * singleton pattern
     */
    private static GadgetLinker instance;
    private List<Gadget> gadgets;
    private GadgetRepository gadgetRepository;

    private GadgetLinker(Context context) {
        this.gadgetRepository = new SQLiteGadgetRepository(context);
        this.gadgets = gadgetRepository.getAll();
    }

    public static GadgetLinker getInstance(Context context) {
        if(instance == null)
        {
            instance = new GadgetLinker(context);
        }
        return instance;
    }

    public List<Gadget> getAll() {
        return this.gadgets;
    }

    public Gadget find(ContentValues values) {
        for(Gadget gadget : gadgets)
        {
            if(gadget.getId() == values.get("id"))
            {
                ;
            }
            return gadget;
        }
        return GadgetFactory.create(values);
    }

    public List<Gadget> findMany(Cursor cursor) {

        return null;
    }

}
