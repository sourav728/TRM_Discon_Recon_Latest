package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.receiver.NetworkChangeReceiver;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import java.util.Calendar;

public class DateSelectActivity2 extends AppCompatActivity {
    ImageView date;
    String dd, date1, date2;
    FunctionCall fcall;
    TextView show_date;
    private int day, month, year;
    static Button reconnect;
    private Calendar mcalender;
    private Toolbar toolbar;
    TextView toolbar_text;
    static TextView tv_check_connection;
    private BroadcastReceiver mNetworkReceiver;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select2);
       /* mNetworkReceiver = new NetworkChangeReceiver();
        registerNetworkBroadcastForNougat();*/

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Select Date");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        show_date = (TextView) findViewById(R.id.txt_date);
        fcall = new FunctionCall();
        reconnect = (Button) findViewById(R.id.btn_Reconnect);



        date = (ImageView) findViewById(R.id.img_fromdate);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });
        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fcall.isInternetOn(DateSelectActivity2.this)) {
                    if (!show_date.getText().toString().equals("")) {
                        SavePreferences("RECONNECTION_DATE", date1);
                        Intent intent = new Intent(DateSelectActivity2.this, Recon_List_Activity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(DateSelectActivity2.this, "Please Select Reconnection Date!!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DateSelectActivity2.this, "Please Turn on Internet!!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    /*    public static void dialog3(boolean value)
        {
            if (value)
            {
                tv_check_connection.setText("Online");
                tv_check_connection.setBackgroundColor(Color.parseColor("#558B2F"));
                tv_check_connection.setTextColor(Color.WHITE);
                Handler handler = new Handler();
                Runnable delayrunnable = new Runnable() {
                    @Override
                    public void run() {
                        tv_check_connection.setVisibility(View.GONE);
                    }
                };
                handler.postDelayed(delayrunnable, 3000);
                reconnect.setEnabled(true);
            }
            else {
                tv_check_connection.setVisibility(View.VISIBLE);
                tv_check_connection.setText("No Internet Connection!!");
                tv_check_connection.setBackgroundColor(Color.RED);
                tv_check_connection.setTextColor(Color.WHITE);
                reconnect.setEnabled(false);
            }
        }*/
    public void DateDialog1() {
        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date1 = fcall.Parse_Date3(dd);
                show_date.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(this, listener, year, month, day);
        //it will show dates upto current date
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //below code will set calender min date to 30 days before from system date
        mcalender.add(Calendar.MONTH, -1);
        dpdialog.getDatePicker().setMinDate(mcalender.getTimeInMillis());
        dpdialog.show();
    }

  /*  private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }*/

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
