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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter3;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_FETCH_SUCCESS;

public class FeederName extends AppCompatActivity {
    Spinner fdr_spinner;
    SendingData sendingData;
    ArrayList<GetSetValues> arrayList;
    GetSetValues getSetValues;
    RoleAdapter3 roleAdapter3;
    FunctionCall fcall;
    private Toolbar toolbar;
    Button search;
    TextView toolbar_text;
    ProgressDialog progressDialog;
    String main_role = "", MRCODE = "", SUBDIV = "", DATE = "";

    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FDR_FETCH_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(FeederName.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case FDR_FETCH_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(FeederName.this, "No Data Found..", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            return false;
        }
    });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder_name);

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
        SUBDIV = sharedPreferences.getString("FDR_FETCH_SUB_DIVCODE", "");
        DATE = sharedPreferences.getString("FDR_FETCH_DATE", "");
        MRCODE = sharedPreferences.getString("MRCODE", "");

        search = findViewById(R.id.btn_Search);
        fcall = new FunctionCall();
        fdr_spinner = findViewById(R.id.spinner);

        sendingData = new SendingData(this);
        getSetValues = new GetSetValues();
        arrayList = new ArrayList<>();

        roleAdapter3 = new RoleAdapter3(arrayList, this);
        fdr_spinner.setAdapter(roleAdapter3);

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

        progressDialog = new ProgressDialog(this, R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.Feeder_Name_Fetch feeder_name_fetch = sendingData.new Feeder_Name_Fetch(mhandler, getSetValues, arrayList, roleAdapter3);
        feeder_name_fetch.execute(SUBDIV, fcall.Parse_Date6(DATE));

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SavePreferences("MRCODE", MRCODE);
                SavePreferences("FEEDER_NAME", main_role);
                SavePreferences("FDR_FETCH_DATE", DATE);
                Intent intent = new Intent(FeederName.this, TC_MAPPING.class);
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

