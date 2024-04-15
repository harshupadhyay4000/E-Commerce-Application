package com.example.e_commerceapp.NetworkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Getorder_Response implements Serializable {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("cart_items")
    @Expose
    private List<CartItem> cartItems;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class CartItem implements Serializable {

        @SerializedName("cart_item_id")
        @Expose
        private String cartItemId;
        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("product_id")
        @Expose
        private String productId;
        @SerializedName("customer_id")
        @Expose
        private String customerId;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("product_price")
        @Expose
        private String productPrice;
        @SerializedName("product_name")
        @Expose
        private String productName;
        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("order_number")
        @Expose
        private String orderNumber;
        @SerializedName("cart_status")
        @Expose
        private String cartStatus;
        @SerializedName("confirmation_status")
        @Expose
        private String confirmationStatus;
        @SerializedName("added_date")
        @Expose
        private String addedDate;
        @SerializedName("update_date")
        @Expose
        private String updateDate;


        public String getCartItemId() {
            return cartItemId;
        }

        public void setCartItemId(String cartItemId) {
            this.cartItemId = cartItemId;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public String getCartStatus() {
            return cartStatus;
        }

        public void setCartStatus(String cartStatus) {
            this.cartStatus = cartStatus;
        }
        public String getConfirmationStatus() {
            return confirmationStatus;
        }

        public void setConfirmationStatus(String confirmationStatus) {
            this.confirmationStatus = confirmationStatus;
        }

        public String getAddedDate() {
            return addedDate;
        }

        public void setAddedDate(String addedDate) {
            this.addedDate = addedDate;
        }

        public String getUpdateDate() {
            return updateDate;
        }

        public void setUpdateDate(String updateDate) {
            this.updateDate = updateDate;
        }

    }
}
