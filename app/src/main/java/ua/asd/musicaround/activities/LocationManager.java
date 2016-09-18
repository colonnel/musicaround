package ua.asd.musicaround.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

public class LocationManager {

    private static final String TAG = LocationManager.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient = null;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private final static int DEFAULT_TIME_INTERVAL_MILLISECONDS = 10000;
    public static final int REQUEST_RESOLVE_ERROR = 1001;
    public static final int REQUEST_CHECK_SETTINGS = 1;
    public static final int RESULT_OK = -1;
    private static final String DIALOG_ERROR = "dialog_error";
    private boolean mStartedUpdateLocation = false;
    private boolean mResolvingError = false;
    private LocationRequest mLocationRequest;
    private Context context;
    private LocationDeterminedCallback locationDeterminedCallback;

    private LocationListener mLocationListener;

    /**
     * This interface return user location when they are determined
     */
    public interface LocationDeterminedCallback {
        void getLocation(Location location);
    }

    public LocationManager(Context context) {
        this.context = context;
    }

    //////////////////////////////////////////////////// PUBLIC METHODS ////////////////////////////////////////////////////////////////

    public void initGoogleApiClient(LocationDeterminedCallback locationDeterminedCallback) {
        if (checkPlayServices(context)) {
            this.locationDeterminedCallback = locationDeterminedCallback;
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(@Nullable Bundle bundle) {
                            createLocationRequest(DEFAULT_TIME_INTERVAL_MILLISECONDS);
                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED
                                    || i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                Log.e(TAG,"onConnectionSuspended: " + i);
                            }
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            if (mResolvingError) {
                                // Already attempting to resolve an error.
                                return;
                            } else if (connectionResult.hasResolution()) {
                                try {
                                    mResolvingError = true;
                                    //mSwitchOnLocale - false --> if user click on button NO in ErrorDialogFragment
                                    connectionResult.startResolutionForResult((Activity) context, REQUEST_RESOLVE_ERROR);

                                } catch (IntentSender.SendIntentException e) {
                                    // There was an error with the resolution intent. Try again.
                                    mGoogleApiClient.connect();
                                }
                            } else {
                                // Show dialog using GooglePlayServicesUtil.getErrorDialog()
                                showErrorDialog(connectionResult.getErrorCode());
                                mResolvingError = true;
                            }
                        }
                    }).build();
            mGoogleApiClient.connect();
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog((Activity) context, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST, new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.cancel();
                            }
                        }).show();
            }

            return false;
        }
        return true;
    }

    @NonNull
    public void createLocationRequest(long updatesInterval) {
        final LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(updatesInterval);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        addRequestAndCheckIt(mLocationRequest);

    }

    public void stopLocationUpdates() {
        if(mStartedUpdateLocation){
            if (mGoogleApiClient != null && mLocationListener != null) {
                LocationServices.FusedLocationApi.removeLocationUpdates(
                        mGoogleApiClient, mLocationListener);
            }
            mStartedUpdateLocation = false;
        }

    }


    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public void connectAgainAfterResolveError(int resultCode) {
        mResolvingError = false;
        if (resultCode == RESULT_OK) {
            if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
        }
    }

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient.isConnected();
    }

    public void checkLocationSettings() {
        if (mLocationRequest != null) {
            if (!mStartedUpdateLocation) {
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
                final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

                final ResultCallback<LocationSettingsResult> listener = new ResultCallback<LocationSettingsResult>() {
                    @Override
                    public void onResult(LocationSettingsResult locationSettingsResult) {
                        Status status = locationSettingsResult.getStatus();
                        if (status.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Log.e(TAG,"PendingIntent unable to execute request: " + e.getMessage());
                            }
                        }
                    }
                };
                result.setResultCallback(listener);

            }
        }
    }

    //////////////////////////////////////////////////// PRIVATE METHODS  //////////////////////////////////////////////////////////////

    private void addRequestAndCheckIt(final LocationRequest locationRequest) {
        final LocationRequest mLocationRequest = locationRequest;
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        final PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                final Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdates(mLocationRequest);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.e(TAG,"PendingIntent unable to execute request: " + e.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way
                        // to fix the settings so we won't show the dialog.
                        break;
                }

            }
        });

        startLocationUpdates(mLocationRequest);

    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "errordialog");
    }

    private void startLocationUpdates(LocationRequest mLocationRequest) {
        if(!mStartedUpdateLocation){
            mStartedUpdateLocation = true;
            this.mLocationRequest = mLocationRequest;
            mLocationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    locationDeterminedCallback.getLocation(location);
                }
            };
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
        }
    }


    @SuppressLint("ValidFragment")
    public class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            onDialogDismissed();
        }
    }

}
