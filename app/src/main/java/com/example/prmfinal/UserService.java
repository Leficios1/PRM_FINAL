package com.example.prmfinal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("api/User/create")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
