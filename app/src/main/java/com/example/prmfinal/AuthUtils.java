package com.example.prmfinal;

import android.content.Context;
import android.content.SharedPreferences;

public class AuthUtils {
    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");
        int userId = prefs.getInt("user_id", -1);
        boolean isLoggedIn = prefs.getBoolean("is_logged_in", false);

        return !token.isEmpty() && userId != -1 && isLoggedIn;
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();
    }

    public static int getCurrentUserId(Context context) {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getInt("user_id", -1);
    }

    public static String getAuthToken(Context context) {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                .getString("auth_token", "");
    }
}
