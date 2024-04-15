package com.example.e_commerceapp.Pages.Vendor;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.DeleteCategory_Response;
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.Pages.Vendor.AddCategory.AddCategory;
import com.example.e_commerceapp.Pages.Vendor.V_Home_Adapters.CatAdapter;
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

public class Category_Vendor extends AppCompatActivity {

    RecyclerView recyclerView;
    CatAdapter catAdapter;
    RestCall restCall;
    LinearLayoutManager linearLayoutManager;
    PreferenceManager preferenceManager;
    FloatingActionButton floatingActionButton;
    SearchView searchView;
    List<V_Category_Response.Category> allCategories;
    private int scrollThreshold = 20;
    private boolean isFabVisible = true;
    ImageView bgimg;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_vendor);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        recyclerView=findViewById(R.id.recycle_v_category);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        preferenceManager = new PreferenceManager(this);
        String vendorId = preferenceManager.getVendorId();
        bgimg=findViewById(R.id.cat_bg_image);
        bgimg.setVisibility(View.INVISIBLE);

        allCategories = new ArrayList<>(); // Initialize allCategories list

        if (vendorId != null) {
            CategoryNames(vendorId);
        } else {
            Toast.makeText(this, "Vendor ID not found", Toast.LENGTH_SHORT).show();
        }
        floatingActionButton=findViewById(R.id.addcatbtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Category_Vendor.this, AddCategory.class);
                startActivity(intent);
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

        searchView=findViewById(R.id.search_view_category);
        setupSearchView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CategoryNames(preferenceManager.getVendorId());
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
        List<V_Category_Response.Category> filteredList = new ArrayList<>();

        // Loop through allCategories to find matches with the search text
        for (V_Category_Response.Category category : allCategories) {
            if (category.getCategoryName().toLowerCase().contains(text.toLowerCase())) {
                // If a match is found, add the category to the filtered list
                filteredList.add(category);
            }
        }

        // Update the adapter with the filtered list of categories
        catAdapter.filterList(filteredList);
    }

    private void CategoryNames(String vendorId){
        restCall.getCategories("get_categories", vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Category_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Category_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Category_Response vCategoryResponse) {
                        if (vCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            Collections.reverse(vCategoryResponse.getCategories());
                            allCategories = vCategoryResponse.getCategories(); // Populate allCategories with fetched data
                            catAdapter=new CatAdapter(Category_Vendor.this, allCategories);
                            recyclerView.setAdapter(catAdapter);
                            catAdapter.setEdit(new CatAdapter.EditInterface() {
                                @Override
                                public void EditClick(V_Category_Response.Category Category) {
                                    Intent intent= new Intent(Category_Vendor.this, AddCategory.class);
                                    intent.putExtra("message",Category.getCategoryName());
                                    intent.putExtra("id",Category.getCategoryId());
                                    startActivity(intent);
                                }

                                @Override
                                public void deleteclick(V_Category_Response.Category Category) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(Category_Vendor.this);
                                    builder.setTitle("Confirm Delete");
                                    builder.setMessage("Delete this Category");
                                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            DeleteCategory(Category.getCategoryId());

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
                            bgimg.setVisibility(View.VISIBLE);
                            Toast.makeText(Category_Vendor.this, vCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void DeleteCategory(String id){
        restCall.deleteCategories("delete_category",id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DeleteCategory_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Category_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeleteCategory_Response deleteCategoryResponse) {
                        if (deleteCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            CategoryNames(preferenceManager.getVendorId());

                        }
                        else {
                            Toast.makeText(Category_Vendor.this, deleteCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}