package com.classified.upuse.Adapters;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R;
import com.classified.upuse.Models.homeResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Objects;

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
        if(position%6==0 && position!=0) {
            adViewHolder holder1=(adViewHolder) holder;
            holder1.refreshAd();
        }
        else {
            ViewHolder holder1=(ViewHolder) holder;
            final int radius = 13;
            final int margin = 0;
            try {
                    final Transformation transformation = new RoundedCornersTransformation(radius, margin);
                    Picasso.get().load(list.get(position).getAd_image())
                            .transform(transformation).into(holder1.adsimg);

                    holder1.adstitle.setText(list.get(position).getAd_title().toString());
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
//                }
            }
            catch (Throwable err) {
                Log.d("error on ads", err.getMessage().toString());
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
        if(position%6==0 && position!=0) {
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
        NativeAd nativeAd;
        public adViewHolder(@NonNull View itemView) {
            super(itemView);
            adview=itemView.findViewById(R.id.ad_nativeview);
            loadAds();
        }

        private void loadAds() {
            // native ad id- ca-app-pub-8346863949141411/5883819900
            // native ads id
            AdLoader adLoader = new AdLoader.Builder(mcontext, "ca-app-pub-3736420404472867/7252409338")
                    .forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                        @Override
                        public void onNativeAdLoaded(NativeAd NativeAd) {
                            NativeAdView adView= itemView.findViewById(R.id.ad_nativeview);
                            adView.setNativeAd(NativeAd);
//                            Toast.makeText(mcontext, "Native Loaded:", Toast.LENGTH_SHORT).show();


                        }
                    })
                    .withAdListener(new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError adError) {
//                            Toast.makeText(mcontext, "Native Error "+adError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .withNativeAdOptions(new NativeAdOptions.Builder()
                            // Methods in the NativeAdOptions.Builder class can be
                            // used here to specify individual options settings.
                            .build())
                    .build();

            AdRequest adRequest=new AdRequest.Builder().build();
            adLoader.loadAd(adRequest);
        }


        private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_app_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


            ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());
            Objects.requireNonNull(adView.getMediaView()).setMediaContent(Objects.requireNonNull(nativeAd.getMediaContent()));


            if (nativeAd.getBody() == null) {
                Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);

            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
            if (nativeAd.getCallToAction() == null) {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
            if (nativeAd.getIcon() == null) {
                Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
            } else {
                ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getPrice() == null) {
                Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);

            } else {
                Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }
            if (nativeAd.getStore() == null) {
                Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
            } else {
                Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }
            if (nativeAd.getStarRating() == null) {
                Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) Objects.requireNonNull(adView.getStarRatingView())).setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }

            if (nativeAd.getAdvertiser() == null) {
                Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
            } else
                ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);


            adView.setNativeAd(nativeAd);


        }


        private void refreshAd() {
            // test ad id - ca-app-pub-3940256099942544/2247696110
            // native ad id - ca-app-pub-3736420404472867/7252409338
            AdLoader.Builder builder = new AdLoader.Builder(mcontext, "ca-app-pub-3736420404472867/7252409338");
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {
                @Override
                public void onNativeAdLoaded(NativeAd unifiedNativeAd) {

                    if (nativeAd != null) {
                        nativeAd.destroy();
                    }
                    nativeAd = unifiedNativeAd;
                    FrameLayout frameLayout = itemView.findViewById(R.id.ad_nativeview);
                    NativeAdView adView = (NativeAdView) LayoutInflater.from(mcontext).inflate(R.layout.admob_adlay, null);

                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    frameLayout.removeAllViews();
                    frameLayout.addView(adView);
                }
            }).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                public void onAdFailedToLoad(int i) {

                }
            }).build();
            adLoader.loadAd(new AdRequest.Builder().build());

        }

    }
}
