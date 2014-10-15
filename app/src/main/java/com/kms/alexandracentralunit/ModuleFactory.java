package com.kms.alexandracentralunit;

import android.content.ContentValues;

/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public class ModuleFactory {


    public static Module create (ContentValues values)
    {
        return new Module();
    }
}
