package com.example.prmfinal;

import java.util.List;

public class CartResponse {
    private List<CartItem> data;
    private String message;

    public List<CartItem> getData() {
        return data;
    }

    public void setData(List<CartItem> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
