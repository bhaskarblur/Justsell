package com.classified.justsell.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class promadsAdapter extends RecyclerView.Adapter<promadsAdapter.ViewHolder> {

    private Context mcontext;
    private List<homeResponse .adsResult> list;

    public promadsAdapter(Context mcontext, List<homeResponse.adsResult> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.ads_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getAd_image()).resize(300,300).into(holder.adsimg);
        holder.adstitle.setText(list.get(position).getAd_title());
        holder.adsprice.setText("₹ "+list.get(position).getAd_price());
        holder.adspricecut.setText("₹ "+list.get(position).getAd_pricecut());
        holder.adspricecut.setPaintFlags(holder.adspricecut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.adsdesr.setText("Description: "+list.get(position).getAd_description());
        // holder.adsdate.setText("Posted on "+list.get(position).getAd_date());
        if(list.get(position).getFeatured_status().equals("yes")) {
            holder.adsfeat.setVisibility(View.VISIBLE);
        }
     }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView adsimg;
        TextView adstitle;
        TextView adsprice;
        TextView adspricecut;
        TextView adsdesr;
        TextView adsdate;
        View adsfeat;
        View adsbg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            adsimg=itemView.findViewById(R.id.ads_image);
            adstitle=itemView.findViewById(R.id.ads_title);
            adsprice=itemView.findViewById(R.id.ads_price);
            adspricecut=itemView.findViewById(R.id.ads_pricecut);
            adsdesr=itemView.findViewById(R.id.ads_descr);
            adsdate=itemView.findViewById(R.id.ads_date);
            adsfeat=itemView.findViewById(R.id.featuredimg);
            adsbg=itemView.findViewById(R.id.ads_card);

        }
    }
}
