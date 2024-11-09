package com.example.prmfinal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("api/User/create")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("api/User/getById")
    Call<ApiResponse> GetById(@Header("Authorization") String token, @Query("id") int id);
}
