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
package com.tbaehr.sharewifi.android.features.shareDialog;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.tbaehr.sharewifi.android.R;

public class ShareInfoActivity extends AbstractShareActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sharedialog_info);

        configureToolbar();
        configureFab();
        configureCards();
    }

    /**
     * Set the toolbar. This includes...
     * - the title of the collapsing toolbar
     * - show/hide the back arrow button and click listener
     */
    private void configureToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.share_dialog_info_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Set title of this activity including the network name
        String title = mNetworkName.isEmpty() ? getString(R.string.networkstatus_hidden_wifi) : mNetworkName;

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.share_dialog_info_collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        // Make back arrow white or disable it if opened over notification
        final Drawable backArrow;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            backArrow = getDrawable(R.drawable.abc_ic_ab_back_material);
        } else {
            backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        }
        actionBar.setDisplayHomeAsUpEnabled(true);
        int color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            color = getColor(R.color.white);
        } else {
            color = getResources().getColor(R.color.white);
        }
        backArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(backArrow);
    }

    private void configureFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.share_dialog_info_fab);
        FloatingActionMenu fam = (FloatingActionMenu) findViewById(R.id.share_dialog_info_menu);

        if (mShareStatus.isSharedByMe() || !mShareStatus.isShared()) {
            fam.setVisibility(View.GONE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openShareInfoDialog();
                }
            });
        } else {
            fab.setVisibility(View.GONE);
            FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.share_dialog_info_fab1);
            FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.share_dialog_info_fab2);
            FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.share_dialog_info_fab3);

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(
                            findViewById(R.id.share_dialog_info_coordinator_layout),
                            "Sorry, das kommt erst noch.",
                            Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            };
            fab1.setOnClickListener(listener);
            fab2.setOnClickListener(listener);
            fab3.setOnClickListener(listener);
        }
    }

    /**
     * Configure the CardViews.
     * These represent the UI for share and network info.
     */
    private void configureCards() {
        // set share image
        ImageView shareImage = (ImageView) findViewById(R.id.share_dialog_info_card_drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shareImage.setImageDrawable(getDrawable(mShareStatus.getDrawable()));
        } else {
            shareImage.setImageDrawable(getResources().getDrawable(mShareStatus.getDrawable()));
        }

        // set title
        TextView shareHeadline = (TextView) findViewById(R.id.share_dialog_info_card_title);
        String shareTitle;
        if (!mShareStatus.isShared()) {
            shareHeadline.setText(R.string.sharedialog_info_card_share_title_not_shared);
        } else {
            if (mShareStatus.isSharedWithEveryone()) {
                shareTitle = getString(R.string.sharedialog_info_card_share_title_everyone);
            } else if (mShareStatus.isSharedWithinGroups()) {
                // TODO: Implementation: Distinguish between all contacts and some
                shareTitle = getString(R.string.sharedialog_info_card_share_title_selected_contacts);
                if (mShareStatus.isSharedByMe()) {
                    //shareTitle = getString(R.string.sharedialog_info_card_share_title_all_contacts);
                }
            } else if (mShareStatus.isSharedWithMyDevices()) {
                shareTitle = getString(R.string.sharedialog_info_card_share_title_my_devices);
            } else {
                shareTitle = getString(R.string.sharedialog_info_card_share_title_not_shared);
            }
            shareHeadline.setText(getString(R.string.sharedialog_info_card_share_title, shareTitle));
        }

        // set subtitle (line 1)
        TextView shareLine1 = (TextView) findViewById(R.id.share_dialog_info_card_subtitle_line1);
        if (mShareStatus.isShared()) {
            String sharer;
            if (mShareStatus.isSharedByMe()) {
                sharer = getString(R.string.sharedialog_info_card_share_subtitle_line1_by_me);
            } else {
                // TODO: Implementation: Insert the person's name who shared this network
                sharer = "Max Mustermann";
            }
            shareLine1.setText(getString(R.string.sharedialog_info_card_share_subtitle_line1_by, sharer));
        } else {
            shareLine1.setVisibility(View.GONE);
        }

        // set subtitle (line 2)
        TextView shareLine2 = (TextView) findViewById(R.id.share_dialog_info_card_subtitle_line2);
        if (mShareStatus.isShared()) {
            String whom;
            if (mShareStatus.isSharedWithMyDevices()) {
                whom = getString(R.string.sharedialog_info_card_share_subtitle_line2_whom_my_devices);
            } else if (mShareStatus.isSharedWithinGroups() && mShareStatus.isSharedByMe()){
                // TODO: Implementation: List all group names
                whom = "Familie, Freunde und Arbeitskollegen";
            } else if (mShareStatus.isSharedWithEveryone()) {
                whom = getString(R.string.sharedialog_info_card_share_subtitle_line2_whom_all);
            } else {
                whom = getString(R.string.sharedialog_info_card_share_subtitle_line2_whom_contacts);
            }
            shareLine2.setText(getString(R.string.sharedialog_info_card_share_subtitle_line2_whom, whom));
        } else {
            shareLine2.setText(getString(R.string.sharedialog_info_card_share_subtitle_line2_whom_nobody));
        }

        // set signal strength
        TextView signalStrengthTitle = (TextView) findViewById(R.id.share_dialog_card_network_signal_strength_title);
        int perfect = R.string.sharedialog_info_card_network_signal_strength_perfect;
        int good = R.string.sharedialog_info_card_network_signal_strength_good;
        int medium = R.string.sharedialog_info_card_network_signal_strength_medium;
        int poor = R.string.sharedialog_info_card_network_signal_strength_poor;
        TextView signalStrengthDesc = (TextView) findViewById(R.id.share_dialog_card_network_signal_strength_desc);
        // TODO

        // set security
        TextView securityTitle = (TextView) findViewById(R.id.share_dialog_card_network_security_title);
        int highSec = R.string.sharedialog_info_card_network_security_secure;
        int poorSec = R.string.sharedialog_info_card_network_security_poor;
        int noSecur = R.string.sharedialog_info_card_network_security_dangerous;
        TextView securityDesc = (TextView) findViewById(R.id.share_dialog_card_network_security_desc);
        int highSecDesc = R.string.sharedialog_info_card_network_security_secure_wpa2;
        int poorSecDesc = R.string.sharedialog_info_card_network_security_poor_wep;
        int noSecurDesc = R.string.sharedialog_info_card_network_security_dangerous_not_encrypted;
        // TODO

        // set speed
        TextView speedTitle = (TextView) findViewById(R.id.share_dialog_card_network_speed_title);
        TextView speedDesc = (TextView) findViewById(R.id.share_dialog_card_network_speed_desc);
        // TODO
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }

    private void openShareInfoDialog() {
        final Intent openShareViewIntent = new Intent(this, ShareOptionsActivity.class);
        openShareViewIntent.putExtra(ShareInfoActivity.EXTRA_NETWORKNAME, mNetworkName);
        openShareViewIntent.putExtra(ShareInfoActivity.EXTRA_SELECTED_SHARE_OPTION, mShareStatus.toInteger());
        openShareViewIntent.putExtra(ShareOptionsActivity.EXTRA_OPENED_OVER_NOTIFICATION, false);
        startActivity(openShareViewIntent);
    }
}
