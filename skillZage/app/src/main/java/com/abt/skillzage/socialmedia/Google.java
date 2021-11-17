package com.abt.skillzage.socialmedia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.abt.skillzage.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class Google{
    private Context mContext;
    private GoogleResponseListener listener;
    private GoogleSignInClient mGoogleApiClient;

    public interface GoogleResponseListener {
        void onGoogleResponseListener(JSONObject response, boolean error);
    }

    public Google(Context mContext) {
        this.mContext = mContext;
        listener = (GoogleResponseListener) mContext;

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = GoogleSignIn.getClient(mContext, gso);
//        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
//                .enableAutoManage(((FragmentActivity) mContext), this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                .build();
    }

    public void login() {

        if (mGoogleApiClient != null) {
            Intent signInIntent = mGoogleApiClient.getSignInIntent();
//            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            if (mContext instanceof Activity) {
                ((Activity) mContext).startActivityForResult(signInIntent, 101);
            } else {
                generateError(mContext.getString(R.string.context_error));
            }
        } else {
            generateError(mContext.getString(R.string.general_error));
        }
    }

    public void logout() {

        if (mGoogleApiClient != null) {
            mGoogleApiClient.signOut()
                    .addOnCompleteListener((Activity) mContext, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("message", mContext.getString(R.string.google_logout_success));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            listener.onGoogleResponseListener(jsonObject, false);
                        }
                    });

//            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
//                @Override
//                public void onResult(@NonNull Status status) {
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("message", mContext.getString(R.string.google_logout_success));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    listener.onGoogleResponseListener(jsonObject, false);
//                }
//            });
        } else {
            generateError(mContext.getString(R.string.general_error));
        }
    }

    public JSONObject activityResult(int requestCode, int resultCode, Intent data) {
        JSONObject jsonObject = null;
        // The Task returned from this call is always completed, no need to attach
        // a listener.
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        jsonObject =  handleSignInResult(task);

        return jsonObject;
//        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
//        handleSignInResult(result);
    }

    private JSONObject handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        JSONObject jsonObject = null;
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            jsonObject =   handleSignInResult(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GOOGLE LOGIN", "signInResult:failed code=" + e.getStatusCode());
        }
        return jsonObject;
    }

    private JSONObject handleSignInResult(GoogleSignInAccount acct) {
        Log.d("GoogleSignIn", "handleSignInResult:" + acct.getDisplayName());
      //  if (result.) {
            // Signed in successfully, show authenticated UI.
      //      GoogleSignInAccount acct = result.getSignInAccount();
            Toast.makeText(mContext, "" + acct.isExpired(), Toast.LENGTH_SHORT).show();
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", acct.getId());
                jsonObject.put("displayName", acct.getDisplayName());
                jsonObject.put("email", acct.getEmail());
                jsonObject.put("photoUrl", acct.getPhotoUrl());
                jsonObject.put("familyName", acct.getFamilyName());
                jsonObject.put("givenName", acct.getGivenName());
                jsonObject.put("idToken", acct.getIdToken());
                System.out.println("  Account details  ::  "+jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listener.onGoogleResponseListener(jsonObject, false);
            return jsonObject;
       // }
    }

    private void generateError(String msg) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("message", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listener.onGoogleResponseListener(jsonObject, true);
    }
}