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
import com.tripget.tripget.R;
import com.tripget.tripget.Model.Trip;

import java.util.List;
/**
 * Created by ivonne on 08/08/17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {


    private Context mContext;
    private List<Trip> trips;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, total_budget, title, trip_date, votes, saved, likeText, saveText;
        public ImageView user_image, trip_image;
        public ImageButton like, save;

        public MyViewHolder(View view) {
            super(view);

            username = (TextView) view.findViewById(R.id.usernameTrip);
            total_budget = (TextView) view.findViewById(R.id.totalBudgetTrip);
            title = (TextView) view.findViewById(R.id.titleTripCard);
            trip_date = (TextView) view.findViewById(R.id.dateTripCard);
            votes = (TextView) view.findViewById(R.id.counterLikesTrip);
            saved = (TextView) view.findViewById(R.id.counterSavedTrips);

            likeText = (TextView) view.findViewById(R.id.likeText);
            saveText = (TextView) view.findViewById(R.id.saveText);

            user_image = (ImageView) view.findViewById(R.id.userImgTrip);
            trip_image = (ImageView) view.findViewById(R.id.tripImgCard);

            like = (ImageButton) view.findViewById(R.id.likeActionTrip);
            save = (ImageButton) view.findViewById(R.id.savedActionTrip);


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View button) {

                    if (button.isSelected()){
                        save.setSelected(false);
                        saveText.setText("Save");
                    }else{
                        save.setSelected(true);
                        saveText.setText("Saved");
                    }
                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View button) {
                    if (button.isSelected()){
                        like.setSelected(false);
                        likeText.setText("Like");
                    } else {
                        like.setSelected(true);
                        likeText.setText("Liked");
                    }
                }
            });

        }
    }

    public TripAdapter(Context mContext, List<Trip> trips) {
        this.mContext = mContext;
        this.trips = trips;
    }

    @Override
    public TripAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_cardview, parent,false);

        return new TripAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TripAdapter.MyViewHolder holder, int position) {

        Trip trip = trips.get(position);

        holder.username.setText(trips.get(position).getUsername());
        holder.total_budget.setText("$ "+Integer.toString(trips.get(position).getBudget()));
        holder.title.setText(trip.getTitle());
        holder.trip_date.setText(trips.get(position).getTrip_date().toString());

        holder.votes.setText(Integer.toString(trips.get(position).getVotes())+ " likes");
        //holder.saved.setText(Integer.toString(trip.getSaved()) + " saved");

        Glide.with(mContext).load(trips.get(position).getUser_image()).into(holder.user_image);
        Glide.with(mContext).load(trips.get(position).getTrip_image()).into(holder.trip_image);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }
}
