package com.kms.alexandracentralunit;


import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-14.
 */
public interface ModuleRepository {

    public boolean add(Module module);
    public boolean delete(Module module);
    public boolean update(Module module);
    public Module find(long id);
    public List<Module> getAll();
    public List<Module> getAllByRoom(long roomID);
    public List<Module> getAllByType(int type);
}
