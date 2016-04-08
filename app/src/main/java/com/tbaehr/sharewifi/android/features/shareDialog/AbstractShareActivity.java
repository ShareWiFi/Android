package com.tbaehr.sharewifi.android.features.shareDialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tbaehr.sharewifi.android.model.viewmodel.WiFiNetwork;

public class AbstractShareActivity extends AppCompatActivity {

    public static final String EXTRA_NETWORKNAME = "network_name";

    public static final String EXTRA_SELECTED_SHARE_OPTION = "selected_share_option";

    protected String mNetworkName;

    protected WiFiNetwork.ShareStatus mShareStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNetworkName = getIntent().getStringExtra(EXTRA_NETWORKNAME);

        mShareStatus = WiFiNetwork.ShareStatus.fromInteger(getIntent().getIntExtra(EXTRA_SELECTED_SHARE_OPTION, -1));

        setAsFullScreenActivity();
    }


    /**
     * Set as fullscreen activity with translucent status
     */
    private void setAsFullScreenActivity() {
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}
