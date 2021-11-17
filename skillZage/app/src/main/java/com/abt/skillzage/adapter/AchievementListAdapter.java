package com.abt.skillzage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.achievement.AchievementViewModel;
import com.abt.skillzage.ui.achievementlist.model.AchievementModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class AchievementListAdapter extends RecyclerView.Adapter<AchievementListAdapter.AchievementListViewHolder> {

    public  Context context;
    public List<AchievementModel> listAchievement = new ArrayList<>();
    public AchievementViewModel viewmodel;



    @NonNull
    @Override
    public AchievementListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_layout_item, parent, false);
        return new AchievementListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementListViewHolder holder, int position) {

        if(listAchievement.get(position).getUploadCertificate() != null && !listAchievement.get(position).getUploadCertificate().equalsIgnoreCase("null")
           && !listAchievement.get(position).getUploadCertificate().isEmpty()){
            listAchievement.get(position).setUploadCertificate(context.getResources().getString(R.string.baseurl4)+"api/download/"+listAchievement.get(position).getUploadCertificate());

            if(position%2 == 0){
                Picasso.get().load(listAchievement.get(position).getUploadCertificate()).into(holder.achievementImg);
            }else{
                Picasso.get().load(listAchievement.get(position).getUploadCertificate()).into(holder.achievementImg);
            }
        }

        holder.achievementTitle.setText(listAchievement.get(position).getCertificationTitle());
        holder.achievementDesc.setText(listAchievement.get(position).getCertificateDescription());
        holder.achievementImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setAchievementListmodel(listAchievement.get(holder.getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_navigation_achievement_list_to_navigation_achievement);
            }
        });
        holder.achievementDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setAchievementListmodel(listAchievement.get(holder.getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_navigation_achievement_list_to_navigation_achievement);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listAchievement.size();
    }

    public class AchievementListViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView achievementImg;
        private androidx.appcompat.widget.AppCompatTextView achievementTitle , achievementDesc , achievementDetailsView;


        public AchievementListViewHolder(@NonNull View itemView) {
            super(itemView);
            achievementImg = itemView.findViewById(R.id.achievementImg);
            achievementTitle = itemView.findViewById(R.id.achievementTitle);
            achievementDesc = itemView.findViewById(R.id.achievementDesc);
            achievementDetailsView = itemView.findViewById(R.id.achievementDetailsView);
        }
    }
}
