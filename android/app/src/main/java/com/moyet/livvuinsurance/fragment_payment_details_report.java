package com.moyet.livvuinsurance;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import com.android.volley.toolbox.StringRequest;


public class fragment_payment_details_report extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    private fragment_payment_details_report mFragment;
    private static final String TAG = "PaymentDetFrag";
    private TextView mActionOk, mActionCancel;
    ArrayList<PaymentDetails> itemList;
    ArrayList<String> listViewItems;
    String URL;
    ListView lv;
    listviewCashOutAdapter adapter;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    String transaction_date;
    String no_of_points,startReportDate,endReportDate,reportNo,startReportDateSaved,endReportDateSaved;
    String merchantName;
    String  merchantCode;
    String userName;
    String phone_no,merchantUserName;
    String message;
    Button cancel;
    FragmentActionListener fragmentActionListener;
    String ip,sending_frag,user_login_key,nameOfUser,user_role,user_login_id,country,phone_number,session_id;
    TextView txtHeading,txtTo,txtStartDate,txtEndDate;
    EditText edtUsername;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    boolean radioButtonSelected=false,radioButton2Selected=false,
            radioButton3Selected=false,radioButton4Selected=false,screenChanged=false;


    public fragment_payment_details_report() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
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
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (fragment_payment_details_report)getFragmentManager().findFragmentByTag("PaymentDetailsFrag");
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
            }
        }
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
            }
        }
        rootView = inflater.inflate(R.layout.fragment_payment_details_report, container, false);
        if(savedInstanceState==null) {
            initUI();
        }else{
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
            screenChanged=true;
            mActionCancel = rootView.findViewById(R.id.action_cancel);
            mActionOk = rootView.findViewById(R.id.action_ok);
            final RadioGroup surveyRadioGroup = rootView.findViewById(R.id.moodRadioGroup); //pull group
            final RadioButton radioButton = rootView.findViewById(R.id.radioButton1);
            final RadioButton radioButton2 = rootView.findViewById(R.id.radioButton2);
            final RadioButton radioButton3 = rootView.findViewById(R.id.radioButton3);
            final RadioButton radioButton4 = rootView.findViewById(R.id.radioButton4);


            mActionOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedId = surveyRadioGroup.getCheckedRadioButtonId();
                    final RadioButton selectedRadioButton = rootView.findViewById(selectedId); //get r button val via ID
                    String choice = selectedRadioButton.getText().toString();
                    if (choice.contentEquals(activity.getResources().getString(R.string.full_report))) {
                        ///phone_no="9999999999";
                        //startReportDate="00";
                        //endReportDate="00";
                        reportNo = "1";
                        if (fragmentActionListener!=null) {
                            if(sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 42);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "paymentStatementFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 42);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                            }
                        }

                    }else if(choice.equals(activity.getString(R.string.report_by_phone_number))) {
                        //startReportDate="00";
                        //endReportDate="00";
                        reportNo = "2";
                            if (fragmentActionListener != null) {
                                if (sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 191);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PhoneNoPaymentDetailsFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    fragmentActionListener.onActionPerformed(bundle);
                                } else if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 191);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                                }
                            }

                }else if(choice.contentEquals(activity.getString(R.string.report_by_date_range))) {
                        reportNo = "3";
                        //phone_no = "9999999999";
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 190);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "EnterPaymentDetailsDateRangeFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 190);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                                }
                            }

                    }else if(choice.equals(activity.getString(R.string.report_by_phone_number_date_range))) {
                        reportNo = "4";
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 192);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SelectPhonePaymentDetailsDateRange");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 192);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                                }
                            }

                    }

                }
            });

            mActionCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fragmentActionListener != null) {
                        if(sending_frag.contentEquals("companyMainMenu")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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

                        }
                    }

                }
            });

        }
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



        mActionCancel = rootView.findViewById(R.id.action_cancel);
        mActionOk = rootView.findViewById(R.id.action_ok);
        final RadioGroup surveyRadioGroup = rootView.findViewById(R.id.moodRadioGroup); //pull group
        final RadioButton radioButton = rootView.findViewById(R.id.radioButton1);
        final RadioButton radioButton2 = rootView.findViewById(R.id.radioButton2);
        final RadioButton radioButton3 = rootView.findViewById(R.id.radioButton3);
        final RadioButton radioButton4 = rootView.findViewById(R.id.radioButton4);


        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = surveyRadioGroup.getCheckedRadioButtonId();
                final RadioButton selectedRadioButton = rootView.findViewById(selectedId); //get r button val via ID
                String choice = selectedRadioButton.getText().toString();
                if (choice.contentEquals(activity.getResources().getString(R.string.full_report))) {
                    //phone_no="9999999999";
                    //startReportDate="00";
                    //endReportDate="00";
                    reportNo = "1";
                    if (fragmentActionListener!=null) {
                        Bundle bundle = new Bundle();
                        if(sending_frag.contentEquals("companyMainMenu")) {
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 42);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "paymentStatementFrag");
                            fragmentActionListener.onActionPerformed(bundle);
                        }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 42);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                            bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                        }
                    }

                }else if(choice.equals(activity.getString(R.string.report_by_phone_number))) {
                    //startReportDate="00";
                    //endReportDate="00";
                    reportNo = "2";
                        if (fragmentActionListener != null) {
                            if (sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 191);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PhoneNoPaymentDetailsFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            } else if (sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 191);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                            }
                        }
                }else if(choice.contentEquals(activity.getString(R.string.report_by_date_range))) {
                    reportNo = "3";
                   // phone_no = "9999999999";
                        if (fragmentActionListener != null) {
                            if(sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 190);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "EnterPaymentDetailsDateRangeFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 190);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                            }
                        }
                }else if(choice.equals(activity.getString(R.string.report_by_phone_number_date_range))) {
                    reportNo = "4";
                        if (fragmentActionListener != null) {
                            if(sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 192);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SelectPhonePaymentDetailsDateRange");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 192);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                            }
                        }

                }

            }
        });

        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    if(sending_frag.contentEquals("companyMainMenu")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "reportsMainMenuFrag");
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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

                    }

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
