package com.example.tvd.trm_discon_recon.activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DisconnectionReportActivity extends AppCompatActivity {
    Database database;
    FunctionCall fcall;
    String from_date = "", to_date = "";
    private Toolbar toolbar;
    TextView toolbar_text;
    double percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disconnection_report);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Disconnection Report");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        from_date = sharedPreferences.getString("DISON_FROM_DATE", "");
        to_date = sharedPreferences.getString("DISCON_TO_DATE", "");
        Log.d("Debug", "DISCONNECTION_FROM_DATE" + from_date);
        Log.d("Debug", "DISCONNECTION_TO_DATE" + to_date);

        database = new Database(this);
        database.open();
        fcall = new FunctionCall();

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(" Diss Date ");
        tv0.setTextColor(Color.RED);
        tv0.setTextSize(15);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(" Tot_cnt ");
        tv1.setTextColor(Color.RED);
        tv1.setTextSize(15);
        tbrow0.addView(tv1);

        TextView tv2 = new TextView(this);
        tv2.setText(" Tot_amt ");
        tv2.setTextColor(Color.RED);
        tv2.setTextSize(15);
        tbrow0.addView(tv2);

        TextView tv3 = new TextView(this);
        tv3.setText(" Dis_cnt ");
        tv3.setTextColor(Color.RED);
        tv3.setTextSize(15);
        tbrow0.addView(tv3);

        TextView tv4 = new TextView(this);
        tv4.setText(" Dis Amt ");
        tv4.setTextColor(Color.RED);
        tv4.setTextSize(15);
        tbrow0.addView(tv4);

        TextView tv5 = new TextView(this);
        tv5.setText(" Dis eff ");
        tv5.setTextColor(Color.RED);
        tv5.setTextSize(15);
        tbrow0.addView(tv5);

        stk.addView(tbrow0);

        try {
            Cursor c2 = database.check_discon_accid();
            int count = 0;
            while (c2.moveToNext()) {
                //checking whether any acc id is reconnected or not
                String FLAG = String.valueOf(c2.getString(c2.getColumnIndex("FLAG")));
                if (FLAG.equals("Y"))
                    count++;
            }
            if (count > 0) {
                Cursor c1 = database.get_report(fcall.Parse_Date5(from_date), fcall.Parse_Date5(to_date));
                Log.d("Debug", "After Parse discon_from_date" + fcall.Parse_Date5(from_date));
                Log.d("Debug", "After Parse discon_to_date" + fcall.Parse_Date5(to_date));
                while (c1.moveToNext()) {
                    String dis_date = String.valueOf(c1.getString(c1.getColumnIndex("DisDate1")));
                    Log.d("Debug", "Dis_Date " + dis_date);
                    String tot_cnt = String.valueOf(c1.getString(c1.getColumnIndex("tot_cnt")));
                    Log.d("Debug", "Tot_count " + tot_cnt);
                    String tot_amt = String.valueOf(c1.getString(c1.getColumnIndex("tot_amt")));
                    Log.d("Debug", "Tot_amt " + tot_amt);
                    String dis_cnt = String.valueOf(c1.getString(c1.getColumnIndex("Dis_cnt")));
                    Log.d("Debug", "Dis_cnt " + dis_cnt);
                    String dis_amt = String.valueOf(c1.getString(c1.getColumnIndex("Dis_Amt")));
                    Log.d("Debug", "Dis_amt " + dis_amt);

                    percentage = (100 * (Double.parseDouble(dis_cnt)) / Double.parseDouble(tot_cnt));
                    //Below code will rounding off to 2 digits
                    BigDecimal bd = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_EVEN);
                    percentage = bd.doubleValue();

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(dis_date);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    tbrow.addView(t1v);

                    TextView t2v = new TextView(this);
                    t2v.setText(tot_cnt);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.CENTER);
                    tbrow.addView(t2v);

                    TextView t3v = new TextView(this);
                    t3v.setText(tot_amt);
                    t3v.setTextColor(Color.BLACK);
                    t3v.setGravity(Gravity.CENTER);
                    tbrow.addView(t3v);

                    TextView t4v = new TextView(this);
                    t4v.setText(dis_cnt);
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(this);
                    t5v.setText(dis_amt);
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.CENTER);
                    tbrow.addView(t5v);

                    TextView t6v = new TextView(this);
                    t6v.setText(String.valueOf(percentage) + "%");
                    t6v.setTextColor(Color.BLACK);
                    t6v.setGravity(Gravity.CENTER);
                    tbrow.addView(t6v);

                    stk.addView(tbrow);
                }
            } else {
                Toast.makeText(DisconnectionReportActivity.this, "Disconnection data is not available!!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}


