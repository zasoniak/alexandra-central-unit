package com.kms.alexandracentralunit;


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
    private static final String ACTIONS = "actions";
    private static final String MEASUREMENTS = "measurements";
    private static final String ERRORS = "errors";
    private static final String SCENES = "scenes";

    @Override
    public void onReceive(Context context, final Intent intent) {
        new Thread(new Runnable() {
            public void run() {
                Firebase historianReference = new Firebase("https://sizzling-torch-8921.firebaseio.com/historian/"+String.valueOf(CoreService.getHomeId())+"/");
                Map<String, String> historianLog = new HashMap<String, String>();

                switch((LogType) intent.getSerializableExtra(LOG_TYPE))
                {
                    case Action:
                        break;
                    case Scene:
                        historianLog.put(SCENE, intent.getStringExtra(SCENE));
                        historianLog.put(TIME, intent.getStringExtra(TIME));
                        historianReference.child(SCENES).push().setValue(historianLog);
                    case Measurement:
                        break;
                    case ConfiguratioChange:
                        break;
                    case Error:
                        break;
                }

                //                if(intent.getStringExtra(LOG_TYPE).equals(LOG_ACTION))
                //                {
                //                    historianLog.put(GADGET, intent.getStringExtra(GADGET));
                //                    historianLog.put(TIME, intent.getStringExtra(TIME));
                //                    historianLog.put(ACTION, intent.getStringExtra(ACTION));
                //                    historianLog.put(PARAMETERS, intent.getStringExtra(PARAMETERS));
                //                    historianReference.child(ACTIONS).push().setValue(historianLog);
                //                }
                //                else
                //                {
                //                    if(intent.getStringExtra(LOG_TYPE).equals(LOG_MEASUREMENT))
                //                    {
                //                        historianLog.put(GADGET, intent.getStringExtra(GADGET));
                //                        historianLog.put(TIME, intent.getStringExtra(TIME));
                //                        historianLog.put(TYPE, intent.getStringExtra(TYPE));
                //                        historianLog.put(VALUE, intent.getStringExtra(VALUE));
                //                        historianLog.put(UNIT, intent.getStringExtra(UNIT));
                //                        historianReference.child(MEASUREMENTS).push().setValue(historianLog);
                //                    }
                //                    else
                //                    {
                //                        if(intent.getStringExtra(LOG_TYPE).equals(LOG_ERROR))
                //                        {
                //                            historianLog.put(DESCRIPTION, intent.getStringExtra(DESCRIPTION));
                //                            historianLog.put(TIME, intent.getStringExtra(TIME));
                //                            historianReference.child(ERRORS).push().setValue(historianLog);
                //                        }
                //                        else
                //                        {
                //                            Log.e("historian", "code error");
                //                        }
                //                    }
                //                }

            }
        }).start();
    }

    public static enum LogType {
        Action,
        Scene,
        Measurement,
        ConfiguratioChange,
        Error
    }
}
