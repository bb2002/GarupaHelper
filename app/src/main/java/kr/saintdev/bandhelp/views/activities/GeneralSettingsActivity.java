package kr.saintdev.bandhelp.views.activities;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import kr.saintdev.bandhelp.R;

public class GeneralSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_settings);
    }
}
