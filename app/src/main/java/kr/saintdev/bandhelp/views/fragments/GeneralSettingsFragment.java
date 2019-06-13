package kr.saintdev.bandhelp.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.View;

import kr.saintdev.bandhelp.R;
import kr.saintdev.bandhelp.core.libs.PermissionUtility;

public class GeneralSettingsFragment extends PreferenceFragment {
    private PreferenceScreen accessibilityService = null;
    private PreferenceScreen overlayPremission = null;
    private ListPreference displayOrient = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.general_settings);

        this.accessibilityService = (PreferenceScreen) findPreference("permission_accessibility");
        this.overlayPremission = (PreferenceScreen) findPreference("permission_overlay");
        this.displayOrient = (ListPreference) findPreference("display_orient");

        this.accessibilityService.setOnPreferenceClickListener(accessibilityServieClicked);
        this.overlayPremission.setOnPreferenceClickListener(overlayServiceOverlay);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String[] listItems = getResources().getStringArray(R.array.general_display_orient_dirg);

        String listVal = pref.getString("display_orient", "0");
        switch(listVal) {
            case "0": this.displayOrient.setSummary(listItems[0]); break;
            case "8": this.displayOrient.setSummary(listItems[1]); break;
            case "1": this.displayOrient.setSummary(listItems[2]); break;
            case "9": this.displayOrient.setSummary(listItems[3]); break;
        }

        this.displayOrient.setOnPreferenceChangeListener(displayOrientValChangeListener);
    }

    private Preference.OnPreferenceClickListener accessibilityServieClicked = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            PermissionUtility.openAccessibilityServiceSettings(getActivity());
            return false;
        }
    };

    private Preference.OnPreferenceClickListener overlayServiceOverlay = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            PermissionUtility.openOverlayPermissionSetting(getActivity());
            return false;
        }
    };

    private Preference.OnPreferenceChangeListener displayOrientValChangeListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String[] listItems = getResources().getStringArray(R.array.general_display_orient_dirg);
            switch(newValue.toString()) {
                case "0": displayOrient.setSummary(listItems[0]); break;
                case "8": displayOrient.setSummary(listItems[1]); break;
                case "1": displayOrient.setSummary(listItems[2]); break;
                case "9": displayOrient.setSummary(listItems[3]); break;
            }
            return true;
        }
    };
}
