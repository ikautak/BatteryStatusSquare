package com.exiashio.batterystatussquare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;

public class BatteryStatusSquarePreference extends PreferenceActivity
        implements OnSharedPreferenceChangeListener {
    private static final boolean DEBUG = false;
    private static final String TAG = "Preference";

    // preference
    private static final String SQUARE_COLOR = "square_color2";
    private static final String TEXT_VISIBLE = "text_visible";
    private static final String TEXT_COLOR = "text_color2";

    private boolean mChanged = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.v(TAG, "onCreate");

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        getPreferenceScreen().getSharedPreferences().
                registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.v(TAG, "onStop");
        super.onStop();

        getPreferenceScreen().getSharedPreferences().
                unregisterOnSharedPreferenceChangeListener(this);

        // need update widget ?
        if (mChanged) {
            if (UpdateService.isRunning()) {
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
        if (key.equals(SQUARE_COLOR) || key.equals(TEXT_VISIBLE) || key.equals(TEXT_COLOR)) {
            mChanged = true;
        }
    }

    public static int getSquareColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(SQUARE_COLOR, 0x80000000);
    }

    public static boolean isTextVisible(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getBoolean(TEXT_VISIBLE, true);
    }

    public static int getTextColor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).
                getInt(TEXT_COLOR, Color.BLACK);
    }
}
