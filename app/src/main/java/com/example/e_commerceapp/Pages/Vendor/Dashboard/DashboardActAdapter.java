package com.example.e_commerceapp.Pages.Vendor.Dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.NetworkResponse.Getorder_Response;
import com.example.e_commerceapp.R;

import java.util.List;

public class DashboardActAdapter extends RecyclerView.Adapter<DashboardActAdapter.View_Dash_Holder>{

    Context context;
    List<Getorder_Response.CartItem>cartItems;
    LayoutInflater layoutInflater;



    public DashboardActAdapter(Context context, List<Getorder_Response.CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public View_Dash_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.dashboard,parent,false);
        return new View_Dash_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Dash_Holder holder, int position) {
    holder.ordernumber.setText("ORD"+cartItems.get(position).getOrderNumber());
    holder.productname.setText(cartItems.get(position).getProductName());
    holder.productprice.setText(cartItems.get(position).getProductPrice());
    holder.quantity.setText(cartItems.get(position).getQuantity());
    holder.date.setText(cartItems.get(position).getAddedDate());
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class View_Dash_Holder extends RecyclerView.ViewHolder {
        TextView ordernumber,productname, productprice, quantity, date;

        public View_Dash_Holder(@NonNull View itemView) {
            super(itemView);
            ordernumber=itemView.findViewById(R.id.order_number_dashboard);
            productname=itemView.findViewById(R.id.product_name_dashboard);
            productprice=itemView.findViewById(R.id.product_price_dashboard);
            quantity=itemView.findViewById(R.id.product_quantity_dashboard);
            date=itemView.findViewById(R.id.Date_dashboard);
        }
    }
}
