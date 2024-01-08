package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.time.LocalDateTime;
import java.util.*;

public class Total implements Writable {

    private List<Sale> total;
    private LocalDateTime date;
    private int totalPrice;

    public Total() {
        total = new ArrayList<>();
        this.date = LocalDateTime.now();
        this.totalPrice = 0;
    }

    public Total(LocalDateTime time) {
        total = new ArrayList<>();
        this.date = time;
        this.totalPrice = 0;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
    /*
     * EFFECTS: return objects(employee) as json object
     */

    public void addSales(Sale sale) {
        total.add(sale);
        totalPrice += sale.salePrice();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("total", totalToJson());
        json.put("totalPrice", totalPrice);
        json.put("year", date.getYear());
        json.put("month", date.getMonthValue());
        json.put("day",date.getDayOfMonth());
        json.put("hour", date.getHour());
        json.put("minute",date.getMinute());
        return json;
    }

    // EFFECTS: returns employee in this list as a JSON array
    private JSONArray totalToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Sale s : total) {
            jsonArray.put(s.toJson());
        }

        return jsonArray;
    }
}
