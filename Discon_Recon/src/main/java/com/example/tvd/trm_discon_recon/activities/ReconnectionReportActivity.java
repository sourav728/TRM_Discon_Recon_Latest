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

public class ReconnectionReportActivity extends AppCompatActivity {
    Database database;
    FunctionCall fcall;
    String from_date = "", to_date = "";
    private Toolbar toolbar;
    TextView toolbar_text;
    Double percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconnection_report);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Reconnection Report");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        from_date = sharedPreferences.getString("RECONNECTION_FROM_DATE", "");
        to_date = sharedPreferences.getString("RECONNECTION_TO_DATE", "");

        Log.d("Debug", "RECONNECTION_FROM_DATE" + from_date);
        Log.d("Debug", "RECONNECTION_TO_DATE" + to_date);


        database = new Database(this);
        database.open();
        fcall = new FunctionCall();

        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);

        TextView tv0 = new TextView(this);
        tv0.setText(" Recon Date ");
        tv0.setTextColor(Color.RED);
        tv0.setTextSize(15);
        tbrow0.addView(tv0);

        TextView tv1 = new TextView(this);
        tv1.setText(" Tot_cnt ");
        tv1.setTextColor(Color.RED);
        tv1.setTextSize(15);
        tbrow0.addView(tv1);

        TextView tv3 = new TextView(this);
        tv3.setText(" Recon_cnt ");
        tv3.setTextColor(Color.RED);
        tv3.setTextSize(15);
        tbrow0.addView(tv3);
        stk.addView(tbrow0);

        TextView tv4 = new TextView(this);
        tv4.setText(" Recon_Eff ");
        tv4.setTextColor(Color.RED);
        tv4.setTextSize(15);
        tbrow0.addView(tv4);

        try {
            //getting all recon data
            Cursor c2 = database.check_recon_accid();
            int count=0;
            while (c2.moveToNext())
            {
                //checking whether any acc is reconnected or not
                String FLAG = String.valueOf(c2.getString(c2.getColumnIndex("FLAG")));
                if (FLAG.equals("Y"))
                   count++;
            }
            if (count>0)
            {
                Cursor c1 = database.get_recon_report(fcall.Parse_Date5(from_date), fcall.Parse_Date5(to_date));
                Log.d("Debug", "After Parse recon_from_date" + fcall.Parse_Date5(from_date));
                Log.d("Debug", "After Parse recon_to_date" + fcall.Parse_Date5(to_date));
                while (c1.moveToNext()) {
                    String recon_date = String.valueOf(c1.getString(c1.getColumnIndex("ReDate1")));
                    String tot_cnt = String.valueOf(c1.getString(c1.getColumnIndex("tot_cnt")));
                    String dis_cnt = String.valueOf(c1.getString(c1.getColumnIndex("Re_cnt")));

                    percentage = (100 * (Double.parseDouble(dis_cnt)) / Double.parseDouble(tot_cnt));
                    //Below code will rounding off to 2 digits
                    BigDecimal bd = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_EVEN);
                    percentage = bd.doubleValue();

                    TableRow tbrow = new TableRow(this);
                    TextView t1v = new TextView(this);
                    t1v.setText(recon_date);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setGravity(Gravity.CENTER);
                    tbrow.addView(t1v);

                    TextView t2v = new TextView(this);
                    t2v.setText(tot_cnt);
                    t2v.setTextColor(Color.BLACK);
                    t2v.setGravity(Gravity.CENTER);
                    tbrow.addView(t2v);

                    TextView t4v = new TextView(this);
                    t4v.setText(dis_cnt);
                    t4v.setTextColor(Color.BLACK);
                    t4v.setGravity(Gravity.CENTER);
                    tbrow.addView(t4v);

                    TextView t5v = new TextView(this);
                    t5v.setText(String.valueOf(percentage) + "%");
                    t5v.setTextColor(Color.BLACK);
                    t5v.setGravity(Gravity.CENTER);
                    tbrow.addView(t5v);

                    stk.addView(tbrow);
                }
            } else {
                Toast.makeText(ReconnectionReportActivity.this, "Reconnection data is not available!!", Toast.LENGTH_SHORT).show();
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
