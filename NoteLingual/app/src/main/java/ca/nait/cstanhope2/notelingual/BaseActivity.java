package ca.nait.cstanhope2.notelingual;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);

        //region set the theme
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

        //region override phone's locale with selected language
        String langChoice = prefs.getString(getString(R.string.prefsLangKey), getString(R.string.prefsLangDefaultValue));

        // only override the phone's default language if the user has asked for it
        if(!langChoice.equals(getString(R.string.prefsLangDefaultValue))){
            Locale locale = new Locale(langChoice);
            Locale.setDefault(locale);

            Resources res = this.getResources();
            Configuration config = new Configuration(res.getConfiguration());

            if(Build.VERSION.SDK_INT < 17){ // "setLocale" only available for 17+ API
                config.locale = locale;
            } else {
                config.setLocale(locale);
            }

            res.updateConfiguration(config, res.getDisplayMetrics());
        }
        //endregion

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Intent intent = new Intent(this, NoteListActivity.class);

        finish(); // this closes the current instance of the activity
        this.startActivity(intent);
    }
}
