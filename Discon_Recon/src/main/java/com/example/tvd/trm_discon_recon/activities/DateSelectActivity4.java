package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import java.util.Calendar;

public class DateSelectActivity4 extends AppCompatActivity {
    TextView from_date, to_date;
    String dd, date1, date2;
    ImageView img_from, img_to;
    TextView show_date1, show_date2;
    private int day, month, year;
    private Calendar mcalender;
    Button report;
    FunctionCall fcall;
    private Toolbar toolbar;
    TextView toolbar_text;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select4);

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

        from_date = (TextView) findViewById(R.id.txt_from_date);
        to_date = (TextView) findViewById(R.id.txt_to_dtae);
        report = (Button) findViewById(R.id.btn_Report);
        fcall = new FunctionCall();
        img_from = (ImageView) findViewById(R.id.img_fromdate);
        img_to = (ImageView) findViewById(R.id.img_todate);



        img_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });
        img_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog2();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fcall.isInternetOn(DateSelectActivity4.this)) {
                    if (!from_date.getText().toString().equals("")) {
                        if (!to_date.getText().toString().equals("")) {
                            SavePreferences("RECONNECTION_FROM_DATE", date1);
                            SavePreferences("RECONNECTION_TO_DATE", date2);
                            Intent intent = new Intent(DateSelectActivity4.this, ReconnectionReportActivity.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(DateSelectActivity4.this, "Please select To Date!!", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(DateSelectActivity4.this, "Please select From Date!!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(DateSelectActivity4.this, "Please Turn on Internet!!", Toast.LENGTH_SHORT).show();


            }
        });
    }

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
                from_date.setText(date1);
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

    public void DateDialog2() {
        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date2 = fcall.Parse_Date3(dd);
                to_date.setText(date2);
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

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
