package com.example.e_commerceapp.Pages.Vendor.AddNewCat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.AddSubCat_Response;
import com.example.e_commerceapp.NetworkResponse.EditsubCat_Response;
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddsubCat extends AppCompatActivity {
    Spinner spinner;
    EditText text;
    Button addbtn;
    RestCall restCall;
    PreferenceManager preferenceManager;
    List<String> categoryNames;
    ArrayAdapter<String> spinnerAdapter;
    TextView last;
    Boolean IsEdit=false;
    String GetId;
    V_Category_Response vCategoryResponse;
    List<V_Category_Response.Category> category_list;
    ImageButton camera;
    ImageView capturedImage;
    int TAKE_PICTURE = 1;
    String impath;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsub_cat);
        spinner = findViewById(R.id.spinner_addsub_cat_add);
        text = findViewById(R.id.sub_catedt_txt_add);
        addbtn = findViewById(R.id.subcat_addbutton_add);
        restCall = RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager = new PreferenceManager(this);
        String vendorId = preferenceManager.getVendorId();
        last=findViewById(R.id.textlast);
        camera=findViewById(R.id.camera_icon_subcategory);
        capturedImage=findViewById(R.id.captured_image_subcategory);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }
        });

        GetCat(vendorId);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedCategory = parent.getItemAtPosition(position).toString();
                if (category_list!=null)
                {
                    for (V_Category_Response.Category items:category_list)
                    {
                        if (selectedCategory.equals(items.getCategoryName()))
                        {
                            preferenceManager.setCategory_id(items.getCategoryId());
                        }
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent intent=getIntent();
        if (intent!=null&& intent.getStringExtra("message")!=null && intent.getStringExtra("id")!=null){
            IsEdit=true;
            text.setText(intent.getStringExtra("message"));
            addbtn.setText("edit");
            GetId=intent.getStringExtra("id");
        }
        else {
            IsEdit=false;
            addbtn.setText("Add");
        }

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedCategory = spinner.getSelectedItem().toString();
                String enteredText = text.getText().toString();
                 if (text.getText().toString().trim().equals("")){
                     text.setError("Enter SubCategory");
                     text.requestFocus();
                 }
                 else {
                     if (IsEdit){
                         EditSubCat();
                     }else {
                         AddsubCategory();
                     }
                 }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {

                    capturedImage.setImageBitmap(bitmap);
                    camera.setVisibility(View.INVISIBLE);

                    File imagePaths=bitmapToFile(bitmap);
                    if(imagePaths!=null){
                        impath=imagePaths.getAbsolutePath();
                    }

                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private File bitmapToFile(Bitmap bitmap) {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagePaths = null;
        try {
            String imageFileName = "Jpeg" + System.currentTimeMillis() + ".jpg";

            imagePaths = new File(storageDir, imageFileName);

            FileOutputStream fos = new FileOutputStream(imagePaths);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error creating image file", e);
        }
        return new File(imagePaths.getAbsolutePath());
    }

    public void AddsubCategory() {
        File file = new File(impath);
        RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), "register_subcategory");
        RequestBody subcatName = RequestBody.create(MediaType.parse("text/plain"), text.getText().toString());
        RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"), preferenceManager.getCategory_id());
        RequestBody vendorId = RequestBody.create(MediaType.parse("text/plain"), preferenceManager.getVendorId());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"),file));


        Log.d("TAG", String.valueOf(imagePart));
        restCall.addSubcategory(tag,subcatName ,categoryId, vendorId, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddSubCat_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        String err=e.getLocalizedMessage();
                        last.setText(err);

                    }

                    @Override
                    public void onNext(AddSubCat_Response addSubCatResponse) {
                        if (addSubCatResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {

                            finish();
                        } else {
                            Toast.makeText(AddsubCat.this, addSubCatResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void GetCat(String vendorId) {
        restCall.getCategories("get_categories", vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Category_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddsubCat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Category_Response vCategoryResponse) {
                        if (vCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            category_list=vCategoryResponse.getCategories();
                            List<V_Category_Response.Category> categories = vCategoryResponse.getCategories();

                            List<String> categoryNames = new ArrayList<>();
                            for (V_Category_Response.Category category : categories) {
                                categoryNames.add(category.getCategoryName());
                            }

                            spinnerAdapter = new ArrayAdapter<>(AddsubCat.this, android.R.layout.simple_spinner_item, categoryNames);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerAdapter);
                        }
                    }
                });
    }
    private String getCategoryID(String categoryName) {
        if (vCategoryResponse != null) {
            List<V_Category_Response.Category> categories = vCategoryResponse.getCategories();
            if (categories != null) {
                for (V_Category_Response.Category category : categories) {
                    if (category.getCategoryName().equals(categoryName)) {
                        return category.getCategoryId();
                    }
                }
            } else {
                Log.e("AddsubCat", "Categories list in vCategoryResponse is null");
            }
        } else {
            Log.e("AddsubCat", "vCategoryResponse is null");
        }
        return null;
    }

    private void EditSubCat(){
        restCall.Editsubcat("edit_subcategory",GetId,text.getText().toString())
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditsubCat_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddsubCat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(EditsubCat_Response editsubCatResponse) {
                        if (editsubCatResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            text.setText("");
                            spinner.setVisibility(View.INVISIBLE);
                            finish();
                        }
                        else {
                            Toast.makeText(AddsubCat.this, editsubCatResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}