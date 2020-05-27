package com.coolapps.yo.maple;

import androidx.annotation.NonNull;

public enum AccountTypeEnum {
    UNKNOWN("0"),
    MANUFACTURER("1"),
    TRADER("2"),
    SERVICE_PROVIDER("3");

    private final String mValue;

    AccountTypeEnum(@NonNull String value) {
        mValue = value;
    }

    public static AccountTypeEnum from(@NonNull String value) {
        for (AccountTypeEnum type : values()) {
            if (value.equals(type.mValue)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public String getValue() {
        return mValue;
    }
}
