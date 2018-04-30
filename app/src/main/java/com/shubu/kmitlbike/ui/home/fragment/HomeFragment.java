package com.shubu.kmitlbike.ui.home.fragment;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.annimon.stream.Stream;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shubu.kmitlbike.R;
import com.shubu.kmitlbike.data.model.bike.Bike;
import com.shubu.kmitlbike.ui.base.BaseFragment;
import com.shubu.kmitlbike.ui.common.CONSTANTS;
import com.shubu.kmitlbike.ui.home.fragment.interfaces.DrawerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import timber.log.Timber;

public class HomeFragment extends BaseFragment implements OnMapReadyCallback  {


    @BindView(R.id.hamburgerMenu)
    ImageButton hamburgerMenu;


    private MapView mapView;
    private GoogleMap googleMap;
    private List<Bike> bikeList;
    private LatLng prevLocation;
    private List<Marker> markers;
    private DrawerListener mListener;
    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DrawerListener){
            mListener = (DrawerListener) context;
        }
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

        eventBus.getLocation().subscribe(new Subscriber<Location>() {
            @Override
            public void onCompleted() { }

            @Override
            public void onError(Throwable e) { }

            @Override
            public void onNext(Location location) {
                updateTrackingLocation(location);
            }
        });
    }

    private void updateTrackingLocation(Location location){
        if (markers != null){
            Stream.of(markers).forEach( marker -> marker.remove() );
            markers = null;
        }

        if (location == null)
            return;
        //updqate user location
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));

        Polyline line = this.googleMap.addPolyline(
                new PolylineOptions().add(
                        this.prevLocation,
                        new LatLng(location.getLatitude(), location.getLongitude())
                ).width(2).color(Color.GREEN).geodesic(true)
        );
        this.prevLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private void updateBikeLocation(){
        // slow internet case : map was loaded faster then api
        // fast internet case : map hasn't loaded but receive bikelist already

        if (this.bikeList != null && googleMap != null) {
            googleMap.clear();
            List<MarkerOptions> markerOptions;
            markerOptions = Stream.of(this.bikeList).map(bike -> {
                return new MarkerOptions().position(new LatLng(bike.getLatitude(), bike.getLongitude())).title(bike.getBikeModel()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bike_location));
            }).toList();
            markers = Stream.of(markerOptions).map( markerOptions1 -> googleMap.addMarker(markerOptions1)).toList();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.initializeBikeService();
        eventBus.getMapEvent().subscribe( location -> {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        hamburgerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOpenDrawer();
            }
        });
        this.mapView = (MapView) rootView.findViewById(R.id.mapView);
        this.mapView.onCreate(savedInstanceState);
        this.mapView.onResume();
        this.mapView.getMapAsync(this);

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap mMap) {

        try {
            googleMap = mMap;
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity() ,R.raw.map_style ));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CONSTANTS.KMITL_LOCATION, 18));

            this.updateBikeLocation();
        } catch ( SecurityException e){
            Timber.e(e);
        }

    }




    private GeofencingRequest getGeofencingRequest(Geofence geofence) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofence(geofence);
        return builder.build();
    }

}
