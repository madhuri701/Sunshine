package com.example.android.sunshine;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    private static final String FORECAST_SHARE_HASHTAG = "#SunshineApp";
    private String mForecast;

    private TextView mWeatherDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherDisplay = (TextView) (findViewById(R.id.display_weather_data));

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                 mForecast = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);

                 mWeatherDisplay.setText(mForecast);
            }
        }
    }

    private Intent createShareForecastIntent()
    {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this).setType("text/plain").setText(mForecast+FORECAST_SHARE_HASHTAG).getIntent();
        return shareIntent;
    }


}