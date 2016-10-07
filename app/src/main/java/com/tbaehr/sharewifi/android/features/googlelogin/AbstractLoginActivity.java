package com.tbaehr.sharewifi.android.features.googlelogin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;

import static com.tbaehr.sharewifi.android.features.googlelogin.GoogleAccount.RC_SIGN_IN;

/**
 * Created by timo.baehr@gmail.com on 03.10.2016.
 */
public abstract class AbstractLoginActivity extends AppCompatActivity {

    protected GoogleAccount googleAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleAccount = new GoogleAccount(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            googleAccount.onActivityResult(data);
        }
    }

    public abstract void onConnectionFailed(@NonNull ConnectionResult connectionResult);

    public abstract void onSignIn(String accountName, String email, Uri photoUri);

    public abstract void onSignOut();

    public abstract void onRevokeAccount();

}
