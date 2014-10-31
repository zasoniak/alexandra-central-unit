package com.kms.alexandracentralunit;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.kms.alexandracentralunit.model.Gadget;
import com.kms.alexandracentralunit.model.Room;
import com.kms.alexandracentralunit.model.Scene;
import com.kms.alexandracentralunit.repository.GadgetRepository;
import com.kms.alexandracentralunit.repository.RoomRepository;
import com.kms.alexandracentralunit.repository.SceneRepository;

import java.util.List;


public class CoreService extends Service {

    private static final String TAG = "CoreService";

    private List<Gadget> gadgets;
    private GadgetRepository gadgetRepository;
    private List<Room> rooms;
    private RoomRepository roomRepository;
    private List<Scene> scenes;
    private SceneRepository sceneRepository;

    public CoreService() {
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Intent intents = new Intent(getBaseContext(), AdminActivity.class);
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intents);
        Toast.makeText(this, "Core Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");

        /**
         * internal system data creation
         * initialization of all required data
         */
        gadgetRepository = new SQLiteGadgetRepository(getBaseContext());
        //roomRepository  = new SQLiteRoomRepository(getBaseContext());
        // sceneRepository = new SQLiteSceneRepository(getBaseContext());

        for(Gadget gadget : gadgets = gadgetRepository.getAll())
        {
            //TODO: communication setup
        }
        ;
        //rooms = roomRepository.getAll();

        //scenes = sceneRepository.getAll();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * log data
     */
    private void logData() {
        Intent intent = new Intent(getBaseContext(), HistorianBroadcastReceiver.class);
        intent.putExtra("table", "measurements");
        intent.putExtra("type", "temperature");
        intent.putExtra("value", "XXX");
        sendBroadcast(intent);
    }

}
