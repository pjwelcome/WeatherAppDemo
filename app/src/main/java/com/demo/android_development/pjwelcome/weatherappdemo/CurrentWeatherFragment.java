package com.demo.android_development.pjwelcome.weatherappdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.demo.android_development.pjwelcome.weatherappdemo.Data.VolleyDataController;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Constants;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Utilities;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;
import com.google.android.gms.common.ConnectionResult;
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

import org.json.JSONObject;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class CurrentWeatherFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {

    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final String TAG = CurrentWeatherFragment.class.getName();
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    protected LocationSettingsRequest mLocationSettingsRequest;
    private ImageView currentWeatherIcon;
    private AppCompatTextView currentTemperature;
    private AppCompatTextView currentMinimumTemperature;
    private AppCompatTextView currentMaximumTemperature;
    private AppCompatTextView currentPressure;
    private AppCompatTextView currentHumidity;
    private AppCompatTextView currentWeatherDescription;
    private AppCompatTextView currentLocationName;
    private boolean mLocationDidUpdate = false;
    private ForecastWeatherFragment forecastWeatherFragment;

    public static CurrentWeatherFragment newInstance() {
        return new CurrentWeatherFragment();
    }

    private void initViews(View view) {
        currentWeatherIcon = (ImageView) view.findViewById(R.id.weatherImage);
        currentTemperature = (AppCompatTextView) view.findViewById(R.id.currentWeatherTextView);
        currentMinimumTemperature = (AppCompatTextView) view.findViewById(R.id.currentMinimumTemp);
        currentMaximumTemperature = (AppCompatTextView) view.findViewById(R.id.currentMaximumTemp);
        currentPressure = (AppCompatTextView) view.findViewById(R.id.currentPressure);
        currentHumidity = (AppCompatTextView) view.findViewById(R.id.currentHumidity);
        currentWeatherDescription = (AppCompatTextView) view.findViewById(R.id.currentWeatherTypeTextView);
        currentLocationName = (AppCompatTextView) view.findViewById(R.id.placeTextView);
    }

    private void setViewValues(ForecastModel model) {
        Glide.with(getContext())
                .load(WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(model.getWeatherId()))
                .into(currentWeatherIcon);
        currentTemperature.setText(String.valueOf(model.getCurrentTemp()) + (Utilities.getInstance().isCelsius(getContext()) ? getString(R.string.celsiusString) : getString(R.string.fahrenheitString)));
        currentMinimumTemperature.setText("Min:" + String.valueOf(model.getMinTemp()) + (Utilities.getInstance().isCelsius(getContext()) ? getString(R.string.celsiusString) : getString(R.string.fahrenheitString)));
        currentMaximumTemperature.setText("Max:" + String.valueOf(model.getMaxTemp()) + (Utilities.getInstance().isCelsius(getContext()) ? getString(R.string.celsiusString) : getString(R.string.fahrenheitString)));
        currentPressure.setText("Pressure:" + String.valueOf(model.getPressure()) + " kPa");
        currentHumidity.setText("Humidity:" + String.valueOf(model.getHumidity()));
        currentWeatherDescription.setText(model.getWeatherDescription());
        currentLocationName.setText(model.getName());
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.activity_daily_weather, container, false);
        initViews(v);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        mLocationDidUpdate = false;
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
                        builder.setTitle(getActivity().getString(R.string.LocationErrorMessageTitleString));
                        builder.setMessage(getActivity().getString(R.string.LocationErrorMessageString));
                        builder.setPositiveButton(getActivity().getString(R.string.RetryString), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkLocationSettings();
                            }
                        });
                        builder.setNegativeButton(getActivity().getString(R.string.ExitString), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                        builder.show();
                        break;
                }
                break;
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d(TAG, "onConnected: Connected");
        }
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {

            }
        });
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    public void setFiveDayWeather(ForecastWeatherFragment forecastWeatherFragment) {
        this.forecastWeatherFragment = forecastWeatherFragment;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.getLatitude() + location.getLongitude());
        if (!mLocationDidUpdate) {
            makeJsonWeatherRequest(getContext(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            if (this.forecastWeatherFragment != null) {
                this.forecastWeatherFragment.startRequest();
            }
            mLocationDidUpdate = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
        mLocationDidUpdate = false;
    }

    /**
     * Makes a Json Request to get the weather an returns a Json Object
     *
     * @param context
     * @param params
     */
    public void makeJsonWeatherRequest(Context context, String... params) {
        final ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(R.style.CircularProgress);
        progressBar.setMessage(getString(R.string.LoadingString));
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String QueryParams = String.format("lat=%s&lon=%s&APPID=%s&units=" + prefs.getString(getString(R.string.tempUnitKey), getString(R.string.tempUnitDefault)), params[0], params[1], context.getString(R.string.weather_api_key));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.REQUEST_CURRENT_URL + QueryParams,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        setViewValues(WeatherRequestUtil.getInstance().createModelFromJson(response));
                        progressBar.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                progressBar.hide();
            }
        });
        // Adding request to request queue
        VolleyDataController.getInstance().addToRequestQueue(jsonObjReq);
    }

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }
}
