package com.kms.alexandracentralunit;


import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import com.kms.alexandracentralunit.data.model.Home;


/**
 * Created by Mateusz Zasoński on 2014-11-15.
 * RemoteControlService - entry point for system remote control
 * provides access to Home instance for data manipulation
 */
public class RemoteControlService extends IntentService {

    Home home;

    public RemoteControlService() {
        super("Remote Control");
        home = CoreService.getHome();
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
