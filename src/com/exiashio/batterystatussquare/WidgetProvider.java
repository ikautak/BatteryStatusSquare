package com.exiashio.batterystatussquare;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class WidgetProvider extends AppWidgetProvider {
    private static final boolean DEBUG = false;
    private static final String TAG = "WidgetProvider";

    private static Intent mIntent = null;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        if (DEBUG) Log.v(TAG, "onUpdate");

        if (mIntent == null) {
            mIntent = new Intent(context, UpdateService.class);
        }
        context.startService(mIntent);
    }

    @Override
    public void onDisabled(Context context) {
        if (DEBUG) Log.v(TAG, "onDisabled");

        if (mIntent == null) {
            mIntent = new Intent(context, UpdateService.class);
        }
        context.stopService(mIntent);
    }
}
