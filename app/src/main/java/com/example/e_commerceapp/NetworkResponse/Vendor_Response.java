package com.example.e_commerceapp.NetworkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vendor_Response {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("vendors")
    @Expose
    private List<Vendor> vendors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Vendor> getVendors() {
        return vendors;
    }

    public void setVendors(List<Vendor> vendors) {
        this.vendors = vendors;
    }



    public class Vendor {

        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("vendor_name")
        @Expose
        private String vendorName;
        @SerializedName("vendor_image")
        @Expose
        private String vendorImage;

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getVendorName() {
            return vendorName;
        }

        public void setVendorName(String vendorName) {
            this.vendorName = vendorName;
        }

        public String getVendorImage() {
            return vendorImage;
        }

        public void setVendorImage(String vendorImage) {
            this.vendorImage = vendorImage;
        }

    }
}
