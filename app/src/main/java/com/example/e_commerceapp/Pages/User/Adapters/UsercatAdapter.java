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

public class UsercatAdapter extends RecyclerView.Adapter<UsercatAdapter.View_cat_Holder> {

    Context context;
    List<MasterApi_Response.Category> categories;
    LayoutInflater layoutInflater;
    private CategoryClickListener categoryClickListener;

    public interface CategoryClickListener {
        void onCategoryClicked(String categoryId);
    }

    public UsercatAdapter(Context context, List<MasterApi_Response.Category> categories, CategoryClickListener listener) {
        this.context = context;
        this.categories = categories;
        this.categoryClickListener = listener;
    }

    @NonNull
    @Override
    public View_cat_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_cat_adapter, parent, false);
        return new View_cat_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_cat_Holder holder, int position) {
        MasterApi_Response.Category currentCategory = categories.get(position);
        holder.textView.setText(currentCategory.getCategoryName());
        Glide.with(context)
                .load(categories.get(position).getImageUrl())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.camera_icon)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryClickListener != null) {
                    categoryClickListener.onCategoryClicked(currentCategory.getCategoryId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void setCategories(List<MasterApi_Response.Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public static class View_cat_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;

        public View_cat_Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cat_u_card);
            imageView = itemView.findViewById(R.id.category_image);
            textView = itemView.findViewById(R.id.category_name);
        }
    }
}
