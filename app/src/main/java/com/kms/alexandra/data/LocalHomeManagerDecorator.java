package com.kms.alexandra.data;


import com.kms.alexandra.centralunit.FirebaseCurrentStateObserver;
import com.kms.alexandra.data.model.Gadget;
import com.kms.alexandra.data.model.Room;
import com.kms.alexandra.data.model.Scene;
import com.kms.alexandra.data.model.ScheduledScene;
import com.kms.alexandra.data.model.User;


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
            update(room);
        }
        return false;
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

    private boolean updateAndSave(Room room) {
        for(Room room1 : home.getRooms())
        {
            if(room1.getId().equals(room.getId()))
            {
                room1.setColor(room.getColor());
                room1.setName(room.getName());
                room1.setGadgets(room.getGadgets());
                return homeRepository.update(room1);
            }
        }
        return false;
    }

    private boolean addAndSave(Gadget newGadget) {
        if(home.getGadget(newGadget.getId()) == null)
        {
            home.getGadgets().add(newGadget);
            FirebaseCurrentStateObserver.getInstance().addGadget(newGadget);
            if(home.getRoom(newGadget.getRoom()) != null)
            {
                home.getRoom(newGadget.getRoom()).addGadget(newGadget.getId());
            }
            return homeRepository.add(newGadget);
        }
        else
        {
            update(newGadget);
        }
        return false;
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
                if(!newGadget.getRoom().equals(gadget.getRoom()))
                {
                    home.getRoom(gadget.getRoom()).getGadgets().remove(gadget.getId());
                    home.getRoom(newGadget.getRoom()).getGadgets().add(newGadget.getId());
                    gadget.setRoomId(newGadget.getRoom());
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
            update(scene);
        }
        return false;
    }

    private boolean deleteAndSave(Scene scene) {
        home.getScenes().remove(scene);
        return homeRepository.delete(scene);
    }

    private boolean updateAndSave(Scene newScene) {
        for(Scene scene : home.getScenes())
        {
            if(scene.getId().equals(newScene.getId()))
            {
                scene.setName(newScene.getName());
                scene.unregisterTriggers(home);
                scene.setTriggers(newScene.getTriggers());
                scene.registerTriggers(home);
                scene.setChildren(newScene.getChildren());
                return homeRepository.update(scene);
            }
        }
        return false;
    }

    private boolean addAndSave(ScheduledScene scheduledscene) {
        if(home.getScheduledScene(scheduledscene.getId()) == null)
        {
            home.getSchedule().add(scheduledscene);
            return homeRepository.add(scheduledscene);
        }
        else
        {
            update(scheduledscene);
        }
        return false;
    }

    private boolean deleteAndSave(ScheduledScene scheduledscene) {
        home.getSchedule().remove(scheduledscene);
        return homeRepository.delete(scheduledscene);
    }

    private boolean updateAndSave(ScheduledScene scheduledscene) {
        for(ScheduledScene scheduledscene1 : home.getSchedule())
        {
            if(scheduledscene1.getId().equals(scheduledscene.getId()))
            {
                scheduledscene1 = scheduledscene;
                return homeRepository.update(scheduledscene1);
            }
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
            update(user);
        }
        return false;
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
