package com.example.tvd.trm_discon_recon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tvd.trm_discon_recon.R;
import com.example.tvd.trm_discon_recon.values.GetSetValues;

import java.util.ArrayList;

public class Discon_List_Adapter extends RecyclerView.Adapter<Discon_List_Adapter.Discon_Holder>{
    private ArrayList<GetSetValues>arraylist = new ArrayList<>();
    private Context context;
    private GetSetValues getSetValues;
    private Discon_List_Adapter discon_list_adapter;
    public Discon_List_Adapter(Context context, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues)
    {
        this.context = context;
        this.arraylist = arrayList;
        this.getSetValues = getSetValues;
    }
    @Override
    public Discon_List_Adapter.Discon_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discon_adapter_layout, null);
        return new Discon_Holder(view);
    }

    @Override
    public void onBindViewHolder(Discon_List_Adapter.Discon_Holder holder, int position) {
        GetSetValues getSetValues = arraylist.get(position);
        holder.accountid.setText(getSetValues.getAcc_id());
        holder.arrears.setText(getSetValues.getArrears());
        holder.prevraed.setText(getSetValues.getPrev_read());
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class Discon_Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView accountid,arrears,prevraed;
        public Discon_Holder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            accountid = (TextView) itemView.findViewById(R.id.txt_account_id);
            arrears = (TextView) itemView.findViewById(R.id.txt_arrears);
            prevraed = (TextView) itemView.findViewById(R.id.txt_prevread);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
