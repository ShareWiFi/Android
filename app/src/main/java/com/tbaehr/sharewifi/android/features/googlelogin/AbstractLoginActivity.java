package com.tbaehr.sharewifi.android.features.googlelogin;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by timo.baehr@gmail.com on 03.10.2016.
 */
public abstract class AbstractLoginActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        new GoogleAccount(this);
    }

    public abstract void onConnectionFailed(@NonNull ConnectionResult connectionResult);

    public abstract void onSignIn(String accountName, String email, Uri photoUri);

    public abstract void onSignOut();

    public abstract void onRevokeAccount();

}
