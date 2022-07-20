package com.latienditadeportiva.Inventory_ms.models;

import org.springframework.data.annotation.Id;

public class Product{
    @Id
    private String productId;
    private String productName;
    private String imageURL;
    private double price;
    private Integer stock;
    private String description;
    private String category;
    private int sales;

    public Product(String productId, String productName, String imageURL, double price, Integer stock, String description, String category, int sales) {
        this.productId = productId;
        this.productName = productName;
        this.imageURL = imageURL;
        this.price = price;
        this.stock = stock;
        this.description = description;
        this.category = category;
        this.sales = sales;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }
}