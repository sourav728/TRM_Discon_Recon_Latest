package com.example.tvd.trm_discon_recon.invoke;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.example.tvd.trm_discon_recon.LoginActivity;
import com.example.tvd.trm_discon_recon.adapter.Feeder_details_Adapter;
import com.example.tvd.trm_discon_recon.adapter.Recon_Memo_Adapter;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter2;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter3;
import com.example.tvd.trm_discon_recon.adapter.RoleAdapter4;
import com.example.tvd.trm_discon_recon.adapter.TCCode_Adapter;
import com.example.tvd.trm_discon_recon.adapter.TCDetailsAdapter2;
import com.example.tvd.trm_discon_recon.adapter.TcDetailsAdapter;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.REAL_TRM_URL;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVICE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.SERVICE2;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.TEST_TRM_URL;

public class SendingData {

    public SendingData(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        String test_real = sharedPreferences.getString("TEST_REAL_SERVER", "");
        if (StringUtils.equalsIgnoreCase(test_real,"TEST"))
            server_link(0);
            //flag = "test";
        else
            server_link(1);
        //flag = "real";
    }

    private ReceivingData receivingData = new ReceivingData();
    private FunctionCall functionCall = new FunctionCall();
    private Handler handler;
    private String BASEURL,BASETCURL;

