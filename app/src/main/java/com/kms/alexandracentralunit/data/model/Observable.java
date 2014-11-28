package com.kms.alexandracentralunit.data.model;


import java.util.ArrayList;


/**
 * implements registration and notifications for object's observers
 *
 * @author Mateusz Zasoński
 * @version 0.1
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
