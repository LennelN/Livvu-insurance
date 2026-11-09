package com.moyet.livvuinsurance;


import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


public class FragmentTransferAmountContribution extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    FragmentActionListener fragmentActionListener;
    TextView welcomeMsg;
    TextView sendLink;
    TextView cancelLink;
    private static final String TAG = "TranAmtContrFrag";
    boolean flag = false;
    View rootView;
    EditText etMenuChoice;
    String firstName;
    String lastName;
    String userName;
    String phone_no;
    int no_of_points;
    String amount;
    String message,ip,session_id,country,transfer_country;

    public FragmentTransferAmountContribution() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            transfer_country= bundle.getString(FragmentActionListener.KEY_TRANSFER_COUNTRY);
        }
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        //welcomeMsg.setText(activity.getResources().getString(R.string.welcome_space_2) + firstName + " " + lastName);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            firstName = bundle.getString(FragmentActionListener.KEY_NAME);
            lastName = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
            userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
            phone_no = bundle.getString(FragmentActionListener.KEY_PHONE_NO);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            transfer_country= bundle.getString(FragmentActionListener.KEY_TRANSFER_COUNTRY);
        }

        rootView = inflater.inflate(R.layout.fragment_transfer_amount, container, false);
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


        welcomeMsg = rootView.findViewById(R.id.tvWelcomeMsg);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        etMenuChoice.setFocusable(true);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        welcomeMsg.setText((activity.getResources().getString(R.string.enter_transfer_amount)));
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 4);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "IndividualMainMenu");
                    bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                    bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, userName);
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }

            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
                double amount_entered = Double.parseDouble(etMenuChoice.getText().toString());
                amount = etMenuChoice.getText().toString();
                if (amount_entered <= 0) {
                    Dialog epicDialog = null;

                    if(getContext() != null && isAdded()) {
                        epicDialog = new Dialog(getContext());
                    }else{
                        context = getContextNullSafety();
                        epicDialog = new Dialog(context);
                    }

                    if (getActivity() != null && isAdded()){
                        activity=getActivity();
                    }else{
                        activity=getActivityNullSafety();
                    }
                    epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                    TextView txtview = epicDialog.findViewById(R.id.message_text);
                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                    txtview.setText(activity.getResources().getString(R.string.amount_must_be_greater_than_zero));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });

                } else {

                    //no_of_points = pa(etMenuChoice.getText().toString());
                    message = activity.getResources().getString(R.string.are_you_sure_you_want_to_transfer) +
                            "  " + amount + " " +
                            activity.getResources().getString(R.string.points_to_phone_number) + "  " + phone_no;

                    if (fragmentActionListener != null) {
                        Bundle bundle = new Bundle();

                        bundle.putInt(FragmentActionListener.ACTION_KEY, 187);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_NAME, firstName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, lastName);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                        bundle.putString(FragmentActionListener.KEY_TRANSFER_AMOUNT, amount);
                        bundle.putString(FragmentActionListener.KEY_TRANSFER_COUNTRY, transfer_country);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "TransferAmountContributionFrag");
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ConfirmTransferAmountContributionFrag");
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }
                }
            }
        });
    }

    private TextWatcher registerTextWatcher =new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            String choice = etMenuChoice.getText().toString();
            int cleanlength = choice.length();
            if(cleanlength>0) {
                sendLink.setEnabled(true);
                sendLink.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
                sendLink.setEnabled(false);
                sendLink.setTextColor(activity.getResources().getColor(R.color.colorCell));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
