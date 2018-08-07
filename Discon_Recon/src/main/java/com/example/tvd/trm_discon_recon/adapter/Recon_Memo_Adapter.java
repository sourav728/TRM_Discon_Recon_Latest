package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.activities.Recon_memo_printing;
import com.example.tvd.trm_discon_recon.values.GetSetValues;
import java.util.ArrayList;

public class Recon_Memo_Adapter extends RecyclerView.Adapter<Recon_Memo_Adapter.ReconHolder> {
    private ArrayList<GetSetValues> arraylist;
    private Context context;
    private GetSetValues getSetValues;

    public Recon_Memo_Adapter(ArrayList<GetSetValues> arrayList, Context context, GetSetValues getSetValues) {
        this.arraylist = arrayList;
        this.context = context;
        this.getSetValues = getSetValues;
    }

    @Override
    public Recon_Memo_Adapter.ReconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recon_memo_adapter_layout, null);
        return new ReconHolder(view);
    }

    @Override
    public void onBindViewHolder(Recon_Memo_Adapter.ReconHolder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.acc_id.setText(getSetValues.getRecon_memo_acc_id());
        holder.name.setText(getSetValues.getRecon_memo_customer_name());
        holder.tariff.setText(getSetValues.getReoon_memo_tariff());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ReconHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView acc_id, name, tariff;

        public ReconHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            acc_id = itemView.findViewById(R.id.txt_acc_id);
            name = itemView.findViewById(R.id.txt_name);
            tariff = itemView.findViewById(R.id.txt_tariff);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            GetSetValues getSetValues = arraylist.get(position);
            Intent intent = new Intent(context, Recon_memo_printing.class);
            intent.putExtra("ACCT_ID", getSetValues.getRecon_memo_acc_id());
            intent.putExtra("LEG_RRNO", getSetValues.getRecon_memo_rrno());
            intent.putExtra("CONSUMER_NAME", getSetValues.getRecon_memo_customer_name());
            intent.putExtra("ADD1", getSetValues.getRecon_memo_add1());
            intent.putExtra("TARIFF", getSetValues.getReoon_memo_tariff());
            intent.putExtra("RE_DATE", getSetValues.getRecon_memo_reconnection_date());
            //intent.putExtra("ARREARS", getSetValues.getRecon_memo_arrears());
            intent.putExtra("SO", getSetValues.getRecon_memo_so());
            intent.putExtra("subdivcode", getSetValues.getRecon_memo_subdiv());
            intent.putExtra("DR_FEE", getSetValues.getRecon_memo_dr_fee());
            intent.putExtra("READ_DATE", getSetValues.getRecon_memo_readdate());
            intent.putExtra("MRCODE", getSetValues.getRecon_memo_mrcode());
            context.startActivity(intent);
        }
    }
}
