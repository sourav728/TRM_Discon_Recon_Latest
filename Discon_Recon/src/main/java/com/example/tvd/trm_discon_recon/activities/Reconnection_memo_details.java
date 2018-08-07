package com.example.tvd.trm_discon_recon.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.Recon_Memo_Adapter;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;


import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_MEMO_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECON_MEMO_SUCCESS;

public class Reconnection_memo_details extends AppCompatActivity {
    String acc_id = "", subdivision = "";
    ProgressDialog progressDialog;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    SendingData sendingData;
    FunctionCall functionCall;
    private Toolbar toolbar;
    TextView toolbar_text, date;
    Recon_Memo_Adapter recon_memo_adapter;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case RECON_MEMO_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(Reconnection_memo_details.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case RECON_MEMO_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(Reconnection_memo_details.this, "Account ID does not exists..", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reconnection_memo_details);
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        acc_id = sharedPreferences.getString("RECON_MEMO_ACC_ID", "");
        subdivision = sharedPreferences.getString("RECON_MEMO_SUBDIVISION", "");

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Recon Memo Details");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendingData = new SendingData(this);
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();

        recyclerview = (RecyclerView) findViewById(R.id.feeder_details_recyclerview);
        arraylist = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        recon_memo_adapter = new Recon_Memo_Adapter(arraylist, this, getsetvalues);
        recyclerview.setAdapter(recon_memo_adapter);

        progressDialog = new ProgressDialog(this, R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.Recon_Memo_details recon_memo_details = sendingData.new Recon_Memo_details(mhandler, getsetvalues, arraylist, recon_memo_adapter);
        recon_memo_details.execute(acc_id, subdivision);
    }
}
