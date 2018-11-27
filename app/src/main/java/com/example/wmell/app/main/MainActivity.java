package com.example.wmell.app.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.wmell.app.DAO.GateResponse;
import com.example.wmell.app.DAO.GateResponseList;
import com.example.wmell.app.DAO.Gates;
import com.example.wmell.app.DAO.Historical;
import com.example.wmell.app.DAO.HistoricalUserList;
import com.example.wmell.app.DAO.Permissions;
import com.example.wmell.app.DAO.ResponsePermissionsUpdate;
import com.example.wmell.app.DAO.ResponseUpdatePermissions;
import com.example.wmell.app.R;
import com.example.wmell.app.login.LoginApplication;
import com.example.wmell.app.networking.ApiManager;
import com.example.wmell.app.networking.DigitalKeyApi;
import com.example.wmell.app.networking.ServerCallback;
import com.example.wmell.app.networking.ServerCallbackGatesByStatus;
import com.example.wmell.app.networking.ServerCallbackPermissionsStatus;
import com.example.wmell.app.networking.ServerCallbackUpdatePermissions;
import com.example.wmell.app.util.Utils;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wmell.app.util.Constants.EMAIL_PREFERENCE;
import static com.example.wmell.app.util.Constants.FROM_LOGIN_PREFERENCE;
import static com.example.wmell.app.util.Constants.GATE_DETAILS_INTENT;
import static com.example.wmell.app.util.Constants.GATE_ID;
import static com.example.wmell.app.util.Constants.GATE_KEY;
import static com.example.wmell.app.util.Constants.GATE_LAST_ACCESS;
import static com.example.wmell.app.util.Constants.GATE_NAME;
import static com.example.wmell.app.util.Constants.HOLD_ACCESS;
import static com.example.wmell.app.util.Constants.LASTACCESS_PREFERENCE;
import static com.example.wmell.app.util.Constants.PERMISSIONS_PREFERENCE;
import static com.example.wmell.app.util.Constants.PERMISSION_DENIED;
import static com.example.wmell.app.util.Constants.PERMISSION_GRANTED;
import static com.example.wmell.app.util.Constants.PERMISSION_REQUESTED;
import static com.example.wmell.app.util.Constants.USERID_PREFERENCE;
import static com.example.wmell.app.util.Constants.USERNAME_PREFERENCE;
import static com.example.wmell.app.util.Constants.USER_LOGIN_PREFERENCES;
import static com.example.wmell.app.util.Constants.USER_PREFERENCES;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ServerCallback, ServerCallbackPermissionsStatus, ServerCallbackUpdatePermissions, ServerCallbackGatesByStatus {

    private static LinearLayout mLinerarLayoutMain;


    private ProgressBar mProgressBarMain;
    private TextView mTextViewMain;
    private static RecyclerView mRecyclerViewData;
    private static RecyclerView mRecyclerViewDataHistorical;
    private static RecyclerView mRecyclerViewDataPermissions;
    private RecyclerView.LayoutManager mLayoutManager;
    public static List<GateResponse> mGatesByStatus;
    private GatesAdapter mGateAdapter;
    private HistoricalAdapter mHistoricalAdapter;
    private PermissionsAdapter mPermissionsAdapter;
    private List<Historical> mHistoricalList;
    private SharedPreferences mSharedPreferences;
    private List<ResponsePermissionsUpdate> mPermissions;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
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

        if (id == R.id.action_update) {
            setTitle(getString(R.string.title_main_activity_gates));
            updateActivity();
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
            getPermissionsOnHold(new ServerCallbackPermissionsStatus() {
                @Override
                public void onSuccess(Permissions permissions) {
                    setTitle("Permissions On Hold");
                    mRecyclerViewDataPermissions.setVisibility(View.VISIBLE);
                    mRecyclerViewData.setVisibility(View.GONE);
                    mRecyclerViewDataHistorical.setVisibility(View.GONE);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerViewDataPermissions.setLayoutManager(mLayoutManager);
                    mRecyclerViewDataPermissions.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getApplicationContext(), mRecyclerViewDataPermissions, new ClickListenerRecyclerView() {
                        @Override
                        public void onClick(View view, int position) {
                            Toast.makeText(MainActivity.this, "You have already made a request for this port! Wait for manager's approval!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));

                    mPermissions = permissions.getPermissions();
                    mPermissionsAdapter = new PermissionsAdapter(getApplicationContext(), mPermissions);
                    mRecyclerViewDataPermissions.setAdapter(mPermissionsAdapter);
                    if (mPermissions.size() == 0) {
                        Toast.makeText(MainActivity.this, "There is no On hold permissions!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFail(Throwable throwable) {
                    Toast.makeText(MainActivity.this, "It's no possible get permissions now. Try later!", Toast.LENGTH_SHORT).show();
                }

            });
        } else if (id == R.id.nav_history) {
            getUserHistorical(new ServerCallback() {
                @Override
                public void onSuccess(Gates gates) {
                }

                @Override
                public void onSuccess(com.example.wmell.app.DAO.Response response) {
                }

                @Override
                public void onSuccess(HistoricalUserList historicalUserList) {
                    setTitle(getString(R.string.title_main_activity_historical));
                    mRecyclerViewDataHistorical.setVisibility(View.VISIBLE);
                    mRecyclerViewData.setVisibility(View.GONE);
                    mRecyclerViewDataPermissions.setVisibility(View.GONE);
                    mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    mRecyclerViewDataHistorical.setLayoutManager(mLayoutManager);
                    mRecyclerViewDataHistorical.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getApplicationContext(), mRecyclerViewData, new ClickListenerRecyclerView() {
                        @Override
                        public void onClick(View view, int position) {
                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));

                    mHistoricalList = historicalUserList.getHistorical();
                    mHistoricalAdapter = new HistoricalAdapter(getApplicationContext(), mHistoricalList);
                    mRecyclerViewDataHistorical.setAdapter(mHistoricalAdapter);
                    if (mHistoricalList != null) {
                        if (mHistoricalList.size() == 0) {
                            Toast.makeText(MainActivity.this, "There is no Historical list!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "There is no Historical list!", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFail(Throwable throwable) {
                    Toast.makeText(MainActivity.this, "There was a requisition problem. Try again later!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_AppCompat));
            builder.setMessage("Do you wanna quit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences sharedPreferences = getSharedPreferences(USER_LOGIN_PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    startActivity(new Intent(MainActivity.this, LoginApplication.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initView();
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen2);
        setTitle(R.string.title_activity_main_screen2);
        mSharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        initView();
        initData();

    }


    @Override
    public void onSuccess(ResponseUpdatePermissions response) {
    }

    @Override
    public void onSuccess(GateResponseList gateResponseList) {

    }

    @Override
    public void onFail(Throwable throwable) {
    }

    @Override
    public void onSuccess(Gates gates) {
    }

    @Override
    public void onSuccess(com.example.wmell.app.DAO.Response response) {
    }

    @Override
    public void onSuccess(HistoricalUserList historicalUserList) {
    }

    @Override
    public void onSuccess(Permissions permissions) {

    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        name.setText(mSharedPreferences.getString(USERNAME_PREFERENCE, ""));
        email.setText(mSharedPreferences.getString(EMAIL_PREFERENCE, ""));

        mLinerarLayoutMain = findViewById(R.id.linearLayout_mainScreen);
        mProgressBarMain = findViewById(R.id.progressBar_MainScreen);
        mTextViewMain = findViewById(R.id.textView_mainScreen);
        mRecyclerViewData = findViewById(R.id.recyclerView_mainScreen);
        mRecyclerViewDataHistorical = findViewById(R.id.recyclerView_Historical);
        mRecyclerViewDataPermissions = findViewById(R.id.recyclerView_OnHoldPermissions);

        mLinerarLayoutMain.setVisibility(View.VISIBLE);
        mRecyclerViewData.setVisibility(View.GONE);


    }

    private void initData() {
        getPermissionsOnHold(new ServerCallbackPermissionsStatus() {
            @Override
            public void onSuccess(Permissions permissions) {
                mPermissions = permissions.getPermissions();
            }

            @Override
            public void onFail(Throwable throwable) {
                Log.v("WILLIAN", throwable.getMessage());
            }
        });

        getGatesListByStatus(new ServerCallbackGatesByStatus() {
            @Override
            public void onSuccess(GateResponseList gateResponseList) {
                mLinerarLayoutMain.setVisibility(View.GONE);
                mRecyclerViewData.setVisibility(View.VISIBLE);
                //Setting RecyclerView
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerViewData.setLayoutManager(mLayoutManager);
                mRecyclerViewData.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getApplicationContext(), mRecyclerViewData, new ClickListenerRecyclerView() {
                    @Override
                    public void onClick(View view, final int position) {
                        onClickRequest(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));

                mGatesByStatus = gateResponseList.getResult();
                mGateAdapter = new GatesAdapter(getApplicationContext(), mGatesByStatus);
                mRecyclerViewData.setAdapter(mGateAdapter);
            }

            @Override
            public void onFail(Throwable throwable) {
                mLinerarLayoutMain.setVisibility(View.GONE);
                mRecyclerViewData.setVisibility(View.VISIBLE);
            }
        }, mSharedPreferences.getInt(USERID_PREFERENCE, 0));

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

    private void getGatesListByStatus(final ServerCallbackGatesByStatus serverCallbackGatesByStatus, int user_id) {
        DigitalKeyApi service = ApiManager.getService();

        Call<GateResponseList> call = service.getGatesByStatus(user_id);
        call.enqueue(new Callback<GateResponseList>() {
            @Override
            public void onResponse(Call<GateResponseList> call, Response<GateResponseList> response) {
                if (response.isSuccessful()) {
                    GateResponseList gateResponseList = response.body();
                    serverCallbackGatesByStatus.onSuccess(gateResponseList);
                }
            }

            @Override
            public void onFailure(Call<GateResponseList> call, Throwable t) {
                serverCallbackGatesByStatus.onFail(t);
            }
        });
    }

    public void updateActivity() {

        getGatesListByStatus(new ServerCallbackGatesByStatus() {
            @Override
            public void onSuccess(GateResponseList gateResponseList) {
                mLinerarLayoutMain.setVisibility(View.GONE);
                mRecyclerViewData.setVisibility(View.VISIBLE);
                //Setting RecyclerView
                mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerViewData.setLayoutManager(mLayoutManager);
                mRecyclerViewData.addOnItemTouchListener(new MainActivity.RecyclerTouchListener(getApplicationContext(), mRecyclerViewData, new ClickListenerRecyclerView() {
                    @Override
                    public void onClick(View view, final int position) {
                        onClickRequest(position);
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));

                mGatesByStatus = gateResponseList.getResult();
                mGateAdapter = new GatesAdapter(getApplicationContext(), mGatesByStatus);
                mRecyclerViewData.setAdapter(mGateAdapter);
                if (mGatesByStatus.size() == 0) {
                    Toast.makeText(MainActivity.this, "There is no gates list!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Gates list updated!", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFail(Throwable throwable) {
                mLinerarLayoutMain.setVisibility(View.GONE);
                mRecyclerViewData.setVisibility(View.VISIBLE);
            }
        }, mSharedPreferences.getInt(USERID_PREFERENCE, 0));

    }

    public void getUserHistorical(final ServerCallback serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

        Call<HistoricalUserList> call = service.getUserHistorical(mSharedPreferences.getInt(USERID_PREFERENCE, 0));

        call.enqueue(new Callback<HistoricalUserList>() {
            @Override
            public void onResponse(Call<HistoricalUserList> call, Response<HistoricalUserList> response) {
                if (response.isSuccessful()) {
                    HistoricalUserList historicalUserList = response.body();
                    serverCallback.onSuccess(historicalUserList);
                }
            }

            @Override
            public void onFailure(Call<HistoricalUserList> call, Throwable t) {
                Toast.makeText(MainActivity.this, "There was a requisition problem. Try again later!", Toast.LENGTH_SHORT).show();
                serverCallback.onFail(t);
            }
        });
    }

    public void getPermissionsOnHold(final ServerCallbackPermissionsStatus serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

        Call<Permissions> call = service.getUserPermissions(mSharedPreferences.getInt(USERID_PREFERENCE, 0));
        call.enqueue(new Callback<Permissions>() {
            @Override
            public void onResponse(Call<Permissions> call, Response<Permissions> response) {
                if (response.isSuccessful()) {
                    Permissions responsePermissionsUpdate = response.body();
                    serverCallback.onSuccess(responsePermissionsUpdate);
                }
            }

            @Override
            public void onFailure(Call<Permissions> call, Throwable t) {
                Toast.makeText(MainActivity.this, "There was a problem about your permissions. Try again later!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void updatePermissions(final ServerCallbackUpdatePermissions serverCallback) {
        DigitalKeyApi service = ApiManager.getService();

        Call<ResponseUpdatePermissions> call = service.updateUserPermissions(mSharedPreferences.getInt(USERID_PREFERENCE, 0));
        call.enqueue(new Callback<ResponseUpdatePermissions>() {
            @Override
            public void onResponse(Call<ResponseUpdatePermissions> call, Response<ResponseUpdatePermissions> response) {
                if (response.isSuccessful()) {
                    ResponseUpdatePermissions responseUpdatePermissions = response.body();
                    serverCallback.onSuccess(responseUpdatePermissions);
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdatePermissions> call, Throwable t) {
                Toast.makeText(MainActivity.this, "There was a problem to update gates. Try again later!", Toast.LENGTH_SHORT).show();
                serverCallback.onFail(t);
            }
        });
    }

    private void onClickRequest(final int position) {
        if (mGatesByStatus.get(position).getStatus() == 1) {
            Toast.makeText(MainActivity.this, "You have already made a request for this port! Wait for manager's approval!", Toast.LENGTH_SHORT).show();
        } else if (mGatesByStatus.get(position).getStatus() == 2) {
            Intent intent = new Intent(MainActivity.this, GateDetails.class);
            intent.putExtra(GATE_NAME, mGatesByStatus.get(position).getName());
            intent.putExtra(GATE_LAST_ACCESS, mGatesByStatus.get(position).getLastAccess());
            intent.putExtra(GATE_ID, mGatesByStatus.get(position).getGateId());
            intent.putExtra(GATE_KEY, mGatesByStatus.get(position).getGateKey());
            startActivityForResult(intent, GATE_DETAILS_INTENT);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(MainActivity.this, R.style.Theme_AppCompat));
            builder.setMessage("Do you wanna request access to this port?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    makeNewRequestAccess(new ServerCallback() {
                        @Override
                        public void onSuccess(Gates gates) {
                        }

                        @Override
                        public void onSuccess(com.example.wmell.app.DAO.Response response) {
                            if (Integer.valueOf(response.getStatus()) == 200) {
                                Toast.makeText(MainActivity.this, "Successful request!", Toast.LENGTH_SHORT).show();
                                updateActivity();
                            } else {
                                Toast.makeText(MainActivity.this, "Error: " + response.getStatus() + "!\nTry again later!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onSuccess(HistoricalUserList historicalUserList) {
                        }

                        @Override
                        public void onFail(Throwable throwable) {
                            Log.v("WILLIAN", throwable.getMessage());
                            Toast.makeText(MainActivity.this, "Error to request access. Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }, position);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }

    }

    private void makeNewRequestAccess(final ServerCallback serverCallback, int position) {
        DigitalKeyApi service = ApiManager.getService();

        Call<com.example.wmell.app.DAO.Response> call = service.makeRequestAccess(getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE).getInt(USERID_PREFERENCE, 0), mGatesByStatus.get(position).getGateId(), mGatesByStatus.get(position).getName());
        call.enqueue(new Callback<com.example.wmell.app.DAO.Response>() {
            @Override
            public void onResponse(Call<com.example.wmell.app.DAO.Response> call, Response<com.example.wmell.app.DAO.Response> response) {
                if (response.isSuccessful()) {
                    com.example.wmell.app.DAO.Response response1 = response.body();
                    serverCallback.onSuccess(response1);
                }
            }

            @Override
            public void onFailure(Call<com.example.wmell.app.DAO.Response> call, Throwable t) {
                serverCallback.onFail(t);
            }
        });
    }

}