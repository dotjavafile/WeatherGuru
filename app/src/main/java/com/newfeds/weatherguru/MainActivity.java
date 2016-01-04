package com.newfeds.weatherguru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.newfeds.weatherguru.app.AppController;
import com.newfeds.weatherguru.utils.Const;
import com.newfeds.weatherguru.utils.L;

public class MainActivity extends AppCompatActivity {

    TextView textViewShowString;
    SharedPreferences sharedPreferences;
    Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L.log("Application Started");

        sharedPreferences = getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);

        boolean isRunBefore = sharedPreferences.getBoolean(Const.FIRST_TIME_CHECK, false);


        if(isRunBefore){
            Intent intent = new Intent(this, WeatherDisplay.class);
            startActivity(intent);
        }else{
            editor = sharedPreferences.edit();
            editor.putBoolean(Const.FIRST_TIME_CHECK, true);
            editor.commit();
            Intent intent = new Intent(this, FirstTimeRun.class);
            startActivity(intent);
        }
    }

    private void downloadString(){

        String tag_string_req  = "string_req";
        String url = "http://polok.newfeds.com/";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading....");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                L.log(response.toString());
                textViewShowString.setText(response.toString());
                pDialog.hide();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.log("Error: "+ error.getMessage());
                pDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, tag_string_req);
    }
}
