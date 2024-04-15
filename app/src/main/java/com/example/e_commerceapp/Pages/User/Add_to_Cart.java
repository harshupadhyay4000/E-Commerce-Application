package com.example.e_commerceapp.Pages.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.AddOrder_Response;
import com.example.e_commerceapp.NetworkResponse.Cart_item_Response;
import com.example.e_commerceapp.NetworkResponse.Get_Cart_Response;
import com.example.e_commerceapp.Pages.User.Adapters.CartAdapter;
import com.example.e_commerceapp.Pages.Vendor.Dashboard.Dashboard;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Add_to_Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton applycoupon, applycouponbtn;
    Button checkout;
    TextView Amount,totalAmt;
    CartAdapter cartAdapter;
    List<Get_Cart_Response.CartItem>Cartitem;
    RestCall restCall;
    PreferenceManager preferenceManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        // Initialize views
        recyclerView = findViewById(R.id.recycle_product);
        applycoupon = findViewById(R.id.coupon);
        applycouponbtn = findViewById(R.id.applycouponbtn);
        checkout = findViewById(R.id.proceed_to_checkout);
        Amount = findViewById(R.id.amount);
        totalAmt = findViewById(R.id.totalamt);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager = new PreferenceManager(this);

        getcart();
        cart();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Cartitem != null && !Cartitem.isEmpty()) {
                    // Cart is not empty, proceed with checkout
                    addorder();
                    Intent intent = new Intent(Add_to_Cart.this, Dashboard.class);
                    String totalQuantity = Amount.getText().toString();
                    String totalAmount = totalAmt.getText().toString();
                    intent.putExtra("totalQuantity", totalQuantity);
                    intent.putExtra("totalAmount", totalAmount);
                    intent.putExtra("customerId", preferenceManager.getCustomerId());
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(Add_to_Cart.this, "Your cart is Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getcart(){
        restCall.Getcart("get_cart",preferenceManager.getCustomerId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Get_Cart_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Get_Cart_Response getCartResponse) {
                        if (getCartResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){

                            double totalAmount = 0;

                            StringBuilder amountBuilder = new StringBuilder(); // StringBuilder to accumulate individual item amounts

                            Cartitem = getCartResponse.getCartItems(); // Assign CartItem list

                            for (Get_Cart_Response.CartItem cartItem : Cartitem) {
                                double itemPrice = Double.parseDouble(cartItem.getProductPrice().replace("Rs ", ""));
                                int itemQuantity = Integer.parseInt(cartItem.getQuantity());

                                double itemAmount = itemPrice * itemQuantity;
                                amountBuilder.append(cartItem.getProductName()).append(": Rs ").append(itemAmount).append("\n");
                                totalAmount += itemAmount;
                            }

                            totalAmt.setText(String.valueOf(totalAmount));
                            Amount.setText(String.valueOf(totalAmount));

                            // Set the adapter after loading the data
                            cartAdapter = new CartAdapter(Add_to_Cart.this, Cartitem);
                            recyclerView.setAdapter(cartAdapter);
                        }
                        else {

                            Toast.makeText(Add_to_Cart.this, getCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void cart(){

        restCall.itemCart("get_cart", preferenceManager.getCustomerId(), preferenceManager.getVendorId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Cart_item_Response>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Add_to_Cart.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Cart_item_Response cartItemResponse) {
                        if (cartItemResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {

                        }
                    }
                });
    }

    private void addorder(){
        restCall.addOrder("add_order",preferenceManager.getCustomerId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddOrder_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Add_to_Cart.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddOrder_Response addOrderResponse) {
                        if (addOrderResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){

                        }
                        else {
                            Toast.makeText(Add_to_Cart.this, addOrderResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
