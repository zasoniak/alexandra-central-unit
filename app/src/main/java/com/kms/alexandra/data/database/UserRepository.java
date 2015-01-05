package com.kms.alexandra.data.database;


import com.kms.alexandra.data.model.User;

import java.util.List;


/**
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface UserRepository {

    public boolean add(User user);
    public boolean delete(User user);
    public boolean update(User user);
    public User find(String id);
    public List<User> getAll();

}
