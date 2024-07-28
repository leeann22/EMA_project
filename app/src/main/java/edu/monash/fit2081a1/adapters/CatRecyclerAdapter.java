package edu.monash.fit2081a1.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.monash.fit2081a1.R;
import edu.monash.fit2081a1.activities.GoogleMapActivity;
import edu.monash.fit2081a1.storage.EventCategory;

public class CatRecyclerAdapter extends RecyclerView.Adapter<CatRecyclerAdapter.ViewHolder> {

    ArrayList<EventCategory> data = new ArrayList<EventCategory>();

    public void setData(ArrayList<EventCategory> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_cardview, parent, false); //CardView inflated as RecyclerView list item
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setBackgroundColor(Color.rgb(247, 137, 255));
        holder.tvCatID.setText(data.get(position).getCatID());
        holder.tvCatName.setText(data.get(position).getCatName());
        holder.tvEventCount.setText(String.valueOf(data.get(position).getEventCount()));

        if (data.get(position).isActive()){
            holder.tvIsCatActive.setText("Active");
        } else {
            holder.tvIsCatActive.setText("Inactive");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            String loc = data.get(position).getCategoryLocation();
            @Override
            public void onClick(View view){
                Intent intent = new Intent(view.getContext(), GoogleMapActivity.class);
                intent.putExtra("categoryLocation", loc);
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
        public TextView tvCatID;
        public TextView tvCatName;
        public TextView tvEventCount;
        public TextView tvIsCatActive;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCatID = itemView.findViewById(R.id.card_cat_id);
            tvCatName = itemView.findViewById(R.id.card_cat_name);
            tvEventCount = itemView.findViewById(R.id.card_event_count);
            tvIsCatActive = itemView.findViewById(R.id.card_is_active);
        }
    }
}
