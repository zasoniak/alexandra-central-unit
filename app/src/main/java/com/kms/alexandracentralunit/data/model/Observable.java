package com.kms.alexandracentralunit.data.model;


import android.util.Log;

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
        Log.d("gadget obserwatorzy", String.valueOf(observers.size()));
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers(String field, String value) {
        for(Observer observer : observers)
        {
            observer.update(field, value);
        }
    }
}
