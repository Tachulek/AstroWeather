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
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;

public class MoonFragment extends Fragment {

    private TextView textLongitude;
    private TextView textLatitude;
    private TextView textMoonriseTime;
    private TextView textMoonsetTime;
    private TextView textNewMoon;
    private TextView textFullMoon;
    private TextView textPhase;
    private TextView textSynodicDay;

    private MoonViewModel moonViewModel;

    private SharedPreferences preferences;

    private final String PREFERENCES_FILE = "filePreference";

    private final String PREFERENCES_LONGITUDE = "longitudePreference";
    private final String PREFERENCES_LATITUDE = "latitudePreference";

    public void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        moonViewModel = ViewModelProviders.of(getActivity()).get(MoonViewModel.class);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.menu_moon, container, false);

        textLongitude = view.findViewById(R.id.textLongitude);
        textLatitude = view.findViewById(R.id.textLatitude);
        textMoonriseTime = view.findViewById(R.id.textMoonriseTime);
        textMoonsetTime = view.findViewById(R.id.textMoonsetTime);
        textNewMoon = view.findViewById(R.id.textNewMoon);
        textFullMoon = view.findViewById(R.id.textFullMoon);
        textPhase = view.findViewById(R.id.textPhase);

        Context context = getActivity();
        preferences = context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE);

        textLongitude.setText(preferences.getString(PREFERENCES_LONGITUDE, "19.46"));
        textLatitude.setText(preferences.getString(PREFERENCES_LATITUDE, "51.76"));

        AstroCalculator.MoonInfo moonInfo = moonViewModel.getMoonInfo();

        if(moonInfo != null) {

            textMoonriseTime.setText((String.format("%02d", moonInfo.getMoonrise().getHour()))
                    .concat(":")
                    .concat(String.format("%02d", moonInfo.getMoonrise().getMinute()))
                    .concat(":")
                    .concat(String.format("%02d", moonInfo.getMoonrise().getSecond()))
            );
            textMoonsetTime.setText((String.format("%02d", moonInfo.getMoonset().getHour()))
                    .concat(":")
                    .concat(String.format("%02d", moonInfo.getMoonset().getMinute()))
                    .concat(":")
                    .concat(String.format("%02d", moonInfo.getMoonset().getSecond()))
            );
            textNewMoon.setText(moonInfo.getNextNewMoon().toString());
            textFullMoon.setText(moonInfo.getNextFullMoon().toString());
            textPhase.setText(String.valueOf(Math.round((moonInfo.getIllumination()*100)))
                    .concat(" %")
            );

            Calendar today = Calendar.getInstance();
            AstroDateTime nextNewMoon = moonInfo.getNextNewMoon();
            Calendar newMoonCalendar = Calendar.getInstance();
            newMoonCalendar.set(nextNewMoon.getYear(),
                    nextNewMoon.getMonth() - 1, nextNewMoon.getDay());

        }

        return view;
    }
}
