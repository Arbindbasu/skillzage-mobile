package com.abt.skillzage.ui.course_detail;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.adapter.SessionListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.course_detail.model.CourseSessionModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.abt.skillzage.widget.Util;
import com.google.android.material.snackbar.Snackbar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class CourseDetail extends Fragment {

    private CourseDetailViewModel mViewModel;

    public static CourseDetail newInstance() {
        return new CourseDetail();
    }

    View rootView;
    AppCompatImageView courseimg;
    RecyclerView sessionList;
    AppCompatTextView labelCourseName , coursedescription;
    String courseImage = "";
    String courseDescr = "";
    CourseModel courseModel;
    String enrollmentid = "0";
    String subscriptionid = "0";
    private android.widget.VideoView videourl_videoview , url1_videoview , url2_videoview , url3_videoview;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView videourl_youtube ,url1_youtube , url2_youtube , url3_youtube;
    private WebView url1_webview , url2_webview , url3_webview;
    private androidx.appcompat.widget.AppCompatImageView ur1_docx , ur1_pdf , url2_docx , url2_pdf , url3_docx ,
            url3_pdf ;
    private LinearLayout linear_url1_webview , linear_url2_webview , linear_url3_webview;
    private com.google.android.material.button.MaterialButton btnEnroll;
    String courseid = "0";
    com.google.android.material.button.MaterialButton btnKnowyourself , btnFinalTestyourself;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.course_detail_fragment, container, false);
        courseimg = rootView.findViewById(R.id.courseimg);
        sessionList = rootView.findViewById(R.id.sessionList);
        labelCourseName = rootView.findViewById(R.id.labelCourseName);
        coursedescription = rootView.findViewById(R.id.coursedescription);

        courseid = String.valueOf(((MainActivity)getActivity()).getCourseId());
        courseModel = ((MainActivity)getActivity()).getModel();
        courseImage = ((MainActivity)getActivity()).getCourse_image();
        //  Toast.makeText(getActivity(), courseImage+"   ::::   selected course id   :::    "+courseid, Toast.LENGTH_LONG).show();

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
        btnKnowyourself = rootView.findViewById(R.id.btnKnowyourself);
        btnFinalTestyourself = rootView.findViewById(R.id.btnFinalTestyourself);


        System.out.println("hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii **********************************   &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&     ");
        Log.d(" course detail ::;  ", String.valueOf(((MainActivity)getActivity()).isIsnavigatefromcourse_knowurself()));
        if(((MainActivity)getActivity()).isIsnavigatefromcourse_knowurself()){
            ((MainActivity)getActivity()).setIsnavigatefromcourse_knowurself(false);
            ((MainActivity)getActivity()).setQuizid(0);
            // Call one api to update user based status for knowurself / final test to conclude
            //course completed.....
            updateCourseForKnowurselfOrCourseComplete(courseid , true);
        }else if(((MainActivity)getActivity()).isIsnavigatefromsession_test_quiz()){
            ((MainActivity)getActivity()).setIsnavigatefromsession_test_quiz(false);
            ((MainActivity)getActivity()).setQuizid(0);


        }else if(((MainActivity)getActivity()).isIsnavigatefromsession_final_test_urself()){
            // Call one api to update user based status for knowurself / final test to conclude
            //course completed.....
            ((MainActivity)getActivity()).setIsnavigatefromsession_final_test_urself(false);
            ((MainActivity)getActivity()).setQuizid(0);
            updateCourseForKnowurselfOrCourseComplete(courseid , false);
        }

        btnKnowyourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(" Quiz know ur self b4 course quiz id :::   "+quizb4course);
                if(quizb4course == null || quizb4course.equalsIgnoreCase("null")
                || quizb4course.isEmpty()){
                    return;
                }
                ((MainActivity)getActivity()).setIsnavigatefromcourse_knowurself(true);
                ((MainActivity)getActivity()).setQuizid(Integer.parseInt(quizb4course));
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_course_detail_to_navigation_test_ur_self);
            }
        });

        btnFinalTestyourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(" Quiz know ur self a4 course quiz id :::   "+quiza4course);
                if(quiza4course == null || quiza4course.equalsIgnoreCase("null")
                        || quiza4course.isEmpty()){
                    return;
                }
                ((MainActivity)getActivity()).setIsnavigatefromsession_final_test_urself(true);
                ((MainActivity)getActivity()).setQuizid(Integer.parseInt(quiza4course));
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_course_detail_to_navigation_test_ur_self);
            }
        });

        ur1_docx = rootView.findViewById(R.id.ur1_docx);
        ur1_pdf = rootView.findViewById(R.id.ur1_pdf);
        url2_docx = rootView.findViewById(R.id.url2_docx);
        url2_pdf = rootView.findViewById(R.id.url2_pdf);
        url3_docx = rootView.findViewById(R.id.url3_docx);
        url3_pdf = rootView.findViewById(R.id.url3_pdf);
        btnEnroll = rootView.findViewById(R.id.btnEnroll);

        checkUserSubscription();

        btnEnroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnEnroll.getText().equals("Enrolled")){
//                    Snackbar snackbar = Snackbar
//                            .make(v, " You have already enrolled to the coourse", Snackbar.LENGTH_LONG);
//                    snackbar.show();
                    // For un - enroll...............
                    enrollmentConfirmation(true);
                }else{
                    // For enroll...............
                    enrollmentConfirmation(false);
                }
             }
        });
        System.out.println("  course  data :::  "+courseModel.toString());
        if(courseModel != null){
            quizb4course = courseModel.getQuizb4Course();
            courseid = String.valueOf(courseModel.getCourseId());
            labelCourseName.setText(courseModel.getCourseName());
            Picasso.get().load(courseModel.getImagePath()).into(courseimg);
            coursedescription.setText(courseModel.getCourseDescription());
        }
        sessionList.setLayoutManager(new LinearLayoutManager(getActivity()));
        sessionList.setNestedScrollingEnabled(false);

        return rootView;
    }

      String quiza4course = "";
      String quizb4course = "";
    public void getCourseContent(){

        quiza4course = courseModel.getQuiza4Course();
        System.out.println("   course content :::::::      "+courseModel.toString());
        if(courseModel != null) {

            if (courseModel.getVideoUrl() != null && !courseModel.getVideoUrl().equalsIgnoreCase("null") &&
                    !courseModel.getVideoUrl().isEmpty()) {
                if (courseModel.getVideoUrl().contains("youtube")) {
                    String videoid = Util.extractYTId(courseModel.getVideoUrl());
                    videourl_youtube.setVisibility(View.VISIBLE);
                    getLifecycle().addObserver(videourl_youtube);
                    videourl_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }
                    });

                } else {

                }
            }else{
                videourl_youtube.setVisibility(View.GONE);
            }

            if (courseModel.getUrl1() != null
                    && !courseModel.getUrl1().equalsIgnoreCase("null")
                    && !courseModel.getUrl1().isEmpty()) {
                if (courseModel.getUrl1().contains("youtube")) {
                    String videoid = Util.extractYTId(courseModel.getUrl1());
                    url1_youtube.setVisibility(View.VISIBLE);
                    url1_youtube.setEnableAutomaticInitialization(false);
                    getLifecycle().addObserver(url1_youtube);

                    url1_youtube.initialize(new YouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);;;
                        }

                        @Override
                        public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {

                        }

                        @Override
                        public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                        }

                        @Override
                        public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {

                        }

                        @Override
                        public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {

                        }

                        @Override
                        public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {

                        }

                        @Override
                        public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

                        }
                    });


