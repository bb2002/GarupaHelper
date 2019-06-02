package kr.saintdev.bandhelp.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import kr.saintdev.bandhelp.R;

public class TimeSettingFragment extends PreferenceFragment {
    private ListPreference safeTimePref;
    private ListPreference warningTimePref;
    private ListPreference dangerTimePref;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.time_settings_pref);

        this.safeTimePref = (ListPreference) findPreference("time_safe");
        this.warningTimePref = (ListPreference) findPreference("time_warning");
        this.dangerTimePref = (ListPreference) findPreference("time_danger");
        this.prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        safeTimePref.setSummary(prefs.getString("time_safe", "30m"));
        warningTimePref.setSummary(prefs.getString("time_warning", "1h 00m"));
        dangerTimePref.setSummary(prefs.getString("time_danger", "1h 30m"));

    }
}
