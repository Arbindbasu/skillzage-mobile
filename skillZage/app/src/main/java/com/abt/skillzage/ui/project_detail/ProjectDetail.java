package com.abt.skillzage.ui.project_detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CompletedProjectAdapter;
import com.abt.skillzage.ui.project.model.ProjectModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.abt.skillzage.widget.Util;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class ProjectDetail extends Fragment {

    private ProjectDetailViewModel mViewModel;

    public static ProjectDetail newInstance() {
        return new ProjectDetail();
    }

    private View rootView;
    private AppCompatImageView projectimg;
    private RecyclerView completedProjectList;
    public ProjectModel projectModel;
    private com.google.android.material.button.MaterialButton btnJoin , btnQuit;
    private AppCompatTextView labelProjectName , companyname , companyprofiledesc , projectdesc , projectobjective , projectvalidity;
    private AppCompatImageView projectimage;
    private List<String> listEnrolledProjects = new ArrayList<>();
    private boolean projectenrolledstatus = false;
    private int enrollmentid = 0;

    private android.widget.VideoView videourl_videoview , url1_videoview , url2_videoview , url3_videoview;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView videourl_youtube ,url1_youtube , url2_youtube , url3_youtube;
    private WebView url1_webview , url2_webview , url3_webview;
    private androidx.appcompat.widget.AppCompatImageView ur1_docx , ur1_pdf , url2_docx , url2_pdf , url3_docx ,
            url3_pdf ;
    private LinearLayout linear_url1_webview , linear_url2_webview , linear_url3_webview;
    private LinearLayout linearprojectcontent;
    private  com.google.android.material.button.MaterialButton btnRetest;
    private  String questionset_for_project = "0";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.project_detail_fragment, container, false);
        projectimg = rootView.findViewById(R.id.projectimg);
        completedProjectList = rootView.findViewById(R.id.completedProjectList);

        labelProjectName = rootView.findViewById(R.id.labelProjectName);
        companyprofiledesc = rootView.findViewById(R.id.companyprofiledesc);
        projectdesc = rootView.findViewById(R.id.projectdesc);
        projectobjective = rootView.findViewById(R.id.projectobjective);
        projectvalidity = rootView.findViewById(R.id.projectvalidity);
        btnJoin = rootView.findViewById(R.id.btnJoin);
        btnQuit = rootView.findViewById(R.id.btnQuit);
        btnQuit.setVisibility(View.GONE);
        linear_url1_webview = rootView.findViewById(R.id.linear_url1_webview);
        linear_url2_webview = rootView.findViewById(R.id.linear_url2_webview);
        linear_url3_webview = rootView.findViewById(R.id.linear_url3_webview);

        videourl_videoview = rootView.findViewById(R.id.videourl_videoview);
        url1_videoview = rootView.findViewById(R.id.url1_videoview);
        url2_videoview = rootView.findViewById(R.id.url2_videoview);
        url3_videoview = rootView.findViewById(R.id.url3_videoview);
        videourl_youtube = rootView.findViewById(R.id.videourl_youtube);

        url1_youtube = rootView.findViewById(R.id.url1_youtube);
        url2_youtube = rootView.findViewById(R.id.url2_youtube);
        url3_youtube = rootView.findViewById(R.id.url3_youtube);
        url1_webview = rootView.findViewById(R.id.url1_webview);
        url2_webview = rootView.findViewById(R.id.url2_webview);
        url3_webview = rootView.findViewById(R.id.url3_webview);
        linearprojectcontent = rootView.findViewById(R.id.linearprojectcontent);

        ur1_docx = rootView.findViewById(R.id.ur1_docx);
        ur1_pdf = rootView.findViewById(R.id.ur1_pdf);
        url2_docx = rootView.findViewById(R.id.url2_docx);
        url2_pdf = rootView.findViewById(R.id.url2_pdf);
        url3_docx = rootView.findViewById(R.id.url3_docx);
        url3_pdf = rootView.findViewById(R.id.url3_pdf);

        btnRetest = rootView.findViewById(R.id.btnRetest);



        btnRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!questionset_for_project.equalsIgnoreCase("0")){
                    ((MainActivity)getActivity()).setIsnavigatefromproject(true);
                    ((MainActivity)getActivity()).setResCheckEnrollment(resCheckEnrollment);
                    try {
                        ((MainActivity) getActivity()).setQuizid(Integer.parseInt(questionset_for_project));
                        Navigation.findNavController(rootView).navigate(R.id.action_navigation_projet_detail_to_navigation_test_ur_self);
                    }catch(Exception e){
                        Toast.makeText(getActivity() , " No Question Set ", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity() , " No Question Set ", Toast.LENGTH_LONG).show();
                }
            }
        });

        checkUserEnrolledForProject();

        projectModel = ((MainActivity)getActivity()).getProjectModel();
        if(null != projectModel ){
            updateProjectDetails();
        }

        Log.d("projectstatus  ### " ,"COMPLETE********************    "+((MainActivity)getActivity()).isIsnavigatefromproject());
        if(((MainActivity)getActivity()).isIsnavigatefromproject()){
            ((MainActivity)getActivity()).setIsnavigatefromproject(false);
            btnRetest.setBackgroundColor(getResources().getColor(R.color.btn_grey));
            btnRetest.setEnabled(false);
            if(projectModel != null){
                Log.d("projectstatus ### " ,"COMPLETE********************");
                updateProjectStatusToComplete(String.valueOf(projectModel.getId()) ,"COMPLETE");
            }
        }

        if(((MainActivity)getActivity()).getListAllCompletedProjects().size() > 0){

            completedProjectList.setLayoutManager(new LinearLayoutManager(getActivity()));
            CompletedProjectAdapter adapter = new CompletedProjectAdapter();
            adapter.listProject = ((MainActivity)getActivity()).getListAllCompletedProjects();
            adapter.context = getActivity();
            completedProjectList.setAdapter(adapter);

        }

        return rootView;
    }


    public void projectContentUiUpdate(){

        if(projectModel.getVideoUrl() != null && !projectModel.getVideoUrl().equalsIgnoreCase("null") &&
                !projectModel.getVideoUrl().isEmpty()){
            if(projectModel.getVideoUrl().contains("youtube")){
                String videoid = Util.extractYTId(projectModel.getVideoUrl());
                videourl_youtube.setVisibility(View.VISIBLE);
//                    videourl_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(getActivity() , courseModel.getVideoUrl());
//                        }
//                    });
                getLifecycle().addObserver(videourl_youtube);
                videourl_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = videoid;
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });

            }else {
                videourl_youtube.setVisibility(View.GONE);
            }
        }else{
            videourl_youtube.setVisibility(View.GONE);
            videourl_videoview.setVisibility(View.GONE);
        }

        if(projectModel.getUrl1() != null && !projectModel.getUrl1().equalsIgnoreCase("null")
        && !projectModel.getUrl1().isEmpty()) {
            if(projectModel.getUrl1().contains("youtube")){
                String videoid = Util.extractYTId(projectModel.getUrl1());
                url1_youtube.setVisibility(View.VISIBLE);
                getLifecycle().addObserver(url1_youtube);
                url1_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = videoid;
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
//                    url1_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(getActivity() , videoid);
//                        }
//                    });
            }else if(!projectModel.getUrl1().contains("youtube") &&
                    !projectModel.getUrl1().contains(".mp4") &&
                    projectModel.getUrl1().contains("http://") &&
                    projectModel.getUrl1().contains("https://")){

                url1_webview.setVisibility(View.VISIBLE);
                url1_webview.loadData(projectModel.getUrl1(), "text/html", "utf-8");
            }else if(projectModel.getUrl1().contains("doc")){
                linear_url1_webview.setVisibility(View.VISIBLE);
                ur1_docx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/msword";
                        intent.setDataAndType(Uri.fromFile(new File(projectModel.getUrl1())), type);
                        startActivity(intent);
                    }
                });
            }else if(projectModel.getUrl1().contains("pdf")){
                linear_url1_webview.setVisibility(View.VISIBLE);
                ur1_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/pdf";
                        intent.setDataAndType(Uri.fromFile(new File(projectModel.getUrl1())), type);
                        startActivity(intent);
                    }
                });
            }
        }else{
            url1_youtube.setVisibility(View.GONE);
            url1_webview.setVisibility(View.GONE);
            linear_url1_webview.setVisibility(View.GONE);
        }


        if(projectModel.getUrl2() != null && !projectModel.getUrl2().equalsIgnoreCase("null")
        && !projectModel.getUrl2().isEmpty()) {
            if(projectModel.getUrl2().contains("youtube")){
                String videoid = Util.extractYTId(projectModel.getUrl2());
                url2_youtube.setVisibility(View.VISIBLE);
                getLifecycle().addObserver(url2_youtube);
                url2_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = videoid;
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
//                    url2_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(getActivity() , videoid);
//                        }
//                    });

            }
            else if(!projectModel.getUrl2().contains("youtube") &&
                    !projectModel.getUrl2().contains(".mp4") &&
                    projectModel.getUrl2().contains("http://") &&
                    projectModel.getUrl2().contains("https://")){

                url2_webview.setVisibility(View.VISIBLE);
                url2_webview.loadData(projectModel.getUrl2(), "text/html", "utf-8");
            }else if(projectModel.getUrl2().contains("doc")){
                linear_url2_webview.setVisibility(View.VISIBLE);
                url2_docx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/msword";
                        intent.setDataAndType(Uri.fromFile(new File(projectModel.getUrl2())), type);
                        startActivity(intent);
                    }
                });
            }else if(projectModel.getUrl2().contains("pdf")){
                linear_url2_webview.setVisibility(View.VISIBLE);
                url2_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/pdf";
                        intent.setDataAndType(Uri.fromFile(new File(projectModel.getUrl2())), type);
                        startActivity(intent);
                    }
                });
            }

        }else{
            url2_youtube.setVisibility(View.GONE);
            url2_webview.setVisibility(View.GONE);
            linear_url2_webview.setVisibility(View.GONE);
        }

        if(projectModel.getUrl3() != null && !projectModel.getUrl3().equalsIgnoreCase("null")
        && !projectModel.getUrl3().isEmpty()) {
            if(projectModel.getUrl3().contains("youtube")){
                String videoid = Util.extractYTId(projectModel.getUrl3());
                url3_youtube.setVisibility(View.VISIBLE);
                getLifecycle().addObserver(url3_youtube);
                url3_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                        String videoId = videoid;
                        youTubePlayer.loadVideo(videoId, 0);
                    }
                });
