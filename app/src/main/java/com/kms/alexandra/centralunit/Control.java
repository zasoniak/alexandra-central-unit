package com.kms.alexandra.centralunit;


import com.kms.alexandra.data.model.Controller;
import com.kms.alexandra.data.model.Home;
import com.kms.alexandra.data.model.actions.ActionMessage;
import com.kms.alexandra.data.model.actions.BaseAction;


/**
 * Class providing scenes and actions processing
 * <p/>
 * Entrance place for running any hardware-ended process in automation system
 * Implemented as singleton to provide controlled access to bluetooth transmission
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class Control {

    private static Control instance;

    private Home home;
    private Controller controller;

    /**
     * private singleton constructor
     */
    private Control() {
        this.home = ((Alexandra) Alexandra.getContext()).getHome();
        this.controller = new BLEController();
    }

    /**
     * singleton pattern getInstance
     * synchronized for thread-safety
     *
     * @return ControlService instance
     */
    public static synchronized Control getInstance() {
        if(instance == null)
        {
            instance = new Control();
        }
        return instance;
    }

    /**
     * start scene execution
     *
     * @param sceneID id of scene to be executed
     */
    public void run(String sceneID) {
        if(home.getScene(sceneID) != null)
        {
            home.getScene(sceneID).start(controller);
        }
    }

    /**
     * start single action execution
     *
     * @param actionMessage structure providing essential information about action to be executed
     */
    public void run(ActionMessage actionMessage) {
        if(home.getGadget(actionMessage.gadgetID) != null)
        {
            BaseAction action = home.getGadget(actionMessage.gadgetID).prepare(actionMessage);
            action.setDelay(actionMessage.delay);
            action.start(controller);
        }
    }
}
