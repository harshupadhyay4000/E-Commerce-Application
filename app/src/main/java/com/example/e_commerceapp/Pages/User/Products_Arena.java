package com.example.e_commerceapp.Pages.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.MasterApi_Response;
import com.example.e_commerceapp.Pages.User.Adapters.UsercatAdapter;
import com.example.e_commerceapp.Pages.User.Adapters.UserproductAdapter;
import com.example.e_commerceapp.Pages.User.Adapters.UsersubcatAdapter;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Products_Arena extends AppCompatActivity implements UsercatAdapter.CategoryClickListener, UsersubcatAdapter.SubcategoryClickedListener {
    RecyclerView recyclerView, recyclerViewTwo, recyclerViewThree;
    RestCall restCall;
    PreferenceManager preferenceManager;

    List<MasterApi_Response.Category> categories;
    List<MasterApi_Response.Subcategory> subcategories;
    List<MasterApi_Response.Product> products;

    UsercatAdapter usercatAdapter;
    UsersubcatAdapter usersubcatAdapter;
    UserproductAdapter userproductAdapter;

    LinearLayoutManager linearLayoutManager, linearLayoutManagertwo, linerLayoutManagerthree;
    ImageView subcat,productimg;
    ImageButton cart;
    NestedScrollView nestedScrollView;
    SearchView searchView;
    ImageView itemconfirm;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_arena);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager = new PreferenceManager(this);
        // Get the vendor ID passed from CategoryAdapter
        String vendorId = getIntent().getStringExtra("vendor_id");
        itemconfirm=findViewById(R.id.cart_item_confirm);
        itemconfirm.setVisibility(View.INVISIBLE);
        // Set the vendor ID in PreferenceManager
        preferenceManager.setVendorId(vendorId);
        subcat=findViewById(R.id.subcategory_text);
        subcat.setVisibility(View.INVISIBLE);
        productimg=findViewById(R.id.product_text);
        productimg.setVisibility(View.INVISIBLE);
        nestedScrollView = findViewById(R.id.nested_scroll_view);

        recyclerView = findViewById(R.id.recycle_u_one);
        recyclerViewTwo = findViewById(R.id.recycle_u_two);
        recyclerViewThree = findViewById(R.id.recycle_u_three);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagertwo = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linerLayoutManagerthree = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerViewTwo.setLayoutManager(linearLayoutManagertwo);
        recyclerViewThree.setLayoutManager(linerLayoutManagerthree);

        // Initialize adapters
        usercatAdapter = new UsercatAdapter(this, new ArrayList<>(), this);
        usersubcatAdapter = new UsersubcatAdapter(this, new ArrayList<>(), this);
        userproductAdapter = new UserproductAdapter(this, new ArrayList<>(), itemconfirm );

        // Set adapters
        recyclerView.setAdapter(usercatAdapter);
        recyclerViewTwo.setAdapter(usersubcatAdapter);
        recyclerViewThree.setAdapter(userproductAdapter);

        // Initially hide the subcategory RecyclerView
        recyclerViewTwo.setVisibility(View.GONE);

        getMasterData();

        cart=findViewById(R.id.cart_btn);
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Products_Arena.this, Add_to_Cart.class);
                startActivity(intent);
            }
        });
        searchView=findViewById(R.id.search_product_arena);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (products != null) {
                    List<MasterApi_Response.Product> filteredProducts = new ArrayList<>();
                    for (MasterApi_Response.Product product : products) {
                        if (product.getProductName().toLowerCase().contains(newText.toLowerCase())) {
                            filteredProducts.add(product);
                        }
                    }
                    userproductAdapter.setProducts(filteredProducts);
                }
                return true;
            }
        });

    }

    private void getMasterData() {
        restCall.Getdata("master_api", preferenceManager.getVendorId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MasterApi_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Products_Arena.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MasterApi_Response masterApiResponse) {
                        if (masterApiResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            categories = masterApiResponse.getCategories();
                            usercatAdapter.setCategories(categories);
                            subcategories = masterApiResponse.getSubcategories();
                            usersubcatAdapter.setSubcategories(subcategories);
                            products = masterApiResponse.getProducts();
                        }
                    }
                });
    }

    @Override
    public void onCategoryClicked(String categoryId) {
        // Filter subcategories based on the clicked category
        List<MasterApi_Response.Subcategory> filteredSubcategories = new ArrayList<>();
        for (MasterApi_Response.Subcategory subcategory : subcategories) {
            if (subcategory.getCategoryId().equals(categoryId)) {
                filteredSubcategories.add(subcategory);
            }
        }

        usersubcatAdapter.setSubcategories(filteredSubcategories);
        recyclerViewTwo.setVisibility(View.VISIBLE);
        subcat.setVisibility(View.VISIBLE);
        recyclerViewTwo.smoothScrollToPosition(0);
    }

    @Override
    public void onSubcategoryClicked(String subcategoryId) {
        if (products != null) {
            List<MasterApi_Response.Product> filteredProducts = new ArrayList<>();
            for (MasterApi_Response.Product product : products) {
                if (product.getSubcategoryId().equals(subcategoryId)) {
                    filteredProducts.add(product);
                }
            }
            productimg.setVisibility(View.VISIBLE);
            userproductAdapter.setProducts(filteredProducts);
            recyclerViewThree.smoothScrollToPosition(0); // Scroll to the first position
        } else {
            Toast.makeText(this, "Products list is null", Toast.LENGTH_SHORT).show();
        }
    }
}