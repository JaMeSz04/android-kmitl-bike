package com.shubu.kmitlbike.ui.home.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.ui.base.BaseBottomSheetFragment;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.StatusListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class StatusFragment extends BaseBottomSheetFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MAC_ADDRESS = "mac-address";
    private StatusListener mListener;

    // TODO: Rename and change types of parameters
    private String macAddress = null;

    @BindView(R.id.BikeStatusText) TextView statusText;
    @BindView(R.id.BikeStatusProgressBar) ProgressBar progressBar;

    public StatusFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static StatusFragment newInstance(String macAddress) {
        StatusFragment fragment = new StatusFragment();
        if (macAddress != null) {
            Bundle args = new Bundle();
            args.putString(MAC_ADDRESS, macAddress);
        }
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            macAddress = getArguments().getString(MAC_ADDRESS);
        }
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
        footerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onStatusBorrowCompleted();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_status, container, false);
        ButterKnife.bind(this,view);
        setRideOnClick();
        setFooterButton();
        this.setbListener();
        footerButton.setVisibility(View.INVISIBLE);
        eventBus.getBikeState().subscribe( state -> {
            Timber.i("on new state : " + state.toString());
            this.statusText.setText(state.toString());
        });
        eventBus.getBikePassword().subscribe( password -> {
            this.progressBar.setVisibility(View.INVISIBLE);
            this.statusText.setText("Your bike password : " + password);
            footerButton.setVisibility(View.VISIBLE);
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatusListener) {
            mListener = (StatusListener) context;
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
