package com.tbaehr.sharewifi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class WiFiListFragment extends Fragment {

    private ListView wiFiNetList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_wi_fi_list, container, false);

        String[] values = new String[] { "Wi-Fi network ", "Wi-Fi network ",
                "Wi-Fi network ", "Wi-Fi network ", "Wi-Fi network ",
                "Wi-Fi network ", "Wi-Fi network ", "Wi-Fi network ",
                "Wi-Fi network ", "Wi-Fi network ", "Wi-Fi network ",
                "Wi-Fi network ", "Wi-Fi network ", "Wi-Fi network ",
                "Wi-Fi network ", "Wi-Fi network ", "Wi-Fi network "};

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            list.add(values[i]+(i+1));
        }
        final ArrayAdapter<String> adapter = new StableArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);

        wiFiNetList = (ListView) view.findViewById(R.id.listView);
        wiFiNetList.setAdapter(adapter);

        return view;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}