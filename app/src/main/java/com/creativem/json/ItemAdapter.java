package com.creativem.json;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<MainActivity.ItemAdapter.ViewHolder>{
    private final ArrayList<ModelItem> modelItem;
    private final Context context;

    public ItemAdapter(ArrayList<ModelItem> modelItem, Context context) {
        this.modelItem = modelItem;
        this.context = context;
    }

    @NonNull
    @Override
    public MainActivity.ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MainActivity.ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivity.ItemAdapter.ViewHolder holder, int position) {
        ModelItem item = modelItem.get(position);
        Glide.with(context).load(item.getImagen()).into(holder.imagen);
        holder.title.setText(item.getTitle());
        holder.detail.setText(item.getDetail());
        holder.consejos.setText(item.getConsejos());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedModelItem = item;
                notifyDataSetChanged();
            }
        });

        if (selectedModelItem != null && selectedModelItem.equals(item)) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, androidx.cardview.R.color.cardview_dark_background));
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return modelItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imagen;
        private final TextView title;
        private final TextView detail;
        private final TextView consejos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imagenItem);
            title = itemView.findViewById(R.id.titleItem);
            detail = itemView.findViewById(R.id.detailItem);
            consejos = itemView.findViewById(R.id.consejosItem);
        }
    }
}
