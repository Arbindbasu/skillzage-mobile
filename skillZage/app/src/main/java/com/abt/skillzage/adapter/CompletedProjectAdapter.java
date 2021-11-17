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
import com.abt.skillzage.ui.project.model.ProjectModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CompletedProjectAdapter extends RecyclerView.Adapter<CompletedProjectAdapter.CompletedProjectViewHolder> {
    public List<ProjectModel> listProject = new ArrayList<>();
    public Context context;

    @NonNull
    @Override
    public CompletedProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_layout, parent, false);
        return new CompletedProjectViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedProjectViewHolder holder, int position) {
        if(listProject.get(position).getLogo() != null && !listProject.get(position).getLogo().equalsIgnoreCase("null")
                && !listProject.get(position).getLogo().isEmpty()){
            if(position%2 == 0){
                Picasso.get().load(listProject.get(position).getLogo()).into(holder.projectImg);
            }else{
                Picasso.get().load(listProject.get(position).getLogo()).into(holder.projectImg);
            }
        }

        holder.projectDesc.setText(listProject.get(position).getProjectDescription());
        holder.projectDetailsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setProjectModel(listProject.get(holder.getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_navigation_project_detail_to_navigation_project_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProject.size();
    }

    public class CompletedProjectViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView projectImg;
        private androidx.appcompat.widget.AppCompatTextView projectDesc , projectDetailsView;

        public CompletedProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            projectImg = itemView.findViewById(R.id.projectImg);
            projectDesc = itemView.findViewById(R.id.projectDesc);
            projectDetailsView = itemView.findViewById(R.id.projectDetailsView);
        }
    }
}
