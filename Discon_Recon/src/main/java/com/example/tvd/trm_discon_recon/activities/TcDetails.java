package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter2;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_SUCCESS;

public class TcDetails extends AppCompatActivity {
    Spinner fdr_spinner;
    Button submit;
    SendingData sendingData;
    ArrayList<GetSetValues> arrayList;
    GetSetValues getSetValues;
    String fdr_fetch_subdiv_code = "", fdr_fetch_date = "";
    RoleAdapter2 roleAdapter;
    FunctionCall fcall;
    private Toolbar toolbar;
    Button search;
    TextView toolbar_text, Date, display_subdivision;
    ProgressDialog progressDialog;
    String main_role = "";
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FDR_FETCH_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(TcDetails.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case FDR_FETCH_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(TcDetails.this, "No Data Found..", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            return false;
        }
    });

    @SuppressLint({"SetTextI18n", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc_details);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Feeder List");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        fdr_fetch_subdiv_code = sharedPreferences.getString("FDR_FETCH_SUB_DIVCODE", "");
        fdr_fetch_date = sharedPreferences.getString("FDR_FETCH_DATE", "");

        search = findViewById(R.id.btn_Search);
        fcall = new FunctionCall();
        fdr_spinner = findViewById(R.id.spinner);
        submit = findViewById(R.id.btn_Search);
        Date = findViewById(R.id.txt_date);
        display_subdivision = findViewById(R.id.txt_subdiv);
        Date.setText(fdr_fetch_date);
        display_subdivision.setText(fdr_fetch_subdiv_code);


        sendingData = new SendingData(this);
        getSetValues = new GetSetValues();
        arrayList = new ArrayList<>();

        roleAdapter = new RoleAdapter2(arrayList, this);
        fdr_spinner.setAdapter(roleAdapter);

        fdr_spinner.setSelection(0);

        fdr_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView tvrole2 = view.findViewById(R.id.spinner_txt);
                String role = tvrole2.getText().toString();
                main_role = role;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Log.d("Debug", "Date_Parse" + fcall.Parse_Date6(fdr_fetch_date));

        progressDialog = new ProgressDialog(this, R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.FDR_Fetch fdr_fetch = sendingData.new FDR_Fetch(mhandler, getSetValues, arrayList, roleAdapter);
        fdr_fetch.execute(fdr_fetch_subdiv_code, fcall.Parse_Date6(fdr_fetch_date));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePreferences("FEEDER_TYPE", main_role);
                Intent intent = new Intent(TcDetails.this, TcDetails2.class);
                startActivity(intent);
            }
        });
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
