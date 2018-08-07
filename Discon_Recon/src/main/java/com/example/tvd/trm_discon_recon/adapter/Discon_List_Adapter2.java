package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.DisconListActivity;
import com.example.tvd.trm_discon_recon.location.Location;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Locale;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DISCONNECTION_DIALOG;

public class Discon_List_Adapter2 extends RecyclerView.Adapter<Discon_List_Adapter2.Discon_Holder> implements Filterable {
    private ArrayList<GetSetValues> arraylist ;
    private ArrayList<GetSetValues>filteredList;
    private Context context;
    private DisconListActivity disconListActivity;
    public Discon_List_Adapter2(Context context, ArrayList<GetSetValues>arraylist,DisconListActivity disconListActivity)
    {
        this.context = context;
        this.arraylist = arraylist;
        this.disconListActivity = disconListActivity;
        this.filteredList = arraylist;
    }
    @Override
    public Discon_List_Adapter2.Discon_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discon_adapter_layout, null);
        return new Discon_Holder(view);
    }

    @Override
    public void onBindViewHolder(Discon_List_Adapter2.Discon_Holder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.conname.setText(getSetValues.getDiscon_consumer_name());
        holder.accountid.setText(getSetValues.getDiscon_acc_id());
        Log.d("Holder","Acc id"+getSetValues.getDiscon_acc_id());
        //here %s%s meaning first string will set on first then space and then second string
        holder.arrears.setText(String.format("%s %s",context.getResources().getString(R.string.rupee), getSetValues.getDiscon_arrears()));
        Log.d("Holder","Arrears"+getSetValues.getDiscon_arrears());
        holder.prevraed.setText(getSetValues.getDiscon_prevread());
        Log.d("Holder","PrevRead"+getSetValues.getDiscon_prevread());
        if (StringUtils.startsWithIgnoreCase(getSetValues.getDiscon_flag(),"Y"))
            holder.disconnected.setVisibility(View.VISIBLE);
        else holder.disconnected.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search=constraint.toString();
                if (search.isEmpty())
                    arraylist = filteredList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i = 0; i < filteredList.size(); i++) {
                        GetSetValues getSetValues = filteredList.get(i);
                        //todo searching based on discon_acc_id
                        if (getSetValues.getDiscon_acc_id().contains(search)) {
                            filterlist.add(getSetValues);
                        }
                    }
                    arraylist = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arraylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arraylist = (ArrayList<GetSetValues>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class Discon_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView accountid, arrears, prevraed, disconnected,conname;
        ImageView marker,disconnection;
        LinearLayout lin;
        public Discon_Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            accountid =  itemView.findViewById(R.id.txt_account_id);
            arrears =  itemView.findViewById(R.id.txt_arrears);
            prevraed =  itemView.findViewById(R.id.txt_prevread);
            disconnected = itemView.findViewById(R.id.txt_disconnected);
            conname = itemView.findViewById(R.id.txt_cons_name);
            marker = itemView.findViewById(R.id.img_marker);
            marker.setOnClickListener(this);
            disconnection = itemView.findViewById(R.id.img_disconnect);
            disconnection.setOnClickListener(this);
            lin =  itemView.findViewById(R.id.lin_layout);
            lin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            /*****Comment should be removed from here************/
            GetSetValues getSetValues = arraylist.get(position);
            switch (v.getId())
            {
                case R.id.img_marker:
                    String lat = getSetValues.getDiscon_lat();
                    String lon = getSetValues.getDiscon_lon();
                    String address = getSetValues.getDiscon_add1();
                    String discon_id = getSetValues.getDiscon_acc_id();
                    Log.d("Debug","discon_id:"+discon_id);
                    String name = getSetValues.getDiscon_consumer_name();
                    Log.d("Debug","name:"+name);
                    Intent intent = new Intent(context, Location.class);
                    intent.putExtra("LATITUDE",lat);
                    intent.putExtra("LONGITUDE", lon);
                    intent.putExtra("ACCOUNT_ID",discon_id);
                    intent.putExtra("NAME",name);
                    intent.putExtra("ADDRESS",address);
                    context.startActivity(intent);
                    break;
                case R.id.img_disconnect:
                    if (StringUtils.startsWithIgnoreCase(getSetValues.getDiscon_flag(),"Y"))
                        Toast.makeText(context, "Account ID already Disconnected!!", Toast.LENGTH_SHORT).show();
                    else disconListActivity.show_disconnection_dialog(DISCONNECTION_DIALOG, position, arraylist);
                    break;
            }
        }
    }
}
