package com.example.prmfinal;

import java.util.Date;
import java.util.List;

public class OrderRequestDTO {
    private List<OrderDetailDTO> OrderDetails;
    private String OrderDate; // Changed from Date to String
    private int status;
    private String statusMessage;
    private String PaymentName;
    private String NameCustomer;
    private String AddressCustomer;
    private String PhoneCustomer;
    private int UserId;

    // Getters and Setters
    public List<OrderDetailDTO> getOrderDetails() {
        return OrderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.OrderDetails = orderDetails;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
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
        return PaymentName;
    }

    public void setPaymentName(String paymentName) {
        this.PaymentName = paymentName;
    }

    public String getNameCustomer() {
        return NameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.NameCustomer = nameCustomer;
    }

    public String getAddressCustomer() {
        return AddressCustomer;
    }

    public void setAddressCustomer(String addressCustomer) {
        this.AddressCustomer = addressCustomer;
    }

    public String getPhoneCustomer() {
        return PhoneCustomer;
    }

    public void setPhoneCustomer(String phoneCustomer) {
        this.PhoneCustomer = phoneCustomer;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }
}

