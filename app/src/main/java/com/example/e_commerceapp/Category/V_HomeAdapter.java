package com.example.e_commerceapp.Category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Pages.Vendor.AddNewCat.SubCategory_Vendor;
import com.example.e_commerceapp.Pages.Vendor.Category_Vendor;
import com.example.e_commerceapp.Pages.Vendor.Products_Vendor;
import com.example.e_commerceapp.R;

import java.util.List;

public class V_HomeAdapter extends RecyclerView.Adapter<V_HomeAdapter.View_vnew_Holder>{
    LayoutInflater layoutInflater;
    Context context;

    private List<Category> categories;

    public V_HomeAdapter(Context context, List<Category> categories){
        this.context=context;
        this.categories = categories;
    }


    @NonNull
    @Override
    public View_vnew_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.v_home,parent,false);
        return new View_vnew_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_vnew_Holder holder, int position) {
        Category category= categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryImage.setImageResource(category.getImageResource());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (category.getName()){
                    case "Categories":
                        CategoryActivity();
                        break;
                    case "Sub Categories":
                        SubCategoryActivity();
                        break;
                    case "Products":
                        ProductsActivity();
                        break;
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public static class View_vnew_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView categoryImage;
        TextView categoryName;
        public View_vnew_Holder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.vendor_home_card);
            categoryImage=itemView.findViewById(R.id.vendor_home_image);
            categoryName=itemView.findViewById(R.id.vendor_home_name);

        }
    }

    private void CategoryActivity(){
        if (context != null) {

            Intent intent= new Intent(context, Category_Vendor.class);
            context.startActivity(intent);
        }
    }
    private void SubCategoryActivity(){
        if (context != null) {
            Intent intent= new Intent(context, SubCategory_Vendor.class);
            context.startActivity(intent);
        }
    }
    private void ProductsActivity(){
        if (context != null) {
            Intent intent= new Intent(context, Products_Vendor.class);
            context.startActivity(intent);
        }
    }

}
