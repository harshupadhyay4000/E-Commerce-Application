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
import com.example.e_commerceapp.NetworkResponse.V_Category_Response;
import com.example.e_commerceapp.R;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.View_cat_Holder> {
    LayoutInflater layoutInflater;
    Context context;
    List<V_Category_Response.Category> category;
    EditInterface editInterface;
    List<V_Category_Response.Category> filteredCategory;

    public interface EditInterface {
        void EditClick(V_Category_Response.Category Category);
        void deleteclick(V_Category_Response.Category Category);
    }

    public void setEdit(EditInterface editInterface) {
        this.editInterface = editInterface;
    }

    public CatAdapter(Context context, List<V_Category_Response.Category> Category) {
        this.context = context;
        this.category = Category;
        this.filteredCategory = new ArrayList<>(Category); // Initialize filteredCategory with all categories initially
    }

    // Method to set the filtered list of categories
    public void filterList(List<V_Category_Response.Category> filteredCategory) {
        this.filteredCategory = filteredCategory;
        notifyDataSetChanged(); // Notify adapter about data change
    }

    @NonNull
    @Override
    public View_cat_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.v_cat_pg, parent, false);
        return new View_cat_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_cat_Holder holder, int position) {
        // Bind data from filteredCategory list
        V_Category_Response.Category currentCategory = filteredCategory.get(position);
        // Update views using currentCategory data
        holder.catText.setText(currentCategory.getCategoryName());
        // Load image with Glide as before
        Glide.with(context)
                .load(currentCategory.getImagePath())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.cat_section)
                .into(holder.imageView);
        // Set click listeners as before
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle card click
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInterface != null) {
                    editInterface.EditClick(currentCategory);
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInterface != null) {
                    editInterface.deleteclick(currentCategory);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredCategory.size(); // Return size of filtered list
    }

    public static class View_cat_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView catText;
        Button edit, delete;

        public View_cat_Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.category_v_card);
            imageView = itemView.findViewById(R.id.category_v_image);
            catText = itemView.findViewById(R.id.category_v_name);
            edit = itemView.findViewById(R.id.edit_button);
            delete = itemView.findViewById(R.id.delete_button);
        }
    }
}