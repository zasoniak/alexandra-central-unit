package com.kms.alexandracentralunit.data;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Home;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.Trigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


/**
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class FirebaseSyncService extends SyncService {
    //TODO: constants!!!!

    private static final String TAG = "FirebaseSyncService";

    public FirebaseSyncService() {
        super();
        final Firebase homeReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/configuration/"+String.valueOf(CoreService.getHomeId())+"/");

        homeReference.child(Home.NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(CoreService.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(CoreService.HOME_NAME, dataSnapshot.getValue().toString());
                    editor.apply();
                    home.setName(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //TODO: users?
        homeReference.child(Home.USERS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        homeReference.child(Home.ROOMS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild(Room.NAME) && dataSnapshot.hasChild(Room.COLOR))
                {
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child(Room.NAME).getValue().toString();
                    int color = Integer.parseInt(dataSnapshot.child(Room.COLOR).getValue().toString());
                    List<UUID> gadgets = new ArrayList<UUID>();
                    if(dataSnapshot.child(Room.GADGETS).hasChildren())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.child(Room.GADGETS).getChildren())
                        {
                            try
                            {
                                gadgets.add(UUID.fromString(snapshot.child(Room.ID).getValue().toString()));
                            }
                            catch (IllegalArgumentException ex)
                            {
                                Log.e(TAG, "Rooms - gadget UUID parse error");
                            }
                        }
                    }
                    add(new Room(id, name, color, gadgets));
                }
                else
                {
                    Log.e(TAG, "Rooms - missing data");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild(Room.NAME) && dataSnapshot.hasChild(Room.COLOR))
                {
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child(Room.NAME).getValue().toString();
                    int color = Integer.parseInt(dataSnapshot.child(Room.COLOR).getValue().toString());
                    List<UUID> gadgets = new ArrayList<UUID>();
                    if(dataSnapshot.child(Room.GADGETS).hasChildren())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.child(Room.GADGETS).getChildren())
                        {
                            try
                            {
                                gadgets.add(UUID.fromString(snapshot.child(Room.ID).getValue().toString()));
                            }
                            catch (IllegalArgumentException ex)
                            {
                                Log.e(TAG, "Rooms - gadget UUID parse error");
                            }
                        }
                    }
                    update(new Room(id, name, color, gadgets));
                }
                else
                {
                    Log.e(TAG, "Rooms - missing data");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Room.NAME) && dataSnapshot.hasChild(Room.COLOR))
                {
                    String id = dataSnapshot.getKey();
                    String name = dataSnapshot.child(Room.NAME).getValue().toString();
                    int color = Integer.parseInt(dataSnapshot.child(Room.COLOR).getValue().toString());
                    List<UUID> gadgets = new ArrayList<UUID>();
                    if(dataSnapshot.child(Room.GADGETS).hasChildren())
                    {
                        for(DataSnapshot snapshot : dataSnapshot.child(Room.GADGETS).getChildren())
                        {
                            try
                            {
                                gadgets.add(UUID.fromString(snapshot.child(Room.ID).getValue().toString()));
                            }
                            catch (IllegalArgumentException ex)
                            {
                                Log.e(TAG, "Rooms - gadget UUID parse error");
                            }
                        }
                    }
                    delete(new Room(id, name, color, gadgets));
                }
                else
                {
                    Log.e(TAG, "Rooms - missing data");
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        homeReference.child(Home.GADGETS).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE))
                {
                    try
                    {
                        UUID id = UUID.fromString(dataSnapshot.getKey());
                        String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                        String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                        String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                        Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                        add(GadgetFactory.create(id, roomId, name, MAC, type, 2));
                    }
                    catch (IllegalArgumentException ex)
                    {
                        Log.e(TAG, "Gadget - UUID parse error");
                    }
                }
                else
                {
                    Log.e(TAG, "Gadget - missing data");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE))
                {
                    try
                    {
                        UUID id = UUID.fromString(dataSnapshot.getKey());
                        String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                        String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                        String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                        Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                        update(GadgetFactory.create(id, roomId, name, MAC, type, 2));
                    }
                    catch (IllegalArgumentException ex)
                    {
                        Log.e(TAG, "Gadget - UUID parse error");
                    }
                }
                else
                {
                    Log.e(TAG, "Gadget - missing data");
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(Gadget.ROOM_ID) && dataSnapshot.hasChild(Gadget.NAME) && dataSnapshot.hasChild(Gadget.MAC_ADDRESS) && dataSnapshot.hasChild(Gadget.TYPE))
                {
                    try
                    {
                        UUID id = UUID.fromString(dataSnapshot.getKey());
                        String roomId = dataSnapshot.child(Gadget.ROOM_ID).getValue().toString();
                        String name = dataSnapshot.child(Gadget.NAME).getValue().toString();
                        String MAC = dataSnapshot.child(Gadget.MAC_ADDRESS).getValue().toString();
                        Gadget.GadgetType type = Gadget.GadgetType.valueOf(dataSnapshot.child(Gadget.TYPE).getValue().toString());
                        delete(GadgetFactory.create(id, roomId, name, MAC, type, 2));
                    }
                    catch (IllegalArgumentException ex)
                    {
                        Log.e(TAG, "Gadget - UUID parse error");
                    }
                }
                else
                {
                    Log.e(TAG, "Gadget - missing data");
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        TimerTask taskScenes = new TimerTask() {
            @Override
            public void run() {
                homeReference.child(Home.SCENES).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.hasChild(Scene.NAME) && (dataSnapshot.hasChild(Scene.SUBSCENES) || dataSnapshot.hasChild(Scene.ACTIONS)))
                        {
                            String id = dataSnapshot.getKey();
                            String name = dataSnapshot.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(home);
                            builder.create(id, name);
                            List<Trigger> triggers = new ArrayList<Trigger>();

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */
                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            for(DataSnapshot actionSnapshot : dataSnapshot.child(Scene.ACTIONS).getChildren())
                            {
                                String action = actionSnapshot.child(ActionMessage.ACTION).getValue().toString();
                                try
                                {
                                    UUID gadget = UUID.fromString(actionSnapshot.child(ActionMessage.GADGET).getValue().toString());
                                    String parameter = actionSnapshot.child(ActionMessage.PARAMETER).getValue().toString();
                                    long delay = Long.parseLong(actionSnapshot.child(ActionMessage.DELAY).getValue().toString());
                                    actions.add(new ActionMessage(gadget, action, parameter, delay));
                                }
                                catch (IllegalArgumentException ex)
                                {
                                    Log.e(TAG, "Scene - action - gadget UUID parse error");
                                }
                            }
                            builder.addActions(actions);

                            /**
                             * getting subscenes' ID list
                             * and passing it to scene builder
                             */
                            List<String> subscenes = new ArrayList<String>();
                            for(DataSnapshot subsceneSnapshot : dataSnapshot.child(Scene.SUBSCENES).getChildren())
                            {
                                subscenes.add(subsceneSnapshot.child(Scene.ID).getValue().toString());
                            }
                            builder.addSubscenes(subscenes);

                            /**
                             * first step of trigger creation
                             * for next step passing triggers list to scene builder
                             */
                            for(DataSnapshot triggerSnapshot : dataSnapshot.child(Scene.TRIGGERS).getChildren())
                            {
                                Trigger trigger = new Trigger(id);
                                for(DataSnapshot condition : triggerSnapshot.child(Trigger.CONDITIONS).getChildren())
                                {
                                    try
                                    {
                                        UUID gadgetID = UUID.fromString(condition.child(Trigger.CONDITION_GADGET).getValue().toString());
                                        String parameter = condition.child(Trigger.CONDITION_PARAMETER).getValue().toString();
                                        String value = condition.child(Trigger.CONDITION_VALUE).getValue().toString();
                                        trigger.addObserver(gadgetID, parameter, value);
                                    }
                                    catch (IllegalArgumentException ex)
                                    {
                                        Log.e(TAG, "scene - trigger - gadget UUID parse error");
                                    }
                                }
                                triggers.add(trigger);
                            }
                            builder.addTriggers(triggers);
                            add(builder.getScene());
                        }
                        else
                        {
                            Log.e(TAG, "Scene - missing data");
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.hasChild(Scene.NAME) && (dataSnapshot.hasChild(Scene.SUBSCENES) || dataSnapshot.hasChild(Scene.ACTIONS)))
                        {
                            String id = dataSnapshot.getKey();
                            String name = dataSnapshot.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(home);
                            builder.create(id, name);
                            List<Trigger> triggers = new ArrayList<Trigger>();

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */
                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            for(DataSnapshot actionSnapshot : dataSnapshot.child(Scene.ACTIONS).getChildren())
                            {
                                String action = actionSnapshot.child(ActionMessage.ACTION).getValue().toString();
                                try
                                {
                                    UUID gadget = UUID.fromString(actionSnapshot.child(ActionMessage.GADGET).getValue().toString());
                                    String parameter = actionSnapshot.child(ActionMessage.PARAMETER).getValue().toString();
                                    long delay = Long.parseLong(actionSnapshot.child(ActionMessage.DELAY).getValue().toString());
                                    actions.add(new ActionMessage(gadget, action, parameter, delay));
                                }
                                catch (IllegalArgumentException ex)
                                {
                                    Log.e(TAG, "Scene - action - gadget UUID parse error");
                                }
                            }
                            builder.addActions(actions);

                            /**
                             * getting subscenes' ID list
                             * and passing it to scene builder
                             */
                            List<String> subscenes = new ArrayList<String>();
                            for(DataSnapshot subsceneSnapshot : dataSnapshot.child(Scene.SUBSCENES).getChildren())
                            {
                                subscenes.add(subsceneSnapshot.child(Scene.ID).getValue().toString());
                            }
                            builder.addSubscenes(subscenes);

                            /**
                             * first step of trigger creation
                             * for next step passing triggers list to scene builder
                             */
                            for(DataSnapshot triggerSnapshot : dataSnapshot.child(Scene.TRIGGERS).getChildren())
                            {
                                Trigger trigger = new Trigger(id);
                                for(DataSnapshot condition : triggerSnapshot.child(Trigger.CONDITIONS).getChildren())
                                {
                                    try
                                    {
                                        UUID gadgetID = UUID.fromString(condition.child(Trigger.CONDITION_GADGET).getValue().toString());
                                        String parameter = condition.child(Trigger.CONDITION_PARAMETER).getValue().toString();
                                        String value = condition.child(Trigger.CONDITION_VALUE).getValue().toString();
                                        trigger.addObserver(gadgetID, parameter, value);
                                    }
                                    catch (IllegalArgumentException ex)
                                    {
                                        Log.e(TAG, "scene - trigger - gadget UUID parse error");
                                    }
                                }
                                triggers.add(trigger);
                            }
                            builder.addTriggers(triggers);
                            update(builder.getScene());
                        }
                        else
                        {
                            Log.e(TAG, "Scene - missing data");
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(Scene.NAME) && (dataSnapshot.hasChild(Scene.SUBSCENES) || dataSnapshot.hasChild(Scene.ACTIONS)))
                        {
                            String id = dataSnapshot.getKey();
                            String name = dataSnapshot.child(Scene.NAME).getValue().toString();
                            SceneBuilder builder = new SceneBuilder(home);
                            builder.create(id, name);
                            List<Trigger> triggers = new ArrayList<Trigger>();

                            /**
                             * essential action data encapsulation
                             * and passing it to scene builder
                             */
                            List<ActionMessage> actions = new ArrayList<ActionMessage>();
                            for(DataSnapshot actionSnapshot : dataSnapshot.child(Scene.ACTIONS).getChildren())
                            {
                                String action = actionSnapshot.child(ActionMessage.ACTION).getValue().toString();
                                try
                                {
                                    UUID gadget = UUID.fromString(actionSnapshot.child(ActionMessage.GADGET).getValue().toString());
                                    String parameter = actionSnapshot.child(ActionMessage.PARAMETER).getValue().toString();
                                    long delay = Long.parseLong(actionSnapshot.child(ActionMessage.DELAY).getValue().toString());
                                    actions.add(new ActionMessage(gadget, action, parameter, delay));
                                }
                                catch (IllegalArgumentException ex)
                                {
                                    Log.e(TAG, "Scene - action - gadget UUID parse error");
                                }
                            }
                            builder.addActions(actions);

                            /**
                             * getting subscenes' ID list
                             * and passing it to scene builder
                             */
                            List<String> subscenes = new ArrayList<String>();
                            for(DataSnapshot subsceneSnapshot : dataSnapshot.child(Scene.SUBSCENES).getChildren())
                            {
                                subscenes.add(subsceneSnapshot.child(Scene.ID).getValue().toString());
                            }
                            builder.addSubscenes(subscenes);

                            /**
                             * first step of trigger creation
                             * for next step passing triggers list to scene builder
                             */
                            for(DataSnapshot triggerSnapshot : dataSnapshot.child(Scene.TRIGGERS).getChildren())
                            {
                                Trigger trigger = new Trigger(id);
                                for(DataSnapshot condition : triggerSnapshot.child(Trigger.CONDITIONS).getChildren())
                                {
                                    try
                                    {
                                        UUID gadgetID = UUID.fromString(condition.child(Trigger.CONDITION_GADGET).getValue().toString());
                                        String parameter = condition.child(Trigger.CONDITION_PARAMETER).getValue().toString();
                                        String value = condition.child(Trigger.CONDITION_VALUE).getValue().toString();
                                        trigger.addObserver(gadgetID, parameter, value);
                                    }
                                    catch (IllegalArgumentException ex)
                                    {
                                        Log.e(TAG, "scene - trigger - gadget UUID parse error");
                                    }
                                }
                                triggers.add(trigger);
                            }
                            builder.addTriggers(triggers);
                            delete(builder.getScene());
                        }
                        else
                        {
                            Log.e(TAG, "Scene - missing data");
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        };
        Timer timerScenes = new Timer();
        timerScenes.schedule(taskScenes, 5000);

        TimerTask taskSchedules = new TimerTask() {
            @Override
            public void run() {
                homeReference.child(Home.SCHEDULE).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.hasChild(ScheduledScene.SCENE) && dataSnapshot.hasChild(ScheduledScene.TIME) && dataSnapshot.hasChild(ScheduledScene.REPEAT_INTERVAL))
                        {
                            try
                            {
                                String id = dataSnapshot.getKey();
                                String scene = dataSnapshot.child(ScheduledScene.SCENE).getValue().toString();
                                long time = Long.parseLong(dataSnapshot.child(ScheduledScene.TIME).getValue().toString());
                                long repeatInterval = Long.parseLong(dataSnapshot.child(ScheduledScene.REPEAT_INTERVAL).getValue().toString());
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                for(DataSnapshot snapshot : dataSnapshot.child(ScheduledScene.CONDITIONS).getChildren())
                                {
                                    if(snapshot.hasChild(ScheduledScene.CONDITION_TYPE) && snapshot.hasChild(ScheduledScene.CONDITION_VALUE))
                                    {
                                        conditions.put(snapshot.child(ScheduledScene.CONDITION_TYPE).getValue().toString(), snapshot.child(ScheduledScene.CONDITION_VALUE).getValue().toString());
                                    }
                                }
                                add(new ScheduledScene(id, scene, time, repeatInterval, conditions));
                            }
                            catch (NumberFormatException ex)
                            {
                                Log.e(TAG, "Schedule - long parse error");
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Schedule - missing data");
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        if(dataSnapshot.hasChild(ScheduledScene.SCENE) && dataSnapshot.hasChild(ScheduledScene.TIME) && dataSnapshot.hasChild(ScheduledScene.REPEAT_INTERVAL))
                        {
                            try
                            {
                                String id = dataSnapshot.getKey();
                                String scene = dataSnapshot.child(ScheduledScene.SCENE).getValue().toString();
                                long time = Long.parseLong(dataSnapshot.child(ScheduledScene.TIME).getValue().toString());
                                long repeatInterval = Long.parseLong(dataSnapshot.child(ScheduledScene.REPEAT_INTERVAL).getValue().toString());
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                for(DataSnapshot snapshot : dataSnapshot.child(ScheduledScene.CONDITIONS).getChildren())
                                {
                                    if(snapshot.hasChild(ScheduledScene.CONDITION_TYPE) && snapshot.hasChild(ScheduledScene.CONDITION_VALUE))
                                    {
                                        conditions.put(snapshot.child(ScheduledScene.CONDITION_TYPE).getValue().toString(), snapshot.child(ScheduledScene.CONDITION_VALUE).getValue().toString());
                                    }
                                }
                                update(new ScheduledScene(id, scene, time, repeatInterval, conditions));
                            }
                            catch (NumberFormatException ex)
                            {
                                Log.e(TAG, "Schedule - long parse error");
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Schedule - missing data");
                        }
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(ScheduledScene.SCENE) && dataSnapshot.hasChild(ScheduledScene.TIME) && dataSnapshot.hasChild(ScheduledScene.REPEAT_INTERVAL))
                        {
                            try
                            {
                                String id = dataSnapshot.getKey();
                                String scene = dataSnapshot.child(ScheduledScene.SCENE).getValue().toString();
                                long time = Long.parseLong(dataSnapshot.child(ScheduledScene.TIME).getValue().toString());
                                long repeatInterval = Long.parseLong(dataSnapshot.child(ScheduledScene.REPEAT_INTERVAL).getValue().toString());
                                HashMap<String, String> conditions = new HashMap<String, String>();
                                for(DataSnapshot snapshot : dataSnapshot.child(ScheduledScene.CONDITIONS).getChildren())
                                {
                                    if(snapshot.hasChild(ScheduledScene.CONDITION_TYPE) && snapshot.hasChild(ScheduledScene.CONDITION_VALUE))
                                    {
                                        conditions.put(snapshot.child(ScheduledScene.CONDITION_TYPE).getValue().toString(), snapshot.child(ScheduledScene.CONDITION_VALUE).getValue().toString());
                                    }
                                }
                                delete(new ScheduledScene(id, scene, time, repeatInterval, conditions));
                            }
                            catch (NumberFormatException ex)
                            {
                                Log.e(TAG, "Schedule - long parse error");
                            }
                        }
                        else
                        {
                            Log.e(TAG, "Schedule - missing data");
                        }
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        };
        Timer timerSchedules = new Timer();
        timerSchedules.schedule(taskSchedules, 8000);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("firebase", "intentservice");
        super.onHandleIntent(intent);
    }
}
