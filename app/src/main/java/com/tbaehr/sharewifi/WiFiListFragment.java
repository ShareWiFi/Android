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
package com.tbaehr.sharewifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tbaehr.sharewifi.model.viewmodel.WiFiNetwork;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class WiFiListFragment extends Fragment {

    private ListView wiFiNetList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_wi_fi_list, container, false);

        // Some test data (must be removed later)
        final WiFiNetwork[] values = new WiFiNetwork[] {
                new WiFiNetwork("guest-wifi", true, WiFiNetwork.SignalStrenght.PERFECT, true, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_BY_ME_WITHIN_GROUPS),
                new WiFiNetwork("NeighborWiFi", true, WiFiNetwork.SignalStrenght.GOOD, false, WiFiNetwork.ShareStatus.UNKNOWN),
                new WiFiNetwork("EatMyShorts", true, WiFiNetwork.SignalStrenght.LOW, false, WiFiNetwork.ShareStatus.UNKNOWN),
                new WiFiNetwork("WiFiAtWork", true, WiFiNetwork.SignalStrenght.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_BY_ME_WITH_MY_DEVICES),
                new WiFiNetwork("FamilyRocks", true, WiFiNetwork.SignalStrenght.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
                new WiFiNetwork("Free-WiFi", false, WiFiNetwork.SignalStrenght.NONE, false, WiFiNetwork.Quality.BAD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
                new WiFiNetwork("Saturn-Kunden", true, WiFiNetwork.SignalStrenght.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
                new WiFiNetwork("TopSecretWiFi", true, WiFiNetwork.SignalStrenght.NONE, false, WiFiNetwork.Quality.GOOD, WiFiNetwork.ShareStatus.NOT_SHARED),
                new WiFiNetwork("Hotel Billich", true, WiFiNetwork.SignalStrenght.NONE, false, WiFiNetwork.Quality.BAD, WiFiNetwork.ShareStatus.SHARED_WITH_ME_WITH_EVERYONE),
        };

        final ArrayList<WiFiNetwork> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]);
        }
        final ArrayAdapter<WiFiNetwork> adapter = new WiFiListItemAdapter(getActivity(), list);

        wiFiNetList = (ListView) view.findViewById(R.id.listView);
        wiFiNetList.setAdapter(adapter);

        wiFiNetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openShareViewIntent = new Intent(getActivity(), ShareActivity.class);
                openShareViewIntent.putExtra(ShareActivity.EXTRA_NETWORKNAME, values[position].getSsid());
                startActivity(openShareViewIntent);
            }
        });

        return view;
    }
}