package com.abt.skillzage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class OnBoarding extends AppCompatActivity {

    private MaterialButton btnStartLearning;
    private com.google.android.material.textfield.TextInputEditText editFirstName , editLastName , editEmail , editInstitution , editInstitutionID ,
            editphone ,  editInstitutionId , editPhone , editAddress1 , editAddress2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        initLayout();
    }

    private void initLayout() {
        btnStartLearning = findViewById(R.id.btnStartLearning);
        editFirstName = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editEmail = findViewById(R.id.editEmail);
        editInstitution = findViewById(R.id.editInstitution);
        editInstitutionID = findViewById(R.id.editInstitutionID);
        editphone = findViewById(R.id.editphone);
        editAddress1 = findViewById(R.id.editAddress1);
        editAddress2 = findViewById(R.id.editAddress2);

        btnStartLearning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editFirstName.getText().toString().isEmpty()){
                    Toast.makeText(OnBoarding.this, " Firstname shouldn't be empty ", Toast.LENGTH_LONG).show();
                    return;
                }
                if(editLastName.getText().toString().isEmpty()){
                    Toast.makeText(OnBoarding.this, " Lastname shouldn't be empty ", Toast.LENGTH_LONG).show();
                    return;
                }
                if(editEmail.getText().toString().isEmpty()){
                    Toast.makeText(OnBoarding.this, " Email shouldn't be empty ", Toast.LENGTH_LONG).show();
                    return;
                }
                doSignupPost(editFirstName.getText().toString() , editLastName.getText().toString() , editEmail.getText().toString() ,
                        editInstitution.getText().toString() ,  editInstitutionID.getText().toString() ,  editphone.getText().toString() ,
                        editAddress1.getText().toString() , editAddress2.getText().toString());

            }
        });
    }



    public void doSignupPost(String firstname , String lastname , String email , String institutionname , String institutionid , String phoneno , String address1 , String address2 ) {

        String regurl = getResources().getString(R.string.baseurl)+"skillzag/auth/users/create";
        final ProgressDialog progressDialog = new ProgressDialog(OnBoarding.this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        RequestParams reqparam = new RequestParams();

        StringEntity entity = null;
        try {
            params.put("email",email);
            params.put("firstname",firstname);
            params.put("lastname",lastname);
            params.put("phoneNumber",phoneno);
            params.put("password","123456");
            params.put("role","b2c");
            params.put("institutionName","NA");
            params.put("institutionID","0");
            params.put("address1",address1);
            params.put("address2",address2);

            params.put("status","ACTIVE");
            params.put("statusCode",1);
            params.put("subscriptionEndDate",0);
            params.put("subscriptionStartDate",0);
            params.put("subscriptionType","NA");
            params.put("validFrom",0);
            params.put("validTo",0);
            entity = new StringEntity(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        reqparam.add("file" , "");
        reqparam.add("request" , params.toString());

        client.post(OnBoarding.this , regurl, reqparam , new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(OnBoarding.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    startActivity(new Intent(OnBoarding.this, EmailAuthenticationScreen.class));
                    finish();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}