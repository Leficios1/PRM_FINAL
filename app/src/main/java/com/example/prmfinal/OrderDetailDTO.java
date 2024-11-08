package com.example.prmfinal;

import java.util.Date;

public class OrderDetailDTO {
    private int productId;
    private int quantity;
    private double price;
    private Date expiredWarranty;

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExpiredWarranty() {
        return expiredWarranty;
    }

    public void setExpiredWarranty(Date expiredWarranty) {
        this.expiredWarranty = expiredWarranty;
    }
}

