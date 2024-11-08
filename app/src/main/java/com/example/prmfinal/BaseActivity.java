package com.example.prmfinal;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    protected boolean isLoggedIn() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");
        int userId = prefs.getInt("user_id", -1);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        return !token.isEmpty() && userId != -1 && isLoggedIn;
    }

    protected String getAuthToken() {
        return getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getString("auth_token", "");
    }

    protected int getUserId() {
        return getSharedPreferences("user_prefs", MODE_PRIVATE)
                .getInt("user_id", -1);
    }
}
