package com.example.e_commerceapp.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.Login_Response;
import com.example.e_commerceapp.Pages.HomePage;
import com.example.e_commerceapp.Pages.V_Home;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Login extends AppCompatActivity {

    ImageButton signup;
    TextView loginTextView, last;
    PreferenceManager preferenceManager;
    RestCall restCall;
    EditText email;

    EditText passedit;
    ImageButton login;
    String getemail,getpassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.log_u_signup);
        loginTextView = findViewById(R.id.login);

        // Initialize PreferenceManager
        preferenceManager = new PreferenceManager(this);

        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        email = findViewById(R.id.usernameEditText);

        passedit = findViewById(R.id.pass);
        login = findViewById(R.id.loginbtn);
        last = findViewById(R.id.lasttxt);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getemail = email.getText().toString().trim();
                getpassword = passedit.getText().toString().trim();

                if (getemail.equals("") || getpassword.equals("")) {
                    Toast.makeText(Login.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                } else {
                    if (loginTextView.getText().toString().equals("User Login")) {
                        loginUser();
                    } else {
                        loginVendor();
                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, User_register.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("isVendor")) {
            boolean isVendor = intent.getBooleanExtra("isVendor", false);
            if (isVendor) {
                loginTextView.setText("Vendor Login");
            } else {
                loginTextView.setText("User Login");
            }
        }

    }

    public void loginUser() {
        restCall.UserLogin("customerlogin", getemail, getpassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Login_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        String err = e.getLocalizedMessage();
                        last.setText(err);
                    }

                    @Override
                    public void onNext(Login_Response loginResponse) {
                        if (loginResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            // Save customer ID to preferences
                            preferenceManager.setCustomerId(VariableBag.User_id, loginResponse.getCustomerId());
                            Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this, HomePage.class);
                            startActivity(i);
                            finish(); // Finish current activity after login
                        } else {
                            Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void loginVendor() {
        restCall.LoginVendor("vendorlogin", getemail, getpassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Login_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Login.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Login_Response loginResponse) {
                        if (loginResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            // Save vendor ID to preferences
                            preferenceManager.setVendorId(loginResponse.getVendorId());
                            Toast.makeText(Login.this, "Login success", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Login.this, V_Home.class);
                            startActivity(i);
                            finish(); // Finish current activity after login
                        } else {
                            Toast.makeText(Login.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}