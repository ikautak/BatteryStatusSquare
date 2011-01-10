package com.exiashio.batterystatussquare;

import android.content.Context;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public abstract class AbstractColorPreference extends DialogPreference
        implements SeekBar.OnSeekBarChangeListener {
    private static final int PROGRESS_MAX = 255;

    // array index
    private static final int RED = 0;
    private static final int GREEN = 1;
    private static final int BLUE = 2;
    private static final int ALPHA = 3;

    private String[] mString = new String[4];
    private SeekBar[] mSeekBar = new SeekBar[4];
    private TextView[] mTextView = new TextView[4];

    abstract protected void initSampleView(View view);
    abstract protected void updateSampleView(int color);

    public AbstractColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        int current = BatteryStatusSquarePreference.getSquareColor(getContext());

        initViews(view, current);

        updateSampleView(current);
    }

    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromTouch) {
        final int id = seekBar.getId();
        if (mSeekBar[RED].getId() == id) {
            mTextView[RED].setText(getColorValueText(mString[RED], progress));
        } else if (mSeekBar[GREEN].getId() == id) {
            mTextView[GREEN].setText(getColorValueText(mString[GREEN], progress));
        } else if (mSeekBar[BLUE].getId() == id) {
            mTextView[BLUE].setText(getColorValueText(mString[BLUE], progress));
        } else if (mSeekBar[ALPHA].getId() == id) {
            mTextView[ALPHA].setText(getColorValueText(mString[ALPHA], progress));
        }

        updateSampleView(totalColor());
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        // NA
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        // NA
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            persistInt(totalColor());
        }
    }

    private void initViews(View view, int color) {
        final int red = Color.red(color);
        mString[RED] = view.getResources().getString(R.string.pref_color_red);
        mTextView[RED] = (TextView)view.findViewById(R.id.text_red);
        mTextView[RED].setText(getColorValueText(mString[RED], red));

        mSeekBar[RED] = (SeekBar)view.findViewById(R.id.seekbar_red);
        mSeekBar[RED].setMax(PROGRESS_MAX);
        mSeekBar[RED].setProgress(red);
        mSeekBar[RED].setOnSeekBarChangeListener(this);

        final int green = Color.green(color);
        mString[GREEN] = view.getResources().getString(R.string.pref_color_green);
        mTextView[GREEN] = (TextView)view.findViewById(R.id.text_green);
        mTextView[GREEN].setText(getColorValueText(mString[GREEN], green));

        mSeekBar[GREEN] = (SeekBar)view.findViewById(R.id.seekbar_green);
        mSeekBar[GREEN].setMax(PROGRESS_MAX);
        mSeekBar[GREEN].setProgress(green);
        mSeekBar[GREEN].setOnSeekBarChangeListener(this);

        final int blue = Color.blue(color);
        mString[BLUE] = view.getResources().getString(R.string.pref_color_blue);
        mTextView[BLUE] = (TextView)view.findViewById(R.id.text_blue);
        mTextView[BLUE].setText(getColorValueText(mString[BLUE], blue));

        mSeekBar[BLUE] = (SeekBar)view.findViewById(R.id.seekbar_blue);
        mSeekBar[BLUE].setMax(PROGRESS_MAX);
        mSeekBar[BLUE].setProgress(blue);
        mSeekBar[BLUE].setOnSeekBarChangeListener(this);

        final int alpha = Color.alpha(color);
        mString[ALPHA] = view.getResources().getString(R.string.pref_color_alpha);
        mTextView[ALPHA] = (TextView)view.findViewById(R.id.text_alpha);
        mTextView[ALPHA].setText(getColorValueText(mString[ALPHA], alpha));

        mSeekBar[ALPHA] = (SeekBar)view.findViewById(R.id.seekbar_alpha);
        mSeekBar[ALPHA].setMax(PROGRESS_MAX);
        mSeekBar[ALPHA].setProgress(alpha);
        mSeekBar[ALPHA].setOnSeekBarChangeListener(this);

        // Sample
        initSampleView(view);
    }

    private String getColorValueText(String color, int value) {
        return color + " : " + value;
    }

    private int totalColor() {
        return Color.argb(mSeekBar[ALPHA].getProgress(),
                mSeekBar[RED].getProgress(),
                mSeekBar[GREEN].getProgress(),
                mSeekBar[BLUE].getProgress());
    }
}
