package com.exiashio.batterystatussquare;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

public class UpdateService extends Service {
    private static final boolean DEBUG = false;
    private static final String TAG = "UpdateService";

    private static final int SQUARE_MIN = 1;
    private static final int SQUARE_MAX = 70;
    private static final int TEXT_SIZE = 22;

    private int mLevel;

    private static RemoteViews mRemoteViews;
    private static ComponentName mComponentName;
    private static AppWidgetManager mAppWidgetManager;
    private static Bitmap mBitmap;

    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (DEBUG) Log.v(TAG, "onReceive");
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_BATTERY_CHANGED) == false) {
                // nothing to do.
                return;
            }

            mLevel = intent.getIntExtra("level", 0);
            updateWidget();
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        if (DEBUG) Log.v(TAG, "onCreate");

        BatteryStatusSquarePreference.mRunning = true;

        mRemoteViews = new RemoteViews(getPackageName(), R.layout.widget);
        mComponentName = new ComponentName(this, WidgetProvider.class);
        mAppWidgetManager = AppWidgetManager.getInstance(this);

        // receive ACTION_BATTERY_CHANGED.
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);

        // set preference activity intent.
        Intent clickIntent = new Intent(this, BatteryStatusSquarePreference.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, clickIntent, 0);
        mRemoteViews.setOnClickPendingIntent(R.id.widget_base, pendingIntent);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        if (DEBUG) Log.v(TAG, "onStart");

        updateWidget();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (DEBUG) Log.v(TAG, "onStartCommand");

        updateWidget();

        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.v(TAG, "onDestroy");

        unregisterReceiver(mBroadcastReceiver);

        BatteryStatusSquarePreference.mRunning = false;
    }

    private void updateWidget() {
        mBitmap = Bitmap.createBitmap(SQUARE_MAX, SQUARE_MAX, Bitmap.Config.ARGB_8888);

        drawBitmap(mBitmap);

        mRemoteViews.setImageViewBitmap(R.id.image, mBitmap);

        if (BatteryStatusSquarePreference.isTextVisible(this)) {
            mRemoteViews.setTextViewText(R.id.text, Integer.toString(mLevel) + "%");
            mRemoteViews.setFloat(R.id.text, "setTextSize", TEXT_SIZE);
            mRemoteViews.setTextColor(R.id.text,
                    BatteryStatusSquarePreference.getTextColor(this));
            mRemoteViews.setViewVisibility(R.id.text, View.VISIBLE);
        } else {
            mRemoteViews.setViewVisibility(R.id.text, View.GONE);
        }

        mAppWidgetManager.updateAppWidget(mComponentName, mRemoteViews);
    }

    private void drawBitmap(Bitmap bitmap) {
        if (DEBUG) Log.v(TAG, "drawBitmap");

        Canvas c = new Canvas(bitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);

        // color setting
        int color = BatteryStatusSquarePreference.getSquareColor(this);
        if (DEBUG) Log.v(TAG, "color : " + Integer.toHexString(color));
        p.setColor(color);
        p.setAlpha(0x80);

        int level = (int)(mLevel*SQUARE_MAX/100 + 0.5f);
        if (DEBUG) Log.v(TAG, "level : " + (SQUARE_MAX - level));

        c.drawRect(SQUARE_MIN, SQUARE_MAX - level, SQUARE_MAX, SQUARE_MAX, p);
    }
}
