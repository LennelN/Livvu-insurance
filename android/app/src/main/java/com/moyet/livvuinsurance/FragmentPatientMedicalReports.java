package com.moyet.livvuinsurance;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FragmentPatientMedicalReports extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    View rootView;
    private FragmentPatientMedicalReports mFragment;
    private static final String TAG = "PatMedReportsFrag";
    private TextView mActionOk, mActionCancel;
    EditText edtUsername;
    TextView txtStartDate,txtEndDate,txtTo;
    FragmentActionListener fragmentActionListener;
    String userName,phoneNo,userRole,errorMessage,URL,firstName,ip,sending_frag;
    String welcomeMessage;
    String user_login_key,nameOfUser,user_role,user_login_id,country,phone_no,startReportDateSaved,endReportDateSaved;
    String merchantName;
    String merchantCode;
    String merchantUserName,reportPhoneNo,reportStartDate,reportEndDate,reportNo;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    boolean  screenChanged=false;
    String reportType,session_id;
    ArrayList<String> CountryName;
    String url_spinner,country_name_client,key_country_name,main_frag;
    Spinner spinner;
    int key_country_position;
    boolean flag = false;
    private  int screenWidth,screenHeight;
    AlertDialog.Builder builder;
    public FragmentPatientMedicalReports() {
        // Required empty public constructor
    }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener) {
        this.fragmentActionListener = fragmentActionListener;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (FragmentPatientMedicalReports)getFragmentManager().findFragmentByTag("PatientMedicalReportFrag");
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        if (savedInstanceState != null) {
            fragmentActionListener = (MainActivity) getActivity();
        }
        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            reportNo= bundle.getString(FragmentActionListener.KEY_REPORT_NUMBER);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(stringRequestAdapter != null){
            stringRequestAdapter.cancel();
        }
        if(stringRequest != null){
            stringRequest.cancel();
        }

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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            reportNo= bundle.getString(FragmentActionListener.KEY_REPORT_NUMBER);
            if(sending_frag.contentEquals("companyMainMenu")) {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
            }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                user_login_key= bundle.getString(FragmentActionListener.KEY_LOGIN_TYPE);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_NAME);
                phone_no = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                nameOfUser = bundle.getString(FragmentActionListener.KEY_USERNAME);
                user_role = bundle.getString(FragmentActionListener.KEY_USER_ROLE);
                user_login_id = bundle.getString(FragmentActionListener.KEY_LOGIN_USER_ID);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
                merchantCode = bundle.getString(FragmentActionListener.KEY_MERCHANT_CODE);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_MERCHANT_USERNAME);
                merchantName = bundle.getString(FragmentActionListener.KEY_MERCHANT_NAME);
                session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            }
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_patient_medical_reports, container, false);
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

            String server_logged_in = ip + "check_login_status.php";
            checkLoginStatus(server_logged_in);


            screenChanged=true;
            edtUsername = rootView.findViewById(R.id.edtUserName);
            edtUsername.requestFocus();
            txtStartDate= rootView.findViewById(R.id.tvStartDate);
            txtTo= rootView.findViewById(R.id.to_date);
            txtEndDate= rootView.findViewById(R.id.tvEndDate);
            mActionOk= rootView.findViewById(R.id.action_ok);
            mActionCancel= rootView.findViewById(R.id.action_cancel);

            if(startReportDateSaved!=null){
                txtStartDate.setText(startReportDateSaved);
            }
            if(endReportDateSaved!=null){
                txtEndDate.setText(endReportDateSaved);
            }


            txtStartDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((CheckBox) rootView.findViewById(R.id.dateRange)).isChecked()) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                context,
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                mStartDateSetListener,
                                year, month, day);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                        dialog.show();
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
                        txtview.setText(activity.getResources().getString(R.string.please_select_report_by_date_range_box));

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
                    if (((CheckBox) rootView.findViewById(R.id.dateRange)).isChecked()) {
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
                        txtview.setText(activity.getResources().getString(R.string.please_select_report_by_date_range_box));

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

            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month + 1;

                    String date = String.format(Locale.getDefault(),"%02d",day) + "/" +
                            String.format(Locale.getDefault(),"%02d",month) + "/" + String.format(Locale.getDefault(),"%02d",year);

                    txtEndDate.setText(date);
                    //txtEndDate.setText(date);
                    endReportDateSaved=date;
                }
            };

            txtEndDate.addTextChangedListener(registerTextWatcher);

            url_spinner=ip +"spinner_country.php";

            CountryName = new ArrayList<>();
            spinner = rootView.findViewById(R.id.country_Name);
            spinner.setFocusable(true);
            spinner.setFocusableInTouchMode(true);
            loadSpinnerData(url_spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    if(screenChanged) {
                        spinner.setSelection(key_country_position);
                        screenChanged=false;
                    }else key_country_position = i;

                    country_name_client = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                    if (!flag)
                        flag = true;
                    else
                        mActionOk.requestFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    // DO Nothing here
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
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
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
                        }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                            Bundle bundle = new Bundle();
                            bundle.putInt(FragmentActionListener.ACTION_KEY, 58);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminReportFrag");
                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                            bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                            bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                            bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                            fragmentActionListener.onActionPerformed(bundle);
                        }

                    }

                }
            });

            mActionOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reportPhoneNo=edtUsername.getText().toString();
                    reportStartDate=txtStartDate.getText().toString();
                    reportEndDate=txtEndDate.getText().toString();

                    if(reportPhoneNo.isEmpty()){
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
                        txtview.setText(activity.getResources().getString(R.string.please_enter_phone_number_for_report));

                        epicDialog.show();
                        Dialog finalEpicDialog = epicDialog;
                        action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });
                    }else{
                        if (((CheckBox) rootView.findViewById(R.id.dateRange)).isChecked()) {
                            if(reportStartDate.isEmpty()|| reportEndDate.isEmpty())
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
                                final String msg = activity.getResources().getString(R.string.report_dates_caanot_be_empty)+" " +
                                        activity.getResources().getString(R.string.remove_report_by_date_range_selection);
                                txtview.setText(msg);

                                epicDialog.show();
                                Dialog finalEpicDialog = epicDialog;
                                action_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalEpicDialog.dismiss();
                                    }
                                });

                            }else{
                                    reportType="DateRange";
                                    if (reportNo.contentEquals("1")) {
                                        if (fragmentActionListener != null) {
                                            if(sending_frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }
                                    } else if (reportNo.contentEquals("2")) {
                                        if (fragmentActionListener != null) {
                                            if(sending_frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }
                                    } else if (reportNo.contentEquals("3")) {
                                        if (fragmentActionListener != null) {
                                            if(sending_frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }
                                    }else if (reportNo.contentEquals("4")) {
                                        if (fragmentActionListener != null) {
                                            if(sending_frag.contentEquals("companyMainMenu")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                                bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                                bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }
                                    }
                            }
                        }else{
                            reportType="PhoneNo";
                            if(reportNo.contentEquals("1")){
                                if (fragmentActionListener != null) {
                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }else if(reportNo.contentEquals("2")) {
                                if (fragmentActionListener != null) {
                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }else if(reportNo.contentEquals("3")) {
                                if (fragmentActionListener != null) {
                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }else if(reportNo.contentEquals("4")) {
                                if (fragmentActionListener != null) {
                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }else {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                        bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }

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
        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);

        edtUsername = rootView.findViewById(R.id.edtUserName);
        edtUsername.requestFocus();
        txtStartDate= rootView.findViewById(R.id.tvStartDate);
        txtTo= rootView.findViewById(R.id.to_date);
        txtEndDate= rootView.findViewById(R.id.tvEndDate);
        mActionOk= rootView.findViewById(R.id.action_ok);
        mActionCancel= rootView.findViewById(R.id.action_cancel);

        txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CheckBox) rootView.findViewById(R.id.dateRange)).isChecked()) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(
                            context,
                            android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                            mStartDateSetListener,
                            year, month, day);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GREEN));
                    dialog.show();
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
                    txtview.setText(activity.getResources().getString(R.string.please_select_report_by_date_range_box));

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
                if (((CheckBox) rootView.findViewById(R.id.dateRange)).isChecked()) {
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
                    txtview.setText(activity.getResources().getString(R.string.please_select_report_by_date_range_box));

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

        txtEndDate.addTextChangedListener(registerTextWatcher);

        url_spinner=ip +"spinner_country.php";

        CountryName = new ArrayList<>();
        spinner = rootView.findViewById(R.id.country_Name);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        loadSpinnerData(url_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                key_country_position = i;

                country_name_client = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                //key_country_name=country_name;
                if (!flag)
                    flag = true;
                else
                    mActionOk.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
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
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
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
                    }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 58);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminReportFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }

            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPhoneNo=edtUsername.getText().toString();
                reportStartDate=txtStartDate.getText().toString();
                reportEndDate=txtEndDate.getText().toString();

                if(reportPhoneNo.isEmpty()){

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
                    txtview.setText(activity.getResources().getString(R.string.please_enter_phone_number_for_report));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                }else{
                    if (((CheckBox) rootView.findViewById(R.id.dateRange)).isChecked()) {
                        if(reportStartDate.isEmpty() && reportEndDate.isEmpty())
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
                            final String msg = activity.getResources().getString(R.string.report_dates_caanot_be_empty)+" " +
                                    activity.getResources().getString(R.string.remove_report_by_date_range_selection);
                            txtview.setText(msg);

                            epicDialog.show();
                            Dialog finalEpicDialog = epicDialog;
                            action_ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finalEpicDialog.dismiss();
                                }
                            });

                        }else{
                                reportType="DateRange";
                                if (reportNo.contentEquals("1")) {
                                    if (fragmentActionListener != null) {

                                        if(sending_frag.contentEquals("companyMainMenu")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }
                                    }
                                } else if (reportNo.contentEquals("2")) {
                                    if (fragmentActionListener != null) {
                                        if(sending_frag.contentEquals("companyMainMenu")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }
                                    }
                                } else if (reportNo.contentEquals("3")) {
                                    if (fragmentActionListener != null) {
                                        if(sending_frag.contentEquals("companyMainMenu")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }
                                    }
                                }else if (reportNo.contentEquals("4")) {
                                    if (fragmentActionListener != null) {
                                        if(sending_frag.contentEquals("companyMainMenu")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }else {
                                            Bundle bundle = new Bundle();
                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                            bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                            bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                            bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                            bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                            bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                            bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                            bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                            fragmentActionListener.onActionPerformed(bundle);
                                        }
                                    }
                                }
                        }
                    }else{
                        reportType="PhoneNo";
                        if(reportNo.contentEquals("1")){
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 60);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalStatementFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }
                            }
                        }else if(reportNo.contentEquals("2")) {
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 61);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalSummaryFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }
                            }
                        }else if(reportNo.contentEquals("3")) {
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 62);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalBillsFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }

                            }
                        }else if(reportNo.contentEquals("4")) {
                            if (fragmentActionListener != null) {
                                if(sending_frag.contentEquals("companyMainMenu")) {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }else {
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 172);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "PatientMedicalHistoryReportFrag");
                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                    bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                    bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                    bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                    bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                    bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_PHONE_NO, reportPhoneNo);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_START_DATE, reportStartDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_END_DATE, reportEndDate);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_TYPE, reportType);
                                    bundle.putString(FragmentActionListener.KEY_REPORT_COUNTRY, country_name_client);
                                    fragmentActionListener.onActionPerformed(bundle);
                                }

                            }
                        }


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
                if (userDateInput.length() > 0) {
                    String clean = userDateInput.replaceAll("[^\\d.]", "");
                    int inputDay = Integer.parseInt(clean.substring(0, 2));
                    int inputYear = Integer.parseInt(clean.substring(4, 8));
                    int inputMonth = Integer.parseInt(clean.substring(2, 4));

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    month = month + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    String date = String.format(Locale.getDefault(),"%02d", day) + "/" +
                            String.format(Locale.getDefault(),"%02d", month) + "/" + String.format(Locale.getDefault(),"%02d", year);

                    if (inputYear > year) {
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
                    } else if (inputYear == year) {
                        if (inputMonth > month) {
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
                        } else {
                            if (inputMonth == month) {
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
                if (userEndDateInput.length() > 0) {

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

                    String cleanEndDate = userEndDateInput.replaceAll("[^\\d.]", "");
                    int inputDay = Integer.parseInt(cleanEndDate.substring(0, 2));
                    int inputYear = Integer.parseInt(cleanEndDate.substring(4, 8));
                    int inputMonth = Integer.parseInt(cleanEndDate.substring(2, 4));

                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    month = month + 1;
                    int day = cal.get(Calendar.DAY_OF_MONTH);
                    String date = String.format(Locale.getDefault(),"%02d", day) + "/" +
                            String.format(Locale.getDefault(),"%02d", month) + "/" + String.format(Locale.getDefault(),"%02d", year);

                    if (inputYear > year) {
                        Toast.makeText(context, activity.getResources().getString(R.string.year_cannot_be_greater_than_current_year), Toast.LENGTH_SHORT).show();
                        txtEndDate.setText(date);
                    } else if (inputYear == year) {
                        if (inputMonth > month) {
                            Toast.makeText(context, activity.getResources().getString(R.string.month_cannot_be_greater_current_month), Toast.LENGTH_SHORT).show();
                            txtEndDate.setText(date);
                        } else {
                            if (inputMonth == month) {
                                if (inputDay > day) {
                                    Toast.makeText(context, activity.getResources().getString(R.string.day_cannot_be_greater_tan_today), Toast.LENGTH_SHORT).show();
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

    private void loadSpinnerData(String url) {
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

        stringRequestAdapter = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int cnt = 0;
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String country = jsonObject1.getString("country_name");
                        cnt = cnt + 1;
                        CountryName.add(country);
                        //   }
                    }
                    //spinner.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryName));
                    //spinner.setDropDownViewResource(R.layout.my_spinner);
                    if(cnt > 0) {
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
                        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(context, R.layout.my_spinner, CountryName);
                        spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                        spinner.setAdapter(spinnerArrayAdapter);
                    }

                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                            AlertDialog.Builder(activity, R.style.MyDialogTheme240);
                }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                        || screenWidth==720 || screenWidth==1184) {
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme16sp);
                }else if(screenWidth==600 || screenWidth==1024){
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme600);
                }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme720);
                }else{
                    builder = new
                            AlertDialog.Builder(activity, R.style.MyDialogTheme18sp);
                }
                builder.setMessage(activity.getResources().getString(R.string.unable_to_process))
                        .setNegativeButton(activity.getResources().getString(R.string.retry), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (fragmentActionListener != null) {
                                    if(sending_frag.contentEquals("companyMainMenu")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 37);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
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
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
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
                                    }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 58);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminReportFrag");
                                        bundle.putString(FragmentActionListener.KEY_NAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, nameOfUser);
                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchantUserName);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                        fragmentActionListener.onActionPerformed(bundle);
                                    }
                                }
                            }
                        })
                        .create()
                        .show();

                error.printStackTrace();

            }
        });
        Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);


    }
    private void checkLoginStatus(String server_logged_in) {
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

        stringRequest = new StringRequest(Request.Method.POST, server_logged_in,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //JSONArray jsonArray = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (!success) {
                                Bundle bundle = new Bundle();
                                bundle.putInt(FragmentActionListener.ACTION_KEY, 99);
                                bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseLogoutSessionFrag");
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "PatientMedicalReportFrag");
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        } catch (JSONException e) {
                            //pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        String errorMessage=getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "PatientMedicalReportFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        //error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                if(sending_frag.contentEquals("companyMainMenu")) {
                    param.put("username", merchantUserName);
                }else{
                    param.put("username", user_login_id);
                }
                param.put("ssid",session_id);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequest); // vConnection i claas which used to connect volley

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
