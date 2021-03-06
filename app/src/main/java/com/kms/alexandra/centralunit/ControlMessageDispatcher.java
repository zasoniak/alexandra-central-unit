package com.kms.alexandra.centralunit;


import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;


/**
 * entry point for system remote control
 * <p/>
 * provides access to system control service
 * for different remote control ways
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ControlMessageDispatcher extends IntentService {

    protected Control control;

    public ControlMessageDispatcher() {
        super("Remote Control");
        control = Control.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
