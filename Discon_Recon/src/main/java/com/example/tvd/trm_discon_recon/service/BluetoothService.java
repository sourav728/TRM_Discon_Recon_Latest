package com.example.tvd.trm_discon_recon.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.Canvas;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.lvrenyang.io.IOCallBack;
import com.lvrenyang.io.Pos;
import com.ngx.BluetoothPrinter;
import org.apache.commons.lang3.StringUtils;
import java.io.IOException;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.BLUETOOTH_RESULT;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.CONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTED;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TURNED_OFF;

public class BluetoothService extends Service implements IOCallBack {
    public static final int PRINTER_CONNECTED = 10;
    public static final int PRINTER_DISCONNECTED = 11;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    FunctionCall functionCall;
    String rep_deviceconnected;
    public static boolean printerconnected = false;
    boolean ngxprinter = false, ngxservice = false, analogics = false, goojprt = false, broadcast = false,
            devicepaired = false, service_destroyed = false;
    String printer = "";

    public static BluetoothPrinter mBtp = BluetoothPrinter.INSTANCE;
    public static AnalogicsThermalPrinter conn = new AnalogicsThermalPrinter();

    private String mConnectedDeviceName = "";
    public static String printer_address = "";
    public static SharedPreferences mSp;

    BTPrinting mBt = new BTPrinting();
    public static Canvas mCanvas = new Canvas();
    public static Pos mPos = new Pos();
    public static ExecutorService es = Executors.newScheduledThreadPool(30);
    BluetoothService mActivity;
    Bluetooth_Printer_3inch_prof_ThermalAPI api;
    String selected_printer="";

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothPrinter.STATE_CONNECTED:
                            printer_address = bluetoothDevice.getAddress();
                            functionCall.logStatus("Connected to: " + mConnectedDeviceName);
                            sendBroadcastMessage("NGX", DISCONNECTED);
                            ngxprinter = true;
                            printerconnected = true;
                            break;
                        case BluetoothPrinter.STATE_CONNECTING:
                            functionCall.logStatus("Connecting");
                            break;
                        case BluetoothPrinter.STATE_LISTEN:
                            functionCall.logStatus("State Listening");
                            break;
                        case BluetoothPrinter.STATE_NONE:
                            functionCall.logStatus("Not Connected");
                            if (!broadcast)
                                startBroadcast();
                            functionCall.logStatus("Handler Printer Turned Off: " + rep_deviceconnected);
                            sendBroadcastMessage("", TURNED_OFF);
                            ngxprinter = false;
                            break;
                    }
                    break;

                case BluetoothPrinter.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = "Device: " + msg.getData().getString(BluetoothPrinter.DEVICE_NAME);
                    break;

                case BluetoothPrinter.MESSAGE_STATUS:
                    functionCall.logStatus("Status: " + msg.getData().getString(BluetoothPrinter.STATUS_TEXT));
                    break;

                case PRINTER_CONNECTED:
                    if (!goojprt)
                        analogics = true;
                    printerconnected = true;
                    printer_address = bluetoothDevice.getAddress();
                    functionCall.logStatus("Handler Printer Connected: " + rep_deviceconnected);
                    functionCall.logStatus("Handler Printer Broadcast sending Connected from service");
                    if (printer.equals("GPT")) {
                        sendBroadcastMessage("Phi", CONNECTED);
                    } else if (printer.equals("ALG")) {
                        sendBroadcastMessage("Analogics", CONNECTED);
                    }
                    break;

                case PRINTER_DISCONNECTED:
                    analogics = false;
                    goojprt = false;
                    devicepaired = false;
                    if (printerconnected) {
                        printerconnected = false;
                        functionCall.logStatus("Handler Printer Disconnected: " + rep_deviceconnected);
                        sendBroadcastMessage("", DISCONNECTED);
                    } else {
                        functionCall.logStatus("Handler Printer Turned Off: " + rep_deviceconnected);
                        sendBroadcastMessage("", TURNED_OFF);
                    }
                    if (!broadcast) {
                        if (!service_destroyed) {
                            startBroadcast();
                        }
                    }
                    break;

                default:
                    break;
            }
            return false;
        }
    });

    public BluetoothService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        selected_printer = sharedPreferences.getString("PRINTER","");

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        functionCall = new FunctionCall();
         // printer = "ALG";
