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

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tbaehr on 21.02.16.
 * Last modified by tbaehr on 22.04.16.
 */
public class WiFiListItemAdapter extends ArrayAdapter<WiFiNetwork> {

    private List<WiFiNetwork> listOfWiFiNetworks;

    public WiFiListItemAdapter(Context context, List<WiFiNetwork> wiFiNetworks) {
        super(context, R.layout.networklist_item, wiFiNetworks);
        this.listOfWiFiNetworks = wiFiNetworks;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view != null) {
            viewHolder = (ViewHolder) view.getTag();
        } else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.networklist_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }

        final WiFiNetwork viewModelItem = listOfWiFiNetworks.get(position);
        viewHolder.ssidTextView.setText(viewModelItem.getSsid());
        viewHolder.statusTextView.setText(viewModelItem.getDescription());
        viewHolder.networkStatusImageView.setImageResource(viewModelItem.getSignalStrengthIcon());
        if (viewModelItem.isUnknownNetwork()) {
            viewHolder.shareStatusImageView.setVisibility(View.GONE);
        } else {
            viewHolder.shareStatusImageView.setImageResource(viewModelItem.getShareStatus().getDrawable());
        }
        viewHolder.encryptedImageView.setVisibility(viewModelItem.isEncrypted() ? View.VISIBLE : View.GONE);
        viewHolder.qualityImageView.setVisibility(viewModelItem.isQualityBad() ? View.VISIBLE : View.GONE);

        view.setContentDescription(String.format("network %d: %s: %s", position + 1, viewModelItem.getSsid(), viewModelItem.getDescription()));

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    static class ViewHolder {
        @Bind(R.id.network_item_title) TextView ssidTextView;
        @Bind(R.id.network_item_description) TextView statusTextView;
        @Bind(R.id.network_item_networkstatus) ImageView networkStatusImageView;
        @Bind(R.id.network_item_sharestatus) ImageView shareStatusImageView;
        @Bind(R.id.network_item_encrypted) ImageView encryptedImageView;
        @Bind(R.id.network_item_quality) ImageView qualityImageView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}