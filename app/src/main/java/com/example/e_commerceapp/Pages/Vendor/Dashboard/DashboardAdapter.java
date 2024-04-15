package com.example.e_commerceapp.Pages.Vendor.Dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.NetworkResponse.Getorder_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.View_order_Holder>{

    Context context;
    //List<Getorder_Response.CartItem>cartItems;
    List<String> cartno=new ArrayList<>();
    LayoutInflater layoutInflater;
    String totalQuantity;
    String totalAmount;
    PreferenceManager preferenceManager;
    List<Getorder_Response.CartItem>cartItems;


    public DashboardAdapter(Context context, List<Getorder_Response.CartItem> cartItems, String totalQuantity, String totalAmount) {
        this.context = context;
        this.cartItems = cartItems;

        this.preferenceManager = new PreferenceManager(context);
    }

    @NonNull
    @Override
    public View_order_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.orders,parent,false);
        return new View_order_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_order_Holder holder, int position) {

        if (!cartno.contains(cartItems.get(position).getOrderNumber())){
            cartno.add(cartItems.get(position).getOrderNumber());
            holder.cartno.setText("ORD"+ cartItems.get(position).getOrderNumber());
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
        else {
            holder.cardView.setVisibility(View.INVISIBLE);
        }



        holder.Viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DashboardActivity.class);
                intent.putExtra("order_id",cartItems.get(position).getOrderNumber());
                intent.putExtra("myKey",(Serializable) cartItems);
                 context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class View_order_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView cartno,date, status;
        Button Viewbtn;
        public View_order_Holder(@NonNull View itemView) {
            super(itemView);
            cartno=itemView.findViewById(R.id.cart_number);
            date=itemView.findViewById(R.id.order_date);

            Viewbtn=itemView.findViewById(R.id.viewmorebtn);
            cardView=itemView.findViewById(R.id.order_card);
            status=itemView.findViewById(R.id.order_status_user);
        }
    }

}

