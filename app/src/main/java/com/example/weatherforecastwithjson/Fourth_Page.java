package com.example.weatherforecastwithjson;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.Nullable;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Fourth_Page extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fourth, container, false);
    }

    private RadioButton kelvinRadioButton, celciusRadioButton, fahrenheitRadioButton;
    private RadioButton GPSRadioButton, manualRadioButton;
    private Button saveButton;
    private static EditText locationEditText;
    public static String location = "Istanbul";
    public static String temperatureType = "metric";
    public static String lon = "28.58", lat = "41";
    public static int lastLocationOption = 0, lastTemperatureOption = 0;
    final Context activity = getContext();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        kelvinRadioButton = (RadioButton) view.findViewById(R.id.kelvinRadioButton);
        celciusRadioButton = (RadioButton) view.findViewById(R.id.celciusRadioButton);
        fahrenheitRadioButton = (RadioButton) view.findViewById(R.id.fahrenheitRadioButton);
        GPSRadioButton = (RadioButton) view.findViewById(R.id.gpsRadioButton);
        manualRadioButton = (RadioButton) view.findViewById(R.id.manualRadioButton);
        saveButton = (Button) view.findViewById(R.id.saveButton);
        locationEditText = (EditText) view.findViewById(R.id.locationEditText);
        locationEditText.setText(location);

        if (lastLocationOption == R.id.gpsRadioButton) {
            locationEditText.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
            GPSRadioButton.setChecked(true);
            GPSRadioButton.setSelected(true);
        } else if (lastLocationOption == R.id.manualRadioButton) {
            locationEditText.setVisibility(View.VISIBLE);
            locationEditText.setText(location);
            saveButton.setVisibility(View.VISIBLE);
            manualRadioButton.setChecked(true);
            manualRadioButton.setSelected(true);
        }

        if (lastTemperatureOption == R.id.kelvinRadioButton) {
            First_Page.temperatureType = Second_Page.temperatureType = temperatureType = "standard";
            kelvinRadioButton.setChecked(true);
            kelvinRadioButton.setSelected(true);
        } else if (lastTemperatureOption == R.id.celciusRadioButton) {
            First_Page.temperatureType = Second_Page.temperatureType = temperatureType = "metric";
            celciusRadioButton.setChecked(true);
            celciusRadioButton.setSelected(true);
        } else if (lastTemperatureOption == R.id.fahrenheitRadioButton) {
            First_Page.temperatureType = Second_Page.temperatureType = temperatureType = "imperial";
            fahrenheitRadioButton.setChecked(true);
            fahrenheitRadioButton.setSelected(true);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location.length() == 0)
                    location = "Istanbul";
                new hukoWeatherForecast().execute();
                location = locationEditText.getText().toString();
                First_Page.location = Second_Page.location = Third_Page.location = location;
                Second_Page.lat = Third_Page.lat = lat;
                Second_Page.lon = Third_Page.lon = lon;
            }
        });

        GPSRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationEditText.setVisibility(View.INVISIBLE);
                saveButton.setVisibility(View.INVISIBLE);
                GPSRadioButton.setSelected(true);
                lastLocationOption = GPSRadioButton.getId();
                lat = "41";
                lon = "28.58";
                temperatureType = "metric";
                location = "Istanbul";
            }
        });
        manualRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationEditText.setVisibility(View.VISIBLE);
                locationEditText.setText(location);
                saveButton.setVisibility(View.VISIBLE);
                manualRadioButton.setSelected(true);
                lastLocationOption = manualRadioButton.getId();
            }
        });
        kelvinRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                First_Page.temperatureType = Second_Page.temperatureType = temperatureType = "standard";
                kelvinRadioButton.setSelected(true);
                lastTemperatureOption = kelvinRadioButton.getId();
            }
        });
        celciusRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                First_Page.temperatureType = Second_Page.temperatureType = temperatureType = "metric";
                celciusRadioButton.setSelected(true);
                lastTemperatureOption = celciusRadioButton.getId();
            }
        });
        fahrenheitRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                First_Page.temperatureType = Second_Page.temperatureType = temperatureType = "imperial";
                fahrenheitRadioButton.setSelected(true);
                lastTemperatureOption = fahrenheitRadioButton.getId();
            }
        });
    }

    private void parseJSON(String s) throws JSONException {
        JSONObject myMainJSON = new JSONObject(s);
        JSONObject coordinates = myMainJSON.getJSONObject("coord");
        lon = String.valueOf(coordinates.getDouble("lon"));
        lat = String.valueOf(coordinates.getDouble("lat"));
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
