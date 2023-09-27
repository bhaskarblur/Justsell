package com.classified.upuse.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R;
import com.classified.upuse.Models.homeResponse;


import java.util.ArrayList;
import java.util.List;

public class cityAdapter extends RecyclerView.Adapter<cityAdapter.viewHolder> {
    private Context mcontext;
    private List<homeResponse .citiesResp> list;
    private List<homeResponse .citiesResp> searchedlist=new ArrayList<>();
    private onCityClick listener;
    public cityAdapter(Context mcontext, List<homeResponse.citiesResp> searchedlist) {
        this.mcontext = mcontext;
        this.searchedlist = searchedlist;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mcontext).inflate(R.layout.city_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.cityname.setText(searchedlist.get(position).getCity()+", "+searchedlist.get(position).getState());
    }

    public void searchList(List<homeResponse .citiesResp> searchedList1) {
        searchedlist.clear();
        searchedlist=searchedList1;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return searchedlist.size();
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
                        listener.oncitynameclick(searchedlist.get(getAdapterPosition()).getCity(),
                                searchedlist.get(getAdapterPosition()).getState());
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
