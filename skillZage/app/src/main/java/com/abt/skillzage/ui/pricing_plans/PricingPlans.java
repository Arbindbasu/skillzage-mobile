package com.abt.skillzage.ui.pricing_plans;

import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abt.skillzage.R;
import com.abt.skillzage.adapter.PricingPlanListAdapter;
import com.abt.skillzage.ui.pricing_plans.model.SubscriptionPlan;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PricingPlans extends Fragment {

    private PricingPlansViewModel mViewModel;
    androidx.recyclerview.widget.RecyclerView pricingplanList;

    public static PricingPlans newInstance() {
        return new PricingPlans();
    }

    public View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.pricing_plans_fragment, container, false);
        pricingplanList = rootView.findViewById(R.id.pricingplanList);
        pricingplanList.setLayoutManager(new LinearLayoutManager(getActivity()));
       // pricingplanList.setNestedScrollingEnabled(false);

        getAllSubscriptionPlans();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PricingPlansViewModel.class);
        // TODO: Use the ViewModel
    }


    public void getAllSubscriptionPlans() {

        String url = getResources().getString(R.string.baseurl3)+"api/subscription-managements";
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading....Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Error  Response from server recved is   " + responseString);
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Error in make your Request . Please try again.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                System.out.println(" Success Response from server recved is   " + responseString);
                try {
                    progressDialog.dismiss();
                    JSONArray mainarr = new JSONArray(responseString);
                    String userid = SharedPrefUtil.with(getActivity()).getString("userid" , "");
                    List<SubscriptionPlan> listSubscriptionPlan = new ArrayList<>();
                    for(int t =0; t < mainarr.length(); t++){
                        JSONObject mainobj = mainarr.getJSONObject(t);
                        SubscriptionPlan plan = new SubscriptionPlan();
                        plan.setAmount(mainobj.getString("amount"));
                        plan.setSubscriptionDescription(mainobj.getString("subscriptionDescription"));
                        plan.setSubscriptionStatus(mainobj.getString("subscriptionStatus"));
                        plan.setSubscriptionType(mainobj.getString("subscriptionType"));
                        plan.setSubscriptionName(mainobj.getString("subscriptionName"));
                        plan.setDiscountPercentage(mainobj.getString("discountPercentage"));
                        plan.setId(mainobj.getInt("id"));
                        plan.setSubscriptionduration(mainobj.getString("paymentStatus"));
                        listSubscriptionPlan.add(plan);
                    }
                    PricingPlanListAdapter  adapter = new PricingPlanListAdapter();
                    adapter.listSubscriptionPlan = listSubscriptionPlan;
                    adapter.context = getActivity();
                    pricingplanList.setAdapter(adapter);


                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

}