package com.example.tvd.trm_discon_recon.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
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
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter2;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter;
import com.example.tvd.trm_discon_recon.database.Database;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;
import java.util.Date;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTION_DIALOG;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVER_DATE_SUCCESS;

public class DisconListActivity extends AppCompatActivity {

    ProgressDialog progressDialog;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues> arraylist;
    ArrayList<GetSetValues> arrayList1;
    ArrayList<GetSetValues> arrayList3;
    SendingData sendingData;
    FunctionCall functionCall;
    private Discon_List_Adapter2 discon_list_adapter;
    AlertDialog discon_dialog;
    String get_Discon_date = "";
    ProgressDialog pdialog;
    Database database;
    Cursor c1, c2, c3;
    RoleAdapter roleAdapter1;
    String selected_role = "", disconnection_date = "", reading = "", count = "", login_mr_code = "";
    int dialog_position;
    TextView total_count, discon_count, remaining;
    private Toolbar toolbar;
    TextView toolbar_text;
    private SearchView searchView;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case DISCON_LIST_SUCCESS:
                    progressDialog.dismiss();
                    insertDiscondata();
                    Toast.makeText(DisconListActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    break;
                case DISCON_LIST_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(DisconListActivity.this, "Disconnection Data is not available for you!!", Toast.LENGTH_SHORT).show();
                    /************************FOLLOWING CODE NEEDS TO BE CHANGED*******************/
                    finish();
                    /*HomeFragment homeFragment = new HomeFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, homeFragment).commit();*/
                    break;
                case DISCON_SUCCESS:
                    progressDialog.dismiss();
                    Toast.makeText(DisconListActivity.this, getsetvalues.getDiscon_acc_id() + "Account Disconnected Successfully..", Toast.LENGTH_SHORT).show();
                    update_db_values();
                    discon_dialog.dismiss();
                    break;
                case DISCON_FAILURE:
                    progressDialog.dismiss();
                    Toast.makeText(DisconListActivity.this, "Disconnection Failure!!", Toast.LENGTH_SHORT).show();
                    discon_dialog.dismiss();
                    break;
                case SERVER_DATE_SUCCESS:
                    Date server_date = functionCall.selectiondate(functionCall.convertdateview(getsetvalues.getServer_date(), "dd", "/"));
                    Log.d("Debug", "Server_date" + server_date);
                    Date selected_date = functionCall.selectiondate(functionCall.convertdateview(functionCall.Parse_Date2(disconnection_date), "dd", "/"));
                    Log.d("Debug", "Got_Selected_date" + selected_date);

                    /**********Current Date is hardcoaded here later i have to pass selected_date in if condition******************/
                    /**************************************************/
                    /*************************************************/

                    Date selected_date1 = functionCall.selectiondate(functionCall.convertdateview(functionCall.Parse_Date2("2018/06/26"), "dd", "/"));
                    Log.d("Debug", "Hardcoaded" + selected_date1);

                   /* if (server_date.equals(selected_date1)) {
                        Log.d("Debug", "Date Matching..");
                        progressDialog = new ProgressDialog(DisconListActivity.this, R.style.MyProgressDialogstyle);
                        progressDialog.setTitle("Connecting To Server");
                        progressDialog.setMessage("Please Wait..");
                        progressDialog.show();
                        SendingData.Discon_List discon_list = sendingData.new Discon_List(mhandler, getsetvalues, arraylist);
                        discon_list.execute(login_mr_code, disconnection_date);
                    } else {
                        Log.d("Debug", "Date Not Matching..");
                        *//***************Datbase should be cleared if user enter more than 30 days than the system date***************//*
                        //database.delete_table();
                    }*/
                    break;
                case SERVER_DATE_FAILURE:
                    Log.d("Debug", "Server Date Failure!!");
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discon_list);
        setSupportActionBar(toolbar);

        toolbar = findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        searchView = toolbar.findViewById(R.id.search_view);
        searchView.setQueryHint("Enter Account ID..");

