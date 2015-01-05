package com.kms.alexandracentralunit.data;


import android.util.Log;

import com.firebase.client.Firebase;
import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.data.model.Gadget;
import com.kms.alexandracentralunit.data.model.Room;
import com.kms.alexandracentralunit.data.model.Scene;
import com.kms.alexandracentralunit.data.model.ScheduledScene;
import com.kms.alexandracentralunit.data.model.User;

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
        String FIREBASE_URL = "https://sizzling-torch-8921.firebaseio.com/configuration/"+CoreService.getHomeId()+"/";
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
        return homeManager.add(scene);
    }

    private boolean deleteAndSync(Scene scene) {
        return homeManager.delete(scene);
    }

    private boolean updateAndSync(Scene newScene) {
        return homeManager.update(newScene);
    }

    private boolean addAndSync(ScheduledScene scheduledscene) {
        return homeManager.add(scheduledscene);
    }

    private boolean deleteAndSync(ScheduledScene scheduledscene) {
        return homeManager.delete(scheduledscene);
    }

    private boolean updateAndSync(ScheduledScene scheduledscene) {
        return homeManager.update(scheduledscene);
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
