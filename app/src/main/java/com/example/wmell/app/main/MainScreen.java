package com.example.wmell.app.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.User;
import com.example.wmell.app.R;
import com.example.wmell.app.login.LoginApplication;
import com.example.wmell.app.networking.APIRoutes;
import com.example.wmell.app.networking.DigitalKeyApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.wmell.app.util.Constants.GATE_DOOR;
import static com.example.wmell.app.util.Constants.PERMISSION_DENIED;
import static com.example.wmell.app.util.Constants.PERMISSION_GRANTED;
import static com.example.wmell.app.util.Constants.PERMISSION_REQUESTED;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, onDataLoader {

    public User mUserText = new User("Willian", "w.melo.fernandes.un@gmail.com");
    private static LinearLayout mLinerarLayoutMain;
    private ProgressBar mProgressBarMain;
    private TextView mTextViewMain;
    private static RecyclerView mRecyclerViewData;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Gate> mGates;
    public static Gates mGatesDAO;
    private GatesAdapter mGateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen2);
        setTitle(R.string.title_activity_main_screen2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getGatesList(onDataLoader);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);

        TextView name = headerLayout.findViewById(R.id.nameView);
        TextView email = headerLayout.findViewById(R.id.emailView);
        name.setText(mUserText.getUsername());
        email.setText(mUserText.getEmail());

        mLinerarLayoutMain = findViewById(R.id.linearLayout_mainScreen);
        mProgressBarMain = findViewById(R.id.progressBar_MainScreen);
        mTextViewMain = findViewById(R.id.textView_mainScreen);
        mRecyclerViewData = findViewById(R.id.recyclerView_mainScreen);
        mGates = new ArrayList<>();

        APIRoutes.waitServerResponse();

        //Setting RecyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerViewData.setLayoutManager(mLayoutManager);
        mRecyclerViewData.addOnItemTouchListener(new MainScreen.RecyclerTouchListener(this, mRecyclerViewData, new ClickListenerRecyclerView() {
            @Override
            public void onClick(View view, int position) {
                //checkHasAuthorization(mGates.get(position).getAccessStatus(), view, mGates.get(position));
                Toast.makeText(MainScreen.this, "Clicou", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        //addMockedGates(mGates);

        mGateAdapter = new GatesAdapter(getApplicationContext(), mGatesDAO);
        mRecyclerViewData.setAdapter(mGateAdapter);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            updateUIFetchingDataHide();
            APIRoutes.waitServerResponse();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_permissionsOnHold) {
            // Filtrar permissões ON HOLD
        } else if (id == R.id.nav_history) {
            //Pegar historico do Servidor
        } else if (id == R.id.nav_logout) {
            startActivity(new Intent(MainScreen.this, LoginApplication.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDataLoaded(Gates gates) {
        mGatesDAO = gates;
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

    public static void updateUIFetchingData() {
        mLinerarLayoutMain.setVisibility(View.GONE);
        mRecyclerViewData.setVisibility(View.VISIBLE);
    }

    public static void updateUIFetchingDataHide() {
        mLinerarLayoutMain.setVisibility(View.VISIBLE);
        mRecyclerViewData.setVisibility(View.GONE);
    }

    private void checkHasAuthorization(int authorization, final View view, final Gate gate) {
        switch (authorization) {
            case PERMISSION_GRANTED:
                startActivity(new Intent(MainScreen.this, GateDetails.class));
                break;
            case PERMISSION_DENIED:
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainScreen.this, R.style.Theme_AppCompat));
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

    public void getGatesList(final onDataLoader onDataLoader) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.101:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DigitalKeyApi service = retrofit.create(DigitalKeyApi.class);

        Call<Gates> call = service.getGates();

        call.enqueue(new Callback<Gates>() {
            @Override
            public void onResponse(Call<Gates> call, Response<Gates> response) {
                if (response.isSuccessful()) {
                    onDataLoader.onDataLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<Gates> call, Throwable t) {
                Toast.makeText(MainScreen.this, "There was a requisition problem.Try again later!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
