package ua.asd.musicaround.activities;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
        if (isLocationRecieved){
            actionAfterInitMapAndReceiveLocation();
        }
    }

    private void actionAfterInitMapAndReceiveLocation() {
        LatLng currentUserLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(currentUserLocation).title("Marker in currentUserLocation"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentUserLocation));
        animateTo(currentUserLocation,14);
    }

    protected void animateTo(LatLng coordinates, int zoomParam) {
        if (mMap != null) {
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(coordinates, zoomParam);
            mMap.animateCamera(zoom);
        }
    }

}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
////        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.);
////        mapFragment.getMapAsync(this);
//        tvEnabledGPS = (TextView) findViewById(R.id.tvEnabledGPS);
//        tvStatusGPS = (TextView) findViewById(R.id.tvStatusGPS);
//        tvLocationGPS = (TextView) findViewById(R.id.tvLocationGPS);
//        tvEnabledNet = (TextView) findViewById(R.id.tvEnabledNet);
//        tvStatusNet = (TextView) findViewById(R.id.tvStatusNet);
//        tvLocationNet = (TextView) findViewById(R.id.tvLocationNet);
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10, locationListener);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        // Add a marker in Dnipro and move the camera
//        LatLng dnipro = new LatLng(48.45810997, 35.00414461);
//
//        LatLng user = new LatLng(48.45810888, 35.00414777);
//        mMap.addMarker(new MarkerOptions().position(dnipro).title("Marker in Dnipro"));
//        mMap.addMarker(new MarkerOptions().position(user).title("user"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(dnipro));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationManager.removeUpdates(locationListener);
//    }
//
//    private LocationListener locationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            showLocation(location);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            if (provider.equals(LocationManager.GPS_PROVIDER)) {
//                tvStatusGPS.setText("Status: " + String.valueOf(status));
//            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
//                tvStatusNet.setText("Status: " + String.valueOf(status));
//            }
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            checkEnabled();
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            showLocation(locationManager.getLastKnownLocation(provider));
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//            checkEnabled();
//        }
//    };
//
//    private void showLocation(Location location) {
//        if (location == null)
//            return;
//        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
//            tvLocationGPS.setText(formatLocation(location));
//        } else if (location.getProvider().equals(LocationManager.NETWORK_PROVIDER)) {
//            tvLocationNet.setText(formatLocation(location));
//        }
//    }
//
//    private String formatLocation(Location location) {
//        if (location == null)
//            return"";
//        return String.format(
//                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
//                location.getLatitude(), location.getLongitude(), new Date(
//                        location.getTime()));
//    }
//
//    private void checkEnabled() {
//        tvEnabledGPS.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.GPS_PROVIDER));
//        tvEnabledNet.setText("Enabled: "
//                + locationManager
//                .isProviderEnabled(LocationManager.NETWORK_PROVIDER));
//    }
//
//    public void onClickLocationSettings(View view) {
//        startActivity(new Intent(
//                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//    };
//}



