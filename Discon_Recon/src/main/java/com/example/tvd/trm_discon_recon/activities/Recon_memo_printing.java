package com.example.tvd.trm_discon_recon.activities;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.analogics.thermalAPI.Bluetooth_Printer_3inch_prof_ThermalAPI;
import com.analogics.thermalprinter.AnalogicsThermalPrinter;
import com.example.tvd.trm_discon_recon.MainActivity;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.service.BluetoothService;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.lvrenyang.io.Pos;
import com.ngx.BluetoothPrinter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutorService;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.PRINT_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.PRINT_SUCCESS;

public class Recon_memo_printing extends AppCompatActivity {
    Bluetooth_Printer_3inch_prof_ThermalAPI api;
    ImageView date;
    String dd, date1, date2;
    private int day, month, year;
    private Calendar mcalender;
    FunctionCall fcall;
    BluetoothPrinter mBtp;
    Pos mPos = BluetoothService.mPos;
    ExecutorService es = BluetoothService.es;
    float yaxis = 0;
    FunctionCall functionCall;
    Button print;
    EditText rcpt_no, bill_amount, mobile_no;
    TextView rcpt_date;
    AnalogicsThermalPrinter conn = DateSelectActivity6.conn;
    private ArrayList<String> res;
    TextView text_subdiv, text_acc_id, text_rrno, text_name, text_address, text_tariff, text_recon_date, text_so, text_dr_fee,
            text_mrcode, text_readdate;
    String memo_subdiv = "", memo_acc_id = "", memo_rrno = "", memo_name = "", memo_address = "", memo_tariff = "", memo_recon_date = "",
            memo_bill_amt = "", memo_section = "", memo_dr_fee = "", memo_rcptdate = "", memo_rcptno = "", memo_mrcode = "", memo_readdate = "", memo_mblno = "";
    SendingData sendingData;
    String selected_printer = "";
    String rep_address_1="",rep_address_2="",address="",regex="",splchar="";
    ArrayList<String>addresslist;
    private final Handler mhandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PRINT_SUCCESS:
                    Toast.makeText(Recon_memo_printing.this, "Print Status updated successfully!!", Toast.LENGTH_SHORT).show();
                    break;
                case PRINT_FAILURE:
                    Toast.makeText(Recon_memo_printing.this, "Print Status Updation Failure!!", Toast.LENGTH_SHORT).show();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recon_memo_printing);

        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        selected_printer = sharedPreferences.getString("PRINTER", "");
        addresslist = new ArrayList<>();

        functionCall = new FunctionCall();
        sendingData = new SendingData(this);
        text_acc_id = findViewById(R.id.txt_acc_id);
        text_subdiv = findViewById(R.id.txt_subdiv_code);
        text_rrno = findViewById(R.id.txt_rrno);
        text_name = findViewById(R.id.txt_name);
        text_address = findViewById(R.id.txt_address);
        text_tariff = findViewById(R.id.txt_tariff);
        text_recon_date = findViewById(R.id.txt_recon_date);
        text_so = findViewById(R.id.txt_so);
        text_dr_fee = findViewById(R.id.txt_dr_fee);
        text_mrcode = findViewById(R.id.txt_mrcode);
        text_readdate = findViewById(R.id.txt_readdate);
        rcpt_date = findViewById(R.id.edt_rcpt_date);
        rcpt_no = findViewById(R.id.edt_rcpt_nbr);
        bill_amount = findViewById(R.id.edt_bill_amt);
        mobile_no = findViewById(R.id.edt_mblno);
        date = findViewById(R.id.img_date);
        fcall = new FunctionCall();
        Intent intent = getIntent();

        memo_acc_id = intent.getStringExtra("ACCT_ID");
        memo_rrno = intent.getStringExtra("LEG_RRNO");
        memo_name = intent.getStringExtra("CONSUMER_NAME");
        memo_address = intent.getStringExtra("ADD1");
        memo_tariff = intent.getStringExtra("TARIFF");
        memo_recon_date = intent.getStringExtra("RE_DATE");
        memo_section = intent.getStringExtra("SO");
        memo_dr_fee = intent.getStringExtra("DR_FEE");
        memo_subdiv = intent.getStringExtra("subdivcode");
        memo_mrcode = intent.getStringExtra("MRCODE");
        memo_readdate = intent.getStringExtra("READ_DATE");


        Log.d("Debug", "Memo_Subdiv" + memo_subdiv);

        text_acc_id.setText(memo_acc_id);
        text_subdiv.setText(memo_subdiv);
        text_rrno.setText(memo_rrno);
        text_name.setText(memo_name);
        text_address.setText(memo_address);
        text_tariff.setText(memo_tariff);
        text_recon_date.setText(functionCall.Parse_Date4(memo_recon_date));
        text_so.setText(memo_section);
        text_dr_fee.setText(memo_dr_fee);
        text_mrcode.setText(memo_mrcode);
        text_readdate.setText(functionCall.Parse_Date4(memo_readdate));


        res = new ArrayList<>();
        api = new Bluetooth_Printer_3inch_prof_ThermalAPI();
        functionCall = new FunctionCall();
        print = findViewById(R.id.btn_print);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                memo_bill_amt = bill_amount.getText().toString();
                memo_rcptdate = rcpt_date.getText().toString();
                memo_rcptno = rcpt_no.getText().toString();
                memo_mblno = mobile_no.getText().toString();


                address = memo_address;

                fcall.splitString(address, 40, addresslist);
                if (addresslist.size() > 0) {
                    rep_address_1 = "  "+addresslist.get(0);
                    if (addresslist.size() > 1) {
                        rep_address_2 = "  "+addresslist.get(1);
                    }
                }

                if (!memo_bill_amt.equals("")) {
                    if (!memo_rcptdate.equals("")) {
                        if (!memo_rcptno.equals("")) {
                            if (!memo_mblno.equals("")) {
                                if (BluetoothService.printerconnected) {
                                    if (selected_printer.equals("ALG"))
                                        printanalogics();
                                    else es.submit(new TaskPrint(mPos));
                                } else Toast.makeText(Recon_memo_printing.this, "Please Connect to Printer and proceed!!", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(Recon_memo_printing.this, "Please Enter memo mobile no!!", Toast.LENGTH_SHORT).show();

                        } else Toast.makeText(Recon_memo_printing.this, "Please Enter memo recptno!", Toast.LENGTH_SHORT).show();

                    } else Toast.makeText(Recon_memo_printing.this, "Please Enter memo recpt date!!", Toast.LENGTH_SHORT).show();

                } else Toast.makeText(Recon_memo_printing.this, "Please Enter Paid Amount!!", Toast.LENGTH_SHORT).show();

              /*  if (BluetoothService.printerconnected) {
                    if (selected_printer.equals("ALG"))
                        printanalogics();
                    else es.submit(new TaskPrint(mPos));
                } else
                    Toast.makeText(Recon_memo_printing.this, "Please Connect to Printer and proceed!!", Toast.LENGTH_SHORT).show();*/

                // printngx();
                //printanalogics();
            }
        });

        mcalender = Calendar.getInstance();
        day = mcalender.get(Calendar.DAY_OF_MONTH);
        year = mcalender.get(Calendar.YEAR);
        month = mcalender.get(Calendar.MONTH);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateDialog1();
            }
        });
    }

    public void DateDialog1() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                dd = (year + "-" + (month + 1) + "-" + dayOfMonth);
                date1 = fcall.Parse_Date3(dd);
                rcpt_date.setText(date1);
            }
        };
        DatePickerDialog dpdialog = new DatePickerDialog(this, listener, year, month, day);
        //it will show dates upto current date
        dpdialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        //below code will set calender min date to 30 days before from system date
       /* mcalender.add(Calendar.DATE, -30);
        dpdialog.getDatePicker().setMinDate(mcalender.getTimeInMillis());*/
        dpdialog.show();
    }

    private class TaskPrint implements Runnable {
        Pos pos;

        private TaskPrint(Pos pos) {
            this.pos = pos;
        }

        @Override
        public void run() {
            final boolean bPrintResult = PrintGpt();
            final boolean bIsOpened = pos.GetIO().IsOpened();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), bPrintResult ? getResources().getString(R.string.printsuccess)
                            : getResources().getString(R.string.printfailed), Toast.LENGTH_SHORT).show();
                    if (bIsOpened) {
                        yaxis = 0;
                        SendingData.Print_Update print_update = sendingData.new Print_Update(mhandler);
                        print_update.execute(memo_acc_id,memo_bill_amt,memo_rcptno);
                    }
                }
            });
        }

        /*************************************GPT PRINTING*************************/
        public boolean PrintGpt() {
            boolean bPrintResult;
            int pre_normal_text_length = 21;
            pos.POS_FeedLine();
            pos.POS_S_Align(1);
            printdoubleText("HUBLI ELECTRICITY SUPPLY COMPANY LTD");
            printdoubleText("RECONNECTION MEMO");
            printText("");
            pos.POS_S_Align(0);
            printText(functionCall.space("  Reconnection Date", pre_normal_text_length) + ":" + " " + functionCall.Parse_Date4(memo_recon_date));
            printText(functionCall.space("  Sub Division", pre_normal_text_length) + ":" + " " + memo_subdiv);
            printText(functionCall.space("  Section", pre_normal_text_length) + ":" + " " + memo_section);
            printdoubleText(functionCall.space("  Account ID", pre_normal_text_length) + ":" + " " + memo_acc_id);
            printText(functionCall.space("  RR NO", pre_normal_text_length) + ":" + " " + memo_rrno);
            pos.POS_S_Align(1);
            printText("Name and Address");
            pos.POS_S_Align(0);
            printText("  " + memo_name);
            printText( rep_address_1);
            printText( rep_address_2);
            printText(functionCall.space("  Mobile No", pre_normal_text_length) + ":" + " " + memo_mblno);
            printText(functionCall.space("  Tariff", pre_normal_text_length) + ":" + " " + memo_tariff);
            printText(functionCall.space("  Reading Date", pre_normal_text_length) + ":" + " " + functionCall.Parse_Date4(memo_readdate));
            printText(functionCall.space("  MR Code", pre_normal_text_length) + ":" + " " + memo_mrcode);
            printText(functionCall.space("  Paid Amount", pre_normal_text_length) + ":" + " " + memo_bill_amt);
            printText(functionCall.space("  Receipt No", pre_normal_text_length) + ":" + " " + memo_rcptno);
            printText(functionCall.space("  Receipt Date", pre_normal_text_length) + ":" + " " + functionCall.Parse_Date7(memo_rcptdate));
            printdoubleText(functionCall.space("  D & R Fee", pre_normal_text_length) + ":" + " " + memo_dr_fee + ".00");

            pos.POS_FeedLine();
            pos.POS_FeedLine();
            printText(functionCall.space("                                 sign",pre_normal_text_length));

            printText("---------------------------------------------");
            printText("  " + "NOTE: Pay bill before due date to avoid");
            printText("  " + "Dis-Reconnection charges.");
            pos.POS_FeedLine();
            pos.POS_FeedLine();
            bPrintResult = pos.GetIO().IsOpened();
            return bPrintResult;
        }

        private void printText(String msg) {
            pos.POS_S_TextOut(msg + "\r\n", 0, 0, 0, 0, 4);
        }

        private void printdoubleText(String msg) {
            pos.POS_S_TextOut(msg + "\r\n", 0, 0, 1, 0, 4);
        }
    }


    //*****************************************Analogics print***************************************************************
    public void printanalogics() {
        StringBuilder stringBuilder = new StringBuilder();
        analogics_header__double_print(functionCall.aligncenter("HUBLI ELECTRICITY SUPPLY COMPANY LTD", 38), 4);
        analogics_header__double_print(functionCall.aligncenter("RECONNECTION MEMO", 38), 4);
        analogicsprint(functionCall.space("", 12), 4);
        analogicsprint(functionCall.space(" Reconnection Date", 12) + ":" + " " + functionCall.Parse_Date4(memo_recon_date), 4);
        analogicsprint(functionCall.space(" Sub Division", 12) + ":" + " " + memo_subdiv, 4);
        analogicsprint(functionCall.space(" Section", 12) + ":" + " " + memo_section, 4);
        analogics_double_print(functionCall.space(" Account ID", 12) + ":" + " " + memo_acc_id, 4);
        analogicsprint(functionCall.space(" RRNO", 12) + ":" + " " + memo_rrno, 4);
        analogics_48_print(functionCall.aligncenter("Name and Address", 48), 6);
        analogics_48_print("  "+memo_name, 3);
        analogics_48_print(rep_address_1, 3);
        analogics_48_print(rep_address_2, 3);
        analogicsprint(functionCall.space(" Mobile No", 12) + ":" + " " + memo_mblno, 4);
        analogicsprint(functionCall.space(" Tariff", 12) + ":" + " " + memo_tariff, 4);
        analogicsprint(functionCall.space(" Reading Date", 12) + ":" + " " + functionCall.Parse_Date4(memo_readdate), 4);
        analogicsprint(functionCall.space(" MR Code", 12) + ":" + " " + memo_mrcode, 4);
        analogicsprint(functionCall.space(" Paid Amount", 12) + ":" + " " + memo_bill_amt, 4);
        analogicsprint(functionCall.space(" Receipt No", 12) + ":" + " " + memo_rcptno, 4);
        analogicsprint(functionCall.space(" Receipt Date", 12) + ":" + " " + functionCall.Parse_Date7(memo_rcptdate), 4);
        analogics_double_print(functionCall.space(" D&R Fee", 12) + ":" + " " + memo_dr_fee + ".00", 4);

        analogicsprint(functionCall.space("", 12), 4);
        stringBuilder.setLength(0);
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        analogicsprint(functionCall.space("                    sign", 12) +   " " , 4);
        analogics_48_print("-----------------------------------------------", 3);
        analogics_48_print("  NOTE: Pay bill before due date to avoid", 3);
        analogics_48_print("  Dis-Reconnection charges.", 3);

        stringBuilder.setLength(0);
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        stringBuilder.append("\n");
        analogicsprint(stringBuilder.toString(), 4);
    }

    public void analogicsprint(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_30_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void analogics_double_print(String Printdata, int feed_line) {
        conn.printData(api.font_Double_Height_On_VIP());
        analogicsprint(Printdata, feed_line);
        conn.printData(api.font_Double_Height_Off_VIP());
    }

    public void analogics_header_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_38_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void analogics_header__double_print(String Printdata, int feed_line) {
        conn.printData(api.font_Double_Height_On_VIP());
        analogics_header_print(Printdata, feed_line);
        conn.printData(api.font_Double_Height_Off_VIP());
    }

    public void analogics_48_print(String Printdata, int feed_line) {
        conn.printData(api.font_Courier_48_VIP(Printdata));
        text_line_spacing(feed_line);
    }

    public void text_line_spacing(int space) {
        conn.printData(api.variable_Size_Line_Feed_VIP(space));
    }
}
