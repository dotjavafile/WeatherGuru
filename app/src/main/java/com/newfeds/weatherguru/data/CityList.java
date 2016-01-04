package com.newfeds.weatherguru.data;

import android.content.Context;

import com.newfeds.weatherguru.utils.L;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by GT on 1/1/2016.
 */
public class CityList {
    public ArrayList<String> cities = null;
    Context context;

    public CityList(Context context){
        this.context = context;
        cities =  new ArrayList<>();
    }

    public void initList()  {
        try{
        InputStream inp= context.getResources().getAssets().open("citylist", Context.MODE_PRIVATE);
        BufferedReader input = new BufferedReader(new InputStreamReader(inp));
        String line = "";
        while ((line = input.readLine()) != null) {
            cities.add(line);

        }
        input.close();
        inp.close();
    } catch (Exception e) {
            L.log(e.getMessage());

    }
    }

    public void close(){
        cities = null;
    }
}
