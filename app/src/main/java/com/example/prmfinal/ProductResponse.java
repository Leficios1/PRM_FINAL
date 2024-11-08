package com.example.prmfinal;

public class ProductResponse {
    private ProductData data;
    private String message;

    public ProductData getData() { return data; }
    public void setData(ProductData data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
