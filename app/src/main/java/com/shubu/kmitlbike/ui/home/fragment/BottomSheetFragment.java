package com.shubu.kmitlbike.ui.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseBottomSheetFragment;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.ScannerListener;

import butterknife.ButterKnife;


public class BottomSheetFragment extends BaseBottomSheetFragment {


    private ScannerListener mListener;

    public static BottomSheetFragment newInstance(String param1, String param2) {
        BottomSheetFragment fragment = new BottomSheetFragment();
        return fragment;
    }
    public BottomSheetFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void setRideOnClick() {
        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bListener.onToggle();
            }
        });
    }

    @Override
    protected void setFooterButton() {
        footerButton.setText("GOT IT");
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onScannerStart();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        ButterKnife.bind(this,view);
        // Inflate the layout for this fragment
        this.setRideOnClick();
        this.setFooterButton();
        this.setbListener();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ScannerListener) {
            mListener = (ScannerListener) context;

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

}
