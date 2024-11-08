package com.example.prmfinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText emailField;  // Changed from usernameField
    private EditText passwordField;
    private Button loginButton;
    private TextView createAccountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_acivity);

        Log.d(TAG, "onCreate: Initializing views");

        // Initialize views
        emailField = findViewById(R.id.usernameField);  // Keep the ID same for now
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);
        createAccountText = findViewById(R.id.createAccountText);

        // Set up login button click listener
        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked");
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString();

            if (!validateLoginInput(email, password)) {
                return;
            }

            performLogin(email, password);
        });

        // Set up create account text click listener
        createAccountText.setOnClickListener(v -> {
            Log.d(TAG, "Create account clicked");
            try {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            } catch (Exception e) {
                Log.e(TAG, "Error starting RegisterActivity", e);
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateLoginInput(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Email is required");
            emailField.requestFocus();
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.setError("Please enter a valid email address");
            emailField.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Password is required");
            passwordField.requestFocus();
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        AuthService service = NetworkService.getClient().create(AuthService.class);
        LoginRequest loginRequest = new LoginRequest(email, password);  // Make sure LoginRequest uses "email" field

        service.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    handleSuccessfulLogin(response.body());
                } else {
                    handleFailedLogin(response);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSuccessfulLogin(LoginResponse loginResponse) {
        Log.d(TAG, "Login successful, saving data");

        // Save login data
        SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();

        // Save token
        String token = loginResponse.getData().getTokenString();
        editor.putString("auth_token", token);

        try {
            // Decode JWT token
            String[] parts = token.split("\\.");
            String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
            JSONObject jsonPayload = new JSONObject(payload);

            // Get user ID from nameidentifier claim
            String userId = jsonPayload.getString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/nameidentifier");
            editor.putInt("user_id", Integer.parseInt(userId));

            // Get user role
            String role = jsonPayload.getString("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
            editor.putString("user_role", role);

            // Get user email
            String email = jsonPayload.getString("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/emailaddress");
            editor.putString("user_email", email);

            // Set logged in status
            editor.putBoolean("is_logged_in", true);

            // Log saved data
            Log.d(TAG, "Saved token: " + token);
            Log.d(TAG, "Saved userId: " + userId);
            Log.d(TAG, "Saved role: " + role);
            Log.d(TAG, "Saved email: " + email);

            // Apply changes
            editor.apply();

            // Navigate to ProductActivity
            Intent intent = new Intent(this, ProductActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        } catch (Exception e) {
            Log.e(TAG, "Error parsing token", e);
            // Log the payload for debugging
            try {
                String[] parts = token.split("\\.");
                String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
                Log.d(TAG, "Token payload: " + payload);
            } catch (Exception ex) {
                Log.e(TAG, "Error logging payload", ex);
            }
            Toast.makeText(this, "Error processing login data", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFailedLogin(Response<LoginResponse> response) {
        try {
            String errorBody = response.errorBody() != null ?
                    response.errorBody().string() : "Unknown error";
            Log.e(TAG, "Error body: " + errorBody);

            // Parse the error message to show a more user-friendly message
            String displayMessage = "Login failed";
            if (errorBody.contains("Email field is required")) {
                displayMessage = "Email is required";
            } else if (errorBody.contains("validation errors")) {
                displayMessage = "Please check your email and password";
            }

            Toast.makeText(LoginActivity.this, displayMessage, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToken(String token) {
        Log.d(TAG, "Saving auth token");
        getSharedPreferences("app_prefs", MODE_PRIVATE)
                .edit()
                .putString("auth_token", token)
                .apply();
    }
    private void saveUserData(LoginResponse loginResponse) {
        Log.d(TAG, "Saving user data and token");

        // Get the JWT claims
        String token = loginResponse.getData().getTokenString();
        int userId = extractUserIdFromToken(token); // Hoặc lấy từ response nếu API trả về

        // Save to SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("user_prefs", MODE_PRIVATE).edit();
        editor.putString("auth_token", token);
        editor.putInt("user_id", userId);
        editor.putBoolean("is_logged_in", true);
        editor.apply();
    }

    private int extractUserIdFromToken(String token) {
        try {
            // Giả sử token format là: header.payload.signature
            String[] parts = token.split("\\.");
            if (parts.length >= 2) {
                String payload = new String(android.util.Base64.decode(parts[1], android.util.Base64.DEFAULT));
                // Parse payload JSON và lấy user ID
                // Ví dụ: {"nameid": "1", ...}
                JSONObject jsonPayload = new JSONObject(payload);
                return Integer.parseInt(jsonPayload.getString("nameid"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error extracting user ID from token", e);
        }
        return -1;
    }
}