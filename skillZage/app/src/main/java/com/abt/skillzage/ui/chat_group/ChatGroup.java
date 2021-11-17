package com.abt.skillzage.ui.chat_group;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.R;
import com.abt.skillzage.adapter.ChatGrpAdapter;
import com.abt.skillzage.adapter.CourseActiveListAdapter;
import com.abt.skillzage.adapter.CourseHistoryListAdapter;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ChatGroup extends Fragment {

    private ChatGroupViewModel mViewModel;

    public static ChatGroup newInstance() {
        return new ChatGroup();
    }

    private View rootView;
    private AppCompatTextView labelActiveChats;
    private AppCompatTextView labelOldChats;
    private RecyclerView activeChatGrpList;
    private RecyclerView oldChatGrpList;
    private boolean isActiveChatVisible = true;
    private boolean isOldChatVisible = false;
    List<CourseModel> listCourseModel_mycourses = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chat_group_fragment, container, false);
        labelActiveChats = rootView.findViewById(R.id.labelActiveChats);
        labelOldChats = rootView.findViewById(R.id.labelOldChats);
        activeChatGrpList = rootView.findViewById(R.id.activeChartGrpList);
        oldChatGrpList = rootView.findViewById(R.id.oldChatGrpList);

        labelActiveChats.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getActivity(),R.drawable.ic_arrow_up), null);
        activeChatGrpList.setLayoutManager(new LinearLayoutManager(getActivity()));

        activeChatGrpList.setVisibility(View.VISIBLE);

        oldChatGrpList.setLayoutManager(new LinearLayoutManager(getActivity()));
        //oldChatGrpList.setAdapter(new ChatGrpAdapter());

        labelActiveChats.setOnClickListener(v -> {
            if (isActiveChatVisible) {
                isActiveChatVisible = false;
                activeChatGrpList.setVisibility(View.GONE);
                labelActiveChats.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getActivity(),R.drawable.ic_arrow_down), null);
            } else {
                isActiveChatVisible = true;
                activeChatGrpList.setVisibility(View.VISIBLE);
                labelActiveChats.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getActivity(),R.drawable.ic_arrow_up), null);
            }
        });

        labelOldChats.setOnClickListener(v -> {
            if (isOldChatVisible) {
                isOldChatVisible = false;
                oldChatGrpList.setVisibility(View.GONE);
                labelOldChats.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getActivity(),R.drawable.ic_arrow_down), null);
            } else {
                isOldChatVisible = true;
                oldChatGrpList.setVisibility(View.VISIBLE);
                labelOldChats.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(getActivity(),R.drawable.ic_arrow_up), null);
            }
        });

        checkUserCourses();

        return rootView;
    }

    public void doGetAllCourses() {

        String url = getResources().getString(R.string.baseurl3)+"api/courses-managements?page=0&size=500";
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
                        CourseModel courseModel = new CourseModel();
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


                        if(enrolledcourses.contains(String.valueOf(mainarr.getJSONObject(t).getInt("id")))) {
                            listCourseModel_mycourses.add(courseModel);
                        }


                        // }
                    }


                    ChatGrpAdapter adapter = new ChatGrpAdapter();
                    adapter.listCourseModel_mycourses = listCourseModel_mycourses;
                    adapter.context = getActivity();
                    activeChatGrpList.setAdapter(adapter);


                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    List<String> enrolledcourses = new ArrayList<>();
    List<String> coursesHistory = new ArrayList<>();

    public void checkUserCourses() {

        String url = getResources().getString(R.string.baseurl3)+"api/user-activities?page=0&size=500";
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
                                    if(mainobj.getInt("instituteId") == 1 || mainobj.getInt("instituteId") == -1){
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