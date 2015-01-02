package com.kms.alexandracentralunit.data.model;


import android.content.Intent;
import android.util.Log;

import com.kms.alexandracentralunit.BLEController;
import com.kms.alexandracentralunit.CoreService;
import com.kms.alexandracentralunit.HistorianBroadcastReceiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class Scene implements SceneComponent {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TRIGGERS = "triggers";
    public static final String ACTIONS = "actions";
    public static final String SUBSCENES = "subscenes";

    public String id;
    public String name;

    public List<SceneComponent> children;
    public List<Trigger> triggers;

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
    public void start(BLEController controller) {

        Log.d("scene", id+" ruszyła, dzieci: "+String.valueOf(children.size()));
        Intent intent = new Intent(CoreService.getContext(), HistorianBroadcastReceiver.class);
        intent.putExtra(HistorianBroadcastReceiver.LOG_TYPE, HistorianBroadcastReceiver.LogType.Scene);
        intent.putExtra(HistorianBroadcastReceiver.TIME, Calendar.getInstance().getTime().toString());
        intent.putExtra(HistorianBroadcastReceiver.SCENE, this.getId());
        CoreService.getContext().sendBroadcast(intent);

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

}
