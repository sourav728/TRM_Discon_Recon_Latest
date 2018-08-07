package com.example.tvd.trm_discon_recon.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DLG_PRINT;


public class SettingActivity extends AppCompatActivity {

    ArrayList<GetSetValues> arrayList;
    RoleAdapter roleAdapter;
    String selected_role = "";
    Button save;
    private Toolbar toolbar;
    TextView toolbar_text;
    Button printer_change;
    FunctionCall fcall;
    String mrcode="", mrname="",subdiv_code="",device_id="",subdiv_name="", current_version="",selected_printer="";
    TextView set_mrname,set_mrcode,set_suvdiv_code,set_subdiv_name,set_device_id,set_app_version,set_bt_printer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        set_mrname = findViewById(R.id.txt_mrname);
        set_mrcode = findViewById(R.id.txt_mrcode);
        set_suvdiv_code = findViewById(R.id.txt_subdiv_code);
        set_subdiv_name = findViewById(R.id.txt_subdiv_name);
        set_device_id = findViewById(R.id.txt_deviceid);
        set_app_version = findViewById(R.id.txt_appversion);
        set_bt_printer = findViewById(R.id.txt_btprinter);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        mrcode = sharedPreferences.getString("MRCODE", "");
        mrname = sharedPreferences.getString("MRNAME", "");
        subdiv_code = sharedPreferences.getString("SUBDIVCODE", "");
        device_id = sharedPreferences.getString("DEVICE_ID", "");
        subdiv_name = sharedPreferences.getString("SUBDIVNAME", "");
       // current_version = sharedPreferences.getString("CURRENT_VERSION","");
        selected_printer = sharedPreferences.getString("PRINTER", "");

        set_mrname.setText(mrname);
        set_mrcode.setText(mrcode);
        set_suvdiv_code.setText(subdiv_code);
        set_subdiv_name.setText(subdiv_name);
        set_device_id.setText(device_id);
        //set_app_version.setText(current_version);
        if (!selected_printer.equals(""))
        set_bt_printer.setText(selected_printer);
        else set_bt_printer.setText("NA");

        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            current_version = packageInfo.versionName;
            set_app_version.setText(current_version);
            // SavePreferences("CURRENT_VERSION", current_version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        fcall = new FunctionCall();
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Select Printer");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        printer_change = findViewById(R.id.btn_printer);
        //printer_spinner = findViewById(R.id.spinner);


      /*  for (int i = 0; i < getResources().getStringArray(R.array.printer).length; i++) {
            GetSetValues getSetValues = new GetSetValues();
            getSetValues.setRemark(getResources().getStringArray(R.array.printer)[i]);
            arrayList.add(getSetValues);
            roleAdapter.notifyDataSetChanged();
        }
        printer_spinner.setSelection(0);

        printer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView role = (TextView) view.findViewById(R.id.spinner_txt);
                role.setBackgroundDrawable(null);
                selected_role = role.getText().toString();
                //Toast.makeText(SettingActivity.this, "Selected Role" + selected_role, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selected_role.equals("--SELECT--")) {
                    SavePreferences("PRINTER", selected_role);
                    Toast.makeText(SettingActivity.this, "Printer changed successfully..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(SettingActivity.this, "Please Select Printer!!", Toast.LENGTH_SHORT).show();

            }
        });*/

        printer_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog(DLG_PRINT);
            }
        });

    }

    public void showdialog(int id) {
        switch (id) {
            case DLG_PRINT:
                AlertDialog.Builder print_dlg = new AlertDialog.Builder(this);
                print_dlg.setTitle("Select Printer");
                print_dlg.setCancelable(false);
              /*  RelativeLayout dlg_linear = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.printer_layout, null);
                print_dlg.setView(dlg_linear);*/
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.printer_layout, null);
                print_dlg.setView(view);

                final Spinner printer_spinner = view.findViewById(R.id.spinner);
                final Button ok_button = view.findViewById(R.id.dialog_positive_btn);
                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);

                arrayList = new ArrayList<>();
                roleAdapter = new RoleAdapter(arrayList, this);
                printer_spinner.setAdapter(roleAdapter);

                final AlertDialog print_dialog = print_dlg.create();
                print_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        for (int i = 0; i < getResources().getStringArray(R.array.printer).length; i++) {
                            GetSetValues getSetValues = new GetSetValues();
                            getSetValues.setRemark(getResources().getStringArray(R.array.printer)[i]);
                            arrayList.add(getSetValues);
                            roleAdapter.notifyDataSetChanged();
                        }
                        printer_spinner.setSelection(0);

                        printer_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView role = (TextView) view.findViewById(R.id.spinner_txt);
                                role.setBackgroundDrawable(null);
                                selected_role = role.getText().toString();
                                Toast.makeText(SettingActivity.this, "Selected Role" + selected_role, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        ok_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!selected_role.equals("--SELECT--")) {
                                    SavePreferences("PRINTER", selected_role);
                                    Toast.makeText(SettingActivity.this, "Printer changed successfully..", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else
                                    Toast.makeText(SettingActivity.this, "Please Select Printer!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                print_dialog.dismiss();
                            }
                        });
                    }
                });
                print_dialog.show();
        }
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}