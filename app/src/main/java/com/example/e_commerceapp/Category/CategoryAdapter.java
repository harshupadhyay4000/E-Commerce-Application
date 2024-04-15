package com.example.e_commerceapp.Category;

import android.annotation.SuppressLint;
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

import com.example.e_commerceapp.NetworkResponse.Vendor_Response;
import com.example.e_commerceapp.Pages.User.Products_Arena;
import com.example.e_commerceapp.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.View_new_Holder>{
    LayoutInflater layoutInflater;
    Context context;
    List<Vendor_Response.Vendor> Vendor;
    public CategoryAdapter(Context context, List<Vendor_Response.Vendor> Vendor) {
        this.context = context;
        this.Vendor = Vendor;
    }
    @NonNull
    @Override
    public View_new_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view= layoutInflater.inflate(R.layout.home_one,parent,false);
        return new View_new_Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull View_new_Holder holder, int position) {
        Vendor_Response.Vendor vendor = Vendor.get(position);
        holder.vendorName.setText(Vendor.get(position).getVendorName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vendorId = Vendor.get(position).getVendorId();


                Intent intent = new Intent(context, Products_Arena.class);
                intent.putExtra("vendor_id", vendorId);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return Vendor.size();
    }
    public static class View_new_Holder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView vendorImage;
        TextView vendorName;
        @SuppressLint("WrongViewCast")
        public View_new_Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.vendor_card);
            vendorImage = itemView.findViewById(R.id.vendor_image);
            vendorName = itemView.findViewById(R.id.vendor_name);
        }
    }
}
