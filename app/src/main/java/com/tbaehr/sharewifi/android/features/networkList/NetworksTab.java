package com.tbaehr.sharewifi.android.features.networkList;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
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

import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.features.shareDialog.ShareInfoActivity;
import com.tbaehr.sharewifi.android.model.viewmodel.WiFiNetwork;

import java.util.List;

/**
 * Created by tbaehr on 05.03.16.
 */
public class NetworksTab extends Fragment {

    public final static String BUNDLE_EXTRA_IN_RANGE = "display networks in range";

    private ListView wiFiNetList;

    private boolean inRangeNetworks;

    private BroadcastReceiver wifiStateChangedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate this tab
        View view = inflater.inflate(R.layout.networklist_tab_content, container, false);

        // Retrieve UI model
        inRangeNetworks = getArguments().getBoolean(BUNDLE_EXTRA_IN_RANGE, true);
        List<WiFiNetwork> networks = getNetworks();

        // Setup list view
        final ArrayAdapter<WiFiNetwork> adapter = new WiFiListItemAdapter(getActivity(), networks);
        wiFiNetList = (ListView) view.findViewById(R.id.networklist_listView);
        wiFiNetList.setAdapter(adapter);
        wiFiNetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WiFiNetwork network = adapter.getItem(position);
                WiFiNetwork.ShareStatus shareStatus = network.getShareStatus();
                if (network.isUnknownNetwork()) {
                    // TODO: Implement Connect Dialog
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.network_dialog_title)
                            .setMessage(getString(R.string.network_dialog_message, network.getSsid()))
                            .setPositiveButton(R.string._continue, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent openConnectDialog = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
                            startActivity(openConnectDialog);
                        }
                    })
                    .setNegativeButton(R.string._no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
                } else {
                    openShareInfoDialog(position, view);
                }
            }

            private void openShareInfoDialog(int position, View view) {
                final View shareIconView = view.findViewById(R.id.network_item_sharestatus);
                final ActivityOptions options = setupAcitivityOptions(shareIconView);
                final Intent openShareViewIntent = new Intent(getActivity(), ShareInfoActivity.class);
                openShareViewIntent.putExtra(ShareInfoActivity.EXTRA_NETWORKNAME, adapter.getItem(position).getSsid());
                openShareViewIntent.putExtra(ShareInfoActivity.EXTRA_SELECTED_SHARE_OPTION, adapter.getItem(position).getShareStatus().toInteger());
                if (options != null) {
                    startActivity(openShareViewIntent, options.toBundle());
                } else {
                    startActivity(openShareViewIntent);
                }
            }

            private ActivityOptions setupAcitivityOptions(View view) {
                int startX = view.getWidth()/2;
                int startY = view.getHeight()/2;
                ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                        view, startX, startY, view.getWidth(), view.getHeight());
                //@TargetApi(21)
                //ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "sharedImage");

                return options;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        wifiStateChangedListener = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ArrayAdapter<WiFiNetwork> adapter = (ArrayAdapter<WiFiNetwork>) wiFiNetList.getAdapter();
                adapter.clear();
                adapter.addAll(getNetworks());
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getActivity().registerReceiver(wifiStateChangedListener, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(wifiStateChangedListener);
    }

    private List<WiFiNetwork> getNetworks() {
        List<WiFiNetwork> networks;
        if (inRangeNetworks) {
            networks = (new WiFiListGrabber()).getNetworksInRange();
        } else {
            networks = (new WiFiListGrabber()).getNetworksOutOfRange();
        }

        return networks;
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
