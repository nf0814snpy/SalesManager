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
    private String imagePath;

    public Product(String name, int quantity, int price, Category category) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id = productID++;
        this.category = category;
        this.imagePath = imagePath(category);
    }

    public Product(String name, int quantity, int price, Category category, String imagepath) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id = productID++;
        this.category = category;
        this.imagePath = imagepath;
    }


    public void decreaseMutualID() {
        productID--;
    }

    public void decreaseThisID() {
        this.id--;
    }

    public Product(String name, int quantity, int price, int id) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
        productID++;
        this.category = ProductList.getInstance().getCategory(id);
    }

    public String imagePath(Category category) {
        if(Category.valueOf("お守り").equals(category)) {
            return "./data/ProductImages/amulet.png";
        } else if (Category.valueOf("絵馬").equals(category)) {
            return "./data/ProductImages/ema.png";
        } else if (Category.valueOf("御朱印").equals(category)) {
            return "./data/ProductImages/gosyuinchou.png";
        } else {
            return "./data/ProductImages/komainu.png";
        }
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

    public String getImagePath() {
        return imagePath;
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
        json.put("imagePath",imagePath);
        json.put("category",category.name());
        return json;
    }

}
