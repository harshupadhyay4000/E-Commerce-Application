package com.example.e_commerceapp.Pages.Vendor.Dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Dashboard extends AppCompatActivity {
    RecyclerView recyclerView;
    RestCall restCall;
    PreferenceManager preferenceManager;

    ImageView bgimg, bgtxt;

    DashboardAdapter dashboardAdapter;
   // List<Get_Cart_Response.CartItem>cartItems;
    List<Getorder_Response.CartItem>cartItems;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(this);
        bgimg=findViewById(R.id.order_bg_img);
        bgtxt=findViewById(R.id.order_bg_txt);
        bgimg.setVisibility(View.INVISIBLE);
        bgtxt.setVisibility(View.INVISIBLE);
        recyclerView=findViewById(R.id.recycle_order);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        orderdetails();
    }


//    private void orderdetails(){
//        restCall.Getcart("get_cart",preferenceManager.getCustomerId())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Get_Cart_Response>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(Dashboard.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(Get_Cart_Response getCartResponse) {
//                        if (getCartResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
//                            cartItems = getCartResponse.getCartItems(); // Assign received cartItems here
//
//
//                            String totalQuantity = calculateTotalQuantity(cartItems);
//                            String totalAmount = calculateTotalAmount(cartItems);
//
//                            dashboardAdapter = new DashboardAdapter(Dashboard.this, cartItems, totalQuantity, totalAmount);
//                            recyclerView.setAdapter(dashboardAdapter);
//                        }
//                        else {
//                            Toast.makeText(Dashboard.this, getCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
//                            bgimg.setVisibility(View.VISIBLE);
//                            bgtxt.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//    }

    private void orderdetails(){
        restCall.getOrder("get_order",preferenceManager.getCustomerId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Getorder_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Dashboard.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Getorder_Response getorderResponse) {
                        if (getorderResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            cartItems= getorderResponse.getCartItems();
                            String totalQuantity= calculateTotalQuantity(cartItems);
                            String totalAmount=calculateTotalAmount(cartItems);

                            dashboardAdapter=new DashboardAdapter(Dashboard.this,cartItems,totalQuantity,totalAmount);
                            recyclerView.setAdapter(dashboardAdapter);

                        }
                        else {
                            bgimg.setVisibility(View.VISIBLE);
                            Toast.makeText(Dashboard.this, getorderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String calculateTotalQuantity(List<Getorder_Response.CartItem> cartItems) {
        int total = 0;
        for (Getorder_Response.CartItem item : cartItems) {
            total += Integer.parseInt(item.getQuantity());
        }
        return String.valueOf(total);
    }

    private String calculateTotalAmount(List<Getorder_Response.CartItem> cartItems) {
        double total = 0;
        for (Getorder_Response.CartItem item : cartItems) {
            total += Double.parseDouble(item.getProductPrice().replace("Rs ", "")) * Integer.parseInt(item.getQuantity());
        }
        return String.valueOf(total);
    }

}