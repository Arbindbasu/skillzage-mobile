package com.abt.skillzage;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import static androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.achievementlist.model.AchievementModel;
import com.abt.skillzage.ui.chat.model.ChatMsg;
import com.abt.skillzage.ui.chat.util.ObservableChatMsg;
import com.abt.skillzage.ui.course.CourseFragment;
import com.abt.skillzage.ui.payment.model.PaymentGatewayModel;
import com.abt.skillzage.ui.pricing_plans.model.SubscriptionPlan;
import com.abt.skillzage.ui.profile.util.ObservableProfileModel;
import com.abt.skillzage.ui.project.model.ProjectModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private AppCompatImageView btnHome;
    private CourseModel model;
    public int courseId = 0;
    public String course_image = "";
    private CourseFragment courseFragment;
    private BottomNavigationView navView;
    private JSONObject useractivityres;
    private SubscriptionPlan planSubs;
    private JSONObject profile;

    PaymentGatewayModel gateway;
    public PaymentGatewayModel getGateway() {
        return gateway;
    }

    public void setGateway(PaymentGatewayModel gateway) {
        this.gateway = gateway;
    }

    private String buyamount;
    public String getBuyamount() {
        return buyamount;
    }

    public void setBuyamount(String buyamount) {
        this.buyamount = buyamount;
    }

    private String chatgroupname;

    public String getChatgroupname() {
        return chatgroupname;
    }

    public void setChatgroupname(String chatgroupname) {
        this.chatgroupname = chatgroupname;
    }

    public JSONObject getUseractivityres() {
        return useractivityres;
    }

    public void setUseractivityres(JSONObject useractivityres) {
        this.useractivityres = useractivityres;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) {
        this.course_image = course_image;
    }

    public CourseModel getModel() {
        return model;
    }

    public void setModel(CourseModel model) {
        this.model = model;
    }

    public ProjectModel projectModel;

    public ProjectModel getProjectModel() {
        return projectModel;
    }

    public void setProjectModel(ProjectModel projectModel) {
        this.projectModel = projectModel;
    }

    private AchievementModel achievementListmodel;

    de.hdodenhof.circleimageview.CircleImageView btnProfile;

    public AchievementModel getAchievementListmodel() {
        return achievementListmodel;
    }

    public void setAchievementListmodel(AchievementModel achievementListmodel) {
        this.achievementListmodel = achievementListmodel;
    }

    public String project_navigation_status = "0";

    public String getProject_navigation_status() {
        return project_navigation_status;
    }

    public void setProject_navigation_status(String project_navigation_status) {
        this.project_navigation_status = project_navigation_status;
    }

    public SubscriptionPlan getPlanSubs() {
        return planSubs;
    }

    public void setPlanSubs(SubscriptionPlan planSubs) {
        this.planSubs = planSubs;
    }

    private boolean isnavigatefromhome = false, isnavigatefromcourse_knowurself = false, isnavigatefromsession_test_quiz = false, isnavigatefromproject = false , isnavigatefromsession_final_test_urself = false;
    private int quizid=0;
    public boolean isIsnavigatefromhome() {
        return isnavigatefromhome;
    }

    public void setIsnavigatefromhome(boolean isnavigatefromhome) {
        this.isnavigatefromhome = isnavigatefromhome;
    }

    public boolean isIsnavigatefromcourse_knowurself() {
        return isnavigatefromcourse_knowurself;
    }

    public void setIsnavigatefromcourse_knowurself(boolean isnavigatefromcourse_knowurself) {
        this.isnavigatefromcourse_knowurself = isnavigatefromcourse_knowurself;
    }

    public boolean isIsnavigatefromsession_test_quiz() {
        return isnavigatefromsession_test_quiz;
    }

    public void setIsnavigatefromsession_test_quiz(boolean isnavigatefromsession_test_quiz) {
        this.isnavigatefromsession_test_quiz = isnavigatefromsession_test_quiz;
    }

    public boolean isIsnavigatefromproject() {
        return isnavigatefromproject;
    }

    public void setIsnavigatefromproject(boolean isnavigatefromproject) {
        this.isnavigatefromproject = isnavigatefromproject;
    }
    private JSONObject resCheckEnrollment = null;

    public JSONObject getResCheckEnrollment() {
        return resCheckEnrollment;
    }

    public void setResCheckEnrollment(JSONObject resCheckEnrollment) {
        this.resCheckEnrollment = resCheckEnrollment;
    }

    public int getQuizid() {
        return quizid;
    }

    public void setQuizid(int quizid) {
        this.quizid = quizid;
    }

    public boolean isIsnavigatefromsession_final_test_urself() {
        return isnavigatefromsession_final_test_urself;
    }

    public void setIsnavigatefromsession_final_test_urself(boolean isnavigatefromsession_final_test_urself) {
        this.isnavigatefromsession_final_test_urself = isnavigatefromsession_final_test_urself;
    }

    private List<ProjectModel> listAllCompletedProjects = new ArrayList<>();

    public List<ProjectModel> getListAllCompletedProjects() {
        return listAllCompletedProjects;
    }

    public void setListAllCompletedProjects(List<ProjectModel> listAllCompletedProjects) {
        this.listAllCompletedProjects = listAllCompletedProjects;
    }

    private static ObservableProfileModel obsrProfileModel;

    public static ObservableProfileModel getObsrProfileModel() {
        return obsrProfileModel;
    }

    public static void setObsrProfileModel(ObservableProfileModel obsrProfileModel) {
        MainActivity.obsrProfileModel = obsrProfileModel;
    }

    public JSONObject getProfile() {
        return profile;
    }

    public void setProfile(JSONObject profile) {
        this.profile = profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        btnProfile = findViewById(R.id.btnProfile);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Confirmation")
                        .setMessage(" Do you  want to Logout ? ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(MainActivity.this, " Logout successfully ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this , WelcomeScreen.class));
                                SharedPrefUtil.with(MainActivity.this).clearAll().save();
                                finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }}).show();
            }
        });
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_book, R.id.navigation_search, R.id.navigation_notifications, R.id.navigation_profile,
                R.id.navigation_course, R.id.navigation_allcourse, R.id.navigation_knowledgehub, R.id.navigation_announcements,
                R.id.navigation_chatgroup, R.id.navigation_careerpath, R.id.navigation_achievement, R.id.navigation_projet_detail,
                R.id.navigation_course_detail, R.id.navigation_test_ur_self ,
                R.id.navigation_all_projects , R.id.navigation_chat , R.id.navigation_my_calender)
                .build();
        NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(MainActivity.this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });
        obsrProfileModel = new ViewModelProvider(MainActivity.this).get(ObservableProfileModel.class);

        if(SharedPrefUtil.with(MainActivity.this).getString("imagepath","") != null && !SharedPrefUtil.with(MainActivity.this).getString("imagepath","").isEmpty()){
            if(SharedPrefUtil.with(MainActivity.this).getString("imagepath","").contains("/")){
                System.out.println("  Profile img ::::  "+SharedPrefUtil.with(MainActivity.this).getString("imagepath","").split("/")[SharedPrefUtil.with(MainActivity.this).getString("imagepath","").split("/").length - 1]);
                doGetProfileImage(SharedPrefUtil.with(MainActivity.this).getString("imagepath","").split("/")[SharedPrefUtil.with(MainActivity.this).getString("imagepath","").split("/").length - 1]);
               // obsrProfileModel.getCurrentProfileImg().setValue(SharedPrefUtil.with(MainActivity.this).getString("imagepath",""));
            }
        }

        try{
            profile = new JSONObject(getIntent().getStringExtra("profile"));
        }catch(Exception e){
            e.printStackTrace();
        }

        // Create the observer which updates the UI.
        final Observer<String> profileObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String profileimg) {
                // Update the UI, in this case,
                if(profileimg != null && !profileimg.isEmpty() && profileimg.contains("/")){
                    System.out.println("  Profile img ::::  "+profileimg.split("/")[profileimg.split("/").length - 1]);
                    doGetProfileImage(profileimg.split("/")[profileimg.split("/").length - 1]);
                    getObsrProfileModel().getCurrentProfileImg().setValue(profileimg);
                }
            }
        };
        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        obsrProfileModel.getCurrentProfileImg().observe(MainActivity.this, profileObserver);



    }


    public void doGetProfileImage( String filename ) {

        String getuserurl = getResources().getString(R.string.baseurl)+"skillzag/auth/users/download/"+filename;
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Accept", "application/octet-stream");
        client.get(MainActivity.this , getuserurl, new AsyncHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    Bitmap bmp = BitmapFactory.decodeByteArray(responseString, 0, responseString.length);
                    btnProfile.setImageBitmap(bmp);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



}