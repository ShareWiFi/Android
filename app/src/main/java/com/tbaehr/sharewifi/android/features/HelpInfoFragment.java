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
package com.tbaehr.sharewifi.android.features;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tbaehr.sharewifi.android.BuildConfig;
import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.features.infoTour.InfoTour;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tbaehr on 17.05.16.
 */
public class HelpInfoFragment extends Fragment {

    @Bind(R.id.help_about1_text)
    TextView mHelpAbout1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        ButterKnife.bind(this, view);
        setViewItemValues();
        return view;
    }

    private void setViewItemValues() {
        setVersionName(getString(R.string.help_about_version, BuildConfig.VERSION_NAME));
    }

    void setVersionName(String versionName) {
        mHelpAbout1.setText(versionName);
    }

    @OnClick(R.id.help_terms_of_usage)
    void onShowTermsOfUsageClicked() {
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

    @OnClick(R.id.help_feedback1)
    void onPlayStoreLinkClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.tbaehr.sharewifi.android")));
    }

    @OnClick(R.id.help_feedback2)
    void onGooglePlusLinkClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/communities/104257911836487064327")));
    }

    @OnClick(R.id.help_about2)
    void onStartInfoTourClicked() {
        Intent nextActivity = new Intent(getActivity(), InfoTour.class);
        nextActivity.putExtra(InfoTour.KEY_INFOTOUR_RESTARTED, true);
        startActivity(nextActivity);
        getActivity().finish();
    }

    @OnClick(R.id.help_about3)
    void onQuestionsAndAnswersClick() {
        Snackbar.make(this.getView(), "Noch nicht implementiert", Snackbar.LENGTH_SHORT).show();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
