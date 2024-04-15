package com.example.e_commerceapp.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerceapp.MainActivity;
import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.AddUser_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class User_register extends AppCompatActivity {

    PreferenceManager preferenceManager;
    RestCall restCall;
    TextView login;
    EditText name, email, mobil, password, confpassword;
    Button signupbt,v_signupbt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        name=findViewById(R.id.firstname);

        mobil=findViewById(R.id.mobile);
        email=findViewById(R.id.emailid);
        password=findViewById(R.id.passw);
        confpassword=findViewById(R.id.confpass);
        signupbt=findViewById(R.id.signupbtn);
        v_signupbt=findViewById(R.id.vendorregbtn);


        preferenceManager=new PreferenceManager(this);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);




        v_signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam=name.getText().toString();

                String et=email.getText().toString();
                String pas=password.getText().toString();
                String conf=confpassword.getText().toString();
                String mob=mobil.getText().toString();
                if (name.equals("")|| et.equals("")||pas.equals("")||conf.equals("")){
                    Toast.makeText(User_register.this, "Fill Required Details", Toast.LENGTH_SHORT).show();
                }else if (mob.equals("")) {
                    mobil.setError("Enter Mobile Number");
                    mobil.requestFocus();
                }else if (mob.length()<10) {
                    mobil.setError("Enter Valid Mobile Number");
                    mobil.requestFocus();
                }else if (et.equals("")) {
                    email.setError("Enter Email");
                    email.requestFocus();
                }else if (!isValidEmail(et)) {
                    Toast.makeText(User_register.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if (pas.equals("")) {
                    Toast.makeText(User_register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }else if (conf.equals("")) {
                    Toast.makeText(User_register.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }else if (!pas.equals(conf)) {
                    Toast.makeText(User_register.this, "Password Should be Same", Toast.LENGTH_SHORT).show();
                }
                else {
                    Addvendor();
                }

            }
        });

        signupbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nam=name.getText().toString();

                String et=email.getText().toString();
                String pas=password.getText().toString();
                String conf=confpassword.getText().toString();
                String mob=mobil.getText().toString();
                if (name.equals("")|| et.equals("")||pas.equals("")||conf.equals("")){
                    Toast.makeText(User_register.this, "Fill Required Details", Toast.LENGTH_SHORT).show();
                }else if (mob.equals("")) {
                    mobil.setError("Enter Mobile Number");
                }else if (mob.length()<10) {
                    mobil.setError("Enter Valid Mobile Number");
                }else if (et.equals("")) {
                    email.setError("Enter Email");
                    email.requestFocus();
                }else if (!isValidEmail(et)) {
                    Toast.makeText(User_register.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }else if (pas.equals("")) {
                    Toast.makeText(User_register.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }else if (conf.equals("")) {
                    Toast.makeText(User_register.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                }else if (!pas.equals(conf)) {
                    Toast.makeText(User_register.this, "Password Should be Same", Toast.LENGTH_SHORT).show();
                }
                else {
                        Adduser();
                    }

            }
        });

    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$";
        return email.matches(emailRegex);
    }

    public void Adduser(){
        restCall.AddUser("register_customer",name.getText().toString(),mobil.getText().toString(),email.getText().toString(),password.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddUser_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Toast.makeText(User_register.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(AddUser_Response addUserResponse) {
                        if(addUserResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            Toast.makeText(User_register.this, "User Registration SuccessFull", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(User_register.this, MainActivity.class);
                            startActivity(i);

                        }
                        else {
                            Toast.makeText(User_register.this, ""+addUserResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    public void Addvendor(){
        restCall.AddVendor("register_vendor",name.getText().toString(),mobil.getText().toString(),email.getText().toString(),password.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddUser_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(User_register.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNext(AddUser_Response addUserResponse) {
                        if (addUserResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            Toast.makeText(User_register.this, "Vendor Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(User_register.this, MainActivity.class);
                           startActivity(i);
                        }
                        else {
                            Toast.makeText(User_register.this, ""+addUserResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



}