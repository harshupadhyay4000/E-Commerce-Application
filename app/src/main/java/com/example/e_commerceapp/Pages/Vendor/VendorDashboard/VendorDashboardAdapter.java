package com.example.e_commerceapp.Pages.Vendor.VendorDashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.Confirmation_Response;
import com.example.e_commerceapp.NetworkResponse.Getorder_Response;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VendorDashboardAdapter extends RecyclerView.Adapter<VendorDashboardAdapter.View_VDash_Holder>{

    Context context;

    //List<Getorder_Response.CartItem>cartItems;
    List<String> cartno=new ArrayList<>();
    LayoutInflater layoutInflater;
    String totalQuantity;
    String totalAmount;
    PreferenceManager preferenceManager;
    List<Getorder_Response.CartItem>cartItems;
    RestCall restCall;

    public VendorDashboardAdapter(Context context, List<Getorder_Response.CartItem> cartItems, String totalQuantity, String totalAmount) {
        this.context = context;
        this.cartItems = cartItems;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.preferenceManager = new PreferenceManager(context);

    }

    @NonNull
    @Override
    public View_VDash_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.v_dash,parent,false);
        return new View_VDash_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_VDash_Holder holder, int position) {

        if (!cartno.contains(cartItems.get(position).getOrderNumber())){
            cartno.add(cartItems.get(position).getOrderNumber());
            holder.cartno.setText("ORD"+ cartItems.get(position).getOrderNumber());
            holder.date.setText(cartItems.get(position).getAddedDate());
            holder.amount.setText(totalAmount);

        }
        else {
            holder.cardView.setVisibility(View.INVISIBLE);
        }

        holder.Viewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, VendorDashboardActivity.class);
                intent.putExtra("order_id",cartItems.get(position).getOrderNumber());
                intent.putExtra("myKey",(Serializable) cartItems);
                context.startActivity(intent);
            }
        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.decline.setVisibility(View.INVISIBLE);
                holder.accept.setVisibility(View.INVISIBLE);

                confirmation(cartItems.get(position).getOrderNumber(),"1");
            }
        });
        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.accept.setVisibility(View.INVISIBLE);
                holder.decline.setVisibility(View.INVISIBLE);

                confirmation(cartItems.get(position).getOrderNumber(),"2");
            }
        });
        String confirmationStatus = cartItems.get(position).getConfirmationStatus();
        if (confirmationStatus.equals("")) {
            holder.status.setText("Pending");

        } else if (confirmationStatus.equals("1")) {
            holder.status.setText("Accepted");
            holder.accept.setVisibility(View.INVISIBLE);
            holder.decline.setVisibility(View.INVISIBLE);

        } else {
            holder.status.setText("Declined");
            holder.accept.setVisibility(View.INVISIBLE);
            holder.decline.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class View_VDash_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView cartno,date,amount, status;
        Button Viewbtn;
        ImageButton accept, decline;
        public View_VDash_Holder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.order_vendor_card);
            cartno=itemView.findViewById(R.id.cart_vendor_dash_number);

            amount=itemView.findViewById(R.id.vednor_dash_amount);
            date=itemView.findViewById(R.id.vendor_dash_date);
            Viewbtn=itemView.findViewById(R.id.viewmorebtn_v_dash);
            status=itemView.findViewById(R.id.vednor_dash_status);
            accept=itemView.findViewById(R.id.accept_btn);
            decline=itemView.findViewById(R.id.decline_btn);
        }
    }


    private void confirmation(String ordernumber, String status){


        restCall= RestClient.createService(RestCall.class, VariableBag.BASE_URL,VariableBag.API_KEY);
        restCall.getConfirmation("add_confirmation",ordernumber,status,preferenceManager.getVendorId(),preferenceManager.getCustomerId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Confirmation_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Confirmation_Response confirmationResponse) {
                        if (confirmationResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){

                        }
                        else {

                            Toast.makeText(context, confirmationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
