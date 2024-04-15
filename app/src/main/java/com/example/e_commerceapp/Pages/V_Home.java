package com.example.e_commerceapp.Pages;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Category.Category;
import com.example.e_commerceapp.Category.V_HomeAdapter;
import com.example.e_commerceapp.MainActivity;
import com.example.e_commerceapp.Pages.Vendor.VendorDashboard.VendorDashboard;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class V_Home extends AppCompatActivity {

    RecyclerView recyclerView;
    V_HomeAdapter v_homeAdapter;
    Button orders;
    ImageButton logout;
    PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vhome);
        recyclerView = findViewById(R.id.recycle_v_home);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        logout=findViewById(R.id.logout_vendor);
        preferenceManager=new PreferenceManager(this);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                preferenceManager.setVendorId(null);


                Intent loginIntent = new Intent(V_Home.this, MainActivity.class);
                startActivity(loginIntent);
                finish(); // Finish current activity to prevent going back to it on back press
            }
        });
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Categories", R.drawable.cat_section));
        categories.add(new Category("Sub Categories", R.drawable.sub_catergory));
        categories.add(new Category("Products", R.drawable.products_section));

        v_homeAdapter = new V_HomeAdapter(this, categories);
        recyclerView.setAdapter(v_homeAdapter);
        orders=findViewById(R.id.orders_v_btn);


        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(V_Home.this, VendorDashboard.class);
                startActivity(intent);
            }
        });

    }
}