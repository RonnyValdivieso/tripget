package com.tripget.tripget.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.tripget.tripget.Conexion.Constantes;
import com.tripget.tripget.Conexion.VolleySingleton;
import com.tripget.tripget.Fragments.DetailTripFragment;
import com.tripget.tripget.Fragments.TripEditFragment;
import com.tripget.tripget.Model.Trip;
import com.tripget.tripget.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public void onBindViewHolder(MyTripAdapter.MyViewHolder holder1, final int position) {

        final Trip trip = trips.get(position);
        final MyTripAdapter.MyViewHolder holder = holder1;
        holder.username.setText(trip.getUsername());
        holder.total_budget.setText("$ "+Integer.toString(trip.getBudget()));
        holder.titleTrip.setText(trip.getTitle());
        holder.trip_date.setText(trip.getTrip_date().toString());
        holder.likes.setText(Integer.toString(trip.getLikes())+ " likes");
        Glide.with(mContext).load(trip.getPhoto()).apply(RequestOptions.circleCropTransform()).into(holder.user_image);
        Glide.with(mContext).load(trip.getTrip_image()).into(holder.trip_image);


        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*int newPosition = holder.getAdapterPosition();
                Log.d("thien.van","on Click onBindViewHolder");
                trips.remove(newPosition);
                notifyItemRemoved(newPosition);
                notifyItemRangeChanged(newPosition, trips.size());*/
                deleteTrip(String.valueOf(trip.getId()));
            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                TripEditFragment myFragment = new TripEditFragment();
                //Create a bundle to pass data, add data, set the bundle to your fragment and:
                Bundle bundle = new Bundle();
                bundle.putString("id", String.valueOf(trip.getId()));
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_content, myFragment).addToBackStack(null).commit();

            }
        });

        holder.trip_image.setOnClickListener(new View.OnClickListener() {
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

    private void deleteTrip(String id) {

        //destination_get = tripHash.get("destination");

        HashMap <String,String> deleteTripHash = new LinkedHashMap<>();
        deleteTripHash.put("id", id);
        JSONObject jobjecthis = new JSONObject(deleteTripHash);
        Log.d("DELETE", jobjecthis.toString());

        final ProgressDialog loading = ProgressDialog.show(mContext,"Deleting...","Please wait...",false,false);

        VolleySingleton.getInstance(mContext).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.DELETE_TRIP,
                                jobjecthis,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        loading.dismiss();
                                        getDeleteResponse(response);
                                    }
                                },
                                new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loading.dismiss();
                                        Log.d("DELETE", "ERROR VOLLEY: " + error.getMessage());
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

    private void getDeleteResponse(JSONObject response) {

        try {
            String status = response.getString("status");
            System.out.print(status);

            switch (status){
                case "1":
                    Toast.makeText(mContext, "Success delete", Toast.LENGTH_SHORT).show();
                    break;
                case "2": //FAIL
                    Toast.makeText(mContext,"An error has occur, try again", Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

}
