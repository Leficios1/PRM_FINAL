package com.example.prmfinal;

import java.util.Date;

public class OrderResponseDTO {
    private int orderId;
    private Date orderDate;
    private double totalPrice;
    private int status;
    private String statusMessage;
    private String paymentName;
    private String nameCustomer;
    private String addressCustomer;
    private String phoneCustomer;
    private int userId;

    // Constructor
    public OrderResponseDTO(int orderId, Date orderDate, double totalPrice, int status,
                            String statusMessage, String paymentName, String nameCustomer,
                            String addressCustomer, String phoneCustomer, int userId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.statusMessage = statusMessage;
        this.paymentName = paymentName;
        this.nameCustomer = nameCustomer;
        this.addressCustomer = addressCustomer;
        this.phoneCustomer = phoneCustomer;
        this.userId = userId;
    }

    // Getters and Setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public String getAddressCustomer() {
        return addressCustomer;
    }

    public void setAddressCustomer(String addressCustomer) {
        this.addressCustomer = addressCustomer;
    }

    public String getPhoneCustomer() {
        return phoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.phoneCustomer = phoneCustomer;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

