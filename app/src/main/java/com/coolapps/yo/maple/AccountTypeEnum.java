package com.coolapps.yo.maple;

import androidx.annotation.NonNull;

public enum AccountTypeEnum {
    UNKNOWN("0"),
    ADMIN("1"),
    EDITOR("2"),
    USER("3");

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
