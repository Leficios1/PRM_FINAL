package com.example.prmfinal;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.Header;

public interface OrderService {

    @GET("api/Order/getAll")
    Call<ApiResponse> getAllOrders(
            @Header("Authorization") String token,
            @Query("page") Integer page,
            @Query("size") Integer size
    );

    @GET("api/Order/GetByUserId/{id}")
    Call<ApiResponse> getOrderByUserId(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Query("status") Integer status
    );

    @GET("api/Order/Totalprice")
    Call<ApiResponse> getTotalPrice(
            @Header("Authorization") String token,
            @Query("ordertime") String ordertime,
            @Query("type") int type
    );

    @GET("api/Order/bestsellerbycategy")
    Call<ApiResponse> getBestSellerByCategory(@Header("Authorization") String token);

    @GET("api/Order/getorderdetailsbyorderid/{id}")
    Call<ApiResponse> getOrderDetailsByOrderId(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @GET("api/Order/getfiveweek")
    Call<ApiResponse> getFiveWeekRevenue(@Header("Authorization") String token);

    @POST("api/Order/create")
    Call<ApiResponse> createOrder(
            @Header("Authorization") String token,
            @Body OrderRequestDTO dto
    );

    @PUT("api/Order/updateStatus/{id}/{status}")
    Call<ApiResponse> updateStatusOrder(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Path("status") int status
    );
}

