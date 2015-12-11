package com.demo.android_development.pjwelcome.weatherappdemo;

import android.content.Context;
import android.util.Log;

import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.WeatherItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapWeatherClusterActivity extends BaseGoogleMapFragmentActivity implements ClusterManager.OnClusterClickListener<WeatherItem>
        , ClusterManager.OnClusterInfoWindowClickListener<WeatherItem>,
        ClusterManager.OnClusterItemClickListener<WeatherItem>,
        ClusterManager.OnClusterItemInfoWindowClickListener<WeatherItem> {
    private ClusterManager<WeatherItem> mClusterManager;

    private List<ForecastModel> forecastItem;

    @Override
    protected void ExecuteGoogleMapCode() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-26.167616, 28.079329), 10));

        mClusterManager = new ClusterManager<>(this, getMap());
        mClusterManager.setRenderer(new RenderClusterInfoWindow(getApplicationContext(), getMap(), mClusterManager));

        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        getWeatherCoOrdinates();
        //mClusterManager.cluster();
    }

    private void setUpData() {
        for (int i = 0; i < 3; i++) {
            ForecastModel JHB = new ForecastModel();
            ForecastModel PE = new ForecastModel();
            ForecastModel CPT = new ForecastModel();
            ForecastModel PTA = new ForecastModel();
            ForecastModel DBN = new ForecastModel();
            JHB.setName("JHB");
            CPT.setName("CPT");
            PTA.setName("PTA");
            DBN.setName("DBN");
            JHB.setLatitude(-26.156709);
            PE.setLatitude(-33.699091);
            CPT.setLatitude(-33.902011);
            PTA.setLatitude(-25.694899);
            DBN.setLatitude(-29.800342);
            PE.setLongitude(25.515884);
            JHB.setLongitude(28.039165);
            CPT.setLongitude(18.473043);
            PTA.setLongitude(28.229347);
            DBN.setLongitude(30.967958);
            //TODO: Get the current temperature
            JHB.setCurrentTemp(33.0);
            CPT.setCurrentTemp(33.0);
            PTA.setCurrentTemp(33.0);
            DBN.setCurrentTemp(33.0);
            PE.setCurrentTemp(33.0);

            forecastItem.add(JHB);
            forecastItem.add(PTA);
            forecastItem.add(CPT);
            forecastItem.add(DBN);
        }
    }

    private void getWeatherCoOrdinates() {

        forecastItem = new ArrayList<>();
        setUpData();
        List<WeatherItem> items = new ArrayList<>();

        for (int i = 0; i < forecastItem.size(); i++) {
            items.add(new WeatherItem(forecastItem.get(i).getLatitude(),
                    forecastItem.get(i).getLongitude()
                    , forecastItem.get(i).getName(), String.valueOf(forecastItem.get(i).getCurrentTemp())));
        }
        if (mClusterManager != null) {
            mClusterManager.addItems(items);
        } else {
            Log.i("NULL", "Null");
        }
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
