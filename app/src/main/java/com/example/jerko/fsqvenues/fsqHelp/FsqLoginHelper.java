package com.example.jerko.fsqvenues.fsqHelp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.jerko.fsqvenues.MainActivity;
import com.foursquare.android.nativeoauth.FoursquareCancelException;
import com.foursquare.android.nativeoauth.FoursquareDenyException;
import com.foursquare.android.nativeoauth.FoursquareInvalidRequestException;
import com.foursquare.android.nativeoauth.FoursquareOAuth;
import com.foursquare.android.nativeoauth.FoursquareOAuthException;
import com.foursquare.android.nativeoauth.FoursquareUnsupportedVersionException;
import com.foursquare.android.nativeoauth.model.AccessTokenResponse;
import com.foursquare.android.nativeoauth.model.AuthCodeResponse;

/**
 * Created by Jerko on 6.4.2017..
 */

public class FsqLoginHelper {
    public static final String CLIENT_ID = "GCLLN0NGTTYS2XZLG340YKZNDS3WGGAMODD0DESYUF1GZ4UG";
    public static final String CLIENT_SECRET = "T0QZ4E5FIEWKY2NERC4HLAMX1KCQE3NKJSWWX0E5I1KRR5GX";
    private static final int REQUEST_CODE_FSQ_CONNECT = 200;
    private static final int REQUEST_CODE_FSQ_TOKEN_EXCHANGE = 201;

    private Activity activity;

    public FsqLoginHelper(Activity activity){
        this.activity = activity;
    }

    public void onCompleteConnect(int resultCode, Intent data) {
        AuthCodeResponse codeResponse = FoursquareOAuth.getAuthCodeFromResult(resultCode, data);
        Exception exception = codeResponse.getException();

        if (exception == null) {
            // Success.
            String code = codeResponse.getCode();
            performTokenExchange(code);

        } else {
            if (exception instanceof FoursquareCancelException) {
                // Cancel.
                toastMessage(activity, "Canceled");

            } else if (exception instanceof FoursquareDenyException) {
                // Deny.
                toastMessage(activity, "Denied");

            } else if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = exception.getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(activity, errorMessage + " [" + errorCode + "]");

            } else if (exception instanceof FoursquareUnsupportedVersionException) {
                // Unsupported Fourquare app version on the device.
                toastError(activity, exception);

            } else if (exception instanceof FoursquareInvalidRequestException) {
                // Invalid request.
                toastError(activity, exception);

            } else {
                // Error.
                toastError(activity, exception);
            }
        }
    }

    public void onCompleteTokenExchange(int resultCode, Intent data) {
        AccessTokenResponse tokenResponse = FoursquareOAuth.getTokenFromResult(resultCode, data);
        Exception exception = tokenResponse.getException();

        if (exception == null) {
            String accessToken = tokenResponse.getAccessToken();
            // Success.
            //toastMessage(this, "Access token: " + mAccessToken);

            // Persist the token for later use. In this example, we save
            // it to shared prefs.
            ExampleTokenStore.get().setToken(accessToken);

            ((MainActivity)activity).fetchData("950");

            // Refresh UI.
            ((MainActivity)activity).ensureUi();

        } else {
            if (exception instanceof FoursquareOAuthException) {
                // OAuth error.
                String errorMessage = ((FoursquareOAuthException) exception).getMessage();
                String errorCode = ((FoursquareOAuthException) exception).getErrorCode();
                toastMessage(activity, errorMessage + " [" + errorCode + "]");

            } else {
                // Other exception type.
                toastError(activity, exception);
            }
        }
    }

    /**
     * Exchange a code for an OAuth Token. Note that we do not recommend you
     * do this in your app, rather do the exchange on your server. Added here
     * for demo purposes.
     *
     * @param code
     *          The auth code returned from the native auth flow.
     */
    private void performTokenExchange(String code) {
        Intent intent = FoursquareOAuth.getTokenExchangeIntent(activity, CLIENT_ID, CLIENT_SECRET, code);
        activity.startActivityForResult(intent, REQUEST_CODE_FSQ_TOKEN_EXCHANGE);
    }

    public static void toastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void toastError(Context context, Throwable t) {
        Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
    }

}
