package com.example.prmfinal;

import java.util.Date;

public class OrderDetailDTO {
    private int ProductId;
    private int Quantity;
    private double Price;
    public Date ExpiredWarranty;


    // Getters and Setters
    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        this.ProductId = productId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        this.Quantity = quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        this.Price = price;
    }

    public Date getExpiredWarranty() {
        return ExpiredWarranty;
    }

    public void setExpiredWarranty(Date expiredWarranty) {
        ExpiredWarranty = expiredWarranty;
    }
}

