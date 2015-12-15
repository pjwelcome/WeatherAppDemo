package com.demo.android_development.pjwelcome.weatherappdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class CurrentWeatherFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = CurrentWeatherFragment.class.getName();
    private static final String REQUEST_CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public LocationRequest mLocationRequest;
    public GoogleApiClient mGoogleApiClient;
    protected Location mCurrentLocation;
    ForecastModel model;
    private ImageView currentWeatherIcon;
    private AppCompatTextView currentTemperature;
    private AppCompatTextView currentMinimumTemperature;
    private AppCompatTextView currentMaximumTemperature;
    private AppCompatTextView currentPressure;
    private AppCompatTextView currentHumidity;
    private AppCompatTextView currentWeatherDescription;
    private AppCompatTextView currentLocationName;
    private boolean mLocationDidUpdate = false;

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
        currentTemperature.setText(String.valueOf(model.getCurrentTemp()) + "°C");
        currentMinimumTemperature.setText("Min:" + String.valueOf(model.getMinTemp()) + "°C");
        currentMaximumTemperature.setText("Max:" + String.valueOf(model.getMaxTemp()) + "°C");
        currentPressure.setText("Pressure:" + String.valueOf(model.getPressure()) + " kPa");
        currentHumidity.setText("Humidity:" + String.valueOf(model.getHumidity()));
        currentWeatherDescription.setText(model.getWeatherDescription());
        currentLocationName.setText(model.getName());
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

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
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
            startLocationUpdates();
            mLocationDidUpdate = false;
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
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: " + location.getLatitude() + location.getLongitude());
        if (!mLocationDidUpdate) {
            makeJsonWeatherRequest(getContext(), String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
            mLocationDidUpdate = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
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
        progressBar.setMessage("Loading...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.show();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String QueryParams = String.format("lat=%s&lon=%s&APPID=%s&units=" + prefs.getString("TemperatureUnits", "metric"), params[0], params[1], context.getString(R.string.weather_api_key));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                REQUEST_CURRENT_URL + QueryParams,
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
}
