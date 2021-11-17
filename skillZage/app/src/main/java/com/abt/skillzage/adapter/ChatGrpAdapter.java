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
import com.abt.skillzage.model.CourseModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatGrpAdapter extends RecyclerView.Adapter<ChatGrpAdapter.ChatGrpListViewHolder> {

    public Context context;
    public List<CourseModel> listCourseModel_mycourses = new ArrayList<>();

    @NonNull
    @Override
    public ChatGrpListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_course_layout, parent, false);
        return new ChatGrpListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatGrpListViewHolder holder, int position) {

        holder.courseDesc.setText(listCourseModel_mycourses.get(position).getCourseName() +" : "+listCourseModel_mycourses.get(position).getCourseObjective());
        holder.btnView.setText("Chat");
        if(listCourseModel_mycourses.get(position).getImagePath() != null && !listCourseModel_mycourses.get(position).getImagePath().equalsIgnoreCase("null") && !listCourseModel_mycourses.get(position).getImagePath().isEmpty()){
            Picasso.get().load(listCourseModel_mycourses.get(position).getImagePath()).into(holder.courseImage);
        }
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setChatgroupname(listCourseModel_mycourses.get(holder.getAdapterPosition()).getCourseName());
                Navigation.findNavController(v).navigate(R.id.action_navigation_chat_gr_to_navigation_chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCourseModel_mycourses.size();
    }

    public class ChatGrpListViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView courseImage;
        androidx.appcompat.widget.AppCompatTextView courseDesc;
        androidx.appcompat.widget.AppCompatTextView btnView;

        public ChatGrpListViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseDesc = itemView.findViewById(R.id.courseDesc);
            btnView = itemView.findViewById(R.id.btnView);
        }
    }
}
