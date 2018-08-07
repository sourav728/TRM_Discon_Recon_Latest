package com.example.tvd.trm_discon_recon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;

public class LoginActivity extends AppCompatActivity {
    Button login;
    FunctionCall fcall;
    String getMrcode="",getpassword="";
    EditText mrcode,password;
    LayoutInflater inflater;
    View layout;
    ProgressDialog progressdialog;
    GetSetValues getsetvalues;
    SendingData sendingdata;
    private final Handler mhandler;
    {
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case LOGIN_SUCCESS:
                        SavePreferences("MRCODE",getsetvalues.getMrcode());
                        SavePreferences("MRNAME",getsetvalues.getMrname());
                        SavePreferences("SUBDIVCODE",getsetvalues.getSubdivcode());
                        progressdialog.dismiss();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        //Below code is for custom toast message
                        inflater = getLayoutInflater();
                        layout = inflater.inflate(R.layout.toast1,
                                (ViewGroup) findViewById(R.id.toast_layout));
                        ImageView imageView = (ImageView) layout.findViewById(R.id.image);
                        imageView.setImageResource(R.drawable.tick);
                        TextView textView = (TextView) layout.findViewById(R.id.text);
                        textView.setText("Success");
                        textView.setTextSize(20);
                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                        //end of custom toast coding
                        break;
                    case  LOGIN_FAILURE:
                        progressdialog.dismiss();
                        //below code is for custom toast
                        inflater = getLayoutInflater();
                        layout = inflater.inflate(R.layout.toast,
                                (ViewGroup) findViewById(R.id.toast_layout));
                        ImageView imageView1 = (ImageView) layout.findViewById(R.id.image);
                        imageView1.setImageResource(R.drawable.invalid);
                        TextView textView1 = (TextView) layout.findViewById(R.id.text);
                        textView1.setText("Invalid Credentials!!");
                        textView1.setTextSize(20);
                        Toast toast1 = new Toast(getApplicationContext());
                        toast1.setGravity(Gravity.BOTTOM, 0, 0);
                        toast1.setDuration(Toast.LENGTH_SHORT);
                        toast1.setView(layout);
                        toast1.show();
                        //end of custom toast code
                        mrcode.setText("");
                        password.setText("");
                        mrcode.requestFocus();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        initialize();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (fcall.isInternetOn(LoginActivity.this))
               {
                   getMrcode = mrcode.getText().toString();
                   String DeviceID ="352514083077473";
                   /*Code: 54003892
                   Date: 27/04/2018
                   Password: 123123*/
                   getpassword = password.getText().toString();
                   if (mrcode.getText().length()<=0)
                   {
                       mrcode.setError("Please Enter MR code!!");
                   }else if (password.getText().length()<=0)
                   {
                       password.setError("Please Enter password!!");
                   }else {
                       progressdialog = new ProgressDialog(LoginActivity.this, R.style.MyProgressDialogstyle);
                       progressdialog.setTitle("Connecting To Server");
                       progressdialog.setMessage("Please Wait..");
                       progressdialog.show();
                       SendingData.Login login = sendingdata.new Login(mhandler,getsetvalues);
                       login.execute(getMrcode,DeviceID,getpassword);
                   }
               }else Toast.makeText(LoginActivity.this, "Please Connect to Internet!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void initialize()
    {
        getsetvalues = new GetSetValues();
        sendingdata = new SendingData();
        login = (Button) findViewById(R.id.login_btn);
        fcall = new FunctionCall();
        mrcode = (EditText) findViewById(R.id.edit_mrcode);
        password = (EditText) findViewById(R.id.edit_password);
    }
    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
