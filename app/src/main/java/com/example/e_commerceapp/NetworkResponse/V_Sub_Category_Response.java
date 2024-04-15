package com.example.e_commerceapp.NetworkResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class V_Sub_Category_Response {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("subcategories")
    @Expose
    private List<Subcategory> subcategories;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class Subcategory {

        @SerializedName("subcategory_id")
        @Expose
        private String subcategoryId;
        @SerializedName("subcategory_name")
        @Expose
        private String subcategoryName;
        @SerializedName("vendor_id")
        @Expose
        private String vendorId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("image_path")
        @Expose
        private String imagePath;

        public String getSubcategoryId() {
            return subcategoryId;
        }

        public void setSubcategoryId(String subcategoryId) {
            this.subcategoryId = subcategoryId;
        }

        public String getSubcategoryName() {
            return subcategoryName;
        }

        public void setSubcategoryName(String subcategoryName) {
            this.subcategoryName = subcategoryName;
        }

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }
    }
}
