package com.example.wmell.app.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wmell.app.DAO.*;
import com.example.wmell.app.R;


public class GatesAdapter extends RecyclerView.Adapter<GatesAdapter.GateViewHolder> {

    public static final String TAG = GatesAdapter.class.getSimpleName();
    private Context mContext;
    private Gates mGates;


    public GatesAdapter(Context context, Gates gates) {
        mContext = context;
        mGates = gates;
    }

    @Override
    public GateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_model, parent, false);
        return new GateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GateViewHolder holder, int position) {
        //Gate gate = mGates.getGates().get(position);
        com.example.wmell.app.DAO.Gate gate = mGates.getGates().get(position);
        holder.viewImageIcon.setImageResource(R.drawable.door);
        holder.viewName.setText(gate.getName());
        holder.viewLock.setImageResource(R.drawable.green_padlock);
    }

    @Override
    public int getItemCount() {
        return mGates.getGates().size();
    }

    public static class GateViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImageIcon;
        private TextView viewName;


        private ImageView viewLock;

        public GateViewHolder(View itemView) {
            super(itemView);
            viewImageIcon = itemView.findViewById(R.id.imageView_icon);
            viewName = itemView.findViewById(R.id.textView_name_gate);
            viewLock = itemView.findViewById(R.id.imageView_padlock);
        }
    }


}
