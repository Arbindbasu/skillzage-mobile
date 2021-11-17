package com.abt.skillzage.ui.achievement;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.adapter.CourseActiveListAdapter;
import com.abt.skillzage.adapter.CourseListAdapter;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.achievement.model.AchievementModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AchievementFragment extends Fragment {

    private AchievementViewModel mViewModel;

    public static AchievementFragment newInstance() {
        return new AchievementFragment();
    }

    private View rootView;
    private AppCompatImageView certificateImage;
    private AppCompatTextView labelCourseName , date , courseDesc;
    private com.abt.skillzage.ui.achievementlist.model.AchievementModel achievementListmodel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.achievement_fragment, container, false);
        certificateImage = rootView.findViewById(R.id.certificateImage);
        labelCourseName = rootView.findViewById(R.id.labelCourseName);
        date = rootView.findViewById(R.id.date);
        courseDesc = rootView.findViewById(R.id.courseDesc);

        achievementListmodel = ((MainActivity)getActivity()).getAchievementListmodel();
        if(achievementListmodel != null ){

            labelCourseName.setText(achievementListmodel.getCertificationTitle());
            courseDesc.setText(achievementListmodel.getCertificateDescription());
            date.setText(achievementListmodel.getDateOfCompletion());
            if(achievementListmodel.getUploadCertificate() != null && !achievementListmodel.getUploadCertificate().equalsIgnoreCase("null")
            && !achievementListmodel.getUploadCertificate().isEmpty()) {
                Picasso.get().load(achievementListmodel.getUploadCertificate()).into(certificateImage);
            }

        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AchievementViewModel.class);
        // TODO: Use the ViewModel
    }


    public void doGetMyAchivements() {

        String url = getResources().getString(R.string.baseurl4)+"api/achievements";
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
                    List<AchievementModel> listAchievement = new ArrayList<>();

                    JSONArray mainarr = new JSONArray(responseString);
                    for(int t=0; t < mainarr.length();t++){

                        AchievementModel achievementModel = new AchievementModel();
                        achievementModel.setId(mainarr.getJSONObject(t).getInt("id"));
                        achievementModel.setCertificateDescription(mainarr.getJSONObject(t).getString("certificateDescription"));
                        achievementModel.setCertificationTitle(mainarr.getJSONObject(t).getString("certificationTitle"));
                        achievementModel.setCertificationType(mainarr.getJSONObject(t).getString("certificationType"));
                        achievementModel.setDateOfCompletion(mainarr.getJSONObject(t).getString("dateOfCompletion"));
                        achievementModel.setUploadCertificate(mainarr.getJSONObject(t).getString("uploadCertificate"));
                        achievementModel.setCertificationScore(mainarr.getJSONObject(t).getInt("certificationScore"));
                        listAchievement.add(achievementModel);

                    }


                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


}