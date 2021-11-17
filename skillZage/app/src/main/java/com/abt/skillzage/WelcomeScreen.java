package com.abt.skillzage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.abt.skillzage.socialmedia.Facebook;
import com.abt.skillzage.socialmedia.Google;
import com.abt.skillzage.socialmedia.LinkedIn;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WelcomeScreen extends AppCompatActivity {

    private MaterialButton btnFacebook;
    private MaterialButton btnLinkedIn;
    private MaterialButton btnEmail;

    //Facebook
    private Facebook facebook;

    //Google
    Google google;

    //LinkedIn
    LinkedIn linkedIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        btnFacebook =  findViewById(R.id.btnFacebook);
        btnLinkedIn =  findViewById(R.id.btnLinkedIn);
        btnEmail =  findViewById(R.id.btnEmail);

        /**
         * Social media login
         */
//        getFBKeyHash();
//        initFbLogin();
//        initGoogleLogin();
//        initLinkedInLogin();


        if(SharedPrefUtil.with(WelcomeScreen.this).getString("sessionmob","") != null
        && !SharedPrefUtil.with(WelcomeScreen.this).getString("sessionmob","").equalsIgnoreCase("null")
                && !SharedPrefUtil.with(WelcomeScreen.this).getString("sessionmob","").isEmpty()){
            if(SharedPrefUtil.with(WelcomeScreen.this).getString("sessionmob","").equalsIgnoreCase("1")){
                startActivity(new Intent(WelcomeScreen.this, MainActivity.class));
                finish();
            }else{
                initLayout();
            }
        }else{
            initLayout();
        }
    }

    private void getFBKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.abt.skillzage",                  //Insert your own package name.
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    private void initFbLogin() {
        facebook = new Facebook(this);
        btnFacebook.setOnClickListener((v) -> {
            facebook.login();
        });
    }

    private void initGoogleLogin() {
        google = new Google(this);
        btnEmail.setOnClickListener(v -> {
            google.login();
        });
    }

    private void initLinkedInLogin() {
        linkedIn = new LinkedIn();
        linkedIn.init(this);
        linkedIn.setLinkedInCredentials(getString(R.string.linkedin_client_id), getString(R.string.linkedin_client_secret), "state");
        linkedIn.setRedirect_URL("url");
        btnLinkedIn.setOnClickListener(v -> {
            try {
                linkedIn.login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void initLayout(){
        btnFacebook = findViewById(R.id.btnFacebook);
        btnLinkedIn = findViewById(R.id.btnLinkedIn);
        btnEmail = findViewById(R.id.btnEmail);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //throw new RuntimeException("Test Crash");
                  startActivity(new Intent(WelcomeScreen.this, EmailAuthenticationScreen.class));
            }
        });
    }
}