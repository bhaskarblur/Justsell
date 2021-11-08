package com.classified.justsell.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.justsell.Models.homeResponse;
import com.classified.justsell.R;

import java.util.List;

public class cityAdapter extends RecyclerView.Adapter<cityAdapter.viewHolder> {
    private Context mcontext;
    private List<homeResponse .citiesResp> list;
    private onCityClick listener;
    public cityAdapter(Context mcontext, List<homeResponse.citiesResp> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.city_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.cityname.setText(list.get(position).getCity()+", "+list.get(position).getState());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView cityname;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cityname=itemView.findViewById(R.id.city_txt);

            cityname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(getAdapterPosition()!=RecyclerView.NO_POSITION) {
                        listener.oncitynameclick(list.get(getAdapterPosition()).getCity(),
                                list.get(getAdapterPosition()).getState());
                    }
                }
            });
        }
    }
    public interface onCityClick {
        void oncitynameclick(String city,String state);
    }
    public void setonCityclickListener(onCityClick listener){
        this.listener=listener;
    }
}
