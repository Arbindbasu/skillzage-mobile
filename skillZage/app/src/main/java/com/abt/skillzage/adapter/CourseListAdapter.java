package com.abt.skillzage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.widget.CourseViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseListViewHolder> {

    public  Context context;
    public List<CourseModel> listCourseModel = new ArrayList<>();
    public CourseViewModel viewmodel;

    @NonNull
    @Override
    public CourseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_course_layout, parent, false);
        return new CourseListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseListViewHolder holder, int position) {
        if(position%2 == 0){
            Picasso.get().load(listCourseModel.get(position).getImagePath()).into(holder.courseImage);
        }else{
            Picasso.get().load(listCourseModel.get(position).getImagePath()).into(holder.courseImage);
        }
        holder.courseDesc.setText(listCourseModel.get(position).getCourseName()+":"+listCourseModel.get(position).getCourseObjective());
        holder.viewcourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setCourseId(listCourseModel.get(holder.getAdapterPosition()).getCourseId());
                ((MainActivity)context).setCourse_image(listCourseModel.get(holder.getAdapterPosition()).getImagePath());
                ((MainActivity)context).setModel(listCourseModel.get(holder.getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_navigation_all_course_to_navigation_course_detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCourseModel.size();
    }

    public class CourseListViewHolder extends RecyclerView.ViewHolder {

        private AppCompatImageView courseImage;
        private androidx.appcompat.widget.AppCompatTextView courseDesc , viewcourses;


        public CourseListViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.courseImg);
            courseDesc = itemView.findViewById(R.id.courseDesc);
            viewcourses = itemView.findViewById(R.id.btnView);
        }
    }
}
