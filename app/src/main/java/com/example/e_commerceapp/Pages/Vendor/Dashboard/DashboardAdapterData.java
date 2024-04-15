package com.example.e_commerceapp.Pages.Vendor.Dashboard;

import com.example.e_commerceapp.NetworkResponse.Get_Cart_Response;

import java.util.List;

public class DashboardAdapterData {
    private List<Get_Cart_Response.CartItem> cartItems;
    private String totalQuantity;
    private String totalAmount;

    public DashboardAdapterData(List<Get_Cart_Response.CartItem> cartItems, String totalQuantity, String totalAmount) {
        this.cartItems = cartItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
    }

    public List<Get_Cart_Response.CartItem> getCartItems() {
        return cartItems;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}