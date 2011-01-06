package com.exiashio.batterystatussquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class BatteryStatusSquarePreference extends PreferenceActivity
        implements OnSharedPreferenceChangeListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "Preference";

    // preference
    private static final String SQUARE_COLOR = "square_color";
    private static final String TEXT_VISIBLE = "text_visible";
    private static final String TEXT_COLOR = "text_color";

    // view
    private ListPreference mSquareColor;
    private ListPreference mTextColor;

    private boolean mChanged = false;

    // service state
    static boolean mRunning = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);

        mSquareColor = (ListPreference)getPreferenceScreen().findPreference(SQUARE_COLOR);
        mSquareColor.setSummary(mSquareColor.getEntry());

        mTextColor = (ListPreference)getPreferenceScreen().findPreference(TEXT_COLOR);
        mTextColor.setSummary(mTextColor.getEntry());
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.v(TAG, "onStop");
        super.onStop();

        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);

        // need update widget ?
        if (mChanged) {
            if (mRunning) {
                if (DEBUG) Log.v(TAG, "setting changed, update service");
                Intent intent = new Intent(this, UpdateService.class);
                startService(intent);
            }

            mChanged = false;
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (DEBUG) Log.v(TAG, "onSharedPreferenceChanged:" + key);

        // update summary text
        if (key.equals(SQUARE_COLOR)) {
            mChanged = true;
            mSquareColor.setSummary(mSquareColor.getEntry());
        } else if (key.equals(TEXT_VISIBLE)) {
            mChanged = true;
        } else if (key.equals(TEXT_COLOR)) {
            mChanged = true;
            mTextColor.setSummary(mTextColor.getEntry());
        }
    }

    public static int getSquareColor(Context context) {
        String c = PreferenceManager.getDefaultSharedPreferences(context).
                getString(SQUARE_COLOR, "black");
        return Color.parseColor(c);
    }

    public static boolean isTextVisible(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(TEXT_VISIBLE, false);
    }

    public static int getTextColor(Context context) {
        String c = PreferenceManager.getDefaultSharedPreferences(context).
                getString(TEXT_COLOR, "black");
        return Color.parseColor(c);
    }
}
