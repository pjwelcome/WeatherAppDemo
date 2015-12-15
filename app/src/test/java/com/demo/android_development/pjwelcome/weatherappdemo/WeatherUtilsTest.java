package com.demo.android_development.pjwelcome.weatherappdemo;

import android.content.Context;
import android.test.suitebuilder.annotation.SmallTest;

import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

/**
 * Created by PWelcome on 2015/12/14.
 * WeatherAppDemo
 */
@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class WeatherUtilsTest {
    JSONObject jsonObject;
    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        jsonObject = new JSONObject("{\"coord\":{\"lon\":-122.09,\"lat\":37.39},\n" +
                "\"sys\":{\"type\":3,\"id\":168940,\"message\":0.0297,\"country\":\"US\",\"sunrise\":1427723751,\"sunset\":1427768967},\n" +
                "\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"Sky is Clear\",\"icon\":\"01n\"}],\n" +
                "\"base\":\"stations\",\n" +
                "\"main\":{\"temp\":285.68,\"humidity\":74,\"pressure\":1016.8,\"temp_min\":284.82,\"temp_max\":286.48},\n" +
                "\"wind\":{\"speed\":0.96,\"deg\":285.001},\n" +
                "\"clouds\":{\"all\":0},\n" +
                "\"dt\":1427700245,\n" +
                "\"id\":0,\n" +
                "\"name\":\"Shuzenji\",\n" +
                "\"cod\":200}");
    }

    @Test
    public void testCreateModelFromJson_Test() throws JSONException {
        assertEquals(WeatherRequestUtil.getInstance().createModelFromJson(jsonObject).getName(), "Shuzenji");
    }
}
