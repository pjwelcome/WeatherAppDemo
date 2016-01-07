package com.demo.android_development.pjwelcome.weatherappdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class ForecastContainerActivity extends AppCompatActivity {

    private static final String TAG = ForecastContainerActivity.class.getName();
    CurrentWeatherFragment currentWeatherFragment;
    ForecastWeatherFragment forecastWeatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forcast_container);
        currentWeatherFragment = CurrentWeatherFragment.newInstance();
        forecastWeatherFragment = ForecastWeatherFragment.newInstance();
        initToolbar();
        initViews();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CurrentWeatherFragment.REQUEST_CHECK_SETTINGS) {
            currentWeatherFragment.onActivityResult(requestCode, resultCode, data);
            currentWeatherFragment.setFiveDayWeather(forecastWeatherFragment);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(currentWeatherFragment, getString(R.string.currentWeatherHeadingTabString));
        adapter.addFragment(forecastWeatherFragment, getString(R.string.fiveDayWeatherHeadingTabString));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getBaseContext(), UserSettingsActivity.class));
            return true;
        } else if (id == R.id.action_map) {
            startActivity(new Intent(getBaseContext(), GoogleMapWeatherClusterActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
