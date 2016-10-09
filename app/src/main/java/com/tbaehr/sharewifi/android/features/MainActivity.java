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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.features.googlelogin.AbstractLoginActivity;
import com.tbaehr.sharewifi.android.features.networkList.NetworkListFragment;
import com.tbaehr.sharewifi.android.features.settings.SettingsFragment;
import com.tbaehr.sharewifi.android.utils.ImageViewHelper;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.tbaehr.sharewifi.android.utils.ImageViewHelper.PROFILE_IMAGE;

public class MainActivity extends AbstractLoginActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static boolean isAccountPageOpened = false;

    private static boolean isGoogleAccountRegistered = false;

    /**
     * The layout that contains toolbar, sideBar and  main activity
     */
    @Bind(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;

    /**
     * SupportActionBar as toolbar on the top.
     * Contains the toggleButton to open the navigation bar
     */
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    /**
     * Side bar at the left
     */
    @Bind(R.id.activity_main_nav_view)
    NavigationView sideBar;

    private FragmentHolder fragmentHolder;

    private ImageView menuSwitchArrow;

    private void initActionBar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        sideBar.setNavigationItemSelectedListener(this);
    }

    private void initSideMenuSwitch() {
        final View sideBarHeader = sideBar.getHeaderView(0);
        FrameLayout menuSwitchLayout = (FrameLayout) sideBarHeader.findViewById(R.id.navbar_header_manage_account_frame_layout);
        menuSwitchArrow = (ImageView) sideBarHeader.findViewById(R.id.navbar_header_manage_account_image_view);
        menuSwitchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSideMenuContent();
            }
        });
    }

    /**
     * Switch between default menu and account settings menu
     */
    private void switchSideMenuContent() {
        isAccountPageOpened = !isAccountPageOpened;
        refreshSideMenu();
    }

    private void refreshSideMenu() {
        final TextView menuNameTextView = (TextView) findViewById(R.id.navbar_header_textUserName);
        final TextView menuMailTextView = (TextView) findViewById(R.id.navbar_header_textLoginCredential);;
        final ImageView menuAccountIcon = (ImageView) findViewById(R.id.navbar_header_imageView);;

        if (isGoogleAccountRegistered) {
            menuNameTextView.setText(googleAccount.getAccountName());
            menuMailTextView.setText(googleAccount.getEmail());
            Uri profilePhotoUri = googleAccount.getPhotoUri();
            Bitmap profilePhoto = ImageViewHelper.loadImageFromStorage(getApplicationContext(), PROFILE_IMAGE);
            if (profilePhoto != null) {
                menuAccountIcon.setImageBitmap(profilePhoto);
            } else {
                ImageViewHelper.loadImageInBackground(profilePhotoUri, new ImageViewHelper.OnDownloadListener() {
                    @Override
                    public void onDownloadStarted() {
                        // ;
                    }

                    @Override
                    public void onDownloadCompleted(Bitmap bitmap) {
                        int size = menuAccountIcon.getWidth();
                        bitmap = ImageViewHelper.scale(bitmap, size, size);
                        bitmap = ImageViewHelper.cropCircular(bitmap);
                        ImageViewHelper.saveToInternalStorage(getApplicationContext(), PROFILE_IMAGE, bitmap);
                        menuAccountIcon.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onDownloadError() {
                        Log.e(MainActivity.this.getClass().getSimpleName(), "Profile photo download failed");
                    }
                });
            }
        } else {
            menuNameTextView.setText(R.string.nav_noaccount_title);
            menuMailTextView.setText(R.string.nav_noaccount_subtitle);
            menuAccountIcon.setImageResource(android.R.drawable.sym_def_app_icon);
        }

        // refresh all menu items
        sideBar.getMenu().clear();
        if (isAccountPageOpened) {
            menuSwitchArrow.setImageResource(android.R.drawable.arrow_up_float);
            sideBar.inflateMenu(R.menu.account_settings);
            sideBar.getMenu().removeItem(R.id.nav_account_settings);
            if (isGoogleAccountRegistered) {
                sideBar.getMenu().removeItem(R.id.nav_add_account);
            } else {
                sideBar.getMenu().removeItem(R.id.nav_logout);
            }
        } else {
            menuSwitchArrow.setImageResource(android.R.drawable.arrow_down_float);
            sideBar.inflateMenu(R.menu.activity_wifilist_drawer);
        }
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);

        initActionBar();
        initSideMenuSwitch();

        requestLocationPermission();

        fragmentHolder = new FragmentHolder(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        googleAccount.signInSilent();
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
                Snackbar.make(drawerLayout,
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
            fragmentHolder.showNetworkTabHostFragment();
        } else if (id == R.id.nav_contacts) {
            tempTesterMessage = "Sorry, die Kontaktverwaltung gibt's noch nicht. Dauert noch.";
        } else if (id == R.id.nav_settings) {
            fragmentHolder.showSettingsFragment();
        } else if (id == R.id.nav_help) {
            fragmentHolder.showHelpInfoFragment();
        } else if (id == R.id.nav_add_account) {
            googleAccount.signIn();
        } else if (id == R.id.nav_logout) {
            googleAccount.signOut();
        } else if (id == R.id.nav_account_settings) {
            tempTesterMessage = "Sorry, die Account-Verwaltung ist noch nicht fertig. Dauert noch...";
        }

        if (!tempTesterMessage.isEmpty()) {
            Snackbar.make(drawerLayout,
                    tempTesterMessage,
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.activity_main_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        isGoogleAccountRegistered = false;
        refreshSideMenu();
        Log.e(MainActivity.this.getClass().getSimpleName(), "onConnectionFailed(" + connectionResult + ")");
    }

    @Override
    public void onFirstSignIn(String accountName, String email, Uri photoUri) {
        onSignIn(accountName, email, photoUri);
        Snackbar.make(drawerLayout, getString(R.string.snackbarmessage_loggedInAs, accountName), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string._undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googleAccount.signOut();
                    }
                }).show();
    }

    @Override
    public void onSignIn(String accountName, String email, Uri photoUri) {
        isGoogleAccountRegistered = true;
        refreshSideMenu();
    }

    @Override
    public void onSignOut() {
        isGoogleAccountRegistered = false;
        refreshSideMenu();
        Snackbar.make(drawerLayout, getString(R.string.snackbarmessage_loggedOut), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string._undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googleAccount.signIn();
                    }
                }).show();
    }

    @Override
    public void onRevokeAccount() {
        isGoogleAccountRegistered = false;
        refreshSideMenu();
        Log.v(MainActivity.this.getClass().getSimpleName(), "onRevokeAccount()");
    }
}

