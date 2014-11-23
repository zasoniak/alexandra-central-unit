package com.kms.alexandracentralunit;


import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.BaseAction;
import com.kms.alexandracentralunit.data.model.Home;


/**
 * Created by Mateusz Zasoński on 2014-11-23.
 * ControlService - class providing scenes and actions processing
 */
public class ControlService {

    private static ControlService instance;

    private Home home;
    private BLEController controller;

    private ControlService() {
        this.home = CoreService.getHome();
        this.controller = new BLEController();
    }

    public static synchronized ControlService getInstance() {
        if(instance == null)
        {
            instance = new ControlService();
        }
        return instance;
    }

    public void run(String sceneID) {
        home.getScene(sceneID).start(controller);
    }

    public void run(ActionMessage actionMessage) {
        BaseAction action = home.getGadget(actionMessage.gadgetID).prepare(actionMessage);
        action.start(controller);
    }
}
