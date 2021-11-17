package com.abt.skillzage.ui.cart;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.abt.skillzage.MainActivity;
import com.abt.skillzage.R;
import com.abt.skillzage.ui.pricing_plans.model.SubscriptionPlan;
import com.abt.skillzage.util.Utils;

public class MyCart extends Fragment {

    private MyCartViewModel mViewModel;
    private com.google.android.material.button.MaterialButton btnCheckout;
    private com.google.android.material.textfield.TextInputEditText editPromocode;
    private androidx.appcompat.widget.AppCompatTextView labelPlanSubs , price , labelPromo ,promoPrice , totalPrice;
    private SubscriptionPlan planSubs;


    public static MyCart newInstance() {
        return new MyCart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_cart_fragment, container, false);
        labelPlanSubs = v.findViewById(R.id.labelPlanSubs);
        price = v.findViewById(R.id.price);
        labelPromo = v.findViewById(R.id.labelPromo);
        promoPrice = v.findViewById(R.id.promoPrice);
        totalPrice = v.findViewById(R.id.totalPrice);
        editPromocode = v.findViewById(R.id.editPromocode);
        btnCheckout = v.findViewById(R.id.btnCheckout);

        if(((MainActivity)getActivity()).getPlanSubs() != null){
            planSubs = ((MainActivity)getActivity()).getPlanSubs();
        }else{
            Toast.makeText(getActivity()  , "  Cart data not found " , Toast.LENGTH_LONG).show();
        }

        if(planSubs != null){
            labelPlanSubs.setText(planSubs.getSubscriptionName());
            price.setText(planSubs.getSubscriptionName());
            totalPrice.setText(Utils.getCurrencySymbol("INR")   + "  "+planSubs.getAmount());
            promoPrice.setText(Utils.getCurrencySymbol("INR")   + "  0");
        }

        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setBuyamount(planSubs.getAmount());
                Navigation.findNavController(v).navigate(R.id.action_navigation_my_cart_to_navigation_payment);

            }
        });


        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyCartViewModel.class);
        // TODO: Use the ViewModel
    }

}