package com.example.wmell.app.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wmell.app.R;

import java.util.ArrayList;

public class GatesAdapter extends RecyclerView.Adapter<GatesAdapter.GateViewHolder> {

    public static final String TAG = GatesAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<Gate> mGates;


    public GatesAdapter(Context context, ArrayList<Gate> gates) {
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
        Gate gate = mGates.get(position);
        holder.viewImageIcon.setImageResource(gate.getTypeGateImage());
        holder.viewName.setText(gate.getGateName());
        holder.viewLock.setImageResource(gate.setIconStatus());
    }

    @Override
    public int getItemCount() {
        return mGates.size();
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
