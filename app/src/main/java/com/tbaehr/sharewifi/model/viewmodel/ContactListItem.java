package com.tbaehr.sharewifi.model.viewmodel;

/**
 * Created by tbaehr on 28.03.16.
 */
public class ContactListItem {

    final String mName;

    final boolean mSingleContactItem;

    final boolean mSectionHeadline;

    public ContactListItem(String name, boolean singleContactItem) {
        mName = name;
        mSingleContactItem = singleContactItem;
        mSectionHeadline = false;
    }

    public ContactListItem(String name) {
        mName = name;
        mSectionHeadline = true;
        mSingleContactItem = false;
    }

    public boolean isSingleContactItem() {
        return mSingleContactItem;
    }

    public boolean isSectionHeadline() {
        return mSectionHeadline;
    }

    public String getName() {
        return mName;
    }

}
