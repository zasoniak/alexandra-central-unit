package com.kms.alexandracentralunit.data.database.json;


import com.kms.alexandracentralunit.data.database.UserRepository;
import com.kms.alexandracentralunit.data.model.User;

import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-11-11.
 */
public class JSONUserRepository implements UserRepository {

    @Override
    public boolean add(User user) {
        return false;
    }

    @Override
    public boolean delete(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User find(String id) {
        return null;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
