package com.example.tvd.trm_discon_recon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.activities.DateSelectActivity;
import com.example.tvd.trm_discon_recon.activities.DateSelectActivity2;
import com.example.tvd.trm_discon_recon.activities.SettingActivity;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.fragments.HomeFragment;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView mrname, mrcode, logout;
    String Mrname = "", Mrcode = "", selected_date2 = "";
    FunctionCall fcall;
    GetSetValues getSetValues;
    SendingData sendingData;
    Database database;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    ProgressDialog progress;
    String user_role = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = new Database(this);
        database.open();

        //todo for deleting database records less than 3 days
        // database.delete_data();

        progress = new ProgressDialog(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        replaceFragment(R.id.nav_home);
        fcall = new FunctionCall();
        getSetValues = new GetSetValues();
        sendingData = new SendingData(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        Mrname = sharedPreferences.getString("MRNAME", "");
        Mrcode = sharedPreferences.getString("MRCODE", "");
        user_role = sharedPreferences.getString("USER_ROLE", "");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem disconnect = menu.findItem(R.id.nav_disconnect);
        MenuItem reconnect = menu.findItem(R.id.nav_reconnect);
        if (user_role.equals("MR")) {
            disconnect.setVisible(true);
            reconnect.setVisible(true);
        } else {
            disconnect.setVisible(false);
            reconnect.setVisible(false);
        }
        View view = navigationView.getHeaderView(0);
        mrname = view.findViewById(R.id.nav_mrname);
        mrcode = view.findViewById(R.id.nav_mrcode);
        mrname.setText(Mrname);
        mrcode.setText(Mrcode);
        NavigationView logout_view = findViewById(R.id.nav_view2);
        logout_view.setNavigationItemSelectedListener(this);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
        if (user_role.equals("MR"))
            item.setVisible(false);
        else item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        onNavigationItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment;
        int id = item.getItemId();
        replaceFragment(id);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(int id) {
        Fragment fragment = null;
        if (id == R.id.nav_home) {
            fragment = new HomeFragment();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            finish();
        } else if (id == R.id.nav_disconnect) {
            Intent intent = new Intent(MainActivity.this, DateSelectActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_reconnect) {
            Intent intent = new Intent(MainActivity.this, DateSelectActivity2.class);
            startActivity(intent);
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

    }

    public Database get_discon_Database() {
        return this.database;
    }

}
