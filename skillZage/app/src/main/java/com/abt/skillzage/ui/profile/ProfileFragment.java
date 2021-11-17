package com.abt.skillzage.ui.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.OnBoarding;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.profile.model.MyProfile;
import com.abt.skillzage.util.Utils;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    com.google.android.material.textfield.TextInputEditText editId , editFirstName , editLastName ,
            editEmail , editPhoneno , editAddress1 , editAddress2 , editSubscriptionEnddate , editInstitutionname;
    com.google.android.material.textfield.TextInputLayout fieldinstName;

    de.hdodenhof.circleimageview.CircleImageView profileimg;
    com.google.android.material.button.MaterialButton btnUpdate;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    com.google.android.material.textfield.TextInputEditText editSubscriptionType;
    protected static final int FILE_PICK = 1010;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editId = view.findViewById(R.id.editId);
        editFirstName = view.findViewById(R.id.editFirstName);
        editLastName = view.findViewById(R.id.editLastName);
        editEmail = view.findViewById(R.id.editEmail);
        editPhoneno = view.findViewById(R.id.editPhoneno);
        editAddress1 = view.findViewById(R.id.editAddress1);
        editAddress2 = view.findViewById(R.id.editAddress2);
        editSubscriptionType =  view.findViewById(R.id.editSubscriptionType);
        editSubscriptionEnddate = view.findViewById(R.id.editSubscriptionEnddate);
        profileimg = view.findViewById(R.id.profileimg);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        editInstitutionname = view.findViewById(R.id.editInstitutionname);
        fieldinstName = view.findViewById(R.id.fieldinstName);

        if(SharedPrefUtil.with(getActivity()).getString("userid","") != null &&
                !SharedPrefUtil.with(getActivity()).getString("userid","").equalsIgnoreCase("null") &&
                !SharedPrefUtil.with(getActivity()).getString("userid","").isEmpty()){

            doGetProfileData(SharedPrefUtil.with(getActivity()).getString("userid",""), false);

        }

        if(SharedPrefUtil.with(getActivity()).getString("role","") == "b2b" ){
            fieldinstName.setVisibility(View.VISIBLE);
        }else{
            fieldinstName.setVisibility(View.GONE);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                 //   doUpdateProfileWithIMage(skillid , editPhoneno.getText().toString() ,editAddress1.getText().toString() , editAddress2.getText().toString() );

                    if (ret != null) {
                        Toast.makeText(getActivity() , "  file path ::  "+new File(ret.getString("uri")).getAbsolutePath(),Toast.LENGTH_LONG).show();
                        if (ret.getString("uri") != null && !ret.getString("uri").isEmpty()) {
                            Toast.makeText(getActivity() , "  3333333333333  ",Toast.LENGTH_LONG).show();

                            doUpdateProfileWithIMage(skillid , editPhoneno.getText().toString() ,editAddress1.getText().toString() , editAddress2.getText().toString() );
                        }else{
                            Toast.makeText(getActivity() , "  222222222222222  ",Toast.LENGTH_LONG).show();

                            doUpdateProfile(skillid , editPhoneno.getText().toString() ,editAddress1.getText().toString() , editAddress2.getText().toString() );
                        }
                    }else{
                        Toast.makeText(getActivity() , "  11111111111111  ",Toast.LENGTH_LONG).show();

                        doUpdateProfile(skillid , editPhoneno.getText().toString() ,editAddress1.getText().toString() , editAddress2.getText().toString() );
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        profileimg.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(intent, FILE_PICK);
        });

        return view;
    }


    public void doGetProfileImage( String filename ) {

        String getuserurl = getResources().getString(R.string.baseurl)+"skillzag/auth/users/download/"+filename;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/octet-stream");
        client.get(getActivity() , getuserurl, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    Bitmap bmp = BitmapFactory.decodeByteArray(responseString, 0, responseString.length);
                    profileimg.setImageBitmap(bmp);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    JSONObject myprofileattribute = null;
    String skillid = "";
    public void doGetProfileData( String userid ,boolean needImage) {

        String getuserurl = getResources().getString(R.string.baseurl)+"skillzag/auth/users/by-userid/"+userid;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        if(needImage){
            getUserProfileImageConsume(client , progressDialog , getuserurl);
        }else{
            getUserProfileConsume(client , progressDialog , getuserurl);
        }

    }

    public void getUserProfileConsume(AsyncHttpClient client , ProgressDialog progressDialog , String getuserurl){

        client.get(getActivity() , getuserurl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONArray mainarr = new JSONArray(responseString);
                    MyProfile profile = new MyProfile();

                    for(int t=0; t < mainarr.length();t++){

                        myprofileattribute = mainarr.getJSONObject(t).getJSONObject("attributes");
                        if(myprofileattribute.has("address1")){
                            profile.setAddress1(myprofileattribute.getJSONArray("address1").getString(0));

                        }else{
                            profile.setAddress1("");
                        }

                        if(myprofileattribute.has("address2")){
                            profile.setAddress2(myprofileattribute.getJSONArray("address2").getString(0));

                        }else{
                            profile.setAddress2("");
                        }

                        profile.setEmail(mainarr.getJSONObject(t).getString("email"));
                        profile.setId(mainarr.getJSONObject(t).getString("id"));
                        profile.setFirstName(mainarr.getJSONObject(t).getString("firstName"));
                        profile.setLastName(mainarr.getJSONObject(t).getString("lastName"));
                        if(myprofileattribute.has("imagePath")){
                            profile.setImagePath(myprofileattribute.getJSONArray("imagePath").getString(0));
                        }else{
                            profile.setImagePath("");
                        }
                        if(myprofileattribute.has("subscriptionType")){
                            profile.setSubscriptionType(myprofileattribute.getJSONArray("subscriptionType").getString(0));
                        }else{
                            profile.setImagePath("");
                        }

                        if(myprofileattribute.has("phoneNumber")){
                            profile.setPhoneNumber(myprofileattribute.getJSONArray("phoneNumber").getString(0));
                        }else{
                            profile.setPhoneNumber("");
                        }
                        if(myprofileattribute.has("institutionName")){
                            profile.setInstitutionName(myprofileattribute.getJSONArray("institutionName").getString(0));
                        }else{
                            profile.setInstitutionName("");
                        }
                        System.out.println("  subs end dt ::::  "+myprofileattribute.getJSONArray("subscriptionEndDate").getString(0));
                        if(myprofileattribute.has("subscriptionEndDate")){
                            if(myprofileattribute.getJSONArray("subscriptionEndDate").getString(0) != null &&
                                    !myprofileattribute.getJSONArray("subscriptionEndDate").getString(0).isEmpty() ){
                                if(Long.parseLong(myprofileattribute.getJSONArray("subscriptionEndDate").getString(0)) > 0){
                                    profile.setSubscriptionEndDate( Utils.getDate(Long.parseLong(myprofileattribute.getJSONArray("subscriptionEndDate").getString(0)) , "d MMM , yyyy , EEE") );
                                }
                            }
                        }else{
                            profile.setSubscriptionEndDate("");
                        }


                    }


                    skillid = profile.getId();
                    editId.setText(profile.getId());
                    editFirstName.setText(profile.getFirstName());
                    editLastName.setText(profile.getLastName());
                    editEmail.setText(profile.getEmail());
                    editPhoneno.setText(profile.getPhoneNumber());
                    editAddress1.setText(profile.getAddress1());
                    editAddress2.setText(profile.getAddress2());
                    editSubscriptionEnddate.setText(profile.getSubscriptionEndDate());
                    editSubscriptionType.setText(profile.getSubscriptionType());
                    editSubscriptionType.setEnabled(false);
                    editInstitutionname.setText(profile.getInstitutionName());
                    editInstitutionname.setEnabled(false);

                    editId.setVisibility(View.GONE);
                    editId.setEnabled(false);
                    editFirstName.setEnabled(true);
                    editLastName.setEnabled(true);
                    editEmail.setEnabled(false);
                    editSubscriptionEnddate.setEnabled(false);
                    System.out.println("  Profile img ::::  "+profile.getImagePath());
                    if(profile.getImagePath() != null && !profile.getImagePath().isEmpty() && profile.getImagePath().contains("/")){
                        System.out.println("  Profile img ::::  "+profile.getImagePath().split("/")[profile.getImagePath().split("/").length - 1]);
                        doGetProfileImage(profile.getImagePath().split("/")[profile.getImagePath().split("/").length - 1]);
                    }

                    //  Picasso.get().load(achievementListmodel.getUploadCertificate()).into(certificateImage);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getUserProfileImageConsume(AsyncHttpClient client , ProgressDialog progressDialog , String getuserurl){

        client.get(getActivity() , getuserurl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONArray mainarr = new JSONArray(responseString);
                    MyProfile profile = new MyProfile();
                    for(int t=0; t < mainarr.length();t++){
                        myprofileattribute = mainarr.getJSONObject(t).getJSONObject("attributes");
                        if(myprofileattribute.has("imagePath")){
                            profile.setImagePath(myprofileattribute.getJSONArray("imagePath").getString(0));

                        }else{
                            profile.setImagePath("");
                        }
                    }
                    System.out.println("  Profile img ::::  "+profile.getImagePath());
                    if(profile.getImagePath() != null && !profile.getImagePath().isEmpty() && profile.getImagePath().contains("/")){
                        System.out.println("  Profile img ::::  "+profile.getImagePath().split("/")[profile.getImagePath().split("/").length - 1]);
                        ((MainActivity)getActivity()).getObsrProfileModel().getCurrentProfileImg().setValue(profile.getImagePath());
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void doUpdateProfileWithIMage(String id , String phoneno , String address1 , String address2 ) {

        String regurl = getResources().getString(R.string.baseurl) + "skillzag/auth/users/upload/" + id;
        System.out.println("  Profile url ::  " + regurl);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject subparams = new JSONObject();
        JSONObject params = new JSONObject();
        RequestParams param = new RequestParams();
        param.setForceMultipartEntityContentType(true);
        StringEntity entity = null;
        try {
            if (myprofileattribute.has("phoneNumber") && myprofileattribute.getJSONArray("phoneNumber").length() > 0) {
                myprofileattribute.getJSONArray("phoneNumber").put(0, phoneno);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(phoneno);
                myprofileattribute.put("phoneNumber", subarr);
            }

            if (myprofileattribute.has("address1") && myprofileattribute.getJSONArray("address1").length() > 0) {
                myprofileattribute.getJSONArray("address1").put(0, address1);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(address1);
                myprofileattribute.put("address1", subarr);
            }

            if (myprofileattribute.has("address2") && myprofileattribute.getJSONArray("address2").length() > 0) {
                myprofileattribute.getJSONArray("address2").put(0, address2);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(address2);
                myprofileattribute.put("address2", subarr);
            }

            params.put("attributes", myprofileattribute);
            param.put("attribute",params.toString());
           // param.put("file",new File ( "https://d1jnx9ba8s6j9r.cloudfront.net/imgver.1538723273/img/s/co_img_31_1542869147.png" ));
            param.put("file",new File ( ret.getString("uri") ));
            // params.put("userAttribute" , subparams);
            entity = new StringEntity(params.toString());

            System.out.println(" REqqqqqqqqqqqqqqqqqqq  " + params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(getActivity(), regurl, param, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again."+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "  Profile Updated Successfully ", Toast.LENGTH_LONG).show();
//                    if(SharedPrefUtil.with(getActivity()).getString("userid","") != null &&
//                            !SharedPrefUtil.with(getActivity()).getString("userid","").equalsIgnoreCase("null") &&
//                            !SharedPrefUtil.with(getActivity()).getString("userid","").isEmpty()){
//
//                        doGetProfileData(SharedPrefUtil.with(getActivity()).getString("userid",""));
//                     //  ((MainActivity)getActivity()).getObsrProfileModel().getCurrentProfileImg().setValue(profile.getImagePath());
//
//                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void doUpdateProfile(String id , String phoneno , String address1 , String address2 ) {

        String regurl = getResources().getString(R.string.baseurl) + "skillzag/auth/users/update/" + id;
        System.out.println("  Profile url ::  " + regurl);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject subparams = new JSONObject();
        JSONObject params = new JSONObject();

        StringEntity entity = null;
        try {
            if (myprofileattribute.has("phoneNumber") && myprofileattribute.getJSONArray("phoneNumber").length() > 0) {
                myprofileattribute.getJSONArray("phoneNumber").put(0, phoneno);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(phoneno);
                myprofileattribute.put("phoneNumber", subarr);
            }

            if (myprofileattribute.has("address1") && myprofileattribute.getJSONArray("address1").length() > 0) {
                myprofileattribute.getJSONArray("address1").put(0, address1);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(address1);
                myprofileattribute.put("address1", subarr);
            }

            if (myprofileattribute.has("address2") && myprofileattribute.getJSONArray("address2").length() > 0) {
                myprofileattribute.getJSONArray("address2").put(0, address2);
            } else {
                JSONArray subarr = new JSONArray();
                subarr.put(address2);
                myprofileattribute.put("address2", subarr);
            }

            params.put("attributes", myprofileattribute);
            // params.put("userAttribute" , subparams);
            entity = new StringEntity(params.toString());

            System.out.println(" REqqqqqqqqqqqqqqqqqqq  " + params);

        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(getActivity(), regurl, entity, "application/json", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "  Profile Updated Successfully ", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    JSONObject ret = new JSONObject();
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Activity.RESULT_OK:
                if (requestCode == FILE_PICK) {
                    if (data != null) {

                        String mimeType = getContext().getContentResolver().getType(data.getData());
                        String extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);

                        Cursor c = getContext().getContentResolver().query(data.getData(), null, null, null, null);
                        c.moveToFirst();
                        String name = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        long size = c.getLong(c.getColumnIndex(OpenableColumns.SIZE));
                        if(size > 0){

                            try {
                                try {
                                    String path = copyFileToInternalStorage(data.getData(), getContext().getString(R.string.app_name));
//                                    path = path.startsWith("file://") ? path : "file://" + path;
                                    ret.put("uri", path);
//                                    Toast.makeText(getActivity(), "  file patth ::  "+path, Toast.LENGTH_LONG).show();
//
//                                    Picasso.get().load(new File(path)).centerCrop().into(profileimg);
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    ret.put("uri", "");
                                }
                                ret.put("name", name);
                                ret.put("mimeType", mimeType);
                                ret.put("extension", extension);
                                ret.put("size", size);
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }
                           // System.out.println(ret);
//                            call.resolve(ret);
                            Bitmap myBitmap = null;
                            try {
                                System.out.println(new File(ret.getString("uri")));
                                myBitmap = BitmapFactory.decodeFile(new File(ret.getString("uri")).getAbsolutePath());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            profileimg.setImageBitmap(myBitmap);
                        }else{
                            Toast.makeText(getActivity(),"Invalid/Corrupted file selected.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
            case Activity.RESULT_CANCELED:
                Toast.makeText(getActivity(),"File picking was cancelled.", Toast.LENGTH_LONG).show();
                break;
            default:
                Toast.makeText(getActivity(),"An unknown error occurred.", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String copyFileToInternalStorage(Uri uri, String newDirName) {
        Uri returnUri = uri;

        Cursor returnCursor = getContext().getContentResolver().query(returnUri, new String[]{
                OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE
        }, null, null, null);


        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File output;
        if (!newDirName.equals("")) {
            File dir = new File(getContext().getFilesDir() + "/" + newDirName);
            if (!dir.exists()) {
                dir.mkdir();
            }
            output = new File(getContext().getFilesDir() + "/" + newDirName + "/" + name);
        } else {
            output = new File(getContext().getFilesDir() + "/" + name);
        }
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(output);
            int read = 0;
            int bufferSize = 1024;
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }

            inputStream.close();
            outputStream.close();

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        return output.getPath();
    }

}