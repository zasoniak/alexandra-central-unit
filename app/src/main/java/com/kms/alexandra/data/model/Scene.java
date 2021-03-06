package com.kms.alexandra.data.model;


import android.content.Intent;
import android.util.Log;

import com.kms.alexandra.centralunit.Alexandra;
import com.kms.alexandra.centralunit.HistorianBroadcastReceiver;
import com.kms.alexandra.data.model.actions.ActionMessage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class Scene implements SceneComponent, Listable {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TRIGGERS = "triggers";
    public static final String ACTIONS = "actions";
    public static final String SUBSCENES = "subscenes";

    public String id;
    public String name;

    public List<SceneComponent> children;
    public List<Trigger> triggers;

    public List<ActionMessage> actionsList;
    public List<String> subscenesList;

    public Scene(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Scene(String id, String name, List<Trigger> triggers, List<SceneComponent> children) {
        this.id = id;
        this.name = name;
        this.triggers = triggers;
        this.children = children;
    }

    public Scene(String name, List<SceneComponent> children, List<Trigger> triggers) {
        this.name = name;
        this.children = children;
        this.triggers = triggers;
    }

    /**
     * starts scene execution
     * sends info to historian
     *
     * @param controller reference to bluetooth messages queue control
     */
    @Override
    public void start(Controller controller) {

        Log.d("scene", id+" ruszyła, dzieci: "+String.valueOf(children.size()));
        Intent intent = new Intent(Alexandra.getContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LogType.Scene);
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent.putExtra(HistorianBroadcastReceiver.SCENE, this.getId());
        Alexandra.getContext().sendBroadcast(intent);

        for(SceneComponent child : children)
        {
            child.start(controller);
        }

    }

    @Override
    public List<SceneComponent> getComponents() {
        if(children != null)
        {
            return children;
        }
        return new ArrayList<SceneComponent>();
    }

    @Override
    public List<UUID> getGadgetsID() {
        ArrayList<UUID> gadgetArrayList = new ArrayList<UUID>();
        for(SceneComponent child : children)
        {
            gadgetArrayList.addAll(child.getGadgetsID());
        }
        return gadgetArrayList;
    }

    public void registerTriggers(Home home) {
        for(Trigger trigger : this.triggers)
        {
            Log.d("zarejestrowano trigger", trigger.getScene());
            trigger.registerObservers(home);
        }
    }

    public void unregisterTriggers(Home home) {
        for(Trigger trigger : this.triggers)
        {
            Log.d("odrejestrowano trigger", trigger.getScene());
            trigger.unregisterObservers(home);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SceneComponent> getChildren() {
        return children;
    }

    public void setChildren(List<SceneComponent> children) {
        this.children = children;
    }

    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }

    public String toString() {
        return this.name;
    }

    public List<ActionMessage> getActionsList() {
        return actionsList;
    }

    public List<String> getSubscenesList() {
        return subscenesList;
    }

    @Override
    public String getObjectType() {
        return "Scene";
    }
}
