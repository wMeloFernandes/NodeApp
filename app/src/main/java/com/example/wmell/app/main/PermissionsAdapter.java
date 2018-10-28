package com.example.wmell.app.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wmell.app.DAO.ResponsePermissionsUpdate;
import com.example.wmell.app.R;

import java.util.List;

/**
 * Created by wmell on 27/10/2018.
 */

class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.PermissionsViewHolder> {
    public static final String TAG = PermissionsAdapter.class.getSimpleName();
    private Context mContext;
    private List<ResponsePermissionsUpdate> mPermissionsList;

    public PermissionsAdapter(Context context, List<ResponsePermissionsUpdate> list) {
        this.mContext = context;
        this.mPermissionsList = list;
    }


    @NonNull
    @Override
    public PermissionsAdapter.PermissionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.permissions_recycler_model, parent, false);
        return new PermissionsAdapter.PermissionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionsViewHolder holder, int position) {
        ResponsePermissionsUpdate response = mPermissionsList.get(position);
        holder.viewImageIcon.setImageResource(R.drawable.door);
        holder.viewName.setText(response.getGateName());
        holder.viewRequestDate.setText(response.getTime());
        holder.viewImageLock.setImageResource(R.drawable.yellow_padlock);
    }


    @Override
    public int getItemCount() {
        return mPermissionsList.size();
    }

    public static class PermissionsViewHolder extends RecyclerView.ViewHolder {
        private ImageView viewImageIcon;
        private TextView viewName;
        private TextView viewRequestDate;
        private ImageView viewImageLock;


        public PermissionsViewHolder(View itemView) {
            super(itemView);
            viewImageIcon = itemView.findViewById(R.id.imageView_iconPermissions);
            viewName = itemView.findViewById(R.id.tv_gate_namePermissions);
            viewRequestDate = itemView.findViewById(R.id.tv_request_date);
            viewImageLock = itemView.findViewById(R.id.imageView_padlockPermissions);
        }
    }
}
