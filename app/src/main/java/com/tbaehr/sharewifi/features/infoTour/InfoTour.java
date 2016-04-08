package com.tbaehr.sharewifi.features.infoTour;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.tbaehr.sharewifi.R;
import com.tbaehr.sharewifi.features.MainActivity;

/**
 * Created by tbaehr on 05.04.16.
 */
public class InfoTour extends AppIntro2 {

    private FrameLayout mFrameLayout;

    @Override
    public void init(Bundle savedInstanceState) {
        mFrameLayout = (FrameLayout) findViewById(R.id.background);
        mFrameLayout.setBackgroundColor(getResources().getColor(R.color.blue));

        // Add your slide's fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
        //addSlide(first_fragment);
        //addSlide(second_fragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        String title = getString(R.string.infotour_slide1_hallo);
        String description = getString(R.string.infotour_slide1_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.share_wifi_logo, android.R.color.transparent));

        title = getString(R.string.infotour_slide2_title);
        description = getString(R.string.infotour_slide2_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.ic_menu_downloaded_white, android.R.color.transparent));

        title = getString(R.string.infotour_slide3_title);
        description = getString(R.string.infotour_slide3_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.ic_menu_shared_white, android.R.color.transparent));

        title = getString(R.string.infotour_slide4_title);
        description = getString(R.string.infotour_slide4_text);
        addSlide(AppIntroFragment.newInstance(title, description, R.drawable.under_construction_400, android.R.color.transparent));

        // OPTIONAL METHODS
        // Override bar/separator color.
        //setBarColor(Color.parseColor("#3F51B5"));
        //setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        //setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest.
        //setVibrate(true);
        //setVibrateIntensity(30);
    }

    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        Intent nextActivity = new Intent(this, MainActivity.class);
        startActivity(nextActivity);
    }

    @Override
    public void onSlideChanged() {
        // Do something when the slide changes.
    }

    @Override
    public void onNextPressed() {
        // Do something when users tap on Next button.
    }

}