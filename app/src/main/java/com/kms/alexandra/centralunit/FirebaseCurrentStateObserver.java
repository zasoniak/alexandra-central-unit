package com.kms.alexandra.centralunit;


import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.firebase.client.Firebase;
import com.kms.alexandra.data.model.Observer;
import com.kms.alexandra.data.model.gadgets.Gadget;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class FirebaseCurrentStateObserver extends CurrentStateObserver {

    private static FirebaseCurrentStateObserver instance;
    List<FirebaseGadgetObserver> observers = new ArrayList<FirebaseGadgetObserver>();
    private Firebase currentStateReference;

    private FirebaseCurrentStateObserver() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Alexandra.getContext());
        String homeId = sharedPreferences.getString(MainActivity.HOME_ID, "-JcMyexVThw7PEv2Z2PL");

        String FIREBASE_ROOT = "https://sizzling-torch-8921.firebaseio.com/currentState/"+homeId+"/";
        currentStateReference = new Firebase(FIREBASE_ROOT);
    }

    public static synchronized FirebaseCurrentStateObserver getInstance() {
        if(instance == null)
        {
            instance = new FirebaseCurrentStateObserver();
        }
        return instance;
    }

    public void addGadget(Gadget gadget) {
        FirebaseGadgetObserver observer = new FirebaseGadgetObserver(gadget);
        observer.register();
        observer.pushCurrentState();
        observers.add(observer);
    }

    class FirebaseGadgetObserver implements Observer {

        private Gadget gadgetReference;

        public FirebaseGadgetObserver(Gadget gadgetReference) {
            this.gadgetReference = gadgetReference;
        }

        public void register() {
            this.gadgetReference.registerObserver(this);
        }

        public void pushCurrentState() {

            currentStateReference.child(gadgetReference.getId().toString()).setValue(gadgetReference.getCurrentState());
        }

        @Override
        public void update(final String field, String value) {
            currentStateReference.child(gadgetReference.getId().toString()).setValue(gadgetReference.getCurrentState());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    currentStateReference.child(gadgetReference.getId().toString()).setValue(gadgetReference.getCurrentState());
                }
            });

        }
    }
}
