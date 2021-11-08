package com.classified.justsell.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.R;

public class notiAdapter extends RecyclerView.Adapter<notiAdapter.viewHolder> {
    private Context mcontext;


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.noti_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView notiimg;
        TextView notititle;
        TextView notidesc;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            notiimg=itemView.findViewById(R.id.noti_img);
            notititle=itemView.findViewById(R.id.noti_title);
            notidesc=itemView.findViewById(R.id.noti_descr);

        }
    }
}
