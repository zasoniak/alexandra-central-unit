package com.kms.alexandracentralunit;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public interface ModuleRepository {
    public boolean add(Module module, Context context);
    public boolean delete(Module module, Context context);
    public boolean update(Module module, Context context);
    public Module find(int id);
    public ArrayList<Module> getAll();
    public ArrayList<Module> getAllByRoom(int roomId);
    public ArrayList<Module> getAllByType(int type);
}