//                    url1_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                        @Override
//                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                            String videoId = videoid;
//                            youTubePlayer.loadVideo(videoId, 0);
//                        }
//                    });

                } else if (!courseModel.getUrl1().contains("youtube") &&
                        !courseModel.getUrl1().contains(".mp4") &&
                        courseModel.getUrl1().contains("http://") &&
                        courseModel.getUrl1().contains("https://")) {

                    url1_webview.setVisibility(View.VISIBLE);
                    url1_webview.loadData(courseModel.getUrl1(), "text/html", "utf-8");
                } else if (courseModel.getUrl1().contains("doc")) {
                    ur1_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/msword";
                            intent.setDataAndType(Uri.fromFile(new File(courseModel.getUrl1())), type);
                            startActivity(intent);
                        }
                    });
                } else if (courseModel.getUrl1().contains("pdf")) {
                    ur1_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/pdf";
                            intent.setDataAndType(Uri.fromFile(new File(courseModel.getUrl1())), type);
                            startActivity(intent);
                        }
                    });
                }
            }else{
                url1_youtube.setVisibility(View.GONE);
                url1_webview.setVisibility(View.GONE);
                ur1_docx.setVisibility(View.GONE);
                ur1_pdf.setVisibility(View.GONE);
            }

            if (courseModel.getUrl2() != null
                    && !courseModel.getUrl2().equalsIgnoreCase("null")
                    && !courseModel.getUrl2().isEmpty()) {
                if (courseModel.getUrl2().contains("youtube")) {
                    String videoid = Util.extractYTId(courseModel.getUrl2());
                    url2_youtube.setVisibility(View.VISIBLE);
                    url2_youtube.setEnableAutomaticInitialization(false);
                    getLifecycle().addObserver(url2_youtube);

                    url2_youtube.initialize(new YouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }

                        @Override
                        public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {

                        }

                        @Override
                        public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                        }

                        @Override
                        public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {

                        }

                        @Override
                        public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {

                        }

                        @Override
                        public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {

                        }

                        @Override
                        public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

                        }
                    });



