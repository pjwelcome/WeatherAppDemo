package com.demo.android_development.pjwelcome.weatherappdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.android_development.pjwelcome.weatherappdemo.Adapters.RecyclerForecastAdapter;
import com.demo.android_development.pjwelcome.weatherappdemo.Data.VolleyDataController;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Constants;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Utilities;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class ForecastWeatherFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = ForecastWeatherFragment.class.getName();

    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    List<ForecastModel> forecastModelList = new ArrayList<>();
    private RecyclerForecastAdapter adapter;
    private RecyclerView rv;

    public static ForecastWeatherFragment newInstance() {
        ForecastWeatherFragment fragment = new ForecastWeatherFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.activity_forecast_weather, container, false);
        buildGoogleApiClient();
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setRecyclerAdapter(rv);
        return rv;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        adapter = new RecyclerForecastAdapter(forecastModelList);
        recyclerView.setAdapter(adapter);
    }

    public void makeJsonForecastWeatherRequest(Context context, String... params) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle).setMessage(getString(R.string.LoadingString));
        final AppCompatDialog alert = builder.create();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String QueryParams = String.format("lat=%s&lon=%s&APPID=%s&units=" + prefs.getString(getString(R.string.tempUnitKey), getString(R.string.tempUnitDefault)) + "&cnt=5", params[0], params[1], context.getString(R.string.weather_api_key));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Constants.REQUEST_FORECAST_URL + QueryParams,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        forecastModelList = WeatherRequestUtil.getInstance().createFiveForecastList(response);
                        Utilities.getInstance().createWearablePagingNotification(getContext(), forecastModelList);
                        rv.setAdapter(new RecyclerForecastAdapter(forecastModelList));
                        alert.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                alert.hide();
            }
        });
        // Adding request to request queue
        VolleyDataController.getInstance().addToRequestQueue(jsonObjReq);
    }


    @Override
    public void onConnected(Bundle bundle) {
        startRequest();
    }

    public void startRequest() {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            makeJsonForecastWeatherRequest(getContext(), String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
