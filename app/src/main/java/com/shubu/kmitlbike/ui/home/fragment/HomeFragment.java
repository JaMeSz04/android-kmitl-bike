package com.shubu.kmitlbike.ui.home.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shubu.kmitlbike.KMITLBikeApplication;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.Bike;
import com.shubu.kmitlbike.ui.base.BaseFragment;
import com.shubu.kmitlbike.ui.common.CONSTANTS;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import timber.log.Timber;

public class HomeFragment extends BaseFragment implements OnMapReadyCallback  {


    private MapView mapView;
    private GoogleMap googleMap;
    private List<Bike> bikeList;
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void initializeBikeService(){
        Timber.i("in fragment!!!");
        eventBus.getBike().subscribe( (bikes -> {
            this.bikeList = bikes;
        }));
    }

    private void updateBikeLocation(){
        // slow internet case : map was loaded faster then api
        // fast internet case : map hasn't loaded but receive bikelist already

        if (this.bikeList != null && googleMap != null) {
            googleMap.clear();
            List<MarkerOptions> markers;
            markers = Stream.of(this.bikeList).map(bike -> {
                return new MarkerOptions().position(new LatLng(bike.getLatitude(), bike.getLongitude()));
            }).toList();

            Stream.of(markers).forEach(marker -> googleMap.addMarker(marker));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.initializeBikeService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        this.mapView = (MapView) rootView.findViewById(R.id.mapView);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.onResume();
        this.mapView.getMapAsync(this);

        // Inflate the layout for this fragment
        return rootView;
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

    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CONSTANTS.KMITL_LOCATION, 17));
        this.updateBikeLocation();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
