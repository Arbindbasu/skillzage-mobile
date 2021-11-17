package com.abt.skillzage.ui.knowledge;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.adapter.ProjectListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.announcements.util.ISO8601;
import com.abt.skillzage.ui.knowledge.model.Institution;
import com.abt.skillzage.ui.project.model.ProjectModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class KnowledgeHubFragment extends Fragment {

    private View rootView;
    private AppCompatImageView instituteBanner;
    private AppCompatImageView institution_logo;
    androidx.appcompat.widget.AppCompatTextView institutionname , institutiondesc;


    private RecyclerView institutionCourseList;
    private RecyclerView projectsList;
    private List<ProjectModel> listProject = new ArrayList<>();
    private List<Institution> listInstitution = new ArrayList<>();
    private ProjectListAdapter projectadapter;
    private CourseListAdapter adapter;
    private List<CourseModel> listCourseModel = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_knowledge_hub, container, false);
        instituteBanner = rootView.findViewById(R.id.instituteBanner);
        institutionCourseList = rootView.findViewById(R.id.institutionCourseList);
        projectsList = rootView.findViewById(R.id.projectsList);
        institution_logo = rootView.findViewById(R.id.institution_logo);
        institutionname = rootView.findViewById(R.id.institutionname);
        institutiondesc = rootView.findViewById(R.id.institutiondesc);

        Picasso.get().load("http://skillsage.peeqer.com/img/instbg.png").into(instituteBanner);
        Picasso.get().load("http://skillsage.peeqer.com/img/CLogow.png").into(institution_logo);

        //Project list
        projectsList.setLayoutManager(new LinearLayoutManager(getActivity()));
      //  projectsList.setNestedScrollingEnabled(false);
        projectadapter = new ProjectListAdapter();
        projectadapter.listProject = listProject;
        projectadapter.context = getActivity();
        projectsList.setAdapter(projectadapter);

        //Institution course list
        institutionCourseList.setLayoutManager(new LinearLayoutManager(getActivity()));
     //   institutionCourseList.setNestedScrollingEnabled(false);
        adapter = new CourseListAdapter();
        adapter.listCourseModel = listCourseModel;
        adapter.context = getActivity();
        institutionCourseList.setAdapter(adapter);

        doGetAllProjects();

        return rootView;
    }


    public void doGetUserInstitutionDetails(String instituteid) {

        String url = getResources().getString(R.string.baseurl4)+"api/institutions/"+instituteid;
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
                    Institution institutionModel = new Institution();
                    JSONObject mainarr = new JSONObject(responseString);
                        if (mainarr.getString("id") != null &&
                                !mainarr.getString("id").isEmpty()) {
                                    institutionModel.setId(mainarr.getInt("id"));
                                    institutionModel.setInstitutionName(mainarr.getString("institutionName"));
                                    institutionModel.setInstitutionDescription(mainarr.getString("institutionDescription"));
                                    institutionModel.setInstitutionLogo(mainarr.getString("institutionLogo"));
                                }
                    System.out.println(institutionModel.toString()+"    Insti :::   ");

                        if(institutionModel.getInstitutionName() != null && !institutionModel.getInstitutionName().isEmpty()){
                            institutionname.setText(institutionModel.getInstitutionName());
                        }
                    if(institutionModel.getInstitutionDescription() != null && !institutionModel.getInstitutionDescription().isEmpty()){
                        institutiondesc.setText(institutionModel.getInstitutionDescription());
                    }
                    if(institutionModel.getInstitutionLogo()!= null && !institutionModel.getInstitutionLogo().isEmpty()){
                        institutiondesc.setText(institutionModel.getInstitutionLogo());
                        Picasso.get().load(institutionModel.getInstitutionLogo()).into(institution_logo);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void doGetAllProjects() {

        String url = getResources().getString(R.string.baseurl4)+"api/listProjectManagement";
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
                    listProject.clear();
                    JSONArray mainarr = new JSONArray(responseString);
                    for(int t=0; t < mainarr.length();t++){

//                        Calendar validfr = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("validFrom"));
//                        Calendar validTo = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("validTo"));
//                        if ((validfr.getTime().getTime() <= new Date().getTime()) && (new Date().getTime() <= validTo.getTime().getTime())) {

                            if (mainarr.getJSONObject(t).getString("institutionId") != null &&
                                    !mainarr.getJSONObject(t).getString("institutionId").isEmpty()) {
                                if (null != SharedPrefUtil.with(getActivity()).getString("institutionID", "")) {
                                    if (SharedPrefUtil.with(getActivity()).getString("institutionID", "").equalsIgnoreCase(mainarr.getJSONObject(t).getString("institutionId"))) {
                                        ProjectModel projectModel = new ProjectModel();
                                        projectModel.setId(mainarr.getJSONObject(t).getInt("id"));
                                        projectModel.setProjectName(mainarr.getJSONObject(t).getString("projectName"));
                                        projectModel.setProjectDescription(mainarr.getJSONObject(t).getString("projectDescription"));
                                        projectModel.setCompanyDescription(mainarr.getJSONObject(t).getString("companyDescription"));
                                        projectModel.setCompanyProfile(mainarr.getJSONObject(t).getString("companyProfile"));
                                        projectModel.setCompanyName(mainarr.getJSONObject(t).getString("companyName"));
                                        projectModel.setEmailID(mainarr.getJSONObject(t).getString("emailID"));
//                        if(Integer.pmainarr.getJSONObject(t).get("institutionId") != null){
//                            projectModel.setInstitutionId(mainarr.getJSONObject(t).getInt("institutionId"));
//                        }else{
                                        projectModel.setInstitutionId(0);
//                        }
                                        projectModel.setVideoUrl(mainarr.getJSONObject(t).getString("videoUrl"));
                                        projectModel.setUrl1(mainarr.getJSONObject(t).getString("url1"));
                                        projectModel.setUrl2(mainarr.getJSONObject(t).getString("url2"));
                                        projectModel.setUrl3(mainarr.getJSONObject(t).getString("url3"));
                                        projectModel.setLogo(mainarr.getJSONObject(t).getString("logo"));
                                        projectModel.setValidfrom(mainarr.getJSONObject(t).getString("validFrom"));
                                        projectModel.setValidto(mainarr.getJSONObject(t).getString("validTo"));
                                        listProject.add(projectModel);
                                    }
                                }
                            }

                        }
//                    }
                    System.out.println(listProject.size()+"    Active list size :::   "+listProject.toString());
                    projectadapter.listProject = listProject;
                    projectadapter.notifyDataSetChanged();

                    doGetAllCourses();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
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

                    adapter.listCourseModel = listCourseModel;
                    adapter.notifyDataSetChanged();

                    if(SharedPrefUtil.with(getActivity()).getString("institutionID","") != null &&
                            !SharedPrefUtil.with(getActivity()).getString("institutionID","").isEmpty()){
                        doGetUserInstitutionDetails(SharedPrefUtil.with(getActivity()).getString("institutionID",""));
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}