package com.example.tvd.trm_discon_recon;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.adapter.Discon_List_Adapter;
import com.example.tvd.trm_discon_recon.invoke.SendingData;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_FAILURE;
import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCON_LIST_SUCCESS;

public class Discon_List extends Fragment {
    ProgressDialog progressDialog;
    SendingData sendingdata;
    GetSetValues getsetvalues;
    RecyclerView recyclerview;
    ArrayList<GetSetValues>arraylist;
    private Discon_List_Adapter discon_list_adapter;

    private final Handler mhandler;
    {
        mhandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what)
                {
                    case DISCON_LIST_SUCCESS:
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        break;
                    case DISCON_LIST_FAILURE:
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failure!!", Toast.LENGTH_SHORT).show();
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }
    public Discon_List() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_discon__list, container, false);
        sendingdata = new SendingData();
        getsetvalues = new GetSetValues();
        recyclerview = (RecyclerView) view.findViewById(R.id.discon_recyclerview);
        arraylist = new ArrayList<>();
        getsetvalues = new GetSetValues();

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setHasFixedSize(true);
        discon_list_adapter = new Discon_List_Adapter(getActivity(), arraylist, getsetvalues);
        recyclerview.setAdapter(discon_list_adapter);

        progressDialog = new ProgressDialog(getActivity(), R.style.MyProgressDialogstyle);
        progressDialog.setTitle("Connecting To Server");
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        SendingData.Discon_List discon_list = sendingdata.new Discon_List(mhandler, getsetvalues,arraylist,discon_list_adapter);
        /********Below Mrcode and date is hardcoaded********/
        discon_list.execute("14000521","2018-04-27");

        return view;
    }

}
