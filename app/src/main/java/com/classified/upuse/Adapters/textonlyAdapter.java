package com.classified.upuse.Adapters;

import static android.content.Context.UI_MODE_SERVICE;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.classified.upuse.R

import java.util.List;

public class textonlyAdapter extends RecyclerView.Adapter<textonlyAdapter.viewHolder> {
    private Context mcontext;
    private List<String> list;
    private oncardclicklistener listener;
    private Integer checkpos = 0;
    private Boolean selected = false;

    public textonlyAdapter(Context mcontext, List<String> list) {
        this.mcontext = mcontext;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.textonly_lay, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.fieldname.setText(list.get(position));
        UiModeManager uiModeManager = (UiModeManager) mcontext.getSystemService(UI_MODE_SERVICE);
        if (checkpos == position) {
            holder.fieldbg.setBackgroundResource(R.drawable.backgroundbg_yellow);
            holder.fieldname.setTextColor(Color.parseColor("#1D1D1D"));


        } else if (checkpos != position) {
            holder.fieldbg.setBackgroundResource(R.drawable.backgroundbg);
            int nightModeFlags =
                    mcontext.getResources().getConfiguration().uiMode &
                            Configuration.UI_MODE_NIGHT_MASK;
            switch (nightModeFlags) {
                case Configuration.UI_MODE_NIGHT_YES:
                    holder.fieldname.setTextColor(Color.parseColor("#FFFFFF"));
                    break;

                case Configuration.UI_MODE_NIGHT_NO:
                    holder.fieldname.setTextColor(Color.parseColor("#1D1D1D"));
                    break;

                case Configuration.UI_MODE_NIGHT_UNDEFINED:
                    break;
            }
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView fieldname;
        View fieldbg;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            fieldname = itemView.findViewById(R.id.fieldname);
            fieldbg = itemView.findViewById(R.id.fieldbg);
            fieldbg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        checkpos = position;
                        notifyDataSetChanged();
                        listener.oncardclick(list.get(position));
                    }

                }
            });
        }
    }

    public interface oncardclicklistener {
        void oncardclick(String catname);
    }

    public void setoncardclicklistener(oncardclicklistener listener) {
        this.listener = listener;
    }
}
