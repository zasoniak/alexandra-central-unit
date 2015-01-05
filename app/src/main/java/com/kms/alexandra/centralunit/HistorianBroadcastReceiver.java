package com.kms.alexandra.centralunit;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mateusz Zasoński on 2014-10-31.
 * HistorianBroadcastReceiver  - class responsible for logging all events
 */
public class HistorianBroadcastReceiver extends BroadcastReceiver {

    public static final String LOG_TYPE = "logType";
    public static final String LOG_ACTION = "action";
    public static final String LOG_MEASUREMENT = "measurement";
    public static final String LOG_ERROR = "error";
    public static final String LOG_SCENE = "scene";
    public static final String DESCRIPTION = "description";
    public static final String GADGET = "gadget";
    public static final String TIME = "time";
    //measurement
    public static final String TYPE = "type";
    public static final String VALUE = "value";
    public static final String UNIT = "unit";
    //action
    public static final String ACTION = "action";
    public static final String PARAMETERS = "parameters";
    //scene
    public static final String SCENE = "scene";
    public static final String MESSAGE = "message";
    private static final String ACTIONS = "actions";
    private static final String MEASUREMENTS = "measurements";
    private static final String ERRORS = "errors";
    private static final String SCENES = "scenes";
    //system
    private static final String SYSTEM = "system";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        new Thread(new Runnable() {
            public void run() {
                Firebase historianReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/historian/"+String.valueOf(CoreService.getHomeId())+"/");
                Map<String, String> historianLog = new HashMap<String, String>();

                switch((LogType) intent.getSerializableExtra(LOG_TYPE))
                {
                    case System:
                        historianLog.put(TYPE, intent.getStringExtra(TYPE));
                        historianLog.put(MESSAGE, intent.getStringExtra(MESSAGE));
                        historianLog.put(TIME, intent.getStringExtra(TIME));
                        historianReference.child(SYSTEM).push().setValue(historianLog);
                        JSONHistorianRepository.getInstance(context).log(LogType.System, historianLog);
                        break;
                    case Action:
                        JSONHistorianRepository.getInstance(context).log(LogType.Action, historianLog);
                        break;
                    case Scene:
                        historianLog.put(SCENE, intent.getStringExtra(SCENE));
                        historianLog.put(TIME, intent.getStringExtra(TIME));
                        historianReference.child(SCENES).push().setValue(historianLog);
                        JSONHistorianRepository.getInstance(context).log(LogType.Scene, historianLog);
                        break;
                    case Measurement:
                        JSONHistorianRepository.getInstance(context).log(LogType.Measurement, historianLog);
                        break;
                    case ConfigurationChange:
                        JSONHistorianRepository.getInstance(context).log(LogType.ConfigurationChange, historianLog);
                        break;
                    case Error:
                        JSONHistorianRepository.getInstance(context).log(LogType.Error, historianLog);
                        break;
                }
            }
        }).start();
    }

    public static enum LogType {
        System,
        Action,
        Scene,
        Measurement,
        ConfigurationChange,
        Error
    }
}
