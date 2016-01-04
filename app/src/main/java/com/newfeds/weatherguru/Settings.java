package com.newfeds.weatherguru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.newfeds.weatherguru.data.CityList;
import com.newfeds.weatherguru.utils.Const;
import com.newfeds.weatherguru.utils.L;

import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class Settings extends AppCompatActivity {
    ImageView imageViewChooseTick;
    AutoCompleteTextView autoCompleteTextViewCityChooser;

    SmallBang smallBang;

    CityList cityList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        smallBang = SmallBang.attach2Window(this);

        imageViewChooseTick = (ImageView) findViewById(R.id.imageViewChooseTick);
        autoCompleteTextViewCityChooser = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextViewSettings);


        cityList = new CityList(this);
        final CityLoader cityLoader = new CityLoader();
        cityLoader.execute();




        autoCompleteTextViewCityChooser.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, cityList.cities));
        autoCompleteTextViewCityChooser.setValidator(new AutoCompleteTextView.Validator() {
            @Override
            public boolean isValid(CharSequence text) {
                try {
                    L.log("Charsec: " + text);
                    if (cityList.cities.contains(text.toString())) {
                        L.log("validation called: true");
                        Intent intent = new Intent(getBaseContext(), WeatherDisplay.class);

                        sharedPreferences = getSharedPreferences(Const.PREF_NAME, Context.MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString(Const.CITY_NAME, text.toString());
                        editor.commit();
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        cityList.close();
                        finish();
                        return true;
                    }
                    L.log("validation called: false");
                } catch (Exception e) {
                    L.log(e.getMessage());
                }
                Toast.makeText(getBaseContext(), "Please select exact one from list", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public CharSequence fixText(CharSequence invalidText) {
                L.log("fixtext called");
                return null;
            }
        });

        imageViewChooseTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smallBang.setColors(new int[]{Color.parseColor("#7CC4D8"), Color.parseColor("#AFE0EE")});
                smallBang.bang(v, new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {

                    }

                    @Override
                    public void onAnimationEnd() {
                        autoCompleteTextViewCityChooser.performValidation();

                    }
                });

            }
        });



    }


    class CityLoader extends AsyncTask<Void,Void,Void> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Settings.this,"Loading City List", "Please wait...");

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }



        @Override
        protected Void doInBackground(Void... params) {
            cityList.initList();

            return null;
        }
    }
}
