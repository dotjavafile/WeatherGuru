package com.newfeds.weatherguru;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class MapActivity extends AppCompatActivity {

    WebView webViewMap;

    //String mapUrl= "http://openweathermap.org/maps?zoom=7&lat=23.07267&lon=91.17051&layers=B0FTTFFT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();

        String lat = intent.getStringExtra(WeatherDisplay.LAT_KEY);
        String lon = intent.getStringExtra(WeatherDisplay.LON_KEY);

        String mapUrl = "http://openweathermap.org/maps?zoom=7&lat="+lat+"&lon="+lon+"&layers=B0FTTFFT";

        webViewMap = (WebView) findViewById(R.id.webViewMap);
        webViewMap.getSettings().setJavaScriptEnabled(true);

        webViewMap.loadUrl(mapUrl);

    }
}
