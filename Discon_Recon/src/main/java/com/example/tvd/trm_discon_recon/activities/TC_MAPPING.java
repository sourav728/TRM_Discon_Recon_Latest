package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter4;
import com.example.tvd.trm_discon_recon.adapter.TCDetailsAdapter2;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;
import java.util.Calendar;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DTC_DETAILS_UPDATE_DIALOG;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.MRCODE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.MRCODE_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_MAPPING_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_MAPPING_SUCCESS;

public class TC_MAPPING extends AppCompatActivity {
    SendingData sendingData;
    TCDetailsAdapter2 tcDetailsAdapter2;
    GetSetValues getSetValues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arrayList;
    ArrayList<GetSetValues> mr_arrayList;
    String FEEDER_NAME = "", MRCODE = "", DATE = "", DTC_CODE = "", FEEDER = "",FDR_DATE="";
    ProgressDialog progressdialog;
    AlertDialog tc_details_update_dialog;
    FunctionCall fcall;
    Toolbar toolbar;
    TextView toolbar_text;
    SearchView searchView;
    Spinner mrcode;
    RoleAdapter4 roleAdapter4;
    String SUBDIV = "", MR_CODE = "";

    //************************************************************************************************************
    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case TC_DETAILS_SUCCESS:
                    SendingData.MRCODE mrcode = sendingData.new MRCODE(handler, getSetValues, mr_arrayList, roleAdapter4);
                    mrcode.execute(SUBDIV);
                    break;

                case TC_DETAILS_FAILURE:
                    Toast.makeText(TC_MAPPING.this, "Data not Found!!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case MRCODE_SUCCESS:
                    progressdialog.dismiss();
                    Toast.makeText(TC_MAPPING.this, "Success", Toast.LENGTH_SHORT).show();
                    break;

                case MRCODE_FAILURE:
                    progressdialog.dismiss();
                    Toast.makeText(TC_MAPPING.this, "Data not Found!!", Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case TC_MAPPING_SUCCESS:
                    Toast.makeText(TC_MAPPING.this, "DTC Details Updated..", Toast.LENGTH_SHORT).show();
                    tc_details_update_dialog.dismiss();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    break;

                case TC_MAPPING_FAILURE:
                    Toast.makeText(TC_MAPPING.this, "Update Failure!!", Toast.LENGTH_SHORT).show();
                    tc_details_update_dialog.dismiss();
                    break;

            }
            return false;
        }
    });

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tc__mapping);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("DTC Details");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint("Enter DTC Code..");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tcDetailsAdapter2.getFilter().filter(newText);
                return false;
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        FEEDER = sharedPreferences.getString("FEEDER_NAME", "");
        FEEDER_NAME = FEEDER.substring(FEEDER.lastIndexOf('-') + 2);
        FDR_DATE = sharedPreferences.getString("FDR_FETCH_DATE", "");
        MR_CODE = sharedPreferences.getString("MRCODE", "");
        Log.d("Debug", "MRCODE" + MRCODE);
        SUBDIV = MR_CODE.substring(0, 6);


        fcall = new FunctionCall();
        sendingData = new SendingData(this);
        recyclerview = findViewById(R.id.dtc_recyclerview);
        arrayList = new ArrayList<>();
        mr_arrayList = new ArrayList<>();
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        mrcode = findViewById(R.id.spinner_mrcode);
        roleAdapter4 = new RoleAdapter4(mr_arrayList, this);
        mrcode.setAdapter(roleAdapter4);
        roleAdapter4.notifyDataSetChanged();

        progressdialog = new ProgressDialog(TC_MAPPING.this, R.style.MyProgressDialogstyle);
        progressdialog.setCancelable(false);
        progressdialog.setTitle("Fetching TC Details");
        progressdialog.setMessage("Please Wait.......");
        progressdialog.show();
        tcDetailsAdapter2 = new TCDetailsAdapter2(arrayList, this, getSetValues, TC_MAPPING.this);
        recyclerview.setAdapter(tcDetailsAdapter2);

        SendingData.Send_Feeder_Name send_feeder_name = sendingData.new Send_Feeder_Name(handler, getSetValues, arrayList, tcDetailsAdapter2);
        send_feeder_name.execute(FEEDER_NAME, fcall.Parse_Date6(FDR_DATE));
    }

    //***************************************************************************************************************************************
    public void show_tc_details_update_dialog2(int id, int position, ArrayList<GetSetValues> arrayList) {
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case DTC_DETAILS_UPDATE_DIALOG:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("DTC and MR Tagging");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.tc_tagging, null);
                dialog.setView(view);

                final TextView tc_name = view.findViewById(R.id.txt_tcname);
                final TextView tc_code = view.findViewById(R.id.txt_tccode);
                final TextView date = view.findViewById(R.id.txt_date);

                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);
                final Button update_button = view.findViewById(R.id.dialog_positive_btn);
                final ImageView Sel_Date = view.findViewById(R.id.sel_date);

                final Spinner mrcode = view.findViewById(R.id.set_mrcode);
                arrayList = new ArrayList<>();
                RoleAdapter4 roleAdapter4 = new RoleAdapter4(mr_arrayList, this);
                mrcode.setAdapter(roleAdapter4);
                roleAdapter4.notifyDataSetChanged();
                tc_details_update_dialog = dialog.create();

                final ArrayList<GetSetValues> finalArrayList = arrayList;
                tc_details_update_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Sel_Date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DateDialog1();
                            }
                        });

                        mrcode.setSelection(0);
                        mrcode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView role = view.findViewById(R.id.spinner_txt);
                                role.setBackgroundDrawable(null);
                                MRCODE = role.getText().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                        DTC_CODE = getSetValues.getDTCCODE();
                        tc_code.setText(DTC_CODE);
                        tc_name.setText(getSetValues.getDTCNAME());
                        date.setText(DATE);

                        update_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!DATE.equals("")) {
                                    SendingData.DTC_MAPPING dtc_mapping = sendingData.new DTC_MAPPING(handler, getSetValues, finalArrayList);
                                    dtc_mapping.execute(MRCODE, DTC_CODE, DATE);
                                } else
                                    Toast.makeText(TC_MAPPING.this, "Please select date!!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tc_details_update_dialog.dismiss();
                            }
                        });
                    }

                    public void DateDialog1() {
                        Calendar mcalender = Calendar.getInstance();
                        int day = mcalender.get(Calendar.DAY_OF_MONTH);
                        int year = mcalender.get(Calendar.YEAR);
                        int month = mcalender.get(Calendar.MONTH);

                        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                                DATE = fcall.Parse_Date8(dd);
                                date.setText(DATE);
                            }
                        };
                        DatePickerDialog dpdialog = new DatePickerDialog(tc_details_update_dialog.getContext(), listener, year, month, day);
//                        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        mcalender.add(Calendar.MONTH, -1);
                        dpdialog.getDatePicker().setMinDate(mcalender.getTimeInMillis());
                        dpdialog.show();
                    }
                });
                tc_details_update_dialog.show();
                break;
        }
    }
}