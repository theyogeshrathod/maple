package com.coolapps.yo.maple.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.coolapps.yo.maple.R;

/**
 * Basic edit text to use in this Project. This has custom support like adding Bold, Italic, etc styles.
 */
@SuppressLint("AppCompatCustomView")
public class MapleEditText extends EditText {

    private static final String TAG = "MapleEditText";

    public MapleEditText(Context context) {
        this(context, null);
    }

    public MapleEditText(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.editTextStyle);
    }

    public MapleEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(@NonNull ActionMode mode, @NonNull Menu menu) {
                Log.d(TAG, "onCreateActionMode: " + "mode = " + mode + ", menu = " + menu);

                final MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.selection_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(@NonNull ActionMode mode, @NonNull Menu menu) {
                Log.d(TAG, "onPrepareActionMode: " + "mode = " + mode + ", menu = " + menu);
                return false;
            }

            @Override
            public boolean onActionItemClicked(@NonNull ActionMode mode, @NonNull MenuItem item) {
                Log.d(TAG, "onActionItemClicked: " + "mode = " + mode + ", item = " + item);

                final CharacterStyle cs;
                final int start = getSelectionStart();
                final int end = getSelectionEnd();
                final SpannableStringBuilder ssb = new SpannableStringBuilder(getText());

                switch(item.getItemId()) {

                    case R.id.bold:
                        cs = new StyleSpan(Typeface.BOLD);
                        ssb.setSpan(cs, start, end, 1);
                        setText(ssb);
                        return true;

                    case R.id.italic:
                        cs = new StyleSpan(Typeface.ITALIC);
                        ssb.setSpan(cs, start, end, 1);
                        setText(ssb);
                        return true;

                    case R.id.underline:
                        cs = new UnderlineSpan();
                        ssb.setSpan(cs, start, end, 1);
                        setText(ssb);
                        return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(@NonNull ActionMode mode) {
                Log.d(TAG, "onDestroyActionMode: " + "mode = " + mode);
                // NO-OP
            }
        });

    }
}
