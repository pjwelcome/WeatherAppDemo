package com.demo.android_development.pjwelcome.weatherappdemo.Model;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class ForecastModel {

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public String getWeatherType() {
        return WeatherType;
    }

    public void setWeatherType(String weatherType) {
        WeatherType = weatherType;
    }

    public String getWeatherDescription() {
        return WeatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        WeatherDescription = weatherDescription;
    }

    public Double getHumidity() {
        return Humidity;
    }

    public void setHumidity(Double humidity) {
        Humidity = humidity;
    }

    public Double getPressure() {
        return Pressure;
    }

    public void setPressure(Double pressure) {
        Pressure = pressure;
    }

    public Double getCurrentTemp() {
        return CurrentTemp;
    }

    public void setCurrentTemp(Double currentTemp) {
        CurrentTemp = currentTemp;
    }

    public Double getMinTemp() {
        return MinTemp;
    }

    public void setMinTemp(Double minTemp) {
        MinTemp = minTemp;
    }

    public Double getMaxTemp() {
        return MaxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        MaxTemp = maxTemp;
    }

    public Double getWindSpeed() {
        return WindSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        WindSpeed = windSpeed;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private Double Latitude;
    private Double Longitude;
    private String WeatherType;
    private String WeatherDescription;
    private Double Humidity;
    private Double Pressure;
    private Double CurrentTemp;
    private Double MinTemp;
    private Double MaxTemp;
    private Double WindSpeed;
    private String Name;

    public int getWeatherId() {
        return WeatherId;
    }

    public void setWeatherId(int weatherId) {
        WeatherId = weatherId;
    }

    private int WeatherId;
}
