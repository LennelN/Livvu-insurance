package com.moyet.livvuinsurance;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.braintreepayments.cardform.view.CardForm;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentBrainTreeCardForm extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    View rootView;
    CardForm cardForm;
    private static final String TAG = "CardFormFragment";
    private FragmentBrainTreeCardForm mFragment;
    FragmentActionListener fragmentActionListener;
    Button btnBuy;
    String firstName;
    String lastName;
    String userName;
    String URL;
    String ip,session_id;
    double purchaseAmount;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;

    public FragmentBrainTreeCardForm() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
        }
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            purchaseAmount=bundle.getDouble(FragmentActionListener.KEY_PURCHASE_AMOUNT);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contextNullSafe == null) getContextNullSafety();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            purchaseAmount=bundle.getDouble(FragmentActionListener.KEY_PURCHASE_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //context = getActivity();
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            purchaseAmount=bundle.getDouble(FragmentActionListener.KEY_PURCHASE_AMOUNT);
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;


        rootView = inflater.inflate(R.layout.fragment_brain_tree_card_form, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {

        if (getContext() != null && isAdded()){
            context=getContext();
        }else{
            context=getContextNullSafety();
        }
        if (getActivity() != null && isAdded()){
            activity=getActivity();
        }else{
            activity=getActivityNullSafety();
        }

        cardForm = rootView.findViewById(R.id.card_form);

        btnBuy=rootView.findViewById(R.id.btnBuy);

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS Is Required On This Number")
                .setup(activity);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cardForm.isValid()){

                    if (getContext() != null && isAdded()){
                        context=getContext();
                    }else{
                        context=getContextNullSafety();
                    }
                    if (getActivity() != null && isAdded()){
                        activity=getActivity();
                    }else{
                        activity=getActivityNullSafety();
                    }

                    if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                        builder = new
                                AlertDialog.Builder(context, R.style.MyDialogTheme240);
                    }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                            || screenWidth==720 || screenWidth==1184) {
                        builder = new
                                AlertDialog.Builder(context, R.style.MyDialogTheme16sp);
                    }else if(screenWidth==600 || screenWidth==1024){
                        builder = new
                                AlertDialog.Builder(context, R.style.MyDialogTheme600);
                    }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                        builder = new
                                AlertDialog.Builder(context, R.style.MyDialogTheme720);
                    }else{
                        builder = new
                                AlertDialog.Builder(context, R.style.MyDialogTheme18sp);
                    }
                    builder.setTitle("Confirm Before Purchase");
                    builder.setMessage("Card Number: "+cardForm.getCardNumber()+ "\n" +
                            "Card Expiry Date: "+cardForm.getExpirationDateEditText().getText().toString()+"\n"+
                            "Card CVV: "+cardForm.getCvv()+ "\n"+
                            "Postal Code: "+ cardForm.getPostalCode()+ "\n"+
                            "Phone Number: "+ cardForm.getMobileNumber());
                    builder.setPositiveButton("Confirm",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (getContext() != null && isAdded()){
                                context=getContext();
                            }else{
                                context=getContextNullSafety();
                            }
                            if (getActivity() != null && isAdded()){
                                activity=getActivity();
                            }else{
                                activity=getActivityNullSafety();
                            }
                            Toast.makeText(context,activity.getResources().getString(R.string.thank_you_for_purchase), Toast.LENGTH_LONG).show();

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (fragmentActionListener != null) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                                bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                    });

                    builder.create();
                    builder.show();
                }
            }
        });
    }

    public Context getContextNullSafety() {

        if (getContext() != null) return getContext();
        if (getActivity() != null) return getActivity();
        if (contextNullSafe != null) return contextNullSafe;
        if (getView() != null && getView().getContext() != null) return getView().getContext();
        if (requireContext() != null) return requireContext();
        if (requireActivity() != null) return requireActivity();
        if (requireView() != null && requireView().getContext() != null)
            return requireView().getContext();

        return null;

    }

    /**CALL THIS IF YOU NEED ACTIVITY*/
    public FragmentActivity getActivityNullSafety() {

        if (getContextNullSafety() != null && getContextNullSafety() instanceof FragmentActivity) {
            /*It is observed that if context it not null then it will be
             * the related host/container activity always*/
            return (FragmentActivity) getContextNullSafety();
        }
        return null;
    }


}
