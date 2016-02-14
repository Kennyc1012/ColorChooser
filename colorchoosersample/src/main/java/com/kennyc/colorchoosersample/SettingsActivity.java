package com.kennyc.colorchoosersample;


import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;

public class SettingsActivity extends AppCompatPreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        findPreference("color").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Toast.makeText(getApplicationContext(), "Color Selected " + newValue, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
