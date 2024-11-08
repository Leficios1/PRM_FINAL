package com.example.prmfinal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends BaseActivity {
    private ViewPager2 imageSlider;
    private TabLayout indicator;
    private TextView productName;
    private TextView productPrice;
    private TextView warrantyInfo;
    private TextView productDescription;
    private TableLayout detailsTable;
    private Button addToCartButton;
    private ImageSliderAdapter sliderAdapter;
    private ProgressBar progressBar;

    private ProductInfo currentProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_item);

        // Lấy product ID từ intent
        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId == -1) {
            Toast.makeText(this, "Invalid product", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupSlider();
        loadProductDetails(productId);

        // Setup click listener cho nút Add to Cart
        setupAddToCart();
    }

    private void initViews() {
        imageSlider = findViewById(R.id.imageSlider);
        indicator = findViewById(R.id.indicator);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        warrantyInfo = findViewById(R.id.warrantyInfo);
        productDescription = findViewById(R.id.productDescription);
        detailsTable = findViewById(R.id.detailsTable);
        addToCartButton = findViewById(R.id.addToCartButton);
        progressBar = findViewById(R.id.progressBar);

        // Back button
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
    }

    private void setupSlider() {
        // Initialize with empty list, will update when data is loaded
        sliderAdapter = new ImageSliderAdapter(this, new ArrayList<>());
        imageSlider.setAdapter(sliderAdapter);

        // Set up TabLayout with ViewPager2
        new TabLayoutMediator(indicator, imageSlider, (tab, position) -> {
            // Optional: customize tab appearance
        }).attach();
    }

    private void loadProductDetails(int productId) {
        progressBar.setVisibility(View.VISIBLE);
        ProductService service = NetworkService.getClient().create(ProductService.class);

        service.getProductDetails(productId).enqueue(new Callback<ProductDetailResponse>() {
            @Override
            public void onResponse(Call<ProductDetailResponse> call, Response<ProductDetailResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    ProductDetailResponse productResponse = response.body();
                    if (productResponse.getData() != null) {
                        // Lưu thông tin sản phẩm hiện tại
                        currentProduct = productResponse.getData().getProducts();
                        displayProductDetails(productResponse.getData());
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this,
                            "Error loading product details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetailResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ProductDetailActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayProductDetails(ProductDetailData data) {
        // Display basic product info
        ProductInfo product = data.getProducts();
        productName.setText(product.getName());

        // Format price in Vietnamese currency
        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(product.getPrice()) + " ₫";
        productPrice.setText(formattedPrice);

        warrantyInfo.setText(String.format("Bảo hành: %d ngày", product.getWarrantyPeriod()));
        productDescription.setText(product.getDescription());

        // Create list of all images
        List<String> allImages = new ArrayList<>();

        // Add main product image first
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            allImages.add(product.getImageUrl());
        }

        // Add all detail images
        if (data.getImages() != null) {
            for (ProductImage image : data.getImages()) {
                if (image.getUrl() != null && !image.getUrl().isEmpty()) {
                    allImages.add(image.getUrl());
                }
            }
        }

        // Update slider with all images
        updateImageSlider(allImages);

        // Display product details in table
        displayProductDetails(data.getDetails());
    }

    private void updateImageSlider(List<String> imageUrls) {
        List<ProductImage> images = new ArrayList<>();
        for (String url : imageUrls) {
            ProductImage image = new ProductImage();
            image.setUrl(url);
            images.add(image);
        }
        sliderAdapter.updateImages(images);

        // Show first image
        imageSlider.setCurrentItem(0);

        // Make indicator visible only if there are multiple images
        indicator.setVisibility(images.size() > 1 ? View.VISIBLE : View.GONE);
    }

    private void displayProductDetails(List<ProductDetail> details) {
        detailsTable.removeAllViews();

        if (details != null && !details.isEmpty()) {
            for (ProductDetail detail : details) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT));

                // Name column
                TextView nameView = new TextView(this);
                nameView.setText(detail.getName());
                nameView.setPadding(16, 8, 16, 8);
                nameView.setTextColor(getResources().getColor(R.color.colorPrimary));

                // Value column
                TextView valueView = new TextView(this);
                valueView.setText(detail.getValue());
                valueView.setPadding(16, 8, 16, 8);

                row.addView(nameView);
                row.addView(valueView);

                detailsTable.addView(row);
            }
        }
    }
    private void setupAddToCart() {
        addToCartButton.setOnClickListener(v -> {
            // Log current auth state
            String token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("auth_token", "");
            int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
            boolean isLoggedIn = getSharedPreferences("user_prefs", MODE_PRIVATE).getBoolean("is_logged_in", false);

            Log.d("ProductDetail", "Token: " + token);
            Log.d("ProductDetail", "UserId: " + userId);
            Log.d("ProductDetail", "IsLoggedIn: " + isLoggedIn);

            if (token.isEmpty() || userId == -1 || !isLoggedIn) {
                Toast.makeText(this, "Vui lòng đăng nhập để thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }

            // Rest of your add to cart code...
            CartRequest request = new CartRequest(currentProduct.getId(), 1, userId);

            CartServices cartService = NetworkService.getClient().create(CartServices.class);
            cartService.addToCart("Bearer " + token, request).enqueue(new Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                    // Log response for debugging
                    Log.d("ProductDetail", "Response code: " + response.code());
                    if (response.errorBody() != null) {
                        try {
                            Log.d("ProductDetail", "Error body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(ProductDetailActivity.this,
                                "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                        showGoToCartDialog();
                    } else {
                        if (response.code() == 401) {
                            // Token hết hạn
                            clearLoginState();
                            Toast.makeText(ProductDetailActivity.this,
                                    "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ProductDetailActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(ProductDetailActivity.this,
                                    "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Log.e("ProductDetail", "Network error", t);
                    Toast.makeText(ProductDetailActivity.this,
                            "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void clearLoginState() {
        getSharedPreferences("user_prefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }

    private void showGoToCartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Thêm vào giỏ hàng thành công")
                .setMessage("Bạn có muốn xem giỏ hàng không?")
                .setPositiveButton("Xem giỏ hàng", (dialog, which) -> {
                    Intent intent = new Intent(this, CartActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Tiếp tục mua sắm", null)
                .show();
    }
}
