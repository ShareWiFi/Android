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

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class ShareActivity extends AppCompatActivity {

    public static final String EXTRA_NETWORKNAME = "network_name";
    public static final String EXTRA_OPENED_OVER_NOTIFICATION = "opened_over_notification";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set as fullscreen activity with translucent status
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.sharedialog_activity);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.share_dialog_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        // Make back arrow white or disable it if opened over notification
        boolean openedOverNotification = getIntent().getBooleanExtra(EXTRA_OPENED_OVER_NOTIFICATION, false);
        final Drawable backArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        if (openedOverNotification) {
            backArrow.setVisible(false, false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        } else {
            actionBar.setDisplayHomeAsUpEnabled(true);
            backArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
        }

        // Set title of this activity including the network name
        String title = getString(R.string.sharedialog_title);
        Intent intent = getIntent();
        final String networkName = intent.getStringExtra(EXTRA_NETWORKNAME);
        title = title.replace("§", networkName);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.share_dialog_collapsing_toolbar);
        collapsingToolbar.setTitle(title);

        // Delete notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);

        // Set click listeners for cards
        CardView    cardShareGlobal       = (CardView) findViewById(R.id.share_dialog_card_globalshare);
        CardView    cardShareGroups       = (CardView) findViewById(R.id.share_dialog_card_groupshare);
        CardView    cardShareDevices      = (CardView) findViewById(R.id.share_dialog_card_deviceshare);
        CardView    cardShareOff          = (CardView) findViewById(R.id.share_dialog_card_shareoff);
        FrameLayout disable_notifications = (FrameLayout) findViewById(R.id.share_dialog_never_show_again);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(
                        findViewById(R.id.share_dialog_coordinator_layout),
                        "Sorry, noch nicht implementiert.",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        };
        cardShareGlobal.setOnClickListener(onClickListener);
        cardShareGroups.setOnClickListener(onClickListener);
        cardShareDevices.setOnClickListener(onClickListener);
        cardShareOff.setOnClickListener(onClickListener);
        disable_notifications.setOnClickListener(onClickListener);

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