    private void server_link(int val)
    {
        if (val == 0)
        {
            BASEURL = TEST_TRM_URL + SERVICE2;
            BASETCURL = TEST_TRM_URL + SERVICE;
        }else {
            BASEURL = REAL_TRM_URL + SERVICE2;
            BASETCURL = REAL_TRM_URL + SERVICE;
        }
    }
    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        try {
            StringBuilder response = new StringBuilder();
            URL url = new URL(Post_Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60000);
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(datamap));
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            } else {
                response = new StringBuilder();
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Debug", "SERVER TIME OUT");

        }
        return null;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    private String UrlGetConnection(String Get_Url) throws IOException {
        String response;
        URL url = new URL(Get_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(60000);
        conn.setConnectTimeout(60000);
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseBuilder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                responseBuilder.append(line);
            }
            response = responseBuilder.toString();
        } else response = "";
        return response;
    }

    //For MR Login
    @SuppressLint("StaticFieldLeak")
    public class Login extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public Login(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap();
            datamap.put("MRCode", params[0]);
            datamap.put("DeviceId", params[1]);
            datamap.put("PASSWORD", params[2]);
            functionCall.logStatus("MRCODE " + params[0] + "\n" + "DEVICE ID" + params[1] + "\n" + "PASSWORD" + params[2]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/MRDetails", datamap);
                    response = UrlPostConnection(BASETCURL + "MRDetails",datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getMR_Details(result, handler, getSetValues);
        }
    }

    //Disconnection List
    @SuppressLint("StaticFieldLeak")
    public class Discon_List extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;

        public Discon_List(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("MRCode", params[0]);
            datamap.put("Date", params[1]);
            functionCall.logStatus("Discon_Mrcoe" + params[0] + "\n" + "Discon_Date" + params[1]);
            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/DisConList", datamap);
                response = UrlPostConnection(BASEURL + "DisConList", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.getDiscon_List(result, handler, getSetValues, arrayList);
        }
    }

    //Disconnection Update
    @SuppressLint("StaticFieldLeak")
    public class Disconnect_Update extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public Disconnect_Update(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("Acc_id", params[0]);
            datamap.put("Dis_Date", params[1]);
            datamap.put("CURREAD", params[2]);
            datamap.put("Remarks", params[3]);
            datamap.put("Comment", params[4]);
            functionCall.logStatus("Acc_id: " + params[0] + "\n" + "Dis_Date: " + params[1] + "\n" + "CURREAD: " + params[2] + "\n" + "Remarks:" + params[3] + "\n" + "Comment:" + params[4]);
            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/DisConUpdate", datamap);
                response = UrlPostConnection(BASEURL+"DisConUpdate", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getDisconnection_update_status(result, handler, getSetValues);
        }
    }

    //Reconnection List
    @SuppressLint("StaticFieldLeak")
    public class Recon_List extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;

        public Recon_List(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("MRCode", params[0]);
            datamap.put("Date", params[1]);
            functionCall.logStatus("Recon_MrCode" + params[0] + "\n" + "Recon_Date" + params[1]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReConList", datamap);
                response = UrlPostConnection(BASEURL+"ReConList", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.getReconcon_List(result, handler, getSetValues, arrayList);
        }
    }

    //Reconnection Update
    @SuppressLint("StaticFieldLeak")
    public class Reconnect_Update extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public Reconnect_Update(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("Acc_id", params[0]);
            datamap.put("Dis_Date", params[1]);
            datamap.put("CURREAD", params[2]);
            datamap.put("Remarks", params[3]);
            datamap.put("Comment", params[4]);
            functionCall.logStatus("Acc_id: " + params[0] + "\n" + "Dis_Date: " + params[1] + "\n" + "CURREAD: " + params[2] + "\n" + "Remarks:" + params[3]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReConUpdate", datamap);
                response = UrlPostConnection(BASEURL+"ReConUpdate", datamap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            receivingData.getReconnectionUpdateStatus(result, handler, getSetValues);
        }
    }

    //Checking Server Date
    @SuppressLint("StaticFieldLeak")
    public class Get_server_date extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;

        public Get_server_date(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                //response = UrlGetConnection("http://bc_service2.hescomtrm.com/Service.asmx/systemDate");
                response = UrlGetConnection(BASETCURL+ "systemDate");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_Server_Date_status(result, handler, getSetValues);
            super.onPostExecute(result);
        }
    }

    //Send Feeder Details
    @SuppressLint("StaticFieldLeak")
    public class SendFeeder_Details extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        Feeder_details_Adapter feeder_details_adapter;

        public SendFeeder_Details(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, Feeder_details_Adapter feeder_details_adapter) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.feeder_details_adapter = feeder_details_adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            //Send TC Code
            datamap.put("SUB_DIVCODE", params[0]);
            datamap.put("DATE", params[1]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDR_DETAILS", datamap);
                response = UrlPostConnection(BASEURL + "FDR_DETAILS", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_Feeder_details(result, handler, getSetValues, arrayList, feeder_details_adapter);
            super.onPostExecute(result);
        }
    }

    //FDR_FR_UPDATE
    @SuppressLint("StaticFieldLeak")
    public class FDR_Fr_Update extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public FDR_Fr_Update(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("FDRCODE", params[0]);
            datamap.put("DATE", params[1]);
            datamap.put("FDRFR", params[2]);
            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDRFR_Update", datamap);
                response = UrlPostConnection(BASEURL + "FDRFR_Update", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_fdr_update_status(result, handler, getSetValues);
            super.onPostExecute(result);
        }
    }

    //FDR FETCH
    @SuppressLint("StaticFieldLeak")
    public class FDR_Fetch extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        RoleAdapter2 roleAdapter;

        public FDR_Fetch(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, RoleAdapter2 roleAdapter) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.roleAdapter = roleAdapter;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("SUBDIV_CODE", params[0]);
            datamap.put("DATE", params[1]);
            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDR_FETCH", datamap);
                response = UrlPostConnection(BASEURL + "FDR_FETCH", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_fdr_fetch(result, handler, getSetValues, arrayList, roleAdapter);
            super.onPostExecute(result);
        }
    }

    //Recon memo details
    @SuppressLint("StaticFieldLeak")
    public class Recon_Memo_details extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        Recon_Memo_Adapter recon_memo_adapter;

        public Recon_Memo_details(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, Recon_Memo_Adapter recon_memo_adapter) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.recon_memo_adapter = recon_memo_adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("AccountId", params[0]);
            datamap.put("SUBDIVCODE", params[1]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReConMemo", datamap);
                response = UrlPostConnection(BASEURL + "ReConMemo", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_reconMemo_details(result, handler, getSetValues, arrayList, recon_memo_adapter);
            super.onPostExecute(result);
        }
    }

    //recon memo update
    @SuppressLint("StaticFieldLeak")
    public class Print_Update extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;

        public Print_Update(Handler handler) {
            this.handler = handler;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("ACCT_ID", params[0]);
            datamap.put("paid_amount", params[1]);
            datamap.put("rcpt_num", params[2]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/ReconMemo_Update", datamap);
                response = UrlPostConnection(BASEURL + "ReconMemo_Update", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_print_update_status(result, handler);
            super.onPostExecute(result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Send_Tc_details extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        TcDetailsAdapter tcDetailsAdapter;

        public Send_Tc_details(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, TcDetailsAdapter tcDetailsAdapter) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.tcDetailsAdapter = tcDetailsAdapter;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("SUBDIVCODE", params[0]);
            datamap.put("DATE", params[1]);
            datamap.put("FDRCODE", params[2]);
            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/TC_DETAILS", datamap);
                response = UrlPostConnection(BASEURL + "TC_DETAILS", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_details_status(result, handler, getSetValues, arrayList, tcDetailsAdapter);
            super.onPostExecute(result);
        }
    }

    //Tc Details update
    //FDR_FR_UPDATE
    @SuppressLint("StaticFieldLeak")
    public class TC_Update extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public TC_Update(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("TCCODE", params[0]);
            datamap.put("DATE", params[1]);
            datamap.put("TCFR", params[2]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/TCFR_Update", datamap);
                response = UrlPostConnection(BASEURL + "TCFR_Update", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_update_status(result, handler, getSetValues);
            super.onPostExecute(result);
        }
    }


    //For TC Search
    @SuppressLint("StaticFieldLeak")
    public class Search_Tccode extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        TCCode_Adapter tcCode_adapter;

        public Search_Tccode(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, TCCode_Adapter tcCode_adapter) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.tcCode_adapter = tcCode_adapter;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("MRCODE", params[0]);
            datamap.put("DATE", params[1]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/TC_DETAILS_MR", datamap);
                response = UrlPostConnection(BASETCURL + "TC_DETAILS_MR", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_code(result, handler, getSetValues, arrayList, tcCode_adapter);
            super.onPostExecute(result);
        }
    }

    //Update TC details
    @SuppressLint("StaticFieldLeak")
    public class Update_Tcdetails extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;

        public Update_Tcdetails(Handler handler, GetSetValues getSetValues) {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("MRCODE", params[0]);
            datamap.put("TCCODE", params[1]);
            datamap.put("DATE", params[2]);
            datamap.put("TCFR", params[3]);
            datamap.put("Latitude", params[4]);
            datamap.put("Langitude", params[5]);
            datamap.put("imagename", params[6]);

            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/Service.asmx/TCFR_Update_MR", datamap);
                response = UrlPostConnection(BASETCURL + "TCFR_Update_MR", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_update(result, handler, getSetValues);
            super.onPostExecute(result);
        }
    }

    //Feeder name
    @SuppressLint("StaticFieldLeak")
    public class Feeder_Name_Fetch extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        RoleAdapter3 roleAdapter3;

        public Feeder_Name_Fetch(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, RoleAdapter3 roleAdapter3) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.roleAdapter3 = roleAdapter3;

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("SUBDIV_CODE", params[0]);
            datamap.put("DATE", params[1]);

            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDR_NAME_FETCH", datamap);
                response = UrlPostConnection(BASEURL + "FDR_NAME_FETCH", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_fdr_name(result, handler, getSetValues, arrayList, roleAdapter3);
            super.onPostExecute(result);
        }
    }

    //Feeder name
    @SuppressLint("StaticFieldLeak")
    public class Send_Feeder_Name extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;
        TCDetailsAdapter2 tcDetailsAdapter2;

        public Send_Feeder_Name(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList, TCDetailsAdapter2 tcDetailsAdapter2) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.tcDetailsAdapter2 = tcDetailsAdapter2;


        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("FDRNAME", params[0]);
            datamap.put("DATE", params[1]);

            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/FDR_DETAILS_FETCH", datamap);
                response = UrlPostConnection(BASEURL + "FDR_DETAILS_FETCH", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_tc_details(result, handler, getSetValues, arrayList, tcDetailsAdapter2);
            super.onPostExecute(result);
        }
    }

    //DTC Mapping
    @SuppressLint("StaticFieldLeak")
    public class DTC_MAPPING extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> arrayList;

        public DTC_MAPPING(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> arrayList) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;


        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("MRCODE", params[0]);
            datamap.put("TCCODE", params[1]);
            datamap.put("READ_DATE", params[2]);

            try {
               // response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/Update_DTC_MR", datamap);
                response = UrlPostConnection(BASEURL + "Update_DTC_MR", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_DTC_update(result, handler, getSetValues, arrayList);
            super.onPostExecute(result);
        }
    }

    // MRCODE
    @SuppressLint("StaticFieldLeak")
    public class MRCODE extends AsyncTask<String, String, String> {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues> mr_arrayList;
        RoleAdapter4 roleAdapter4;

        public MRCODE(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues> mr_arrayList, RoleAdapter4 roleAdapter4) {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.mr_arrayList = mr_arrayList;
            this.roleAdapter4 = roleAdapter4;

        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> datamap = new HashMap<>();
            datamap.put("SUBDIVCODE", params[0]);
            try {
                //response = UrlPostConnection("http://bc_service2.hescomtrm.com/ReadFile.asmx/MR_FETCH", datamap);
                response = UrlPostConnection(BASEURL + "MR_FETCH", datamap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.get_mrcode(result, handler, getSetValues, mr_arrayList, roleAdapter4);
            super.onPostExecute(result);
        }
    }
}
