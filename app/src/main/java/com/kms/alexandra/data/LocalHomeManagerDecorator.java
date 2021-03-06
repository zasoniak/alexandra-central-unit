package com.kms.alexandra.data;


import com.kms.alexandra.centralunit.FirebaseCurrentStateObserver;
import com.kms.alexandra.centralunit.ScheduleManager;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.User;
import com.kms.alexandra.data.model.gadgets.Gadget;


/**
 * @author Mateusz Zasoński
 */
public class LocalHomeManagerDecorator extends HomeManagerDecorator {

    public LocalHomeManagerDecorator(HomeManager homeManager) {
        super(homeManager);
    }

    @Override
    public boolean add(Room room) {
        super.add(room);
        return addAndSave(room);
    }

    @Override
    public boolean delete(Room room) {
        super.delete(room);
        return deleteAndSave(room);
    }

    @Override
    public boolean update(Room room) {
        super.update(room);
        return updateAndSave(room);
    }

    @Override
    public boolean add(Gadget newGadget) {
        super.add(newGadget);
        return addAndSave(newGadget);
    }

    @Override
    public boolean delete(Gadget gadget) {
        super.delete(gadget);
        return deleteAndSave(gadget);
    }

    @Override
    public boolean update(Gadget newGadget) {
        super.update(newGadget);
        return updateAndSave(newGadget);
    }

    @Override
    public boolean add(Scene scene) {
        super.add(scene);
        return addAndSave(scene);
    }

    @Override
    public boolean delete(Scene scene) {
        super.delete(scene);
        return deleteAndSave(scene);
    }

    @Override
    public boolean update(Scene newScene) {
        super.update(newScene);
        return updateAndSave(newScene);
    }

    @Override
    public boolean add(ScheduledScene scheduledscene) {
        super.add(scheduledscene);
        return addAndSave(scheduledscene);
    }

    @Override
    public boolean delete(ScheduledScene scheduledscene) {
        super.delete(scheduledscene);
        return deleteAndSave(scheduledscene);
    }

    @Override
    public boolean update(ScheduledScene scheduledscene) {
        super.update(scheduledscene);
        return updateAndSave(scheduledscene);
    }

    @Override
    public boolean add(User user) {
        super.add(user);
        return addAndSave(user);
    }

    @Override
    public boolean delete(User user) {
        super.delete(user);
        return deleteAndSave(user);
    }

    @Override
    public boolean update(User user) {
        super.update(user);
        return updateAndSave(user);
    }

    private boolean addAndSave(Room room) {
        if(home.getRoom(room.getId()) == null)
        {
            home.getRooms().add(room);
            return homeRepository.add(room);
        }
        else
        {
            return update(room);
        }
    }

    private boolean deleteAndSave(Room room) {
        Room temp;
        if((temp = home.getRoom(room.getId())) != null)
        {
            home.getRooms().remove(temp);
            return homeRepository.delete(temp);
        }
        return false;
    }

    private boolean updateAndSave(Room updatedRoom) {
        Room room = home.getRoom(updatedRoom.getId());
        if (room != null) {
            room.setColor(updatedRoom.getColor());
            room.setName(updatedRoom.getName());
            room.setGadgets(updatedRoom.getGadgets());
            return homeRepository.update(room);
        }
        return false;
    }

    private boolean addAndSave(Gadget newGadget) {
        if(home.getGadget(newGadget.getId()) == null)
        {
            home.getGadgets().add(newGadget);
            FirebaseCurrentStateObserver.getInstance().addGadget(newGadget);
            if(home.getRoom(newGadget.getTemporaryRoomId()) != null)
            {
                home.getRoom(newGadget.getTemporaryRoomId()).addGadget(newGadget.getId());
                newGadget.setRoom(home.getRoom(newGadget.getTemporaryRoomId()));
            }
            return homeRepository.add(newGadget);
        }
        else
        {
            return update(newGadget);
        }
    }

    private boolean deleteAndSave(Gadget gadget) {
        home.getGadgets().remove(gadget);
        return homeRepository.delete(gadget);
    }

    private boolean updateAndSave(Gadget newGadget) {
        for(Gadget gadget : home.getGadgets())
        {
            if(gadget.getId().equals(newGadget.getId()))
            {
                gadget.setName(newGadget.getName());
                if(!newGadget.getRoom().getId().equals(gadget.getRoom().getId()))
                {
                    home.getRoom(gadget.getRoom().getId()).getGadgets().remove(gadget.getId());
                    home.getRoom(newGadget.getRoom().getId()).getGadgets().add(newGadget.getId());
                    gadget.setRoom(home.getRoom(newGadget.getRoom().getId()));
                }
                return homeRepository.update(gadget);
            }
        }
        return false;
    }

    private boolean addAndSave(Scene scene) {
        if(home.getScene(scene.getId()) == null)
        {
            home.getScenes().add(scene);
            scene.registerTriggers(home);
            return homeRepository.add(scene);
        }
        else
        {
            return update(scene);
        }
    }

    private boolean deleteAndSave(Scene scene) {
        home.getScenes().remove(scene);
        return homeRepository.delete(scene);
    }

    private boolean updateAndSave(Scene newScene) {
        Scene scene = home.getScene(newScene.getId());
        if(scene != null)
        {
            scene.unregisterTriggers(home);
            home.getScenes().set(home.getScenes().indexOf(scene), newScene);
            newScene.registerTriggers(home);
            return homeRepository.update(newScene);
        }
        return false;
    }

    private boolean addAndSave(ScheduledScene scheduledscene) {
        if(home.getScheduledScene(scheduledscene.getId()) == null)
        {
            home.getSchedule().add(scheduledscene);
            ScheduleManager.getInstance(context).add(scheduledscene);
            return homeRepository.add(scheduledscene);
        }
        else
        {
            return update(scheduledscene);
        }
    }

    private boolean deleteAndSave(ScheduledScene scheduledscene) {
        ScheduleManager.getInstance(context).delete(scheduledscene);
        home.getSchedule().remove(scheduledscene);
        return homeRepository.delete(scheduledscene);
    }

    private boolean updateAndSave(ScheduledScene scheduledscene) {
        int index = home.getSchedule().indexOf(home.getScheduledScene(scheduledscene.getId()));
        if (index != -1) {
            home.getSchedule().set(index, scheduledscene);
            return homeRepository.update(scheduledscene);
        }
        return false;
    }

    private boolean addAndSave(User user) {
        if(home.getUser(user.getId()) == null)
        {
            home.getUsers().add(user);
            return homeRepository.add(user);
        }
        else
        {
            return update(user);
        }
    }

    private boolean deleteAndSave(User user) {
        home.getUsers().remove(user);
        return homeRepository.delete(user);
    }

    private boolean updateAndSave(User user) {
        for(User user1 : home.getUsers())
        {
            if(user1.getId().equals(user.getId()))
            {
                user1 = user;
                return homeRepository.update(user1);
            }
        }
        return false;
    }
}
