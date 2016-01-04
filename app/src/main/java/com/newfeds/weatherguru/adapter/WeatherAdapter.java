package com.newfeds.weatherguru.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newfeds.weatherguru.R;
import com.newfeds.weatherguru.data.WeatherData;

import java.util.ArrayList;

/**
 * Created by Yousuf on 1/3/2016.
 */
public class WeatherAdapter extends ArrayAdapter<WeatherData>{

    private Context context;
    String deg = (char) 0x00B0 + "C";

    public WeatherAdapter(Context context,ArrayList<WeatherData> objects){
        super(context,0,objects);
        this.context = context;
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.forecast_row_layout, parent, false);
        }

        ImageView ivWeatherIcon = (ImageView)row.findViewById(R.id.ivWeatherIcon);
        TextView tvDay = (TextView)row.findViewById(R.id.tvDay);
        TextView tvDescription = (TextView) row.findViewById(R.id.tvDescription);
        TextView tvTempMin = (TextView) row.findViewById(R.id.tvTempMin);
        TextView tvTempMax = (TextView) row.findViewById(R.id.tvTempMax);
        TextView tvHumidity = (TextView) row.findViewById(R.id.tvHumidity);
        TextView tvPressure = (TextView) row.findViewById(R.id.tvPressure);

        WeatherData weatherData = getItem(position);
        ivWeatherIcon.setImageResource(weatherData.getIcon());
        tvDay.setText(weatherData.getDay());
        tvDescription.setText(weatherData.getDescription());
        tvTempMin.setText("Min: " + weatherData.getTempMin()+deg );
        tvTempMax.setText("Max: " + weatherData.getTempMax()+ deg );
        tvHumidity.setText("Humidity: " + weatherData.getHumidity()+"%");
        tvPressure.setText("Pressure: "+ weatherData.getPressure() + " hpa");

        return row;
    }
}
