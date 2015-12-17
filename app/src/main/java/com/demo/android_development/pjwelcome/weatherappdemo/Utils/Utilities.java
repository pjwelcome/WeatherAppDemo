package com.demo.android_development.pjwelcome.weatherappdemo.Utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.demo.android_development.pjwelcome.weatherappdemo.ForecastContainerActivity;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    public boolean isCelsius(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.tempUnitKey), context.getString(R.string.tempUnitDefault))
                .equals(context.getString(R.string.tempUnitDefault));
    }

    public String getDayName(Context context, int dayIndex) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int julianDay = calendar.get(Calendar.DAY_OF_MONTH) + dayIndex;
        int currentJulianDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if (julianDay == currentJulianDay + 1) {
            return context.getString(R.string.tomorrow);
        } else {
            GregorianCalendar date = new GregorianCalendar();
            date.set(Calendar.YEAR, Calendar.MONTH, julianDay);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            return outFormat.format(date.getTime());

        }
    }


    public void createWearablePagingNotification(Context context, List<ForecastModel> models) {

        Resources resources = context.getResources();
        List<Notification> pages = new ArrayList<>();
        for (int i = 1; i < models.size(); i++) {

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(models.get(i).getName())
                    .setContentText(getDayName(context, i) + " " + models.get(i).getCurrentTemp() +
                            (Utilities.getInstance().isCelsius(context) ? context.getString(R.string.celsiusString) : context.getString(R.string.fahrenheitString)))
                    .setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(getDayName(context, i) + " " + models.get(i).getCurrentTemp() +
                            (Utilities.getInstance().isCelsius(context) ? context.getString(R.string.celsiusString) : context.getString(R.string.fahrenheitString)) +
                            " \n" + models.get(i).getWeatherDescription()))
                    .setLargeIcon(BitmapFactory.decodeResource(resources,
                            WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(models.get(i).getWeatherId())))
                    .build();
            pages.add(notification);
        }

        Intent resultIntent = new Intent(context, ForecastContainerActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ForecastContainerActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Style style = new NotificationCompat.BigTextStyle()
                .setBigContentTitle(models.get(0).getName())
                .setSummaryText(getDayName(context, 0)).bigText(models.get(0).getWeatherDescription());
        NotificationCompat.WearableExtender extender = new NotificationCompat
                .WearableExtender()
                .addPages(pages);
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_clear)
                .setContentTitle(models.get(0).getName())
                .setContentText(models.get(0).getWeatherDescription())
                .setAutoCancel(true)
                .setContentIntent(resultPendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(resources,
                        WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(models.get(0).getWeatherId())))
                .setStyle(style)
                .extend(extender)
                .build();

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context);
        mNotificationManager.notify(NOTIFCATIONID, notification);
    }
}
