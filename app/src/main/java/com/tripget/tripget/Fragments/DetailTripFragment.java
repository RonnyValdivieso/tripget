package com.tripget.tripget.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.tripget.tripget.Adapters.TripAdapter;
import com.tripget.tripget.Conexion.Constantes;
import com.tripget.tripget.Conexion.VolleySingleton;
import com.tripget.tripget.Model.Trip;
import com.tripget.tripget.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DetailTripFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DetailTripFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static final String TAG = DetailTripFragment.class.getSimpleName();
    private Gson gson = new Gson();

    //ID TRIP
    String myInt;

    //GUI ELEMENTS

    private TextView detailFoodTxt, detailTripTransportationTxt, detailLocalTransportationTxt, detailAcommodationTxt, detailShoppingTxt, detailEnterTxt;

    private TextView budgetFor, budgetTotal;

    private TextView username,title,date,context;

    private ImageView userImage, tripImage;

    private ExpandableTextView expTv1;


    public DetailTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            myInt = bundle.getString("id", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_detail_trip, container, false);

        //ELEMENTS GUI//

        userImage = (ImageView)rootView.findViewById(R.id.userImgTrip);
        username = (TextView)rootView.findViewById(R.id.usernameTrip);
        title = (TextView)rootView.findViewById(R.id.titleTripCard);
        tripImage = (ImageView)rootView.findViewById(R.id.tripImgCard);
        budgetTotal = (TextView)rootView.findViewById(R.id.totalBudgetTrip);
        budgetFor = (TextView)rootView.findViewById(R.id.guest_duration_txt);

        expTv1 = (ExpandableTextView) rootView.findViewById(R.id.expand_text_view);


        detailFoodTxt = (TextView)rootView.findViewById(R.id.detailFieldFoodTxt);
        detailAcommodationTxt = (TextView)rootView.findViewById(R.id.detailFieldAccommodationTxt);
        detailTripTransportationTxt = (TextView)rootView.findViewById(R.id.detailFieldTransportationTxt);
        detailLocalTransportationTxt = (TextView)rootView.findViewById(R.id.detailFieldLocalTransportationTxt);
        detailEnterTxt = (TextView)rootView.findViewById(R.id.detailFieldEnterTxt);
        detailShoppingTxt = (TextView)rootView.findViewById(R.id.detailFieldShoppingTxt);

        //Toast.makeText(this.getContext(), myInt.toString(),Toast.LENGTH_SHORT).show();
        loadAdapterDetailTrip(myInt);
        return rootView;
    }

    private void loadAdapterDetailTrip(String myInt) {

        HashMap <String,String> tripHash = new LinkedHashMap<>();
        tripHash.put("id", myInt);
        JSONObject jobjecthis = new JSONObject(tripHash);

        Log.d(TAG, jobjecthis.toString());
        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.GET_TRIP_BY_ID,
                                jobjecthis,
                                new Response.Listener<JSONObject>(){
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        getTripDetailJson(response);
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

    private void getTripDetailJson(JSONObject response) {

        String arrayGuest[] = getResources().getStringArray(R.array.trip_type);
        String arrayDuration[] = getResources().getStringArray(R.array.trip_duration);
        try {
            String status = response.getString("status");
            System.out.print(status);

            switch (status){
                case "1":

                    String guest = " ", trip_duration = " ";

                    JSONArray usersJson = response.getJSONArray("trips");
                    JSONObject userNow = usersJson.getJSONObject(0);

                    switch (userNow.getString("guest_id")){
                        case "1":
                            guest = arrayGuest[0];
                            break;
                        case "2":
                            guest = arrayGuest[1];
                            break;
                        case "3":
                            guest = arrayGuest[2];
                            break;
                        case "4":
                            guest = arrayGuest[3];
                            break;
                    }


                    switch (userNow.getString("trip_duration_id")){
                        case "1":
                            trip_duration = arrayDuration[0];
                            break;
                        case "2":
                            trip_duration = arrayDuration[1];
                            break;
                        case "3":
                            trip_duration = arrayDuration[2];
                            break;
                        case "4":
                            trip_duration = arrayDuration[3];
                            break;
                        case "5":
                            trip_duration = arrayDuration[4];

                    }


                    username.setText(userNow.getString("username"));
                    title.setText(userNow.getString("title"));
                    expTv1.setText(userNow.getString("content"));
                    budgetTotal.setText("$ "+ userNow.getString("budget"));

                    Glide.with(this.getContext()).load(userNow.getString("trip_image")).into(tripImage);
                    Glide.with(this.getContext()).load(userNow.getString("photo")).apply(RequestOptions.circleCropTransform()).into(userImage);
                    detailFoodTxt.setText(userNow.getString("food"));
                    detailAcommodationTxt.setText(userNow.getString("accommodation"));
                    detailTripTransportationTxt.setText(userNow.getString("trip_transportation"));
                    detailLocalTransportationTxt.setText(userNow.getString("local_transportation"));
                    detailEnterTxt.setText(userNow.getString("entertainment"));
                    detailShoppingTxt.setText(userNow.getString("shopping"));


                    budgetFor.setText(guest + " / " + trip_duration);

                    break;

                case "2": //FAIL
                    String message2 =  response.getString("message");
                    Toast.makeText(this.getContext(),message2, Toast.LENGTH_SHORT).show();
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void loadGuiElements(JSONObject userNow) {
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
