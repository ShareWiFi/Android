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

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.features.networkList.NetworkListFragment;
import com.tbaehr.sharewifi.android.features.settings.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean isAccountPageOpened = false;

    private FragmentHolder mFragmentHolder;

    /**
     * The layout that contains toolbar, sideBar and  main activity
     */
    @Bind(R.id.activity_main_drawer_layout) DrawerLayout drawerLayout;

    /**
     * SupportActionBar as toolbar on the top.
     * Contains the toggleButton to open the navigation bar
     */
    @Bind(R.id.toolbar) Toolbar toolbar;

    /**
     * Side bar at the left
     */
    @Bind(R.id.activity_main_nav_view) NavigationView sideBar;

    private ImageView menuSwitchArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        sideBar.setNavigationItemSelectedListener(this);

        // set OnContactItemClickListener for the manage account icon
        final View sideBarHeader = sideBar.getHeaderView(0);
        FrameLayout menuSwitchLayout = (FrameLayout) sideBarHeader.findViewById(R.id.navbar_header_manage_account_frame_layout);
        menuSwitchArrow = (ImageView) sideBarHeader.findViewById(R.id.navbar_header_manage_account_image_view);
        menuSwitchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSideMenuSwitchClick();
            }
        });

        requestLocationPermission();

        // Fragment configuration
        mFragmentHolder = new FragmentHolder(this);
    }

    private void onSideMenuSwitchClick() {
        sideBar.getMenu().clear();
        if (!isAccountPageOpened) {
            menuSwitchArrow.setImageResource(android.R.drawable.arrow_up_float);
            sideBar.inflateMenu(R.menu.account_settings);
            // TODO: Change between logout and add_account
            sideBar.getMenu().removeItem(R.id.nav_logout);
        } else {
            menuSwitchArrow.setImageResource(android.R.drawable.arrow_down_float);
            sideBar.inflateMenu(R.menu.activity_wifilist_drawer);
        }
        isAccountPageOpened = !isAccountPageOpened;
    }

    private void requestLocationPermission() {
        int permissionCheck = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            permissionCheck = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 42);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
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
        fab.setOnClickListener(new View.OnContactItemClickListener() {
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

        NavigationView sideBar = (NavigationView) findViewById(R.id.activity_main_nav_view);

        FrameLayout manageAccountFrameLayout = (FrameLayout) sideBar.getHeaderView(R.id.navbar_header_manage_account_frame_layout);
        manageAccountFrameLayout.setOnClickListener(new View.OnContactItemClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(R.id.activity_main_drawer_layout),
                        "Replace with your own action",
                        Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        return super.onNavigateUp();
    }*/

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        // TODO: Must be removed! Only for alpha testers.
        String tempTesterMessage = "";

        if (id == R.id.nav_allnetworks) {
            mFragmentHolder.showNetworkTabHostFragment();
        } else if (id == R.id.nav_contacts) {
            tempTesterMessage = "Sorry, die Kontaktverwaltung gibt's noch nicht. Dauert noch.";
        } else if (id == R.id.nav_settings) {
            mFragmentHolder.showSettingsFragment();
        } else if (id == R.id.nav_help) {
            mFragmentHolder.showHelpInfoFragment();
        } else if (id == R.id.nav_add_account) {
            tempTesterMessage = "Sorry, die Account-Verwaltung ist noch nicht fertig. Dauert noch...";
        } else if (id == R.id.nav_logout) {
            tempTesterMessage = "Sorry, Du kannst Dich noch nicht einloggen oder registrieren. Dauert noch...";
        } else if (id == R.id.nav_account_settings) {
            tempTesterMessage = "Sorry, die Account-Verwaltung ist noch nicht fertig. Dauert noch...";
        }

        if (!tempTesterMessage.isEmpty()) {
            Snackbar.make(findViewById(R.id.activity_main_drawer_layout),
                    tempTesterMessage,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

class FragmentHolder {

    private NetworkListFragment mNetworkTabHostFragment;

    private SettingsFragment mSettingsFragment;

    private HelpInfoFragment mHelpInfoFragment;

    private static final String TAG_NET = "net";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_HELP = "help";

    private AppCompatActivity mContext;

    public FragmentHolder(AppCompatActivity context) {
        mContext = context;

        mNetworkTabHostFragment = (NetworkListFragment) mContext.getSupportFragmentManager().findFragmentByTag(TAG_NET);
        if (mNetworkTabHostFragment == null) {
            mNetworkTabHostFragment = new NetworkListFragment();
            mContext.getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, mNetworkTabHostFragment, TAG_NET).commit();
        }

        mSettingsFragment = (SettingsFragment) mContext.getFragmentManager().findFragmentByTag(TAG_SETTINGS);
        if (mSettingsFragment == null) {
            mSettingsFragment = new SettingsFragment();
            mContext.getFragmentManager().beginTransaction().add(R.id.item_detail_container, mSettingsFragment, TAG_SETTINGS).commit();
        }

        mHelpInfoFragment = (HelpInfoFragment) mContext.getSupportFragmentManager().findFragmentByTag(TAG_HELP);
        if (mHelpInfoFragment == null) {
            mHelpInfoFragment = new HelpInfoFragment();
            mContext.getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, mHelpInfoFragment, TAG_HELP).commit();
        }

        showNetworkTabHostFragment();
    }

    void showNetworkTabHostFragment() {
        mContext.getFragmentManager().beginTransaction().detach(mSettingsFragment).commit();
        mContext.getSupportFragmentManager().beginTransaction().detach(mHelpInfoFragment).commit();
        mContext.getSupportFragmentManager().beginTransaction().attach(mNetworkTabHostFragment).commit();
    }

    void showSettingsFragment() {
        mContext.getSupportFragmentManager().beginTransaction().detach(mNetworkTabHostFragment).commit();
        mContext.getSupportFragmentManager().beginTransaction().detach(mHelpInfoFragment).commit();
        mContext.getFragmentManager().beginTransaction().attach(mSettingsFragment).commit();
    }

    void showHelpInfoFragment() {
        mContext.getSupportFragmentManager().beginTransaction().detach(mNetworkTabHostFragment).commit();
        mContext.getFragmentManager().beginTransaction().detach(mSettingsFragment).commit();
        mContext.getSupportFragmentManager().beginTransaction().attach(mHelpInfoFragment).commit();
    }

}