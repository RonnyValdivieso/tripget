package com.tripget.tripget.Adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripget.tripget.Activity.MainActivity;
import com.tripget.tripget.Fragments.DetailTripFragment;
import com.tripget.tripget.Fragments.TripFormFragment;
import com.tripget.tripget.R;
import com.tripget.tripget.Model.Trip;
import com.tripget.tripget.common.logger.Log;

import java.util.List;
/**
 * Created by ivonne on 08/08/17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private Context mContext;
    private List<Trip> trips;
    MainActivity myActivity = (MainActivity) mContext;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, total_budget, title, trip_date, likes, likeText;
        public ImageView user_image, trip_image;
        public ImageButton likeBtn;
        public CardView cardView;





        public MyViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.usernameTrip);
            total_budget = (TextView) view.findViewById(R.id.totalBudgetTrip);
            title = (TextView) view.findViewById(R.id.titleTripCard);
            trip_date = (TextView) view.findViewById(R.id.dateTripCard);
            likes = (TextView) view.findViewById(R.id.counterLikesTrip);

            likeText = (TextView) view.findViewById(R.id.likeText);

            user_image = (ImageView) view.findViewById(R.id.userImgTrip);
            trip_image = (ImageView) view.findViewById(R.id.tripImgCard);

            likeBtn = (ImageButton) view.findViewById(R.id.likeActionTrip);

            cardView = (CardView) view.findViewById(R.id.cardView);

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View button) {
                    if (button.isSelected()){
                        likeBtn.setSelected(false);
                        likeText.setText("Like");
                    } else {
                        likeBtn.setSelected(true);
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

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_cardview, parent,false);

        return new TripAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(TripAdapter.MyViewHolder holder, final int position) {
        final Trip trip = trips.get(position);
        holder.username.setText(trip.getUsername());
        holder.total_budget.setText("$ "+Integer.toString(trips.get(position).getBudget()));
        holder.title.setText(trip.getTitle());
        holder.trip_date.setText(trip.getTrip_date().toString());
        holder.likes.setText(Integer.toString(trip.getLikes())+ " likes");
        Glide.with(mContext).load(trip.getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.user_image);
        if (trip.getLiked() == 1){
            holder.likeBtn.setSelected(true);
            holder.likeText.setText("Liked");
        }else {
            holder.likeBtn.setSelected(false);
            holder.likeText.setText("Like");
        }
        Glide.with(mContext).load(trip.getTrip_image()).into(holder.trip_image);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                DetailTripFragment myFragment = new DetailTripFragment();
                //Create a bundle to pass data, add data, set the bundle to your fragment and:

                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(trip.getId()));
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, myFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

}
