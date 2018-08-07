package com.example.tvd.trm_discon_recon.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.Feeder_details_Adapter;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_UPDATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FDR_UPDATE_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_UPDATE_DIALOG;

public class FeederDetails extends AppCompatActivity {
    ProgressDialog progressDialog;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arrayList;
    SendingData sendingData;
    FunctionCall functionCall;
    private Toolbar toolbar;
    TextView toolbar_text, date, display_subdivision;
    String fdr_details_date = "", subdivision = "", parsed_date = "", cur_reading = "";
    AlertDialog feeder_details_update_dialog;
    private SearchView searchView;
    private Feeder_details_Adapter feeder_details_adapter;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case FEEDER_DETAILS_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(FeederDetails.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case FEEDER_DETAILS_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(FeederDetails.this, "Failure..", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case FDR_UPDATE_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(FeederDetails.this, "Updated successfully..", Toast.LENGTH_SHORT).show();
                    feeder_details_update_dialog.dismiss();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    break;
                case FDR_UPDATE_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(FeederDetails.this, "Failure!!", Toast.LENGTH_SHORT).show();
                    feeder_details_update_dialog.dismiss();
                    finish();
                    break;
            }
            return false;
        }
    });

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder_details);


        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        fdr_details_date = sharedPreferences.getString("FDR_DETAILS_DATE", "");
        subdivision = sharedPreferences.getString("SUB_DIVCODE", "");

        toolbar = findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Feeder Details");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint("Enter FDR Code..");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                feeder_details_adapter.getFilter().filter(newText);
                return false;
            }
        });

        date = findViewById(R.id.txt_date);
        display_subdivision = findViewById(R.id.txt_subdiv);

        date.setText(fdr_details_date);
        display_subdivision.setText(subdivision);


        sendingData = new SendingData(this);
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();

        recyclerview = findViewById(R.id.feeder_details_recyclerview);
        arrayList = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        feeder_details_adapter = new Feeder_details_Adapter(arrayList, this, getsetvalues, FeederDetails.this);
        recyclerview.setAdapter(feeder_details_adapter);

        progressDialog = new ProgressDialog(this, R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        parsed_date = functionCall.Parse_Date6(fdr_details_date);
        Log.d("Debug", "PARSED_DATE" + parsed_date);
        SendingData.SendFeeder_Details sendFeeder_details = sendingData.new SendFeeder_Details(mhandler, getsetvalues, arrayList, feeder_details_adapter);
        sendFeeder_details.execute(subdivision, parsed_date);
    }

    public void show_fdr_details_update_dialog(int id, final int position, ArrayList<GetSetValues> arrayList) {
        final AlertDialog alertDialog;
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case FEEDER_DETAILS_UPDATE_DIALOG:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("FDR UPDATE");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.feeder_details_update_layout, null);
                dialog.setView(view);
                final TextView fdr_code = view.findViewById(R.id.txt_fdr_code);
                final TextView fdr_ir = view.findViewById(R.id.txt_fdr_ir);
                final EditText current_reading = view.findViewById(R.id.edit_current_reading);
                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);
                final Button update_button = view.findViewById(R.id.dialog_positive_btn);

                feeder_details_update_dialog = dialog.create();
                feeder_details_update_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        fdr_code.setText(getSetValues.getFdr_code());
                        current_reading.setText(getSetValues.getFdr_fr());
                        fdr_ir.setText(getSetValues.getFdr_ir());
                       /* current_reading.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                update_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                update_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                update_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }
                        });*/
                        update_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cur_reading = current_reading.getText().toString();
                                if (Double.parseDouble(getSetValues.getFdr_ir()) < Double.parseDouble(cur_reading)) {
                                    progressDialog = new ProgressDialog(FeederDetails.this, R.style.MyProgressDialogstyle);
                                    progressDialog.setTitle("Updating Feeder details..");
                                    progressDialog.setMessage("Please Wait..");
                                    progressDialog.show();
                                    SendingData.FDR_Fr_Update fdr_fr_update = sendingData.new FDR_Fr_Update(mhandler, getSetValues);
                                    fdr_fr_update.execute(getSetValues.getFdr_code(), parsed_date, current_reading.getText().toString());
                                } else
                                    Toast.makeText(FeederDetails.this, "Current Reading should be greater than Previous Reading!!",
                                            Toast.LENGTH_SHORT).show();

                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                feeder_details_update_dialog.dismiss();
                            }
                        });
                    }
                });
                feeder_details_update_dialog.show();
                break;
        }
    }

}
