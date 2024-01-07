package model;

import org.json.JSONObject;
import persistence.Writable;

public class Product implements Writable {

    private String name;
    private int quantity;
    private int price;
    private int id;
    private static int productID = 0;
    private Category category;

    public Product(String name, int quantity, int price, Category category) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id = productID++;
        this.category = category;
    }

    public Product(String name, int quantity, int price, int id) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
        productID++;
    }

    public Category getCategory() {
        return category;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void decreaseNum(int num) {
        quantity = quantity - num;
    }

    /*
     * EFFECTS: return objects as json object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("quantity", quantity);
        json.put("price", price);
        json.put("id", id);
        json.put("category",category.name());
        return json;
    }

}
