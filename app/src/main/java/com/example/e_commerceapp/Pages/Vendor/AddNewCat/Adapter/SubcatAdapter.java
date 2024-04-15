package com.example.e_commerceapp.Pages.Vendor.AddNewCat.Adapter;

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
import com.example.e_commerceapp.NetworkResponse.V_Sub_Category_Response;
import com.example.e_commerceapp.R;

import java.util.ArrayList;
import java.util.List;

public class SubcatAdapter extends RecyclerView.Adapter<SubcatAdapter.View_new_Holder> {
    LayoutInflater layoutInflater;
    Context context;
    List<V_Sub_Category_Response.Subcategory> Subcategory;
    EditInterface editInterface;
    List<V_Sub_Category_Response.Subcategory> filteredSubcategory;

    public interface EditInterface {
        void EditClick(V_Sub_Category_Response.Subcategory Subcategory);

        void DeleteClick(V_Sub_Category_Response.Subcategory Subcategory);
    }

    public void setEdit(EditInterface editInterface) {
        this.editInterface = editInterface;
    }


    public SubcatAdapter(Context context, List<V_Sub_Category_Response.Subcategory> Subcategory) {
        this.context = context;
        this.Subcategory = Subcategory;
        this.filteredSubcategory = new ArrayList<>(Subcategory);
    }

    public void filterList(List<V_Sub_Category_Response.Subcategory> filteredSubcategory) {
        this.filteredSubcategory = filteredSubcategory;
        notifyDataSetChanged(); // Notify adapter about data change
    }

    @NonNull
    @Override
    public View_new_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.sub, parent, false);
        return new View_new_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_new_Holder holder, int position) {
        V_Sub_Category_Response.Subcategory currentSubcategory = filteredSubcategory.get(position);
        holder.textView.setText(currentSubcategory.getSubcategoryName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle item click if needed
            }
        });
        Glide.with(context)
                .load(currentSubcategory.getImagePath())
                .placeholder(R.drawable.frozen)
                .error(R.drawable.sub_catergory)
                .into(holder.imageView);

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInterface != null) {
                    editInterface.EditClick(currentSubcategory);
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editInterface != null) {
                    editInterface.DeleteClick(currentSubcategory);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredSubcategory.size();
    }

    public static class View_new_Holder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView;
        TextView textView;
        Button edit, delete;

        public View_new_Holder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.subcategory_v_one_card);
            imageView = itemView.findViewById(R.id.subcategory_v_one_image);
            textView = itemView.findViewById(R.id.subcategory_v_one_name);
            edit = itemView.findViewById(R.id.subcatedit_one_button);
            delete = itemView.findViewById(R.id.subcatdelete_one_button);
        }
    }
}
