package com.example.wmell.app.main;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wmell.app.DAO.Historical;
import com.example.wmell.app.R;

import java.util.List;

class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.HistoricalViewHolder> {

    public static final String TAG = HistoricalAdapter.class.getSimpleName();
    private Context mContext;
    private List<Historical> mHistoricalList;

    public HistoricalAdapter(Context context, List<Historical> list) {
        this.mContext = context;
        this.mHistoricalList = list;
    }


    @NonNull
    @Override
    public HistoricalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.historical_recyclerview_model, parent, false);
        return new HistoricalAdapter.HistoricalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoricalViewHolder holder, int position) {
        Historical historical = mHistoricalList.get(position);
        holder.viewImageIcon.setImageResource(R.drawable.door);
        holder.viewName.setText(historical.getGateName());
        holder.viewLastAccess.setText(historical.getTime());
    }

    @Override
    public int getItemCount() {
        return mHistoricalList.size();
    }

    public static class HistoricalViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImageIcon;
        private TextView viewName;
        private TextView viewLastAccess;


        public HistoricalViewHolder(View itemView) {
            super(itemView);
            viewImageIcon = itemView.findViewById(R.id.imageView_icon);
            viewName = itemView.findViewById(R.id.tv_gate_name);
            viewLastAccess = itemView.findViewById(R.id.tv_last_access);
        }
    }
}
