package com.example.patata;

import java.sql.Timestamp;

public class Sale {
    private int tableId;
    private String productName;
    private float price;
    private int quantity;
    private Timestamp saleTime;

    // Constructores, getters y setters


    public Sale(int tableId, String productName, float price, int quantity, Timestamp saleTime) {
        this.tableId = tableId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.saleTime = saleTime;
    }

    public Sale() {

    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Timestamp saleTime) {
        this.saleTime = saleTime;
    }
}
