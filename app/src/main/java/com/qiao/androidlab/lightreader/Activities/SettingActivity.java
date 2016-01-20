package com.qiao.androidlab.lightreader.Activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;

import com.qiao.androidlab.lightreader.R;

import java.util.List;

/**
 * Created by Administrator on 2015/11/30.
 */
public class SettingActivity extends AppCompatPreferenceActivity {

    /**
     * 及时更新表单数据
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummmaryToValueListener =
            new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String stringValue = newValue.toString();
                    if (preference instanceof ListPreference) {
                        ListPreference listPreference = (ListPreference) preference;
                        int index = listPreference.findIndexOfValue(stringValue);
                        preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
                    } else if (preference instanceof RingtonePreference) {
                        if (TextUtils.isEmpty(stringValue)) {
                            preference.setSummary(R.string.pref_ringtone_silent);
                        } else {
                            Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));
                            if (ringtone == null) {
                                preference.setSummary(null);
                            } else {
                                String name = ringtone.getTitle(preference.getContext());
                                preference.setSummary(name);
                            }
                        }
                    } else {
                        preference.setSummary(stringValue);
                    }
                    return false;
                }
            };

    //判断屏幕大小
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummmaryToValueListener);
        sBindPreferenceSummmaryToValueListener.onPreferenceChange(preference, PreferenceManager
                .getDefaultSharedPreferences(preference.getContext())
                .getString(preference.getKey(), " "));
    }


    private void setupActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_setting);
        setupActionBar();
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {  //按返回键
            onBackPressed();    //相当于按下返回键
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
