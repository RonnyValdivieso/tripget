package com.tripget.tripget;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BestBudgetFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class BestBudgetFragment extends Fragment {



    Activity activity ;

    //
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<Trip> tripList;
    private ImageView imageViewBack;
    private Spinner spinnerFilter;


    public BestBudgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_best_budget, container, false);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TripFormFragment fragment = new TripFormFragment();
                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(R.id.main_content, fragment).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
            }
        });

        spinnerFilter = (Spinner) view.findViewById(R.id.spinner_filter);
        String[] filter = getResources().getStringArray(R.array.filters_array);
        ArrayAdapter<String> adapterFilters =
                new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, filter);
        spinnerFilter.setAdapter(adapterFilters);


        imageViewBack = (ImageView)view.findViewById(R.id.backrop);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        tripList = new ArrayList<>();
        adapter = new TripAdapter(activity,tripList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareTrips();

        try{
            Glide.with(activity).load(R.drawable.new_york).into(imageViewBack);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void prepareTrips() {

        int [] covers = new int[]{
                R.drawable.galapagos,
                R.drawable.newyork2,
                R.drawable.newyork3,
                R.drawable.madrid
        };
        int [] userImg = new int[]{
                R.drawable.p1,
                R.drawable.p3,
                R.drawable.p4
        };

        Trip a = new Trip(1, "Pedro Ortiz", "It was the best trip I have ever had", 2000, 50, 10, userImg[0], covers[1], "Oct, 2016" );
        tripList.add(a);
        Trip b = new Trip(2, "Francis Bermúdez", "Amazing journey with new friends", 2000, 35, 5, userImg[1], covers[2], "Oct, 2016" );
        tripList.add(b);
        Trip c = new Trip(3, "Valeria Ramos", "New York, pure love", 1500, 500, 335, userImg[2], covers[3], "Oct, 2016" );
        tripList.add(c);

        adapter.notifyDataSetChanged();

    }


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
