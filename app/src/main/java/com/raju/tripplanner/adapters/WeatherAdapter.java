package com.raju.tripplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raju.tripplanner.R;
import com.raju.tripplanner.models.WeatherResult.Day;
import com.raju.tripplanner.models.WeatherResult.ForecastDay;
import com.raju.tripplanner.utils.Tools;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Context context;
    private List<ForecastDay> forecastDayList;

    public WeatherAdapter(Context context, List<ForecastDay> forecastDayList) {
        this.context = context;
        this.forecastDayList = forecastDayList;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View weatherView = LayoutInflater.from(context).inflate(R.layout.layout_weather, parent, false);
        return new WeatherViewHolder(weatherView);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        ForecastDay forecastDay = forecastDayList.get(position);
        holder.bindWeatherData(forecastDay);
    }

    @Override
    public int getItemCount() {
        return forecastDayList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {

        private TextView date, temperature, maxTemp, minTemp, condition, wind, visibility, humidity, uv;
        private ImageView weatherIcon;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            weatherIcon = itemView.findViewById(R.id.weather_icon);
            date = itemView.findViewById(R.id.date);
            temperature = itemView.findViewById(R.id.temperature);
            maxTemp = itemView.findViewById(R.id.max_temp);
            minTemp = itemView.findViewById(R.id.min_temp);
            condition = itemView.findViewById(R.id.weather_condition);
            wind = itemView.findViewById(R.id.tv_wind_value);
            visibility = itemView.findViewById(R.id.tv_visibility_value);
            humidity = itemView.findViewById(R.id.tv_humidity_value);
            uv = itemView.findViewById(R.id.tv_uv_value);
        }

        public void bindWeatherData(ForecastDay forecastDay) {
            Day day = forecastDay.getDay();
            String imageUri = day.getCondition().getIcon();
            Picasso.get().load("http://" + imageUri.substring(2)).into(weatherIcon);
            date.setText(Tools.formatDate("yyyy-MM-dd", "EEE, MMM dd", forecastDay.getDate()));
            temperature.setText(day.getAvgTemp() + "");
            maxTemp.setText(day.getMaxTemp() + "");
            minTemp.setText(day.getMinTemp() + "");
            condition.setText(day.getCondition().getText());
            wind.setText(day.getWind() + "");
            visibility.setText(day.getVisibility() + "");
            humidity.setText(day.getHumidity() + "");
            uv.setText(day.getUv() + "");
        }
    }
}
