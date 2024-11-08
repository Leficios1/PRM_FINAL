package com.example.prmfinal;

public class RegisterResponse {
    private UserData data;
    private String message;

    public UserData getData() { return data; }
    public void setData(UserData data) { this.data = data; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
