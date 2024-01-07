package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;


import java.util.*;

public class SalesRecord implements Writable {

    private List<Total> saleList;

    public SalesRecord() {
        saleList = new ArrayList<>();
    }

    public void addTotal(Total total) {
        saleList.add(total);
    }

    /*
     * EFFECTS: return objects(employee) as json object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("salesRecords", salesToJson());
        return json;
    }

    // EFFECTS: returns employee in this list as a JSON array
    private JSONArray salesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Total t : saleList) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

}
