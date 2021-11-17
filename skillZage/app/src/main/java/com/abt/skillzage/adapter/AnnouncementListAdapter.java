package com.abt.skillzage.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.announcements.model.AnnouncementModel;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.google.android.material.button.MaterialButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class AnnouncementListAdapter extends RecyclerView.Adapter<AnnouncementListAdapter.AnnouncementViewHolder> {

    public Context context;
    public List<AnnouncementModel> listAnnouncement = new ArrayList<>();
    public List<String>  announcementids = new ArrayList<>();

    @NonNull
    @Override
    public AnnouncementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_row_item, parent, false);
        return new AnnouncementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnouncementViewHolder holder, int position) {

        holder.announcementTitle.setText(listAnnouncement.get(holder.getAdapterPosition()).getAnnouncementtitle());
        holder.announcementDesc.setText(listAnnouncement.get(holder.getAdapterPosition()).getDescription());
        holder.announcementDt.setText("Date: "+listAnnouncement.get(holder.getAdapterPosition()).getDate());
       System.out.println(" announcement  list ids :::::::::::    "+announcementids);
        if(announcementids.contains(listAnnouncement.get(holder.getAdapterPosition()).getId())){
            holder.btnBookSlot.setEnabled(false);
            holder.btnBookSlot.setBackgroundColor(context.getResources().getColor(R.color.btn_grey));
        }

        holder.btnBookSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Confirmation")
                        .setMessage(" Do you want to book this announcement in your calender ? ")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                doSaveAnnouncementInCalender(listAnnouncement.get(holder.getAdapterPosition()).getId() ,
                                        listAnnouncement.get(holder.getAdapterPosition()).getEvent_reg_date(),holder.btnBookSlot);
                            }})
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }}).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listAnnouncement.size();
    }

    public class AnnouncementViewHolder extends RecyclerView.ViewHolder {

        androidx.appcompat.widget.AppCompatTextView announcementTitle , announcementDesc , announcementDt;
        com.google.android.material.button.MaterialButton btnBookSlot;

        public AnnouncementViewHolder(@NonNull View itemView) {
            super(itemView);
            announcementTitle = itemView.findViewById(R.id.announcementTitle);
            announcementDesc = itemView.findViewById(R.id.announcementDesc);
            announcementDt = itemView.findViewById(R.id.announcementDt);
            btnBookSlot = itemView.findViewById(R.id.btnBookSlot);

        }
    }

    boolean checkregisteredev = false;


    public void doSaveAnnouncementInCalender(String announceid , String evengtdate , MaterialButton btnBook) {

        String announcementregurl = context.getResources().getString(R.string.baseurl4)+"api/announcements-event-registrations";
        System.out.println("  Profile url ::  "+announcementregurl);
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        JSONObject params = new JSONObject();

        StringEntity entity = null;
        try {
            params.put("userid" , SharedPrefUtil.with(context).getString("userid",""));
            params.put("announcementId" , announceid);
            params.put("eventRegistrationDate" , evengtdate);

            entity = new StringEntity(params.toString());
            System.out.println(" REqqqqqqqqqqqqqqqqqqq  "+params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        client.post(context , announcementregurl, entity , "application/json" , new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(context, "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONObject mainobj = new JSONObject(responseString);
                    if(mainobj.has("id")){

                    }
                    Toast.makeText(context , "  Announcement Booked Successfully ", Toast.LENGTH_LONG).show();
                    btnBook.setEnabled(false);
                    btnBook.setBackgroundColor(context.getResources().getColor(R.color.btn_grey));

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
