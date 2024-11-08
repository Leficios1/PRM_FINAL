package com.example.prmfinal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CartServices {
    @POST("api/Cart/add-product-into-cart")
    Call<BaseResponse> addToCart(@Header("Authorization") String token, @Body CartRequest request);

    @GET("api/Cart/get-by-user-id/{userId}")
    Call<CartResponse> getCartByUserId(@Header("Authorization") String token, @Path("userId") int userId);
}
