package com.example.prmfinal;

public class CartRequest {
    private int productId;
    private int quantity;
    private int userId;

    public CartRequest(int productId, int quantity, int userId) {
        this.productId = productId;
        this.quantity = quantity;
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
