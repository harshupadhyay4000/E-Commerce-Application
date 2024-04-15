package com.example.e_commerceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.e_commerceapp.Login.Login;
import com.example.e_commerceapp.Pages.HomePage;
import com.example.e_commerceapp.Pages.V_Home;
import com.example.e_commerceapp.Pages.Vendor.Reminder;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    ImageButton user, vendor, scanner, reminder, timer;
    ImageView errorImage;
    TextView usertxt, vendortxt, title, data;
    BroadcastReceiver networkReceiver, bluetoothStateReceiver, locationStateReceiver;

    private static final int PERMISSION_REQUEST_BLUETOOTH = 101;
    private static final int PERMISSION_REQUEST_LOCATION = 102;
    private static final int PERMISSION_REQUEST_CAMERA = 103;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        user = findViewById(R.id.userbtn);
        vendor = findViewById(R.id.vendorbtn);
        errorImage = findViewById(R.id.nointernet);
        usertxt = findViewById(R.id.user_text);
        vendortxt = findViewById(R.id.vendor_text);
        title = findViewById(R.id.title_text);
        scanner = findViewById(R.id.scanner_icon);
        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check if camera permission is granted
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // Camera permission has not been granted, request it
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                        } else {
                            // Camera permission is already granted, initiate QR code scan
                            initiateScan();
                        }
                    }
                });

            }
        });
        timer = findViewById(R.id.timerbtn);
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Timer.class);
                startActivity(intent);
            }
        });
        reminder = findViewById(R.id.reminder_icon);
        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Reminder.class);
                startActivity(intent);
            }
        });
        data = findViewById(R.id.qr_data);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndNavigateToLogin(false);
            }
        });

        vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndNavigateToLogin(true);
            }
        });
        checkInternetConnection();

        networkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isConnected = isConnectedToInternet();
                if (isConnected) {
                    errorImage.setVisibility(View.INVISIBLE);
                    user.setVisibility(View.VISIBLE);
                    usertxt.setVisibility(View.VISIBLE);
                    vendor.setVisibility(View.VISIBLE);
                    vendortxt.setVisibility(View.VISIBLE);
                    title.setVisibility(View.VISIBLE);
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
        checkBluetoothState();
        checkLocationState();

        bluetoothStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_OFF) {
                    Toast.makeText(context, "Bluetooth is OFF, Kindly Turn ON", Toast.LENGTH_SHORT).show();
                }

            }
        };
        IntentFilter bluetoothFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(bluetoothStateReceiver, bluetoothFilter);

        locationStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!isLocationEnabled()) {
                    Toast.makeText(context, "Location is OFF, Please turn it ON", Toast.LENGTH_SHORT).show();
                }
            }
        };
        IntentFilter locationFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(locationStateReceiver, locationFilter);


        PreferenceManager preferenceManager = new PreferenceManager(this);


        String userId = preferenceManager.getCustomerId();
        if (userId != null) {

            Intent homeIntent = new Intent(MainActivity.this, HomePage.class);
            startActivity(homeIntent);
            finish();
            return;
        }


        String vendorId = preferenceManager.getVendorId();
        if (vendorId != null) {

            Intent vendorHomeIntent = new Intent(MainActivity.this, V_Home.class);
            startActivity(vendorHomeIntent);
            finish();
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkReceiver);
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(locationStateReceiver);
    }

    private void checkAndNavigateToLogin(boolean isVendor) {
        if (isConnectedToInternet()) {
            navigateToLogin(isVendor);
        } else {
            errorImage.setImageResource(R.drawable.no_internet);
            errorImage.setVisibility(View.VISIBLE);
            user.setVisibility(View.INVISIBLE);
            usertxt.setVisibility(View.INVISIBLE);
            vendor.setVisibility(View.INVISIBLE);
            vendortxt.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
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

    private void navigateToLogin(boolean isVendor) {
        Intent i = new Intent(MainActivity.this, Login.class);
        i.putExtra("isVendor", isVendor);
        startActivity(i);
    }

    private void checkBluetoothState() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is OFF, Kindly Turn ON", Toast.LENGTH_SHORT).show();
        } else {
            requestBluetoothPermission();
        }
    }

    private void checkLocationState() {
        if (!isLocationEnabled()) {
            Toast.makeText(this, "Location is OFF, Please turn it ON", Toast.LENGTH_SHORT).show();
        } else {
            requestLocationPermission();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void checkInternetConnection() {
        if (!isConnectedToInternet()) {
            errorImage.setImageResource(R.drawable.no_internet);
            errorImage.setVisibility(View.VISIBLE);
            user.setVisibility(View.INVISIBLE);
            usertxt.setVisibility(View.INVISIBLE);
            vendor.setVisibility(View.INVISIBLE);
            vendortxt.setVisibility(View.INVISIBLE);
            title.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this, "Please connect to the internet", Toast.LENGTH_SHORT).show();
        } else {
            errorImage.setVisibility(View.INVISIBLE);
            user.setVisibility(View.VISIBLE);
            usertxt.setVisibility(View.VISIBLE);
            vendor.setVisibility(View.VISIBLE);
            vendortxt.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
        }
    }

    private void requestBluetoothPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    PERMISSION_REQUEST_BLUETOOTH);
        }
    }

    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_LOCATION);
        }
    }


    private void initiateScan() {
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        integrator.setPrompt("Scan a QR code");
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, initiate QR code scan
                initiateScan();
            } else {
                // Camera permission denied, show a message or handle it accordingly
                Toast.makeText(MainActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String scannedData = result.getContents();
                this.data.setText(scannedData);
                if (scannedData.startsWith("http://") || scannedData.startsWith("https://")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(scannedData));
                    startActivity(browserIntent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}