package com.moyet.livvuinsurance;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.autofill.AutofillValue;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class fragment_reports_main_menu extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    private TextView welcomeMsg;
    private TextView sendLink;
    private TextView cancelLink;
    private static final String TAG = "repMainMenuFrag";
    boolean flag = false;
    View rootView;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    EditText etMenuChoice;
    FragmentActionListener fragmentActionListener;
    String userName,phoneNo,userRole,errorMessage,URL,firstName,ip,sending_frag;
    String welcomeMessage;
    String user_login_key,nameOfUser,user_role,user_login_id,country,phone_number,session_id,reportNo;

    public fragment_reports_main_menu() {
        // Required empty public constructor
    }


    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            fragmentActionListener = (MainActivity) getActivity();
        }

        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }else{
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_number = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }else{
                firstName = bundle.getString(FragmentActionListener.KEY_NAME);
                phoneNo = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                userName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                userRole = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
            }
        }
        rootView = inflater.inflate(R.layout.fragment_reports_main_menu, container, false);
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

        welcomeMessage = activity.getResources().getString(R.string.reports_main_menu);

        welcomeMsg.setText(welcomeMessage);
        sendLink = rootView.findViewById(R.id.tvSendHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        etMenuChoice = rootView.findViewById(R.id.etMenuChoice);
        etMenuChoice.addTextChangedListener(registerTextWatcher);
        etMenuChoice.requestFocus();
        etMenuChoice.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    if(sending_frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 43);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAdminMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, userName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phoneNo);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, userName);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, userRole);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }
            }
        });

        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                etMenuChoice = rootView.findViewById(R.id.etMenuChoice);

                final int choice = Integer.parseInt(etMenuChoice.getText().toString());
                switch (choice){

                    case 1:
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 21);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantDetailsFragment");
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 21);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "BillMerchantDetailsFragment");
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 2:

                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 30);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CashOutDetailsFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;


                    case 3:

                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 41);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PaymentDetailsFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 41);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PaymentDetailsFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 4:
                        reportNo = "1";
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "NoOfAdmissionsMerchantCountFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "NoOfAdmissionsMerchantCountFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 5:
                        reportNo = "2";
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "NoOfPatientsAttendedFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "NoOfPatientsAttendedFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 6:
                        reportNo = "3";
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CostOfAdmissionsFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CostOfAdmissionsFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 7:
                        reportNo = "4";
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CostOfMedicinesMerchantReportFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 148);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CostOfMedicinesMerchantReportFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 8:
                        reportNo = "5";
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 162);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TariffCostReportFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 162);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TariffCostReportFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;

                    case 9:
                        if (fragmentActionListener!=null) {
                            if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 58);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminReportsFrag");
                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_number);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 58);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminReportsFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }

                        }
                        break;


                    default:

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


                            epicDialog.setContentView(R.layout.dialog_my_custom_message);
                            TextView txtview = epicDialog.findViewById(R.id.message_text);
                            TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                            txtview.setText(activity.getResources().getString(R.string.invalid_choice));

                            epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                }
                            });


                        break;

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
