package com.abt.skillzage.ui.announcements;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.R;
import com.abt.skillzage.adapter.AchievementListAdapter;
import com.abt.skillzage.adapter.AnnouncementListAdapter;
import com.abt.skillzage.ui.achievementlist.model.AchievementModel;
import com.abt.skillzage.ui.announcements.model.AnnouncementModel;
import com.abt.skillzage.ui.announcements.util.ISO8601;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AnnouncementFragment extends Fragment {

    private AnnouncementViewModel mViewModel;

    private View rootView;
    private RecyclerView annoucementList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.announcement_fragment, container, false);
        annoucementList = rootView.findViewById(R.id.annoucementList);
        annoucementList.setLayoutManager(new LinearLayoutManager(getActivity()));
        annoucementList.setNestedScrollingEnabled(false);
        getregisteredEvent();

        return rootView;
    }


    public void doGetAllAnnouncements() {

        String url = getResources().getString(R.string.baseurl4)+"api/announcements?page=0&size=600";
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
                    List<AnnouncementModel> listAnnouncement = new ArrayList<>();
                    JSONArray mainarr = new JSONArray(responseString);


                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");

                    for(int t=0; t < mainarr.length();t++){
                        String evdt ="";
                        if(mainarr.getJSONObject(t).getString("calendar") != null){
                            evdt = mainarr.getJSONObject(t).getString("calendar").split("T")[0];
                        }
                        Date eventdt = df1.parse(evdt);
                        System.out.println(eventdt+"  Data compare ::  "+new Date()+ "  compare :::  "+eventdt.after(new Date()));
                        if(eventdt.after(new Date())){
                            AnnouncementModel announcementModel = new AnnouncementModel();
                            announcementModel.setId(String.valueOf(mainarr.getJSONObject(t).getInt("id")) );
                            announcementModel.setAnnouncementtitle(mainarr.getJSONObject(t).getString("announcementTitle"));
                            announcementModel.setDescription(mainarr.getJSONObject(t).getString("announcementDescription"));

                            Calendar caldt = ISO8601.toCalendar(mainarr.getJSONObject(t).getString("calendar"));
                            SimpleDateFormat format1 = new SimpleDateFormat("EEE, d MMM yyyy");
                            String formatted_calender_dt = format1.format(caldt.getTime());
                            System.out.println(formatted_calender_dt);
                            announcementModel.setDate(formatted_calender_dt);
                            announcementModel.setEvent_reg_date(mainarr.getJSONObject(t).getString("calendar"));
                            listAnnouncement.add(announcementModel);
                        }
                    }

                    System.out.println(listAnnouncement.size()+"    list size :::   ");
                    AnnouncementListAdapter adapter = new AnnouncementListAdapter();
                    adapter.listAnnouncement = listAnnouncement;
                    adapter.announcementids = announcementids;
                    adapter.context = getActivity();
                    annoucementList.setAdapter(adapter);


                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    List<String>  announcementids = new ArrayList<>();
    public void getregisteredEvent() {

        String announcementregurl = getResources().getString(R.string.baseurl4)+"api/listannouncements-event-registrations";
        System.out.println("  Profile url ::  "+announcementregurl);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getActivity() , announcementregurl,  new TextHttpResponseHandler() {
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
                    announcementids.clear();
                    progressDialog.dismiss();
                    JSONArray mainarr = new JSONArray(responseString);
                    for(int t =0; t < mainarr.length();t++){
                        if(SharedPrefUtil.with(getActivity()).getString("userid","").equalsIgnoreCase(mainarr.getJSONObject(t).getString("userid"))){
                            announcementids.add(mainarr.getJSONObject(t).getString("announcementId"));
                        }
                    }
                    doGetAllAnnouncements();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }



}