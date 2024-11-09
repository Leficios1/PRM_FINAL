package com.example.prmfinal;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prmfinal.Api.CreateOrder;
import com.example.prmfinal.OrderItem;
import com.example.prmfinal.OrderItemAdapter;
import com.example.prmfinal.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CheckOutActivity extends AppCompatActivity {

    private RecyclerView rvOrderItems;
    private OrderItemAdapter orderItemAdapter;
    private TextView tvSubtotalAmount, tvShippingAmount, tvTotalAmount;
    private Spinner paymentMethodSpinner;
    private Button btnConfirmOrder;
    private String transactionId, paymentDate, userName, userPhone;
    private EditText etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        // Find views
        rvOrderItems = findViewById(R.id.rvOrderItems);
        tvSubtotalAmount = findViewById(R.id.tvSubtotalAmount);
        tvShippingAmount = findViewById(R.id.tvShippingAmount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        paymentMethodSpinner = findViewById(R.id.paymentMethodSpinner);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);
        etAddress = findViewById(R.id.etAddress);

        List<CartItem> cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");

        // Get user ID and token from SharedPreferences
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE).getInt("user_id", -1);
        String jwtToken = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("auth_token", "");

        // Fetch user information
        UserService userService = NetworkService.getClient().create(UserService.class);
        Call<ApiResponse> call = userService.GetById("Bearer " + jwtToken, userId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    if (apiResponse.data != null) {
                        userName = String.valueOf(apiResponse.data.get("name")).replaceAll("^\"|\"$", "");
                        userPhone = String.valueOf(apiResponse.data.get("phone")).replaceAll("^\"|\"$", "");

                    } else {
                        Toast.makeText(CheckOutActivity.this, "User data is empty", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CheckOutActivity.this, "Failed to get user information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(CheckOutActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set up order items RecyclerView
        orderItemAdapter = new OrderItemAdapter(cartItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(orderItemAdapter);

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0);
        double totalAmount = totalPrice + 15000;

        // Set order pricing details
        tvSubtotalAmount.setText(formatter.format(totalPrice) + " ₫");
        tvShippingAmount.setText("15.000 ₫");
        tvTotalAmount.setText(formatter.format(totalAmount) + " ₫");

        // Set up payment method spinner
        List<String> paymentMethods = new ArrayList<>();
        paymentMethods.add("Cash On Delivery");
        paymentMethods.add("ZaloPay");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName == null) {
                    Toast.makeText(CheckOutActivity.this, "Please wait for user information to load", Toast.LENGTH_SHORT).show();
                    return;
                }

                String selectedPaymentMethod = paymentMethodSpinner.getSelectedItem().toString();

                if (selectedPaymentMethod.equals("ZaloPay")) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    // ZaloPay SDK Init
                    ZaloPaySDK.init(2553, Environment.SANDBOX);

                    // Prepare total amount for ZaloPay
                    String totalString = String.format("%.0f", totalAmount);

                    CreateOrder orderApi = new CreateOrder();
                    try {
                        JSONObject data = orderApi.createOrder(totalString);
                        String code = data.getString("return_code");
                        if (code.equals("1")) {
                            String token = data.getString("zp_trans_token");
                            transactionId = token;
                            ZaloPaySDK.getInstance().payOrder(CheckOutActivity.this, token, "demozpdk://app", new PayOrderListener() {
                                @Override
                                public void onPaymentSucceeded(String s, String s1, String s2) {
                                    // Prepare OrderRequestDTO with user information
                                    OrderRequestDTO orderRequest = new OrderRequestDTO();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                    orderRequest.setOrderDate(sdf.format(new Date()));
                                    orderRequest.setStatus(1);
                                    orderRequest.setStatusMessage("Processing");
                                    orderRequest.setPaymentName("ZaloPay");
                                    orderRequest.setNameCustomer(userName);
                                    orderRequest.setAddressCustomer(String.valueOf(etAddress.getText()));
                                    orderRequest.setPhoneCustomer(userPhone);
                                    orderRequest.setUserId(userId);

                                    List<OrderDetailDTO> orderDetails = new ArrayList<>();
                                    for (CartItem item : cartItems) {
                                        OrderDetailDTO detail = new OrderDetailDTO();
                                        detail.setProductId(item.getProductId());
                                        detail.setQuantity(item.getQuantity());
                                        detail.setPrice(item.getPrice());
                                        orderDetails.add(detail);
                                    }
                                    orderRequest.setOrderDetails(orderDetails);

                                    // Call the createOrder API
                                    OrderService service = NetworkService.getClient().create(OrderService.class);
                                    Call<ApiResponse> call = service.createOrder("Bearer " + jwtToken, orderRequest);
                                    call.enqueue(new Callback<ApiResponse>() {
                                        @Override
                                        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                                            if (response.isSuccessful() && response.body() != null) {
                                                Intent intent = new Intent(CheckOutActivity.this, PaymentNoti.class);
                                                intent.putExtra("result", "Thanh toán thành công");
                                                intent.putExtra("transactionId", transactionId);
                                                intent.putExtra("totalAmount", totalAmount);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(CheckOutActivity.this, "Order creation failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ApiResponse> call, Throwable t) {
                                            Toast.makeText(CheckOutActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onPaymentCanceled(String s, String s1) {
                                    Intent intent = new Intent(CheckOutActivity.this, PaymentNoti.class);
                                    intent.putExtra("result", "Hủy thanh toán");
                                    startActivity(intent);
                                }

                                @Override
                                public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                                    Intent intent = new Intent(CheckOutActivity.this, PaymentNoti.class);
                                    intent.putExtra("result", "Lỗi thanh toán");
                                    startActivity(intent);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle Cash on Delivery
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}