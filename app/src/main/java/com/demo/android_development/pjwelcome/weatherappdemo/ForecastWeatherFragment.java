package com.demo.android_development.pjwelcome.weatherappdemo;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.demo.android_development.pjwelcome.weatherappdemo.Adapters.RecylcerForecastAdapter;
import com.demo.android_development.pjwelcome.weatherappdemo.Data.VolleyDataController;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Utilities;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class ForecastWeatherFragment extends Fragment {

    private static final String TAG = ForecastWeatherFragment.class.getName();
    private static final String REQUEST_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    List<ForecastModel> forecastModelList = new ArrayList<>();
    private RecylcerForecastAdapter adapter;
    private RecyclerView rv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rv = (RecyclerView) inflater.inflate(
                R.layout.activity_forecast_weather, container, false);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        setRecyclerAdapter(rv);
        return rv;
    }

    @Override
    public void onResume() {
        super.onResume();
        makeJsonForecastWeatherRequest(getContext(), "-26.1212455", "28.0316443");
    }

    private void setRecyclerAdapter(RecyclerView recyclerView) {
        forecastModelList.add(new ForecastModel());
        forecastModelList.add(new ForecastModel());
        adapter = new RecylcerForecastAdapter(forecastModelList);
        recyclerView.setAdapter(adapter);
    }

    public void makeJsonForecastWeatherRequest(Context context, String... params) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle).setMessage("Loading...");
        final AppCompatDialog alert = builder.create();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String QueryParams = String.format("lat=%s&lon=%s&APPID=%s&units=" + prefs.getString("TemperatureUnits", "metric") + "&cnt=5", params[0], params[1], context.getString(R.string.weather_api_key));
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                REQUEST_FORECAST_URL + QueryParams,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        forecastModelList = WeatherRequestUtil.getInstance().CreateFiveForecastList(response);
                        Utilities.getInstance().CreateWearablePagingNotification(getContext(), forecastModelList);
                        rv.setAdapter(new RecylcerForecastAdapter(forecastModelList));
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
}
