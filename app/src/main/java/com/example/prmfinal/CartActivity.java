package com.example.prmfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemUpdateListener {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView totalPriceText;
    private ProgressBar progressBar;
    private Button checkOutBtn;
    private double totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);

        initViews();
        loadCartItems();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.cartRecyclerView);
        totalPriceText = findViewById(R.id.totalPriceText);
        progressBar = findViewById(R.id.progressBar);
        checkOutBtn = findViewById(R.id.checkoutButton);

        adapter = new CartAdapter(this, cartItems, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        checkOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                intent.putExtra("cartItems", new ArrayList<>(cartItems));
                intent.putExtra("totalPrice", totalPrice);
                startActivity(intent);
            }
        });
    }

    private void loadCartItems() {
        progressBar.setVisibility(View.VISIBLE);

        String token = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("auth_token", "");
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);

        CartServices service = NetworkService.getClient().create(CartServices.class);
        service.getCartByUserId("Bearer " + token, userId)
                .enqueue(new Callback<CartResponse>() {
                    @Override
                    public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.updateItems(response.body().getData());
                            cartItems = response.body().getData();
                            updateTotalPrice();
                        } else {
                            Toast.makeText(CartActivity.this,
                                    "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CartResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(CartActivity.this,
                                "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getQuantity();
        }
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        totalPriceText.setText("Tổng tiền: " + formatter.format(total) + " ₫");
        totalPrice = total;
    }

    @Override
    public void onQuantityChanged(CartItem item, int newQuantity) {
        // Implement quantity update logic
    }

    @Override
    public void onItemDeleted(CartItem item) {
        // Implement delete logic
    }
}