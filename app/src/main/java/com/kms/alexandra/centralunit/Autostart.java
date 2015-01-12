package com.kms.alexandra.centralunit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


/**
 * receiver allowing to run application on device startup
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class Autostart extends BroadcastReceiver {

    public Autostart() {
    }

    @Override
    public void onReceive(Context context, Intent arg) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
