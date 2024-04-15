package com.example.e_commerceapp.Pages.Vendor.V_Home_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.NetworkResponse.V_Product_Response;
import com.example.e_commerceapp.NetworkResponse.V_Sub_Category_Response;
import com.example.e_commerceapp.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.View_Prod_Holder>{
    LayoutInflater layoutInflater;
    Context context;
    List<V_Product_Response.Product>Product;
    List<V_Product_Response.Product> filteredProducts;
    EditInterface editInterface;

    public interface EditInterface{
        void EditClick(V_Product_Response.Product Product);
        void DeleteClick(V_Product_Response.Product Product);
    }

    public void setEdit(EditInterface editInterface){this.editInterface=editInterface;}

    public ProductAdapter(Context context, List<V_Product_Response.Product>Product){
        this.context=context;
        this.Product=Product;
        this.filteredProducts=new ArrayList<>(Product);
    }
    public void filterList(List<V_Product_Response.Product> filteredProducts) {
        this.filteredProducts = filteredProducts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View_Prod_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_adapter,parent,false);
        return new View_Prod_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Prod_Holder holder, int position) {
        V_Product_Response.Product currentProduct= filteredProducts.get(position);
        holder.textView.setText(currentProduct.getProductName());
        holder.name.setText("₹ "+Product.get(position).getProductPrice());
        Glide.with(context)
                .load(Product.get(position).getImagePath())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.camera_icon)
                .into(holder.imageView);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInterface != null){
                    editInterface.EditClick(Product.get(position));
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInterface != null){
                    editInterface.DeleteClick(Product.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }

    public static class View_Prod_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView, name;
        Button edit, delete;
        public View_Prod_Holder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.product_v_one_card);
            imageView=itemView.findViewById(R.id.product_v_one_image);
            textView=itemView.findViewById(R.id.product_v_one_price);
            name=itemView.findViewById(R.id.product_v_one_name);
            edit=itemView.findViewById(R.id.productedit_button);
            delete=itemView.findViewById(R.id.productdelete_button);


        }
    }
}
