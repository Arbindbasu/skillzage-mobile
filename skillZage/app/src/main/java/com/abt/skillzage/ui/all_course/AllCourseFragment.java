package com.abt.skillzage.ui.all_course;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.announcements.util.ISO8601;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AllCourseFragment extends Fragment {

    private View rootView;
    private MaterialButton btnCourses , btnAnnouncement;
    private RecyclerView courseList;
    private List<CourseModel> listCourseModel = new ArrayList<>();
    androidx.appcompat.widget.SearchView searchcourse;
    CourseListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_all_course, container, false);
        btnCourses = rootView.findViewById(R.id.btnCourses);
        btnAnnouncement = rootView.findViewById(R.id.btnAnnouncement);
        searchcourse = rootView.findViewById(R.id.searchCourses);
        courseList = rootView.findViewById(R.id.courseList);
        courseList.setLayoutManager(new LinearLayoutManager(getActivity()));
      //  courseList.setNestedScrollingEnabled(false);
        adapter = new CourseListAdapter();
        adapter.listCourseModel = listCourseModel;
        adapter.context = getActivity();
        courseList.setAdapter(adapter);
        doGetAllCourses();

        btnAnnouncement.setOnClickListener(v -> Navigation.findNavController(rootView).navigate(R.id.action_navigation_all_course_to_navigation_announcement));

        searchcourse.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //  filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        btnCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.navigation_course);
            }
        });

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
                    listCourseModel.clear();
                    JSONArray mainarr = new JSONArray(responseString);
                    for(int t=0; t < mainarr.length();t++) {
                        if (mainarr.getJSONObject(t).getString("courseStatus").equalsIgnoreCase("INACTIVE")) {
                            continue;
                        }
                        Calendar validfr = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("validFrom"));
                        Calendar validTo = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("validTo"));
                        if ((validfr.getTime().getTime() <= new Date().getTime()) && (new Date().getTime() <= validTo.getTime().getTime())) {

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
                            listCourseModel.add(courseModel);

                        }

                    }

//                    Collections.sort(listCourseModel, new Comparator<CourseModel>() {
//                        public int compare(CourseModel o1, CourseModel o2) {
//                            // compare two instance of `Score` and return `int` as result.
//                            return o2.getCourseId() - o1.getCourseId();
//                        }
//                    });

                    adapter.listCourseModel = listCourseModel;
                    adapter.notifyDataSetChanged();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void filter(String text){
        List<CourseModel> temp = new ArrayList();
        for(CourseModel d: listCourseModel){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            System.out.println( "   search value   is :  "+d.getCourseName().contains(text));
            if(d.getCourseName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        if(text.isEmpty()){
            adapter.listCourseModel = listCourseModel;
        }else{
            adapter.listCourseModel = temp;
        }
        //update recyclerview
        adapter.notifyDataSetChanged();
    }


//Navigation.findNavController(rootView).navigate(R.id.action_navigation_home_to_navigation_announcements)
}