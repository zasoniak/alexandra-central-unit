package com.kms.alexandracentralunit;


import org.json.JSONObject;

import java.util.List;
import java.util.Map;


/**
 * @author Mateusz Zasoński
 */
public interface HistorianRepository {

    public boolean log(HistorianBroadcastReceiver.LogType type, Map objectMap);
    public List<JSONObject> getAll();
    public List<JSONObject> getAllByDate(String from, String to);
    public List<JSONObject> getAllByDateAndType(String from, String to, HistorianBroadcastReceiver.LogType type);

}
