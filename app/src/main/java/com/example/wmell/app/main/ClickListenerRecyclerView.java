package com.example.wmell.app.main;

import android.view.View;

public interface ClickListenerRecyclerView {
    public void onClick(View view, int position);

    public void onLongClick(View view, int position);
}
