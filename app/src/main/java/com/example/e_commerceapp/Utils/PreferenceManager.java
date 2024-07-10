package com.example.e_commerceapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String Category_id;

    String Subcategory_id;

    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_CUST_ID = "cust_id";

    public static final String PREF_NAME = "com.example.e_commerceapp.PREF";
    public static final String IS_LOGGED_IN_KEY = "is_logged_in";

    public String getCustId(){
        return sharedPreferences.getString(VariableBag.USER_ID,null);
    }
    public void setCustId(String key,String value){
        editor.putString(VariableBag.USER_ID, value);
        editor.apply();
    }


    public String getCustomerId() {
        return sharedPreferences.getString(VariableBag.User_id, null);
    }

    public void setCustomerId(String key,String value) {
        editor.putString(VariableBag.User_id, value);
        editor.apply();
    }

    public String getSubcategory_id() {
        return Subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        Subcategory_id = subcategory_id;
    }

    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME,Context.MODE_PRIVATE);
        this.editor= sharedPreferences.edit();

    }

    private static final String KEY_VENDOR_ID = "vendor_id";
    public String getVendorId() {
        return sharedPreferences.getString(KEY_VENDOR_ID, null);
    }

    public void setVendorId(String vendorId) {
        editor.putString(KEY_VENDOR_ID, vendorId).apply();
    }

    public PreferenceManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public SharedPreferences.Editor getEditor() {
        return editor;
    }

    public void setEditor(SharedPreferences.Editor editor) {
        this.editor = editor;
    }

    public String getCategory_id() {

        return sharedPreferences.getString(Category_id,"");
    }

    public void setCategory_id(String category_id) {
        editor.putString(Category_id,category_id);
        editor.apply();
    }
    public void setLoggedIn(boolean isLoggedIn) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(IS_LOGGED_IN_KEY, isLoggedIn);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(IS_LOGGED_IN_KEY, false);
    }

    public void clearLoginStatus() {
        setLoggedIn(false);
    }


}
