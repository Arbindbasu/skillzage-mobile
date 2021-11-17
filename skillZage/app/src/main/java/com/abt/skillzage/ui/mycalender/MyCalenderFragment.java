package com.abt.skillzage.ui.mycalender;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.WelcomeScreen;
import com.abt.skillzage.adapter.AnnouncementListAdapter;
import com.abt.skillzage.ui.announcements.model.AnnouncementModel;
import com.abt.skillzage.ui.announcements.util.ISO8601;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ru.cleverpumpkin.calendar.CalendarDate;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyCalenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyCalenderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    CompactCalendarView my_calendar_view;
    MaterialButton btnPrevMnth, btnNextMnth;
    AppCompatTextView monthName;

    String[] months = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};


    public MyCalenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyCalenderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyCalenderFragment newInstance(String param1, String param2) {
        MyCalenderFragment fragment = new MyCalenderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // List of preselected dates that will be initially selected
    List<CalendarDate> preselectedDates = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_calender, container, false);

        my_calendar_view = view.findViewById(R.id.calendar_view);
        btnNextMnth = view.findViewById(R.id.btnNextMnth);
        btnPrevMnth = view.findViewById(R.id.btnPrevMnth);
        monthName = view.findViewById(R.id.monthName);


        my_calendar_view.canScrollHorizontally(0);
        Calendar cal = Calendar.getInstance();
        String month = months[cal.get(Calendar.MONTH)];
        monthName.setText(month + " " + cal.get(Calendar.YEAR));

        btnPrevMnth.setOnClickListener(v -> {
            my_calendar_view.scrollLeft();
        });

        btnNextMnth.setOnClickListener(v -> {
            my_calendar_view.scrollRight();
        });

        my_calendar_view.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = my_calendar_view.getEvents(dateClicked);
                Log.d("EVENTDATE", "Day was clicked: " + dateClicked + " with events " + events.toString());
//                if (dateClicked.toString().compareTo("Fri Oct 21 00:00:00 AST 2016") == 0) {
//                    Toast.makeText(getActivity(), "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
//                }else {
//                    Toast.makeText(getActivity(), "No Events Planned for that day", Toast.LENGTH_SHORT).show();
//                }
                if(events.size() > 0){
                    announcementDialogDetails(events.get(0).getData().toString());
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstDayOfNewMonth);
                monthName.setText(months[firstDayOfNewMonth.getMonth()] + " " + calendar.get(Calendar.YEAR));
            }
        });
        doGetAllAnnouncements();


        return view;
    }
    
    public void announcementDialogDetails(String announcement){
        new AlertDialog.Builder(getActivity())
                .setTitle("Announcement Details")
                .setMessage(announcement)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).show();
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
                    getregisteredEvent(listAnnouncement);

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void getregisteredEvent(List<AnnouncementModel> listAnnouncement) {
        System.out.println(listAnnouncement.toString()+"    Announcement ***** pojo  :::   ");

        String announcementregurl = getResources().getString(R.string.baseurl4) + "api/listannouncements-event-registrations";
        System.out.println("  Profile url ::  " + announcementregurl);
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(getActivity(), announcementregurl, new TextHttpResponseHandler() {
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
                    List<Event> eventList = new ArrayList<>();
                    for (int t = 0; t < mainarr.length(); t++) {
                        String announcementtitle = "";
                        if (SharedPrefUtil.with(getActivity()).getString("userid", "").equalsIgnoreCase(mainarr.getJSONObject(t).getString("userid"))) {
                            String[] evReg = mainarr.getJSONObject(t).getString("eventRegistrationDate").split("-");
                            String year = "0";
                            String month = "0";
                            String date = "0";
                            if (evReg.length > 0) {
                                year = evReg[0];
                                month = evReg[1];
                                date = evReg[1].split("T")[0];
                            }

                            for(int c=0; c < listAnnouncement.size();c++){

                                System.out.println(listAnnouncement.get(c).getId()+"    Announcement  details  :::   "+mainarr.getJSONObject(t).getString("announcementId"));
                                if( listAnnouncement.get(c).getId().equalsIgnoreCase(mainarr.getJSONObject(t).getString("announcementId")) ){
                                    announcementtitle = listAnnouncement.get(c).getAnnouncementtitle();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        System.out.println(listAnnouncement.get(c)+"    Announcement  booked for this user  :::   "+ announcementtitle+":"+listAnnouncement.get(c).getDescription());
                                        eventList.add(new Event(Color.RED, Instant.parse(mainarr.getJSONObject(t).getString("eventRegistrationDate"))
                                                .toEpochMilli(), announcementtitle+":"+listAnnouncement.get(c).getDescription()));
                                    }
                                    break;
                                }
                            }

                            System.out.println(eventList.size() + "    Event List :::   ");

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(date));
                        }
                    }
                    my_calendar_view.addEvents(eventList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}