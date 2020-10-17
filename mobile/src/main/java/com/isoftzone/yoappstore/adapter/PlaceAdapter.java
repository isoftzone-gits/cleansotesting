package com.isoftzone.yoappstore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.libraries.places.api.model.Place;
import com.isoftzone.yoappstore.R;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {

    private Context context;
    ArrayList<Place> placeArrayList;
    Listner listner;

    public interface Listner {
        void onClickPlace(Place place);
    }

    public PlaceAdapter(Context context, ArrayList<Place> placeArrayList, Listner listner) {
        this.placeArrayList = placeArrayList;
        this.context = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public PlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_names_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final Place place = placeArrayList.get(position);
        holder.placeNameTextView.setText(place.getName());
        holder.mainLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listner.onClickPlace(place);
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView placeNameTextView;
        private LinearLayout mainLinearLayout;

        public MyViewHolder(View view) {
            super(view);
            placeNameTextView = view.findViewById(R.id.placeNameTextView);
            mainLinearLayout = view.findViewById(R.id.mainLinearLayout);
        }
    }
}
