package com.example.e_commerceapp.Category;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vendor {
    @SerializedName("vendor_name")
    @Expose
    private String name;

    @SerializedName("vendor_image")
    @Expose
    private String imageUrl;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
