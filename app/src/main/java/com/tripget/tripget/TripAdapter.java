package com.tripget.tripget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by ivonne on 08/08/17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {


    private Context mContext;
    private List<Trip> tripList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, totalBudget, votes, locationDate;
        public ImageView userImg, userUpload;

        public MyViewHolder(View view) {
            super(view);

            username = (TextView) view.findViewById(R.id.usernameCard);
            totalBudget = (TextView) view.findViewById(R.id.totalBudget);
            votes = (TextView) view.findViewById(R.id.likes_counter);
            locationDate = (TextView) view.findViewById(R.id.locationDateCard);
            userImg = (ImageView) view.findViewById(R.id.userimgCard);
            userUpload = (ImageView) view.findViewById(R.id.backUserUpload);
        }
    }

    public TripAdapter(Context mContext, List<Trip> tripList) {
        this.mContext = mContext;
        this.tripList = tripList;
    }

    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_trips_cardview, parent,false);

        return new TripAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripAdapter.MyViewHolder holder, int position) {

        Trip trip = tripList.get(position);
        holder.username.setText(trip.getUsername());
        holder.locationDate.setText(trip.getLocationDate());
        holder.totalBudget.setText(trip.getTotalBudget());

        Glide.with(mContext).load(trip.getUserImg()).into(holder.userImg);
        Glide.with(mContext).load(trip.getUserImgUpload()).into(holder.userUpload);
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }
}
