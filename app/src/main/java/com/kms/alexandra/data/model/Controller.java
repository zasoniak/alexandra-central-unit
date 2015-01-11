package com.kms.alexandra.data.model;


import com.kms.alexandra.data.model.Actions.BaseAction;


/**
 * @author Mateusz Zasoński
 */
public interface Controller {

    public void queue(BaseAction action);

}
