package com.example.prmfinal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    @GET("api/Product/getAll")
    Call<ProductResponse> getProducts(@Query("size") int size, @Query("page") int page);

    @GET("api/Product/getDetailsById/{id}")
    Call<ProductDetailResponse> getProductDetails(@Path("id") int productId);
}