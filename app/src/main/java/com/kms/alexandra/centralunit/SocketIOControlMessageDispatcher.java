package com.kms.alexandra.centralunit;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.kms.alexandra.data.model.Scene;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.socketio.client.IO;
import com.kms.alexandra.data.model.actions.ActionMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.UUID;


/**
 * @author Mateusz Zasonski
 */
public class SocketIOControlMessageDispatcher extends ControlMessageDispatcher  {

    private static final String TAG = "SocketIO";
    private static final String CONNECT = "connect";
    private static final String ADD_CENTRAL = "addcentral";
    private static final String SCENE = "scene";
    private static final String ACTION = "action";
    private static final String URL = "http://control-alexandrargb.rhcloud.com:8000";


    private Socket socket;
    {
        try {
            socket= IO.socket(URL);
        }catch (URISyntaxException ex) {

            Log.e(TAG, ex.toString());
        }
    }

    public SocketIOControlMessageDispatcher() {
        super();

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        Log.e(TAG, "started");
        socket.on(CONNECT, connect);
        socket.on(SCENE, scene);
        socket.on(ACTION,action );
        socket.connect();
        Log.e(TAG, "connected");

    }

    private Emitter.Listener connect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String homeId = sharedPreferences.getString(MainActivity.HOME_ID, "-Jg_uD_kB2NLYkOA6nIo");
            String centralId = "f1e1ddd0-a547-11e4-bcd8-0800200c9a66";
            Log.d(TAG, "central added");
            socket.emit(ADD_CENTRAL, centralId, homeId);

//            String home2="QWERTY";
//            String central="WSAD";
//            socket.emit(ADD_CENTRAL,central,home2);
        }
    };

    private Emitter.Listener scene = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            try
            {
                JSONObject data = (JSONObject)args[1];
                Log.d(TAG, "scena: "+data.toString());
                control.run(data.getString(Scene.ID));

            }
            catch (JSONException e)
            {
                Log.e(TAG, e.toString());
            }
        }
    };

    private Emitter.Listener action = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ActionMessage message;
            try
            {
                JSONObject data = (JSONObject)args[1];
                Log.d(TAG, "akcja: "+data.toString());
                UUID gadgetID = UUID.fromString(data.getString(ActionMessage.GADGET));
                String action1 = data.getString(ActionMessage.ACTION);
                String parameter = data.getString(ActionMessage.PARAMETER);
                if(data.has(ActionMessage.DELAY))
                {
                    long delay = data.getLong(ActionMessage.DELAY);
                    message = new ActionMessage(gadgetID, action1,parameter,delay);
                }
                else
                {
                    message = new ActionMessage(gadgetID, action1,parameter);
                }
                control.run(message);
            }
            catch (Exception e)
            {
                Log.e(TAG, e.toString());
            }
        }
    };

}
