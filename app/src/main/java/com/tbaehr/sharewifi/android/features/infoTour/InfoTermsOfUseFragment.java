package com.tbaehr.sharewifi.android.features.infoTour;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.utils.IOnLinkClickListener;
import com.tbaehr.sharewifi.android.utils.TextViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by tbaehr on 10.04.16.
 */
public class InfoTermsOfUseFragment extends Fragment implements IOnLinkClickListener {

    @Bind(R.id.accept) TextView acceptTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_agb, container, false);
        ButterKnife.bind(this, view);
        TextViewHelper.attachOnLinkClickListener(acceptTextView, this);
        return view;
    }

    @Override
    public void onClick(View view, String url) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.agb_terms_of_use)
                .setMessage(R.string.agb)
                .setPositiveButton(R.string._close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

}