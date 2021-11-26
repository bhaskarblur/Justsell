package com.classified.justsell.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class adsAdapter extends RecyclerView.Adapter {

    private Context mcontext;
    private List<homeResponse .adsResult> list;
    onItemClick listener;
    private final static int AD_VIEW_TYPE=0;
    private final static int PRODUCT_VIEW_TYPE=1;
    public adsAdapter(Context mcontext, List<homeResponse.adsResult> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=null;
        if(viewType==AD_VIEW_TYPE) {
            view=LayoutInflater.from(mcontext).inflate(R.layout.admob_adlay,parent,false);
            return new adViewHolder(view);
        }
        else {
            view = LayoutInflater.from(mcontext).inflate(R.layout.ads_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position%7==0 && position!=0) {
            adViewHolder holder1=(adViewHolder) holder;
            holder1.loadAds();
        }
        else {
            ViewHolder holder1=(ViewHolder) holder;
            final int radius = 13;
            final int margin = 7;
            final Transformation transformation = new RoundedCornersTransformation(radius, margin);
            Picasso.get().load(list.get(position).getAd_image()).resize(300, 300).transform(transformation).into(holder1.adsimg);
            holder1.adstitle.setText(list.get(position).getAd_title());
            holder1.adsprice.setText("₹ " + list.get(position).getAd_price());

            if (list.get(position).getAd_pricecut() != null && !list.get(position).getAd_pricecut().equals("")) {
                holder1.adspricecut.setText("₹ " + list.get(position).getAd_pricecut());
                holder1.adspricecut.setPaintFlags(holder1.adspricecut.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder1.adspricecut.setVisibility(View.INVISIBLE);
            }
            holder1.adsdesr.setText(list.get(position).getAd_description());
            holder1.adsdate.setText("Posted on " + list.get(position).getAd_date());
            if (list.get(position).getFeatured_status().equals("1") && list.get(position).getAd_title()
                    .equals(holder1.adstitle.getText().toString())) {
                holder1.adsfeat.setVisibility(View.VISIBLE);
            }
        }
     }
    public void searchList(List<homeResponse .adsResult> searchedList) {
        list=searchedList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(position%7==0 && position!=0) {
            return AD_VIEW_TYPE;
        }
        else {
            return PRODUCT_VIEW_TYPE;
        }
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

    public class adViewHolder extends RecyclerView.ViewHolder{
        NativeAdView adview;
        public adViewHolder(@NonNull View itemView) {
            super(itemView);
            adview=itemView.findViewById(R.id.ad_view);


        }

        private void loadAds() {
            // native ad id- ca-app-pub-8346863949141411/5883819900
            AdLoader adLoader = new AdLoader.Builder(mcontext, "ca-app-pub-8346863949141411/5883819900")
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd NativeAd) {
                            NativeAdView adView= itemView.findViewById(R.id.ad_view);
                            adView.setNativeAd(NativeAd);
                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
                            Log.d("Failed because",adError.getMessage());
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            AdRequest adRequest=new AdRequest.Builder().build();
            adLoader.loadAds(adRequest,4);
        }
    }
}
