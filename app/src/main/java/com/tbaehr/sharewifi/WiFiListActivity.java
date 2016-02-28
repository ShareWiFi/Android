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

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class WiFiListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean accountPageOpened = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifilist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // set OnClickListener for the manage account icon
        final View header = navigationView.getHeaderView(0);
        FrameLayout manageAccountFrameLayout = (FrameLayout) header.findViewById(R.id.navbar_header_manage_account_frame_layout);
        manageAccountFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView downArrow = (ImageView) header.findViewById(R.id.navbar_header_manage_account_image_view);
                navigationView.getMenu().clear();
                if (!accountPageOpened) {
                    downArrow.setImageResource(android.R.drawable.arrow_up_float);
                    navigationView.inflateMenu(R.menu.account_settings);
                    // TODO: Change between logout and add_account
                    navigationView.getMenu().removeItem(R.id.nav_logout);
                } else {
                    downArrow.setImageResource(android.R.drawable.arrow_down_float);
                    navigationView.inflateMenu(R.menu.activity_wifilist_drawer);
                }
                accountPageOpened = !accountPageOpened;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        Snackbar.make(
                findViewById(R.id.activity_main_drawer_layout),
                "Sorry, hier sind noch keine echten Netzwerke zu sehen. Kommt in Kürze.",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.wi_fi_list, menu);
        return true;
    }*/

    /*@Override
    public boolean onNavigateUp() {
        Snackbar.make(findViewById(R.id.activity_main_drawer_layout),
                "Replace with your own action",
                Snackbar.LENGTH_LONG).setAction("Action", null).show();

        NavigationView navigationView = (NavigationView) findViewById(R.id.activity_main_nav_view);

        FrameLayout manageAccountFrameLayout = (FrameLayout) navigationView.getHeaderView(R.id.navbar_header_manage_account_frame_layout);
        manageAccountFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.activity_main_drawer_layout),
                        "Replace with your own action",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return super.onNavigateUp();
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO: Must be removed! Only for alpha testers.
        String tempTesterMessage = "";

        if (id == R.id.nav_allnetworks) {

        } else if (id == R.id.nav_contacts) {
            tempTesterMessage = "Sorry, die Kontaktverwaltung gibt's noch nicht. Dauert noch.";
        } else if (id == R.id.nav_invite) {
            tempTesterMessage = "Sorry, Du kannst leider noch niemanden einladen. Dauert noch.";
        } else if (id == R.id.nav_groups) {
            tempTesterMessage = "Sorry, die Gruppenverwaltung gibt's nocht nicht. Dauert noch.";
        } else if (id == R.id.nav_settings) {
            tempTesterMessage = "Sorry, die Einstellungs-Seite gibt's noch nicht. Dauert noch.";
        } else if (id == R.id.nav_help) {
            tempTesterMessage = "Sorry, die Hilfe-Seite gibt's noch nicht. Kommt demnächst.";
        } else if (id == R.id.nav_add_account) {
            tempTesterMessage = "Sorry, die Account-Verwaltung ist noch nicht fertig. Dauert noch...";
        } else if (id == R.id.nav_logout) {
            tempTesterMessage = "Sorry, Du kannst Dich noch nicht einloggen oder registrieren. Dauert noch...";
        } else if (id == R.id.nav_account_settings) {
            tempTesterMessage = "Sorry, die Account-Verwaltung ist noch nicht fertig. Dauert noch...";
        }

        Snackbar.make(findViewById(R.id.activity_main_drawer_layout),
                tempTesterMessage,
                Snackbar.LENGTH_LONG).setAction("Action", null).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
