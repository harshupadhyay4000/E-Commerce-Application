package com.example.e_commerceapp.Pages.Vendor.VendorDashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.Getorder_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VendorDashboardActivity extends AppCompatActivity {
    RestCall restCall;
    RecyclerView recyclerView;
    PreferenceManager preferenceManager;
    VendorActDashboardAdapter vendorActDashboardAdapter;
    List<Getorder_Response.CartItem> cartItems;
    List<Getorder_Response.CartItem>cartItems1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard2);

        preferenceManager=new PreferenceManager(this);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        recyclerView=findViewById(R.id.recycle_vendor_order_dashboard);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // Get the specific order ID from Intent
        Intent intent = getIntent();
        String orderId = intent.getStringExtra("order_id");
        getOrder();
    }
    // method


    private void getOrder(){
        restCall.getOrder("get_order",preferenceManager.getVendorId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Getorder_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(VendorDashboardActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Getorder_Response getorderResponse) {
                        cartItems1=new ArrayList<>();
                        if (getorderResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            cartItems = getorderResponse.getCartItems();
                            
                            vendorActDashboardAdapter = new VendorActDashboardAdapter(VendorDashboardActivity.this, cartItems);
                            recyclerView.setAdapter(vendorActDashboardAdapter);
                        }
                        else {
                            Toast.makeText(VendorDashboardActivity.this, getorderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}