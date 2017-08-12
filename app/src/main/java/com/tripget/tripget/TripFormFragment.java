package com.tripget.tripget;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TripFormFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TripFormFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private static final int PICK_IMAGE = 100;
    Activity activity;

    Button buttonfragment;
    ImageButton buttonphoto;
    Button buttonDatePicker;
    ImageView photo_gallery_pick;
    EditText story_review;
    Spinner spinner_trip_type;
    Spinner spinner_trip_duration;

    public TripFormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Get mainActivity
        activity = getActivity();

        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_trip_form, container, false);

        story_review = (EditText)view.findViewById(R.id.story_review_text);
        buttonfragment = (Button)view.findViewById(R.id.save_review);
        photo_gallery_pick = (ImageView)view.findViewById(R.id.upload_photo_cont);
        buttonDatePicker = (Button)view.findViewById(R.id.button_date_picker);
       buttonphoto = (ImageButton) view.findViewById(R.id.upload_photo);
        spinner_trip_type = (Spinner)view.findViewById(R.id.trip_type_spinner);
        spinner_trip_duration = (Spinner)view.findViewById(R.id.trip_duration_spinner);


        // Get a reference to the AutoCompleteTextView in the layout
        AutoCompleteTextView destinationView = (AutoCompleteTextView)view.findViewById(R.id.autocomplete_country);

        // Get the string array
        String[] countries = getResources().getStringArray(R.array.countries_array);
        String[] trip_type = getResources().getStringArray(R.array.trip_type);
        String[] trip_duration = getResources().getStringArray(R.array.trip_duration);

        // Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapterCountries =
                new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, countries);
        ArrayAdapter<String> adapterTripType =
                new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, trip_type);
        ArrayAdapter<String> adapterTripDuration =
                new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, trip_duration);

        destinationView.setAdapter(adapterCountries);
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

        activity = getActivity();
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
