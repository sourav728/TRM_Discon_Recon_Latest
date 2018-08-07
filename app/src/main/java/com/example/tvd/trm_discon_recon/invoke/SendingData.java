package com.example.tvd.trm_discon_recon.invoke;

import android.os.AsyncTask;
import android.os.Handler;

import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

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

public class SendingData {
    private ReceivingData receivingData = new ReceivingData();
    private FunctionCall functionCall = new FunctionCall();
    private String UrlPostConnection(String Post_Url, HashMap<String, String> datamap) throws IOException {
        String response = "";
        URL url = new URL(Post_Url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(15000);
        conn.setConnectTimeout(15000);
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
                response += line;
            }
        } else {
            response = "";
        }
        return response;
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
    //For MR Login
    public class Login extends AsyncTask<String,String,String>
    {
        String response="";
        Handler handler;
        GetSetValues getSetValues;
        public Login(Handler handler,GetSetValues getSetValues)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap();
            datamap.put("MRCode",params[0]);
            datamap.put("DeviceId",params[1]);
            datamap.put("PASSWORD",params[2]);
            functionCall.logStatus("MRCODE "+params[0] + "\n" + "DEVICE ID"+ params[1] + "\n" + "PASSWORD" + params[2]);
            try
            {
                response = UrlPostConnection("http://www.bc_service.hescomtrm.com/Service.asmx/MRDetails",datamap);
            }
            catch (IOException e)
            {
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

    public class Discon_List extends AsyncTask<String,String,String>
    {
        String response = "";
        Handler handler;
        GetSetValues getSetValues;
        ArrayList<GetSetValues>arrayList;
        Discon_List_Adapter discon_list_adapter;
        public Discon_List(Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues>arrayList, Discon_List_Adapter discon_list_adapter)
        {
            this.handler = handler;
            this.getSetValues = getSetValues;
            this.arrayList = arrayList;
            this.discon_list_adapter = discon_list_adapter;
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String>datamap = new HashMap<>();
            datamap.put("MRCode",params[0]);
            datamap.put("Date",params[1]);
            functionCall.logStatus("Discon_Mrcoe"+params[0]+"\n"+"Discon_Date"+params[1]);
            try {
                response = UrlPostConnection("http://www.bc_service.hescomtrm.com/ReadFile.asmx/DisConList",datamap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            receivingData.getDiscon_List(result, handler,getSetValues,arrayList,discon_list_adapter);
        }
    }

}
