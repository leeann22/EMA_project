package edu.monash.fit2081a1.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.monash.fit2081a1.R;
import edu.monash.fit2081a1.activities.EventGoogleResult;
import edu.monash.fit2081a1.storage.Event;

public class EvRecyclerAdapter extends RecyclerView.Adapter<EvRecyclerAdapter.ViewHolder>{

    ArrayList<Event> data = new ArrayList<Event>();

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ev_cardview, parent, false); //CardView inflated as RecyclerView list item
        EvRecyclerAdapter.ViewHolder viewHolder = new EvRecyclerAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvEvID.setText(data.get(position).getEventID());
        holder.tvEvName.setText(data.get(position).getEventName());
        holder.tvCatID.setText(data.get(position).getCatID());
        holder.tvTickets.setText(String.valueOf(data.get(position).getTicket()));

        if (data.get(position).isActive()){
            holder.tvIsEvActive.setText("Active");
        } else {
            holder.tvIsEvActive.setText("Inactive");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            String evName = data.get(position).getEventName();
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), EventGoogleResult.class);
                intent.putExtra("eventName", evName);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.data != null) { // if data is not null
            return this.data.size(); // then return the size of ArrayList
        }

        // else return zero if data is null
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEvID;
        public TextView tvEvName;
        public TextView tvCatID;
        public TextView tvTickets;
        public TextView tvIsEvActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEvID = itemView.findViewById(R.id.card_ev_id);
            tvEvName = itemView.findViewById(R.id.card_ev_name);
            tvCatID = itemView.findViewById(R.id.cardE_cat_id);
            tvTickets = itemView.findViewById(R.id.card_ticket);
            tvIsEvActive = itemView.findViewById(R.id.cardE_is_active);
        }
    }
}
