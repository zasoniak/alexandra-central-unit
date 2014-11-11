package com.kms.alexandracentralunit.data.database.json;


import com.kms.alexandracentralunit.data.database.RoomRepository;
import com.kms.alexandracentralunit.data.model.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by Konrad Kowalewski <k.j.kowalewski@gmail.com> on 08.11.14.
 */
public class JSONRoomRepository implements RoomRepository {

    @Override
    public boolean add(Room room) {
        JSONObject object = new JSONObject();
        return false;
    }

    @Override
    public boolean delete(Room room) {
        return false;
    }

    @Override
    public boolean update(Room room) {
        return false;
    }

    @Override
    public Room find(UUID id) {
        return null;
    }

    @Override
    public List<Room> getAll() {
        return null;
    }

    private Room parseJSON(JSONObject object) {
        long id;
        long systemID;
        String name;
        int color;
        List<UUID> gadgets = new ArrayList<UUID>();
        try
        {
            id = object.getLong("id");
            systemID = object.getLong("system_id");
            name = object.getString("name");
            color = object.getInt("color");
            JSONArray uuids = object.getJSONArray("gadgets");
            for(int i = 0; i < uuids.length(); i++)
            {
                gadgets.add(UUID.fromString(uuids.getString(i)));
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
        return new Room(id, systemID, name, color, null);
    }

    private JSONObject toJSON(Room room) {
        JSONObject result = new JSONObject();
        try
        {
            result.put("id", room.getId());
            result.put("system_id", room.getSystemId());
            result.put("name", room.getName());
            result.put("color", room.getColor());
            JSONArray array = new JSONArray(room.getGadgets());
            result.put("gadgets", array);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return result;
    }
}
