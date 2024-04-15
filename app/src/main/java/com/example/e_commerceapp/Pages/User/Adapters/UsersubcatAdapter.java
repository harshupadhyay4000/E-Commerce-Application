package com.example.e_commerceapp.Pages.User.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.e_commerceapp.NetworkResponse.MasterApi_Response;
import com.example.e_commerceapp.R;
import java.util.List;

public class UsersubcatAdapter extends RecyclerView.Adapter<UsersubcatAdapter.View_subcat_Holder> {
    private Context context;
    private List<MasterApi_Response.Subcategory> subcategories;
    private SubcategoryClickedListener subcategoryClickedListener;
    private LayoutInflater layoutInflater;

    public interface SubcategoryClickedListener {
        void onSubcategoryClicked(String subcategoryId);
    }

    public UsersubcatAdapter(Context context, List<MasterApi_Response.Subcategory> subcategories, SubcategoryClickedListener listener) {
        this.context = context;
        this.subcategories = subcategories;
        this.subcategoryClickedListener = listener;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View_subcat_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.user_subcat_adapter, parent, false);
        return new View_subcat_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_subcat_Holder holder, int position) {
        MasterApi_Response.Subcategory subcategory = subcategories.get(position);
        holder.subcatname.setText(subcategory.getSubcategoryName());
        Glide.with(context)
                .load(subcategories.get(position).getImageUrl())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.sub_catergory)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (subcategoryClickedListener != null) {
                    subcategoryClickedListener.onSubcategoryClicked(subcategory.getSubcategoryId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subcategories.size();
    }

    public void setSubcategories(List<MasterApi_Response.Subcategory> subcategories) {
        this.subcategories = subcategories;
        notifyDataSetChanged();
    }

    public static class View_subcat_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView subcatname;

        public View_subcat_Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.subcat_u_card);
            imageView = itemView.findViewById(R.id.subcategory_image);
            subcatname = itemView.findViewById(R.id.subcategory_name);
        }
    }
}
