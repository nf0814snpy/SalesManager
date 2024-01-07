package persistence;

import model.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.stream.Stream;

// Represents a reader that reads workroom from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }


    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public SalesRecord read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseSalesRecord(jsonObject);
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public ProductList readProduct() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseProducts(jsonObject);
    }



    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (Exception ex) {
            //
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private SalesRecord parseSalesRecord(JSONObject jsonObject) {
        SalesRecord salesList = new SalesRecord();
        addSalesRecords(salesList, jsonObject);
        return salesList;
    }

    // EFFECTS: parses workroom from JSON object and returns it
    private ProductList parseProducts(JSONObject jsonObject) {
        ProductList productList = ProductList.getInstance();
        addProduct(productList, jsonObject);
        return productList;
    }

    // MODIFIES: productList
    // EFFECTS: parses a product from a JSON object and adds it to the productList
    private void addProduct(ProductList productList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("productList");
        for (Object json : jsonArray) {
            JSONObject nextProduct = (JSONObject) json;
            addProducts(productList, nextProduct);
        }
    }


    // MODIFIES: productList
    // EFFECTS: parses a product from a JSON object and adds it to the productList
    private void addProducts(ProductList productList, JSONObject jsonObject){
        String name = jsonObject.getString("name");
        int quantity = jsonObject.getInt("quantity");
        int price = jsonObject.getInt("price");
        int id = jsonObject.getInt("id");
        String categoryString = jsonObject.getString("category");
        Category category = Category.valueOf(categoryString);
        Product product = new Product(name, quantity, price,category);
        productList.addProduct(product);
    }


    // MODIFIES: salesRecord
    // EFFECTS: parses a sale from a JSON object and adds it to the salesRecord
    private void addSalesRecords(SalesRecord salesRecord, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("salesRecords");
        for (Object json : jsonArray) {
            JSONObject nextTotal = (JSONObject) json;
            addTotal(salesRecord, nextTotal);
        }
    }

    // MODIFIES: salesRecord
    // EFFECTS: parses a sale from a JSON object and adds it to the salesRecord
    private void addTotal (SalesRecord salesRecord, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("total");
        int year = jsonObject.getInt("year");
        int month = jsonObject.getInt("month");
        int day = jsonObject.getInt("day");
        int hour = jsonObject.getInt("hour");
        int minute = jsonObject.getInt("minute");
        int totalPrice = jsonObject.getInt("totalPrice");
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
        Total total = new Total(dateTime);
        for (Object json : jsonArray) {
            JSONObject nextSale = (JSONObject) json;
            addSales(total, nextSale);
        }
        salesRecord.addTotal(total);
    }


    //MODIFIES: salesRecord
    // EFFECTS: parses a sale from a JSON object and adds it to the salesRecord
    private void addSales(Total total, JSONObject jsonObject) {
        int num = jsonObject.getInt("num");
        int id = jsonObject.getInt("productID");
        Sale sale = new Sale(num,id);
        total.addSales(sale);
    }

}
