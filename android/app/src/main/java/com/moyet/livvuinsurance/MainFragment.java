package com.moyet.livvuinsurance;


import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.moyet.livvuinsurance.FragmentActionListener.KEY_IP_ADDRESS_SELECTED;


public class MainFragment extends Fragment {
    private Context contextNullSafe;
    StringRequest stringRequest;
    StringRequest stringRequestAdapter;
    private static final String TAG = "MainFragment";
    boolean flag = false;
    View rootView;
    Context context;
    FragmentActivity activity;
    FragmentActionListener fragmentActionListener;
    EditText etUsername;
    EditText etPassword;
    TextView loginLink;
    TextView registerLink, forgotPassword;
    TextView cancelLink;
    String username, errorMessage,key_country_name;
    String password,country_name;
    int key_country_position;
    String ip;
    String URL;
    ProgressDialog pd;
    private int screenWidth, screenHeight;
    AlertDialog.Builder builder;
    ArrayList<String> CountryName;
    String url_spinner;
    Spinner spinner;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initUI();
        return rootView;

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
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(stringRequest != null){
            stringRequest.cancel();
        }
        if(stringRequestAdapter != null) {
            stringRequestAdapter.cancel();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        contextNullSafe = context;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (contextNullSafe == null) getContextNullSafety();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

        etUsername = rootView.findViewById(R.id.etPhoneNumber);
        etUsername.addTextChangedListener(loginTextWatcher);
        etUsername.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        etUsername.requestFocus();
        etPassword = rootView.findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(loginTextWatcher);
        etPassword.setTextColor(activity.getResources().getColor(R.color.colorBlack));
        registerLink = rootView.findViewById(R.id.tvRegisterHere);
        loginLink = rootView.findViewById(R.id.tvLoginHere);
        cancelLink = rootView.findViewById(R.id.tvCancelHere);
        forgotPassword = rootView.findViewById(R.id.tvForgotPassword);
        ip = activity.getResources().getString(R.string.ip_address);
        URL = ip + "login.php";

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

                country_name = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                key_country_name=country_name;
                if (!flag)
                    flag = true;
                else
                    loginLink.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        //TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putInt(FragmentActionListener.ACTION_KEY, 135);
                bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                fragmentActionListener.onActionPerformed(bundle);

            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentActionListener != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(FragmentActionListener.ACTION_KEY, 1);
                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "registrationType");
                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS,KEY_IP_ADDRESS_SELECTED);
                    fragmentActionListener.onActionPerformed(bundle);
                }
            }
        });

        cancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //this.dismiss()
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
                activity.finish();;

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString();
                password = etPassword.getText().toString();

                flag = false;
                if (password.length() < 4) {
                        flag = true;

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
                        txtview.setText(activity.getResources().getString(R.string.password_length));

                        epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finalEpicDialog.dismiss();
                            }
                        });

                }else if (country_name == null){
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
                    txtview.setText(activity.getResources().getString(R.string.please_select_country_of_login));

                    epicDialog.show();
                    Dialog finalEpicDialog = epicDialog;
                    action_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finalEpicDialog.dismiss();
                        }
                    });

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

                    if((screenWidth==240 && screenHeight==320) || (screenWidth==320 && screenHeight==240)) {
                        pd = new ProgressDialog(activity, R.style.MyDialogTheme240);
                    }else if((screenWidth==240 && screenHeight==432) || (screenWidth==432 && screenHeight==240)
                            || screenWidth==720 || screenWidth==1184) {
                        pd = new ProgressDialog(activity, R.style.MyDialogTheme16sp);
                    }else if(screenWidth==600 || screenWidth==1024){
                        pd = new ProgressDialog(activity, R.style.MyDialogTheme600);
                    }else if((screenWidth==800 && screenHeight == 1232)|| screenWidth==1280){
                        pd = new ProgressDialog(activity, R.style.MyDialogTheme720);
                    }else{
                        pd = new ProgressDialog(activity, R.style.MyDialogTheme18sp);
                    }

                    pd.setMessage(activity.getResources().getString(R.string.processing));
                    pd.show();

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
                                            int type = jsonResponse.getInt("type");

                                            if (type == 1) {


                                                if(message.contentEquals("Reset Password")) {
                                                    String session_id = jsonResponse.getString("session_id");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else {
                                                    String name = jsonResponse.getString("name");
                                                    String surname = jsonResponse.getString("surname");
                                                    String country_name = jsonResponse.getString("country_name");
                                                    String session_id = jsonResponse.getString("session_id");
                                                    String phone_no = jsonResponse.getString("phone_number");

                                                    if (fragmentActionListener != null) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 185);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ConfirmOTPFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, name);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, surname);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
                                            } else if(type == 2){

                                                if(message.contentEquals("Reset Password")) {
                                                    String session_id = jsonResponse.getString("session_id");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    Bundle bundle = new Bundle();
                                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                    bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                    bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                    bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                    fragmentActionListener.onActionPerformed(bundle);
                                                }else{
                                                    String merchant_name = jsonResponse.getString("merchant_name");
                                                    String merchant_code = jsonResponse.getString("merchant_code");
                                                    String type_of_reg = jsonResponse.getString("type_of_registration");
                                                    String merchant_country = jsonResponse.getString("merchant_country");
                                                    String session_id = jsonResponse.getString("session_id");

                                                    if (fragmentActionListener != null) {
                                                        if (type_of_reg.equals("Health")) {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 5);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "companyMainMenu");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, merchant_country);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        } else {
                                                            Bundle bundle = new Bundle();
                                                            bundle.putInt(FragmentActionListener.ACTION_KEY, 65);
                                                            bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "SocietyCompanyMainMenuFrag");
                                                            bundle.putString(FragmentActionListener.KEY_NAME, merchant_name);
                                                            bundle.putString(FragmentActionListener.KEY_LAST_NAME, merchant_code);
                                                            bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, merchant_country);
                                                            bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                            bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                            fragmentActionListener.onActionPerformed(bundle);
                                                        }
                                                    }
                                                }
                                            }else if(type == 3){

                                                if(message.contentEquals("Successful Login")) {
                                                    String username = jsonResponse.getString("name");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    String user_role = jsonResponse.getString("user_role");
                                                    String country = jsonResponse.getString("country");
                                                    String user_login_id = jsonResponse.getString("user_login_id");
                                                    String session_id = jsonResponse.getString("session_id");

                                                    if(user_role.contentEquals("Accounts")) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 107);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "livvuMainAccountsMenu");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(user_role.contentEquals("Admin")){
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 43);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainAdminMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else{
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 121);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "LivvuMainMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }else{
                                                    if(message.contentEquals("Reset Password")) {
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String session_id = jsonResponse.getString("session_id");
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else {
                                                        String username = jsonResponse.getString("name");
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String user_role = jsonResponse.getString("user_role");
                                                        String country = jsonResponse.getString("country");
                                                        String user_login_id = jsonResponse.getString("user_login_id");
                                                        String session_id = jsonResponse.getString("session_id");

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 49);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CompleteUserRegFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "LivvuUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
                                            }else if(type == 4){

                                                if(message.contentEquals("Successful Login")) {
                                                    String username = jsonResponse.getString("name");
                                                    String phone_no = jsonResponse.getString("phone_number");
                                                    String user_role = jsonResponse.getString("user_role").trim();
                                                    String country = jsonResponse.getString("country");
                                                    String user_login_id = jsonResponse.getString("user_login_id");
                                                    String merchant_phone_number=jsonResponse.getString("merchant_phone_number");
                                                    String merchant_code=jsonResponse.getString("merchant_code");
                                                    String merchant_name=jsonResponse.getString("merchant_name");
                                                    String session_id = jsonResponse.getString("session_id");
                                                    if(user_role.contentEquals("Accounts")) {

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 55);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantMainMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchant_phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else if(user_role.contentEquals("Admin")) {

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 56);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "MerchantAdminMenuFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchant_phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }else{
                                                    if(message.contentEquals("Reset Password")) {
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String session_id = jsonResponse.getString("session_id");
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 127);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "ResetPasswordFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country_name);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }else{
                                                        String username = jsonResponse.getString("name");
                                                        String phone_no = jsonResponse.getString("phone_number");
                                                        String user_role = jsonResponse.getString("user_role").trim();
                                                        String country = jsonResponse.getString("country");
                                                        String user_login_id = jsonResponse.getString("user_login_id");
                                                        String merchant_phone_number=jsonResponse.getString("merchant_phone_number");
                                                        String merchant_code=jsonResponse.getString("merchant_code");
                                                        String merchant_name=jsonResponse.getString("merchant_name");
                                                        String session_id = jsonResponse.getString("session_id");

                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt(FragmentActionListener.ACTION_KEY, 49);
                                                        bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "CompleteUserRegFrag");
                                                        bundle.putString(FragmentActionListener.KEY_SENDING_FRAG, "mainFrag");
                                                        bundle.putString(FragmentActionListener.KEY_NAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_PHONE_NO, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_TYPE, "MerchantUser");
                                                        bundle.putString(FragmentActionListener.KEY_LAST_NAME, phone_no);
                                                        bundle.putString(FragmentActionListener.KEY_USERNAME, username);
                                                        bundle.putString(FragmentActionListener.KEY_USER_ROLE, user_role);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_COUNTRY, country);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_USER_ID, user_login_id);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_USERNAME, merchant_phone_number);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_CODE, merchant_code);
                                                        bundle.putString(FragmentActionListener.KEY_MERCHANT_NAME, merchant_name);
                                                        bundle.putString(FragmentActionListener.KEY_LOGIN_SESSION_ID, session_id);
                                                        bundle.putString(FragmentActionListener.KEY_IP_ADDRESS, KEY_IP_ADDRESS_SELECTED);
                                                        fragmentActionListener.onActionPerformed(bundle);
                                                    }
                                                }
                                            }
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
                                    pd.dismiss();

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

                                    errorMessage=activity.getResources().getString(R.string.unable_to_sign_in_please_check_your_internet_connection_and_try_again);


                                    Bundle bundle = new Bundle();
                                    bundle.putInt(FragmentActionListener.ACTION_KEY, 32);
                                    bundle.putString(FragmentActionListener.KEY_MESSAGE, errorMessage);
                                    bundle.putString(FragmentActionListener.KEY_SELECTED_FRAG, "mainFrag");
                                    fragmentActionListener.onActionPerformed(bundle);
                                    error.printStackTrace();
                                    ////error.getMessage();

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> param = new HashMap<String, String>();
                            param.put("username",username);
                            param.put("password",password);
                            param.put("loginCountry",country_name);

                            return param;
                        }
                    };

                    Vconnection.getnInstance(activity).addRequestQue(stringRequestAdapter);
                }
            }
        });

    }

    private void loadSpinnerData(String url_spinner) {

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


        stringRequest = new StringRequest(Request.Method.GET, url_spinner, new Response.Listener<String>() {
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
                    }

                    if(cnt > 0 ) {

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
                error.printStackTrace();

            }
        });
        Vconnection.getnInstance(activity).addRequestQue(stringRequest);

    }


    private final TextWatcher loginTextWatcher = new TextWatcher() {
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

            String usernameInput = etUsername.getText().toString().trim();
            String usernamePassword = etPassword.getText().toString().trim();
            int confirm_password_length = usernamePassword.length();

                    if(!usernameInput.isEmpty()
                            && !usernamePassword.isEmpty() && confirm_password_length== 4)
                    {
                        loginLink.setEnabled(true);
                        loginLink.setTextColor(activity.getResources().getColor(R.color.colorBlack));
                    }else{
                        loginLink.setEnabled(false);
                        loginLink.setTextColor(activity.getResources().getColor(R.color.colorCell));
                    }
            //loginLink.setTextColor(000);

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
