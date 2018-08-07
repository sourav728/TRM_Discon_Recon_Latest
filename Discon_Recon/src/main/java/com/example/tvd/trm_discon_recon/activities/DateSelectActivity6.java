package com.example.tvd.trm_discon_recon.activities;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.service.BluetoothService;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import static com.example.tvd.trm_discon_recon.service.BluetoothService.conn;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.ANALOGICS_PRINTER_CONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.ANALOGICS_PRINTER_DISCONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.ANALOGICS_PRINTER_PAIRED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.BLUETOOTH_RESULT;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.CONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TURNED_OFF;

public class DateSelectActivity6 extends AppCompatActivity {
    FunctionCall fcall;
    private Toolbar toolbar;
    TextView toolbar_text;
    EditText acc_id, subdivision;
    Button submit;
    String printer = "";
    BluetoothAdapter deviceadapter;
    BluetoothDevice bluetoothDevice;
    public static AnalogicsThermalPrinter conn;
    public static String printer_address = "";
    Bluetooth_Printer_3inch_prof_ThermalAPI api;
    public static boolean PRINTER_CONNECT_OR_NOT = false;
    String selected_printer = "";

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case ANALOGICS_PRINTER_CONNECTED:
                    BluetoothService.printerconnected = true;
                    break;

                case ANALOGICS_PRINTER_DISCONNECTED:
                    BluetoothService.printerconnected = false;
                    handler.sendEmptyMessage(ANALOGICS_PRINTER_PAIRED);
                    break;

                case ANALOGICS_PRINTER_PAIRED:
                    try {
                        fcall.logStatus(bluetoothDevice.getAddress());
                        printer_address = bluetoothDevice.getAddress();
                        conn.openBT(bluetoothDevice.getAddress());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    break;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_select6);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        selected_printer = sharedPreferences.getString("PRINTER", "");

        api = new Bluetooth_Printer_3inch_prof_ThermalAPI();
        /****************Printer name is hardcoaded*************/
        // printer = "GPT";
        //printer = "ALG";
        printer = selected_printer;

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar_text = toolbar.findViewById(R.id.toolbar_title);
        toolbar_text.setText("Select Date");
        toolbar.setNavigationIcon(R.drawable.back);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fcall = new FunctionCall();
        acc_id = findViewById(R.id.edit_acc_id);
        subdivision = findViewById(R.id.edit_subdivision);
        submit = findViewById(R.id.btn_submit);


        //**********************************************************************************************
        if (!printer.equals("ALG")) {
            if (!isMyServiceRunning(BluetoothService.class)) {
                fcall.logStatus("Service not running");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fcall.logStatus("Service Started");
                        Intent bluetoothservice = new Intent(DateSelectActivity6.this, BluetoothService.class);
                        startService(bluetoothservice);
                    }
                }, 500);
            } else fcall.logStatus("Service running");
        } else {
            conn = new AnalogicsThermalPrinter();
            startBroadcast();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fcall.isInternetOn(DateSelectActivity6.this)) {
                    if (!acc_id.getText().toString().equals("")) {
                        if (!subdivision.getText().toString().equals("")) {
                            SavePreferences("RECON_MEMO_ACC_ID", acc_id.getText().toString());
                            SavePreferences("RECON_MEMO_SUBDIVISION", subdivision.getText().toString());
                            Intent intent = new Intent(DateSelectActivity6.this, Reconnection_memo_details.class);
                            startActivity(intent);
                        } else subdivision.setError("Please Enter Subdivision!!");
                    } else acc_id.setError("Please Enter Account Id!!");
                } else Toast.makeText(DateSelectActivity6.this, "Please Turn on Internet!!", Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    protected void onDestroy() {
        if (!printer.equals("ALG")) {
            if (isMyServiceRunning(BluetoothService.class)) {
                fcall.logStatus("Service Stopped");
                Intent bluetoothservice = new Intent(DateSelectActivity6.this, BluetoothService.class);
                stopService(bluetoothservice);
            } else fcall.logStatus("Service Not Started");
        } else {
            unregisterReceiver(Receiver);
            if (BluetoothService.printerconnected)
                try {
                    conn.closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
        }
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        registerReceiver(mReceiver, new IntentFilter(BLUETOOTH_RESULT));
        registerReceiver(Receiver, new IntentFilter(BLUETOOTH_RESULT));
        super.onResume();
    }

    private void startBroadcast() {
        deviceadapter = BluetoothAdapter.getDefaultAdapter();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                deviceadapter.startDiscovery();
                IntentFilter filter = new IntentFilter();
                filter.addAction(BluetoothDevice.ACTION_FOUND);
                filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
                filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
                registerReceiver(Receiver, filter);
            }
        }, 2500);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FunctionCall functionCall = new FunctionCall();
            String status = intent.getStringExtra("message");
            switch (status) {
                case CONNECTED:
                    String printer = intent.getStringExtra("printer");
                    functionCall.showtoast(DateSelectActivity6.this, printer + " Bluetooth Printer Connected");
                    PRINTER_CONNECT_OR_NOT = true;
                    BluetoothService.printerconnected = true;
                    functionCall.logStatus("Handler Printer Broadcast receiving Connected from service");
                    break;

                case DISCONNECTED:
                    functionCall.showtoast(DateSelectActivity6.this, "Bluetooth Printer Disconnected");
                    functionCall.logStatus("Handler Printer Broadcast receiving Disconnected from service");
                    PRINTER_CONNECT_OR_NOT = false;
                    BluetoothService.printerconnected = false;
                    break;

                case TURNED_OFF:
                    functionCall.showtoast(DateSelectActivity6.this, "Please Turn On the printer and proceed...");
                    PRINTER_CONNECT_OR_NOT = false;
                    BluetoothService.printerconnected = false;
                    functionCall.logStatus("Handler Printer Broadcast receiving TurnedOff from service");
                    break;
            }
        }
    };
    private BroadcastReceiver Receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                handler.sendEmptyMessage(ANALOGICS_PRINTER_CONNECTED);
                fcall.showtoast(DateSelectActivity6.this, "Analogics Printer Connected");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                handler.sendEmptyMessage(ANALOGICS_PRINTER_DISCONNECTED);
                fcall.showtoast(DateSelectActivity6.this, "Analogics Printer Disconnected");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if (StringUtils.startsWithIgnoreCase(device.getName(), "AT3TV3")) {
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        bluetoothDevice = device;
                        handler.sendEmptyMessage(ANALOGICS_PRINTER_PAIRED);
                    }
                } else if (StringUtils.startsWithIgnoreCase(device.getName(), "AT2TV3")) {
                    if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                        bluetoothDevice = device;
                        handler.sendEmptyMessage(ANALOGICS_PRINTER_PAIRED);
                    }
                }
            }
        }
    };

    //***********************************************************************************************************************
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
