package com.example.tvd.trm_discon_recon.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.TC_MAPPING;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.DTC_DETAILS_UPDATE_DIALOG;

public class TCDetailsAdapter2 extends RecyclerView.Adapter<TCDetailsAdapter2.TcHolder> implements Filterable {
    private ArrayList<GetSetValues> arrayList;
    private ArrayList<GetSetValues> filteredList;
    private Context context;
    private GetSetValues getSetValues;
    private TC_MAPPING tc_mapping;

    public TCDetailsAdapter2(ArrayList<GetSetValues> arrayList, Context context, GetSetValues getSetValues, TC_MAPPING tc_mapping) {
        this.arrayList = arrayList;
        this.filteredList = arrayList;
        this.context = context;
        this.getSetValues = getSetValues;
        this.tc_mapping = tc_mapping;
    }


    @Override
    public TCDetailsAdapter2.TcHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tcdetails2_adapter, null);
        return new TCDetailsAdapter2.TcHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TcHolder holder, int position) {
        getSetValues = arrayList.get(position);
        holder.tcname.setText(getSetValues.getDTCNAME());
        holder.tccode.setText(getSetValues.getDTCCODE());
        holder.mrname.setText(getSetValues.getDTC_MRCODE());
        String DATE = getSetValues.getDTC_DATE();
//        String date = DATE.substring(0, 11);
        holder.date.setText(DATE);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if (search.isEmpty())
                    arrayList = filteredList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i = 0; i < filteredList.size(); i++) {
                        GetSetValues getSetValues = filteredList.get(i);
                        //todo searching based on Tc_code
                        if (getSetValues.getDTCCODE().contains(search)) {
                            filterlist.add(getSetValues);
                        }
                    }
                    arrayList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<GetSetValues>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class TcHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tcname, tccode, mrname, date;

        public TcHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tcname = itemView.findViewById(R.id.txt_tcname);
            tccode = itemView.findViewById(R.id.txt_tccode);
            mrname = itemView.findViewById(R.id.txt_mrcode);
            date = itemView.findViewById(R.id.txt_date);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            getSetValues = arrayList.get(position);
            tc_mapping.show_tc_details_update_dialog2(DTC_DETAILS_UPDATE_DIALOG, position, arrayList);
        }
    }
}
