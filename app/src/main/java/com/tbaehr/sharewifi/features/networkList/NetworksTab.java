package com.tbaehr.sharewifi.features.networkList;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.ShareActivity;
import com.tbaehr.sharewifi.model.viewmodel.WiFiNetwork;

import java.util.List;

/**
 * Created by tbaehr on 05.03.16.
 */
public class NetworksTab extends Fragment {

    public final static String BUNDLE_EXTRA_IN_RANGE = "display networks in range";

    private ListView wiFiNetList;

    private boolean inRangeNetworks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.networklist_tab_content, container, false);

        // Retrieve UI model
        List<WiFiNetwork> networks;
        inRangeNetworks = getArguments().getBoolean(BUNDLE_EXTRA_IN_RANGE, true);
        if (inRangeNetworks) {
            networks = (new WiFiListGrabber()).getNetworksInRange();
        } else {
            networks = (new WiFiListGrabber()).getNetworksOutOfRange();
        }

        // Setup view
        final ArrayAdapter<WiFiNetwork> adapter = new WiFiListItemAdapter(getActivity(), networks);

        wiFiNetList = (ListView) view.findViewById(R.id.networklist_listView);
        wiFiNetList.setAdapter(adapter);

        wiFiNetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!inRangeNetworks) {
                    openShareDialog(position);
                } else {
                    if (adapter.getItem(position).isUnknownNetwork()) {
                        // TODO: Open Connect Dialog
                        Snackbar.make(view,
                                "Hierüber kannst Du in Kürze Dich mit einem WLAN-Netz verbinden.",
                                Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    } else {
                        openShareDialog(position);
                    }
                }
            }

            private void openShareDialog(int position) {
                Intent openShareViewIntent = new Intent(getActivity(), ShareActivity.class);
                openShareViewIntent.putExtra(ShareActivity.EXTRA_NETWORKNAME, adapter.getItem(position).getSsid());
                startActivity(openShareViewIntent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!inRangeNetworks) {
            Snackbar.make(view.findViewById(R.id.networklist_listView),
                    "Hier siehst Du aktuell nur Dummy-Einträge. Noch kann man keine WLAN-Netze teilen.",
                    Snackbar.LENGTH_SHORT).setAction("Action", null).show();
        }
    }
}
