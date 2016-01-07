package com.demo.android_development.pjwelcome.weatherappdemo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.demo.android_development.pjwelcome.weatherappdemo.Data.VolleyDataController;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.WeatherItem;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Constants;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Utilities;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONObject;

public class GoogleMapWeatherClusterActivity extends BaseGoogleMapFragmentActivity implements
        ClusterManager.OnClusterClickListener<WeatherItem>
        , ClusterManager.OnClusterInfoWindowClickListener<WeatherItem>,
        ClusterManager.OnClusterItemClickListener<WeatherItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<WeatherItem> {
    private static final String TAG = GoogleMapWeatherClusterActivity.class.getName();
    private ClusterManager<WeatherItem> mClusterManager;

    @Override
    protected void executeGoogleMapCode() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.167616, 28.079329), 4));

        mClusterManager = new ClusterManager<>(this, getMap());
        mClusterManager.setRenderer(new RenderClusterInfoWindow(getApplicationContext(), getMap(), mClusterManager));

        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        getWeatherCoOrdinates();
        //mClusterManager.cluster();
    }

    private void getWeatherCoOrdinates() {

        makeJsonWeatherRequest(this, String.valueOf(Constants.capetown.latitude), String.valueOf(Constants.capetown.longitude));
        makeJsonWeatherRequest(this, String.valueOf(Constants.johannesburg.latitude), String.valueOf(Constants.johannesburg.longitude));
        makeJsonWeatherRequest(this, String.valueOf(Constants.portElizabeth.latitude), String.valueOf(Constants.portElizabeth.longitude));
        makeJsonWeatherRequest(this, String.valueOf(Constants.durban.latitude), String.valueOf(Constants.durban.longitude));
        makeJsonWeatherRequest(this, String.valueOf(Constants.pretoria.latitude), String.valueOf(Constants.pretoria.longitude));

    }

    @Override
    public boolean onClusterClick(Cluster<WeatherItem> plantItemCluster) {
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<WeatherItem> plantItemCluster) {

    }

    @Override
    public boolean onClusterItemClick(WeatherItem plantItem) {
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(WeatherItem plantItem) {

    }

    /**
     * Makes a Json Request to get the weather an returns a Json Object
     *
     * @param context
     * @param params
     */
    public void makeJsonWeatherRequest(final Context context, String... params) {
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
                        ForecastModel model = WeatherRequestUtil.getInstance().createModelFromJson(response);
                        mClusterManager.addItem(new WeatherItem(model.getLatitude(), model.getLongitude(), model.getName(), String.valueOf(model.getCurrentTemp()) + (Utilities.getInstance().isCelsius(context) ? context.getString(R.string.celsiusString) : context.getString(R.string.fahrenheitString)) + model.getWeatherDescription()));
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

    private class RenderClusterInfoWindow extends DefaultClusterRenderer<WeatherItem> {

        public RenderClusterInfoWindow(Context context, GoogleMap map, ClusterManager<WeatherItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onClusterRendered(Cluster<WeatherItem> cluster, Marker marker) {
            super.onClusterRendered(cluster, marker);
        }

        @Override
        protected void onBeforeClusterItemRendered(WeatherItem item, MarkerOptions markerOptions) {
            markerOptions.title(item.getmTitle());
            markerOptions.snippet(item.getCurrentTemp());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
}
