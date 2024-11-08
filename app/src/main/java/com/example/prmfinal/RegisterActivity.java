package com.example.prmfinal;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private EditText phoneField;
    private EditText addressField;
    private EditText dateOfBirthField;
    private Spinner genderSpinner;
    private Button registerButton;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // Initialize views
        nameField = findViewById(R.id.registerName);
        emailField = findViewById(R.id.registerEmail);
        passwordField = findViewById(R.id.registerPassword);
        confirmPasswordField = findViewById(R.id.confirmRegisterPassword);
        phoneField = findViewById(R.id.registerPhone);
        addressField = findViewById(R.id.registerAddress);
        dateOfBirthField = findViewById(R.id.registerDateOfBirth);
        genderSpinner = findViewById(R.id.registerGender);
        registerButton = findViewById(R.id.registerButton);

        // Initialize calendar and date formatter
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        // Setup gender spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);

        // Setup date picker dialog
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, day);
            updateDateOfBirthField();
        };

        dateOfBirthField.setOnClickListener(v -> {
            new DatePickerDialog(RegisterActivity.this, date,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        registerButton.setOnClickListener(v -> {
            if (validateInputs()) {
                performRegistration();
            }
        });
    }

    private void updateDateOfBirthField() {
        dateOfBirthField.setText(dateFormatter.format(calendar.getTime()));
    }

    private boolean validateInputs() {
        String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();
        String phone = phoneField.getText().toString().trim();
        String address = addressField.getText().toString().trim();
        String dateOfBirth = dateOfBirthField.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) ||
                TextUtils.isEmpty(dateOfBirth)) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void performRegistration() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        UserService service = NetworkService.getClient().create(UserService.class);

        RegisterRequest request = new RegisterRequest(
                emailField.getText().toString().trim(),
                passwordField.getText().toString(),
                nameField.getText().toString().trim(),
                phoneField.getText().toString().trim(),
                addressField.getText().toString().trim(),
                dateOfBirthField.getText().toString() + "T00:00:00.000Z",
                genderSpinner.getSelectedItem().toString()
        );

        service.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                progressDialog.dismiss();
                Log.d("RegisterActivity", "Response code: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    RegisterResponse registerResponse = response.body();
                    if (registerResponse.getData() != null) {
                        saveUserInfo(registerResponse.getData());
                        Toast.makeText(RegisterActivity.this,
                                registerResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(RegisterActivity.this, ProductActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: Invalid response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Unknown error";
                        Log.e("RegisterActivity", "Error body: " + errorBody);
                        Toast.makeText(RegisterActivity.this,
                                "Registration failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("RegisterActivity", "Network error", t);
                Toast.makeText(RegisterActivity.this,
                        "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserInfo(UserData userData) {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("user_id", userData.getId());
        editor.putString("user_name", userData.getName());
        editor.putString("user_email", userData.getEmail());
        editor.putString("user_phone", userData.getPhone());
        editor.putString("user_avatar", userData.getAvatarUrl());
        editor.putString("user_gender", userData.getGender());
        editor.putBoolean("user_status", userData.isStatus());
        editor.putInt("user_role", userData.getRoleId());

        // Lưu trạng thái đăng nhập
        editor.putBoolean("is_logged_in", true);

        editor.apply();
    }
    }
