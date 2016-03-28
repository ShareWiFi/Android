package com.tbaehr.sharewifi.features.groupManagement;

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

    public String[] getContactListNames() {
        // TODO: Implementation
        return new String[] {"Familie", "Freunde", "Arbeitskollegen"};
    }

    public String[] getContactNames() {
        // TODO: Implementation
        return new String[] {"Lisa Lustig", "Max Mustermann", "Norbert Normalbürger", "Olav Olsen"};
    }

}
