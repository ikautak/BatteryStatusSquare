package com.exiashio.batterystatussquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TextColorPreference extends AbstractColorPreference {
    public TextColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getCurrentColor() {
        return BatteryStatusSquarePreference.getTextColor(getContext());
    }

    @Override
    protected void updateSampleView(int color) {
        // Image color is fixed
        Bitmap bitmap = Bitmap.createBitmap(SAMPLE_IMAGE_W, SAMPLE_IMAGE_H,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(BatteryStatusSquarePreference.getSquareColor(getContext()));

        c.drawRect(0, 0, SAMPLE_IMAGE_W, SAMPLE_IMAGE_H, p);

        mSampleImage.setImageBitmap(bitmap);

        // Change text color
        mSampleText.setTextColor(color);
    }
}
