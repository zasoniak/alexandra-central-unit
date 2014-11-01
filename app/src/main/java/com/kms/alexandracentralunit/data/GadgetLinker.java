package com.kms.alexandracentralunit.data;


import android.content.ContentValues;
import android.content.Context;

import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteGadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 * <p/>
 * class providing gadget objects linkage between main gadget list and other references
 */
public class GadgetLinker {

    /**
     * singleton pattern - provides single, unique list of gadgets inside
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

    public List<Gadget> getAll(List<ContentValues> valuesList) {

        boolean existFlag;
        for(ContentValues values : valuesList)
        {
            existFlag = false;
            for(Gadget gadget : this.gadgets)
            {
                if(gadget.getId() == values.get(SQLiteGadgetRepository.KEY_GADGET_ID))
                {
                    existFlag = true;
                }
            }
            if(!existFlag)
            {
                this.gadgets.add(GadgetFactory.create(values));
            }
        }
        return this.gadgets;
    }

    public Gadget find(ContentValues values) {
        for(Gadget gadget : this.gadgets)
        {
            if(gadget.getId() == values.get(SQLiteGadgetRepository.KEY_GADGET_ID))
            {
                return gadget;
            }
        }
        this.gadgets.add(GadgetFactory.create(values));
        return this.gadgets.get(gadgets.size()-1);
    }
}
