package com.tripget.tripget.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.tripget.tripget.Adapters.PlaceAutocompleteAdapter;
import com.tripget.tripget.R;

import java.io.IOException;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TripFormFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    private OnFragmentInteractionListener mListener;
    private static final int PICK_IMAGE = 100;
    Activity activity;

    Button buttonfragment;
    ImageButton buttonphoto;
    Button buttonDatePicker;
    ImageView photo_gallery_pick;
    EditText story_review;
    AutoCompleteTextView mautoCompleteTextView;
    Spinner spinner_trip_type;
    Spinner spinner_trip_duration;

    //GooglePlaces
    private static final String TAG = BestBudgetFragment.class.getSimpleName();
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;
    private AutoCompleteTextView mAutocompleteView;
    private String placeId;


    public TripFormFragment() {
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


        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trip_form, container, false);

        story_review = (EditText)view.findViewById(R.id.story_review_text);
        buttonfragment = (Button)view.findViewById(R.id.save_review);
        photo_gallery_pick = (ImageView)view.findViewById(R.id.upload_photo_cont);
        buttonDatePicker = (Button)view.findViewById(R.id.button_date_picker);
        buttonphoto = (ImageButton) view.findViewById(R.id.upload_photo);
        spinner_trip_type = (Spinner)view.findViewById(R.id.trip_type_spinner);
        spinner_trip_duration = (Spinner)view.findViewById(R.id.trip_duration_spinner);




        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
        String[] trip_type = getResources().getStringArray(R.array.trip_type);
        String[] trip_duration = getResources().getStringArray(R.array.trip_duration);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterCountries =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, countries);
        ArrayAdapter<String> adapterTripType =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, trip_type);
        ArrayAdapter<String> adapterTripDuration =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, trip_duration);

        //destinationView.setAdapter(adapterCountries);
        spinner_trip_type.setAdapter(adapterTripType);
        spinner_trip_duration.setAdapter(adapterTripDuration);


       buttonphoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openGallery();
            }
        });

       buttonfragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.story_saved, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
            }
        });

        buttonfragment.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });


        //Google Places

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mautoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.autocomplete_new_destination);
        mautoCompleteTextView.setOnItemClickListener(mAutocompleteClickListener);

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();


        mAdapter = new PlaceAutocompleteAdapter(getContext(), mGoogleApiClient, null,typeFilter);
        mautoCompleteTextView.setAdapter(mAdapter);

        return view;
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

           /* edittext.setText(String.valueOf(dayOfMonth) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(year));*/
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == activity.RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImage);
                photo_gallery_pick.setImageBitmap(bitmap);
                photo_gallery_pick.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void openGallery() {

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }

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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

    //Google PLaces
    //Cosas pa Probar

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
