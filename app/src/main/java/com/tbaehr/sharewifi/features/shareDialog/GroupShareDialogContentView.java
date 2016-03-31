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
import android.graphics.Point;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.features.groupManagement.ContactsGrabber;
import com.tbaehr.sharewifi.model.viewmodel.ContactListItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tbaehr on 26.03.16.
 */
public class GroupShareDialogContentView extends LinearLayout {

    private Context mContext;

    private List<OnContactItemClickListener> mOnContactItemClickListeners = new ArrayList<>();

    private RecyclerView mContactLists;

    public GroupShareDialogContentView(Context context) {
        super(context);

        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.sharedialog_group_share_dialog, this, true);

        initializeContentView();
    }

    // TODO: Retrieve previous share status (if already specified by the user)
    // TODO: Provide share status specified inside this view


    private void initializeContentView() {
        // prepare the items
        final ArrayList<ContactListItem> items = new ArrayList<>();

        String all = mContext.getString(R.string.sharedialog_option_groupshare_option_all_contacts);
        ContactListItem allContacts = new ContactListItem(all, false);
        items.add(allContacts);
        items.addAll(ContactsGrabber.getInstance().getContactListNames());

        ContactListItem sectionHeadline = new ContactListItem(mContext.getString(R.string.sharedialog_option_groupshare_title_single_contacts));
        items.add(sectionHeadline);
        items.addAll(ContactsGrabber.getInstance().getContactNames());

        // prepare recycler view
        mContactLists = (RecyclerView) findViewById(R.id.group_share_dialog_recycler_view);
        mContactLists.setHasFixedSize(true);

        final int spans = getSpans();
        GridLayoutManager manager = new GridLayoutManager(getContext(), spans);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                ContactListItem item = items.get(position);
                if (item.isSingleContactItem() || item.isSectionHeadline()) {
                    return spans;
                } else
                    return 1;
            }
        });
        mContactLists.setLayoutManager(manager);

        // set the adapter to the recycler view
        ContactItemsAdapter adpater = new ContactItemsAdapter(items, this);
        mContactLists.setAdapter(adpater);
    }

    private int getSpans() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        // TODO: Don't use pixel here.
        return width / 360;
    }

    public void setOnClickListener(OnContactItemClickListener onContactItemClickListener) {
        mOnContactItemClickListeners.add(onContactItemClickListener);
    }

    public void unregisterOnClickListener(OnContactItemClickListener onContactItemClickListener) {
        mOnContactItemClickListeners.remove(onContactItemClickListener);
    }

    void notify(ContactListItem clickedItem) {
        for (OnContactItemClickListener listener : mOnContactItemClickListeners) {
            listener.onClick(clickedItem);
        }
    }
}

abstract class OnContactItemClickListener {

    public OnContactItemClickListener() {}

    public abstract void onClick(ContactListItem clickedItem);

}


class ContactItemsAdapter extends RecyclerView.Adapter<ContactItemsAdapter.ViewHolder> {

    private static final int VIEW_TYPE_CONTACT_GROUP = 0;

    private static final int VIEW_TYPE_SINGLE_CONTACT = 1;

    private static final int VIEW_TYPE_SECTION_HEADLINE = 2;

    private ArrayList<ContactListItem> mDataset;

    private GroupShareDialogContentView mDialogView;

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtHeader;
        public ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.contact_item_headline);
            imageView = (ImageView) v.findViewById(R.id.contact_item_image_view);
        }
    }

    public ContactItemsAdapter(ArrayList<ContactListItem> dataset, GroupShareDialogContentView dialogView) {
        mDataset = dataset;
        mDialogView = dialogView;
    }

    public void add(int position, ContactListItem item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(ContactListItem item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemViewType(int position) {
        ContactListItem item = mDataset.get(position);
        return item.isSingleContactItem() ?
                VIEW_TYPE_SINGLE_CONTACT :
                item.isSectionHeadline() ?
                        VIEW_TYPE_SECTION_HEADLINE :
                        VIEW_TYPE_CONTACT_GROUP;
    }

    @Override
    public ContactItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent
                .getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if (viewType == VIEW_TYPE_SINGLE_CONTACT) {
            view = inflater.inflate(R.layout.item_single_contact_view, parent, false);
        } else if (viewType == VIEW_TYPE_CONTACT_GROUP) {
            view = inflater.inflate(R.layout.item_contact_group_view, parent, false);
        } else {
            view = inflater.inflate(R.layout.item_section_headline, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ContactListItem item = mDataset.get(position);
        holder.txtHeader.setText(item.getName());
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactItemsAdapter.this.mDialogView.notify(item);
            }
        });

        ImageView imageView = holder.imageView;
        if (imageView != null) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContactItemsAdapter.this.mDialogView.notify(item);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}