//                    url2_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                        @Override
//                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                            String videoId = videoid;
//                            youTubePlayer.loadVideo(videoId, 0);
//                        }
//                    });


                } else if (!courseModel.getUrl2().contains("youtube") &&
                        !courseModel.getUrl2().contains(".mp4") &&
                        courseModel.getUrl2().contains("http://") &&
                        courseModel.getUrl2().contains("https://")) {

                    url2_webview.setVisibility(View.VISIBLE);
                    url2_webview.loadData(courseModel.getUrl2(), "text/html", "utf-8");
                } else if (courseModel.getUrl2().contains("doc")) {
                    url2_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/msword";
                            intent.setDataAndType(Uri.fromFile(new File(courseModel.getUrl2())), type);
                            startActivity(intent);
                        }
                    });
                } else if (courseModel.getUrl2().contains("pdf")) {
                    url2_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/pdf";
                            intent.setDataAndType(Uri.fromFile(new File(courseModel.getUrl2())), type);
                            startActivity(intent);
                        }
                    });
                }
            }else{
                url2_youtube.setVisibility(View.GONE);
                url2_docx.setVisibility(View.GONE);
                url2_pdf.setVisibility(View.GONE);
                url2_webview.setVisibility(View.GONE);

            }

            if (courseModel.getUrl3() != null
                    && !courseModel.getUrl3().equalsIgnoreCase("null")
                    && !courseModel.getUrl3().isEmpty()) {
                if (courseModel.getUrl3().contains("youtube")) {
                    String videoid = Util.extractYTId(courseModel.getUrl3());
                    url3_youtube.setVisibility(View.VISIBLE);
                    url3_youtube.setEnableAutomaticInitialization(false);
                    getLifecycle().addObserver(url3_youtube);

//                    url3_youtube.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                        @Override
//                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
//                            String videoId = videoid;
//                            youTubePlayer.loadVideo(videoId, 0);
//                        }
//                    });

                    url3_youtube.initialize(new YouTubePlayerListener() {
                        @Override
                        public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                            String videoId = videoid;
                            youTubePlayer.loadVideo(videoId, 0);
                        }

                        @Override
                        public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState playerState) {

                        }

                        @Override
                        public void onPlaybackQualityChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackQuality playbackQuality) {

                        }

                        @Override
                        public void onPlaybackRateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlaybackRate playbackRate) {

                        }

                        @Override
                        public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError playerError) {

                        }

                        @Override
                        public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoDuration(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoLoadedFraction(@NonNull YouTubePlayer youTubePlayer, float v) {

                        }

                        @Override
                        public void onVideoId(@NonNull YouTubePlayer youTubePlayer, @NonNull String s) {

                        }

                        @Override
                        public void onApiChange(@NonNull YouTubePlayer youTubePlayer) {

                        }
                    });



                } else if (!courseModel.getUrl3().contains("youtube") &&
                        !courseModel.getUrl3().contains(".mp4") &&
                        courseModel.getUrl3().contains("http://") &&
                        courseModel.getUrl3().contains("https://")) {

                    url3_webview.setVisibility(View.VISIBLE);
                    url3_webview.loadData(courseModel.getUrl3(), "text/html", "utf-8");
                } else if (courseModel.getUrl3().contains("doc")) {
                    url3_docx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/msword";
                            intent.setDataAndType(Uri.fromFile(new File(courseModel.getUrl3())), type);
                            startActivity(intent);
                        }
                    });
                } else if (courseModel.getUrl3().contains("pdf")) {
                    url3_pdf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setAction(Intent.ACTION_VIEW);
                            String type = "application/pdf";
                            intent.setDataAndType(Uri.fromFile(new File(courseModel.getUrl3())), type);
                            startActivity(intent);
                        }
                    });
                }
            }else{
                url3_youtube.setVisibility(View.GONE);
                url3_docx.setVisibility(View.GONE);
                url3_pdf.setVisibility(View.GONE);
                url3_webview.setVisibility(View.GONE);
            }
        }

    }


    public void enrollmentConfirmation(boolean enrollmentstatus){
        String enrollment_description="";
        if(enrollmentstatus){
            enrollment_description = " Do you want to remove your enrollment from the course ? ";
        }else{
            enrollment_description = " Do you want to enroll to the course ? ";
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation")
                .setMessage(enrollment_description)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                       // Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        System.out.println("  inside click event  ::::   "+courseModel.toString());
                        if(courseModel != null){
                            enrollToCourse(String.valueOf(courseModel.getCourseId()) , enrollmentstatus);
                        }
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                      //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CourseDetailViewModel.class);
        // TODO: Use the ViewModel
    }



    public void doGetCourseDetails(String courseid) {

        String url = getResources().getString(R.string.baseurl3)+"api/course-session-bycourse-id/"+courseid;
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
                    List<CourseSessionModel> listCourseSessionModel = new ArrayList<>();
                    JSONArray mainarr = new JSONArray(responseString);
                    String coursename = "";

                    for(int t=0; t < mainarr.length();t++){

                        if(mainarr.getJSONObject(t).getString("session_status").equalsIgnoreCase("INACTIVE")){
                            continue;
                        }

                        CourseSessionModel courseModel = new CourseSessionModel();
                        courseModel.setSession_video_url(mainarr.getJSONObject(t).getString("session_video_url"));
                        courseModel.setUrl_3(mainarr.getJSONObject(t).getString("url_3"));
                        courseModel.setSubscription_required(mainarr.getJSONObject(t).getString("subscription_required"));
                        courseModel.setImage_path(mainarr.getJSONObject(t).getString("image_path"));
                        courseModel.setCourse_description(mainarr.getJSONObject(t).getString("course_description"));
                        courseModel.setVideo_url(mainarr.getJSONObject(t).getString("video_url"));
                        courseModel.setSession_name(mainarr.getJSONObject(t).getString("session_name"));
                        courseModel.setSession_description(mainarr.getJSONObject(t).getString("session_description"));
                        courseModel.setSession_status(mainarr.getJSONObject(t).getString("session_status"));
                        courseModel.setCourse_name(mainarr.getJSONObject(t).getString("course_name"));
                        courseModel.setSession_logo(mainarr.getJSONObject(t).getString("session_logo"));
                        courseModel.setCourses_management_id(mainarr.getJSONObject(t).getInt("courses_management_id"));
                        courseModel.setSession_number(mainarr.getJSONObject(t).getInt("session_number"));
                        courseModel.setSession_id(mainarr.getJSONObject(t).getInt("session_id"));
                        courseModel.setCourse_objective(mainarr.getJSONObject(t).getString("course_objective"));
                        courseModel.setSession_url(mainarr.getJSONObject(t).getString("session_url"));
                        courseModel.setValid_to(mainarr.getJSONObject(t).getString("valid_to"));
                        courseModel.setCourse_status(mainarr.getJSONObject(t).getString("course_status"));
                        courseModel.setRecommended_status(mainarr.getJSONObject(t).getString("recommended_status"));
                        courseModel.setValid_from(mainarr.getJSONObject(t).getString("valid_from"));
                        courseModel.setCourse_id(mainarr.getJSONObject(t).getInt("course_id"));
                        courseModel.setUrl_1(mainarr.getJSONObject(t).getString("url_1"));
                        courseModel.setUrl_2(mainarr.getJSONObject(t).getString("url_2"));
                        courseModel.setQuiz(mainarr.getJSONObject(t).getString("quiz"));
                        courseModel.setShow_collapse_status("0");
                        listCourseSessionModel.add(courseModel);

                        coursename = mainarr.getJSONObject(t).getString("course_name");
                        courseDescr = mainarr.getJSONObject(t).getString("course_description");

                    }

                    Collections.sort(listCourseSessionModel);

                    SessionListAdapter sessionadapter = new SessionListAdapter();
                    sessionadapter.listCourseSessionModel = listCourseSessionModel;
                    sessionadapter.context = getActivity();
                    sessionadapter.lifecycle = getLifecycle();
                    sessionadapter.rootView = rootView;

                    sessionList.setAdapter(sessionadapter);
                    sessionList.setVisibility(View.VISIBLE);

//                    CourseListAdapter adapter = new CourseListAdapter();
//                    adapter.listCourseModel = listCourseModel;
//                    adapter.context = getActivity();
//                    courseList.setAdapter(adapter);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    // Update course completion (Knowurself/testurself) complete...
    public void updateCourseForKnowurselfOrCourseComplete(String courseid , boolean knowurself ) {

        String url = getResources().getString(R.string.baseurl3)+"api/user-activities";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();
        StringEntity entity = null;

        System.out.println("   entityt  ::::  ******* ########################  ::::  "+knowurself);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSSZ", Locale.ENGLISH);
            String currentdate = sdf.format(new Date());

            if(mainobj_course == null){
                mainobj_course = ((MainActivity)getActivity()).getUseractivityres();
            }

            String userid = SharedPrefUtil.with(getActivity()).getString("userid","");
            params.put("activityDate",mainobj_course.getString("activityDate"));
            params.put("courseId",mainobj_course.getInt("courseId"));
            params.put("subscriptionId",mainobj_course.getInt("subscriptionId"));
            params.put("userId",mainobj_course.getString("userId"));
            params.put("enrollmentStatus",mainobj_course.getString("enrollmentStatus"));
            params.put("id",mainobj_course.getInt("id"));
            // change this institution id to some other value....as per the extra parameter needed..
            if(knowurself){
                // Know yourself test is given....proceed to view session........
                params.put("instituteId",1);
            }else{
                // Final Test is also given.........
                params.put("instituteId",-1);
            }
            System.out.println("   $$$$$$$  ******* ########################  ::::  "+params.toString());
            entity = new StringEntity(params.toString());
            courseEnrollmentUpdateServiceImp(client,url,entity,progressDialog,"Yes");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enrollToCourse(String courseid , boolean enrollmentstatus) {

        String url = getResources().getString(R.string.baseurl3)+"api/user-activities";
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

            String userid = SharedPrefUtil.with(getActivity()).getString("userid", "");
            params.put("activityDate", currentdate + "Z");
            params.put("courseId", courseid);
            params.put("subscriptionId", Integer.parseInt(subscriptionid));
            params.put("userId", userid);
            params.put("courseCompletionStatus", "NA");
            params.put("instituteId", 0);
            System.out.println("  main course obj model :::::    " + mainobj_course.toString());

            System.out.println(mainobj_course.getString("enrollmentStatus") + " -- " + mainobj_course.getInt("courseId") + " -- " + "  main course obj model :::::    " + mainobj_course.toString());
            if (mainobj_course.has("id")
                    && (null == mainobj_course.getString("enrollmentStatus")
                    || mainobj_course.getString("enrollmentStatus").isEmpty())
                    && mainobj_course.getString("courseId").equalsIgnoreCase("0")) {
                // Means no enrollment in user actuvuty present for this course id.
                params.put("id", mainobj_course.getInt("id"));
                params.put("enrollmentStatus", "Yes");
                entity = new StringEntity(params.toString());
                System.out.println(" Enroll obj ::  " + params.toString());
                System.out.println(mainobj_course.getString("enrollmentStatus") + " -- " + mainobj_course.getString("courseId") + " -- " + "  main course obj model :::::    " + mainobj_course.toString());
                courseEnrollmentUpdateServiceImp(client, url, entity, progressDialog, "Yes");
                // courseEnrollmentCreateServiceImp(client,url,entity,progressDialog);
            } else if(mainobj_course.has("id") &&
                    mainobj_course.getString("courseId").equalsIgnoreCase(courseid)) {
                if (mainobj_course.getString("enrollmentStatus").equalsIgnoreCase("Yes")) {
                    params.put("enrollmentStatus", "No");
                    params.put("id", mainobj_course.getInt("id"));
                    entity = new StringEntity(params.toString());
                    courseEnrollmentUpdateServiceImp(client, url, entity, progressDialog, "No");
                } else if (mainobj_course.getString("enrollmentStatus").equalsIgnoreCase("No")) {
                    params.put("enrollmentStatus", "Yes");
                    params.put("id", mainobj_course.getInt("id"));
                    entity = new StringEntity(params.toString());
                    courseEnrollmentUpdateServiceImp(client, url, entity, progressDialog, "Yes");
                }
            }else if(mainobj_course.has("id") &&
                    !mainobj_course.getString("courseId").equalsIgnoreCase(courseid)){

                params.put("enrollmentStatus", "Yes");
                entity = new StringEntity(params.toString());
                courseEnrollmentCreateServiceImp(client, url, entity, progressDialog);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void courseEnrollmentCreateServiceImp(AsyncHttpClient client , String url , StringEntity entity , final ProgressDialog progressDialog){

        client.post(getActivity(), url, entity , "application/json" ,new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved for create is    " + responseString);
                try {
                    progressDialog.dismiss();
                    getCourseContent();
                    Toast.makeText(getActivity(), " You need to submit know your self test first before proceed to the course session  ", Toast.LENGTH_SHORT).show();

                    // doGetCourseDetails(courseid);
                   // sessionList.setVisibility(View.VISIBLE);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void courseEnrollmentUpdateServiceImp(AsyncHttpClient client , String url , StringEntity entity , final ProgressDialog progressDialog , String enrollmentStatus){

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

                    if(enrollmentStatus.equalsIgnoreCase("Yes")){
                              btnEnroll.setText("Enrolled");
                        courseContentShow();
                        getCourseContent();
                        doGetCourseDetails(courseid);
                        //sessionList.setVisibility(View.VISIBLE);
                    }else if(enrollmentStatus.equalsIgnoreCase("No")){
                              btnEnroll.setText("Enroll");
                        sessionList.setVisibility(View.GONE);
                        courseContentHide();
                        btnFinalTestyourself.setBackgroundColor(getResources().getColor(R.color.btn_grey));
                        btnFinalTestyourself.setEnabled(false);
                    }

                    if(mainobj_course.getInt("instituteId") == 1){
                        // Course inprogress.....know yourself test done...
                        btnFinalTestyourself.setBackgroundColor(getResources().getColor(R.color.active_orange));
                        btnFinalTestyourself.setEnabled(false);

                    }else if(mainobj_course.getInt("instituteId") == -1){
                        // Course complete.....
                        btnFinalTestyourself.setBackgroundColor(getResources().getColor(R.color.btn_grey));
                        btnFinalTestyourself.setEnabled(false);
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    JSONObject mainobj_course = null;
    private String checkknowurselfcompletornotforuser = "";
    public void checkUserSubscription() {

        String url = getResources().getString(R.string.baseurl3)+"api/listUserActivities";
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
                    checkknowurselfcompletornotforuser = responseString;
                   String userid = SharedPrefUtil.with(getActivity()).getString("userid" , "");
                    boolean checksubscription = false;
                    boolean checkEnroll = false;
                    boolean checkKNowurselfStatus = false;
                    boolean checkcoursecompleteStatus = false;
                    for(int t =0; t < mainarr.length(); t++){
                       JSONObject mainobj = mainarr.getJSONObject(t);
                       if(mainobj.getString("userId") != null &&
                               !mainobj.getString("userId").equalsIgnoreCase("null")){
                         if(mainobj.getString("userId").equalsIgnoreCase(userid) &&
                                  mainobj.getString("subscriptionId") != null &&
                                 !mainobj.getString("subscriptionId").equalsIgnoreCase("null") &&
                                 !mainobj.getString("subscriptionId").isEmpty()){
                             String noofmilli = "0";
                             if(SharedPrefUtil.with(getActivity()).getString("subscriptionEndDate", "") != null &&
                                     !SharedPrefUtil.with(getActivity()).getString("subscriptionEndDate", "").equalsIgnoreCase("0") &&
                                     !SharedPrefUtil.with(getActivity()).getString("subscriptionEndDate", "").equalsIgnoreCase("NA")){
                                 noofmilli = SharedPrefUtil.with(getActivity()).getString("subscriptionEndDate", "");
                             }

                                 if(Integer.parseInt(noofmilli) > 0  &&  Integer.parseInt(noofmilli) < new Date().getTime()){
                                     checksubscription = false;
                                     subscriptionid = mainobj.getString("subscriptionId");
                                 }else{
                                     if(checksubscription == false){
                                         checksubscription = true;
                                         subscriptionid = mainobj.getString("subscriptionId");
                                         if(mainobj.getString("enrollmentStatus") != null &&
                                                 !mainobj.getString("enrollmentStatus").equalsIgnoreCase("null") &&
                                                 !mainobj.getString("enrollmentStatus").isEmpty() &&
                                                 mainobj.getString("courseId") != null &&
                                                 !mainobj.getString("courseId").equalsIgnoreCase("null") &&
                                                 !mainobj.getString("courseId").isEmpty()){
                                             if(mainobj.getString("courseId").equalsIgnoreCase(courseid) &&
                                                     mainobj.getString("enrollmentStatus").equalsIgnoreCase("Yes")){
                                                 mainobj_course = mainobj;
                                                 if(mainobj.getInt("instituteId") == 1){
                                                     checkKNowurselfStatus =  true;
                                                 }else if(mainobj.getInt("instituteId") == -1){
                                                     checkcoursecompleteStatus = true;
                                                 }
                                                 checkEnroll = true;
                                                 break;
                                             }else{
                                                 mainobj_course = mainobj;
                                             }
                                         }else{
                                             mainobj_course = mainobj;
                                         }
                                         break;
                                     }
                                 }

                         }else{
                             if(checksubscription == true) {

                             }else{
                                 checksubscription = false;
                             }
                         }
                       }else{
                           if(checksubscription == true) {

                           }else{
                               checksubscription = false;
                           }
                       }
                   }

                    if(!checksubscription){
                        btnEnroll.setVisibility(View.GONE);
                        sessionList.setVisibility(View.GONE);
                        courseContentHide();
                        buySubscriptionDialog();
                    }else{
                        System.out.println(checkcoursecompleteStatus+"  check booelan data ::   "+checkKNowurselfStatus);

                        if(checkEnroll){
                            btnEnroll.setVisibility(View.VISIBLE);
                            btnEnroll.setText("Enrolled");
                            getCourseContent();
                            courseContentShow();
                            if(checkKNowurselfStatus){
                                btnFinalTestyourself.setBackgroundColor(getResources().getColor(R.color.active_orange));
                                doGetCourseDetails(courseid);
                                sessionList.setVisibility(View.VISIBLE);

                            }else if(checkcoursecompleteStatus){
                                btnEnroll.setBackgroundColor(getResources().getColor(R.color.btn_grey));
                                btnEnroll.setEnabled(false);

                                btnFinalTestyourself.setBackgroundColor(getResources().getColor(R.color.btn_grey));
                                btnFinalTestyourself.setEnabled(false);
                                doGetCourseDetails(courseid);
                                sessionList.setVisibility(View.VISIBLE);

                            }else{
                                if(checkKNowurselfStatus){

                                }else{
                                    Toast.makeText(getActivity(), " You need to submit know yourself test first before proceed further  ", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }else{
                            if(checksubscription){

                                btnEnroll.setVisibility(View.VISIBLE);
                                btnEnroll.setText("Enroll");
                                sessionList.setVisibility(View.GONE);

                                courseContentHide();
                            }else{

                            }
                        }
                    }


                    if(mainobj_course != null){
                        ((MainActivity)getActivity()).setUseractivityres(mainobj_course);
                        System.out.println("   response  data ::::::      "+mainobj_course.toString());
                    }


                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void courseContentHide(){
        url1_webview.setVisibility(View.GONE);
        url2_webview.setVisibility(View.GONE);
        url3_webview.setVisibility(View.GONE);
        url1_youtube.setVisibility(View.GONE);
        url2_youtube.setVisibility(View.GONE);
        url3_youtube.setVisibility(View.GONE);
        videourl_youtube.setVisibility(View.GONE);
        videourl_videoview.setVisibility(View.GONE);
        btnKnowyourself.setEnabled(false);
        btnKnowyourself.setBackgroundColor(getResources().getColor(R.color.btn_grey));
        btnKnowyourself.setTextColor(getResources().getColor(R.color.white));
    }

    public void courseContentShow(){
        url1_webview.setVisibility(View.VISIBLE);
        url2_webview.setVisibility(View.VISIBLE);
        url3_webview.setVisibility(View.VISIBLE);
        url1_youtube.setVisibility(View.VISIBLE);
        url2_youtube.setVisibility(View.VISIBLE);
        url3_youtube.setVisibility(View.VISIBLE);
        videourl_youtube.setVisibility(View.VISIBLE);
        videourl_videoview.setVisibility(View.VISIBLE);
        btnKnowyourself.setEnabled(true);
        btnKnowyourself.setBackgroundColor(getResources().getColor(R.color.active_orange));
        btnKnowyourself.setTextColor(getResources().getColor(R.color.white));
    }


    public void buySubscriptionDialog(){

        new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation")
                .setMessage("You don't have any subscription plan. Do you  want to proceed to purchase a plan ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                      //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

                            Navigation.findNavController(rootView).navigate(R.id.action_navigation_course_detail_to_navigation_pricing_plans);

                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                      //  Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }}).show();

        }

}