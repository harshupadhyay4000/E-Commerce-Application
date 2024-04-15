package com.example.e_commerceapp.Pages.Vendor.VendorDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.NetworkResponse.Getorder_Response;
import com.example.e_commerceapp.R;

import java.util.List;

public class VendorActDashboardAdapter extends RecyclerView.Adapter<VendorActDashboardAdapter.View_vdash_Holder>{
    Context context;
    List<Getorder_Response.CartItem>cartItems;
    LayoutInflater layoutInflater;

    public VendorActDashboardAdapter(Context context, List<Getorder_Response.CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public View_vdash_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.v_dash_details,parent,false);
        return new View_vdash_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_vdash_Holder holder, int position) {

        holder.ordernumber.setText("ORD"+cartItems.get(position).getOrderNumber());
        holder.productname.setText(cartItems.get(position).getProductName());
        holder.productprice.setText(cartItems.get(position).getProductPrice());
        holder.quantity.setText(cartItems.get(position).getQuantity());
        holder.date.setText(cartItems.get(position).getAddedDate());
        String confirmationStatus = cartItems.get(position).getConfirmationStatus();
        if (confirmationStatus.equals("0")) {
            holder.status.setText("Pending");
        }
        else if (confirmationStatus.equals("1")) {
            holder.status.setText("Accepted");
        }
        else if (confirmationStatus.equals("2")) {
            holder.status.setText("Declined");
        }
        else {
            holder.status.setText("Pending");
        }


    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class View_vdash_Holder extends RecyclerView.ViewHolder {
        TextView ordernumber,productname, productprice, quantity, date, status;
        ImageButton accept, decline;
        public View_vdash_Holder(@NonNull View itemView) {
            super(itemView);

            ordernumber=itemView.findViewById(R.id.order_number_vendor_dashboard);
            productname=itemView.findViewById(R.id.product_name_vendor_dashboard);
            productprice=itemView.findViewById(R.id.product_price_vendor_dashboard);
            quantity=itemView.findViewById(R.id.product_quantity_vendor_dashboard);
            date=itemView.findViewById(R.id.Date_vendor_dashboard);
            status=itemView.findViewById(R.id.status_vendor_dashboard);

        }
    }
}
