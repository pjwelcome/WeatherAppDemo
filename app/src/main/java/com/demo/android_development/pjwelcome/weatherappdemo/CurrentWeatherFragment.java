package com.demo.android_development.pjwelcome.weatherappdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
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
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;

import org.json.JSONObject;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class CurrentWeatherFragment extends Fragment {

    private static final String TAG = CurrentWeatherFragment.class.getName();
    private ImageView currentWeatherIcon;
    private AppCompatTextView currentTemperature;
    private AppCompatTextView currentMinimumTemperature;
    private AppCompatTextView currentMaximumTemperature;
    private AppCompatTextView currentPressure;
    private AppCompatTextView currentHumidity;
    private AppCompatTextView currentWeatherDescription;
    private static final String REQUEST_CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?";
    ForecastModel model;

    private void initViews(View view){
         currentWeatherIcon = (ImageView)view.findViewById(R.id.weatherImage);
         currentTemperature=(AppCompatTextView)view.findViewById(R.id.currentWeatherTextView);
         currentMinimumTemperature = (AppCompatTextView)view.findViewById(R.id.currentMinimumTemp);
         currentMaximumTemperature= (AppCompatTextView)view.findViewById(R.id.currentMaximumTemp);
         currentPressure = (AppCompatTextView)view.findViewById(R.id.currentPressure);
         currentHumidity = (AppCompatTextView) view.findViewById(R.id.currentHumidity);
         currentWeatherDescription= (AppCompatTextView)view.findViewById(R.id.currentWeatherTypeTextView);
    }

    private void setViewValues(ForecastModel model){
        Glide.with(getContext())
                .load(WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(model.getWeatherId()))
                .into(currentWeatherIcon);
        currentTemperature.setText(String.valueOf(model.getCurrentTemp()));
        currentMinimumTemperature.setText("Min:"+String.valueOf(model.getMinTemp()) +"°C");
        currentMaximumTemperature.setText("Max:"+String.valueOf(model.getMaxTemp())+"°C");
        currentPressure.setText("Pressure:"+String.valueOf(model.getPressure())+" kPa");
        currentHumidity.setText("Humidity:"+String.valueOf(model.getHumidity()));
        currentWeatherDescription.setText(model.getWeatherDescription());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(
                R.layout.activity_daily_weather, container, false);
        initViews(v);
        makeJsonWeatherRequest(getContext(), "-26.043948", "28.015241");
        return v;
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

        String QueryParams = String.format("lat=%s&lon=%s&APPID=%s&units=metric", params[0], params[1], context.getString(R.string.weather_api_key));
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
