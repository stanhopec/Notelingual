package ca.nait.cstanhope2.notelingual;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class UserPrefsActivity extends PreferenceActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //region set the theme
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String themeChoice = prefs.getString(this.getString(R.string.prefsThemeKey), this.getString(R.string.prefsThemeDefaultValue));
        switch(themeChoice){
            case "light":{
                setTheme(R.style.LightTheme);
                break;
            }
            case "dark":{
                setTheme(R.style.DarkTheme);
                break;
            }
            case "midnight":{
                setTheme(R.style.MidnightTheme);
                break;
            }
            case "sample":{
                setTheme(R.style.SampleTheme);
                break;
            }
            default:{
                setTheme(R.style.LightTheme);
                break;
            }
        }
        //endregion

        super.onCreate(savedInstanceState);
        this.addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }
}
