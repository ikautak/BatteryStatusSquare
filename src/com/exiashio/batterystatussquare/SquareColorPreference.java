package com.exiashio.batterystatussquare;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class SquareColorPreference extends AbstractColorPreference {
    public SquareColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected int getCurrentColor() {
        return BatteryStatusSquarePreference.getSquareColor(getContext());
    }

    @Override
    protected void updateSampleView(int color) {
        // Change image color
        Bitmap bitmap = Bitmap.createBitmap(SAMPLE_IMAGE_W, SAMPLE_IMAGE_H,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(color);

        c.drawRect(0, 0, SAMPLE_IMAGE_W, SAMPLE_IMAGE_H, p);

        mSampleImage.setImageBitmap(bitmap);

        // Text color is fixed
        mSampleText.setTextColor(BatteryStatusSquarePreference.getTextColor(getContext()));
    }
}
