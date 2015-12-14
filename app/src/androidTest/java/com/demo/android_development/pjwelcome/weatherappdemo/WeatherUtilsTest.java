package com.demo.android_development.pjwelcome.weatherappdemo;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;

import junit.framework.TestCase;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Created by PWelcome on 2015/12/14.
 * WeatherAppDemo
 */

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class WeatherUtilsTest extends TestCase {
//    @Test
//    public void artImagesResource_Test(){
//        assertEquals(WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(200),200);
//    }

    @Mock
    Context context;

    @Test
    public void createModelFromJson_Test() throws JSONException {
        JSONObject jsonObject = new JSONObject(context.getString(R.string.json_test_data));
        assertEquals(WeatherRequestUtil.getInstance().createModelFromJson(jsonObject).getName(), "Shuzenji");
    }
}
