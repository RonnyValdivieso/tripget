package com.tripget.tripget.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.tripget.tripget.Adapters.PlaceAutocompleteAdapter;
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
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BestBudgetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BestBudgetFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Activity activity ;


    private static final String TAG = BestBudgetFragment.class.getSimpleName();
    private Gson gson = new Gson();

    //
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<Trip> tripList;
    private ImageView imageViewBack;
    private Spinner spinnerFilter;
    private ImageButton searchButton;
    private EditText destination, budget;
    private TextView txtNoTrips;
    private String destination_get, budget_get;
    private String placeId = " ";
    private String orderBy;
    private boolean ban = false;


    private String placeService, budgetService;
    SharedPreferences sharedpreferences;

    //GooglePlaces
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;


    public BestBudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity()).addConnectionCallbacks(this).addApi(Places.GEO_DATA_API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

    }

    @Override
    public void onStop() {
        //super.onStop();
        //mGoogleApiClient.disconnect();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


       // Log.d("ERROR", mGoogleApiClient.toString());
        //Activity
        activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_budget, container, false);

        //RecyclerView
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity,2);

        //Init Elements

        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TripFormFragment fragment = new TripFormFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.main_content, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        });

        searchButton = (ImageButton) view.findViewById(R.id.search_budget);
        spinnerFilter = (Spinner) view.findViewById(R.id.spinner_filter);
        //destination= (EditText) view.findViewById(R.id.destination_search);
        budget = (EditText) view.findViewById(R.id.budget_search);
        imageViewBack = (ImageView)view.findViewById(R.id.backrop);
        txtNoTrips = (TextView)view.findViewById(R.id.txtNoTrips);
        txtNoTrips.setVisibility(View.VISIBLE);
        txtNoTrips.setText("Search your next trip budget");
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);


        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_places);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        //Elements in Action

        String[] filter = getResources().getStringArray(R.array.filters_array);
        ArrayAdapter<String> adapterFilters =
                new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, filter);
        spinnerFilter.setAdapter(adapterFilters);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();


        mAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient, null,typeFilter);
        mAutocompleteView.setAdapter(mAdapter);

        sharedpreferences = this.getActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);



        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderBy = String.valueOf(position);
                if (ban == true){
                    sendHashMapToService(placeService,budgetService);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                orderBy = "0";
            }
        });


        //Start Searching

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (placeId.toString() != " " || budget.getText().length()!=0){
                    ban = true;
                    if (placeId.toString() != " " && budget.getText().length()!=0){
                        placeService = placeId.toString();
                        budgetService = String.valueOf(budget.getText());
                        placeId = " ";
                        sendHashMapToService(placeService, budgetService);
                    } else if (placeId.toString() != " " && budget.getText().length()== 0 ){
                        budgetService = " ";
                        placeService = placeId.toString();
                        placeId = " ";
                        sendHashMapToService(placeService, budgetService);
                    } else if (placeId.toString() == " " && budget.getText().length()!=0){
                        budgetService = String.valueOf(budget.getText());
                        placeService = " ";
                        sendHashMapToService(placeService, budgetService);
                    }
                }else {
                    placeId = " ";
                    Toast.makeText(getContext(), "Search by destination, budget or both", Toast.LENGTH_LONG).show();
                }
            }
        });
        try{
            Glide.with(activity).load(R.drawable.new_york).into(imageViewBack);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void sendHashMapToService(String placeService, String budgetService) {

        String channel = (sharedpreferences.getString("id", ""));

        if (placeService != " " && budgetService!= " "){
            HashMap <String,String> tripHash = new LinkedHashMap<>();
            tripHash.put("destination", placeService);
            tripHash.put("budget", budgetService);
            tripHash.put("order", orderBy);
            tripHash.put("user_id", channel);
            loadAdapterTrips(tripHash);
        } else if (placeService != " " && budgetService == " " ){
            HashMap <String, String> destinationHash = new HashMap<>();
            destinationHash.put("destination",placeService);
            destinationHash.put("order", orderBy);
            destinationHash.put("user_id",channel);
            loadAdapterDestination(destinationHash);
        } else if (placeService == " " && budgetService !=" "){
            HashMap <String, String> budgetHash = new HashMap<>();
            budgetHash.put("budget", budgetService);
            budgetHash.put("order", orderBy);
            budgetHash.put("user_id", channel);
            loadAdapterBudget(budgetHash);
        }
    }


    /**Adapter**/

    //ByDestination &ByBudget

    public void loadAdapterTrips(HashMap<String, String> tripHash){

        //destination_get = tripHash.get("destination");
        JSONObject jobjecthis = new JSONObject(tripHash);

        Log.d(TAG, jobjecthis.toString());
        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.GET_TRIPS,
                                jobjecthis,
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
    //ByDestination

    public void loadAdapterDestination(HashMap<String, String> destinationHash){

        destination_get = destinationHash.get("destination");
        Log.d(TAG, destination_get);

        JSONObject jobject = new JSONObject(destinationHash);

        Log.d(TAG, jobject.toString());
        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.GET_TRIPS_BY_DESTINATION,
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

    //ByBudget
    public void loadAdapterBudget(HashMap<String, String> budgetHash){

        JSONObject jobject = new JSONObject(budgetHash);

        Log.d(TAG, jobject.toString());
        VolleySingleton.getInstance(getContext()).
                addToRequestQueue(
                        new JsonObjectRequest(Request.Method.POST,
                                Constantes.GET_TRIPS_BY_BUDGET,
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

    /**JsonObject to JsonArray**/

    private void getTripsJson(JSONObject response) {

        try {
            String status = response.getString("status");
            System.out.print(status);

            switch (status){
                case "1":
                    txtNoTrips.setVisibility(View.INVISIBLE);
                    JSONArray tripsJson = response.getJSONArray("trips");
                    System.out.print(tripsJson.toString());
                    Trip[] trips  = gson.fromJson(tripsJson.toString(), Trip[].class);
                    adapter = new TripAdapter(activity, Arrays.asList(trips));
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
                case "2": //FAIL
                    String message2 =  response.getString("message");
                    txtNoTrips.setVisibility(View.VISIBLE);
                    txtNoTrips.setText("No trips available");
                    /*Toast.makeText(activity,message2, Toast.LENGTH_SHORT).show();*/
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

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
    
    
    //Style Grid

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    //Google Places Implementation

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);
            Log.i(TAG, "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);

            Log.i(TAG, "Called getPlaceById to get Place details for " + placeId);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            // Format details of the place for display and show it in a TextView.
            /*mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));*/

            // Display the third party attributions if set.
           /* final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/

            Log.i(TAG, "Place details received: " + place.getName());

            places.release();
        }
    };

}
