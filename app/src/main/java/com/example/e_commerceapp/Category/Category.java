package com.example.e_commerceapp.Category;

public class Category {
    private String name;
    private int imageResource;

    public Category(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
