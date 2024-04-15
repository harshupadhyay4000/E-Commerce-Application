package com.example.e_commerceapp.NetworkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login_Response {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("vendor_id")
    @Expose
    private String vendorId;

    @SerializedName("customer_id")
    @Expose
    private String customerId;

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

}
