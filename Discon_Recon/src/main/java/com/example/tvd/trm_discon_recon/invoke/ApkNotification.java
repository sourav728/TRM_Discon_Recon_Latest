package com.example.tvd.trm_discon_recon.invoke;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import com.example.tvd.trm_discon_recon.LoginActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.splash_screen;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DLG_APK_UPDATE_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;

public class ApkNotification extends BroadcastReceiver {
    FunctionCall functionCalls;
    GetSetValues getSetValues;
    SendingData sendingData;
    String curr_version="";
    Context Notification_context;
    SharedPreferences settings;
    LoginActivity loginActivity;
    String server_apk_version="";
    private static Handler handler = null;
    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case LOGIN_SUCCESS:
                        functionCalls.logStatus("Server Version: "+getSetValues.getApp_version());
                        if (functionCalls.compare(curr_version, getSetValues.getApp_version()))
                            notification(getContext().getApplicationContext());
                        break;

                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Notification_context = context;
        loginActivity = new LoginActivity();
        functionCalls = new FunctionCall();
        getSetValues = new GetSetValues();
        sendingData = new SendingData(context);
        settings = context.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF",MODE_PRIVATE);
        server_apk_version = sharedPreferences.getString("APP_VERSION","");
        functionCalls.logStatus("Apk Notification Current Time: "+functionCalls.currentRecpttime());

        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            curr_version = pInfo.versionName;
            functionCalls.logStatus("Current Version: "+curr_version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (functionCalls.checkInternetConnection(context)) {
            functionCalls.logStatus("Checking for newer version of Discon_Recon application...");
            SendingData.Login mrLogin = sendingData.new Login(handler, getSetValues);
            mrLogin.execute(settings.getString("MRCODE", ""), settings.getString("DEVICE_ID", ""), settings.getString("PASSWORD", ""));
        } else functionCalls.logStatus("No Internet Connection...");

    }

    private Context getContext() {
        return this.Notification_context;
    }

    private void notification(Context context) {
        Intent intent = new Intent(context, splash_screen.class);
         //loginActivity.showdialog(DLG_APK_UPDATE_SUCCESS);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
        //build notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.app_update)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.app_icon))
                        .setContentTitle("Disconnection Reconnection")
                        .setContentText("New version Available"+" "+server_apk_version)
                        .setDefaults(Notification.DEFAULT_ALL) // must requires VIBRATE permission
                        .setPriority(NotificationCompat.PRIORITY_HIGH) //must give priority to High, Max which will considered as heads-up notification
                        .setAutoCancel(true);

        // Gets an instance of the NotificationManager service
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //to post your notification to the notification bar with a id. If a notification with same id already exists, it will get replaced with updated information.
        notificationManager.notify(0, builder.build());
    }
}
