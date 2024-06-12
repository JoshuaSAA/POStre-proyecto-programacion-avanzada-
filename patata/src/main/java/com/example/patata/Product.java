package com.example.patata;

public class Product {
    private int id;
    private String productName;
    private float price;
    private String description;
    private String picture;
    private int category;

    public Product(int id, String productName, float price, String description, String picture, int category) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.description = description;
        this.picture = picture;
        this.category = category;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }
}
