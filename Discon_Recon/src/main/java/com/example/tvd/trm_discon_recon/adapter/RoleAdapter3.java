package com.example.tvd.trm_discon_recon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.values.GetSetValues;
import java.util.ArrayList;

public class RoleAdapter3 extends BaseAdapter {
    private ArrayList<GetSetValues> arraylist;
    private Context context;
    private LayoutInflater inflater;

    public RoleAdapter3(ArrayList<GetSetValues>arrayList, Context context)
    {
        this.arraylist = arrayList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }
    @Override
    public int getCount() {
        return arraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return arraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.spinner_items, null);
        TextView tv_role = convertView.findViewById(R.id.spinner_txt);
        tv_role.setText(arraylist.get(position).getFeeder_name());
        return convertView;
    }
}


