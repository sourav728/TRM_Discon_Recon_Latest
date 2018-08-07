package com.example.tvd.trm_discon_recon.invoke;

import android.os.Handler;

import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter;
import com.example.tvd.trm_discon_recon.values.FunctionCall;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_SUCCESS;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.LOGIN_SUCCESS;


public class ReceivingData {
    private FunctionCall functionCall = new FunctionCall();
    private String parseServerXML(String result) {
        String value = "";
        XmlPullParserFactory pullParserFactory;
        InputStream res;
        try {
            res = new ByteArrayInputStream(result.getBytes());
            pullParserFactory = XmlPullParserFactory.newInstance();
            pullParserFactory.setNamespaceAware(true);
            XmlPullParser parser = pullParserFactory.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(res, null);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String name = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        switch (name) {
                            case "string":
                                value = parser.nextText();
                                break;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
    //FOR getting result based on MR LOGIN
    public void getMR_Details(String result, Handler handler, GetSetValues getSetValues)
    {
        result = parseServerXML(result);
        functionCall.logStatus("MR_Login"+ result);
        JSONArray jsonArray;
        try
        {
            jsonArray = new JSONArray(result);
            for (int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String message = jsonObject.getString("message");
                if (StringUtils.startsWithIgnoreCase(message,"Success!"))
                {
                    getSetValues.setMrcode(jsonObject.getString("MRCODE"));
                    getSetValues.setMrname(jsonObject.getString("MRNAME"));
                    getSetValues.setSubdivcode(jsonObject.getString("SUBDIVCODE"));
                    handler.sendEmptyMessage(LOGIN_SUCCESS);
                }
                else handler.sendEmptyMessage(LOGIN_FAILURE);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(LOGIN_FAILURE);
        }
    }

    public void getDiscon_List(String result, Handler handler, GetSetValues getSetValues, ArrayList<GetSetValues>arrayList, Discon_List_Adapter discon_list_adapter)
    {
        result = parseServerXML(result);
        functionCall.logStatus("DISCON_LIST"+result);
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(result);
            if (jsonArray.length()>0)
            {
                for (int i=0; i<jsonArray.length(); i++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    getSetValues = new GetSetValues();
                    String ACCT_ID = jsonObject.getString("ACCT_ID");
                    String ARREARS = jsonObject.getString("ARREARS");
                    String DIS_DATE = jsonObject.getString("DIS_DATE");
                    String PREVREAD = jsonObject.getString("PREVREAD");
                    getSetValues.setAcc_id(ACCT_ID);
                    getSetValues.setArrears(ARREARS);
                    getSetValues.setDis_date(DIS_DATE);
                    getSetValues.setPrev_read(PREVREAD);
                    arrayList.add(getSetValues);
                    discon_list_adapter.notifyDataSetChanged();
                }
                handler.sendEmptyMessage(DISCON_LIST_SUCCESS);
            }
            else handler.sendEmptyMessage(DISCON_LIST_FAILURE);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
            functionCall.logStatus("JSON Exception Failure!!");
            handler.sendEmptyMessage(DISCON_LIST_FAILURE);
        }
    }
}
