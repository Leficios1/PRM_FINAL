package com.example.prmfinal;

import java.util.List;

public class ProductDetailData {
    private ProductInfo products;
    private List<ProductDetail> details;
    private List<ProductImage> images;
    private List<ProductReview> reviews;

    // Getters and Setters
    public ProductInfo getProducts() { return products; }
    public void setProducts(ProductInfo products) { this.products = products; }

    public List<ProductDetail> getDetails() { return details; }
    public void setDetails(List<ProductDetail> details) { this.details = details; }

    public List<ProductImage> getImages() { return images; }
    public void setImages(List<ProductImage> images) { this.images = images; }

    public List<ProductReview> getReviews() { return reviews; }
    public void setReviews(List<ProductReview> reviews) { this.reviews = reviews; }
}
