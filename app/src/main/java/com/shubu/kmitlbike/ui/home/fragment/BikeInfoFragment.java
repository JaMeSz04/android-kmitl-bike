package com.shubu.kmitlbike.ui.home.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.ui.base.BaseFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BikeInfoFragment extends BaseFragment {

    @BindView(R.id.BikeInfoTitle) TextView titleText;
    @BindView(R.id.BikeInfoSubTitle) TextView subtitleText;
    @BindView(R.id.BIkeInfoBikeName) TextView nameText;
    @BindView(R.id.BikeInfoLock) TextView lockText;
    @BindView(R.id.BikeInfoImage) ImageView bikeImage;
    @BindView(R.id.BikeInfoScannerButton) ImageButton scanButton;

    private String bikeName = "";
    private String bikeModel = "";
    private ScannerListener mListener;

    public BikeInfoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BikeInfoFragment newInstance(String bikeName, String bikeModel) {

        BikeInfoFragment fragment = new BikeInfoFragment();
        Bundle args = new Bundle();
        args.putString("name", bikeName);
        args.putString("model", bikeModel);
        fragment.setArguments(args);
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
        if (getArguments() != null) {
            bikeName = getArguments().getString("name");
            bikeModel = getArguments().getString("model");
        } else {
            // TODO: 4/3/2018  raise error bike not found!!!
        }

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
        this.subtitleText.setText(this.bikeModel);
        this.nameText.setText(this.bikeName);
        this.lockText.setText(this.bikeModel.equals("GIANT Escape 3")? "InfiniLock" : "Manual Lock" );
        this.bikeImage.setImageResource(this.bikeModel.equals("GIANT Escape 3")? R.drawable.giant_escape_cut : R.drawable.la_green_cut);
        this.scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onScannerStart();
            }
        });
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

}
