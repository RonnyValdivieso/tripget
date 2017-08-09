package com.tripget.tripget;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

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
    private  TripAdapter adapter;
    private List<Trip> tripList;
    private ImageView imageViewBack;


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

        imageViewBack = (ImageView)view.findViewById(R.id.backrop);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        tripList = new ArrayList<>();
        adapter = new TripAdapter(activity,tripList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(activity,2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareTrips();

        try{
            Glide.with(activity).load(R.drawable.banner_upload).into(imageViewBack);
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
                R.drawable.p3,
                R.drawable.p4
        };

        Trip a = new Trip("Pedro Ortiz", "$2000",userImg[0], covers[0],"2k","Galapagos, 12 Oct, 2016" );
        tripList.add(a);
        Trip b = new Trip("Francis Bermudez", "$5000",userImg[1], covers[1],"2k","New York , 12 Oct, 2016" );
        tripList.add(b);
        Trip c = new Trip("Francis Bermudez", "$200",userImg[2], covers[2],"2k","New York, 12 Oct, 2016" );
        tripList.add(c);
        Trip d = new Trip("Valeria Ramos", "$2000",userImg[3], covers[3],"2k","Madrid, 12 Oct, 2016" );
        tripList.add(d);
        Trip e = new Trip("Valeria Ramos", "$2000",userImg[3], covers[3],"2k","Madrid, 12 Oct, 2016" );
        tripList.add(e);
        Trip f = new Trip("Valeria Ramos", "$2000",userImg[3], covers[3],"2k","Madrid, 12 Oct, 2016" );
        tripList.add(f);

        adapter.notifyDataSetChanged();

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
