package com.abt.skillzage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.test_yourself.StartDragListener;
import com.abt.skillzage.ui.test_yourself.util.ItemOrderByCallback;

import java.util.ArrayList;
import java.util.Collections;

public class OrderByViewAdapter extends RecyclerView.Adapter<OrderByViewAdapter.MyViewHolder>
        implements ItemOrderByCallback.ItemTouchHelperContract{

    private final StartDragListener mStartDragListener;
    private ArrayList<String> data;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView orderby_option;
        View rowView;

        public MyViewHolder(View itemView) {
            super(itemView);
            rowView = itemView;
            orderby_option = itemView.findViewById(R.id.orderby_option);
        }
    }

    public OrderByViewAdapter(ArrayList<String> data ,
                              StartDragListener startDragListener,
                              Context context) {
        this.data = data;
        mStartDragListener = startDragListener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderby_item_options, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.orderby_option.setText(data.get(position));
        holder.orderby_option.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() ==
                        MotionEvent.ACTION_DOWN) {
                    mStartDragListener.requestDrag(holder);
                }
                return false;
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public ArrayList<String> getDataModel(){
        return data;
    }


    @Override
    public void onRowMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(data, i, i + 1);
            }
            System.out.println(" if**** sorted list :  :::   "+data );
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(data, i, i - 1);
            }
            System.out.println(" else*** sorted list :  :::   "+data );
        }

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.GRAY);

    }

    @Override
    public void onRowClear(MyViewHolder myViewHolder) {
        myViewHolder.rowView.setBackgroundColor(Color.WHITE);

    }

}
