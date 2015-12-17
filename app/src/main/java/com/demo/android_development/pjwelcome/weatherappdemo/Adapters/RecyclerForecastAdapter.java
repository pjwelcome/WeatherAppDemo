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
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.Utilities;
import com.demo.android_development.pjwelcome.weatherappdemo.Utils.WeatherRequestUtil;

import java.util.List;

/**
 * Created by PWelcome on 2015/12/09.
 */
public class RecyclerForecastAdapter extends RecyclerView.Adapter<RecyclerForecastAdapter.ViewHolder> {

    private List<ForecastModel> items;

    public RecyclerForecastAdapter(List<ForecastModel> items) {
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
        holder.minimumForecastTemp.setText("Min:" + String.valueOf(item.getMinTemp()) + (Utilities.getInstance().isCelsius(holder.minimumForecastTemp.getContext()) ? holder.minimumForecastTemp.getContext().getString(R.string.celsiusString) : holder.minimumForecastTemp.getContext().getString(R.string.fahrenheitString)));
        holder.maximumForecastTemp.setText("Max:" + String.valueOf(item.getMaxTemp()) + (Utilities.getInstance().isCelsius(holder.maximumForecastTemp.getContext()) ? holder.maximumForecastTemp.getContext().getString(R.string.celsiusString) : holder.maximumForecastTemp.getContext().getString(R.string.fahrenheitString)));
        Glide.with(holder.forecastImageView.getContext())
                .load(WeatherRequestUtil.getInstance().getArtResourceForWeatherCondition(item.getWeatherId()))
                .into(holder.forecastImageView);
        holder.dateTextView.setText(Utilities.getInstance().getDayName(holder.dateTextView.getContext(), position));

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
        AppCompatTextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            forecastImageView = (ImageView) itemView.findViewById(R.id.forecastImage);
            minimumForecastTemp = (AppCompatTextView) itemView.findViewById(R.id.forecastMinTempTextView);
            maximumForecastTemp = (AppCompatTextView) itemView.findViewById(R.id.forecastMaxTempTextView);
            dateTextView = (AppCompatTextView) itemView.findViewById(R.id.DateTextView);
        }
    }
}
