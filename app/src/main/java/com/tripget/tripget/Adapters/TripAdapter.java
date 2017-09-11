package com.tripget.tripget.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tripget.tripget.Activity.MainActivity;
import com.tripget.tripget.Conexion.Constantes;
import com.tripget.tripget.Conexion.VolleySingleton;
import com.tripget.tripget.Fragments.DetailTripFragment;
import com.tripget.tripget.Fragments.TripFormFragment;
import com.tripget.tripget.R;
import com.tripget.tripget.Model.Trip;
import com.tripget.tripget.common.logger.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by ivonne on 08/08/17.
 */

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private Context mContext;
    private List<Trip> trips;
    SharedPreferences sharedpreferences;


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

            sharedpreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


            /*likeBtn.setOnClickListener(new View.OnClickListener() {
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
            });*/
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
    public void onBindViewHolder(TripAdapter.MyViewHolder holder1, final int position) {
        final Trip trip = trips.get(position);

        final TripAdapter.MyViewHolder holder = holder1;
        holder.username.setText(trip.getUsername());
        holder.total_budget.setText("$ "+Integer.toString(trips.get(position).getBudget()));
        holder.title.setText(trip.getTitle());
        holder.trip_date.setText(trip.getTrip_date().toString());
        holder.likes.setText(Integer.toString(trip.getLikes())+ mContext.getString(R.string.likes));
        Glide.with(mContext).load(trip.getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.user_image);
        if (trip.getLiked() == 1){
            holder.likeBtn.setSelected(true);
            holder.likeText.setText(R.string.liked);
        }else {
            holder.likeBtn.setSelected(false);
            holder.likeText.setText(R.string.like_one);
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

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.isSelected()){
                    holder.likeBtn.setSelected(false);
                    holder.likeText.setText(R.string.liked);
                } else {
                    holder.likeBtn.setSelected(true);
                    holder.likeText.setText(R.string.like_one);
                    String channel = (sharedpreferences.getString("id", ""));
                    loadLike(trip.getId(), channel);
                }
            }
        });
    }

    private void loadLike(int id, String channel) {

        HashMap <String,String> tripHash = new LinkedHashMap<>();
        tripHash.put("trip_id", String.valueOf(id));
        tripHash.put("user_id", channel);

        JSONObject jobjecthis = new JSONObject(tripHash);

        android.util.Log.d(TAG, jobjecthis.toString());
        VolleySingleton.getInstance(mContext).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.INSERT_LIKE,
                                jobjecthis,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        try {
                                            String status = response.getString("status");
                                            System.out.print(status);

                                            switch (status){
                                                case "1":
                                                    Toast.makeText(mContext,"You like this post", Toast.LENGTH_SHORT).show();
                                                    break;
                                                case "2": //FAIL
                                                    Toast.makeText(mContext, "You have already liked this post", Toast.LENGTH_SHORT).show();
                                                    break;
                                            }
                                        }catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                    }
                                },
                                new Response.ErrorListener(){

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        android.util.Log.d(TAG, "ERROR VOLLEY: " + error.getMessage());
                                    }
                                }) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }
                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

}
