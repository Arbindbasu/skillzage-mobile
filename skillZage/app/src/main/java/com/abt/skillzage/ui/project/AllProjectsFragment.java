package com.abt.skillzage.ui.project;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CourseActiveListAdapter;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.adapter.ProjectListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.project.model.ProjectModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AllProjectsFragment extends Fragment {

    private View rootView;
    private CourseListAdapter courseListAdapter;
    private RecyclerView projectList;
    private SearchView searchProjects;
    private MaterialButton btnAllCourses , btnAnnouncement , btnknowledge;
    List<ProjectModel> listProject = new ArrayList<>();
    ProjectListAdapter projectadapter;
    private List<String> listEnrolledProjects = new ArrayList<>();
    private List<String> listUserEnrolledCompletedProjects = new ArrayList<>();
    private List<ProjectModel> listAllCompletedProjects = new ArrayList<>();
    private String navigationstatus = "";
    private  androidx.appcompat.widget.AppCompatTextView projectlebel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_all_projects, container, false);

        projectList = rootView.findViewById(R.id.projectList);
        projectList.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchProjects = rootView.findViewById(R.id.searchProjects);
        btnAllCourses = rootView.findViewById(R.id.btnCourses);
        btnknowledge = rootView.findViewById(R.id.btnknowledge);
        if(SharedPrefUtil.with(getActivity()).getString("role","").equalsIgnoreCase("b2c")){
            btnknowledge.setVisibility(View.GONE);
        }
        btnknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_project_to_navigation_knowledgehub);
            }
        });
        btnAnnouncement = rootView.findViewById(R.id.btnAnnouncement);
        navigationstatus = ((MainActivity)getActivity()).getProject_navigation_status();
        projectlebel = rootView.findViewById(R.id.projectlebel);
        projectadapter = new ProjectListAdapter();
        projectadapter.listProject = listProject;
        projectadapter.context = getActivity();
        projectList.setAdapter(projectadapter);

        if(!navigationstatus.isEmpty()){
            if(navigationstatus.equalsIgnoreCase("ALLPROJECTS")){
                projectlebel.setText("All Projects");
            }else if(navigationstatus.equalsIgnoreCase("MYPROJECTS")) {
                projectlebel.setText("My Projects");
            }
        }

        checkUserEnrolledForProject();

        searchProjects.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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

        btnAllCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_project_to_navigation_allcourse);
            }
        });

        btnAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_project_to_navigation_announcements);
            }
        });

        btnknowledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_navigation_project_to_navigation_knowledgehub);
            }
        });

        return rootView;
    }

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
                                    if(mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("JOIN")){
                                        listEnrolledProjects.add(mainobj.getString("projectID"));
                                    }
                                    if (mainobj.getString("projectEnrollmentStatus").equalsIgnoreCase("COMPLETE")) {
                                        listUserEnrolledCompletedProjects.add(mainobj.getString("projectID"));
                                    }
                                }
                            }else{

                            }
                        }else{

                        }
                    }

                    System.out.println(" All enrolled courses ::::::::      "+listEnrolledProjects);
                    doGetAllProjects();
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

                        if(listUserEnrolledCompletedProjects.contains(String.valueOf(mainarr.getJSONObject(t).getInt("id")))){
                            listAllCompletedProjects.add(projectModel);
                        }
                        if(!navigationstatus.isEmpty()){
                            if(navigationstatus.equalsIgnoreCase("ALLPROJECTS")){
                                    listProject.add(projectModel);

                            }else if(navigationstatus.equalsIgnoreCase("MYPROJECTS")){
                                if(listEnrolledProjects.contains(String.valueOf(mainarr.getJSONObject(t).getInt("id")))){
                                    listProject.add(projectModel);
                                }
                            }else{
                                listProject.add(projectModel);
                            }
                        }else{
                            listProject.add(projectModel);
                        }
                    }
                    System.out.println(listProject.size()+"    Active list size :::   "+listProject.toString());
                    projectadapter.listProject = listProject;
                    projectadapter.notifyDataSetChanged();

                    ((MainActivity)getActivity()).setListAllCompletedProjects(listAllCompletedProjects);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void filter(String text){
        List<ProjectModel> temp = new ArrayList();
        for(ProjectModel d: listProject){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            System.out.println( "   search value   is :  "+d.getProjectName().contains(text));
            if(d.getProjectName().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        if(text.isEmpty()){
            projectadapter.listProject = listProject;
           // projectList.setAdapter(adapter);
        }else{
            projectadapter.listProject = temp;
          //  projectList.setAdapter(adapter);
        }
        //update recyclerview
        projectadapter.notifyDataSetChanged();
    }


}