package com.tbaehr.sharewifi.android.features.groupManagement;

import com.tbaehr.sharewifi.android.model.viewmodel.ContactListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbaehr on 27.03.16.
 */
public class ContactsGrabber {

    private static ContactsGrabber mContactsGrabber;

    private ContactsGrabber() {

    }

    public static ContactsGrabber getInstance() {
        if (mContactsGrabber == null) {
            mContactsGrabber = new ContactsGrabber();
        }

        return mContactsGrabber;
    }

    public List<ContactListItem> getContactListNames() {
        // TODO: Implementation
        ArrayList<ContactListItem> items = new ArrayList<>();
        items.add(new ContactListItem("Familie", false));
        items.add(new ContactListItem("Freunde", false));
        items.add(new ContactListItem("Arbeits-\nkollegen", false));
        return items;
    }

    public List<ContactListItem> getContactNames() {
        // TODO: Implementation
        ArrayList<ContactListItem> items = new ArrayList<>();
        items.add(new ContactListItem("Lisa Lustig", true));
        items.add(new ContactListItem("Max Mustermann", true));
        items.add(new ContactListItem("Norbert Normalbürger", true));
        items.add(new ContactListItem("Olav Olsen", true));
        return items;
    }

}
