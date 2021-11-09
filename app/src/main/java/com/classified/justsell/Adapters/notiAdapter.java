package com.classified.justsell.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class notiAdapter extends RecyclerView.Adapter<notiAdapter.viewHolder> {
    private Context mcontext;
    private List<homeResponse .notiResult> list;

    public notiAdapter(Context mcontext, List<homeResponse.notiResult> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.noti_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Picasso.get().load(list.get(position).getImage()).into(holder.notiimg);
        Log.d("img",list.get(position).getImage());
        holder.notititle.setText(list.get(position).getTitle());
        holder.notidesc.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
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
