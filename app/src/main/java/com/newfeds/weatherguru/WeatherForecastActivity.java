package com.newfeds.weatherguru;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.newfeds.weatherguru.adapter.WeatherAdapter;
import com.newfeds.weatherguru.app.AppController;
import com.newfeds.weatherguru.data.WeatherData;
import com.newfeds.weatherguru.utils.Const;
import com.newfeds.weatherguru.utils.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

public class WeatherForecastActivity extends AppCompatActivity {
    String cityName;
    String apiUrl = "http://api.openweathermap.org/data/2.5/forecast/daily?mode=json&units=metric&cnt=16&q=";
    String apiKey = "&appid=2de143494c0b295cca9337e1e96b00e0";

    SharedPreferences sharedPreferences;

    ListView listViewForecast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        sharedPreferences = getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
        cityName = sharedPreferences.getString(Const.CITY_NAME, "Dhaka,BD");


        listViewForecast = (ListView) findViewById(R.id.listViewForecast);

        showLoading();
        getForecastDate();
    }

    private void showLoading(){
        ArrayList<WeatherData> weatherList = new ArrayList<>();
        weatherList.add(new WeatherData(R.drawable.loading, "loading", "loading", 0, 0, 0, 0));
        weatherList.add(new WeatherData(R.drawable.loading, "loading", "loading", 0, 0, 0, 0));
        weatherList.add(new WeatherData(R.drawable.loading, "loading", "loading", 0, 0, 0, 0));
        weatherList.add(new WeatherData(R.drawable.loading, "loading", "loading", 0, 0, 0, 0));
        weatherList.add(new WeatherData(R.drawable.loading, "loading", "loading", 0, 0, 0, 0));


        WeatherAdapter adapter = new WeatherAdapter(getBaseContext(), weatherList);
        listViewForecast.setAdapter(adapter);
    }

    private void getForecastDate(){
        String encCity = cityName;

        try {
            encCity = URLEncoder.encode(cityName);
        }catch (Exception e){
            L.log(e.getMessage());
        }
        final String requestUrl = apiUrl+ encCity + apiKey;


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<WeatherData> weatherDatas = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("list");

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        long dt = jsonObject.getLong("dt");
                        Date  date = new Date((long) dt *1000);

                        double pressure = jsonObject.getDouble("pressure");
                        double humidity = jsonObject.getDouble("humidity");


                        JSONObject tempObject = jsonObject.getJSONObject("temp");

                        double tempMin = tempObject.getDouble("min");
                        double tempMax = tempObject.getDouble("max");

                        JSONArray weather  = jsonObject.getJSONArray("weather");
                        JSONObject weatherObject = weather.getJSONObject(0);

                        String description  = weatherObject.getString("description");
                        String icon = "w" + weatherObject.getString("icon");

                        int iconres = getResources().getIdentifier(icon,"drawable", getPackageName());

                        WeatherData data  = new WeatherData(iconres,date.toString(),description,tempMin,tempMax,humidity,pressure);
                        weatherDatas.add(data);
                    }

                    WeatherAdapter adapter = new WeatherAdapter(getBaseContext(), weatherDatas);
                    listViewForecast.setAdapter(adapter);
                    //L.log(description);
                } catch (JSONException e) {
                    L.log(e.getMessage());
                }catch (Exception e){

                    L.log("");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.log(error.getMessage());


            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

}
