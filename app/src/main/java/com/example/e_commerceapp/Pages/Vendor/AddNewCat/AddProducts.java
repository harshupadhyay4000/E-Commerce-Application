package com.example.e_commerceapp.Pages.Vendor.AddNewCat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.AddProduct_Response;
import com.example.e_commerceapp.NetworkResponse.Edit_Product_Response;
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.NetworkResponse.V_Sub_Category_Response;
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

public class AddProducts extends AppCompatActivity {

    Spinner spinnerone, spinnertwo;
    EditText text, price;
    Button add;
    RestCall restCall;
    PreferenceManager preferenceManager;
    V_Sub_Category_Response vSubCategoryResponse;
    V_Category_Response vCategoryResponse;
    ArrayAdapter<String> spinnerAdapter;
    ArrayAdapter<String> spinnerAdaptertwo;
    String impath;
    List<String> categoryNames;
    List<String> subcategoryNames;
    int TAKE_PICTURE = 1;
    ImageButton camera;
    ImageView capturedImage;
    List<V_Sub_Category_Response.Subcategory> subcategory_list;
    String GetId;
    Boolean IsEdit=false;

    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        spinnerone=findViewById(R.id.spinnerone_add_products);
        spinnertwo=findViewById(R.id.spinnertwo_add_products);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        preferenceManager=new PreferenceManager(this);
        add=findViewById(R.id.product_addbutton_add);
        text=findViewById(R.id.addproduct_edt_txt);
        price=findViewById(R.id.addproductprice_edt_txt);
        getCategories();
        spinnerone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String categoryId= getCategoryName(parent.getItemAtPosition(position).toString());
                preferenceManager.setCategory_id(categoryId);
                getSubCategory(categoryId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnertwo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedsubcategory= parent.getItemAtPosition(position).toString();
                String subcategoryId=getSubcategoryName(parent.getItemAtPosition(position).toString());
                preferenceManager.setSubcategory_id(subcategoryId);
                if(subcategory_list!=null){
                    for (V_Sub_Category_Response.Subcategory items:subcategory_list){
                        if (selectedsubcategory.equals(items.getSubcategoryName())){
                            preferenceManager.setSubcategory_id(items.getSubcategoryId());
                        }

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Intent intent=getIntent();
        if (intent!=null&& intent.getStringExtra("message")!=null&&intent.getStringExtra("id")!=null){
            IsEdit=true;
            text.setText(intent.getStringExtra("message"));
            add.setText("Edit");
            GetId=intent.getStringExtra("id");
        }
        else {
            IsEdit=false;
            add.setText("Add");
        }
        camera=findViewById(R.id.camera_icon);
        capturedImage=findViewById(R.id.captured_image);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedsubcategory=spinnertwo.getSelectedItem().toString();
                String enteredText = text.getText().toString();
                if (text.getText().toString().trim().equals("")){
                    text.setError("Enter Product");
                    text.requestFocus();
                }
                else {
                    if (IsEdit){
                        EditProducts();
                    }
                    else {
                        // Update the method call to use multipart format
//                        Bitmap bitmap = null; // You need to get the bitmap from somewhere
                        Addproduct();
                    }
                }
            }
        });
    }

    private void getCategories(){
        String vendorId= preferenceManager.getVendorId();
        restCall.getCategories("get_categories",vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Category_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddProducts.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Category_Response vCategoryResponse) {
                        if (vCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            AddProducts.this.vCategoryResponse=vCategoryResponse;
                            categoryNames=new ArrayList<>();
                            for (V_Category_Response.Category category:vCategoryResponse.getCategories()){
                                categoryNames.add(category.getCategoryName());
                            }
                            spinnerAdapter=new ArrayAdapter<>(AddProducts.this, android.R.layout.simple_spinner_item,categoryNames);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerone.setAdapter(spinnerAdapter);
                        }
                        else {
                            Toast.makeText(AddProducts.this, vCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
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

    private String getCategoryName(String categoryName){

        if (vCategoryResponse!=null){
            List<V_Category_Response.Category> categories=vCategoryResponse.getCategories();
            if (categories!=null){
                for (V_Category_Response.Category category:categories){
                    if (category.getCategoryName().equals(categoryName)){
                        return category.getCategoryId();
                    }
                }
            }  else{
                Log.e("AddProducts","Categories list in vCategoryResponse is null");
            }
        } else {
            Log.e("AddProducts","vCategoryResponse is null");
        }

        return null;
    }

    private void getSubCategory(String categoryId){
        String vendorId=preferenceManager.getVendorId();
        restCall.getSubcategories("get_subcategories",categoryId,vendorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<V_Sub_Category_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddProducts.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(V_Sub_Category_Response vSubCategoryResponse) {
                        if (vSubCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            AddProducts.this.vSubCategoryResponse=vSubCategoryResponse;
                            subcategoryNames=new ArrayList<>();
                            for (V_Sub_Category_Response.Subcategory subcategory:vSubCategoryResponse.getSubcategories()){
                                subcategoryNames.add(subcategory.getSubcategoryName());
                            }
                            spinnerAdaptertwo= new ArrayAdapter<>(AddProducts.this, android.R.layout.simple_spinner_item, subcategoryNames);
                            spinnerAdaptertwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnertwo.setAdapter(spinnerAdaptertwo);
                        }
                        else {
                            Toast.makeText(AddProducts.this, vSubCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getSubcategoryName(String subCatName){
        if (vSubCategoryResponse!=null){
            List<V_Sub_Category_Response.Subcategory> subcategories=vSubCategoryResponse.getSubcategories();
            if (subcategories != null){
                for (V_Sub_Category_Response.Subcategory subcategory:subcategories){
                    if (subcategory.getSubcategoryName().equals(subCatName)){
                        return  subcategory.getSubcategoryId();
                    }
                }
            } else {
                Log.e("AddProducts","SubCategory List in vSubcategoryResponse is null");
            }
        } else {
            Log.e("AddProducts","vSubCategoryResponse is null");
        }

        return  null;
    }


    public void Addproduct() {
        File file = new File(impath);

        RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), "register_product");
        RequestBody productName = RequestBody.create(MediaType.parse("text/plain"), text.getText().toString());
        RequestBody subcategoryId = RequestBody.create(MediaType.parse("text/plain"), preferenceManager.getSubcategory_id());
        RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"), preferenceManager.getCategory_id());
        RequestBody vendorId = RequestBody.create(MediaType.parse("text/plain"), preferenceManager.getVendorId());
        RequestBody productPrice = RequestBody.create(MediaType.parse("text/plain"), price.getText().toString());

//        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);


        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"),file));


        Log.d("TAG", String.valueOf(imagePart));


        restCall.addProducts(tag, productName, subcategoryId, categoryId, vendorId, productPrice, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddProduct_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddProducts.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddProduct_Response addProductResponse) {
                        if (addProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)) {
                            finish();
                        } else {
                            Toast.makeText(AddProducts.this, addProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private void EditProducts(){
        restCall.Editprodutcts("edit_product",GetId,price.getText().toString(),text.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Edit_Product_Response>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddProducts.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Edit_Product_Response editProductResponse) {
                        if (editProductResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                            spinnerone.setVisibility(View.INVISIBLE);
                            spinnertwo.setVisibility(View.INVISIBLE);
                            text.setText("");
                            finish();
                        }
                        else {
                            Toast.makeText(AddProducts.this, editProductResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}