package com.abt.skillzage.ui.payment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.abt.skillzage.EmailAuthenticationScreen;
import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.payment.model.PaymentGatewayModel;
import com.abt.skillzage.ui.pricing_plans.model.SubscriptionPlan;
import com.abt.skillzage.util.Utils;
import com.abt.skillzage.widget.SharedPrefUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class PaymentFragment extends Fragment {

    private PaymentViewModel mViewModel;
    private com.google.android.material.textfield.TextInputEditText editUsername , editEmail , editPhone ;
    private androidx.appcompat.widget.AppCompatTextView planprice;
    com.google.android.material.button.MaterialButton btnPay;
    private SubscriptionPlan planSubs;

    public static PaymentFragment newInstance() {
        return new PaymentFragment();
    }

    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.payment_fragment, container, false);
        editUsername = rootView.findViewById(R.id.editUsername);
        editEmail = rootView.findViewById(R.id.editEmail);
        editPhone = rootView.findViewById(R.id.editPhone);
        planprice = rootView.findViewById(R.id.planprice);
        btnPay = rootView.findViewById(R.id.btnPay);
        if(((MainActivity)getActivity()).getPlanSubs() != null){
            planSubs = ((MainActivity)getActivity()).getPlanSubs();

            editUsername.setText(SharedPrefUtil.with(getActivity()).getString("name",""));
            editEmail.setText(SharedPrefUtil.with(getActivity()).getString("userid",""));
            editPhone.setText(SharedPrefUtil.with(getActivity()).getString("phoneNumber",""));
            planprice.setText(Utils.getCurrencySymbol("INR")   + "  "+planSubs.getAmount());
        }else{
            Toast.makeText(getActivity()  , "   data not found " , Toast.LENGTH_LONG).show();
        }
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                PaymentGatewayModel gateway = new PaymentGatewayModel();
                gateway.setEmail(SharedPrefUtil.with(getActivity()).getString("userid",""));
                gateway.setCustomerName(SharedPrefUtil.with(getActivity()).getString("name",""));
                gateway.setPhoneNumber(SharedPrefUtil.with(getActivity()).getString("phoneNumber",""));
                gateway.setAmount(planSubs.getAmount());
                int subscriptionid = ((MainActivity)getActivity()).getPlanSubs().getId();
                JSONObject profileobj = ((MainActivity)getActivity()).getProfile();
                ((MainActivity)getActivity()).setGateway(gateway);
                Intent ProceedToPay_intent = new Intent(getActivity() , ProceedToPay.class);
                ProceedToPay_intent.putExtra("subscriptionmodel",gateway);
                ProceedToPay_intent.putExtra("subscriptionid",subscriptionid);
                ProceedToPay_intent.putExtra("userprofile",profileobj.toString());
                ProceedToPay_intent.putExtra("subscriptionduration",((MainActivity)getActivity()).getPlanSubs().getSubscriptionduration());
                ProceedToPay_intent.putExtra("subscriptionname",((MainActivity)getActivity()).getPlanSubs().getSubscriptionName());
                startActivity(ProceedToPay_intent);

            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        // TODO: Use the ViewModel
    }






}