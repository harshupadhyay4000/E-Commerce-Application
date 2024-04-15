package com.example.e_commerceapp.Pages.Vendor.AddNewCat;

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
import com.example.e_commerceapp.NetworkResponse.Delete_SubCat_Response;
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.NetworkResponse.V_Sub_Category_Response;
import com.example.e_commerceapp.Pages.Vendor.AddNewCat.Adapter.SubcatAdapter;
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

public class SubCategory_Vendor extends AppCompatActivity {
    RecyclerView recyclerView;
    RestCall restCall;
    LinearLayoutManager linearLayoutManager;
    PreferenceManager preferenceManager;
    V_Category_Response vCategoryResponse;
    FloatingActionButton floatingActionButton;

    SubcatAdapter subcatAdapter;
    Spinner spinner;
    List<String> categoryNames;
    ArrayAdapter<String> spinnerAdapter;
    List<V_Sub_Category_Response.Subcategory> subcategories;
    private int scrollThreshold = 20; // Adjust this threshold as needed
    private boolean isFabVisible = true;
    ImageView bgimagesub;
    SearchView searchView;
    List<V_Sub_Category_Response.Subcategory> allSubcategories;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_vendor2);
        recyclerView = findViewById(R.id.recycle_v_sub_categoryone);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        preferenceManager = new PreferenceManager(this);
        bgimagesub=findViewById(R.id.subcat_bg_img);
        bgimagesub.setVisibility(View.INVISIBLE);
        allSubcategories = new ArrayList<>();
        spinner = findViewById(R.id.spinner_sub_cat_two);
        floatingActionButton = findViewById(R.id.addsubcatbtnone);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SubCategory_Vendor.this, AddsubCat.class);
                startActivity(intent);
            }
        });

        getCategories();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String categoryId = getCategoryName(parent.getItemAtPosition(position).toString());
                getSubcategories(categoryId);
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

        searchView=findViewById(R.id.search_view_subcategory);
        setupSearchView();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (spinner.getSelectedItem() != null) {
            String categoryId = getCategoryName(spinner.getSelectedItem().toString());
            if (categoryId != null) {
                getSubcategories(categoryId);
            }
        }
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
        List<V_Sub_Category_Response.Subcategory> filteredList = new ArrayList<>();

        // Loop through allCategories to find matches with the search text
        for (V_Sub_Category_Response.Subcategory subcategory : allSubcategories) {
            if (subcategory.getSubcategoryName().toLowerCase().contains(text.toLowerCase())) {
                // If a match is found, add the category to the filtered list
                filteredList.add(subcategory);
            }
        }

        // Update the adapter with the filtered list of categories
        subcatAdapter.filterList(filteredList);
    }


    private void getCategories() {
        String vendorId = preferenceManager.getVendorId();
        restCall.getCategories("get_categories", vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Category_Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SubCategory_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Category_Response vCategoryResponse) {
                        if (vCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            SubCategory_Vendor.this.vCategoryResponse = vCategoryResponse;
                            categoryNames = new ArrayList<>();
                            for (V_Category_Response.Category category : vCategoryResponse.getCategories()) {
                                categoryNames.add(category.getCategoryName());
                            }
                            spinnerAdapter = new ArrayAdapter<>(SubCategory_Vendor.this, android.R.layout.simple_spinner_item, categoryNames);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerAdapter);
                        }
                    }
                });
    }

    private String getCategoryName(String categoryName) {
        if (vCategoryResponse != null) {
            List<V_Category_Response.Category> categories = vCategoryResponse.getCategories();
            if (categories != null) {
                for (V_Category_Response.Category category : categories) {
                    if (category.getCategoryName().equals(categoryName)) {
                        return category.getCategoryId();
                    }
                }
            } else {
                Log.e("SubCategory_Vendor", "Categories list in vCategoryResponse is null");
            }
        } else {
            Log.e("SubCategory_Vendor", "vCategoryResponse is null");
        }
        return null;
    }

    private void getSubcategories(String categoryId) {
        String vendorId = preferenceManager.getVendorId();

            restCall.getSubcategories("get_subcategories", categoryId, vendorId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<V_Sub_Category_Response>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(SubCategory_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(V_Sub_Category_Response vSubCategoryResponse) {
                            if (vSubCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                                Collections.reverse(vSubCategoryResponse.getSubcategories());
                                subcategories = vSubCategoryResponse.getSubcategories();
                                if (subcategories != null) {
                                    allSubcategories=vSubCategoryResponse.getSubcategories();
                                    subcatAdapter = new SubcatAdapter(SubCategory_Vendor.this, allSubcategories);
                                    recyclerView.setAdapter(subcatAdapter);

                                }
                                subcatAdapter.setEdit(new SubcatAdapter.EditInterface() {
                                    @Override
                                    public void EditClick(V_Sub_Category_Response.Subcategory Subcategory) {
                                        Intent intent= new Intent(SubCategory_Vendor.this, AddsubCat.class);
                                        intent.putExtra("message",Subcategory.getSubcategoryName());
                                        intent.putExtra("id",Subcategory.getSubcategoryId());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void DeleteClick(V_Sub_Category_Response.Subcategory Subcategory) {
                                        AlertDialog.Builder builder= new AlertDialog.Builder(SubCategory_Vendor.this);
                                        builder.setTitle("Confirm Delete");
                                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                DeleteSubCat(Subcategory.getSubcategoryId());
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
                                bgimagesub.setVisibility(View.VISIBLE);
                            }
                        }
                    });

    }
    private void DeleteSubCat(String id){
        restCall.deleteSubcat("delete_subcategory",id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Delete_SubCat_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(SubCategory_Vendor.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Delete_SubCat_Response deleteSubCatResponse) {
                        if (deleteSubCatResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            getCategoryName(preferenceManager.getCategory_id());

                        }
                        else {
                            Toast.makeText(SubCategory_Vendor.this, deleteSubCatResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}