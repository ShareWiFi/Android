package com.tbaehr.sharewifi.android.features.infoTour;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbaehr.sharewifi.android.R;

/**
 * Created by tbaehr on 10.04.16.
 */
public class InfoLoginFragment extends Fragment {

    private CharSequence mTitle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitle = getContext().getString(R.string.infotour_slide4_title);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info_login, container, false);
        TextView t = (TextView) v.findViewById(R.id.title);
        t.setText(mTitle);

        return v;
    }

}
