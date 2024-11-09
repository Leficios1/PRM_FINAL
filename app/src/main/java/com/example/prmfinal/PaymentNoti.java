package com.example.prmfinal;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentNoti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String result = getIntent().getStringExtra("result");

        // Set the appropriate layout based on payment result
        if ("Thanh toán thành công".equals(result)) {
            setContentView(R.layout.activity_payment_success);

            String transactionId = getIntent().getStringExtra("transactionId");
            double totalAmount = getIntent().getDoubleExtra("totalAmount",0);

            NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
            TextView transactionAmount = findViewById(R.id.transactionAmount);
            TextView transactionIdView = findViewById(R.id.transactionId);
            TextView paymentDate = findViewById(R.id.paymentDate);

            // Set the transaction amount
            if (totalAmount != 0) {
                transactionAmount.setText(formatter.format(totalAmount) + " ₫");
            }

            // Set the transaction ID
            if (transactionId != null) {
                transactionIdView.setText(transactionId);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("vi", "VN"));
            String currentDate = sdf.format(new Date());
            paymentDate.setText(currentDate);

            Button btnBackToHome = findViewById(R.id.btnBackToHome);
            deleteCartItemsByUserId();
            btnBackToHome.setOnClickListener(v -> {
                Intent homeIntent = new Intent(PaymentNoti.this, ProductActivity.class);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homeIntent);
                finish();
            });

        } else {
            setContentView(R.layout.activity_payment_fail);
            Intent homeIntent = new Intent(PaymentNoti.this, ProductActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
            finish();
        }
    }

    private void deleteCartItemsByUserId() {
        String token = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("auth_token", "");
        int userId = getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);

        CartServices service = NetworkService.getClient().create(CartServices.class);
        service.DeleteAllCartsByUserId("Bearer " + token, userId)
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG,"delete cart items successfully");
                        } else {
                            Log.d(TAG,"delete cart items failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(PaymentNoti.this,
                                "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