//        printer = "GPT";
//        printer = "NGX";
         printer = selected_printer;
        api = new Bluetooth_Printer_3inch_prof_ThermalAPI();

        if (printer.equals("GPT")) {
            mActivity = this;
            mPos.Set(mBt);
            mCanvas.Set(mBt);
            mBt.SetCallBack(this);
        }
        if (printer.equals("ALG")) {
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    startBroadcast();
                }
            }, 10000);
        }
    }

    private void sendBroadcastMessage(String printer_msg, String message) {
        Intent intent = new Intent();
        intent.setAction(BLUETOOTH_RESULT);
        intent.putExtra("message", message);
        intent.putExtra("printer", printer_msg);
        sendBroadcast(intent);
    }

    private void startBroadcast() {
        functionCall.logStatus("Broadcast Receiver Starting");
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getPairedDevices();

        return Service.START_STICKY;
    }

    private void getPairedDevices() {
        Set<BluetoothDevice> pairedDevice = mBluetoothAdapter.getBondedDevices();
        if (pairedDevice.size() > 0) {
            try {
                for (BluetoothDevice device : pairedDevice) {
                    printerConnection(device);
                }
            } catch (Exception e) {
                if (!devicepaired) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            functionCall.logStatus("Device Paired but Broadcast starting after 1 seconds...");
                            startBroadcast();
                        }
                    }, 1000);
                }
            }
        } else {
            if (!devicepaired) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        functionCall.logStatus("Device Paired but Broadcast starting after 1 seconds...");
                        startBroadcast();
                    }
                }, 1000);
            }
        }
    }

    private void printerConnection(final BluetoothDevice device) {
        switch (printer) {
            case "ALG":
                if (StringUtils.startsWithIgnoreCase(device.getName(), "AT2TV3")) {
                    bluetoothDevice = device;
                    rep_deviceconnected = device.getName();
                    devicepaired = true;
                    functionCall.logStatus("Bluetooth device: " + device.getName());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                functionCall.logStatus("Analogics Bluetooth device connection name: " + device.getName());
                                functionCall.logStatus("Analogics Bluetooth device connection address: " + device.getAddress());
                                conn.openBT(bluetoothDevice.getAddress());
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }, 500);
                }
                break;

            case "NGX":
                devicepaired = true;
                setNgxprinter(device);
                break;

            case "GPT":
                if (StringUtils.startsWithIgnoreCase(device.getName(), "BP301-2")) {
                    bluetoothDevice = device;
                    rep_deviceconnected = device.getName();
                    devicepaired = true;
                    functionCall.logStatus("Bluetooth device: " + device.getName());
                    es.submit(new TaskOpen(mBt, device.getAddress(), mActivity));
                }
                break;
        }
    }

    public static BluetoothPrinter getngxprinter() {
        return mBtp;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service_destroyed = true;
        if (printerconnected) {
            if (analogics) {
                try {
                    conn.closeBT();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (ngxprinter) {
                mBtp.onActivityPause();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBtp.onActivityDestroy();
                    }
                }, 500);
            }
            if (goojprt) {
                es.submit(new TaskClose(mBt));
            }
        }
        if (broadcast)
            unregisterReceiver(mReceiver);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void OnOpen() {
        goojprt = true;
        mHandler.sendEmptyMessage(PRINTER_CONNECTED);
    }

    @Override
    public void OnOpenFailed() {
        goojprt = false;
        mHandler.sendEmptyMessage(PRINTER_DISCONNECTED);
    }

    @Override
    public void OnClose() {
        goojprt = false;
        mHandler.sendEmptyMessage(PRINTER_DISCONNECTED);
    }

    private void setNgxprinter(final BluetoothDevice device) {
        if (!ngxservice) {
            bluetoothDevice = device;
            ngxservice = true;
            mSp = PreferenceManager.getDefaultSharedPreferences(this);
            try {
                mBtp.initService(this, mHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (StringUtils.startsWithIgnoreCase(device.getName(), "BTP")) {
                    mBtp.setPreferredPrinter(device.getAddress());
                    rep_deviceconnected = device.getName();
                    functionCall.logStatus("Bluetooth device: " + device.getName());
                } else {
                    functionCall.logStatus("NGX Bluetooth device not paired or switched On");
                }
            }
        }, 1000);
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            broadcast = true;
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    functionCall.logStatus("ACTION_FOUND_PAIRED: " + device.getName());
                    printerConnection(device);
                } else functionCall.logStatus("ACTION_FOUND_UNPAIRED: " + device.getName());
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                functionCall.logStatus("ACTION_CONNECTED: " + device.getName());
                mHandler.sendEmptyMessage(PRINTER_CONNECTED);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                functionCall.logStatus("ACTION_DISCOVERY_FINISHED");
                if (!printerconnected)
                    mBluetoothAdapter.startDiscovery();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                functionCall.logStatus("ACTION_DISCOVERY_STARTED");
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                functionCall.logStatus("ACTION_DISCONNECTED: " + device.getName());
                mHandler.sendEmptyMessage(PRINTER_DISCONNECTED);
                printerconnected = false;
                mBluetoothAdapter.startDiscovery();
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                    functionCall.logStatus("Paired Device: " + device.getName());
                }
            }
        }
    };

    private class TaskOpen implements Runnable {
        BTPrinting bt;
        String address;
        Context context;

        private TaskOpen(BTPrinting bt, String address, Context context) {
            this.bt = bt;
            this.address = address;
            this.context = context;
        }

        @Override
        public void run() {
            bt.Open(address, context);
        }
    }

    private class TaskClose implements Runnable {
        BTPrinting bt;

        private TaskClose(BTPrinting bt) {
            this.bt = bt;
        }

        @Override
        public void run() {
            bt.Close();
        }
    }
}
