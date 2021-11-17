package com.abt.skillzage.ui.achievementlist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.Toast;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.AchievementListAdapter;
import com.abt.skillzage.adapter.CourseActiveListAdapter;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.achievementlist.model.AchievementModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.gms.common.util.Strings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import droidninja.filepicker.utils.ContentUriUtils;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class AchievementListFragment extends Fragment {
//implements EasyPermissions.PermissionCallbacks
    private static final int RESULT_OK = 100;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private View rootView;
    private androidx.recyclerview.widget.RecyclerView achievementList;
    private com.google.android.material.button.MaterialButton btnAddAchievement;
    private static int CUSTOM_REQUEST_CODE = 1000;
    Bitmap myBitmap;
    Uri picUri;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();
    private final static int ALL_PERMISSIONS_RESULT = 107;
    public static final int RC_PHOTO_PICKER_PERM = 123;
    public static final int RC_FILE_PICKER_PERM = 321;

    public AchievementListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_achievement_list, container, false);
        btnAddAchievement = rootView.findViewById(R.id.btnAddAchievement);
        achievementList  = rootView.findViewById(R.id.achievementList);
        achievementList.setLayoutManager(new LinearLayoutManager(getActivity()));


        doGetAllAchievements();

        btnAddAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddAchievementDialog();
            }
        });

        return rootView;
    }

    public void doGetAllAchievements() {

        String url = getResources().getString(R.string.baseurl4)+"api/achievements?page=0&size=500";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
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
                    List<AchievementModel> listAchievement = new ArrayList<>();
                    JSONArray mainarr = new JSONArray(responseString);

                    for(int t=0; t < mainarr.length();t++){

                            AchievementModel achievementModel = new AchievementModel();
                            achievementModel.setId(mainarr.getJSONObject(t).getInt("id"));
                            achievementModel.setCertificateDescription(mainarr.getJSONObject(t).getString("certificateDescription"));
                            achievementModel.setCertificationTitle(mainarr.getJSONObject(t).getString("certificationTitle"));
                            achievementModel.setCertificationType(mainarr.getJSONObject(t).getString("certificationType"));
                            achievementModel.setDateOfCompletion(mainarr.getJSONObject(t).getString("dateOfCompletion"));
                            achievementModel.setUploadCertificate(mainarr.getJSONObject(t).getString("uploadCertificate"));
                            if(mainarr.getJSONObject(t).getString("certificationScore") != null &&
                                    !mainarr.getJSONObject(t).getString("certificationScore").equalsIgnoreCase("null")){
                                achievementModel.setCertificationScore(Integer.parseInt(mainarr.getJSONObject(t).getString("certificationScore")));
                            }else{
                                achievementModel.setCertificationScore(0);
                            }

                            listAchievement.add(achievementModel);

                    }

                    System.out.println(listAchievement.size()+"    Active list size :::   "+listAchievement.size());

                    AchievementListAdapter adapter = new AchievementListAdapter();
                    adapter.listAchievement = listAchievement;
                    adapter.context = getActivity();
                    achievementList.setAdapter(adapter);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void doAddAchievementFilePost(File certificateFile,int id) {

        String url = getResources().getString(R.string.baseurl4)+"api/upload/"+id;
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("file",certificateFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(getActivity() , url, params , new TextHttpResponseHandler() {
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
                    if(responseString != null && !responseString.isEmpty()){
                        Toast.makeText(getActivity(), " Saved ", Toast.LENGTH_SHORT).show();
                        doGetAllAchievements();
                    }else{
                        Toast.makeText(getActivity(), "  Please try again ", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public void doAddAchievementPost(String certificateDescription , String certificationScore
            , String certificationTitle , String certificationType , String dateOfCompletion
            , String uploadCertificate , AlertDialog alertDialog) {

        String url = getResources().getString(R.string.baseurl4)+"api/achievements";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            params.put("certificateDescription",certificateDescription);
            params.put("certificationScore",certificationScore);
            params.put("certificationTitle",certificationTitle);
            params.put("certificationType",certificationType);
            params.put("dateOfCompletion",dateOfCompletion);
            params.put("uploadCertificate",uploadCertificate);
            entity = new StringEntity(params.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(getActivity() , url, entity , "application/json" , new TextHttpResponseHandler() {
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
                    JSONObject mainaobj = new JSONObject(responseString);
                    if(mainaobj.has("id")){
                        alertDialog.dismiss();
                            Toast.makeText(getActivity(), " Saved ", Toast.LENGTH_SHORT).show();
                            if(ret != null){
                                if(ret.getString("uri") != null && !ret.getString("uri").isEmpty()){
                                    doAddAchievementFilePost(new File(ret.getString("uri")) , mainaobj.getInt("id"));
                                }
                            }
                    }else{
                        Toast.makeText(getActivity(), "  Please try again ", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    AlertDialog alertDialog;
    androidx.appcompat.widget.AppCompatImageView achievementimg;
    public void openAddAchievementDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        // ...Irrelevant code for customizing
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_add_achievement, null);
        dialogBuilder.setView(dialogView);

        com.google.android.material.textfield.TextInputEditText editCertificateTitle = dialogView.findViewById(R.id.editCertificateTitle);
        com.google.android.material.textfield.TextInputEditText editCertificateDescription = dialogView.findViewById(R.id.editCertificateDescription);
        com.google.android.material.textfield.TextInputEditText editCertificatedate = dialogView.findViewById(R.id.editCertificatedate);
        com.google.android.material.textfield.TextInputEditText editCertificateuploadfile = dialogView.findViewById(R.id.editCertificateuploadfile);
        com.google.android.material.button.MaterialButton btnaddachievement = dialogView.findViewById(R.id.btnaddachievement);
        com.google.android.material.textfield.TextInputEditText editCertificatescore = dialogView.findViewById(R.id.editCertificatescore);
        achievementimg =  dialogView.findViewById(R.id.achievementimg);

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear+1);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd'T'HH:mm:ss.000'Z'"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editCertificatedate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        editCertificatedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH+1),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnaddachievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddAchievementPost(editCertificateDescription.getText().toString() ,
                        editCertificatescore.getText().toString() ,
                        editCertificateTitle.getText().toString() ,
                        "EXTERNAL" ,editCertificatedate.getText().toString() ,
                        editCertificateuploadfile.getText().toString(), alertDialog);
            }
        });

        achievementimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    // openFile();

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                startActivityForResult(intent, FILE_PICK);

            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.show();



    }


    protected static final int FILE_PICK = 1010;
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
                            if(null != achievementimg){
                                achievementimg.setImageBitmap(myBitmap);
                            }
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


//    @Override
//    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
//
//    }
//
//    @Override
//    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
//
//    }
}