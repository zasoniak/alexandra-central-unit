package com.kms.alexandra.data;


import android.util.Log;

import com.firebase.client.Firebase;
import com.kms.alexandra.data.model.Gadget;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.SceneComponent;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.Trigger;
import com.kms.alexandra.data.model.User;
import com.kms.alexandra.data.model.actions2.BaseAction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Firebase synchronization for HomeManager
 * <p/>
 * Synchronize changes with remote Firebase server
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class FirebaseHomeManagerDecorator extends HomeManagerDecorator {

    private Firebase firebaseRoot;

    public FirebaseHomeManagerDecorator(HomeManager homeManager) {
        super(homeManager);
        String FIREBASE_URL = "https://sizzling-torch-8921.firebaseio.com/configuration/"+homeManager.home.getId()+"/";
        firebaseRoot = new Firebase(FIREBASE_URL);
    }

    @Override
    public boolean add(Room room) {
        super.add(room);
        return addAndSync(room);
    }

    @Override
    public boolean delete(Room room) {
        super.delete(room);
        return deleteAndSync(room);
    }

    @Override
    public boolean update(Room room) {
        super.update(room);
        return updateAndSync(room);
    }

    @Override
    public boolean add(Gadget newGadget) {
        super.add(newGadget);
        return addAndSync(newGadget);
    }

    @Override
    public boolean delete(Gadget gadget) {
        super.delete(gadget);
        return deleteAndSync(gadget);
    }

    @Override
    public boolean update(Gadget newGadget) {
        super.update(newGadget);
        return updateAndSync(newGadget);
    }

    @Override
    public boolean add(Scene scene) {
        super.add(scene);
        return addAndSync(scene);
    }

    @Override
    public boolean delete(Scene scene) {
        super.delete(scene);
        return deleteAndSync(scene);
    }

    @Override
    public boolean update(Scene newScene) {
        super.update(newScene);
        return updateAndSync(newScene);
    }

    @Override
    public boolean add(ScheduledScene scheduledscene) {
        super.add(scheduledscene);
        return addAndSync(scheduledscene);
    }

    @Override
    public boolean delete(ScheduledScene scheduledscene) {
        super.delete(scheduledscene);
        return deleteAndSync(scheduledscene);
    }

    @Override
    public boolean update(ScheduledScene scheduledscene) {
        super.update(scheduledscene);
        return updateAndSync(scheduledscene);
    }

    @Override
    public boolean add(User user) {
        super.add(user);
        return addAndSync(user);
    }

    @Override
    public boolean delete(User user) {
        super.delete(user);
        return deleteAndSync(user);
    }

    @Override
    public boolean update(User user) {
        super.update(user);
        return updateAndSync(user);
    }

    private boolean addAndSync(Room room) {
        Firebase roomRoot = firebaseRoot.child("rooms");
        Firebase roomIdRef = roomRoot.push();
        room.setId(roomIdRef.getKey());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Room.NAME, room.getName());
        hashMap.put(Room.COLOR, room.getColor());
        roomIdRef.setValue(hashMap);
        return true;
    }

    private boolean deleteAndSync(Room room) {
        firebaseRoot.child("rooms").child(room.getId()).removeValue();
        return true;
    }

    private boolean updateAndSync(Room room) {
        Firebase roomIdRef = firebaseRoot.child("rooms").child(room.getId());
        room.setId(roomIdRef.getKey());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Room.NAME, room.getName());
        hashMap.put(Room.COLOR, room.getColor());
        roomIdRef.setValue(hashMap);
        roomIdRef.child(Room.GADGETS).removeValue();
        for(UUID uuid : room.getGadgets())
        {
            roomIdRef.child(Room.GADGETS).push().child(Room.ID).setValue(uuid.toString());
        }
        return true;
    }

    private boolean addAndSync(Gadget gadget) {
        Firebase gadgetsRoot = firebaseRoot.child("gadgets");
        Firebase gadgetRef = gadgetsRoot.child(gadget.getId().toString());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Gadget.ROOM_ID, gadget.getRoom());
        hashMap.put(Gadget.NAME, gadget.getName());
        hashMap.put(Gadget.MAC_ADDRESS, gadget.getMAC());
        hashMap.put(Gadget.TYPE, gadget.getType().toString());
        hashMap.put(Gadget.CHANNELS, gadget.getChannels());
        hashMap.put(Gadget.INSTALLED, gadget.isInstalled());
        gadgetRef.setValue(hashMap);
        Log.d("newGadgetSynced", gadgetRef.getKey());
        return true;
    }

    private boolean deleteAndSync(Gadget gadget) {
        firebaseRoot.child("gadgets").child(gadget.getId().toString()).removeValue();
        return true;
    }

    private boolean updateAndSync(Gadget gadget) {
        Firebase gadgetsRoot = firebaseRoot.child("gadgets");
        Firebase gadgetRef = gadgetsRoot.child(gadget.getId().toString());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Gadget.ROOM_ID, gadget.getRoom());
        hashMap.put(Gadget.NAME, gadget.getName());
        hashMap.put(Gadget.MAC_ADDRESS, gadget.getMAC());
        hashMap.put(Gadget.TYPE, gadget.getType().toString());
        hashMap.put(Gadget.CHANNELS, gadget.getChannels());
        hashMap.put(Gadget.INSTALLED, gadget.isInstalled());
        gadgetRef.setValue(hashMap);
        return true;
    }

    private boolean addAndSync(Scene scene) {
        Firebase sceneRoot = firebaseRoot.child("scenes");
        Firebase sceneRef = sceneRoot.push();
        scene.setId(sceneRef.getKey());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Scene.NAME, scene.getName());

        //actions and subscenes
        HashMap<String, Object> actions = new HashMap<String, Object>();
        HashMap<String, Object> subscenes = new HashMap<String, Object>();
        int actionsIndex = 0;
        int subscenesIndex = 0;
        for(SceneComponent component : scene.getChildren())
        {
            if(component instanceof Scene)
            {
                HashMap<String, Object> sceneMap = new HashMap<String, Object>();
                sceneMap.put(Scene.ID, ((Scene) component).getId());
                subscenes.put(String.valueOf(subscenesIndex), sceneMap);
                subscenesIndex++;
            }
            else
            {
                HashMap<String, Object> actionMap = new HashMap<String, Object>();
                actionMap.put(BaseAction.ACTION, ((BaseAction) component).getAction());
                actionMap.put(BaseAction.DELAY, ((BaseAction) component).getDelay());
                actionMap.put(BaseAction.PARAMETER, ((BaseAction) component).getParameter());
                actionMap.put(BaseAction.GADGET_ID, ((BaseAction) component).getGadgetID());
                actions.put(String.valueOf(actionsIndex), actionMap);
                actionsIndex++;
            }
        }
        hashMap.put(Scene.ACTIONS, actions);
        hashMap.put(Scene.SUBSCENES, subscenes);

        //triggers
        HashMap<String, Object> triggersMap = new HashMap<String, Object>();
        java.util.List<Trigger> triggers = scene.getTriggers();
        for(int i = 0; i < triggers.size(); i++)
        {
            Trigger trigger = triggers.get(i);
            triggersMap.put(String.valueOf(i), trigger.toHashMap());
        }
        hashMap.put(Scene.TRIGGERS, triggersMap);

        sceneRef.setValue(hashMap);
        return true;
    }

    private boolean deleteAndSync(Scene scene) {
        firebaseRoot.child("scenes").child(scene.getId()).removeValue();
        return true;
    }

    private boolean updateAndSync(Scene scene) {
        Firebase sceneRoot = firebaseRoot.child("scenes");
        Firebase sceneRef = sceneRoot.child(scene.getId());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Scene.NAME, scene.getName());

        //actions and subscenes
        HashMap<String, Object> actions = new HashMap<String, Object>();
        HashMap<String, Object> subscenes = new HashMap<String, Object>();
        int actionsIndex = 0;
        int subscenesIndex = 0;
        for(SceneComponent component : scene.getChildren())
        {
            if(component instanceof Scene)
            {
                HashMap<String, Object> sceneMap = new HashMap<String, Object>();
                sceneMap.put(Scene.ID, ((Scene) component).getId());
                subscenes.put(String.valueOf(subscenesIndex), sceneMap);
                subscenesIndex++;
            }
            else
            {
                HashMap<String, Object> actionMap = new HashMap<String, Object>();
                actionMap.put(BaseAction.ACTION, ((BaseAction) component).getAction());
                actionMap.put(BaseAction.DELAY, ((BaseAction) component).getDelay());
                actionMap.put(BaseAction.PARAMETER, ((BaseAction) component).getParameter());
                actionMap.put(BaseAction.GADGET_ID, ((BaseAction) component).getGadgetID());
                actions.put(String.valueOf(actionsIndex), actionMap);
                actionsIndex++;
            }
        }
        hashMap.put(Scene.ACTIONS, actions);
        hashMap.put(Scene.SUBSCENES, subscenes);

        //triggers
        HashMap<String, Object> triggersMap = new HashMap<String, Object>();

        //        int triggerIndex = 0;
        //        for(Trigger trigger : scene.getTriggers())
        //        {
        //            triggersMap.put(String.valueOf(triggerIndex), trigger.toHashMap());
        //            triggerIndex++;
        //        }

        java.util.List<Trigger> triggers = scene.getTriggers();
        for(int i = 0; i < triggers.size(); i++)
        {
            Trigger trigger = triggers.get(i);
            triggersMap.put(String.valueOf(i), trigger.toHashMap());
        }
        hashMap.put(Scene.TRIGGERS, triggersMap);

        sceneRef.setValue(hashMap);
        return true;
    }

    private boolean addAndSync(ScheduledScene scheduledscene) {
        Firebase scheduleRoot = firebaseRoot.child("schedule");
        Firebase scheduleRef = scheduleRoot.push();
        scheduledscene.setId(scheduleRef.getKey());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(ScheduledScene.SCENE, scheduledscene.getScene());
        hashMap.put(ScheduledScene.HOUR, scheduledscene.getHour());
        hashMap.put(ScheduledScene.MINUTES, scheduledscene.getMinutes());
        hashMap.put(ScheduledScene.DAYS_OF_WEEK, scheduledscene.getDaysOfWeek());
        hashMap.put(ScheduledScene.CONDITIONS, scheduledscene.getConditions());
        scheduleRef.setValue(hashMap);
        return true;
    }

    private boolean deleteAndSync(ScheduledScene scheduledscene) {
        firebaseRoot.child("schedule").child(scheduledscene.getId()).removeValue();
        return true;
    }

    private boolean updateAndSync(ScheduledScene scheduledscene) {
        Firebase scheduleRoot = firebaseRoot.child("schedule");
        Firebase scheduleRef = scheduleRoot.child(scheduledscene.getId());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(ScheduledScene.SCENE, scheduledscene.getScene());
        hashMap.put(ScheduledScene.HOUR, scheduledscene.getHour());
        hashMap.put(ScheduledScene.MINUTES, scheduledscene.getMinutes());
        hashMap.put(ScheduledScene.DAYS_OF_WEEK, scheduledscene.getDaysOfWeek());
        hashMap.put(ScheduledScene.CONDITIONS, scheduledscene.getConditions());
        scheduleRef.setValue(hashMap);
        return true;
    }

    private boolean addAndSync(User user) {
        return homeManager.add(user);
    }

    private boolean deleteAndSync(User user) {
        return homeManager.delete(user);
    }

    private boolean updateAndSync(User user) {
        return homeManager.update(user);
    }

}
