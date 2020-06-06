package com.coolapps.yo.maple;

/**
 * Enum for news type
 */
public enum ArticleContentType {
    UNKNOWN(-1),
    FREE(0),
    PAID(1);

    private int mVal;

    ArticleContentType(int val) {
        mVal = val;
    }

    public ArticleContentType from(int val) {
        for (ArticleContentType type : values()) {
            if (type.mVal == val) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return mVal;
    }
};
