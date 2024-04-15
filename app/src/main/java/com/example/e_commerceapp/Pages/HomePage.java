package com.example.e_commerceapp.Pages;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Category.CategoryAdapter;
import com.example.e_commerceapp.MainActivity;
import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.Vendor_Response;
import com.example.e_commerceapp.Pages.Vendor.Dashboard.Dashboard;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomePage extends AppCompatActivity {
    RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    RestCall restCall;
    ImageView errorImage;
    BroadcastReceiver networkReceiver, bluetoothStateReceiver;

    ImageButton logout;
    PreferenceManager preferenceManager;
    ImageButton orders;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        recyclerView = findViewById(R.id.recycle_home);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        preferenceManager=new PreferenceManager(this);
        errorImage=findViewById(R.id.nointernet_home);
        checkBluetoothState();
        orders=findViewById(R.id.order_page_btn);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this, Dashboard.class);
                startActivity(intent);
            }
        });

        logout=findViewById(R.id.logoutbtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferenceManager.setCustomerId(PreferenceManager.KEY_CUSTOMER_ID, null);
                preferenceManager.setVendorId(null);

                Intent loginIntent = new Intent(HomePage.this, MainActivity.class);
                startActivity(loginIntent);
                finish();

            }
        });
        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = isConnectedToInternet();
                if (isConnected) {
                    errorImage.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    VendorNames();
                } else {
                    errorImage.setVisibility(View.VISIBLE);
                    errorImage.setImageResource(R.drawable.no_internet);
                    recyclerView.setVisibility(View.INVISIBLE);
                    Toast.makeText(HomePage.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);

        bluetoothStateReceiver= new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state=intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state==BluetoothAdapter.STATE_OFF){
                    Toast.makeText(context, "Bluetooth is OFF, Kindly Turn ON", Toast.LENGTH_SHORT).show();
                }

            }
        };
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReceiver, bluetoothFilter);

    }
    private void VendorNames() {
        restCall.getVendorNames("get_vendors")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Vendor_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(HomePage.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Vendor_Response vendorResponse) {
                        if (vendorResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            categoryAdapter=new CategoryAdapter(HomePage.this,vendorResponse.getVendors());
                            recyclerView.setAdapter(categoryAdapter);
                        }
                        else {
                            Toast.makeText(HomePage.this, "No Vendor Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void checkBluetoothState() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is OFF, Kindly Turn ON", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnected();
        }
        return false;
    }

}