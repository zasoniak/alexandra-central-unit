package com.kms.alexandracentralunit;


import com.kms.alexandracentralunit.data.model.ActionMessage;
import com.kms.alexandracentralunit.data.model.BaseAction;
import com.kms.alexandracentralunit.data.model.Home;


/**
 * Class providing scenes and actions processing
 * <p/>
 * Entrance place for running any hardware-ended process in automation system
 * Implemented as singleton to provide controlled access to bluetooth transmission
 *
 * @author Mateusz Zasoński
 * @version 0.1
 */
public class ControlService {

    private static ControlService instance;

    private Home home;
    private BLEController controller;

    /**
     * private singleton constructor
     */
    private ControlService() {
        this.home = CoreService.getHome();
        this.controller = new BLEController();
    }

    /**
     * singleton pattern getInstance
     * synchronized for thread-safety
     *
     * @return ControlService instance
     */
    public static synchronized ControlService getInstance() {
        if(instance == null)
        {
            instance = new ControlService();
        }
        return instance;
    }

    /**
     * start scene execution
     *
     * @param sceneID id of scene to be executed
     */
    public void run(String sceneID) {
        home.getScene(sceneID).start(controller);
    }

    /**
     * start single action execution
     *
     * @param actionMessage structure providing essential information about action to be executed
     */
    public void run(ActionMessage actionMessage) {
        BaseAction action = home.getGadget(actionMessage.gadgetID).prepare(actionMessage);
        action.start(controller);
    }
}
