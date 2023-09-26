package com.classified.upuse.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R
import com.classified.upuse.Models.homeResponse;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MyadsAdapter extends RecyclerView.Adapter<MyadsAdapter.ViewHolder> {

    private Context mcontext;
    private List<homeResponse .adsResult> list;
    onItemClick listener;
    public MyadsAdapter(Context mcontext, List<homeResponse.adsResult> list) {
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
        final int radius = 13;
        final int margin = 7;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.get().load(list.get(position).getAd_image()).resize(300,300).centerCrop().transform(transformation).into(holder.adsimg);
        holder.adstitle.setText(list.get(position).getAd_title());
        holder.adsprice.setText("₹ "+list.get(position).getAd_price());
        if(list.get(position).getAd_pricecut()!=null && !list.get(position).getAd_pricecut().equals("")) {
            holder.adspricecut.setText("₹ " + list.get(position).getAd_pricecut());
            holder.adspricecut.setPaintFlags(holder.adspricecut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else {
            holder.adspricecut.setVisibility(View.INVISIBLE);
        }
        holder.adsdesr.setText(list.get(position).getAd_description());
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

            adsbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        listener.onAdClick(list.get(getAdapterPosition()).getAd_category()
                                ,list.get(getAdapterPosition()).getAd_id(),list.get(getAdapterPosition())
                                        .getProduct_name(),list.get(getAdapterPosition()).getUser_id());
                    }
                }
            });
        }
    }
    public interface onItemClick {
        void onAdClick(String category_name,String ad_id,String prod_name,String userid);
    }

    public void setonItemClick(onItemClick listener) {
        this.listener=listener;
    }
}
