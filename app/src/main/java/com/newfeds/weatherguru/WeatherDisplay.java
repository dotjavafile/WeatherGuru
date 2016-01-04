package com.newfeds.weatherguru;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newfeds.weatherguru.app.AppController;
import com.newfeds.weatherguru.utils.Const;
import com.newfeds.weatherguru.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Date;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class WeatherDisplay extends AppCompatActivity {
    String cityName;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    String apiUrl = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=";
    String apiKey = "&appid=2de143494c0b295cca9337e1e96b00e0";

    ImageView imageViewSettingsButton;
    ImageView imageViewRefreshButton;
    ImageView imageViewForecastButton;
    ImageView imageViewWeatherIcon;
    ImageView imageViewMap;

    TextView textViewCity;
    TextView textViewTime;

    TextView textViewWeatherCondition;
    TextView textViewHumidity;
    TextView textViewPressure;
    TextView textViewWind;
    TextView textViewTempMin;
    TextView textViewTempMax;
    TextView textViewLatLon;

    SmallBang smallBang;
    String latitude = "0.0";
    String longitude = "0.0";

    public static final String LAT_KEY = "com.newfeds.latkey";
    public static final String LON_KEY = "com.newfeds.lonkey";

    boolean netresp = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_display);

        smallBang = SmallBang.attach2Window(this);

        sharedPreferences = getSharedPreferences(Const.PREF_NAME, MODE_PRIVATE);
        cityName = sharedPreferences.getString(Const.CITY_NAME, "Dhaka,BD");

        textViewCity = (TextView) findViewById(R.id.textViewCityName);
        textViewTime = (TextView) findViewById(R.id.textViewTimeUpdated);

        textViewWeatherCondition = (TextView) findViewById(R.id.textViewGeneralWeatherCondition);
        textViewHumidity = (TextView) findViewById(R.id.textViewHumidity);
        textViewPressure = (TextView) findViewById(R.id.textViewPressure);
        textViewWind = (TextView) findViewById(R.id.textViewWind);
        textViewTempMin = (TextView) findViewById(R.id.textViewMin);
        textViewTempMax = (TextView) findViewById(R.id.textViewMax);
        textViewLatLon = (TextView) findViewById(R.id.textViewLatLon);

        imageViewSettingsButton = (ImageView) findViewById(R.id.imageViewSettingsButton);
        imageViewRefreshButton = (ImageView) findViewById(R.id.imageViewRefreshButton);
        imageViewForecastButton = (ImageView) findViewById(R.id.imageViewForecastButton);
        imageViewWeatherIcon = (ImageView) findViewById(R.id.imageViewWeatherIcon);
        imageViewMap = (ImageView) findViewById(R.id.imageViewMap);

        imageViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallBang.setColors(new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#AFE0EE")});
                smallBang.bang(v, new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {

                    }
                    @Override
                    public void onAnimationEnd() {
                        Intent intent = new Intent(getBaseContext(), MapActivity.class);
                        intent.putExtra(LAT_KEY, latitude);
                        intent.putExtra(LON_KEY, longitude);
                        startActivity(intent);
                    }
                });
            }
        });

        imageViewSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallBang.setColors(new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#afe0ee")});
                smallBang.bang(v, new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent intent = new Intent(getBaseContext(), Settings.class);
                        startActivity(intent);
                    }
                });
            }
        });

        imageViewRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallBang.setColors(new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#afe0ee")});
                smallBang.bang(v,new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {
                        getWeatherData();
                    }

                    @Override
                    public void onAnimationEnd() {

                    }
                });
            }
        });

        imageViewForecastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallBang.setColors(new int[]{Color.parseColor("#FFFFFF"), Color.parseColor("#afe0ee")});
                smallBang.bang(v, new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent intent = new Intent(getBaseContext(), WeatherForecastActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        getWeatherData();
        if(netresp){

        }else{

        }
    }
    private void getWeatherData(){
        imageViewWeatherIcon.setImageResource(R.drawable.loading);
        String encCity = cityName;

        try {
            encCity = URLEncoder.encode(cityName);
            L.log(encCity);
        }catch (Exception e){
            L.log(e.getMessage());
        }
        final String requestUrl = apiUrl+ encCity + apiKey;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray weatherArr = response.getJSONArray("weather");
                    JSONObject weather = weatherArr.getJSONObject(0);

                    String description = weather.getString("description");
                    String icon = weather.getString("icon");

                    imageViewWeatherIcon.setImageResource(getResources().getIdentifier("w"+icon,"drawable", getPackageName()));
                    textViewWeatherCondition.setText(description);

                    String cityName = response.getString("name");
                    textViewCity.setText(cityName);

                    Long dt = response.getLong("dt");

                    Date date = new Date((long)dt*1000);
                    textViewTime.setText(date.toString());

                    JSONObject main = response.getJSONObject("main");
                    String pressure = main.getString("pressure");
                    String humidity = main.getString("humidity");
                    String tempMax = main.getString("temp_max");
                    String tempMin = main.getString("temp_min");
                    String windSpeed = response.getJSONObject("wind").getString("speed");
                    String windDeg = response.getJSONObject("wind").getString("deg");

                    textViewPressure.setText("Pressure:" + pressure +" hpa");
                    textViewHumidity.setText("Humidity:" + humidity + "%");

                    textViewTempMin.setText("Temp Min:" + tempMin+(char) 0x00B0 + "C");
                    textViewTempMax.setText("Temp Max:" + tempMax+(char) 0x00B0 + "C");

                    textViewWind.setText("WindSpeed: " + windSpeed + " @ " + windDeg + " deg");

                    String lat = response.getJSONObject("coord").getString("lat");
                    String lon = response.getJSONObject("coord").getString("lon");

                    latitude = lat;
                    longitude = lon;

                    textViewLatLon.setText("Lat: " + lat +  " Lon: "+ lon);

                    L.log(description);
                } catch (JSONException e) {
                    imageViewWeatherIcon.setImageResource(R.drawable.notfound);
                    L.log(e.getMessage());
                }catch (Exception e){
                    imageViewWeatherIcon.setImageResource(R.drawable.notfound);
                    L.log("");
                }
                netresp = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.log(error.getMessage());
                imageViewWeatherIcon.setImageResource(R.drawable.notfound);
                netresp = false;
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }
}
