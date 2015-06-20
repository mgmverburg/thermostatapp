package nl.tue.thermostatv3;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import org.thermostatapp.util.HeatingSystem;
import org.thermostatapp.util.WeekProgram;

/**
 * Created by Stan on 20-6-2015.
 */

public class Settings extends PreferenceActivity{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.user_settings);

    }

}
