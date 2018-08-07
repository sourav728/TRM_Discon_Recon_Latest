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
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ReconnectionEfficiency extends AppCompatActivity {
    private TextView mTextViewInfo;
    private TextView mTextViewPercentage;
    private ProgressBar mProgressBar;
    Database database;
    FunctionCall fcall;
    String from_date = "", to_date = "";
    private Toolbar toolbar;
    TextView toolbar_text;
    double percentage;
    String tot_cnt = "", tot_amt = "", dis_cnt = "", dis_amt = "";
    private int mProgressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconnection_efficiency);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        mTextViewPercentage = (TextView) findViewById(R.id.tv_percentage);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);

        toolbar_text.setText("Reconnection Efficiency");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        from_date = sharedPreferences.getString("RECON_EFFICIENCY_FROM_DATE", "");
        to_date = sharedPreferences.getString("RECON_EFFICIENCY_TO_DATE", "");

        Log.d("Debug", "RECON_EFFICIENCY_FROM_DATE" + from_date);
        Log.d("Debug", "RECON_EFFICIENCY_TO_DATE" + to_date);


        database = new Database(this);
        database.open();
        fcall = new FunctionCall();

        Cursor c1 = database.get_recon_report(fcall.Parse_Date5(from_date), fcall.Parse_Date5(to_date));
        Log.d("Debug", "After Parse recon_from_date" + fcall.Parse_Date5(from_date));
        Log.d("Debug", "After Parse recon_to_date" + fcall.Parse_Date5(to_date));
        if (c1.getCount() > 0) {
            while (c1.moveToNext()) {
                tot_cnt = String.valueOf(c1.getString(c1.getColumnIndex("tot_cnt")));
                dis_cnt = String.valueOf(c1.getString(c1.getColumnIndex("Re_cnt")));
            }
            percentage = (100 * (Double.parseDouble(dis_cnt)) / Double.parseDouble(tot_cnt));
            //Below code will rounding off to 2 digits
            BigDecimal bd = new BigDecimal(percentage).setScale(2, RoundingMode.HALF_EVEN);
            percentage = bd.doubleValue();
            mTextViewPercentage.setText(percentage + " " + "%");
            Integer convert_to_int = (int) percentage;
            mProgressBar.setProgress(convert_to_int);
        } else {
            Toast.makeText(ReconnectionEfficiency.this, "Reconnection data is not available!!", Toast.LENGTH_SHORT).show();
            finish();
        }

    }
}
