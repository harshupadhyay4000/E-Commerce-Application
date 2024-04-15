package com.example.e_commerceapp.Network;

import com.example.e_commerceapp.NetworkResponse.AddCart_Response;
import com.example.e_commerceapp.NetworkResponse.AddCategory_Response;
import com.example.e_commerceapp.NetworkResponse.AddOrder_Response;
import com.example.e_commerceapp.NetworkResponse.AddProduct_Response;
import com.example.e_commerceapp.NetworkResponse.AddSubCat_Response;
import com.example.e_commerceapp.NetworkResponse.AddUser_Response;
import com.example.e_commerceapp.NetworkResponse.Cart_item_Response;
import com.example.e_commerceapp.NetworkResponse.Confirmation_Response;
import com.example.e_commerceapp.NetworkResponse.DeleteCategory_Response;
import com.example.e_commerceapp.NetworkResponse.DeleteProduct_Response;
import com.example.e_commerceapp.NetworkResponse.Delete_SubCat_Response;
import com.example.e_commerceapp.NetworkResponse.EditCategory_Response;
import com.example.e_commerceapp.NetworkResponse.Edit_Product_Response;
import com.example.e_commerceapp.NetworkResponse.EditsubCat_Response;
import com.example.e_commerceapp.NetworkResponse.Get_Cart_Response;
import com.example.e_commerceapp.NetworkResponse.Getorder_Response;
import com.example.e_commerceapp.NetworkResponse.Login_Response;
import com.example.e_commerceapp.NetworkResponse.MasterApi_Response;
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.NetworkResponse.V_Product_Response;
import com.example.e_commerceapp.NetworkResponse.V_Sub_Category_Response;
import com.example.e_commerceapp.NetworkResponse.Vendor_Response;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Single;

public interface RestCall {

    @FormUrlEncoded
    @POST("customerController.php")
    Single<AddUser_Response> AddUser(
        @Field("tag") String tag,
        @Field("customer_name") String customer_name,
        @Field("customer_mobile") String customer_mobile,
        @Field("customer_email") String customer_email,
        @Field("customer_password") String customer_password
    );


 @FormUrlEncoded
 @POST("customerController.php")
 Single<Login_Response> UserLogin(
         @Field("tag") String tag,
         @Field("customer_email") String customer_email,
         @Field("customer_password") String customer_password
 );
    @FormUrlEncoded
    @POST("vendorController.php")
    Single<AddUser_Response> AddVendor(
            @Field("tag") String tag,
            @Field("vendor_name") String vendor_name,
            @Field("vendor_mobile") String vendor_mobile,
            @Field("vendor_email") String vendor_email,
            @Field("vendor_password") String vendor_password
    );

    @FormUrlEncoded
    @POST("vendorController.php")
    Single<Login_Response> LoginVendor(
            @Field("tag") String tag,
            @Field("vendor_email") String vendor_email,
            @Field("vendor_password") String vendor_password
    );


    @FormUrlEncoded
    @POST("vendorController.php")
    Single<Vendor_Response> getVendorNames(
            @Field("tag") String tag
    );

    @FormUrlEncoded
    @POST("categoryController.php")
    Single<V_Category_Response> getCategories(
            @Field("tag") String tag,
            @Field("vendor_id") String vendorId
    );

    @FormUrlEncoded
    @POST("categoryController.php")
    Single<V_Category_Response> getspinnerCategories(
            @Field("tag") String tag,

            @Field("vendor_id") String vendorId
    );

    @Multipart
    @POST("categoryController.php")
    Single<AddCategory_Response> addCategories(
            @Part("tag") RequestBody tag,
            @Part("category_name") RequestBody category_name,
            @Part("vendor_id") RequestBody vendor_id,
            @Part MultipartBody.Part image_path
    );

    @FormUrlEncoded
    @POST("categoryController.php")
    Single<EditCategory_Response> editCategories(
      @Field("tag") String tag,
      @Field("category_id") String category_id,
      @Field("category_name") String category_name
    );

    @FormUrlEncoded
    @POST("categoryController.php")
    Single<DeleteCategory_Response> deleteCategories(
      @Field("tag") String tag,
      @Field("category_id") String category_id
    );

