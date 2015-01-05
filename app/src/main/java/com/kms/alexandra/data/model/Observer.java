package com.kms.alexandra.data.model;


/**
 * basic interface for Observer classes
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public interface Observer {

    public void update(String field, String value);
}
