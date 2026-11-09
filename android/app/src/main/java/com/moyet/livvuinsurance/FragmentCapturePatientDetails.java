package com.moyet.livvuinsurance;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telephony.SmsManager;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class FragmentCapturePatientDetails extends Fragment {
    private Context contextNullSafe;
    Context context;
    FragmentActivity activity;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    StringRequest stringRequestSpinner;
    View rootView;
    private static final String TAG = "CaptPatientDetails";
    private FragmentCapturePatientDetails mFragment;
    FragmentActionListener fragmentActionListener;
    boolean flag = false;
    Spinner spinner;
    List<TariffDetails> tariffList;
    Spinner spinner_quantity;
    String URL;
    String server_url;
    String merchantName;
    String merchantCode;
    String merchantUserName;
    ArrayList<Model> itemList;
    ArrayList<String> listViewItems;
    String[] quantityName;
    EditText etPhoneNumber,etBodyTemperature,etBloodPressure,etBodyPulse,etAllergies,etBloodType,etOtherDetails;
    ListView lv;
    listviewAdapter adapter;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    AlertDialog.Builder alert;
    String ip;
    List<Items> items;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    int key_tariff_position;
    boolean screenChanged = false;
    String tariffDate,country,sending_frag,user_login_key,user_role,user_login_id,nameOfUser,
            phone_number,bill_processed_by,session_id,code_returned_message;
    String patientPhoneNumber,bodyTemperature,bloodPressure,bodyPulse,bodyAllergies,bloodType,otherDetails;
    private TextView sendLink;
    private TextView cancelLink;
    private  int screenWidth,screenHeight;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    ArrayList<String> CountryName;
    String url_spinner,country_name_client,key_country_name,main_frag;
    int key_country_position;


    public FragmentCapturePatientDetails() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState!=null){
            fragmentActionListener = (MainActivity)getActivity();
            assert getFragmentManager() != null;
            mFragment = (FragmentCapturePatientDetails)getFragmentManager().findFragmentByTag("CapturePatientDetailsFrag");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           }

    public void setFragmentActionListener(FragmentActionListener fragmentActionListener){
        this.fragmentActionListener = fragmentActionListener;
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
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contextNullSafe == null) getContextNullSafety();
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
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
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
            }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
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
                session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
            }
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if(bundle!=null) {
            sending_frag=bundle.getString(FragmentActionListener.KEY_SENDING_FRAG);
            ip = bundle.getString(FragmentActionListener.KEY_IP_ADDRESS);
            country = bundle.getString(FragmentActionListener.KEY_LOGIN_COUNTRY);
            session_id = bundle.getString(FragmentActionListener.KEY_LOGIN_SESSION_ID);
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
            }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
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

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_capture_patient_details, container, false);
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

        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        sendLink = rootView.findViewById(R.id.tvSendHere);

        etPhoneNumber=rootView.findViewById(R.id.etPhoneNumber);
        etPhoneNumber.addTextChangedListener(registerTextWatcher);
        etBodyTemperature=rootView.findViewById(R.id.etBodyTemperature);
        etBodyTemperature.addTextChangedListener(registerTextWatcher);
        etBloodPressure=rootView.findViewById(R.id.etBloodPressure);
        etBloodPressure.addTextChangedListener(registerTextWatcher);
        etBodyPulse=rootView.findViewById(R.id.etBodyPulse);
        etBodyPulse.addTextChangedListener(registerTextWatcher);

        etAllergies=rootView.findViewById(R.id.etAllergies);
        etBloodType=rootView.findViewById(R.id.etBloodType);
        etOtherDetails=rootView.findViewById(R.id.etOtherDetails);

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
                    sendLink.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


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
                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
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
                    } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
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

                    patientPhoneNumber = etPhoneNumber.getText().toString();
                    bodyTemperature = etBodyTemperature.getText().toString();
                    bloodPressure = etBloodPressure.getText().toString();
                    bodyPulse = etBodyPulse.getText().toString();
                    bodyAllergies = etAllergies.getText().toString();
                    bloodType = etBloodType.getText().toString();
                    otherDetails = etOtherDetails.getText().toString();


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
                    server_url=ip +"create_patient_medical_details.php";


                    stringRequestAdapter = new StringRequest(Request.Method.POST, server_url,
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

                                            if (fragmentActionListener!=null) {
                                                if(sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 170);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseCapturePatientDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 170);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseCapturePatientDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);

                                                }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 170);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseCapturePatientDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);

                                                }
                                            }
                                        } else {
                                            pd.dismiss();
                                            if (fragmentActionListener!=null) {
                                                if(sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 170);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseCapturePatientDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 170);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseCapturePatientDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else if(sending_frag.contentEquals("MerchantMainMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 170);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, message);
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
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResponseCapturePatientDetailsFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                                                    fragmentActionListener.onActionPerformed(bundle);

                                                }
                                            }
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
                                    epicDialog.setContentView(R.layout.dialog_my_custom_message_retry);
                                    TextView txtview = epicDialog.findViewById(R.id.message_text);
                                    TextView action_ok = epicDialog.findViewById(R.id.action_ok);
                                    txtview.setText(activity.getResources().getString(R.string.unable_to_process));

                                    epicDialog.show();
                                    Dialog finalEpicDialog = epicDialog;
                                    action_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finalEpicDialog.dismiss();
                                            if (fragmentActionListener != null) {
                                                if(sending_frag.contentEquals("companyMainMenu")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                                    bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                                    bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                    bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
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
                                                } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
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
                                    error.printStackTrace();
                                    //error.getMessage();
                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("phone_number", patientPhoneNumber);
                            param.put("body_temperature", bodyTemperature);
                            param.put("blood_pressure", bloodPressure);
                            param.put("body_pulse", bodyPulse);
                            param.put("known_allergies", bodyAllergies);
                            param.put("blood_type", bloodType);
                            param.put("other_details", otherDetails);
                            param.put("merchant_phone_number", merchantUserName);
                            param.put("merchant_code", merchantCode);
                            param.put("merchant_name", merchantName);
                            if(sending_frag.contentEquals("companyMainMenu")) {
                                param.put("record_processed_by", merchantUserName);
                            }else{
                                param.put("record_processed_by", user_login_id);
                            }
                            param.put("country_name_client",country_name_client);

                            return param;
                        }
                    };
                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter); // vConnection i claas which used to connect volley
                }
        });

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
                                bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
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
                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, sending_frag);
                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
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
            String userBodyPulseInput = etBodyPulse.getText().toString().trim();
            String userPhoneInput = etPhoneNumber.getText().toString().trim();
            String userBodyTemperatureInput = etBodyTemperature.getText().toString().trim();
            String userBloodPressureInput = etBloodPressure.getText().toString().trim();

            if(!userBodyPulseInput.isEmpty()
                    && !userPhoneInput.isEmpty() && !userBodyTemperatureInput.isEmpty()
                    && !userBloodPressureInput.isEmpty())
            {
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
        stringRequestSpinner = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
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
                    //spinner.setAdapter(new ArrayAdapter<String>(context(), android.R.layout.simple_spinner_dropdown_item, CountryName));
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
                        ArrayAdapter<String> spinnerArrayAdapter =
                                new ArrayAdapter<String>(context, R.layout.my_spinner, CountryName);
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
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                        bundle.putString(FragmentActionListener.KEY_NAME, merchantName);
                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchantCode);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                        bundle.putString(FragmentActionListener.KEY_USERNAME, merchantUserName);
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
                                    } else if (sending_frag.contentEquals("MerchantAdminMenuFrag")) {
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
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
                        })
                        .create()
                        .show();

                error.printStackTrace();

            }
        });

        Vconnection.getnInstance(activity).addRequestQue(stringRequestSpinner); // vConnection i claas which used to connect volley


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
