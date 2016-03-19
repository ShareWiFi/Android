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
package com.tbaehr.sharewifi.features.shareDialog;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.features.notificationOnConnect.NotificationBuilder;

public class ShareOptionsActivity extends AbstractShareActivity {

    public static final String EXTRA_OPENED_OVER_NOTIFICATION = "opened_over_notification";

    private CardView[] cards = new CardView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sharedialog_options_activity);

        configureToolbar();
        NotificationBuilder.getInstance().hideShareDialog();
        configureCards();
    }

    /**
     * Set the toolbar. This includes...
     * - the title of the collapsing toolbar
     * - show/hide the back arrow button and click listener
     * - show/hide the close button and click listener
     */
    private void configureToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.share_dialog_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        ImageView closeButton = (ImageView) findViewById(R.id.share_dialog_close_button);

        // Set title of this activity including the network name
        final String networkName = getIntent().getStringExtra(EXTRA_NETWORKNAME);
        String title = getString(R.string.sharedialog_title, networkName);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.share_dialog_collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        // Make back arrow white or disable it if opened over notification
        boolean openedOverNotification = getIntent().getBooleanExtra(EXTRA_OPENED_OVER_NOTIFICATION, false);
        final Drawable backArrow;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            backArrow = getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        } else {
            backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        }
        if (openedOverNotification) {
            backArrow.setVisible(false, false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            closeButton.setVisibility(View.VISIBLE);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: Not implemented yet
                    finish();
                }
            });
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            closeButton.setVisibility(View.GONE);
            int color;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                color = getColor(R.color.colorWhite);
            } else {
                color = getResources().getColor(R.color.colorWhite);
            }
            backArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }
    }

    /**
     * Configure the CardViews.
     * These represent the UI for sharing/not sharing a network.
     * This method also initializes the corresponding click listeners.
     */
    private void configureCards() {
        cards[0] = (CardView) findViewById(R.id.share_dialog_card_globalshare);
        cards[1] = (CardView) findViewById(R.id.share_dialog_card_groupshare);
        cards[2] = (CardView) findViewById(R.id.share_dialog_card_deviceshare);
        cards[3] = (CardView) findViewById(R.id.share_dialog_card_shareoff);
        final FrameLayout disable_notifications = (FrameLayout) findViewById(R.id.share_dialog_never_show_again);

        View.OnClickListener globalShareClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                globalShare();
                setSelectedCard(0);
            }
        };
        cards[0].setOnClickListener(globalShareClickListener);

        View.OnClickListener groupShareClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupShare();
                setSelectedCard(1);
            }
        };
        cards[1].setOnClickListener(groupShareClickListener);

        View.OnClickListener deviceShareClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceShare();
                setSelectedCard(2);
            }
        };
        cards[2].setOnClickListener(deviceShareClickListener);

        View.OnClickListener noShareClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noShare();
                setSelectedCard(3);
            }
        };
        cards[3].setOnClickListener(noShareClickListener);

        View.OnClickListener disableNotificationsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableNotifications();
            }
        };
        disable_notifications.setOnClickListener(disableNotificationsClickListener);

        int selectedShareOptionId = getIntent().getIntExtra(EXTRA_SELECTED_SHARE_OPTION, -1);
        setSelectedCard(selectedShareOptionId);
    }

    private void setSelectedCard(int cardId) {
        for (int i = 0; i<cards.length; i++) {
            if (i == cardId) {
                cards[i].setBackgroundColor(getResources().getColor(R.color.backgroundOfSelectedItem));
            } else {
                cards[i].setBackgroundColor(getResources().getColor(R.color.backgroundOfNonSelectedItem));
            }

            if (cardId >= 0 && cardId != 3) {
                TextView title = (TextView) findViewById(R.id.share_dialog_card_shareoff_title);
                title.setText(R.string.sharedialog_option_do_not_longer_share_title);

                TextView desc  = (TextView) findViewById(R.id.share_dialog_card_shareoff_desc);
                desc.setText(R.string.sharedialog_option_do_not_longer_share_desc);
            }
        }
    }

    private void globalShare() {
        Snackbar.make(
                findViewById(R.id.share_dialog_coordinator_layout),
                "Sorry, weltweites Teilen ist noch nicht implementiert.",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void groupShare() {
        Snackbar.make(
                findViewById(R.id.share_dialog_coordinator_layout),
                "Sorry, Teilen mit Gruppen ist noch nicht implementiert.",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void deviceShare() {
        Snackbar.make(
                findViewById(R.id.share_dialog_coordinator_layout),
                "Sorry, Teilen mit Deinen Geräten ist noch nicht implementiert.",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private void noShare() {
        // TODO: Store the share option
        finish();
    }

    private void disableNotifications() {
        Snackbar.make(
                findViewById(R.id.share_dialog_coordinator_layout),
                "Benachrichtigungen können aktuell noch nicht deaktiviert werden.",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return true;
    }
}
