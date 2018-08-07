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
import com.example.tvd.trm_discon_recon.activities.Recon_List_Activity;
import com.example.tvd.trm_discon_recon.location.Location;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.RECONNECTION_DIALOG;

public class Recon_List_Adapter2 extends RecyclerView.Adapter<Recon_List_Adapter2.Recon_Holder> implements Filterable {
    private ArrayList<GetSetValues> arraylist;
    private ArrayList<GetSetValues>filteredList;
    private Context context;
    private Recon_List_Activity recon_list;
    public Recon_List_Adapter2(Context context, ArrayList<GetSetValues>arraylist,Recon_List_Activity recon_list)
    {
        this.context = context;
        this.arraylist = arraylist;
        this.recon_list = recon_list;
        this.filteredList = arraylist;
    }
    @Override
    public Recon_List_Adapter2.Recon_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recon_adapter_layout, null);
        return new Recon_Holder(view);
    }

    @Override
    public void onBindViewHolder(Recon_List_Adapter2.Recon_Holder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.name.setText(getSetValues.getRecon_consumer_name());
        holder.accountid.setText(getSetValues.getRecon_acc_id());
        Log.d("Debug","Recon_Acc id"+getSetValues.getRecon_acc_id());
        //here %s%s meaning first string will set on first then space and then second string
        holder.prevraed.setText(getSetValues.getRecon_prevread());
        holder.re_date.setText(getSetValues.getRecon_date());
        Log.d("Debug","Recon_PrevRead"+getSetValues.getRecon_prevread());
        if (StringUtils.startsWithIgnoreCase(getSetValues.getRecon_flag(),"Y"))
            holder.reconnected.setVisibility(View.VISIBLE);
        else holder.reconnected.setVisibility(View.INVISIBLE);
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
                        if (getSetValues.getRecon_acc_id().contains(search)) {
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

    public class Recon_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView accountid, prevraed, reconnected, re_date,name;
        LinearLayout lin;
        ImageView reconnect, location;
        public Recon_Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            accountid = (TextView) itemView.findViewById(R.id.txt_account_id);
            prevraed = (TextView) itemView.findViewById(R.id.txt_prevread);
            reconnected = (TextView) itemView.findViewById(R.id.txt_reconnected);
            name = itemView.findViewById(R.id.txt_name);

            re_date = itemView.findViewById(R.id.txt_redate);
            reconnect = itemView.findViewById(R.id.img_reconnect);
            reconnect.setOnClickListener(this);
            location = itemView.findViewById(R.id.img_location);
            location.setOnClickListener(this);
            lin = (LinearLayout) itemView.findViewById(R.id.lin_layout);
            lin.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSetValues getSetValues = arraylist.get(position);
            switch (v.getId())
            {
                case R.id.img_location:
                    String lat = getSetValues.getRecon_lat();
                    String lon = getSetValues.getRecon_lon();
                    String accid = getSetValues.getRecon_acc_id();
                    String name = getSetValues.getRecon_consumer_name();
                    String address = getSetValues.getRecon_add1();

                    Intent intent = new Intent(context, Location.class);
                    intent.putExtra("LATITUDE",lat);
                    intent.putExtra("LONGITUDE", lon);
                    intent.putExtra("ACCOUNT_ID",accid);
                    intent.putExtra("NAME",name);
                    intent.putExtra("ADDRESS",address);
                    context.startActivity(intent);
                     break;
                case R.id.img_reconnect:
                    if (StringUtils.startsWithIgnoreCase(getSetValues.getRecon_flag(),"Y"))
                        Toast.makeText(context, "Account ID Already Reconnected!!", Toast.LENGTH_SHORT).show();
                    else recon_list.show_reconnection_dialog(RECONNECTION_DIALOG, position, arraylist);
                    break;
            }
        }
    }
}
