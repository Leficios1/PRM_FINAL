package com.example.prmfinal;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {
//
//    private OrderService orderService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_orders);
//
//        orderService = ApiClient.getClient().create(OrderService.class);
//
//        fetchAllOrders();
//    }
//
//    private void fetchAllOrders() {
//        String token = "Bearer YOUR_JWT_TOKEN"; // Replace with actual token
//        Call<ApiResponse> call = orderService.getAllOrders(token, 1, 10);
//
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // Process response data, e.g., update RecyclerView
//                } else {
//                    // Handle unsuccessful response
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//                // Handle error
//            }
//        });
//    }
}

