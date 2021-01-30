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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Second_Page extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second,container,false);
    }

    public static String lat = "41", lon = "28.58";
    public static String temperatureType = "metric", location = "Istanbul";
    TextView[] degreeText;
    TextView[] weatherNameText;
    TextView[] dayNameText;
    ImageView[] weatherImages;
    TextView cityNameText;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        degreeText = new TextView[4];
        weatherNameText = new TextView[4];
        dayNameText = new TextView[4];
        weatherImages = new ImageView[4];
        cityNameText = (TextView) view.findViewById(R.id.cityName);
        weatherNameText[0] = (TextView) view.findViewById(R.id.weatherNameDay1);
        weatherNameText[1] = (TextView) view.findViewById(R.id.weatherNameDay2);
        weatherNameText[2] = (TextView) view.findViewById(R.id.weatherNameDay3);
        weatherNameText[3] = (TextView) view.findViewById(R.id.weatherNameDay4);
        degreeText[0] = (TextView) view.findViewById(R.id.degree);
        degreeText[1] = (TextView) view.findViewById(R.id.degree2);
        degreeText[2] = (TextView) view.findViewById(R.id.degree3);
        degreeText[3] = (TextView) view.findViewById(R.id.degree4);
        dayNameText[0] = (TextView) view.findViewById(R.id.dayName1);
        dayNameText[1] = (TextView) view.findViewById(R.id.dayName2);
        dayNameText[2] = (TextView) view.findViewById(R.id.dayName3);
        dayNameText[3] = (TextView) view.findViewById(R.id.dayName4);
        weatherImages[0] = (ImageView) view.findViewById(R.id.weatherImageDay1);
        weatherImages[1] = (ImageView) view.findViewById(R.id.weatherImageDay2);
        weatherImages[2] = (ImageView) view.findViewById(R.id.weatherImageDay3);
        weatherImages[3] = (ImageView) view.findViewById(R.id.weatherImageDay4);
        location = Fourth_Page.location;
        temperatureType = Fourth_Page.temperatureType;
        lon = Fourth_Page.lon;
        lat = Fourth_Page.lat;
        new hukoWeatherForecast().execute();
    }

    private void parseJSON(String s) throws JSONException {
        JSONObject myMainJSON = new JSONObject(s);
        JSONArray accessTemperature = myMainJSON.getJSONArray("daily");
        if(temperatureType.compareTo("metric") == 0)
            temperatureType =" °C";
        else if(temperatureType.compareTo("imperial") == 0)
            temperatureType = " °F";
        else if(temperatureType.compareTo("standard") == 0)
            temperatureType = " °K";

        Date day;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        for(int i = 3; i < 7; i++)
        {
            degreeText[i-3].setText(String.valueOf(((JSONObject)accessTemperature.get(i)).getJSONObject("temp").getDouble("day") + temperatureType));
            weatherNameText[i-3].setText(((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("description").toUpperCase());
            day = new Date(((JSONObject)accessTemperature.get(i)).getLong("dt")*1000);
            dayNameText[i-3].setText(simpleDateFormat.format(day));

            if(((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("snow") == 0)
                weatherImages[i-3].setImageResource(R.drawable.ic_baseline_ac_unit_24);
            else if(((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("clear") == 0)
                weatherImages[i-3].setImageResource(R.drawable.ic_baseline_wb_sunny_24);
            else if(((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("clouds") == 0)
                weatherImages[i-3].setImageResource(R.drawable.ic_baseline_filter_drama_24);
            else if(((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("rain") == 0
                    || ((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("drizzle") == 0)
                weatherImages[i-3].setImageResource(R.drawable.ic_baseline_blur_on_24);
            else if(((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("thunderstorm") == 0
                    || ((JSONObject) ((JSONObject) accessTemperature.get(i)).getJSONArray("weather").get(0)).getString("main").toLowerCase().compareTo("tornado") == 0)
                weatherImages[i-3].setImageResource(R.drawable.ic_baseline_flash_on_24);
            else
                weatherImages[i-3].setImageResource(R.drawable.ic_baseline_waves_24);
        }
        cityNameText.setText(location);
    }

    private class hukoWeatherForecast extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... strings)
        {
            String result = "";
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse httpResponse = httpClient.execute(new HttpGet("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly,current&appid=9db5da2f43761af17da19bc3f2c12683&units="+temperatureType));
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
