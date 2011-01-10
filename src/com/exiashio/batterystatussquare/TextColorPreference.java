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
    private static final int SAMPLE_IMAGE_W = 150;
    private static final int SAMPLE_IMAGE_H = 40;
    private static final int TEXT_SIZE = 22;

    private ImageView mSampleImage;
    private TextView mSampleText;

    public TextColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.text_color_preference_dialog);
    }

    @Override
    protected void initSampleView(View view) {
        mSampleImage = (ImageView)view.findViewById(R.id.sample_color);

        mSampleText = (TextView)view.findViewById(R.id.sample_text);
        mSampleText.setTextSize(TEXT_SIZE);
    }

    @Override
    protected void updateSampleView(int color) {
        Bitmap bitmap = Bitmap.createBitmap(SAMPLE_IMAGE_W, SAMPLE_IMAGE_H,
                Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(BatteryStatusSquarePreference.getSquareColor(getContext()));

        c.drawRect(0, 0, SAMPLE_IMAGE_W, SAMPLE_IMAGE_H, p);

        mSampleImage.setImageBitmap(bitmap);


        mSampleText.setTextColor(color);
    }
}
