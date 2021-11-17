package com.abt.skillzage.ui.course;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CourseActiveListAdapter;
import com.abt.skillzage.adapter.CourseHistoryListAdapter;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.announcements.util.ISO8601;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CourseFragment extends Fragment {

    private View rootView;
    private CourseListAdapter courseListAdapter;
    private RecyclerView courseListActive;
    private RecyclerView courseListRecommended;
    private RecyclerView courseListHistory;

    private MaterialButton btnAllCourses;
    List<CourseModel> listCourseModel_mycourses = new ArrayList<>();
    List<CourseModel> listCourseModel_recommended = new ArrayList<>();
    List<CourseModel> listCourseModel_course_history = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_course, container, false);

        courseListActive = rootView.findViewById(R.id.courseListActive);
        courseListRecommended = rootView.findViewById(R.id.courseListRecommended);
        courseListHistory = rootView.findViewById(R.id.courseListHistory);

        courseListActive.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseListRecommended.setLayoutManager(new LinearLayoutManager(getActivity()));
        courseListHistory.setLayoutManager(new LinearLayoutManager(getActivity()));

       // courseListActive.setNestedScrollingEnabled(false);
       // courseListRecommended.setNestedScrollingEnabled(false);
       // courseListHistory.setNestedScrollingEnabled(false);
       // courseListActive.setAdapter(new CourseListAdapter());
       // courseListRecommended.setAdapter(new CourseListAdapter());

        courseListHistory.setAdapter(new CourseListAdapter());
        btnAllCourses = rootView.findViewById(R.id.btnAllCourses);
        btnAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_course_to_navigation_allcourse);
            }
        });
        checkUserCourses();
        return rootView;
    }


    public void doGetAllCourses() {

        String url = getResources().getString(R.string.baseurl3)+"api/listCoursesManagement";
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
                    System.out.println(" All enrolled courses *********      "+enrolledcourses);
                    JSONArray mainarr = new JSONArray(responseString);
                    for(int t=0; t < mainarr.length();t++){
                     //   if(mainarr.getJSONObject(t).getInt("id") == 1302) {
                        if(mainarr.getJSONObject(t).getString("courseStatus").equalsIgnoreCase("INACTIVE")){
                            continue;
                        }
                        Calendar validfr = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("validFrom"));
                        Calendar validTo = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("validTo"));
                            if((validfr.getTime().getTime() <= new Date().getTime()) && (new Date().getTime() <= validTo.getTime().getTime())){

                            CourseModel courseModel = new CourseModel();
                            courseModel.setId(mainarr.getJSONObject(t).getInt("id"));
                            courseModel.setCourseId(mainarr.getJSONObject(t).getInt("id"));
                            courseModel.setCourseName(mainarr.getJSONObject(t).getString("courseName"));
                            courseModel.setCourseDescription(mainarr.getJSONObject(t).getString("courseDescription"));
                            courseModel.setCourseObjective(mainarr.getJSONObject(t).getString("courseObjective"));
                            courseModel.setCourseStatus(mainarr.getJSONObject(t).getString("courseStatus"));
                            courseModel.setImagePath(mainarr.getJSONObject(t).getString("imagePath"));
                            courseModel.setRecommendedStatus(mainarr.getJSONObject(t).getString("recommendedStatus"));

                            courseModel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                            courseModel.setUrl1(mainarr.getJSONObject(t).getString("url1"));
                            courseModel.setUrl2(mainarr.getJSONObject(t).getString("url2"));
                            courseModel.setUrl3(mainarr.getJSONObject(t).getString("url3"));

                            courseModel.setQuiza4Course(mainarr.getJSONObject(t).getString("quiza4Course"));
                            courseModel.setQuizb4Course(mainarr.getJSONObject(t).getString("quizb4Course"));

                            if (mainarr.getJSONObject(t).getString("recommendedStatus").equalsIgnoreCase("RECOMMENDED")) {
                                listCourseModel_recommended.add(courseModel);
                            }
                            if(enrolledcourses.contains(String.valueOf(mainarr.getJSONObject(t).getInt("id")))) {
                                listCourseModel_mycourses.add(courseModel);
                            }

                            if(coursesHistory.contains(String.valueOf(mainarr.getJSONObject(t).getInt("id")))) {
                                listCourseModel_course_history.add(courseModel);
                            }

                            }
                      // }
                    }
                    CourseActiveListAdapter adapter = new CourseActiveListAdapter();
                    adapter.listCourseModel = listCourseModel_mycourses;
                    adapter.context = getActivity();
                    courseListActive.setAdapter(adapter);

                    CourseListAdapter adapterrecom = new CourseListAdapter();
                    adapterrecom.listCourseModel = listCourseModel_recommended;
                    adapterrecom.context = getActivity();
                    courseListRecommended.setAdapter(adapterrecom);

                    CourseHistoryListAdapter adaptercoursehistory = new CourseHistoryListAdapter();
                    adaptercoursehistory.listCourseModel = listCourseModel_course_history;
                    adaptercoursehistory.context = getActivity();
                    courseListHistory.setAdapter(adaptercoursehistory);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    List<String> enrolledcourses = new ArrayList<>();
    List<String> coursesHistory = new ArrayList<>();
    public void checkUserCourses() {

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
                    String userid = SharedPrefUtil.with(getActivity()).getString("userid" , "");
                    boolean checksubscription = false;
                    boolean checkEnroll = false;
                    for(int t =0; t < mainarr.length(); t++){
                        JSONObject mainobj = mainarr.getJSONObject(t);
                        if(mainobj.getString("userId") != null &&
                                !mainobj.getString("userId").equalsIgnoreCase("null")){
                            if(mainobj.getString("userId").equalsIgnoreCase(userid) &&
                                    mainobj.getString("subscriptionId") != null &&
                                    !mainobj.getString("subscriptionId").equalsIgnoreCase("null") &&
                                    !mainobj.getString("subscriptionId").isEmpty()){
                                    Toast.makeText(getActivity(), "  User has subscription plan ", Toast.LENGTH_SHORT).show();
                                    if(mainobj.getString("enrollmentStatus") != null &&
                                            !mainobj.getString("enrollmentStatus").equalsIgnoreCase("null") &&
                                            !mainobj.getString("enrollmentStatus").isEmpty()
                                    ){
                                        // To chck course complete or not.....
                                        if(mainobj.getInt("instituteId") == -1){
                                            coursesHistory.add(mainobj.getString("courseId"));
                                        }

                                        if(mainobj.getString("enrollmentStatus").equalsIgnoreCase("Yes")){
                                            enrolledcourses.add(mainobj.getString("courseId"));
                                        }else{

                                        }
                                    }
                            }else{

                            }
                        }else{

                        }
                    }
                    System.out.println(" All enrolled courses ::::::::      "+enrolledcourses);
                    doGetAllCourses();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}