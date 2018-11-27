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
import com.example.wmell.app.util.Constants;

import java.util.List;

import static com.example.wmell.app.util.Constants.HAS_ACCESS;
import static com.example.wmell.app.util.Constants.HOLD_ACCESS;
import static com.example.wmell.app.util.Constants.NO_ACCESS;


public class GatesAdapter extends RecyclerView.Adapter<GatesAdapter.GateViewHolder> {

    public static final String TAG = GatesAdapter.class.getSimpleName();
    private Context mContext;
    private List<GateResponse> mGates;
    private List<Integer> mPermissionsUser;
    private List<ResponsePermissionsUpdate> mPermissionsOnHold;


    public GatesAdapter(Context context, List<GateResponse> gates) {
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
        GateResponse gateResponse = mGates.get(position);
        holder.viewImageIcon.setImageResource(R.drawable.door);
        holder.viewName.setText(gateResponse.getName());

        if (gateResponse.getStatus() == 2) {
            holder.viewLock.setImageResource(R.drawable.green_padlock);

        } else if (gateResponse.getStatus() == 1) {
            holder.viewLock.setImageResource(R.drawable.yellow_padlock);

        } else {
            holder.viewLock.setImageResource(R.drawable.red_padlocl);
        }

    }

    @Override
    public int getItemCount() {
        if (mGates != null) {
            return mGates.size();
        } else {
            return 0;
        }
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
