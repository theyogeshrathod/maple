package com.coolapps.yo.maple.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.coolapps.yo.maple.R;

/**
 * This is a custom ImageView which supports aspect ratio.
 * Aspect ratio can be either set from XML or programmatically by calling MapleImageView#setAspectRatio().
 */
@SuppressLint("AppCompatCustomView")
public class MapleImageView extends ImageView {

    private static final float NO_ASPECT_RATIO = 0.0f;

    private float mAspectRatio = NO_ASPECT_RATIO;

    public MapleImageView(Context context) {
        this(context, null);
    }

    public MapleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MapleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        if (attrs == null) {
            return;
        }

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MapleImageView);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            final int attr = typedArray.getIndex(i);
            if (attr == R.styleable.MapleImageView_aspectRatio) {
                mAspectRatio = typedArray.getFloat(attr, NO_ASPECT_RATIO);
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (Float.compare(mAspectRatio, NO_ASPECT_RATIO) == 0) {
            return;
        }

        final int measuredWidth = getMeasuredWidth();
        final int measuredHeight = getMeasuredHeight();

        final int finalHeight = (int) (measuredWidth / mAspectRatio);
        if (finalHeight != measuredHeight) {
            setMeasuredDimension(measuredWidth, finalHeight);
        }
    }

    public void setAspectRatio(float aspectRatio) {
        if (Float.compare(mAspectRatio, aspectRatio) == 0) {
            return;
        }
        mAspectRatio = aspectRatio;
        requestLayout();
    }
}
