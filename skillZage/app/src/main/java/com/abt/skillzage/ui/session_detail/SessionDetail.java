package com.abt.skillzage.ui.session_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.abt.skillzage.R;
import com.squareup.picasso.Picasso;

public class SessionDetail extends Fragment {

    private SessionDetailViewModel mViewModel;

    public static SessionDetail newInstance() {
        return new SessionDetail();
    }

    View rootView;
    AppCompatImageView sessionimg;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.session_detail_fragment, container, false);
        sessionimg = rootView.findViewById(R.id.sessionimg);

        Picasso.get().load("http://skillsage.peeqer.com/img/courselg.png").into(sessionimg);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SessionDetailViewModel.class);
        // TODO: Use the ViewModel
    }

}