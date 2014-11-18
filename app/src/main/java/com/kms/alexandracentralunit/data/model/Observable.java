package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;


/**
 * Created by Mateusz Zasoński on 2014-11-18.
 */
public class Observable {

    private ArrayList<Observer> observers = new ArrayList<Observer>();

    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(String field, Object value) {
        for(Observer observer : observers)
        {
            observer.update(field, value);
        }
    }
}
