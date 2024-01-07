package model;


import org.json.JSONObject;
import persistence.Writable;

public class Sale implements Writable{
    private int num;
    private int productID;

    //REQUIRES: num >= 0, product != null
    //EFFECTS: assign num, product and current time and date to this.date
    public Sale(int num,Product product) {
        this.num = num;
        this.productID = product.getID();
    }

    public Sale(int num,int id) {
        this.num = num;
        this.productID = id;
    }

    public int salePrice() {
        ProductList instance = ProductList.getInstance();
        return num * instance.getProductPrice(productID);
    }

    /*
     * EFFECTS: return objects as json object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("num", num);
        json.put("productID", productID);
        return json;
    }


}
