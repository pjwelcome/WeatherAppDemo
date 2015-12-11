package com.demo.android_development.pjwelcome.weatherappdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;


public abstract class BaseGoogleMapFragmentActivity extends AppCompatActivity {
    private GoogleMap mMap;
    private Toolbar toolbar;

    protected int getLayoutId() {
        return R.layout.googlemaps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        setUpMapIfNeeded();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            toolbar.setTitle("Weather Map");
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps)).getMap();
        if (mMap != null) {
            RadioGroup rgViews = (RadioGroup) findViewById(R.id.rg_views);

            rgViews.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rb_normal) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    } else if (checkedId == R.id.rb_satellite) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    } else if (checkedId == R.id.rb_terrain) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    } else if (checkedId == R.id.rb_hybrid) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    }
                }
            });
            mMap.setBuildingsEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.setTrafficEnabled(true);
            UiSettings mUiSettings = mMap.getUiSettings();
            mUiSettings.setZoomControlsEnabled(true);
            mUiSettings.setCompassEnabled(true);
            mUiSettings.setMyLocationButtonEnabled(true);
            mUiSettings.setScrollGesturesEnabled(true);
            mUiSettings.setZoomGesturesEnabled(true);
            mUiSettings.setTiltGesturesEnabled(true);
            mUiSettings.setRotateGesturesEnabled(true);
            mMap.setMyLocationEnabled(true);

            ExecuteGoogleMapCode();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    protected abstract void ExecuteGoogleMapCode();

    protected GoogleMap getMap() {
        setUpMapIfNeeded();
        return mMap;
    }
}