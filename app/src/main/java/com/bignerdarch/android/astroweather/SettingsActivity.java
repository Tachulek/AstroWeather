package com.bignerdarch.android.astroweather;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;

public class SettingsActivity extends AppCompatActivity {

    private EditText editRefresh;
    private EditText editLongitude;
    private EditText editLatitude;
    private FloatingActionButton fabOk;

    private SharedPreferences preferences;

    private final String PREFERENCES_FILE = "filePreference";
    private final String PREFERENCES_REFRESH = "refreshPreference";
    private final String PREFERENCES_LONGITUDE = "longitudePreference";
    private final String PREFERENCES_LATITUDE = "latitudePreference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editRefresh = findViewById(R.id.editRefresh);
        editLongitude = findViewById(R.id.editLongitude);
        editLatitude = findViewById(R.id.editLatitude);
        fabOk = findViewById(R.id.fabOk);

        preferences = getSharedPreferences(PREFERENCES_FILE, Activity.MODE_PRIVATE);

        editRefresh.setText(preferences.getString(PREFERENCES_REFRESH, "60"));
        editLongitude.setText(preferences.getString(PREFERENCES_LONGITUDE, "19.46"));
        editLatitude.setText(preferences.getString(PREFERENCES_LATITUDE, "51.76"));



        fabOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(settingsValidator()) {
                    SharedPreferences.Editor sharedPreferencesEditor = preferences.edit();
                    sharedPreferencesEditor.putString(PREFERENCES_REFRESH, editRefresh.getText().toString());
                    sharedPreferencesEditor.putString(PREFERENCES_LONGITUDE, editLongitude.getText().toString());
                    sharedPreferencesEditor.putString(PREFERENCES_LATITUDE, editLatitude.getText().toString());
                    sharedPreferencesEditor.commit();

                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);

                    finish();
                }
            }
        });
    }

    public boolean settingsValidator() {

        String validateLongitude = editLongitude.getText().toString();
        String validateLatitude = editLatitude.getText().toString();
        String validateRefresh = editRefresh.getText().toString();

        if(TextUtils.isEmpty(validateLatitude) || TextUtils.isEmpty(validateLongitude) || TextUtils.isEmpty(validateRefresh)){
            toast("Uzupełnij wszystkie pola!");
            return false;
        }
        BigDecimal latitudeToCheck = new BigDecimal(validateLatitude);
        if(latitudeToCheck.compareTo(BigDecimal.valueOf(90)) == 1
                || latitudeToCheck.compareTo(BigDecimal.valueOf(-90)) == -1){
            toast("Szerokość geograficzna (latitude) musi być w przedziale od -90 do 90!");
            return false;
        }
        BigDecimal longitudeToCheck = new BigDecimal(validateLongitude);
        if(longitudeToCheck.compareTo(BigDecimal.valueOf(180)) == 1
                || longitudeToCheck.compareTo(BigDecimal.valueOf(-180)) == -1){
            toast("Długość geograficzna (longitude) musi być w przedziale od -180 do 180!");
            return false;
        }
        if(Integer.valueOf(validateRefresh) <= 0){
            toast("Wartość odświeżania musi być większa od 0!");
            return false;
        }

        return true;
    }

    public void toast (CharSequence text) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
