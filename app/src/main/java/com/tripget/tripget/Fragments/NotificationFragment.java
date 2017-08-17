package com.tripget.tripget.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tripget.tripget.Adapters.NotificationAdapter;
import com.tripget.tripget.Model.Notification;
import com.tripget.tripget.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NotificationFragment extends Fragment {

    Activity activity ;

    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

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
        adapter = new NotificationAdapter(activity,notificationList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadNotification();
        return view;
    }

    private void loadNotification() {
        int [] covers = new int[]{
                R.drawable.new_york,
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

        Notification a = new Notification("Pedro Ortiz", " liked your trip ", userImg[0],covers[0] );
        notificationList.add(a);
        Notification b = new Notification("Pedro Ortiz", " saved your trip ", userImg[0],covers[0] );
        notificationList.add(b);
        Notification c = new Notification("Francis Bermudez", " liked your trip ", userImg[1],covers[0] );
        notificationList.add(c);
        Notification d = new Notification("Francis Bermudez", " saved your trip ", userImg[1],covers[0] );
        notificationList.add(d);
        Notification f = new Notification("Valeria Ramos", " saved your trip ", userImg[2],covers[0] );
        notificationList.add(f);

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
