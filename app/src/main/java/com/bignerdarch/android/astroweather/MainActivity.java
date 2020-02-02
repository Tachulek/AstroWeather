package com.bignerdarch.android.astroweather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.TextClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    AppBarLayout appBar;
    TabLayout tabs;
    ViewPager viewPager;
    FloatingActionButton menu;
    PagerAdapter pagerAdapter;
    TabItem tabSun;
    TabItem tabMoon;
    TextClock textClock;

    private SunViewModel sunViewModel;
    private MoonViewModel moonViewModel;

    final Handler handler = new Handler();
    private Runnable sunMoonFragmentsUpdateRunnable;

    private SharedPreferences preferences;

    private final String PREFERENCES_FILE = "filePreference";
    private final String PREFERENCES_REFRESH = "refreshPreference";
    private final String PREFERENCES_LONGITUDE = "longitudePreference";
    private final String PREFERENCES_LATITUDE = "latitudePreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sunViewModel = ViewModelProviders.of(this).get(SunViewModel.class);
        moonViewModel = ViewModelProviders.of(this).get(MoonViewModel.class);

        appBar = findViewById(R.id.appBar);
        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        menu = findViewById(R.id.fabMenu);
        tabSun = findViewById(R.id.tabSun);
        tabMoon = findViewById(R.id.tabMoon);
        textClock = findViewById(R.id.textClock);

        textClock.setFormat24Hour("kk:mm:ss");

        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));

        SunMoonFragmentsRunnable();

    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {

            if (resultCode == Activity.RESULT_OK) {

                SunMoonFragmentsRunnable();
            }
        }
    }

    protected void SunMoonFragmentsRunnable () {

        sunMoonFragmentsUpdateRunnable = new Runnable() {

            @Override
            public void run() {

                preferences = getSharedPreferences(PREFERENCES_FILE, Activity.MODE_PRIVATE);

                String refresh = preferences.getString(PREFERENCES_REFRESH, "60");

                Double longitude = Double.valueOf(preferences.getString(PREFERENCES_LONGITUDE, "19.46"));
                Double latitude = Double.valueOf(preferences.getString(PREFERENCES_LATITUDE, "51.76"));

                Calendar c = Calendar.getInstance();

                AstroDateTime astroDateTime = new AstroDateTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                        c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                        c.get(Calendar.SECOND), 0, true);
                AstroCalculator astroCalculator = new AstroCalculator(astroDateTime, new AstroCalculator.Location(latitude, longitude));

                AstroCalculator.SunInfo sunInfo = astroCalculator.getSunInfo();
                AstroCalculator.MoonInfo moonInfo = astroCalculator.getMoonInfo();

                sunViewModel.setSunInfo(sunInfo);
                moonViewModel.setMoonInfo(moonInfo);

                viewPager.getAdapter().notifyDataSetChanged();

                handler.postDelayed(this, (Long.valueOf(refresh) * 1000));

                toast("Runnable, refresh time " + refresh + "s");

            }
        };
    }

    protected void onResume() {
        super.onResume();

        handler.post(sunMoonFragmentsUpdateRunnable);
        //toast("onResume");
    }

    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(sunMoonFragmentsUpdateRunnable);
        //toast("onPause");
    }

    public void toast (CharSequence text) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
