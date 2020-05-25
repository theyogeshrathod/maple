package com.coolapps.yo.maple.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.appcompat.widget.AppCompatButton;

import com.coolapps.yo.maple.R;

public class MapleButton extends AppCompatButton {

    public MapleButton(Context context) {
        this(context, null);
    }

    public MapleButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setBackgroundResource(R.drawable.maple_button_background);
        setTextColor(getResources().getColor(R.color.buttonTextColor));
        setGravity(Gravity.CENTER);
        setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        setTextSize(getResources().getDimensionPixelSize(R.dimen.maple_button_font_size));
    }
}
