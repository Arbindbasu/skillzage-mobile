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

public class TermCareerListAdapter extends RecyclerView.Adapter<TermCareerListAdapter.TermCareerViewHolder> {

    private Context context;

    public TermCareerListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public TermCareerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.short_term_career_row_item, parent, false);
        return new TermCareerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TermCareerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class TermCareerViewHolder extends RecyclerView.ViewHolder {

//        private ConstraintLayout layout;

        public TermCareerViewHolder(@NonNull View itemView) {
            super(itemView);
//            layout = itemView.findViewById(R.id.layout);
        }
    }
}
