package com.example.weatherforecastwithjson;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Third_Page extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third,container,false);
    }

    public static String lat = "41", lon = "28.58";
    public static String location = "Istanbul";
    TextView[] degreeText;
    TextView[] speedText;
    TextView[] dayNameText;
    TextView cityNameText;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        degreeText = new TextView[4];
        speedText = new TextView[4];
        dayNameText = new TextView[4];
        cityNameText = (TextView) view.findViewById(R.id.locationName);
        speedText[0] = (TextView) view.findViewById(R.id.speed1);
        speedText[1] = (TextView) view.findViewById(R.id.speed2);
        speedText[2] = (TextView) view.findViewById(R.id.speed3);
        speedText[3] = (TextView) view.findViewById(R.id.speed4);
        degreeText[0] = (TextView) view.findViewById(R.id.windDegree1);
        degreeText[1] = (TextView) view.findViewById(R.id.windDegree2);
        degreeText[2] = (TextView) view.findViewById(R.id.windDegree3);
        degreeText[3] = (TextView) view.findViewById(R.id.windDegree4);
        dayNameText[0] = (TextView) view.findViewById(R.id.daySign1);
        dayNameText[1] = (TextView) view.findViewById(R.id.daySign2);
        dayNameText[2] = (TextView) view.findViewById(R.id.daySign3);
        dayNameText[3] = (TextView) view.findViewById(R.id.daySign4);
        location = Fourth_Page.location;
        lon = Fourth_Page.lon;
        lat = Fourth_Page.lat;
        new hukoWeatherForecast().execute();
    }

    private void parseJSON(String s) throws JSONException {
        JSONObject myMainJSON = new JSONObject(s);
        JSONArray accessTemperature = myMainJSON.getJSONArray("daily");

        Date day;
        double windDegree;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
        for(int i = 3; i < 7; i++)
        {
            speedText[i-3].setText(String.valueOf(((JSONObject)accessTemperature.get(i)).getDouble("wind_speed") + "KM/H"));
            degreeText[i-3].setText(String.valueOf(((JSONObject)accessTemperature.get(i)).getDouble("wind_deg") + "°"));

            day = new Date(((JSONObject)accessTemperature.get(i)).getLong("dt")*1000);
            dayNameText[i-3].setText(simpleDateFormat.format(day));
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
                HttpResponse httpResponse = httpClient.execute(new HttpGet("https://api.openweathermap.org/data/2.5/onecall?lat="+lat+"&lon="+lon+"&exclude=minutely,hourly,current&appid=9db5da2f43761af17da19bc3f2c12683&units=metric"));
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
