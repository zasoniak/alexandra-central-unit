package com.kms.alexandracentralunit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Autostart extends BroadcastReceiver {

    public Autostart() {
    }

    @Override
    public void onReceive(Context context, Intent arg) {
        Intent intent = new Intent(context, CoreService.class);
        context.startService(intent);
    }
}
