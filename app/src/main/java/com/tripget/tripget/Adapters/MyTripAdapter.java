package com.tripget.tripget.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripget.tripget.Model.Trip;
import com.tripget.tripget.R;

import java.util.List;

/**
 * Created by ivonne on 20/08/17.
 */

public class MyTripAdapter extends RecyclerView.Adapter<MyTripAdapter.MyViewHolder> {

    private Context mContext;
    private List<Trip> trips;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView username, total_budget, titleTrip, trip_date, likes;
        public ImageView user_image, trip_image;
        public ImageButton editBtn, deleteBtn;
        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.usernameTrip);
            total_budget = (TextView) view.findViewById(R.id.totalBudgetTrip);
            titleTrip = (TextView) view.findViewById(R.id.titleTripCard);
            trip_date = (TextView) view.findViewById(R.id.dateTripCard);
            likes = (TextView) view.findViewById(R.id.counterLikesTrip);


            user_image = (ImageView) view.findViewById(R.id.userImgTrip);
            trip_image = (ImageView) view.findViewById(R.id.tripImgCard);

            editBtn = (ImageButton) view.findViewById(R.id.editActionTrip);
            deleteBtn = (ImageButton) view.findViewById(R.id.deleteActionTrip);

        }
    }

    public MyTripAdapter(Context mContext, List<Trip> trips) {
        this.mContext = mContext;
        this.trips = trips;
    }

    @Override
    public MyTripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_trip_cardview, parent,false);
        return new MyTripAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyTripAdapter.MyViewHolder holder, int position) {

        Trip trip = trips.get(position);
        holder.username.setText(trip.getUsername());
        holder.total_budget.setText("$ "+Integer.toString(trip.getBudget()));
        holder.titleTrip.setText(trip.getTitle());
        holder.trip_date.setText(trip.getTrip_date().toString());
        holder.likes.setText(Integer.toString(trip.getLikes())+ " likes");
        Glide.with(mContext).load(trip.getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.user_image);

        Glide.with(mContext).load(trip.getTrip_image()).into(holder.trip_image);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

}