        toolbar_text.setText("Disconnection List");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                discon_list_adapter.getFilter().filter(newText);
                return false;
            }
        });

        database = new Database(this);
        database.open();
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        disconnection_date = sharedPreferences.getString("DISCONNECTION_DATE", "");
        login_mr_code = sharedPreferences.getString("MRCODE", "");
        Log.d("Debug", "Got Disconnection Date" + disconnection_date);


        total_count = findViewById(R.id.txt_total_count);
        discon_count =  findViewById(R.id.txt_discon_count);
        remaining = findViewById(R.id.txt_remaining);

        pdialog = new ProgressDialog(this);
        sendingData = new SendingData(this);
        functionCall = new FunctionCall();
        getsetvalues = new GetSetValues();
        recyclerview = findViewById(R.id.discon_recyclerview);

        arraylist = new ArrayList<>();
        arrayList1 = new ArrayList<>();

        getsetvalues = new GetSetValues();

        /*SendingData.Get_server_date get_server_date = sendingData.new Get_server_date(mhandler, getsetvalues);
        get_server_date.execute();*/

        progressDialog = new ProgressDialog(this, R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.Discon_List discon_list = sendingData.new Discon_List(mhandler, getsetvalues, arraylist);
        /****************MRCode and Date is hardcoaded******************/
        discon_list.execute(login_mr_code, disconnection_date);
        //discon_list.execute("54003895","2018/08/05");
    }


    public void show_disconnection_dialog(int id, final int position, ArrayList<GetSetValues> arrayList) {
        final GetSetValues getSetValues = arrayList.get(position);
        switch (id) {
            case DISCONNECTION_DIALOG:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Disconnection");
                dialog.setCancelable(false);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.discon_layout, null);
                dialog.setView(view);
                final TextView accno = view.findViewById(R.id.txt_account_no);
                final TextView arrears = view.findViewById(R.id.txt_arrears);
                final TextView prevread = view.findViewById(R.id.txt_prevread);
                final TextView name = view.findViewById(R.id.txt_name);
                final TextView address = view.findViewById(R.id.txt_address);
                final TextView discon_date = view.findViewById(R.id.txt_discon_date);
                final LinearLayout show_hide = view.findViewById(R.id.lin_show_hide);
                final EditText curread = view.findViewById(R.id.edit_curread);
                final EditText comments = view.findViewById(R.id.edit_comment);

                final Button cancel_button = view.findViewById(R.id.dialog_negative_btn);
                final Button disconnect_button = view.findViewById(R.id.dialog_positive_btn);

                final Spinner remark = view.findViewById(R.id.spiner_remark);
                arrayList3 = new ArrayList<>();
                roleAdapter1 = new RoleAdapter(arrayList3, this);
                remark.setAdapter(roleAdapter1);

                discon_dialog = dialog.create();
                discon_dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        dialog_position = position + 1;
                        //Setting status spinner
                        for (int i = 0; i < getResources().getStringArray(R.array.remark2).length; i++) {
                            GetSetValues getSetValues = new GetSetValues();
                            getSetValues.setRemark(getResources().getStringArray(R.array.remark2)[i]);
                            arrayList3.add(getSetValues);
                            roleAdapter1.notifyDataSetChanged();
                        }
                        remark.setSelection(0);

                        remark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView role = (TextView) view.findViewById(R.id.spinner_txt);
                                role.setBackgroundDrawable(null);
                                selected_role = role.getText().toString();
                                //todo I have to remove comments for the below lines for Amount Paid option
                               /* if (!selected_role.equals("Amount Paid"))
                                    show_hide.setVisibility(View.GONE);
                                else show_hide.setVisibility(View.VISIBLE);*/
                                // Toast.makeText(getActivity(), "Selected Role" + selected_role, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        //This method is used to check about if user enter any input then only disconnect button will be enabled
                        curread.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                disconnect_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                disconnect_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                disconnect_button.setEnabled(!TextUtils.isEmpty(s.toString().trim()));
                            }
                        });
                        accno.setText(getSetValues.getDiscon_acc_id());
                        arrears.setText(String.format("%s %s", getResources().getString(R.string.rupee), getSetValues.getDiscon_arrears()));
                        prevread.setText(getSetValues.getDiscon_prevread());
                        name.setText(getSetValues.getDiscon_consumer_name());
                        address.setText(getSetValues.getDiscon_add1());
                        discon_date.setText(getSetValues.getDiscon_date());
                        disconnect_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(curread.getText())) {
                                    if (!selected_role.equals("--SELECT--")) {
                                        show_hide.setVisibility(View.VISIBLE);
                                        reading = curread.getText().toString();
                                        if (Double.parseDouble(getSetValues.getDiscon_prevread()) <= Double.parseDouble(reading)) {
                                            if (!comments.getText().toString().equals("")) {
                                                progressDialog = new ProgressDialog(DisconListActivity.this, R.style.MyProgressDialogstyle);
                                                progressDialog.setTitle("Updating Disconnection");
                                                progressDialog.setMessage("Please Wait..");
                                                progressDialog.show();
                                                SendingData.Disconnect_Update disconnect_update = sendingData.new Disconnect_Update(mhandler, getSetValues);
                                                disconnect_update.execute(getSetValues.getDiscon_acc_id(), disconnection_date, reading, selected_role, comments.getText().toString());
                                            } else
                                                Toast.makeText(DisconListActivity.this, "Please Enter Comments!!", Toast.LENGTH_SHORT).show();
                                        } else
                                            functionCall.setEdittext_error(curread, "Current Reading should be greater than Previous Reading!!");
                                    } else
                                        Toast.makeText(DisconListActivity.this, "Please Select Remark!!", Toast.LENGTH_SHORT).show();

                                } else
                                    functionCall.setEdittext_error(curread, "Enter Current Reading!!");
                            }
                        });

                        cancel_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                discon_dialog.dismiss();
                            }
                        });
                    }
                });
                discon_dialog.show();
                break;
        }
    }

    public void insertDiscondata() {

        /*************************End**********************/

        ContentValues cv = new ContentValues();
        try {
            for (int i = 0; i < arraylist.size(); i++) {
                getsetvalues = arraylist.get(i);
                cv.put("ACC_ID", getsetvalues.getAcc_id());
                Log.d("Debug", "ACC_ID" + getsetvalues.getAcc_id());
                cv.put("ARREARS", getsetvalues.getArrears());
                Log.d("Debug", "ARREARS" + getsetvalues.getArrears());
                String dis_date = getsetvalues.getDis_date();

                dis_date = dis_date.substring(0, dis_date.indexOf(" "));
                Log.d("Debug", "Dis_Date" + dis_date);
                cv.put("DIS_DATE", dis_date);
                cv.put("PREVREAD", getsetvalues.getPrev_read());
                cv.put("CONSUMER_NAME", getsetvalues.getConsumer_name());
                cv.put("ADD1", getsetvalues.getAdd1());
                cv.put("LAT", getsetvalues.getLati());
                cv.put("LON", getsetvalues.getLongi());
                cv.put("MTR_READ", getsetvalues.getMtr_read());
                cv.put("FLAG", "N");
                cv.put("MTR_READING", "Null");
                cv.put("REMARK", "Null");
                database.insert_discon_data(cv);
            }
            //Once value is inserted into database then show() will be called to display values in recycler view
            //It will be called once for first time downloading data
            show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show() {
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setHasFixedSize(true);
        discon_list_adapter = new Discon_List_Adapter2(this, arrayList1, DisconListActivity.this);
        recyclerview.setAdapter(discon_list_adapter);

        Cursor cursor = database.get_Discon_Data();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                GetSetValues getSetValues = new GetSetValues();
                getSetValues.setDiscon_id(String.valueOf(cursor.getInt(cursor.getColumnIndex("_id"))));
                getSetValues.setDiscon_acc_id(String.valueOf(cursor.getString(cursor.getColumnIndex("ACC_ID"))));
                getSetValues.setDiscon_arrears(String.valueOf(cursor.getString(cursor.getColumnIndex("ARREARS"))));
                getSetValues.setDiscon_date(String.valueOf(cursor.getString(cursor.getColumnIndex("DIS_DATE"))));
                getSetValues.setDiscon_prevread(String.valueOf(cursor.getString(cursor.getColumnIndex("PREVREAD"))));
                getSetValues.setDiscon_consumer_name(String.valueOf(cursor.getString(cursor.getColumnIndex("CONSUMER_NAME"))));
                getSetValues.setDiscon_add1(String.valueOf(cursor.getString(cursor.getColumnIndex("ADD1"))));
                getSetValues.setDiscon_lat(String.valueOf(cursor.getString(cursor.getColumnIndex("LAT"))));
                getSetValues.setDiscon_lon(String.valueOf(cursor.getString(cursor.getColumnIndex("LON"))));
                getSetValues.setDiscon_mtr_read(String.valueOf(cursor.getString(cursor.getColumnIndex("MTR_READ"))));
                getSetValues.setDiscon_flag(String.valueOf(cursor.getString(cursor.getColumnIndex("FLAG"))));

                Log.d("Debug", "Discon ID" + cursor.getInt(cursor.getColumnIndex("_id")));
                Log.d("Debug", "Discon Accid" + cursor.getString(cursor.getColumnIndex("ACC_ID")));
                Log.d("Debug", "Discon Arrears" + cursor.getString(cursor.getColumnIndex("ARREARS")));
                Log.d("Debug", "Discon Date" + cursor.getString(cursor.getColumnIndex("DIS_DATE")));
                Log.d("Debug", "Discon prevread" + cursor.getString(cursor.getColumnIndex("PREVREAD")));
                Log.d("Debug", "Discon_consumer_name" + cursor.getString(cursor.getColumnIndex("CONSUMER_NAME")));
                Log.d("Debug", "Discon_add1" + cursor.getString(cursor.getColumnIndex("ADD1")));
                Log.d("Debug", "Discon_lat" + cursor.getString(cursor.getColumnIndex("LAT")));
                Log.d("Debug", "Discon_lon" + cursor.getString(cursor.getColumnIndex("LON")));
                Log.d("Debug", "Discon_Mtr_Read" + cursor.getString(cursor.getColumnIndex("MTR_READ")));
                Log.d("Debug", "Discon_flag" + cursor.getString(cursor.getColumnIndex("FLAG")));
                arrayList1.add(getSetValues);
                discon_list_adapter.notifyDataSetChanged();
            }
        }
        c2 = database.count_details();
        c2.moveToNext();
        count = c2.getString(c2.getColumnIndex("_id"));
        Log.d("Debug", "Total Count" + count);

        c3 = database.get_Total_Discon_Count();
        c3.moveToNext();
        String disconection_count = c3.getString(c3.getColumnIndex("COUNT"));
        Log.d("Debug", "Discon_count" + disconection_count);
        discon_count.setText(disconection_count);
        total_count.setText(count);
        int remaining_count = Integer.parseInt(count) - Integer.parseInt(disconection_count);
        remaining.setText(remaining_count + "");
    }

    public void update_db_values() {
        database.update_Discon_Data(dialog_position, reading, selected_role).moveToNext();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }


}
