/**
 * The MIT License (MIT) Copyright (c) 2016 Timo Bähr
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or s
 * ubstantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.tbaehr.sharewifi.features.shareDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.features.groupManagement.ContactsGrabber;

/**
 * Created by tbaehr on 26.03.16.
 */
public class GroupShareDialogContentView extends LinearLayout {

    private Context mContext;

    private ListView mContactLists;

    private ListView mSingleContacts;

    public GroupShareDialogContentView(Context context) {
        super(context);

        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sharedialog_group_share_dialog, this, true);

        initializeContentView();
    }

    // TODO: Retrieve share status (if already specified by the user)
    // TODO: Provide share status specified inside this view


    private void initializeContentView() {
        mContactLists = (ListView) findViewById(R.id.group_share_dialog_contactlist);
        mSingleContacts = (ListView) findViewById(R.id.group_share_dialog_singlecontacts);

        final ArrayAdapter<String> arrayAdapterContactLists = new ArrayAdapter<>(
                mContext,
                R.layout.checked_text_view,
                R.id.item_headline);

        arrayAdapterContactLists.add(mContext.getString(R.string.sharedialog_option_groupshare_option_all_contacts));
        arrayAdapterContactLists.addAll(ContactsGrabber.getInstance().getContactListNames());

        mContactLists.setAdapter(arrayAdapterContactLists);

        final ArrayAdapter<String> arrayAdapterSingleContacts = new ArrayAdapter<>(
                mContext,
                R.layout.checked_text_view,
                R.id.item_headline);

        arrayAdapterSingleContacts.addAll(ContactsGrabber.getInstance().getContactNames());

        mSingleContacts.setAdapter(arrayAdapterSingleContacts);
    }

}