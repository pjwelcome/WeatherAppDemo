package com.demo.android_development.pjwelcome.weatherappdemo.Adapters;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.demo.android_development.pjwelcome.weatherappdemo.Model.ForecastModel;
import com.demo.android_development.pjwelcome.weatherappdemo.R;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;

import java.util.List;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class RecylcerForecastAdapter extends RecyclerView.Adapter<RecylcerForecastAdapter.ViewHolder> {

    private List<ForecastModel> items;

    public RecylcerForecastAdapter(List<ForecastModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ForecastModel item = items.get(position);
        holder.minimumForecastTemp.setText("Min:" + String.valueOf(item.getMinTemp()) + " °C");
        holder.maximumForecastTemp.setText("Max:" + String.valueOf(item.getMaxTemp()) + " °C");
        Glide.with(holder.forecastImageView.getContext())
                .load(WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(item.getWeatherId()))
                .into(holder.forecastImageView);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView forecastImageView;
        AppCompatTextView minimumForecastTemp;
        AppCompatTextView maximumForecastTemp;

        public ViewHolder(View itemView) {
            super(itemView);
            forecastImageView = (ImageView) itemView.findViewById(R.id.forecastImage);
            minimumForecastTemp = (AppCompatTextView) itemView.findViewById(R.id.forecastMinTempTextView);
            maximumForecastTemp = (AppCompatTextView) itemView.findViewById(R.id.forecastMaxTempTextView);
        }
    }
}
