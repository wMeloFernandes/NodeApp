package com.example.wmell.app.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.R;

import java.util.ArrayList;

import static com.example.wmell.app.util.Constants.GATE_DOOR;
import static com.example.wmell.app.util.Constants.PERMISSION_DENIED;
import static com.example.wmell.app.util.Constants.PERMISSION_GRANTED;
import static com.example.wmell.app.util.Constants.PERMISSION_REQUESTED;

public class MainScreenOld extends AppCompatActivity {

    private LinearLayout mLinerarLayoutMain;
    private ProgressBar mProgressBarMain;
    private TextView mTextViewMain;
    private RecyclerView mRecyclerViewData;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Gate> mGates;
    private GatesAdapter mGateAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter_by:
                startActivity(new Intent(MainScreenOld.this, MainActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLinerarLayoutMain = findViewById(R.id.linearLayout_main);
        mProgressBarMain = findViewById(R.id.progressBar_Main);
        mTextViewMain = findViewById(R.id.textView_main);
        mRecyclerViewData = findViewById(R.id.recyclerView_main);
        mGates = new ArrayList<>();

        //Wait for Server answer(MOCKED YET)
        waitServerResponse();

        //Setting RecyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewData.setLayoutManager(mLayoutManager);
        mRecyclerViewData.addOnItemTouchListener(new RecyclerTouchListener(this, mRecyclerViewData, new ClickListenerRecyclerView() {
            @Override
            public void onClick(View view, int position) {
                checkHasAuthorization(mGates.get(position).getAccessStatus(), view, mGates.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(MainScreenOld.this, "Long Click: " + position, Toast.LENGTH_SHORT).show();
            }
        }));

        addMockedGates(mGates);

        //mGateAdapter = new GatesAdapter(getApplicationContext(), mGates);
        mRecyclerViewData.setAdapter(mGateAdapter);
    }


    public void updateUIFetchingData() {
        mLinerarLayoutMain.setVisibility(View.GONE);
        mRecyclerViewData.setVisibility(View.VISIBLE);
    }

    private void waitServerResponse() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                updateUIFetchingData();
            }
        }.start();
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private ClickListenerRecyclerView clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListenerRecyclerView clicklistener) {

            this.clicklistener = clicklistener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recycleView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clicklistener != null) {
                        clicklistener.onLongClick(child, recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clicklistener != null && gestureDetector.onTouchEvent(e)) {
                clicklistener.onClick(child, rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    public void addMockedGates(ArrayList<Gate> gates) {
        gates.add(new Gate(PERMISSION_DENIED, "Office", GATE_DOOR));
        gates.add(new Gate(PERMISSION_DENIED, "Office 1", GATE_DOOR));
        gates.add(new Gate(PERMISSION_DENIED, "Office 2", GATE_DOOR));
        gates.add(new Gate(PERMISSION_GRANTED, "Room 7", GATE_DOOR));
    }

    private void checkHasAuthorization(int authorization, final View view, final Gate gate) {
        switch (authorization) {
            case PERMISSION_GRANTED:
                startActivity(new Intent(MainScreenOld.this, GateDetails.class));
                break;
            case PERMISSION_DENIED:
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainScreenOld.this, R.style.Theme_AppCompat));
                builder.setMessage("You don't have access to this port. Do you wanna request?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Requesting access...", Toast.LENGTH_SHORT).show();
                        gate.setGateAuthorization(PERMISSION_REQUESTED);
                        mGateAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Thank you!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                break;
            case PERMISSION_REQUESTED:
                Toast.makeText(this, "You already requested access to this gate. Waiting for approval!", Toast.LENGTH_SHORT).show();
                break;
        }

    }

}
