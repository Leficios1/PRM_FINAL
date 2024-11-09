package com.example.prmfinal;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView orderHistoryListView;
    private OrderHistoryAdapter orderHistoryAdapter;
    private List<OrderResponseDTO> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history);

        orderHistoryListView = findViewById(R.id.order_history_item);
        orderHistoryAdapter = new OrderHistoryAdapter(this, orderList);
        orderHistoryListView.setAdapter(orderHistoryAdapter);

        fetchOrderHistory();
    }

    private void fetchOrderHistory() {
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
        String jwtToken = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("auth_token", "");

        OrderService orderService = NetworkService.getClient().create(OrderService.class);
        Call<ApiResponse> call = orderService.getOrderByUserId("Bearer " + jwtToken, userId, null);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();

                    if (apiResponse.statusCode == 200 && apiResponse.data != null) {
                        // Convert JsonObject `data` into a List<OrderResponseDTO>
                        JsonArray orderJsonArray = apiResponse.data.getAsJsonArray("data");
                        List<OrderResponseDTO> orders = new ArrayList<>();

                        Gson gson = new Gson();
                        for (JsonElement element : orderJsonArray) {
                            OrderResponseDTO order = gson.fromJson(element, OrderResponseDTO.class);
                            orders.add(order);
                        }

                        orderList.clear();
                        orderList.addAll(orders);
                        orderHistoryAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(OrderHistoryActivity.this, apiResponse.message, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(OrderHistoryActivity.this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(OrderHistoryActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}


