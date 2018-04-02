package com.shubu.kmitlbike.ui.home.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BikeInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BikeInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BikeInfoFragment extends BaseFragment {

    @BindView(R.id.BikeInfoTitle) TextView titleText;

    private OnFragmentInteractionListener mListener;

    public BikeInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BikeInfoFragment newInstance(String param1, String param2) {
        BikeInfoFragment fragment = new BikeInfoFragment();
        return fragment;
    }

    private void initializeBikeStateService(){
        eventBus.getBikeState().subscribe( state -> {
            titleText.setText(state.toString());

        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        this.initializeBikeStateService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bike_info, container, false);
        ButterKnife.bind(this,view);
        return view;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