//                    url3_youtube.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Util.watchYoutubeVideo(getActivity() , videoid);
//                        }
//                    });

            }
            else if(!projectModel.getUrl3().contains("youtube") &&
                    !projectModel.getUrl3().contains(".mp4") &&
                    projectModel.getUrl3().contains("http://") &&
                    projectModel.getUrl3().contains("https://")){

                url3_webview.setVisibility(View.VISIBLE);
                url3_webview.loadData(projectModel.getUrl3(), "text/html", "utf-8");
            }else if(projectModel.getUrl3().contains("doc")){
                linear_url3_webview.setVisibility(View.VISIBLE);
                url3_docx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/msword";
                        intent.setDataAndType(Uri.fromFile(new File(projectModel.getUrl3())), type);
                        startActivity(intent);
                    }
                });
            }else if(projectModel.getUrl3().contains("pdf")){
                linear_url3_webview.setVisibility(View.VISIBLE);
                url3_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction(Intent.ACTION_VIEW);
                        String type = "application/pdf";
                        intent.setDataAndType(Uri.fromFile(new File(projectModel.getUrl3())), type);
                        startActivity(intent);
                    }
                });
            }
        }else{
            url3_youtube.setVisibility(View.GONE);
            url3_webview.setVisibility(View.GONE);
            linear_url3_webview.setVisibility(View.GONE);
        }


    }

    public void joinquiteventForProject(){
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    enrollmentConfirmation(true);
            }
        });
        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    enrollmentConfirmation(false);
            }
        });

    }


    public void enrollmentConfirmation(boolean enrollmentstatus){
        String enrollment_description="";
        if(enrollmentstatus){
            enrollment_description = " Do you want to join to the project ? ";
        }else{
            enrollment_description = " Do you want to quit from the project ? ";
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation")
                .setMessage(enrollment_description)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        if(projectModel != null){
                            enrollToProject(String.valueOf(projectModel.getId()) , enrollmentstatus ,"NA");
                        }
                    }})

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                      //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }}).show();
    }


    public void updateProjectDetails(){

        labelProjectName.setText(projectModel.getProjectName());
        companyprofiledesc.setText(projectModel.getCompanyProfile());
        projectdesc.setText(projectModel.getProjectDescription());
        projectobjective.setText(projectModel.getObjective());
        projectvalidity.setText(projectModel.getValidfrom() +" - "+projectModel.getValidto());
        labelProjectName.setText(projectModel.getProjectName());

        if(projectModel.getEmailID() != null &&
                !projectModel.getEmailID().equalsIgnoreCase("null") &&
                !projectModel.getEmailID().isEmpty()){
            questionset_for_project = projectModel.getEmailID();
        }else{
            questionset_for_project = "0";
        }

        if( projectModel.getLogo() != null && !projectModel.getLogo().equalsIgnoreCase("null") &&
           !projectModel.getLogo().isEmpty()){
            Picasso.get().load(projectModel.getLogo()).into(projectimg);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProjectDetailViewModel.class);
        // TODO: Use the ViewModel
    }

   private JSONObject resCheckEnrollment = null;
    private String project_completion_status = "NOTPRESENT";
    public void checkUserEnrolledForProject() {

        String url = getResources().getString(R.string.baseurl4)+"api/listUserProjectEnrollmentDetails";
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
                    JSONArray mainarr = new JSONArray(responseString);
                    String userid = SharedPrefUtil.with(getActivity()).getString("userid" , "");
                    for(int t =0; t < mainarr.length(); t++){
                        JSONObject mainobj = mainarr.getJSONObject(t);
                        if(mainobj.getString("userid") != null &&
                                !mainobj.getString("userid").equalsIgnoreCase("null")){

                            if(mainobj.getString("userid").equalsIgnoreCase(userid) &&
                                    mainobj.getString("projectID") != null &&
                                    !mainobj.getString("projectID").equalsIgnoreCase("null") &&
                                    !mainobj.getString("projectID").isEmpty()){


                                    if (mainobj.getString("projectEnrollmentStatus") != null &&
                                            !mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("null") &&
                                            !mainobj.getString("projectEnrollmentStatus").isEmpty()) {

                                        listEnrolledProjects.add(mainobj.getString("projectID"));
                                        if(mainobj.getString("projectID").equalsIgnoreCase(String.valueOf(projectModel.getId()))) {
                                            if (mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("JOIN")) {
                                                projectenrolledstatus = true;
                                                project_completion_status = "JOIN";
                                                enrollmentid = mainobj.getInt("id");
                                                resCheckEnrollment = mainobj;
                                                System.out.println(" Enrollment  :::;   "+enrollmentid);
                                            } else if(mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("QUIT")){
                                                projectenrolledstatus = false;
                                                project_completion_status = "QUIT";
                                                enrollmentid = mainobj.getInt("id");
                                                resCheckEnrollment = mainobj;
                                                System.out.println(" Enrollment  :::;   "+enrollmentid);
                                            }else if(mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("NA")){
                                                projectenrolledstatus = false;
                                                project_completion_status = "NA";
                                                enrollmentid = 0;
                                                resCheckEnrollment = null;
                                                System.out.println(" Enrollment  :::;   "+enrollmentid);
                                            }else if(mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("INPROGRESS")){
                                                projectenrolledstatus = true;
                                                project_completion_status = "INPROGRESS";
                                                enrollmentid = mainobj.getInt("id");
                                                resCheckEnrollment = mainobj;
                                                System.out.println(" Enrollment  :::;   "+enrollmentid);
                                            }else if(mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("COMPLETE")){
                                                projectenrolledstatus = true;
                                                project_completion_status = "COMPLETE";
                                                enrollmentid = mainobj.getInt("id");
                                                resCheckEnrollment = mainobj;
                                                System.out.println(" Enrollment  :::;   "+enrollmentid);
                                            }

                                     }
                                }
                            }else{

                            }
                        }else{

                        }
                    }

                    System.out.println(" All enrolled courses ::::::::      "+listEnrolledProjects);
                    if(projectenrolledstatus){
                        projectContentUiUpdate();
                        btnJoin.setVisibility(View.GONE);
                        btnQuit.setVisibility(View.VISIBLE);
                        linearprojectcontent.setVisibility(View.VISIBLE);
                    }else{
                        btnJoin.setVisibility(View.VISIBLE);
                        btnQuit.setVisibility(View.GONE);
                        linearprojectcontent.setVisibility(View.GONE);
                    }

                    joinquiteventForProject();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void enrollToProject(String projectid , boolean enrollmentstatus ,String completionstatus) {

        String url = getResources().getString(R.string.baseurl4)+"api/user-project-enrollment-details";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            String currentdate = sdf.format(new Date());

            String userid = SharedPrefUtil.with(getActivity()).getString("userid","");
            //params.put("projectEnrollmentDate", Util.getCurrentDateInIso());
            params.put("projectEnrollmentDate", currentdate+"Z");
            params.put("projectID",projectid);
            params.put("userid",userid);
         //   if((enrollmentstatus == true)){
                if(null != resCheckEnrollment){
                    url += "/" + resCheckEnrollment.getString("id");
                    if(resCheckEnrollment.getString("projectEnrollmentStatus").equalsIgnoreCase("JOIN")){
                        resCheckEnrollment.put("projectEnrollmentStatus","QUIT");
                        System.out.println("  PRoejct enrollment status pojo :::::::::::::     " + resCheckEnrollment);
//                    params.put("projectEnrollmentStatus","QUIT");
//                    params.put("id",resCheckEnrollment.getInt("id"));
                        // params.put("id",enrollmentid);
                        entity = new StringEntity(resCheckEnrollment.toString());
                        projectEnrollmentUpdateServiceImp(client,url,entity,progressDialog , enrollmentstatus);
                    }else if(resCheckEnrollment.getString("projectEnrollmentStatus").equalsIgnoreCase("QUIT")){
                        resCheckEnrollment.put("projectEnrollmentStatus","JOIN");
                        System.out.println("  PRoejct enrollment status pojo :::::::::::::     " + resCheckEnrollment);
//                    params.put("projectEnrollmentStatus","JOIN");
//                    params.put("id",resCheckEnrollment.getInt("id"));
                        entity = new StringEntity(resCheckEnrollment.toString());
                        projectEnrollmentUpdateServiceImp(client,url,entity,progressDialog , enrollmentstatus);
                    }else if(completionstatus.equalsIgnoreCase("COMPLETE")){
                        resCheckEnrollment.put("projectEnrollmentStatus","COMPLETE");
                        System.out.println("  PRoejct enrollment status pojo :::::::::::::     " + resCheckEnrollment);
//                    params.put("projectEnrollmentStatus",completestatus);
//                    params.put("id",resCheckEnrollment.getInt("id"));
                        entity = new StringEntity(resCheckEnrollment.toString());
                        projectEnrollmentUpdateServiceImp(client,url,entity,progressDialog , enrollmentstatus);
                    }
                }else if(enrollmentstatus == true){
                    // if(enrollmentstatus){
                    params.put("projectEnrollmentStatus","JOIN");
                    System.out.println("  PRoejct enrollment status pojo :::::::::::::     " + params);
                    entity = new StringEntity(params.toString());
                    projectEnrollmentCreateServiceImp(client,url,entity,progressDialog);
                    //  }
                }

          //  }

         //   Toast.makeText(getActivity() , " Reqeust pojo  :::  "+params.toString(),Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void updateProjectStatusToComplete(String projectid , String completionstatus) {

        String url = getResources().getString(R.string.baseurl4)+"api/user-project-enrollment-details";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
            String currentdate = sdf.format(new Date());

            String userid = SharedPrefUtil.with(getActivity()).getString("userid","");
            //params.put("projectEnrollmentDate", Util.getCurrentDateInIso());
            params.put("projectEnrollmentDate", currentdate+"Z");
            params.put("projectID",projectid);
            params.put("userid",userid);
            if(null != resCheckEnrollment){
            }else{
                resCheckEnrollment = ((MainActivity)getActivity()).getResCheckEnrollment();
            }
            System.out.println("  Project complete req :::::   "+resCheckEnrollment.toString());
            params.put("id",resCheckEnrollment.getInt("id"));
            if(null != resCheckEnrollment){
                url += "/" + resCheckEnrollment.getString("id");
                if(completionstatus.equalsIgnoreCase("COMPLETE")){
                    resCheckEnrollment.put("projectEnrollmentStatus","COMPLETE");
                    System.out.println("  Project complete :::::   "+resCheckEnrollment.toString());
                    entity = new StringEntity(resCheckEnrollment.toString());
                    projectEnrollmentUpdateServiceImp(client,url,entity,progressDialog , true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void projectEnrollmentCreateServiceImp(AsyncHttpClient client , String url , StringEntity entity , final ProgressDialog progressDialog){

        client.post(getActivity(), url, entity , "application/json" ,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), statusCode+"   Error  "+responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved for create is    " + responseString);
                try {
                    progressDialog.dismiss();
                    projectContentUiUpdate();
                    btnJoin.setVisibility(View.GONE);
                    btnQuit.setVisibility(View.VISIBLE);
                    linearprojectcontent.setVisibility(View.VISIBLE);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void projectEnrollmentUpdateServiceImp(AsyncHttpClient client , String url , StringEntity entity ,
                                                  final ProgressDialog progressDialog ,
                                                  final boolean enrollmentstatus){

        client.put(getActivity(), url, entity , "application/json" ,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved for update   " + responseString);
                try {
                    progressDialog.dismiss();

                    if(new JSONObject(responseString).has("id")){
                        resCheckEnrollment = new JSONObject(responseString);
                    }else{
                        Toast.makeText(getActivity() , "Failed" , Toast.LENGTH_LONG).show();
                    }

                        if(resCheckEnrollment.getString("projectEnrollmentStatus").equalsIgnoreCase("QUIT")){
                            btnJoin.setVisibility(View.VISIBLE);
                            btnQuit.setVisibility(View.GONE);
                            linearprojectcontent.setVisibility(View.GONE);
                        }else if(resCheckEnrollment.getString("projectEnrollmentStatus").equalsIgnoreCase("JOIN")){
                            projectContentUiUpdate();
                            btnJoin.setVisibility(View.GONE);
                            btnQuit.setVisibility(View.VISIBLE);
                            linearprojectcontent.setVisibility(View.VISIBLE);
                        }else if(resCheckEnrollment.getString("projectEnrollmentStatus").equalsIgnoreCase("COMPLETE")){
                            // If project has completed...
                          //  projectContentUiUpdate();
                            btnJoin.setVisibility(View.GONE);
                            btnQuit.setVisibility(View.GONE);
                         //   linearprojectcontent.setVisibility(View.VISIBLE);
                        }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }




}