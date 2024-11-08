package com.example.prmfinal;

public class BaseResponse {
    private Object data; // Có thể null hoặc chứa dữ liệu trả về
    private String message; // Thông báo từ server

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
