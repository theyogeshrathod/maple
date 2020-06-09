package com.coolapps.yo.maple.model;

import java.util.Objects;

/**
 * TagInterestsModel - Model class for tags or interests of the user.
 */
public class TagInterestsModel {
    private String mId;
    private String mTagName;

    public TagInterestsModel(String mId, String mTagName) {
        this.mId = mId;
        this.mTagName = mTagName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTagName() {
        return mTagName;
    }

    public void setTagName(String mTagName) {
        this.mTagName = mTagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TagInterestsModel)) return false;
        TagInterestsModel that = (TagInterestsModel) o;
        return mId.equals(that.mId) &&
                mTagName.equals(that.mTagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mId, mTagName);
    }
}
