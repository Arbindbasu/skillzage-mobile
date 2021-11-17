package com.abt.skillzage.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.SessionListAdapter;
import com.abt.skillzage.ui.course_detail.model.CourseSessionModel;
import com.google.android.material.card.MaterialCardView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private View rootView;
    //UI ELement
    private AppCompatImageView chartGradientGraphImage;
    private AppCompatImageView chartGradientGraphImage2;
    private AppCompatImageView chartGradientGraphImage3;
    private AppCompatImageView chartGradientGraphImage4;

    private AppCompatButton btnCourse;
    private AppCompatButton btnKnowledgeHub;
    private AppCompatButton btnAnnouncement;
    private AppCompatButton btnProjects;


    private MaterialCardView mygroup;
    private MaterialCardView careerpath;
    private MaterialCardView achievement;
    private MaterialCardView mycalender;
    private LinearLayout myprojectslinear , testurself;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        btnCourse = rootView.findViewById(R.id.btnCourses);
        btnKnowledgeHub = rootView.findViewById(R.id.btnknowledge);
        btnAnnouncement = rootView.findViewById(R.id.btnAnnouncement);
        btnProjects = rootView.findViewById(R.id.btnProjects);
        mygroup = rootView.findViewById(R.id.mygroup);
        achievement = rootView.findViewById(R.id.achievement);
        careerpath = rootView.findViewById(R.id.careerpath);
        testurself = rootView.findViewById(R.id.testurself);
        mycalender = rootView.findViewById(R.id.mycalender);
        chartGradientGraphImage = rootView.findViewById(R.id.chartGradientGraphImage1);
        chartGradientGraphImage2 = rootView.findViewById(R.id.chartGradientGraphImage2);
        chartGradientGraphImage3 = rootView.findViewById(R.id.chartGradientGraphImage3);
        chartGradientGraphImage4 = rootView.findViewById(R.id.chartGradientGraphImage4);
        Picasso.get().load("http://skillsage.peeqer.com/img/ChartLine.png")
                .into(chartGradientGraphImage);
        Picasso.get().load("http://skillsage.peeqer.com/img/ChartCircle.png")
                .into(chartGradientGraphImage2);
        Picasso.get().load("http://skillsage.peeqer.com/img/ChartLine.png")
                .into(chartGradientGraphImage3);
        Picasso.get().load("http://skillsage.peeqer.com/img/ChartCircle.png")
                .into(chartGradientGraphImage4);

        myprojectslinear = rootView.findViewById(R.id.myprojectslinear);
        myprojectslinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setProject_navigation_status("MYPROJECTS");
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_projects);
            }
        });

        mycalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_my_calender);
            }
        });

        testurself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doGetQuestionSetForHome();


            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), s -> {

        });

        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_course);
            }
        });

        btnKnowledgeHub.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_knowledgehub));

        btnProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setProject_navigation_status("ALLPROJECTS");
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_projects);
            }
        });
      //  btnProjects.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_projects));

        btnAnnouncement.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_announcements));

        mygroup.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_chatgroup));

        careerpath.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_careerpath));

        achievement.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_achievement));

        return rootView;
    }


    public void doGetQuestionSetForHome() {

        String url = getResources().getString(R.string.baseurl2)+"api/question-sets?page=0&size=500";
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
                    boolean st = false;
                    for(int t=0; t < mainarr.length();t++){
                        if(mainarr.getJSONObject(t).getString("questionSetType").equalsIgnoreCase("HOME_TESTYOURSELF")){
                            ((MainActivity)getActivity()).setQuizid(mainarr.getJSONObject(t).getInt("id"));
                            System.out.println("  Home page qus id :::   "+mainarr.getJSONObject(t).getInt("id"));
                            ((MainActivity)getActivity()).setIsnavigatefromhome(true);
                            st = true;
                        }
                    }


                    if(st){
                        Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_test_your_self);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



}