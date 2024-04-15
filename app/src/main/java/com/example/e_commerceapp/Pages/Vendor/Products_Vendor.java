package com.example.e_commerceapp.Pages.Vendor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.DeleteProduct_Response;
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.NetworkResponse.V_Product_Response;
import com.example.e_commerceapp.NetworkResponse.V_Sub_Category_Response;
import com.example.e_commerceapp.Pages.Vendor.AddNewCat.AddProducts;
import com.example.e_commerceapp.Pages.Vendor.V_Home_Adapters.ProductAdapter;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Products_Vendor extends AppCompatActivity {
    RecyclerView recyclerView;
    Spinner spinnerone, spinnertwo;
    RestCall restCall;
    PreferenceManager preferenceManager;
    LinearLayoutManager linearLayoutManager;
    FloatingActionButton floatingActionButton;
    ProductAdapter productAdapter;
    V_Category_Response vCategoryResponse;
    List<String> categoryNames;
    List<String> subcategoryNames;

    V_Sub_Category_Response vSubCategoryResponse;

    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerAdapterone;

    List<V_Product_Response.Product> products;
    SearchView searchView;
    List<V_Product_Response.Product> allProducts;
    private int scrollThreshold = 20; // Adjust this threshold as needed
    private boolean isFabVisible = true;
    ImageView probgimg;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_vendor);
        recyclerView=findViewById(R.id.recycle_v_products);
        spinnerone=findViewById(R.id.spinner_products_cat);
        spinnertwo=findViewById(R.id.spinner_products_subcat);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(this);
        linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        floatingActionButton=findViewById(R.id.addproductbtn);

        probgimg=findViewById(R.id.product_bg_img);
        probgimg.setVisibility(View.INVISIBLE);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Products_Vendor.this, AddProducts.class);
                startActivity(intent);
            }
        });

        getCategories();
        spinnerone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryId= getCategoryName(parent.getItemAtPosition(position).toString());
                getSubCategory(categoryId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnertwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subcategoryId=getSubcatNames(parent.getItemAtPosition(position).toString());
                Gettingproducts(preferenceManager.getCategory_id(),subcategoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > scrollThreshold && isFabVisible) {

                    isFabVisible = false;
                    floatingActionButton.hide();
                } else if (dy < -scrollThreshold && !isFabVisible) {

                    isFabVisible = true;
                    floatingActionButton.show();
                }
            }
        });
        searchView=findViewById(R.id.searchview_products);
        setupSearchView();

    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String text) {
        // Create a filtered list to hold the filtered categories
        List<V_Product_Response.Product> filteredList = new ArrayList<>();

        // Loop through allCategories to find matches with the search text
        for (V_Product_Response.Product product : allProducts) {
            if (product.getProductName().toLowerCase().contains(text.toLowerCase())) {
                // If a match is found, add the category to the filtered list
                filteredList.add(product);
            }
        }

        // Update the adapter with the filtered list of categories
        productAdapter.filterList(filteredList);
    }

    private void getCategories(){
        String vendorId = preferenceManager.getVendorId();
        restCall.getCategories("get_categories",vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Category_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Products_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Category_Response vCategoryResponse) {
                        if (vCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            Products_Vendor.this.vCategoryResponse=vCategoryResponse;
                            categoryNames = new ArrayList<>();
                            for (V_Category_Response.Category category:vCategoryResponse.getCategories()){
                                categoryNames.add(category.getCategoryName());
                            }
                            spinnerAdapter=new ArrayAdapter<>(Products_Vendor.this, android.R.layout.simple_spinner_item,categoryNames);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerone.setAdapter(spinnerAdapter);
                        }
                        else {
                            Toast.makeText(Products_Vendor.this, vCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private String getCategoryName(String categoryName){
        if (vCategoryResponse!= null){
            List<V_Category_Response.Category> categories=vCategoryResponse.getCategories();
            if (categories != null){
                for (V_Category_Response.Category category : categories){
                    if (category.getCategoryName().equals(categoryName)){
                        return  category.getCategoryId();
                    }
                }
            } else {
                Log.e("Product_Vendor","Categories list in vCategoryResponse is null");
            }
        }else {
            Log.e("Product_Vendor","vCategoryResponse is null");
        }

        return null;
    }

    private void getSubCategory(String categoryId){
        String vendorId= preferenceManager.getVendorId();
        restCall.getSubcategories("get_subcategories", categoryId, vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Sub_Category_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Products_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Sub_Category_Response vSubCategoryResponse) {
                        if (vSubCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            Products_Vendor.this.vSubCategoryResponse=vSubCategoryResponse;
                            subcategoryNames=new ArrayList<>();
                            for (V_Sub_Category_Response.Subcategory subcategory:vSubCategoryResponse.getSubcategories()){
                                subcategoryNames.add(subcategory.getSubcategoryName());
                            }
                            spinnerAdapterone=new ArrayAdapter<>(Products_Vendor.this, android.R.layout.simple_spinner_item,subcategoryNames);
                            spinnerAdapterone.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnertwo.setAdapter(spinnerAdapterone);

                        }else{
                            Toast.makeText(Products_Vendor.this, vSubCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
    private String getSubcatNames(String subCatName){

        if (vSubCategoryResponse!=null){
            List<V_Sub_Category_Response.Subcategory> subcategories=vSubCategoryResponse.getSubcategories();
            if (subcategories != null){
                for (V_Sub_Category_Response.Subcategory subcategory: subcategories){
                    if (subcategory.getSubcategoryName().equals(subCatName)){
                        return subcategory.getSubcategoryId();
                    }
                }
            } else {
                Log.e("Product_Vendor","SubCategory List in vSubCategoryResponse is null");
            }
        }else {
            Log.e("Product_Vendor","vSubCategoryResponse is null");
        }
        return null;
    }

    private void Gettingproducts(String categoryId, String subcategoryId){
        restCall.Getproducts("get_products",preferenceManager.getVendorId(),categoryId,subcategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Product_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Products_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Product_Response vProductResponse) {
                            if (vProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                                Collections.reverse(vProductResponse.getProducts());
                                products=vProductResponse.getProducts();
                                if (products != null){
                                    allProducts=vProductResponse.getProducts();
                                    productAdapter= new ProductAdapter(Products_Vendor.this,allProducts);
                                    recyclerView.setAdapter(productAdapter);
                                }

                                productAdapter.setEdit(new ProductAdapter.EditInterface() {
                                    @Override
                                    public void EditClick(V_Product_Response.Product Product) {
                                        Intent intent=new Intent(Products_Vendor.this, AddProducts.class);
                                        intent.putExtra("message",Product.getProductName());
                                        intent.putExtra("id",Product.getProductId());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void DeleteClick(V_Product_Response.Product Product) {
                                        AlertDialog.Builder builder=new AlertDialog.Builder(Products_Vendor.this);
                                        builder.setTitle("Confirm Delete");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DeleteProduct(Product.getProductId());
                                            }
                                        });
                                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        builder.create().show();

                                    }
                                });
                            }
                            else {
                                probgimg.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.INVISIBLE);
                            }
                    }
                });
    }

    private void DeleteProduct(String id){
        restCall.deleteProducts("delete_product",id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeleteProduct_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Products_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeleteProduct_Response deleteProductResponse) {
                        if (deleteProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            getSubcatNames(preferenceManager.getSubcategory_id());
                        }
                        else {
                            Toast.makeText(Products_Vendor.this, deleteProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




}