package com.moyet.livvuinsurance;


import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentActivateDoctorAccount extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestSpinner;
    StringRequest stringRequestDoctor;
    View rootView;
    private FragmentActivateDoctorAccount mFragment;
    private static final String TAG = "ActDoctorFrag";
    FragmentActionListener fragmentActionListener;
    String phone_no,merchantUserName,sending_frag,firstName,userRole,phoneNo,ip,userName,transNo,
            URL,transactionNo,cashed_out_transaction_type,cashed_out_transaction_date,user_login_id,country;
    Button cancel,send;
    TextInputEditText textInputEditText;
    EditText etDoctorPhoneNo,etDoctorName;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    TextInputLayout reason_TextInputLayout;
    String user_deactivate_id,reason_deactivated, transaction_date,session_id,main_frag;
    String merchantCode,merchantName,nameOfUser,user_role,user_login_key,frag,doctor_deactivate_phone,doctor_deactivate_name;
    ProgressDialog pd;
    RelativeLayout relativeLayout;
    AlertDialog.Builder builder;
    private  int screenWidth,screenHeight;
    TextView tvSearch;
    Spinner spinner;
    List<DoctorBillingDetails> doctorBillingDetails;
    String doctor_name_data_selected;
    String doctor_profession_data_selected;
    String doctor_phone_number_data_selected;
    String doctor_reg_no_data_selected;
    String url_get_bill_doctor_details,code_returned_message;
    int key_doctor_description_position;
    boolean flag_desc=false;

    public FragmentActivateDoctorAccount() {
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
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(frag.contentEquals("MerchantMainMenuFrag")){
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
            }else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (FragmentActivateDoctorAccount)getFragmentManager().findFragmentByTag("ActivateDoctorAccountFrag");
        }

    }

    

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(frag.contentEquals("MerchantMainMenuFrag")){
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
            }else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
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

        if(stringRequestAdapter != null){
            stringRequestAdapter.cancel();
        }
        if(stringRequest != null){
            stringRequest.cancel();
        }
        if(stringRequestSpinner != null){
            stringRequestSpinner.cancel();
        }
        if(stringRequestDoctor != null){
            stringRequestDoctor.cancel();
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
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            frag = bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);

            if(frag.contentEquals("MerchantMainMenuFrag")){
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
            }else {
                merchantName = bundle.getString(FragmentActionListener.KEY_NAME);
                merchantCode = bundle.getString(FragmentActionListener.KEY_LAST_NAME);
                merchantUserName = bundle.getString(FragmentActionListener.KEY_USERNAME);
                country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            }
        }

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_activate_doctor_account, container, false);
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

        String server_logged_in = ip + "check_login_status.php";
        checkLoginStatus(server_logged_in);
        relativeLayout = rootView.findViewById(R.id.relativeLayout1);
        //relativeLayout.setOnClickListener(null);
        spinner = rootView.findViewById(R.id.etDoctorPhone);
        spinner.setFocusable(true);
        spinner.setFocusableInTouchMode(true);
        url_get_bill_doctor_details=ip+"get_merchant_doctor_details_deactivated.php";
        loadSpinnerDoctorData(url_get_bill_doctor_details);

        etDoctorName=rootView.findViewById(R.id.etDoctorName);
        etDoctorName.addTextChangedListener(registerTextWatcher);
        cancel = rootView.findViewById(R.id.btnClose);
        send = rootView.findViewById(R.id.btnSend);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // description = spinner_health_desc.getItemAtPosition(spinner_health_desc.getSelectedItemPosition()).toString();
                key_doctor_description_position=i;

                DoctorBillingDetails billRequestDetails = (DoctorBillingDetails) adapterView.getSelectedItem();
                spinner.setSelection(key_doctor_description_position);
                doctor_name_data_selected=billRequestDetails.getDoctorName();
                if(doctor_name_data_selected != null){
                    etDoctorName.setText(doctor_name_data_selected);
                }
                doctor_phone_number_data_selected=billRequestDetails.getDoctorPhone();
                doctor_profession_data_selected=billRequestDetails.getDoctorProfession();
                doctor_reg_no_data_selected=billRequestDetails.getDoctorRegNo();


                if (!flag_desc) {
                    flag_desc = true;
                    //etUsername.requestFocus();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragmentActionListener != null) {
                    if(frag.contentEquals("MerchantMainMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }else{
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                        fragmentActionListener.onActionPerformed(bundle);
                    }

                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
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

                if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                    pd = new ProgressDialog(context, R.style.MyDialogTheme240);
                }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                        || screenWidth==720 || screenWidth==1184) {
                    pd = new ProgressDialog(context, R.style.MyDialogTheme16sp);
                }else if(screenWidth==600 || screenWidth==1024){
                    pd = new ProgressDialog(context, R.style.MyDialogTheme600);
                }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                    pd = new ProgressDialog(context, R.style.MyDialogTheme720);
                }else{
                    pd = new ProgressDialog(context, R.style.MyDialogTheme18sp);
                }
                pd.setMessage(activity.getResources().getString(R.string.processing_please_wait_dialog));
                pd.show();
                URL=ip +"activate_doctor_profile.php";
                //doctor_deactivate_phone=etDoctorPhoneNo.getText().toString();
                doctor_deactivate_name = etDoctorName.getText().toString();
                stringRequestAdapter = new StringRequest(Request.Method.POST, URL,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                //JSONArray jsonArray = null;
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                                    boolean success = jsonResponse.getBoolean("success");
                                    String message = jsonResponse.getString("message");

                                    if (success) {
                                        pd.dismiss();
                                        if (fragmentActionListener != null) {
                                            if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
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
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }else{
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
                                                bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                fragmentActionListener.onActionPerformed(bundle);
                                            }
                                        }

                                        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
                                    } else {
                                        pd.dismiss();
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
                                        txtview.setText(message);

                                        epicDialog.show();

                                        Dialog finalEpicDialog = epicDialog;
                                        action_ok.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                finalEpicDialog.dismiss();
                                            }
                                        });

                                    }
                                } catch (JSONException e) {
                                    pd.dismiss();

                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
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
                                pd.dismiss();
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
                                                    if(frag.contentEquals("MerchantMainMenuFrag")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
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
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else{
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 179);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantDoctorMaintFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY,country);
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, frag);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }

                                                }
                                            }
                                        })
                                        .create()
                                        .show();

                                error.printStackTrace();
                                ////error.getMessage();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<String, String>();
                        if(frag.contentEquals("MerchantMainMenuFrag")){
                            param.put("username", user_login_id);
                        }else{
                            param.put("username", merchantUserName);
                        }
                        param.put("doctor_deactivate_phone",doctor_phone_number_data_selected);
                        param.put("doctor_deactivate_name",doctor_deactivate_name);
                        param.put("country", country);
                        param.put("merchant_phone_number", merchantUserName);
                        param.put("merchant_code", merchantCode);
                        return param;
                    }
                };
                Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley


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

            String userDoctorNameInput = etDoctorName.getText().toString().trim();

            if(!userDoctorNameInput.isEmpty())
            {
                send.setEnabled(true);
                send.setTextColor(activity.getResources().getColor(R.color.colorBlack));
            }else{
                send.setEnabled(false);
                send.setTextColor(activity.getResources().getColor(R.color.colorCell));
            }


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "DeactivateUserFrag");
                                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                                fragmentActionListener.onActionPerformed(bundle);
                            }
                        } catch (JSONException e) {
                            pd.dismiss();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "DeactivateUserFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();

                if(frag.contentEquals("MerchantMainMenuFrag")){
                    param.put("username", user_login_id);
                }else{
                    param.put("username", merchantUserName);
                }
                param.put("ssid",session_id);
                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequest); // vConnection i claas which used to connect volley

    }

    private void checkDoctorDetails(String server_logged_in) {

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

        stringRequestDoctor = new StringRequest(Request.Method.POST, server_logged_in,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //JSONArray jsonArray = null;
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonResponse = jsonArray.getJSONObject(0);
                            boolean success = jsonResponse.getBoolean("success");
                            String message = jsonResponse.getString("message");

                            if (success) {
                                String fname = jsonResponse.getString("full_name");
                                etDoctorName.setText(fname);
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
                                //txtview.setText(getResources().getString(R.string.no_country_found));
                                //txtview.setText(getResources().getString(R.string.doctor_does_not_exist_in_the_system));
                                txtview.setText(message);
                                epicDialog.show();
                                Dialog finalEpicDialog = epicDialog;
                                action_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finalEpicDialog.dismiss();
                                    }
                                });
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

                        String errorMessage=activity.getResources().getString(R.string.unable_to_verify_login_status_check_your_internet_connection_and_try_again);
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MainFragment");
                        bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "DeactivateUserFrag");
                        fragmentActionListener.onActionPerformed(bundle);
                        error.printStackTrace();
                        ////error.getMessage();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("phone_no",doctor_deactivate_phone);
                param.put("merchant_phone_number", merchantUserName);
                param.put("merchant_code", merchantCode);

                return param;
            }
        };
        Vconnection.getnInstance(activity).addRequestQue(stringRequestDoctor); // vConnection i claas which used to connect volley

    }

    private void loadSpinnerDoctorData(String url) {

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

        stringRequestSpinner = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int cnt = 0;
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonResponse = jsonArray.getJSONObject(0);
                    boolean success = jsonResponse.getBoolean("success");
                    code_returned_message = jsonResponse.getString("message");
                    doctorBillingDetails = new ArrayList<>();

                    if(success) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            String merchant_name_doctor = jsonObject1.getString("full_name");
                            String merchant_profession_doctor = jsonObject1.getString("profession");
                            String merchant_phone_number_doctor = jsonObject1.getString("phone_number");
                            String merchant_reg_no_doctor = jsonObject1.getString("registration_no");
                            cnt = cnt + 1;
                            DoctorBillingDetails billRequestDetails =
                                    new DoctorBillingDetails(merchant_name_doctor,
                                            merchant_phone_number_doctor, merchant_profession_doctor,
                                            merchant_reg_no_doctor);

                            doctorBillingDetails.add(billRequestDetails);
                            //spinner_region.notifyDataSetChanged();
                        }
                        //spinner_region.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, CountryRegions));
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
                            ArrayAdapter<DoctorBillingDetails> spinnerArrayAdapter =
                                    new ArrayAdapter<>(context,
                                    R.layout.my_spinner, doctorBillingDetails);
                            //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.my_spinner,doctorBillingDetails);
                            spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                            spinner.setAdapter(spinnerArrayAdapter);
                        }
                    }
                } catch (JSONException e) {
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
                    //description=null;
                    ArrayAdapter<DoctorBillingDetails> spinnerArrayAdapter =
                            new ArrayAdapter<>(context,
                            R.layout.my_spinner, doctorBillingDetails);
                    //ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(),R.layout.my_spinner,doctorBillingDetails);
                    spinnerArrayAdapter.setDropDownViewResource(R.layout.my_spinner);
                    spinner.setAdapter(spinnerArrayAdapter);

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
                    txtview.setText(code_returned_message);

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<String, String>();
                param.put("merchant_phone_number", merchantUserName);
                param.put("merchant_code", merchantCode);
                return param;
            }
        };

        Vconnection.getnInstance(activity).addRequestQue(stringRequestSpinner);

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