    @FormUrlEncoded
    @POST("subcatController.php")
    Single<V_Sub_Category_Response> getSubcategories(
      @Field("tag") String tag,
      @Field("category_id") String category_id,
      @Field("vendor_id") String vendor_id
    );

    @Multipart
    @POST("subcatController.php")
    Single<AddSubCat_Response> addSubcategory(
            @Part("tag") RequestBody tag,
            @Part("subcategory_name") RequestBody subcategory_name,
            @Part("category_id") RequestBody category_id,
            @Part("vendor_id") RequestBody vendor_id,
            @Part MultipartBody.Part image_path
    );

    @FormUrlEncoded
   @POST("subcatController.php")
   Single<Delete_SubCat_Response> deleteSubcat(
      @Field("tag") String tag,
      @Field("subcategory_id") String subcategory_id
    );

    @FormUrlEncoded
    @POST("subcatController.php")
    Single<EditsubCat_Response>Editsubcat(
            @Field("tag") String tag,
            @Field("subcategory_id") String subcategory_id,
            @Field("subcategory_name") String subcategory_name
    );

    @FormUrlEncoded
    @POST("productController.php")
    Single<V_Product_Response>Getproducts(
            @Field("tag") String tag,
            @Field("vendor_id") String vendor_id,
            @Field("category_id") String category_id,
            @Field("subcategory_id") String subcategory_id
    );

    @FormUrlEncoded
    @POST("productController.php")
    Single<Edit_Product_Response>Editprodutcts(
            @Field("tag") String tag,
            @Field("product_id") String product_id,
            @Field("product_price") String product_price,
            @Field("product_name") String product_name
    );

    @Multipart
    @POST("productController.php")
    Single<AddProduct_Response> addProducts(
            @Part("tag") RequestBody tag,
            @Part("product_name") RequestBody product_name,
            @Part("subcategory_id") RequestBody subcategory_id,
            @Part("category_id") RequestBody category_id,
            @Part("vendor_id") RequestBody vendor_id,
            @Part("product_price") RequestBody product_price,
            @Part MultipartBody.Part image_path
    );

    @FormUrlEncoded
    @POST("productController.php")
    Single<DeleteProduct_Response>deleteProducts(
            @Field("tag") String tag,
            @Field("product_id") String product_id
    );

    @FormUrlEncoded
    @POST("cartController.php")
    Single<Get_Cart_Response>Getcart(
      @Field("tag") String tag,
      @Field("customer_id") String customer_id
    );

    @FormUrlEncoded
    @POST("cartController.php")
    Single<AddCart_Response>Addcart(
      @Field("tag") String tag,
      @Field("product_id") String product_id,
      @Field("product_name") String product_name,
      @Field("product_price") String product_price,
      @Field("vendor_id") String vendor_id,
      @Field("customer_id") String customer_id,
      @Field("quantity") String quantity
    );

    @FormUrlEncoded
    @POST("script.php")
    Single<MasterApi_Response>Getdata(
      @Field("tag") String tag,
      @Field("vendor_id") String vendor_id
    );

    @FormUrlEncoded
    @POST("cartitemController.php")
    Single<Cart_item_Response>itemCart(
      @Field("add_cart") String add_cart,
      @Field("customer_id") String customer_id,
      @Field("vendor_id") String vendor_id
    );

    @FormUrlEncoded
    @POST("cartController.php")
    Single<AddOrder_Response>addOrder(
            @Field("tag") String tag,
            @Field("customer_id") String customer_id
    );

    @FormUrlEncoded
    @POST("cartController.php")
    Single<Getorder_Response>getOrder(
            @Field("tag") String tag,
            @Field("vendor_id") String vendor_id
    );



    @FormUrlEncoded
    @POST("cartcontroller.php")
    Single<Confirmation_Response>getConfirmation(
            @Field("tag") String tag,
            @Field("order_number") String order_number,
            @Field("confirmation_status") String confirmation_status,
            @Field("vendor_id") String vendor_id,
            @Field("customer_id") String customer_id
    );


}
