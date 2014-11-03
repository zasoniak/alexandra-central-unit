package com.kms.alexandracentralunit.data;


import android.content.Context;

import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteGadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


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
    private List<Gadget> gadgets = new ArrayList<Gadget>();
    private GadgetRepository gadgetRepository;

    private GadgetLinker(Context context) {
        this.gadgetRepository = new SQLiteGadgetRepository(context);
    }

    public static GadgetLinker getInstance(Context context) {
        if(instance == null)
        {
            instance = new GadgetLinker(context);
        }
        return instance;
    }

    public Gadget find(UUID id) {
        for(Gadget gadget : this.gadgets)
        {
            if(gadget.getId().equals(id))
            {
                return gadget;
            }
        }
        return null;
    }

    public void loadGadgets() {
        this.gadgets = this.gadgetRepository.getAll();
    }

    public List<Gadget> getAll() {
        return this.gadgets;
    }
}
