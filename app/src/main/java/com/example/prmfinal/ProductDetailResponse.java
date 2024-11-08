package com.example.prmfinal;

public class ProductDetailResponse {
    private ProductDetailData data;
    private String message;

    public ProductDetailData getData() { return data; }
    public void setData(ProductDetailData data) { this.data = data; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
