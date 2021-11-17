package com.abt.skillzage.ui.payment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.MyAppplication;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.payment.model.PaymentGatewayModel;
import com.abt.skillzage.ui.profile.model.MyProfile;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ProceedToPay extends AppCompatActivity implements PaymentResultListener {

    ProgressBar progressBar;
    WebView webView;
    androidx.appcompat.widget.AppCompatTextView errorText;
    private Handler mHandler = new Handler();
    private String subscriptionAmount = "0.00";
    private int subscriptionid = 0;
    private String subscriptionname = "";
    private String subscriptionduration = "";
    PaymentGatewayModel gateway;
    JSONObject userprofile = null;
    public String getSubscriptionAmount() {
        return subscriptionAmount;
    }

    public void setSubscriptionAmount(String subscriptionAmount) {
        this.subscriptionAmount = subscriptionAmount;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_to_pay);
        /*
         To ensure faster loading of the Checkout form,
          call this method as early as possible in your checkout flow.
         */
        try{
            userprofile = new JSONObject(getIntent().getStringExtra("userprofile"));
            System.out.println(" User model ::::   "+ userprofile);
        }catch(Exception e){
            e.printStackTrace();
        }
        subscriptionduration = getIntent().getStringExtra("subscriptionduration");
        subscriptionname = getIntent().getStringExtra("subscriptionname");
        subscriptionid = getIntent().getIntExtra("subscriptionid",0);
        gateway = (PaymentGatewayModel) getIntent().getSerializableExtra("subscriptionmodel");
        System.out.println(" Razor pay model ::::   "+ gateway);
       //  Checkout ch = new Checkout();
       // ch.setKeyID(getResources().getString(R.string.razorpaykey));
        Checkout.preload(getApplicationContext());

        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("customerName", SharedPrefUtil.with(ProceedToPay.this).getString("name",""));
        mapParams.put("email", SharedPrefUtil.with(ProceedToPay.this).getString("userid",""));
        mapParams.put("phoneNumber", SharedPrefUtil.with(ProceedToPay.this).getString("phoneNumber",""));

        startPayment();

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", SharedPrefUtil.with(ProceedToPay.this).getString("name",""));
            options.put("description", "Purchaseing Subscription Plan");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", String.valueOf((Double.valueOf(gateway.getAmount()) * 100)));

            try{
                System.out.println("payment amount ::  "+ String.valueOf((Double.valueOf(gateway.getAmount()) * 100)));
            }catch (Exception e){
                e.printStackTrace();
            }

            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPrefUtil.with(ProceedToPay.this).getString("userid",""));
            preFill.put("contact", SharedPrefUtil.with(ProceedToPay.this).getString("phoneNumber",""));
            options.put("prefill", preFill);
            co.open(ProceedToPay.this, options);

        } catch (Exception e) {
            Toast.makeText(ProceedToPay.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "  Payment Successful:  " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            Log.e("SUCCESS", "  Payment Successful:  " + razorpayPaymentID);
            saveSubscriptionInAccount(razorpayPaymentID);
        } catch (Exception e) {
            Log.e("PROCEED-TO-PAY-TAG", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
            Log.e("ERROR", "Payment failed: " + code + " " + response);
            savePaymentInformationOfUser("FAIL","");
        } catch (Exception e) {
            Log.e("", "Exception in onPaymentError", e);
        }
    }


    public void saveSubscriptionInAccount(String razorpayPaymentID) {

        String url = getResources().getString(R.string.baseurl3)+"api/user-activities";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            String currentdate = sdf.format(new Date());

            String userid = SharedPrefUtil.with(this).getString("userid","");
            params.put("activityDate",currentdate+"Z");
            params.put("courseId",0);
            params.put("subscriptionId",String.valueOf(subscriptionid));
            params.put("userId",userid);
            params.put("instituteId",0);
            // Means no user actuvuty present for this course id.
            params.put("enrollmentStatus","");
            params.put("courseCompletionStatus","");
            entity = new StringEntity(params.toString());
            courseEnrollmentCreateServiceImp(client,url,entity,progressDialog, razorpayPaymentID);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void savePaymentInformationOfUser(String paystatus , String refno ) {

        String url = getResources().getString(R.string.baseurl3)+"api/payments";
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            String currentdate = sdf.format(new Date());

            String userid = SharedPrefUtil.with(this).getString("userid","");
            params.put("amount", (int)Double.parseDouble(gateway.getAmount()));
            params.put("cartUsedDate",currentdate+"Z");
            params.put("courseId",0);
            params.put("paymentDate",currentdate+"Z");
            params.put("paymentResponse","");
            params.put("paymentStatus",paystatus);
            params.put("subscriptionId",String.valueOf(subscriptionid));
            params.put("userId",userid);
            params.put("referenceNumber",refno);
            entity = new StringEntity(params.toString());
            client.post(this, url, entity , "application/json" ,new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Error  Response from server recved is   " + responseString);
                    progressDialog.dismiss();
                    Toast.makeText(ProceedToPay.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    System.out.println(" Success Response from server recved for create is    " + responseString);
                    try {
                        progressDialog.dismiss();
                        JSONObject mainobj = new JSONObject(responseString);
                        if(mainobj.has("id") && paystatus.equalsIgnoreCase("SUCCESS")){
                            Toast.makeText(ProceedToPay.this, "success", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(ProceedToPay.this, "fail. Please try again", Toast.LENGTH_LONG).show();
                        }
                        startActivity(new Intent(ProceedToPay.this , MainActivity.class));
                        finish();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void courseEnrollmentCreateServiceImp(AsyncHttpClient client , String url , StringEntity entity , final ProgressDialog progressDialog,String razorpayPaymentID){

        client.post(this, url, entity , "application/json" ,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(ProceedToPay.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved for create is    " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONObject mainobj = new JSONObject(responseString);
                    if(mainobj.has("id")){
                        Toast.makeText(ProceedToPay.this, "Subscription done successfully", Toast.LENGTH_LONG).show();
                        JSONObject myprofileattribute = userprofile;
                        Calendar c_end = Calendar.getInstance();
                        try {
                            c_end.setTime(new Date());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        c_end.add(Calendar.DATE, Integer.parseInt(subscriptionduration));  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        long updatedSubScriptionEndDate = c_end.getTime().getTime();

                        Calendar c_strt = Calendar.getInstance();
                        try {
                            c_strt.setTime(new Date());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        long updatedSubScriptionStartDate = c_strt.getTime().getTime();
                        System.out.println(" User model ::::   "+ myprofileattribute);
                        doUpdateProfile(SharedPrefUtil.with(ProceedToPay.this).getString("skillzagid", "") , subscriptionname ,String.valueOf(subscriptionid)  ,
                                String.valueOf(updatedSubScriptionStartDate)   , String.valueOf(updatedSubScriptionEndDate) , razorpayPaymentID);

                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void doUpdateProfile(String id , String subscriptionType , String subscriptionid , String subscriptionStartDate , String subscriptionEndDate , String razorpayPaymentID) {

        String regurl = getResources().getString(R.string.baseurl) + "skillzag/auth/users/update/" + id;
        System.out.println("  Profile url ::  " + regurl);
        final ProgressDialog progressDialog = new ProgressDialog(ProceedToPay.this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject subparams = new JSONObject();
        JSONObject params = new JSONObject();

        JSONObject myprofileattribute = userprofile;
        StringEntity entity = null;
        try {
            System.out.println("  proceed to pay  profile model  :::::::::::     "+myprofileattribute.toString());
            if (myprofileattribute.has("subscriptionType") && myprofileattribute.getJSONArray("subscriptionType").length() > 0) {
                myprofileattribute.getJSONArray("subscriptionType").put(0, subscriptionType);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(subscriptionType);
                myprofileattribute.put("subscriptionType", subarr);
            }

            if (myprofileattribute.has("subscriptionStartDate") && myprofileattribute.getJSONArray("subscriptionStartDate").length() > 0) {
                myprofileattribute.getJSONArray("subscriptionStartDate").put(0, subscriptionStartDate);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(subscriptionStartDate);
                myprofileattribute.put("subscriptionStartDate", subarr);
            }

            if (myprofileattribute.has("subscriptionEndDate") && myprofileattribute.getJSONArray("subscriptionEndDate").length() > 0) {
                myprofileattribute.getJSONArray("subscriptionEndDate").put(0, subscriptionEndDate);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(subscriptionEndDate);
                myprofileattribute.put("subscriptionEndDate", subarr);
            }
            params.put("attributes", myprofileattribute);

            entity = new StringEntity(params.toString());
            System.out.println(" REqqqqqqqqqqqqqqqqqqq  " + params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(ProceedToPay.this, regurl, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(ProceedToPay.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    Toast.makeText(ProceedToPay.this, "  Profile Updated Successfully ", Toast.LENGTH_LONG).show();
                    savePaymentInformationOfUser("SUCCESS", razorpayPaymentID );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}