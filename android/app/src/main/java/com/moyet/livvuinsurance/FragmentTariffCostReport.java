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


public class FragmentTariffCostReport extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    View rootView;
    private FragmentTariffCostReport mFragment;
    private static final String TAG = "TariffCostFrag";
    private TextView mActionOk, mActionCancel;
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
    String phone_no,merchantUserName,tariff_code;
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


    public FragmentTariffCostReport() {
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
            mFragment = (FragmentTariffCostReport)getFragmentManager().findFragmentByTag("PaymentDetailsFrag");
        }

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
        rootView = inflater.inflate(R.layout.fragment_tariff_code_cost_report, container, false);
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

            edtUsername = rootView.findViewById(R.id.edtUserName);
            txtHeading = rootView.findViewById(R.id.criteria);
            txtTo = rootView.findViewById(R.id.textView1);
            txtStartDate= rootView.findViewById(R.id.etStartDate);
            txtEndDate= rootView.findViewById(R.id.etEndDate);
            txtStartDate.setHint(activity.getResources().getString(R.string.enter_start_date_report));
            txtEndDate.setHint(activity.getResources().getString(R.string.enter_end_date_report));
            txtStartDate.setHintTextColor(activity.getResources().getColor(R.color.colorBlack));
            txtEndDate.setHintTextColor(activity.getResources().getColor(R.color.colorBlack));
            txtStartDate.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            txtEndDate.setTextColor(activity.getResources().getColor(R.color.colorBlack));

            if(screenChanged){
                    if(endReportDateSaved!=null){
                        txtEndDate.setText(endReportDateSaved);
                    }
                    if(startReportDateSaved!=null){
                        txtStartDate.setText(startReportDateSaved);
                    }
                    edtUsername.requestFocus();
                    screenChanged=false;
            }


            txtStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance(TimeZone.getDefault());
                    //Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mStartDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                    dialog.show();
                }
            });

            mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;


                    String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                            String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                    txtStartDate.setText(date);
                    startReportDateSaved=date;
                }
            };

            // etDate.addTextChangedListener(dateWatcher);
            txtStartDate.addTextChangedListener(registerTextWatcher);

            txtEndDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mDateSetListener,
                            year,month,day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                    dialog.show();
                }
            });

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;
                    String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                            String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                    txtEndDate.setText(date);
                    endReportDateSaved=date;
                }
            };

            // etDate.addTextChangedListener(dateWatcher);
            txtEndDate.addTextChangedListener(registerTextWatcher);

            mActionOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tariff_code = edtUsername.getText().toString().trim();
                        startReportDate = txtStartDate.getText().toString().trim();
                        endReportDate = txtEndDate.getText().toString().trim();
                        if(startReportDate.length()!=0 && endReportDate.length()!=0 && tariff_code.length() != 0) {
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 163);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_TARIFF_CODE, tariff_code);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TariffCostOfMedicinesMerchantReportFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 163);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                    bundle.putString(FragmentActionListener.KEY_TARIFF_CODE, tariff_code);
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
                        }else{

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
                            txtview.setText(activity.getResources().getString(R.string.report_dates_and_tariff_code_cannot_be_empty));

                            epicDialog.show();
                            Dialog finalEpicDialog = epicDialog;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                }
                            });
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

        edtUsername = rootView.findViewById(R.id.edtUserName);
        txtHeading = rootView.findViewById(R.id.criteria);
        txtTo = rootView.findViewById(R.id.textView1);
        txtStartDate= rootView.findViewById(R.id.etStartDate);
        txtEndDate= rootView.findViewById(R.id.etEndDate);
        txtStartDate.setHint(activity.getResources().getString(R.string.enter_start_date_report));
        txtEndDate.setHint(activity.getResources().getString(R.string.enter_end_date_report));
        txtStartDate.setHintTextColor(activity.getResources().getColor(R.color.colorBlack));
        txtEndDate.setHintTextColor(activity.getResources().getColor(R.color.colorBlack));
        txtStartDate.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        txtEndDate.setTextColor(activity.getResources().getColor(R.color.colorBlack));

        edtUsername.requestFocus();
                txtStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                context,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mStartDateSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                        dialog.show();
                    }
                });

                mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;

                        String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                                String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                        txtStartDate.setText(date);
                        startReportDateSaved=date;
                    }
                };

                // etDate.addTextChangedListener(dateWatcher);
                txtStartDate.addTextChangedListener(registerTextWatcher);

                txtEndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                context,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mDateSetListener,
                                year,month,day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                        dialog.show();
                    }
                });

                mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                                String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                        txtEndDate.setText(date);
                        endReportDateSaved=date;

                    }
                };

                // etDate.addTextChangedListener(dateWatcher);
                txtEndDate.addTextChangedListener(registerTextWatcher);


        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tariff_code = edtUsername.getText().toString().trim();
                    startReportDate = txtStartDate.getText().toString().trim();
                    endReportDate = txtEndDate.getText().toString().trim();

                    if(startReportDate.length()!=0 && endReportDate.length()!=0 && tariff_code.length() != 0) {
                        if (fragmentActionListener != null) {
                            if(sending_frag.contentEquals("companyMainMenu")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 163);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                bundle.putString(FragmentActionListener.KEY_TARIFF_CODE, tariff_code);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, startReportDate);
                                bundle.putString(FragmentActionListener.KEY_REPORT_NUMBER, reportNo);
                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, endReportDate);
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "TariffCostOfMedicinesMerchantReportFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                fragmentActionListener.onActionPerformed(bundle);
                            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 163);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_TARIFF_CODE, tariff_code);
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
                    }else{
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
                        txtview.setText(activity.getResources().getString(R.string.report_dates_and_tariff_code_cannot_be_empty));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
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

            String userDateInput = txtStartDate.getText().toString().trim();
            String userEndDateInput = txtEndDate.getText().toString().trim();

            if(userDateInput.length() > 0)
            {
                String clean = userDateInput.replaceAll("[^\\d.]", "");

                int inputDay =Integer.parseInt(clean.substring(0, 2));
                int inputYear=Integer.parseInt(clean.substring(4, 8));
                int inputMonth = Integer.parseInt(clean.substring(2, 4));

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                        String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                if(inputYear > year){

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
                    txtview.setText(activity.getResources().getString(R.string.year_cannot_be_greater_than_current_year));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });

                    txtStartDate.setText(date);
                }else if(inputYear == year){
                    if(inputMonth > month)
                    {

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
                        txtview.setText(activity.getResources().getString(R.string.month_cannot_be_greater_current_month));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                        txtStartDate.setText(date);
                    }else{
                        if(inputMonth == month) {
                            if (inputDay > day) {
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
                                txtview.setText(activity.getResources().getString(R.string.day_cannot_be_greater_tan_today));

                                epicDialog.show();
                                Dialog finalEpicDialog = epicDialog;
                                action_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalEpicDialog.dismiss();
                                    }
                                });

                                txtStartDate.setText(date);
                            }
                        }
                    }
                }
            }
            if(userEndDateInput.length() > 0){

                String cleanEndDate = userEndDateInput.replaceAll("[^\\d.]", "");
                int inputDay =Integer.parseInt(cleanEndDate.substring(0, 2));
                int inputYear=Integer.parseInt(cleanEndDate.substring(4, 8));
                int inputMonth = Integer.parseInt(cleanEndDate.substring(2, 4));

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                month = month + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                        String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                if(inputYear > year){

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
                    txtview.setText(activity.getResources().getString(R.string.year_cannot_be_greater_than_current_year));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    txtEndDate.setText(date);
                }else if(inputYear == year){
                    if(inputMonth > month)
                    {

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
                        txtview.setText(activity.getResources().getString(R.string.month_cannot_be_greater_current_month));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                        txtEndDate.setText(date);
                    }else{
                        if(inputMonth == month) {
                            if (inputDay > day) {

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
                                txtview.setText(activity.getResources().getString(R.string.day_cannot_be_greater_tan_today));

                                epicDialog.show();
                                Dialog finalEpicDialog = epicDialog;
                                action_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalEpicDialog.dismiss();
                                    }
                                });
                                txtEndDate.setText(date);
                            }
                        }
                    }
                }

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
