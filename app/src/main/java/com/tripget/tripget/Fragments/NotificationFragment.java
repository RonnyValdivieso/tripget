package com.tripget.tripget.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.tripget.tripget.Adapters.NotificationAdapter;
import com.tripget.tripget.Adapters.TripAdapter;
import com.tripget.tripget.Conexion.Constantes;
import com.tripget.tripget.Conexion.VolleySingleton;
import com.tripget.tripget.Model.Notification;
import com.tripget.tripget.Model.Trip;
import com.tripget.tripget.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotificationFragment extends Fragment {

    Activity activity;
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private Gson gson = new Gson();


    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;
    SharedPreferences sharedpreferences;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        activity = getActivity();

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        notificationList = new ArrayList<>();
        //adapter = new NotificationAdapter(activity,notificationList);

        sharedpreferences = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.setAdapter(adapter);

        loadNotification();
        return view;
    }

    private void loadNotification() {
        String channel = (sharedpreferences.getString("id", ""));

        HashMap <String,String> notificationHash = new LinkedHashMap<>();
        notificationHash.put("id", channel);

        JSONObject jobject = new JSONObject(notificationHash);

        Log.d(TAG, jobject.toString());
        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.GET_NOTIFICATION_BY_ID,
                                jobject,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        getTripsJson(response);
                                    }
                                },
                                new Response.ErrorListener(){

                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "ERROR VOLLEY: " + error.getMessage());
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

    private void getTripsJson(JSONObject response) {

        try {
            String status = response.getString("status");
            System.out.print(status);

            switch (status){
                case "1":
                    JSONArray tripsJson = response.getJSONArray("notification");
                    System.out.print(tripsJson.toString());
                    Notification[] notifications  = gson.fromJson(tripsJson.toString(), Notification[].class);
                    adapter = new NotificationAdapter(activity, Arrays.asList(notifications));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case "2": //FAIL
                    Toast.makeText(getContext(), R.string.no_notifications, Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
