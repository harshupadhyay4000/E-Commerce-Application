package com.example.e_commerceapp.Pages.Vendor.AddCategory;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.AddCategory_Response;
import com.example.e_commerceapp.NetworkResponse.EditCategory_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCategory extends AppCompatActivity {
    EditText txt;
    Button addbtn;
    PreferenceManager preferenceManager;
    RestCall restCall;
    Boolean IsEdit=false;
    String GetId;
    ImageButton camera;
    ImageView capturedImage;
    int TAKE_PICTURE = 1;
    String impath;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        txt=findViewById(R.id.edt_txt);
        addbtn=findViewById(R.id.addbutton);
        preferenceManager=new PreferenceManager(this);
        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL, VariableBag.API_KEY);
        Intent intent=getIntent();
        if (intent!=null && intent.getStringExtra("message")!=null && intent.getStringExtra("id")!=null){
            IsEdit=true;
            txt.setText(intent.getStringExtra("message"));
            addbtn.setText("edit");
            GetId=intent.getStringExtra("id");
        }
        else {
            IsEdit=false;
            addbtn.setText("Add");
        }
        camera=findViewById(R.id.camera_icon_category);
        capturedImage=findViewById(R.id.captured_image_category);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, TAKE_PICTURE);
                }
            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txt.getText().toString().trim().equals("")){
                    txt.setError("Enter Details");
                    txt.requestFocus();
                }
                else {
                        if (IsEdit){
                            EditingCategory();
                        }
                        else {
                            AddingCategory();
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

    private void AddingCategory(){
        File file = new File(impath);

        RequestBody tag = RequestBody.create(MediaType.parse("text/plain"), "register_subcategory");
        RequestBody catName = RequestBody.create(MediaType.parse("text/plain"), txt.getText().toString());
        RequestBody vendorId = RequestBody.create(MediaType.parse("text/plain"), preferenceManager.getVendorId());
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"),file));

        restCall.addCategories(tag,catName,vendorId, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddCategory_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddCategory.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(AddCategory_Response addCategoryResponse) {
                            if (addCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                                txt.setText("");
                                finish();
                            }
                            else {
                                Toast.makeText(AddCategory.this, addCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                    }
                });
    }

    private void EditingCategory(){
        restCall.editCategories("edit_category",GetId,txt.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EditCategory_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(AddCategory.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(EditCategory_Response editCategoryResponse) {
                            if (editCategoryResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){
                                txt.setText("");
                                finish();
                            }
                            else {
                                Toast.makeText(AddCategory.this, editCategoryResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                    }
                });
    }


}