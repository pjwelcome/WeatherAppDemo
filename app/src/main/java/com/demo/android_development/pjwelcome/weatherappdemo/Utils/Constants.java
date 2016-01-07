package com.demo.android_development.pjwelcome.weatherappdemo.Utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by PWelcome on 2015/12/11.
 */
public class Constants {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 60000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    public static final String REQUEST_CURRENT_URL = "http://api.openweathermap.org/data/2.5/weather?";
    public static final String REQUEST_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?";
    public static final LatLng capetown = new LatLng(-26.156709, 28.039165);
    public static final LatLng johannesburg = new LatLng(-33.699091, 25.515884);
    public static final LatLng portElizabeth = new LatLng(-33.902011, 18.473043);
    public static final LatLng durban = new LatLng(-25.694899, 28.229347);
    public static final LatLng pretoria = new LatLng(-29.800342, 30.967958);
}
