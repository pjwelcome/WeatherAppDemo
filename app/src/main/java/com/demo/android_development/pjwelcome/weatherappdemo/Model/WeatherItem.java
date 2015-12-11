package com.demo.android_development.pjwelcome.weatherappdemo.Model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by PJ on 2014/11/14.
 */
public class WeatherItem implements ClusterItem {

    private final LatLng mPosition;
    private String mTitle;
    private String CurrentTemp;

    public WeatherItem(double lat, double lng, String title, String snippet) {
        this.mTitle = title;
        mPosition = new LatLng(lat, lng);
        this.CurrentTemp = snippet;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getCurrentTemp() {
        return CurrentTemp;
    }

    public void setCurrentTemp(String currentTemp) {
        CurrentTemp = currentTemp;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
