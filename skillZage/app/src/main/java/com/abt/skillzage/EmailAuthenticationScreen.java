package com.abt.skillzage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.abt.skillzage.socialmedia.Facebook;
import com.abt.skillzage.socialmedia.Google;
import com.abt.skillzage.socialmedia.LinkedIn;
import com.abt.skillzage.socialmedia.SocialLoginConstant;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class EmailAuthenticationScreen extends AppCompatActivity implements Facebook.FacebookResponseListener, Google.GoogleResponseListener, LinkedIn.LinkedInResponseListener {

    private MaterialButton btnLogin;
    private com.google.android.material.textfield.TextInputEditText editUsername, editPassword;
    private androidx.appcompat.widget.AppCompatTextView btnRegister;
    private androidx.appcompat.widget.AppCompatCheckBox rememberme, checkTerms;
    private int session_in = 0;
    private AppCompatTextView forgotpassword;

    //Facebook
    private AppCompatImageView fb_btn;
    private Facebook facebook;

    //Google
    Google google;
    private AppCompatImageView buttonGoogle;

    //LinkedIn
    LinkedIn linkedIn;
    private AppCompatImageView btnlinkedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_authentication_screen);

        initLayout();
    }

    private void initLayout() {
        forgotpassword = findViewById(R.id.forgotpassword);
        btnLogin = findViewById(R.id.btnLogin);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        checkTerms = findViewById(R.id.checkTerms);
        buttonGoogle = findViewById(R.id.google_btn);
        fb_btn = findViewById(R.id.fb_btn);
        btnlinkedIn = findViewById(R.id.linkedIn);

        checkTerms.setChecked(true);

        checkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    new AlertDialog.Builder(EmailAuthenticationScreen.this)
                            .setTitle("Terms & Conditions")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("agreed", "true").save();
                                    checkTerms.setChecked(true);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    checkTerms.setChecked(false);
                                }
                            })
                            .setMessage(getResources().getString(R.string.sample_logn_text))
                            .show();
                      }
                }
        });