class FragmentHolder {

    private static final String TAG_NET = "net";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_HELP = "help";

    private NetworkListFragment mNetworkTabHostFragment;
    private SettingsFragment settingsFragment;
    private HelpInfoFragment helpInfoFragment;
    private AppCompatActivity context;

    public FragmentHolder(AppCompatActivity context) {
        this.context = context;

        mNetworkTabHostFragment = (NetworkListFragment) this.context.getSupportFragmentManager().findFragmentByTag(TAG_NET);
        if (mNetworkTabHostFragment == null) {
            mNetworkTabHostFragment = new NetworkListFragment();
            this.context.getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, mNetworkTabHostFragment, TAG_NET).commit();
        }

        settingsFragment = (SettingsFragment) this.context.getFragmentManager().findFragmentByTag(TAG_SETTINGS);
        if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
            this.context.getFragmentManager().beginTransaction().add(R.id.item_detail_container, settingsFragment, TAG_SETTINGS).commit();
        }

        helpInfoFragment = (HelpInfoFragment) this.context.getSupportFragmentManager().findFragmentByTag(TAG_HELP);
        if (helpInfoFragment == null) {
            helpInfoFragment = new HelpInfoFragment();
            this.context.getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, helpInfoFragment, TAG_HELP).commit();
        }

        showNetworkTabHostFragment();
    }

    void showNetworkTabHostFragment() {
        context.getFragmentManager().beginTransaction().detach(settingsFragment).commit();
        context.getSupportFragmentManager().beginTransaction().detach(helpInfoFragment).commit();
        context.getSupportFragmentManager().beginTransaction().attach(mNetworkTabHostFragment).commit();
    }

    void showSettingsFragment() {
        context.getSupportFragmentManager().beginTransaction().detach(mNetworkTabHostFragment).commit();
        context.getSupportFragmentManager().beginTransaction().detach(helpInfoFragment).commit();
        context.getFragmentManager().beginTransaction().attach(settingsFragment).commit();
    }

    void showHelpInfoFragment() {
        context.getSupportFragmentManager().beginTransaction().detach(mNetworkTabHostFragment).commit();
        context.getFragmentManager().beginTransaction().detach(settingsFragment).commit();
        context.getSupportFragmentManager().beginTransaction().attach(helpInfoFragment).commit();
    }

}