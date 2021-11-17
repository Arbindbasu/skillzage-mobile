package com.abt.skillzage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.R;

public class ExperienceListAdapter extends RecyclerView.Adapter<ExperienceListAdapter.ExperienceListViewHolder> {

    private Context context;

    public ExperienceListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ExperienceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.experience_row_item, parent, false);
        return new ExperienceListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienceListViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.title.setText("Bachelor in Engineering");
            holder.year.setText("2014-2018");
        }else{
            holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.grey));
            holder.title.setText("MBA Finance");
            holder.year.setText("2018-2020");
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class ExperienceListViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout layout;
        private AppCompatTextView title;
        private AppCompatTextView year;

        public ExperienceListViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            title = itemView.findViewById(R.id.title);
            year = itemView.findViewById(R.id.year);
        }
    }
}
