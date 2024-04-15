package com.example.e_commerceapp.Pages.User.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.Network.RestCall;
import com.example.e_commerceapp.Network.RestClient;
import com.example.e_commerceapp.NetworkResponse.AddCart_Response;
import com.example.e_commerceapp.NetworkResponse.Get_Cart_Response;
import com.example.e_commerceapp.NetworkResponse.MasterApi_Response;
import com.example.e_commerceapp.Pages.User.Add_to_Cart;
import com.example.e_commerceapp.R;
import com.example.e_commerceapp.Utils.PreferenceManager;
import com.example.e_commerceapp.Utils.VariableBag;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserproductAdapter extends RecyclerView.Adapter<UserproductAdapter.View_prod_Holder> {

    Context context;
    List<MasterApi_Response.Product> Product;
    LayoutInflater layoutInflater;
    RestCall restCall;
    PreferenceManager preferenceManager;
    List<Get_Cart_Response.CartItem> Cartitem;
    ImageView itemconfirm ;

    public UserproductAdapter(Context context, List<MasterApi_Response.Product> product,  ImageView itemconfirm) {
        this.context = context;
        Product = product;
        this.preferenceManager = new PreferenceManager(context);
        this.itemconfirm = itemconfirm;
    }

    @NonNull
    @Override
    public View_prod_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater=LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.product_u_adapter,parent,false);
        return new View_prod_Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull View_prod_Holder holder, int position) {
        Getcart();
        MasterApi_Response.Product product = Product.get(position);


        holder.name.setText(Product.get(position).getProductName());
        holder.price.setText("₹" + Product.get(position).getProductPrice());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Glide.with(context)
                .load(Product.get(position).getImageUrl())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.camera_icon)
                .into(holder.imageView);
        holder.cart.setVisibility(View.GONE);
        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Add_to_Cart.class);
                context.startActivity(intent);
            }
        });
        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemconfirm.setVisibility(View.VISIBLE);
                holder.addtocart.setVisibility(View.GONE);
                addtocart(product.getProductId(), product.getProductName(), product.getProductPrice(), preferenceManager.getCustomerId(), "1");
                holder.quantity.setVisibility(View.VISIBLE);
                holder.plus.setVisibility(View.VISIBLE);
                holder.minus.setVisibility(View.VISIBLE);
                holder.cart.setVisibility(View.VISIBLE);
                if (Cartitem != null && !Cartitem.isEmpty()) {
                    for (Get_Cart_Response.CartItem cartItem : Cartitem) {
                        if (cartItem.getProductId().equals(product.getProductId())) {
                            holder.quantity.setText(cartItem.getQuantity());
                            break;
                        }
                    }
                }
                holder.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentText = holder.quantity.getText().toString();
                        int currentValue = Integer.parseInt(currentText);
                        currentValue++;
                        holder.quantity.setText(String.valueOf(currentValue));
                        addtocart(product.getProductId(), product.getProductName(), product.getProductPrice(), preferenceManager.getCustomerId(), String.valueOf(currentValue));
                    }
                });

                holder.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String currentTextminus = holder.quantity.getText().toString();
                        int currentValueminus = Integer.parseInt(currentTextminus);

                        if (currentValueminus > 1) {
                            currentValueminus--;
                            holder.quantity.setText(String.valueOf(currentValueminus));
                            addtocart(product.getProductId(), product.getProductName(), product.getProductPrice(), preferenceManager.getCustomerId(), String.valueOf(currentValueminus));
                        } else {
                            holder.addtocart.setVisibility(View.VISIBLE);
                            holder.quantity.setVisibility(View.GONE);
                            holder.plus.setVisibility(View.GONE);
                            holder.minus.setVisibility(View.GONE);
                            holder.cart.setVisibility(View.GONE);
                            addtocart(product.getProductId(), product.getProductName(), product.getProductPrice(), preferenceManager.getCustomerId(), "0");
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return Product.size();
    }

    public static class View_prod_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView name, price, quantity;
        ImageButton addtocart, plus, minus, cart;

        public View_prod_Holder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.product_u_card);
            imageView=itemView.findViewById(R.id.product_u_image);
            name=itemView.findViewById(R.id.product_u_name);
            price=itemView.findViewById(R.id.product_u_price);
            addtocart=itemView.findViewById(R.id.addcart);
            plus=itemView.findViewById(R.id.addbtn);
            minus=itemView.findViewById(R.id.minusbtn);
            cart=itemView.findViewById(R.id.go_to_cart);
            quantity=itemView.findViewById(R.id.quantity_text);
        }
    }

    public void setProducts(List<MasterApi_Response.Product> products) {
        this.Product = products;
        notifyDataSetChanged();
    }
    private void addtocart(String productId,String productName, String Productprice ,String customerId, String quantity){
        restCall= RestClient.createService(RestCall.class,VariableBag.BASE_URL,VariableBag.API_KEY);


        restCall.Addcart("add_to_cart",productId,productName,Productprice,preferenceManager.getVendorId(),customerId,quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<AddCart_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AddCart_Response addCartResponse) {
                        if (addCartResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){

                        }
                        else {
                            Toast.makeText(context, addCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void Getcart(){
        restCall= RestClient.createService(RestCall.class,VariableBag.BASE_URL,VariableBag.API_KEY);
        restCall.Getcart("get_cart",preferenceManager.getCustomerId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Get_Cart_Response>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Get_Cart_Response getCartResponse) {
                        Cartitem=new ArrayList<>();
                        if (getCartResponse.getStatus().equalsIgnoreCase(VariableBag.SUCCESS_CODE)){

                            Cartitem=getCartResponse.getCartItems();
                            if (Cartitem != null && !Cartitem.isEmpty()) {
                                itemconfirm.setVisibility(View.VISIBLE);
                            } else {
                                itemconfirm.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(context, getCartResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}