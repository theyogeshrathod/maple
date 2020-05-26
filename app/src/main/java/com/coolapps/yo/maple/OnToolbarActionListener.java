package com.coolapps.yo.maple;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

public interface OnToolbarActionListener {
    void setToolbarTitle(@NonNull String title);

    void setToolbarTitle(@StringRes int res);

    void setToolbarBackClickListener(View.OnClickListener listener);

    void setToolbarSettingsClickListener(View.OnClickListener listener);

    void setShowBackButton(boolean show);

    void setShowSettingsButton(boolean show);
}
