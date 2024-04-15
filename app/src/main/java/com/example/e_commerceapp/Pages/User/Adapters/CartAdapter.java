package com.example.e_commerceapp.Pages.User.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.NetworkResponse.Get_Cart_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.View_cart_Holder> {
    private Context context;
    private List<Get_Cart_Response.CartItem> Cartitem;

    PreferenceManager preferenceManager;

    public CartAdapter(Context context, List<Get_Cart_Response.CartItem> cartItems) {
        this.context = context;
        this.Cartitem = cartItems;
        this.preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public View_cart_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
        return new View_cart_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_cart_Holder holder, int position) {
        holder.cardView.setOnClickListener(v -> Toast.makeText(context, "", Toast.LENGTH_SHORT).show());

        holder.quantity.setText(Cartitem.get(position).getQuantity());
        holder.productName.setText(Cartitem.get(position).getProductName());
        holder.productPrice.setText("₹"+Cartitem.get(position).getProductPrice());
        Glide.with(context)
                .load(Cartitem.get(position).getImageUrl())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.camera_icon)
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return Cartitem.size();
    }

    public static class View_cart_Holder extends RecyclerView.ViewHolder {
        TextView quantity, productName, productPrice;
        CardView cardView;
        ImageView imageView;


        public View_cart_Holder(@NonNull View itemView) {
            super(itemView);

            quantity = itemView.findViewById(R.id.quantity_text);
            productName = itemView.findViewById(R.id.product_cart_name);
            productPrice = itemView.findViewById(R.id.product_cart_price);
            cardView = itemView.findViewById(R.id.cart_card);
            imageView=itemView.findViewById(R.id.product_cart_u_image);
        }
    }


}
