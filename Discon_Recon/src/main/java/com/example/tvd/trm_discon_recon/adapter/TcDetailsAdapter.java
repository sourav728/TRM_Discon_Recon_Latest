package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.TcDetails2;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE_DIALOG;

public class TcDetailsAdapter extends RecyclerView.Adapter<TcDetailsAdapter.TcHolder> implements Filterable{
    private ArrayList<GetSetValues> arrayList ;
    private ArrayList<GetSetValues>filteredList;
    private Context context;
    private GetSetValues getSetValues;
    private TcDetails2 tcDetails2;

    public TcDetailsAdapter(ArrayList<GetSetValues>arrayList,Context context,GetSetValues getSetValues, TcDetails2 tcDetails2)
    {
        this.arrayList = arrayList;
        this.filteredList = arrayList;
        this.context = context;
        this.getSetValues = getSetValues;
        this.tcDetails2 = tcDetails2;
    }
    @Override
    public TcDetailsAdapter.TcHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tc_details_adapter_layout, null);
        return new TcHolder(view);
    }

    @Override
    public void onBindViewHolder(TcDetailsAdapter.TcHolder holder, int position) {
        GetSetValues getSetValues = arrayList.get(position);
        holder.tccode.setText(getSetValues.getTc_code());
        holder.tcir.setText(getSetValues.getTcir());
        holder.tcfr.setText(getSetValues.getTcfr());
        holder.tcmf.setText(getSetValues.getTcmf());
        holder.tcname.setText(getSetValues.getTcname());
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
                String search=constraint.toString();
                if (search.isEmpty())
                    arrayList = filteredList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i = 0; i < filteredList.size(); i++) {
                        GetSetValues getSetValues = filteredList.get(i);
                        //todo searching based on Tc_code
                        if (getSetValues.getTc_code().contains(search)) {
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

    public class TcHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tccode,tcir,tcfr,tcmf,tcname;
        public TcHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tccode = itemView.findViewById(R.id.txt_tccode);
            tcir = itemView.findViewById(R.id.txt_tcir);
            tcfr = itemView.findViewById(R.id.txt_tcfr);
            tcmf = itemView.findViewById(R.id.txt_tcmf);
            tcname = itemView.findViewById(R.id.txt_tcname);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSetValues getSetValues = arrayList.get(position);
            tcDetails2.show_tc_details_update_dialog(TC_DETAILS_UPDATE_DIALOG,position,arrayList);
        }
    }
}
