package com.example.prmfinal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("api/Auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
