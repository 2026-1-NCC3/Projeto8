package com.example.projeto8.UI;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

public class CustomTypefaceSpan extends MetricAffectingSpan {

    private final Typeface typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(typeface);
        tp.setFakeBoldText(true);
    }

    @Override
    public void updateMeasureState(TextPaint tp) {
        tp.setTypeface(typeface);
        tp.setFakeBoldText(true);
    }
}
