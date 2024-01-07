package ui;

import model.*;
import persistence.*;
import java.util.Scanner;

public class Manager {

    private String jsonStoreSales = "./data/SalesRecord.json";
    private String jsonProducts = "./data/Products.json";
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JsonWriter jsonWriterProducts;
    private JsonReader jsonReaderProducts;
    private SalesRecord salesRecord;
    private ProductList productList;


    public Manager() {
        input = new Scanner(System.in);
        jsonWriter = new JsonWriter(jsonStoreSales);
        jsonReader = new JsonReader(jsonStoreSales);
        jsonWriterProducts = new JsonWriter(jsonProducts);
        jsonReaderProducts = new JsonReader(jsonProducts);
        salesRecord = new SalesRecord();
        productList = ProductList.getInstance();
    }

    public void test() {
        Product pro1 = new Product("No476家内安全御守(内符入)", 10, 500, Category.valueOf("お守り"));
        Product pro2 = new Product("白神矢G(絵馬・赤短冊・鈴・ﾘﾎﾞﾝ付)60cm", 10, 500, Category.valueOf("その他"));
        Product pro3 = new Product("祈願絵馬(A)諸願成就", 10, 500, Category.valueOf("絵馬"));

        ProductList list = ProductList.getInstance();
        list.addProduct(pro1);
        list.addProduct(pro2);
        list.addProduct(pro3);

        saveProductInfo(list);

        Sale sale1 = new Sale(2,pro1);
        Sale sale2 = new Sale(1,pro2);
        Sale sale3 = new Sale(2,pro3);

        Total total1 = new Total();
        Total total2 = new Total();

        total1.addSales(sale1);
        total1.addSales(sale2);
        total2.addSales(sale3);

        SalesRecord salesRecord1 = new SalesRecord();
        salesRecord1.addTotal(total1);
        salesRecord1.addTotal(total2);

        saveSalesRecords(salesRecord1);


    }
    // MODIFIES: this
    // EFFECTS: loads workroom from file
    public void loadSalesRecords() {
        try {
            salesRecord = jsonReader.read();

        } catch (Exception e) {
        }
    }

    public void reset() {
        jsonWriter = new JsonWriter(jsonStoreSales);
        jsonReader = new JsonReader(jsonStoreSales);
        salesRecord = new SalesRecord();
    }

    public void changeSalesRecordName(String name) {
        jsonStoreSales = name;
    }

    public void backUpSales(SalesRecord salesRecord, String name) {
        try {
            JsonWriter jsonWriterBackUp;
            jsonWriterBackUp = new JsonWriter(name);
            jsonWriterBackUp.open();
            jsonWriterBackUp.write(salesRecord);
            jsonWriterBackUp.close();

        } catch (Exception ex) {
            //
        }
    }

    public SalesRecord getSalesRecord() {
        return salesRecord;
    }

    public ProductList getProductList() {
        return productList;
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    public void loadProductInfo() {
        try {
            productList = jsonReaderProducts.readProduct();
            //System.out.println("Loaded employee information from " + JSON_STORE);
        } catch (Exception e) {
            //System.out.println("Unable to read from file: " + JSON_STORE);//
        }
    }

    public void printProducts() {
        for (Product p: productList.getProductList()) {
            System.out.println(p.getName());
        }
    }


    // EFFECTS: saves the workroom to file
    public void saveProductInfo(ProductList productList) {
        try {
            jsonWriterProducts.open();
            jsonWriterProducts.write(productList);
            jsonWriterProducts.close();

        } catch (Exception ex) {
            //
        }

    }

    // EFFECTS: saves the workroom to file
    public void saveSalesRecords(SalesRecord salesRecord) {
        try {
            jsonWriter.open();
            jsonWriter.write(salesRecord);
            jsonWriter.close();

        } catch (Exception ex) {
            //
        }

    }

}
