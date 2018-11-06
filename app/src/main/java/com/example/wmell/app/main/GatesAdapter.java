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
    private Gates mGates;
    private List<Integer> mPermissionsUser;
    private List<ResponsePermissionsUpdate> mPermissionsOnHold;


    public GatesAdapter(Context context, Gates gates, List<Integer> permissionsUser, List<ResponsePermissionsUpdate> permissionsOnHold) {
        mContext = context;
        mGates = gates;
        mPermissionsUser = permissionsUser;
        mPermissionsOnHold = permissionsOnHold;
    }

    @Override
    public GateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_model, parent, false);
        return new GateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GateViewHolder holder, int position) {
        int noAccessValue = 0;
        com.example.wmell.app.DAO.Gate gate = mGates.getGates().get(position);
        holder.viewImageIcon.setImageResource(R.drawable.door);
        holder.viewName.setText(gate.getName());

        if (mPermissionsUser != null) {
            for (int i = 0; i < mPermissionsUser.size(); i++) {
                if (mPermissionsUser.get(i) == gate.getGateId()) {
                    holder.viewLock.setImageResource(R.drawable.green_padlock);
                    gate.setmStatus(HAS_ACCESS);
                    noAccessValue++;
                }
            }
        }

        if (mPermissionsOnHold != null) {
            for (int i = 0; i < mPermissionsOnHold.size(); i++) {
                if (mPermissionsOnHold.get(i).getGateId() == gate.getGateId()) {
                    holder.viewLock.setImageResource(R.drawable.yellow_padlock);
                    gate.setmStatus(HOLD_ACCESS);
                    noAccessValue++;
                }
            }
        }
        if (noAccessValue == 0) {
            gate.setmStatus(NO_ACCESS);
        }

    }

    @Override
    public int getItemCount() {
        if (mGates.getGates() != null) {
            return mGates.getGates().size();
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
