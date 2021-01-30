package com.example.weatherforecastwithjson;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class First_Page extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first,container,false);
    }

    public TextView cityName, degree, weatherName;
    public ImageView weatherImage;
    public static String location = "Istanbul", temperatureType = "metric";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityName = (TextView) view.findViewById(R.id.cityName);
        weatherName = (TextView) view.findViewById(R.id.weatherName);
        degree = (TextView) view.findViewById(R.id.degree);
        weatherImage = (ImageView) view.findViewById(R.id.weatherImage);
        location = Fourth_Page.location;
        temperatureType = Fourth_Page.temperatureType;
        new hukoWeatherForecast().execute();
    }

    private void parseJSON(String s) throws JSONException {
        JSONObject myMainJSON = new JSONObject(s);
        JSONObject accessTemperature = myMainJSON.getJSONObject("main");

        if(temperatureType.compareTo("metric") == 0)
            temperatureType =" °C";
        else if(temperatureType.compareTo("imperial") == 0)
            temperatureType = " °F";
        else if(temperatureType.compareTo("standard") == 0)
            temperatureType = " °K";

        degree.setText(accessTemperature.getDouble("temp") + temperatureType);
        cityName.setText(location.toUpperCase());

        JSONArray accessWeather = myMainJSON.getJSONArray("weather");
        weatherName.setText(((JSONObject)accessWeather.get(0)).getString("description").toUpperCase());

        if(((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("snow") == 0)
            weatherImage.setImageResource(R.drawable.ic_baseline_ac_unit_24);
        else if(((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("clear") == 0)
            weatherImage.setImageResource(R.drawable.ic_baseline_wb_sunny_24);
        else if(((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("clouds") == 0)
            weatherImage.setImageResource(R.drawable.ic_baseline_filter_drama_24);
        else if(((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("rain") == 0
        || ((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("drizzle") == 0)
            weatherImage.setImageResource(R.drawable.ic_baseline_blur_on_24);
        else if(((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("thunderstorm") == 0
        ||((JSONObject)accessWeather.get(0)).getString("main").toLowerCase().compareTo("tornado") == 0)
            weatherImage.setImageResource(R.drawable.ic_baseline_flash_on_24);
        else
            weatherImage.setImageResource(R.drawable.ic_baseline_waves_24);
    }

    private class hukoWeatherForecast extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            String result = "";
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse httpResponse = httpClient.execute(new HttpGet("http://api.openweathermap.org/data/2.5/weather?q="+location+"&APPID=9db5da2f43761af17da19bc3f2c12683&units="+temperatureType));
                InputStream inputStream = httpResponse.getEntity().getContent();
                if(inputStream!=null){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = "";
                    while ((line = bufferedReader.readLine())!=null){
                        result+=line;
                    }
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
        @Override
        protected void onPostExecute(String weatherResponse) {
            super.onPostExecute(weatherResponse);
            System.out.println(weatherResponse);
            try {
                parseJSON(weatherResponse);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
