package com.bignerdarch.android.astroweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.astrocalculator.AstroCalculator;

public class SunFragment extends Fragment {

    private TextView textLongitude;
    private TextView textLatitude;
    private TextView textSunriseTime;
    private TextView textSunriseAzimuth;
    private TextView textSunsetTime;
    private TextView textSunsetAzimuth;
    private TextView textMorning;
    private TextView textEvening;

    private SunViewModel sunViewModel;

    private SharedPreferences preferences;

    private final String PREFERENCES_FILE = "filePreference";

    private final String PREFERENCES_LONGITUDE = "longitudePreference";
    private final String PREFERENCES_LATITUDE = "latitudePreference";

    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        sunViewModel = ViewModelProviders.of(getActivity()).get(SunViewModel.class);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.menu_sun, container, false);

        textLongitude = view.findViewById(R.id.textLongitude);
        textLatitude = view.findViewById(R.id.textLatitude);
        textSunriseTime = view.findViewById(R.id.textSunriseTime);
        textSunriseAzimuth = view.findViewById(R.id.textSunriseAzimuth);
        textSunsetTime = view.findViewById(R.id.textSunsetTime);
        textSunsetAzimuth = view.findViewById(R.id.textSunsetAzimuth);
        textMorning = view.findViewById(R.id.textMorning);
        textEvening = view.findViewById(R.id.textEvening);

        Context context = getActivity();
        preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        textLongitude.setText(preferences.getString(PREFERENCES_LONGITUDE, "19.46"));
        textLatitude.setText(preferences.getString(PREFERENCES_LATITUDE, "51.76"));

        AstroCalculator.SunInfo sunInfo = sunViewModel.getSunInfo();

        if(sunInfo != null) {

            textSunriseTime.setText((String.format("%02d", sunInfo.getSunrise().getHour()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getSunrise().getMinute()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getSunrise().getSecond()))
            );
            textSunriseAzimuth.setText(String.valueOf(sunInfo.getAzimuthRise()).substring(0,6));
            textSunsetTime.setText((String.format("%02d", sunInfo.getSunset().getHour()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getSunset().getMinute()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getSunset().getSecond()))
            );
            textSunsetAzimuth.setText(String.valueOf(sunInfo.getAzimuthSet()).substring(0,6));
            textMorning.setText((String.format("%02d", sunInfo.getTwilightMorning().getHour()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getTwilightMorning().getMinute()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getTwilightMorning().getSecond()))
            );
            textEvening.setText((String.format("%02d", sunInfo.getTwilightEvening().getHour()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getTwilightEvening().getMinute()))
                    .concat(":")
                    .concat(String.format("%02d", sunInfo.getTwilightEvening().getSecond()))
            );
        }

        return view;
    }

}
