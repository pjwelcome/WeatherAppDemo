package com.demo.android_development.pjwelcome.weatherappdemo.Utils;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.text.format.Time;

import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PWelcome on 2015/12/11.
 */
public class Utilities {


    private static final int NOTIFCATIONID = 1;
    private static Utilities mInstance;

    public static Utilities getInstance() {
        if (mInstance == null)
            mInstance = new Utilities();
        return mInstance;
    }

    public String getDayName(Context context, int dayIndex) {
        Time t = new Time();
        Time dayTime = new Time();
        dayTime.setToNow();
        // we start at the day returned by local time. Otherwise this is a mess.
        int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);
        t.setToNow();
        long dateInMillis = dayTime.setJulianDay(julianStartDay);
        int julianDay = Time.getJulianDay(dateInMillis + dayIndex, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }


    public void CreateWearablePagingNotification(Context context, List<ForecastModel> models) {

        Resources resources = context.getResources();
        List<Notification> pages = new ArrayList<>();
        for (int i = 1; i < models.size(); i++) {
            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(models.get(i).getName())
                    .setContentText(getDayName(context, i) + " " + models.get(i).getCurrentTemp() + " \n" + models.get(i).getWeatherDescription())
                    .setLargeIcon(BitmapFactory.decodeResource(resources,
                            WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(models.get(i).getWeatherId())))
                    .build();
            pages.add(notification);
        }
        NotificationCompat.Style style = new NotificationCompat.BigTextStyle()
                .setBigContentTitle(models.get(0).getName())
                .setSummaryText(getDayName(context, 0)).bigText(models.get(0).getWeatherDescription());
        NotificationCompat.WearableExtender extender = new NotificationCompat
                .WearableExtender()
                .addPages(pages);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.common_ic_googleplayservices)
                .setContentTitle(models.get(0).getName())
                .setContentText(models.get(0).getWeatherDescription())
                .setLargeIcon(BitmapFactory.decodeResource(resources,
                        WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(models.get(0).getWeatherId())))
                .setStyle(style)
                .extend(extender)
                .build();

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.notify(NOTIFCATIONID, notification);
    }
}
