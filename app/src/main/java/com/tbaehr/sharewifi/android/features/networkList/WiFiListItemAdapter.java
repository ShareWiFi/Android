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
package com.tbaehr.sharewifi.android.features.networkList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.model.viewmodel.WiFiNetwork;

import java.util.List;

/**
 * Created by tbaehr on 21.02.16.
 */
public class WiFiListItemAdapter extends ArrayAdapter<WiFiNetwork> {

    //private HashMap<WiFiNetwork, Integer> mIdMap = new HashMap<>();
    private List<WiFiNetwork> listOfWiFiNetworks;

    public WiFiListItemAdapter(Context context, List<WiFiNetwork> wiFiNetworks) {
        super(context, R.layout.networklist_item, wiFiNetworks);
        /*for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }*/
        this.listOfWiFiNetworks = wiFiNetworks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get inflater which is able to create the list item
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int itemLayoutId = R.layout.networklist_item;
        View listItem = inflater.inflate(itemLayoutId, parent, false);

        // Find view objects
        TextView ssidTextView =
                (TextView) listItem.findViewById(R.id.network_item_title);
        TextView statusTextView =
                (TextView) listItem.findViewById(R.id.network_item_description);
        ImageView networkStatusImageView =
                (ImageView) listItem.findViewById(R.id.network_item_networkstatus);
        ImageView shareStatusImageView =
                (ImageView) listItem.findViewById(R.id.network_item_sharestatus);
        ImageView encryptedImageView =
                (ImageView) listItem.findViewById(R.id.network_item_encrypted);
        ImageView qualityImageView =
                (ImageView) listItem.findViewById(R.id.network_item_quality);

        // Get the view model item
        final WiFiNetwork item = listOfWiFiNetworks.get(position);

        // Set values from view model item
        ssidTextView.setText(item.getSsid());
        statusTextView.setText(item.getDescription());
        networkStatusImageView.setImageResource(item.getSignalStrengthIcon());
        if (item.isUnknownNetwork()) {
            shareStatusImageView.setVisibility(View.GONE);
        } else {
            shareStatusImageView.setImageResource(item.getShareStatus().getDrawable());
        }
        encryptedImageView.setVisibility(item.isEncrypted() ? View.VISIBLE : View.GONE);
        if (item.isQualityBad()) {
            qualityImageView.setVisibility(View.VISIBLE);
        }

        // return the list item
        return listItem;
    }

    @Override
    public long getItemId(int position) {
        WiFiNetwork item = getItem(position);
        return position; //mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}