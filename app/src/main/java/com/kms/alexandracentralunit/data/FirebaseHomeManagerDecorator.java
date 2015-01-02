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
 * @author Mateusz Zasoński
 */
public class FirebaseHomeManagerDecorator extends HomeManagerDecorator {

    private Firebase firebaseRoot;

    public FirebaseHomeManagerDecorator(HomeManager homeManager) {
        super(homeManager);
        String FIREBASE_URL = "https://sizzling-torch-8921.firebaseio.com/currentState/"+CoreService.getHomeId()+"/";
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
        return super.add(newGadget);
    }

    @Override
    public boolean delete(Gadget gadget) {
        return super.delete(gadget);
    }

    @Override
    public boolean update(Gadget newGadget) {
        return super.update(newGadget);
    }

    @Override
    public boolean add(Scene scene) {
        return super.add(scene);
    }

    @Override
    public boolean delete(Scene scene) {
        return super.delete(scene);
    }

    @Override
    public boolean update(Scene newScene) {
        return super.update(newScene);
    }

    @Override
    public boolean add(ScheduledScene scheduledscene) {
        return super.add(scheduledscene);
    }

    @Override
    public boolean delete(ScheduledScene scheduledscene) {
        return super.delete(scheduledscene);
    }

    @Override
    public boolean update(ScheduledScene scheduledscene) {
        return super.update(scheduledscene);
    }

    @Override
    public boolean add(User user) {
        return super.add(user);
    }

    @Override
    public boolean delete(User user) {
        return super.delete(user);
    }

    @Override
    public boolean update(User user) {
        return super.update(user);
    }

    private boolean addAndSync(Room room) {
        Firebase roomRoot = firebaseRoot.child("rooms");
        Firebase roomIdRef = roomRoot.push();
        room.setName(roomIdRef.getKey());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Room.NAME, room.getName());
        hashMap.put(Room.COLOR, room.getColor());
        roomIdRef.setValue(hashMap);
        for(UUID uuid : room.getGadgets())
        {
            roomIdRef.child(Room.GADGETS).push().child(Room.ID).setValue(uuid.toString());
        }
        Log.d("newRoomSynced", roomIdRef.getKey());
        return true;
    }

    private boolean deleteAndSync(Room room) {
        firebaseRoot.child("rooms").child(room.getId()).removeValue();
        return homeManager.delete(room);
    }

    private boolean updateAndSync(Room room) {
        Firebase roomIdRef = firebaseRoot.child("rooms").child(room.getId());
        room.setName(roomIdRef.getKey());
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(Room.NAME, room.getName());
        hashMap.put(Room.COLOR, room.getColor());
        roomIdRef.setValue(hashMap);
        roomIdRef.child(Room.GADGETS).removeValue();
        for(UUID uuid : room.getGadgets())
        {
            roomIdRef.child(Room.GADGETS).push().child(Room.ID).setValue(uuid.toString());
        }
        Log.d("newRoomSynced", roomIdRef.getKey());
        return true;
    }

    private boolean addAndSync(Gadget newGadget) {
        return homeManager.add(newGadget);
    }

    private boolean deleteAndSyncAndSync(Gadget gadget) {
        return homeManager.delete(gadget);
    }

    private boolean updateAndSync(Gadget newGadget) {
        return homeManager.update(newGadget);
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
