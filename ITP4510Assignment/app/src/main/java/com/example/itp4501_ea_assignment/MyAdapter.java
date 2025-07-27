package com.example.itp4501_ea_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<String> mData;
    private OnRecyclerViewClickListener listener;
    // Constructor to initialize the adapter with context and data
    public MyAdapter(Context context, List<String> mData) {
        this.context = context;
        this.mData = mData;
    }
    // Constructor to initialize the adapter with context and array resource
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtItem.setText(mData.get(position));
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClickListener(v);
        });
    }
    // Method to update the data in the adapter
    @Override
    public int getItemCount() {
        return mData.size();
    }
    // Method to set the data in the adapter from an array resource
    public void setItemClickListener(OnRecyclerViewClickListener listener) {
        this.listener = listener;
    }
    // Method to set the data in the adapter from an array resource
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(view);
    }
}
