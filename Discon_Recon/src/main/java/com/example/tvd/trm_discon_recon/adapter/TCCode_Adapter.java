package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.TC_Details2;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

import static com.example.tvd.trm_discon_recon.values.ConstantValues.TC_DETAILS_UPDATE;

public class TCCode_Adapter extends RecyclerView.Adapter<TCCode_Adapter.TcHolder> implements Filterable {
    private ArrayList<GetSetValues> arraylist;
    private ArrayList<GetSetValues> filteredList;
    private Context context;
    private GetSetValues getSetValues = new GetSetValues();
    private TC_Details2 tc_details2;

    public TCCode_Adapter(Context context, ArrayList<GetSetValues> arraylist, TC_Details2 tcDetails2) {
        this.arraylist = arraylist;
        this.filteredList = arraylist;
        this.context = context;
        this.tc_details2 = tcDetails2;
    }

    @Override
    public TcHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tccode_adapter, null);
        return new TcHolder(view);
    }

    @Override
    public void onBindViewHolder(TcHolder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.tccode.setText(getSetValues.getTc_code());
        holder.tcir.setText(getSetValues.getTcir());
        holder.tcfr.setText(getSetValues.getTcfr());
        holder.tcmf.setText(getSetValues.getTcmf());
        holder.tcname.setText(getSetValues.getTc_name());


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
                String search = constraint.toString();
                if (search.isEmpty())
                    arraylist = filteredList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for (int i = 0; i < filteredList.size(); i++) {
                        GetSetValues getSetValues = filteredList.get(i);
                        //todo searching based on Tc_code
                        if (getSetValues.getTc_code().contains(search)) {
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

    public class TcHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tccode, tcir, tcfr, tcmf, tcname;

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
            getSetValues = arraylist.get(position);
            tc_details2.show_tc_details_update_dialog2(TC_DETAILS_UPDATE, position, arraylist);
           /* Intent intent= new Intent(context, Tcdetails_Update.class);
            intent.putExtra("TCCODE",getSetValues.getTc_code());
            context.startActivity(intent);*/
        }
    }
}


