package com.abt.skillzage.ui.career_path;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.R;
import com.abt.skillzage.adapter.ExperienceListAdapter;
import com.abt.skillzage.adapter.TermCareerListAdapter;
import com.squareup.picasso.Picasso;

public class CareerPath extends Fragment {

    private CareerPathViewModel mViewModel;

    public static CareerPath newInstance() {
        return new CareerPath();
    }

    private View rootView;
    private RecyclerView experienceList;
    private RecyclerView shrtTermCareerGoalsList;
    private RecyclerView longTermCareerGoalsList;
    private AppCompatImageView image1;
    private AppCompatImageView image2;
    private AppCompatImageView graph;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.career_path_fragment, container, false);

        experienceList = rootView.findViewById(R.id.experienceList);
        shrtTermCareerGoalsList = rootView.findViewById(R.id.shrtTermCareerGoalsList);
        longTermCareerGoalsList = rootView.findViewById(R.id.longTermCareerGoalsList);
        image1 = rootView.findViewById(R.id.image1);
        image2 = rootView.findViewById(R.id.image2);
        graph = rootView.findViewById(R.id.graph);

        Picasso.get().load("http://skillsage.peeqer.com/img/course3.png").into(image1);
        Picasso.get().load("http://skillsage.peeqer.com/img/course3.png").into(image2);
        Picasso.get().load("http://skillsage.peeqer.com/img/ChartCircle.png").into(graph);

        experienceList.setLayoutManager(new LinearLayoutManager(getActivity()));
        experienceList.setNestedScrollingEnabled(false);
        experienceList.setAdapter(new ExperienceListAdapter(getActivity()));

        shrtTermCareerGoalsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        shrtTermCareerGoalsList.setNestedScrollingEnabled(false);
        shrtTermCareerGoalsList.setAdapter(new TermCareerListAdapter(getActivity()));

        longTermCareerGoalsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        longTermCareerGoalsList.setNestedScrollingEnabled(false);
        longTermCareerGoalsList.setAdapter(new TermCareerListAdapter(getActivity()));

        return rootView;
    }
}