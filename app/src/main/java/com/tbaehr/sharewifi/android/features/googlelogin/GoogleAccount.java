package com.tbaehr.sharewifi.android.features.googlelogin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

/**
 * Created by timo.baehr@gmail.comm on 27.09.2016.
 */
public class GoogleAccount implements GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;

    private AbstractLoginActivity activity;

    private GoogleSignInOptions googleSignInOptions;

    private GoogleSignInAccount acct;

    public GoogleAccount(AbstractLoginActivity activity) {
        this.activity = activity;

        configureSignInOptions();
        buildGoogleApiClient();
    }

    /**
     * Configure sign-in to request the
     * - user's ID (included in DEFAULT_SIGN_IN)
     * - basic profile (included in DEFAULT_SIGN_IN)
     * - email address
     */
    private void configureSignInOptions() {
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    /**
     * Build a GoogleApiClient with access to GoogleSignIn.API and the options
     * specified by googleSignInOptions.
     */
    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
    }

    public void signIn() {
        Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient).setResultCallback(
                new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                }
        );
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        activity.onSignOut();
                    }
                });
    }

    /**
     * Forgot the Google account.
     * Revoke access to the account.
     */
    public void revokeAccount() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        activity.onRevokeAccount();
                    }
                });
    }

    public String getAccountName() {
        return acct == null ? null : acct.getDisplayName();
    }

    public Uri getPhotoUri() {
        return acct == null ? null : acct.getPhotoUrl();
    }

    public String getEmail() {
        return acct == null ? null : acct.getEmail();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(this.getClass().getName(), "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            acct = result.getSignInAccount();
            activity.onSignIn(getAccountName(), getEmail(), getPhotoUri());
        } else {
            acct = null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        activity.onConnectionFailed(connectionResult);
    }
}
