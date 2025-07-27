package com.example.itp4501_ea_assignment;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{
//Item inside the RecyclerView

    protected TextView txtItem;
    // Constructor to initialize the ViewHolder with the itemView
    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        txtItem=itemView.findViewById(R.id.txtItem);
    }
}

