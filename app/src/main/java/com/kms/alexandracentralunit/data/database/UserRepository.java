package com.kms.alexandracentralunit.data.database;


import com.kms.alexandracentralunit.data.model.User;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 */
public interface UserRepository {

    public boolean add(User user);
    public boolean delete(User user);
    public boolean update(User user);
    public User find(String id);
    public List<User> getAll();

}
