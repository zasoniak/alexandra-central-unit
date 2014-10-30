package com.kms.alexandracentralunit;


import android.content.ContentValues;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class GadgetFactory {

    public static Gadget create(ContentValues values) {
        return new Gadget();
    }
}
