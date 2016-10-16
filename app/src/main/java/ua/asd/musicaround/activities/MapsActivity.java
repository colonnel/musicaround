package ua.asd.musicaround.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import ua.asd.musicaround.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean isMapInit = false;
    private boolean isLocationRecieved;
    public static final String LAT = "LAT";
    public static final String LNG = "LNG";
    private GoogleApiClient mGoogleApiClient = null;
    private LocationManager locationManager;
    private Location mCurrentLocation;
    public String soundName = "Yesterday";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = new LocationManager(this);
        locationManager.initGoogleApiClient(new LocationManager.LocationDeterminedCallback() {
            @Override
            public void getLocation(Location location) {
                mCurrentLocation = location;
                isLocationRecieved = true;
                locationManager.stopLocationUpdates();
                if (isMapInit) {
                    actionAfterInitMapAndReceiveLocation();
                }
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        isMapInit = true;
        if (isLocationRecieved) {
            actionAfterInitMapAndReceiveLocation();
        }
    }

    private void actionAfterInitMapAndReceiveLocation() {
        LatLng currentUserLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        Marker user1 = mMap.addMarker(new MarkerOptions().position(currentUserLocation).title(soundName));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
        animateTo(currentUserLocation, 14);
        user1.showInfoWindow();
//        mMap.setOnInfoWindowClickListener(OnInfoWindowClickListener);

    }

    protected void onInfoWindowClick(Marker marker){

    }

    protected void animateTo(LatLng coordinates, int zoomParam) {
        if (mMap != null) {
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(coordinates, zoomParam);
            mMap.animateCamera(zoom);
        }
    }

    private void slideUp(){
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.slideup);
        FrameLayout vPlayer = (FrameLayout) findViewById(R.id.player);
    }
}


