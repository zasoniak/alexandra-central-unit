package com.kms.alexandracentralunit;


import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author Mateusz Zasoński
 */
public interface HistorianRepository {

    public boolean log(HistorianBroadcastReceiver.LogType type, Map objectMap);
    public List<JSONObject> getAll();
    public List<JSONObject> getAllByDate(Date from, Date to);
    public List<JSONObject> getAllByDateAndType(Date from, Date to, HistorianBroadcastReceiver.LogType type);

}
