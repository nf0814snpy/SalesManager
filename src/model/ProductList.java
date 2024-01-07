package model;

import persistence.Writable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ProductList implements Writable{

    private Set<Product> productList;

    private static ProductList instance;

    private ProductList() {
        productList = new HashSet<>();
    }

    public static ProductList getInstance() {
        if(instance == null) {
            instance = new ProductList();
        }
        return instance;
    }

    public int returnSizeCategory(Category category) {
        int size = 0;
        for (Product p: productList) {
            if(p.getCategory().equals(category)) {
                size++;
            }
        }
        return size;
    }

    public Category getCategory(int id) {
        Category result = null;
        for (Product p: productList) {
            if (p.getID() == id) {
                result = p.getCategory();
            }
        }
        return result;
    }

    public void removeProduct(Product product) {
        for(Product p: productList) {
            if (product.getID() < p.getID()) {
                p.decreaseThisID();
            }
        }
        productList.remove(product);
        product.decreaseMutualID();

    }

    public Product getProductFromName(String name) {
        for(Product p: productList) {
            if(name.equals(p.getName())) {
                return p;
            }
        }
        return null;
    }

    public void addProduct(Product product) {
        productList.add(product);
    }


    public String getProductName(int productID) {
        String result = "";
        for (Product p: productList) {
            if (p.getID() == productID) {
                result = p.getName();
            }
        }
        return result;
    }

    public int getProductPrice(int productID) {
        int price = 0;
        for (Product p: productList) {
            if (p.getID() == productID) {
                price = p.getPrice();
            }
        }
        return price;
    }

    public Set<Product> getProductList(){
        return productList;
    }


    /*
     * EFFECTS: return objects(employee) as json object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("productList", productsToJson());
        return json;
    }

    // EFFECTS: returns employee in this list as a JSON array
    private JSONArray productsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Product p : productList) {
            jsonArray.put(p.toJson());
        }

        return jsonArray;
    }

}
