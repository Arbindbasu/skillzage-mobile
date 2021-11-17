package com.abt.skillzage.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.model.CourseModel;
import com.abt.skillzage.ui.pricing_plans.model.SubscriptionPlan;
import com.abt.skillzage.util.Utils;
import com.abt.skillzage.widget.CourseViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class PricingPlanListAdapter extends RecyclerView.Adapter<PricingPlanListAdapter.PricingPlanListViewHolder> {

    public  Context context;
    public List<SubscriptionPlan> listSubscriptionPlan = new ArrayList<>();
    public CourseViewModel viewmodel;

    @NonNull
    @Override
    public PricingPlanListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pricingplan_item, parent, false);
        return new PricingPlanListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PricingPlanListViewHolder holder, int position) {

        holder.planname.setText(listSubscriptionPlan.get(holder.getAdapterPosition()).getSubscriptionName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.plandescription.setText(Html.fromHtml(listSubscriptionPlan.get(holder.getAdapterPosition()).getSubscriptionDescription(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.plandescription.setText(Html.fromHtml(listSubscriptionPlan.get(holder.getAdapterPosition()).getSubscriptionDescription()));
        }
        holder.plantype.setText(listSubscriptionPlan.get(holder.getAdapterPosition()).getSubscriptionType());
        holder.plancontent.setText(listSubscriptionPlan.get(holder.getAdapterPosition()).getSubscriptionType() + " purchase to  (save "+listSubscriptionPlan.get(holder.getAdapterPosition()).getDiscountPercentage()+" % )");
        holder.planprice.setText(Utils.getCurrencySymbol("INR")   + "  "+  listSubscriptionPlan.get(holder.getAdapterPosition()).getAmount());

        holder.buyplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)context).setPlanSubs(listSubscriptionPlan.get(holder.getAdapterPosition()));
                Navigation.findNavController(v).navigate(R.id.action_navigation_pricing_plans_to_navigation_my_cart);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listSubscriptionPlan.size();
    }

    public class PricingPlanListViewHolder extends RecyclerView.ViewHolder {

        private com.google.android.material.button.MaterialButton buyplan;
        private androidx.appcompat.widget.AppCompatTextView planname , plantype , planprice , plandescription , plancontent;


        public PricingPlanListViewHolder(@NonNull View itemView) {
            super(itemView);
            buyplan = itemView.findViewById(R.id.buyplan);
            planname = itemView.findViewById(R.id.planname);
            plantype = itemView.findViewById(R.id.plantype);
            plandescription = itemView.findViewById(R.id.plandescription);
            plancontent = itemView.findViewById(R.id.plancontent);
            planprice = itemView.findViewById(R.id.planprice);
        }
    }
}
