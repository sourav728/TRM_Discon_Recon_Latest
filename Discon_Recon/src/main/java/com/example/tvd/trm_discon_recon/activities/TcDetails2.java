package com.example.tvd.trm_discon_recon.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.TcDetailsAdapter;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.FEEDER_DETAILS_UPDATE_DIALOG;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE_DIALOG;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_UPDATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_UPDATE_SUCCESS;


public class TcDetails2 extends AppCompatActivity {
    ProgressDialog progressDialog;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    SendingData sendingData;
    FunctionCall functionCall;
    private Toolbar toolbar;
    TextView toolbar_text, Date, display_subdivision;
    String fdr_details_date = "", subdivision = "", parsed_date = "";
    AlertDialog tc_details_update_dialog;
    private TcDetailsAdapter tcDetailsAdapter;
    String cur_reading = "";
    String fdr_fetch_subdiv_code = "", fdr_fetch_date = "", fdr_type = "", feeder = "";
    private SearchView searchView;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TC_DETAILS_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(TcDetails2.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case TC_DETAILS_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(TcDetails2.this, "Failure..", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case TC_UPDATE_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(TcDetails2.this, "Updated successfully..", Toast.LENGTH_SHORT).show();
                    tc_details_update_dialog.dismiss();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    break;
                case TC_UPDATE_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(TcDetails2.this, "Failure!!", Toast.LENGTH_SHORT).show();
                    tc_details_update_dialog.dismiss();
                    finish();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc_details2);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("TC Details");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint("Enter Tc Code..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tcDetailsAdapter.getFilter().filter(newText);
                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        fdr_fetch_subdiv_code = sharedPreferences.getString("FDR_FETCH_SUB_DIVCODE", "");
        fdr_fetch_date = sharedPreferences.getString("FDR_FETCH_DATE", "");
        feeder = sharedPreferences.getString("FEEDER_TYPE", "");
        fdr_type = feeder.substring(0, 11);

        sendingData = new SendingData(this);
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();

        Date = findViewById(R.id.txt_date);
        display_subdivision = findViewById(R.id.txt_subdiv);
        Date.setText(fdr_fetch_date);
        display_subdivision.setText(fdr_fetch_subdiv_code);

        recyclerview = findViewById(R.id.feeder_details_recyclerview);
        arraylist = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);

        tcDetailsAdapter = new TcDetailsAdapter(arraylist, this, getsetvalues, TcDetails2.this);
        recyclerview.setAdapter(tcDetailsAdapter);

        progressDialog = new ProgressDialog(this, R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();

        parsed_date = functionCall.Parse_Date6(fdr_fetch_date);
        Log.d("Debug", "PARSED_DATE" + parsed_date);
        SendingData.Send_Tc_details send_tc_details = sendingData.new Send_Tc_details(mhandler, getsetvalues, arraylist, tcDetailsAdapter);
        send_tc_details.execute(fdr_fetch_subdiv_code, functionCall.Parse_Date6(fdr_fetch_date), fdr_type);
    }

    public void show_tc_details_update_dialog(int id, final int position, ArrayList<GetSetValues> arrayList) {
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case TC_DETAILS_UPDATE_DIALOG:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("TC UPDATE");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.tc_details_update_layout, null);
                dialog.setView(view);
                final TextView tc_name = view.findViewById(R.id.txt_tcname);
                final TextView tc_code = view.findViewById(R.id.txt_tccode);
                final TextView tc_ir = view.findViewById(R.id.txt_tc_ir);
                final EditText current_reading = view.findViewById(R.id.edit_current_reading);
                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);
                final Button update_button = view.findViewById(R.id.dialog_positive_btn);

                tc_details_update_dialog = dialog.create();
                tc_details_update_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        tc_name.setText(getSetValues.getTcname());
                        tc_code.setText(getSetValues.getTc_code());
                        current_reading.setText(getSetValues.getTcfr());
                        tc_ir.setText(getSetValues.getTcir());
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
                                if (Double.parseDouble(getSetValues.getTcir()) <= Double.parseDouble(cur_reading)) {
                                    progressDialog = new ProgressDialog(TcDetails2.this, R.style.MyProgressDialogstyle);
                                    progressDialog.setTitle("Updating Tc details..");
                                    progressDialog.setMessage("Please Wait..");
                                    progressDialog.show();
                                    SendingData.TC_Update tc_update = sendingData.new TC_Update(mhandler, getSetValues);
                                    tc_update.execute(getSetValues.getTc_code(), parsed_date, current_reading.getText().toString());
                                } else
                                    Toast.makeText(TcDetails2.this, "Current Reading should be greater than Previous Reading!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tc_details_update_dialog.dismiss();
                            }
                        });
                    }
                });
                tc_details_update_dialog.show();
                break;
        }
    }

}
