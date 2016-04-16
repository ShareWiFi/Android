package com.tbaehr.sharewifi.android.features.infoTour;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.tbaehr.sharewifi.android.R;
import com.tbaehr.sharewifi.android.features.MainActivity;

/**
 * Created by tbaehr on 05.04.16.
 */
public class InfoTour extends AppIntro2 {

    private FrameLayout mBackgroundFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBackgroundFrame = (FrameLayout) findViewById(R.id.background);
        mBackgroundFrame.setBackgroundColor(getResources().getColor(R.color.blue));

        String title = getString(R.string.infotour_slide1_hallo);
        String description = getString(R.string.infotour_slide1_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.share_wifi_logo, android.R.color.transparent));

        title = getString(R.string.infotour_slide2_title);
        description = getString(R.string.infotour_slide2_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.info_shared_with_me, android.R.color.transparent));

        title = getString(R.string.infotour_slide3_title);
        description = getString(R.string.infotour_slide3_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.info_shared_by_me, android.R.color.transparent));

        addSlide(new InfoShareWithFragment());

        title = getString(R.string.infotour_slide5_title);
        description = getString(R.string.infotour_slide5_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.under_construction_400, android.R.color.transparent));

        addSlide(new InfoLoginFragment());
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Intent nextActivity = new Intent(this, MainActivity.class);
        startActivity(nextActivity);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        new AlertDialog.Builder(this)
                .setTitle(R.string.infotour_skip_title)
                .setMessage(R.string.infotour_skip_message)
                .setPositiveButton(R.string._continue, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent nextActivity = new Intent(InfoTour.this, MainActivity.class);
                        startActivity(nextActivity);
                    }
                })
                .setNegativeButton(R.string._no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                }).show();
    }

}