//        editUsername.setText("testb2carbindtestme@yopmail.com");
//        editPassword.setText("123456");

        editUsername.setText("crriib2buser@yopmail.com");
        editPassword.setText("123456");

        btnRegister = findViewById(R.id.btnRegister);
        rememberme = findViewById(R.id.rememberme);
        rememberme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    session_in = 1;
                } else {
                    session_in = 0;
                }
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editUsername.getText().toString().isEmpty()) {
                    Toast.makeText(EmailAuthenticationScreen.this,
                            "Email field is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (editPassword.getText().toString().isEmpty()) {
                    Toast.makeText(EmailAuthenticationScreen.this,
                            "Password field is required", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!checkTerms.isChecked()) {
                    Toast.makeText(EmailAuthenticationScreen.this,
                            "Please accept terms and conditions", Toast.LENGTH_LONG).show();
                    return;
                }

                doLoginPost(editUsername.getText().toString(), editPassword.getText().toString());
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmailAuthenticationScreen.this, OnBoarding.class));
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailInputForForgotPassword();
            }
        });

        /**
         * Social media login
         */
        getFBKeyHash();
        initFbLogin();
        initGoogleLogin();
        initLinkedInLogin();
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
        fb_btn.setOnClickListener((v) -> {
            facebook.login();
        });
    }

    private void initGoogleLogin() {
        google = new Google(this);
        buttonGoogle.setOnClickListener(v -> {
            google.login();
        });
    }

    private void initLinkedInLogin() {
        linkedIn = new LinkedIn();
        linkedIn.init(this);
        linkedIn.setLinkedInCredentials(getString(R.string.linkedin_client_id), getString(R.string.linkedin_client_secret), "state");
        linkedIn.setRedirect_URL("url");
        btnlinkedIn.setOnClickListener(v -> {
            try {
                linkedIn.login();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void doLoginPost(String username, String password) {

        String url = getResources().getString(R.string.baseurl) + "skillzag/auth/users/login";
        final ProgressDialog progressDialog = new ProgressDialog(EmailAuthenticationScreen.this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            params.put("email", username);
            params.put("password", password);
            entity = new StringEntity(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(EmailAuthenticationScreen.this, url, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                try{
                    JSONObject jsonresponseString = new JSONObject(responseString);
                    if(jsonresponseString.getString("status").equalsIgnoreCase("400")){
                        Toast.makeText(EmailAuthenticationScreen.this, "Wrong credentials.Please validate and try again.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(EmailAuthenticationScreen.this, "Please try again.", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                        Toast.makeText(EmailAuthenticationScreen.this, "Please try again.", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONObject mainaobj = new JSONObject(responseString);
                    if (mainaobj.has("status")) {
                        if (mainaobj.getString("status").equalsIgnoreCase("success")) {
                            if(mainaobj.getString("role").equalsIgnoreCase("b2c") ||
                                    mainaobj.getString("role").equalsIgnoreCase("b2b")){
                                SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("userid", mainaobj.getString("email")).save();
                                SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("role", mainaobj.getString("role")).save();

                                if (session_in == 1) {
                                    SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("token", mainaobj.getString("token")).save();
                                    SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("sessionmob", "1").save();
                                } else {

                                }
                                getUserDetails(mainaobj.getString("email"));
                            }else{
                                Toast.makeText(EmailAuthenticationScreen.this, " You are not authorize. Please try again ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(EmailAuthenticationScreen.this, " Wrong Credentials. Please try again ", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EmailAuthenticationScreen.this, " Wrong Credentials. Please try again ", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserDetails(String username) {

        String url = getResources().getString(R.string.baseurl) + "skillzag/auth/users/by-userid/"+username;
        final ProgressDialog progressDialog = new ProgressDialog(EmailAuthenticationScreen.this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(EmailAuthenticationScreen.this, url,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(EmailAuthenticationScreen.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONArray mainaarr = new JSONArray(responseString);
                    JSONObject mainaobj = new JSONObject();

                    if(mainaarr.length() > 0){
                        mainaobj = mainaarr.getJSONObject(0);

                        if (mainaobj.has("id")) {
                            SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("skillzagid", mainaobj.getString("id")).save();
                            SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("skillid", mainaobj.getString("email")).save();
                                SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("name", mainaobj.getString("firstName") + "  " + mainaobj.getString("lastName") ).save();

                                if(mainaobj.getJSONObject("attributes").has("phoneNumber")){
                                    if(mainaobj.getJSONObject("attributes").getJSONArray("phoneNumber").length() > 0){
                                        SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("phoneNumber", mainaobj.getJSONObject("attributes").getJSONArray("phoneNumber").getString(0) ).save();
                                    }
                                }
                            if(mainaobj.getJSONObject("attributes").has("imagePath")){
                                if(mainaobj.getJSONObject("attributes").getJSONArray("imagePath").length() > 0){
                                    SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("imagepath", mainaobj.getJSONObject("attributes").getJSONArray("imagePath").getString(0) ).save();
                                }
                            }

                            if(mainaobj.getJSONObject("attributes").has("institutionID")){
                                if(mainaobj.getJSONObject("attributes").getJSONArray("institutionID").length() > 0){
                                    SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("institutionID", mainaobj.getJSONObject("attributes").getJSONArray("institutionID").getString(0) ).save();
                                }
                            }

                            if(mainaobj.getJSONObject("attributes").has("subscriptionEndDate")){
                                if(mainaobj.getJSONObject("attributes").getJSONArray("subscriptionEndDate").length() > 0){
                                    SharedPrefUtil.with(EmailAuthenticationScreen.this).addString("subscriptionEndDate", mainaobj.getJSONObject("attributes").getJSONArray("subscriptionEndDate").getString(0) ).save();
                                }
                            }

                            Intent mainintent = new Intent(EmailAuthenticationScreen.this, MainActivity.class);
                            mainintent.putExtra("profile", mainaobj.getJSONObject("attributes").toString());
                            startActivity(mainintent);
                        } else {
                            Toast.makeText(EmailAuthenticationScreen.this, " Wrong Credentials. Please try again ", Toast.LENGTH_SHORT).show();
                        }


                    }else{
                        Toast.makeText(EmailAuthenticationScreen.this, " Data Error. Please try again ", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * Facebook and Google callback
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SocialLoginConstant.GOOGLE_REQUEST_CODE) {
            google.activityResult(requestCode, resultCode, data);
        } else {
            /**
             * Facebook
             */
            facebook.activityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onFacebookResponseListener(JSONObject response, boolean error) {
        Log.d("Response", String.valueOf(response));
    }

    @Override
    public void onGoogleResponseListener(JSONObject response, boolean error) {
        Log.d("Response", String.valueOf(response));
    }

    @Override
    public void onLinkedInResponseListener(JSONObject response, boolean error) {
        Log.d("Response", String.valueOf(response));
    }
    private String email_Text = "";
    public void emailInputForForgotPassword(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your emailid to validate your account");
        final AppCompatEditText input = new AppCompatEditText(this);
        input.setHint("emailid here");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                email_Text = input.getText().toString();
                if(email_Text != null && !email_Text.isEmpty()){
                    doForgotPasswordPost(email_Text , dialog);
                }else{
                    Toast.makeText(EmailAuthenticationScreen.this , "Please enter your email id" ,Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();


    }

    public void doForgotPasswordPost(String emailid,final DialogInterface dialog) {
        String userid = emailid;
        String url = getResources().getString(R.string.baseurl) + "skillzag/auth/users/"+userid+"/reset-password";
        final ProgressDialog progressDialog = new ProgressDialog(EmailAuthenticationScreen.this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post( url,  new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(EmailAuthenticationScreen.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    dialog.cancel();
                    Toast.makeText(EmailAuthenticationScreen.this , "Thank you. Please check your email to verify ", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}