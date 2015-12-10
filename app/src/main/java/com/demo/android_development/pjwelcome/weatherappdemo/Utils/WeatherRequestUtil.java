package com.demo.android_development.pjwelcome.weatherappdemo.Utils;

import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class WeatherRequestUtil {

    private static WeatherRequestUtil mInstance = null;
    private static final String TAG = WeatherRequestUtil.class.getName();

    /**
     *
     * @return
     */
    public static WeatherRequestUtil getInstance() {
        if (mInstance == null)
            mInstance = new WeatherRequestUtil();
        return mInstance;
    }

    public List<ForecastModel> CreateFiveForecastList(JSONObject jsonObject){
        List<ForecastModel> forecastModelList = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("list");

            for (int i = 0 ; i< jsonArray.length();i++){
                ForecastModel object = new ForecastModel();
                object.setName(jsonObject.getJSONObject("city").getString("name"));
                object.setCurrentTemp(Double.valueOf(jsonArray.getJSONObject(i).getJSONObject("temp").getString("day")));
                object.setHumidity(Double.valueOf(jsonArray.getJSONObject(i).getString("humidity")));
                object.setPressure(Double.valueOf(jsonArray.getJSONObject(i).getString("pressure")));
                object.setMinTemp(Double.valueOf(jsonArray.getJSONObject(i).getJSONObject("temp").getString("min")));
                object.setMaxTemp(Double.valueOf(jsonArray.getJSONObject(i).getJSONObject("temp").getString("max")));
                object.setLatitude(Double.valueOf(jsonObject.getJSONObject("city").getJSONObject("coord").getString("lat")));
                object.setLongitude(Double.valueOf(jsonObject.getJSONObject("city").getJSONObject("coord").getString("lon")));
                object.setWeatherType(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                object.setWeatherDescription(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                object.setWeatherId(Integer.valueOf(jsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("id")));
                forecastModelList.add(object);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onResponse: Forecast Size:" + forecastModelList.size());
        return forecastModelList;
    }
    /**
     *
     * @param jsonObject
     * @return
     */
    public ForecastModel createModelFromJson(JSONObject jsonObject) {
        ForecastModel forecastObject = new ForecastModel();
        try {
            forecastObject.setName(jsonObject.getString("name"));
            forecastObject.setCurrentTemp(Double.valueOf(jsonObject.getJSONObject("main").getString("temp")));
            forecastObject.setHumidity(Double.valueOf(jsonObject.getJSONObject("main").getString("humidity")));
            forecastObject.setPressure(Double.valueOf(jsonObject.getJSONObject("main").getString("pressure")));
            forecastObject.setMinTemp(Double.valueOf(jsonObject.getJSONObject("main").getString("temp_min")));
            forecastObject.setMaxTemp(Double.valueOf(jsonObject.getJSONObject("main").getString("temp_max")));
            forecastObject.setLatitude(Double.valueOf(jsonObject.getJSONObject("coord").getString("lat")));
            forecastObject.setLongitude(Double.valueOf(jsonObject.getJSONObject("coord").getString("lon")));
            forecastObject.setWeatherId(Integer.valueOf(jsonObject.getJSONArray("weather").getJSONObject(0).getString("id")));
            forecastObject.setWeatherType(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            forecastObject.setWeatherDescription(jsonObject.getJSONArray("weather").getJSONObject(0).getString("description"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return forecastObject;
    }

    public int getArtResourceForWeatherCondition(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            return R.drawable.art_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            return R.drawable.art_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            return R.drawable.art_rain;
        } else if (weatherId == 511) {
            return R.drawable.art_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            return R.drawable.art_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            return R.drawable.art_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            return R.drawable.art_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            return R.drawable.art_storm;
        } else if (weatherId == 800) {
            return R.drawable.art_clear;
        } else if (weatherId == 801) {
            return R.drawable.art_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            return R.drawable.art_clouds;
        }
        return -1;
    }

}
