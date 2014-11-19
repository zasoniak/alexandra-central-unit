package com.kms.alexandracentralunit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * Created by Mateusz Zasoński on 2014-11-19.
 * Autostart  - receiver allowing to run on device startup
 */
public class Autostart extends BroadcastReceiver {

    public Autostart() {
    }

    @Override
    public void onReceive(Context context, Intent arg) {
        Intent intent = new Intent(context, CoreService.class);
        context.startService(intent);
    }
}
