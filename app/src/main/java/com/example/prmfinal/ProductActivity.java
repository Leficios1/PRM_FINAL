package com.example.prmfinal;

import static com.example.prmfinal.AuthUtils.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private ListView productListView;
    private ProductAdapter adapter;
    private List<Product> productList = new ArrayList<>();
    private ImageView cartIcon;
    private View sideMenu;
    private DrawerLayout drawerLayout;
    private ImageView menuIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);

        initViews();
        setupMenu();
        setupCartIcon();

        productListView = findViewById(R.id.home_product_listView);
        adapter = new ProductAdapter(this, productList);
        productListView.setAdapter(adapter);


        // Fetch products
        loadProducts();

        // Set click listener
        productListView.setOnItemClickListener((parent, view, position, id) -> {
            Product product = productList.get(position);
             //Handle product click
             Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
             intent.putExtra("product_id", product.getId());
             startActivity(intent);
        });
    }

    private void initViews() {
        productListView = findViewById(R.id.home_product_listView);
        drawerLayout = findViewById(R.id.drawer_layout);
        sideMenu = findViewById(R.id.side_menu);
        menuIcon = findViewById(R.id.menu_icon);
        cartIcon = findViewById(R.id.cart_icon); // Initialize cart icon

        adapter = new ProductAdapter(this, productList);
        productListView.setAdapter(adapter);
    }

    private void setupMenu() {
        // Menu icon click
        menuIcon.setOnClickListener(v -> {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Set user email in header
        TextView headerEmail = sideMenu.findViewById(R.id.header_email);
        String userEmail = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("user_email", "User");
        headerEmail.setText(userEmail);

        // Menu items click listeners
        sideMenu.findViewById(R.id.menu_profile).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, UserActivity.class));
        });

        sideMenu.findViewById(R.id.menu_account).setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);
            startActivity(new Intent(this, AccountPageActivity.class));
        });

        sideMenu.findViewById(R.id.menu_logout).setOnClickListener(v -> {
            logout();
        });
    }

    private void setupCartIcon() {
        cartIcon.setOnClickListener(v -> {
            // Chuyển đến CartActivity
            Intent intent = new Intent(ProductActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }
    private void logout() {
        // Clear user data
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Navigate to login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void loadProducts() {
        ProductService service = NetworkService.getClient().create(ProductService.class);
        service.getProducts(10, 1).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ProductResponse productResponse = response.body();
                    if (productResponse.getData() != null &&
                            productResponse.getData().getProducts() != null) {

                        productList.clear();
                        productList.addAll(productResponse.getData().getProducts());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(ProductActivity.this,
                            "Error loading products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(ProductActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
