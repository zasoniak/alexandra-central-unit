package com.kms.alexandracentralunit;


import com.kms.alexandracentralunit.Models.Gadget;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 * <p/>
 * class providing gadget objects linkage between main gadget list and other references
 */
public class GadgetLinker {

    /**
     * singleton pattern
     */
    private static GadgetLinker instance;
    private List<Gadget> gadgets;

    public GadgetLinker() {
    }

    private GadgetLinker(List<Gadget> gadgets) {
        this.gadgets = gadgets;
    }

    public static void initialize(List<Gadget> gadgetsList) {
        if(instance == null)
        {
            instance = new GadgetLinker(gadgetsList);
        }
        else
        {
            instance.gadgets = gadgetsList;
        }
    }

    public static GadgetLinker getInstance() {
        if(instance == null)
        {
            instance = new GadgetLinker();
        }
        return instance;
    }

}
