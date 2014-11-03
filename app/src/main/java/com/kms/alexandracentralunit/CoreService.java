package com.kms.alexandracentralunit;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.kms.alexandracentralunit.data.GadgetLinker;
import com.kms.alexandracentralunit.data.database.GadgetRepository;
import com.kms.alexandracentralunit.data.database.RoomRepository;
import com.kms.alexandracentralunit.data.database.SceneRepository;
import com.kms.alexandracentralunit.data.database.sqlite.SQLiteGadgetRepository;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CoreService extends Service {

    public static final String UPDATE_MESSAGE = "com.kms.alexandracentralunit.CoreService.UPDATE_MESSAGE";
    public static final String GADGET = "gadgetName";
    private static final String TAG = "CoreService";

    private static Context context;
    public LocalBroadcastManager broadcaster;
    private List<Gadget> gadgets;
    private GadgetRepository gadgetRepository;
    private GadgetLinker gadgetLinker;
    private List<Room> rooms;
    private RoomRepository roomRepository;
    private List<Scene> scenes;
    private SceneRepository sceneRepository;

    public CoreService() {
    }

    public static Context getContext() {
        return CoreService.context;
    }

    @Override
    public void onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this);
        context = getApplicationContext();
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Intent intents = new Intent(getBaseContext(), AdminActivity.class);
        // intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(intents);
        Toast.makeText(this, "Core Service Started", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");

        /**
         * internal system data creation
         * initialization of all required data
         */

        /**
         *  gadgets
         */
        gadgetRepository = new SQLiteGadgetRepository(getApplicationContext());
        gadgetLinker = GadgetLinker.getInstance(getApplicationContext());

        gadgets = new ArrayList<Gadget>();
        gadgets.add(new Gadget(UUID.randomUUID(), 1, UUID.randomUUID(), "test1", "MAC1", 1));
        gadgets.add(new Gadget(UUID.randomUUID(), 1, UUID.randomUUID(), "test2", "MAC2", 2));

        for(Gadget gadget : gadgets)
        {
            gadgetRepository.add(gadget);
        }
        gadgetLinker.loadGadgets();
        for(Gadget gadget : gadgets = gadgetLinker.getAll())
        {
            //TODO: communication setup
            sendResult(gadget.toString());
        }
        /**
         * rooms
         */
        //roomRepository  = new SQLiteRoomRepository(getBaseContext());
        //rooms = roomRepository.getAll();
        /**
         * scenes
         */
        // sceneRepository = new SQLiteSceneRepository(getBaseContext());
        //scenes = sceneRepository.getAll();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void sendResult(String message) {
        Intent intent = new Intent(UPDATE_MESSAGE);
        if(message != null)
        {
            intent.putExtra(GADGET, message);
            broadcaster.sendBroadcast(intent);
        }
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
