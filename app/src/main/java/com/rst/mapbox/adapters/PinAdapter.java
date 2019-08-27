package com.rst.mapbox.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rst.mapbox.R;
import com.rst.mapbox.models.PinItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.PinViewHolder> {

    private Context            mContext;
    private ArrayList<PinItem> pinItems;

    public PinAdapter(Context mContext, ArrayList<PinItem> pinItems) {
        this.mContext = mContext;
        this.pinItems = pinItems;
    }

    @NonNull
    @Override
    public PinViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.pin, viewGroup, false);
        return new PinViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PinViewHolder pinViewHolder, int position) {
        PinItem currentItem = pinItems.get(position);

        String name = currentItem.getName();
        double longitude = currentItem.getLongitude();
        double latitutde = currentItem.getLatitude();
        String description = currentItem.getDescription();

        pinViewHolder.pinName.setText(name);
        pinViewHolder.pinDesc.setText(description);

    }

    @Override
    public int getItemCount() {
        return pinItems.size();
    }

    public class PinViewHolder extends RecyclerView.ViewHolder {

        public TextView pinId, pinName, pinDesc;


        public PinViewHolder(@NonNull View itemView) {
            super(itemView);

            pinName = itemView.findViewById(R.id.pinName);
            pinDesc = itemView.findViewById(R.id.pin_desc);

        }
    }

}
